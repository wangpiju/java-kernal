package com.hs3.service.finance;

import com.hs3.dao.approval.ApprovalDao;
import com.hs3.dao.bank.*;
import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.finance.FinanceSettingDao;
import com.hs3.dao.finance.RechargeDao;
import com.hs3.dao.finance.RechargeLowerDao;
import com.hs3.dao.sys.SysConfigDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.approval.Approval;
import com.hs3.entity.bank.*;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.finance.FinanceSetting;
import com.hs3.entity.finance.Recharge;
import com.hs3.entity.finance.RechargeLower;
import com.hs3.entity.sys.SysConfig;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.exceptions.UnLogException;
import com.hs3.utils.DateUtils;
import com.hs3.utils.IdBuilder;
import com.hs3.utils.StrUtils;
import com.pays.PayApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("rechargeService")
public class RechargeService {
    @Autowired
    private RechargeDao rechargeDao;
    @Autowired
    private RechargeLowerDao rechargeLowerDao;
    @Autowired
    private BankNameDao bankNameDao;
    @Autowired
    private BankLevelDao bankLevelDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BankGroupDao bankGroupDao;
    @Autowired
    private BankSysDao bankSysDao;
    @Autowired
    private FinanceChangeDao financeChangeDao;
    @Autowired
    private BankApiDao bankApiDao;
    @Autowired
    private FinanceSettingDao financeSettingDao;
    @Autowired
    private ApprovalDao approvalDao;
    @Autowired
    private SysConfigDao sysConfigDao;
    @Autowired
    private RechargeWayDao rechargeWayDao;

    public Recharge saveByBank(User user, BigDecimal money, Integer bankNamdId) throws BaseCheckException {
        checkUserCanRecharge(user);
        checkUnRecharge(user.getAccount());
        BankName bankName = findBankName(bankNamdId, money);
        BankSys bankSys = findBankSys(bankName.getId(), user);

        return save(bankName, bankSys, null, null, null, money, user, 0, null);
    }

    public Recharge saveByPay(User user, BigDecimal money, String card, String niceName, Integer bankNamdId, Integer bankApiId, PayApi payApi, boolean isMobile, String classKey)
            throws BaseCheckException {
        checkUserCanRecharge(user);
        checkUnRecharge(user.getAccount());
        //第三方的銀行名
        String bankNameKey = payApi.getBank(bankNamdId.toString(), isMobile);
        BankName bankName = new BankName();

        bankName.setTitle(bankNameKey);
        bankName.setCode(bankNamdId.toString());
        boolean isAli = false;
        boolean isQQ = false;

        if (bankNameKey.contains("支付宝")) {
            isAli = true;
        } else if (bankNameKey.contains("QQ")) {
            isQQ = true;
        }

        List<RechargeWay> rechargeWays = this.rechargeWayDao.listRechargeWay();
        RechargeWay rechargeWayResult = null;

        for (RechargeWay rechargeWay : rechargeWays) {
            if (rechargeWay.getAlino().equals("alimobile") && isAli) {
                rechargeWayResult = rechargeWay;
                break;
            } else if (rechargeWay.getAlino().equals("qq") && isQQ) {
                rechargeWayResult = rechargeWay;
                break;
            }
        }

        if (null == rechargeWayResult) {
            for (RechargeWay rechargeWay : rechargeWays) {
                if (rechargeWay.getAlino().equals("online") && isAli) {
                    rechargeWayResult = rechargeWay;
                    break;
                }

            }
        }


        if (money.compareTo(BigDecimal.valueOf(rechargeWayResult.getMinmoney())) < 0) {
            throw new BaseCheckException("充值金额[" + money + "]小于" + rechargeWayResult.getAlias() + "充值最低限额[" + rechargeWayResult.getMinmoney() + "]");
        }

        if (money.compareTo(BigDecimal.valueOf(rechargeWayResult.getMaxmoney())) > 0) {
            throw new BaseCheckException("充值金额[" + money + "]大于" + rechargeWayResult.getAlias() + "充值最高限额[" + rechargeWayResult.getMaxmoney() + "]");
        }

        BankApi bankApi = findBankApi(bankApiId, money, bankName, user, payApi);
        return save(bankName, null, card, niceName, bankApi, money, user, 1, classKey);
    }

    public RechargeLower saveByLower(User sourceUser, User targetUser, BigDecimal amount, int rechargeStyle, String remark)
            throws BaseCheckException {
        targetUser = this.userDao.findByAccountMaster(targetUser.getAccount());
        sourceUser = this.userDao.findByAccountMaster(sourceUser.getAccount());

        int accountChangeTypeId = 15;

        if (rechargeStyle == 1) {
            accountChangeTypeId = 28;
            remark = targetUser.getAccount();
        } else if (rechargeStyle == 2) {
            accountChangeTypeId = 29;
            remark = targetUser.getAccount();
        } else if (rechargeStyle == 3) {
            accountChangeTypeId = 30;
            remark = targetUser.getAccount();
        }

        if (sourceUser.getAccountRecharge() == 0) {
            throw new BaseCheckException("您尚未开通会员转账功能！");
        }

        if (!targetUser.getParentAccount().equals(sourceUser.getAccount())) {
            throw new BaseCheckException("没有找到该下级用户！");
        }

        if (sourceUser.getAmount().compareTo(amount) < 0) {
            throw new BaseCheckException("余额不足！");
        }

        FinanceSetting financeSetting = (FinanceSetting) this.financeSettingDao.list(null).get(0);

        if (targetUser.getRechargeLowerTarAmount().add(amount).compareTo(financeSetting.getRechargeLowerMaxMoney()) > 0) {
            throw new BaseCheckException("该用户已达上限！");
        }

        if (DateUtils.addHour(targetUser.getRegTime(), financeSetting.getRechargeLowerHours()).after(new Date())) {
            throw new BaseCheckException("该用户注册未满" + financeSetting.getRechargeLowerHours() + "小时，不能被充值，请知悉。");
        }

        this.userDao.updateRechargeLower(targetUser.getAccount(), BigDecimal.ZERO, amount);
        this.userDao.updateRechargeLower(sourceUser.getAccount(), amount, BigDecimal.ZERO);

        Date now = new Date();
        RechargeLower m = new RechargeLower();
        m.setId(IdBuilder.getId("L", 3));
        m.setStatus(Integer.valueOf(0));
        m.setAmount(amount);
        m.setSourceAccount(sourceUser.getAccount());
        m.setTargetAccount(targetUser.getAccount());
        m.setCreateTime(now);
        m.setLastTime(now);
        m.setTest(sourceUser.getTest());
        m.setRemark(remark);

        this.rechargeLowerDao.save(m);
        BigDecimal changeAmount = BigDecimal.ZERO.subtract(amount);
        this.userDao.updateAmount(sourceUser.getAccount(), changeAmount);

        BigDecimal balance = this.userDao.getAmount(sourceUser.getAccount());
        FinanceChange financeChange = new FinanceChange();
        financeChange.setAccountChangeTypeId(Integer.valueOf(accountChangeTypeId));
        financeChange.setTest(sourceUser.getTest());
        financeChange.setBalance(balance);
        financeChange.setChangeAmount(changeAmount);
        financeChange.setChangeTime(now);
        financeChange.setChangeUser(sourceUser.getAccount());
        financeChange.setFinanceId(m.getId());
        financeChange.setStatus(m.getStatus());
        financeChange.setRemark(remark);
        this.financeChangeDao.save(financeChange);

        if (amount.compareTo(financeSetting.getRechargeLowerNotAudit()) <= 0) {
            RechargeLower rechargeLower = new RechargeLower();
            rechargeLower.setId(m.getId());
            rechargeLower.setOperator("");
            rechargeLower.setRemark(remark);
            updateBySuccess(rechargeLower);
        }

        return m;
    }

    public void updateBySuccess(RechargeLower rechargeLower) {
        RechargeLower m =  this.rechargeLowerDao.findLock(rechargeLower.getId());

        if (m.getStatus() != 0) {
            throw new UnLogException("该记录已被处理！");
        }

        Date now = new Date();
        m.setStatus(Integer.valueOf(2));
        m.setLastTime(now);
        m.setOperator(rechargeLower.getOperator());
        m.setRemark(rechargeLower.getRemark());
        this.rechargeLowerDao.update(m);
        this.userDao.updateAmount(m.getTargetAccount(), m.getAmount());
        this.userDao.updateRechargeLowerTotal(m.getTargetAccount(), BigDecimal.ZERO, 0, m.getAmount(), 1);
        this.userDao.updateRechargeLowerTotal(m.getSourceAccount(), m.getAmount(), 1, BigDecimal.ZERO, 0);

        FinanceChange cond = new FinanceChange();
        cond.setFinanceId(m.getId());
        List<FinanceChange> fcList = this.financeChangeDao.listByCond(cond, null, null, false, null, null);
        int accountChangeTypeId = fcList.isEmpty() ? 14 : ((FinanceChange) fcList.get(0)).getAccountChangeTypeId();

        if (accountChangeTypeId == 15) {
            accountChangeTypeId = 14;
        }

        this.financeChangeDao.updateByFinanceId(m.getId(), m.getStatus(), m.getRemark());

        String remark = (StrUtils.hasEmpty(new Object[]{m.getOperator()})) && ((accountChangeTypeId == 28) || (accountChangeTypeId == 29) || (accountChangeTypeId == 30)) ? null : m.getRemark();
        User user = this.userDao.findByAccount(m.getTargetAccount());
        FinanceChange financeChange = new FinanceChange();
        financeChange.setAccountChangeTypeId(Integer.valueOf(accountChangeTypeId));
        financeChange.setBalance(user.getAmount());
        financeChange.setChangeAmount(m.getAmount());
        financeChange.setChangeTime(now);
        financeChange.setChangeUser(m.getTargetAccount());
        financeChange.setTest(user.getTest());
        financeChange.setFinanceId(m.getId());
        financeChange.setStatus(m.getStatus());
        financeChange.setOperator(m.getOperator());
        financeChange.setRemark(remark);

        this.financeChangeDao.save(financeChange);
    }

    public void updateByReject(RechargeLower rechargeLower) {
        RechargeLower m =  this.rechargeLowerDao.findLock(rechargeLower.getId());

        if (m.getStatus() != 0) {
            throw new UnLogException("该记录已被处理！");
        }

        if (StrUtils.hasEmpty(new Object[]{rechargeLower.getRemark()})) {
            throw new BaseCheckException("请填写备注！");
        }

        Date now = new Date();
        m.setStatus(Integer.valueOf(1));
        m.setLastTime(now);
        m.setOperator(rechargeLower.getOperator());
        m.setRemark(rechargeLower.getRemark());

        this.rechargeLowerDao.update(m);
        this.userDao.updateAmount(m.getSourceAccount(), m.getAmount());

        FinanceChange cond = new FinanceChange();
        cond.setFinanceId(m.getId());
        List<FinanceChange> fcList = this.financeChangeDao.listByCond(cond, null, null, false, null, null);
        int accountChangeTypeId = fcList.isEmpty() ? 26 : ((FinanceChange) fcList.get(0)).getAccountChangeTypeId();

        if (accountChangeTypeId == 15) {
            accountChangeTypeId = 26;
        }

        User user = this.userDao.findByAccount(m.getSourceAccount());
        FinanceChange financeChange = new FinanceChange();
        financeChange.setAccountChangeTypeId(Integer.valueOf(accountChangeTypeId));
        financeChange.setBalance(user.getAmount());
        financeChange.setChangeAmount(m.getAmount());
        financeChange.setChangeTime(now);
        financeChange.setChangeUser(m.getSourceAccount());
        financeChange.setTest(user.getTest());
        financeChange.setFinanceId(m.getId());
        financeChange.setStatus(m.getStatus());
        financeChange.setOperator(m.getOperator());
        this.financeChangeDao.save(financeChange);
        this.financeChangeDao.updateByFinanceId(m.getId(), m.getStatus(), m.getRemark());
    }

    private String genTraceId() {
        String traceId = IdBuilder.createRom(3, 2);
        Date today = DateUtils.toDateNull(DateUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
        Date ysday = DateUtils.addDay(today, -1);
        while (this.rechargeDao.countTraceId(traceId, today, ysday) > 0) {
            traceId = IdBuilder.createRom(3, 2);
        }
        return traceId;
    }

    private BankApi findBankApi(Integer bankApiId, BigDecimal money, BankName bankName, User user, PayApi payApi) {
        BankApi bankApi = (BankApi) this.bankApiDao.find(bankApiId);
        if (bankApi == null) {
            throw new BaseCheckException("没有匹配到第三方充值类型！");
        }
        if (bankApi.getStatus() != 0) {
            throw new BaseCheckException("该充值方式不能使用！");
        }
        if (money.compareTo(bankApi.getMinAmount()) < 0) {
            throw new BaseCheckException("充值金额[" + money + "]小于" + bankApi.getTitle() + "充值最低限额[" + bankApi.getMinAmount() + "]");
        }
        if (money.compareTo(bankApi.getMaxAmount()) > 0) {
            throw new BaseCheckException("充值金额[" + money + "]大于" + bankApi.getTitle() + "充值最高限额[" + bankApi.getMaxAmount() + "]");
        }
        boolean checkBankApi = false;
        List<BankApi> bankApiList = listBankApi(user.getAccount(), user.getRegchargeAmount(), user.getRegchargeCount());
        for (BankApi ba : bankApiList) {
            if (ba.getId() == bankApiId) {
                checkBankApi = true;
                break;
            }
        }
        if (!checkBankApi) {
            throw new BaseCheckException("累计额度没有达到充值方式[" + bankApi.getTitle() + "]的最低限制！");
        }
//        if (!payApi.getBankCodes(bankApi.isCredit()).contains(bankName.getCode().toUpperCase())) {
//            Set<String> banks = payApi.getBankCodes(bankApi.isCredit());
//            String bankCode = bankName.getCode();
//            throw new BaseCheckException("[" + bankApi.getTitle() + "]充值方式不支持银行[" + bankName.getTitle() + "]！");
//        }
        return bankApi;
    }

    private void checkUnRecharge(String account) throws BaseCheckException {
        //現在充值不檔, 可以連續送
//        if (this.rechargeDao.countUserStatus(account, Integer.valueOf(0)) > 0) {
//            throw new BaseCheckException("已有未充值的记录，不能进行充值！");
//        }
    }

    private BankName findBankName(Integer bankNamdId, BigDecimal money) throws BaseCheckException {
        BankName bankName = (BankName) this.bankNameDao.find(bankNamdId);

        if (money.compareTo(bankName.getMinAmount()) < 0) {
            throw new BaseCheckException("充值金额[" + money + "]小于" + bankName.getTitle() + "充值最低限额[" + bankName.getMinAmount() + "]");
        }

        if (money.compareTo(bankName.getMaxAmount()) > 0) {
            throw new BaseCheckException("充值金额[" + money + "]大于" + bankName.getTitle() + "充值最高限额[" + bankName.getMaxAmount() + "]");
        }

        return bankName;
    }

    private Recharge save(BankName bankName, BankSys bankSys, String card, String niceName, BankApi bankApi, BigDecimal money, User user, int rechargeType, String classKey) {
        Recharge recharge = new Recharge();
        BigDecimal poundage = BigDecimal.ZERO;
        recharge.setId(IdBuilder.getId(((SysConfig) this.sysConfigDao.find("PROJECT_NAME")).getVal() + "R", 3));
        recharge.setTraceId(genTraceId());
        recharge.setStatus(Integer.valueOf(0));
        recharge.setAmount(money);
        recharge.setCard(card);
        recharge.setNiceName(niceName);
        recharge.setRechargeType(Integer.valueOf(rechargeType));
        recharge.setAccount(user.getAccount());
        recharge.setTest(user.getTest());
        recharge.setCreateTime(new Date());
        recharge.setBankName(bankName.getTitle());
        recharge.setBankNameCode(bankName.getCode());
        recharge.setReceiveLink(bankName.getLink());
        recharge.setClassKey(classKey);

        if (bankSys != null) {

            if (bankName.getId() != bankSys.getNameId()) {
                bankName = (BankName) this.bankNameDao.find(bankSys.getNameId());
            }

            recharge.setReceiveBankName(bankName.getTitle());
            recharge.setReceiveBankId(bankSys.getId());
            recharge.setReceiveCard(bankSys.getCard());
            recharge.setReceiveAddress(bankSys.getAddress());
            recharge.setReceiveNiceName(bankSys.getNiceName());
            recharge.setStatus(Integer.valueOf(99));
        }

        if (bankApi != null) {
            recharge.setReceiveBankId(bankApi.getId());
            recharge.setReceiveBankName(bankApi.getTitle());
            recharge.setReceiveCard(bankApi.getMerchantCode());

            if ((bankApi.getPoundage() != null) && (bankApi.getPoundage().compareTo(BigDecimal.ZERO) > 0)) {
                poundage = recharge.getAmount().multiply(bankApi.getPoundage()).divide(new BigDecimal("100"), 4);
                if (poundage.compareTo(new BigDecimal("0.1")) < 0) {
                    poundage = new BigDecimal("0.1");
                }
            }

            if ("alipay2bank".equals(bankApi.getClassKey())) {
                recharge.setRechargeType(Integer.valueOf(0));
                recharge.setStatus(Integer.valueOf(99));
                recharge.setReceiveAddress(bankApi.getSign());
                recharge.setReceiveNiceName(bankApi.getEmail());
            }
        }

        recharge.setPoundage(poundage);
        recharge.setRealAmount(recharge.getAmount().subtract(poundage));
        save(recharge);
        return recharge;
    }

    public void save(Recharge recharge) throws BaseCheckException {
        this.rechargeDao.save(recharge);
    }

    public List<Recharge> listUnProcess(Date date){
        return this.rechargeDao.listUnProcess(date);
    }

    public Recharge find(String id) {
        return (Recharge) this.rechargeDao.find(id);
    }

    public Recharge updateByExpire(String id, int expireTime) {
        Recharge recharge =  this.rechargeDao.find(id);
        updateExpireStatus(recharge, expireTime);
        return recharge;
    }

    public void updateByUserExpire(String account, int expireTime) {
        List<Recharge> list = this.rechargeDao.listUserStatus(account, Integer.valueOf(0));
        for (Recharge recharge : list) {
            updateExpireStatus(recharge, expireTime);
        }
    }

    public int updateStatus(String id, Integer status) {
        return this.rechargeDao.updateStatus(id, status);
    }

    public List<Recharge> listUserStatus(String account, Integer status) {
        return this.rechargeDao.listUserStatus(account, status);
    }

    private void updateExpireStatus(Recharge recharge, int expireTime) {
        if ((recharge != null) && (recharge.getStatus() == 0) &&
                (new Date().after(DateUtils.addMinute(recharge.getCreateTime(), expireTime)))) {
            this.rechargeDao.updateStatus(recharge.getId(), Integer.valueOf(3));
        }
    }

    public void updateByAudit(Recharge recharge) {
        Recharge m = findAndCheck(recharge);
        if (m.getStatus() == 6) {
            throw new BaseCheckException("该记录已被添加到后台审核！");
        }
        doDispose(m.getId(), Integer.valueOf(6), recharge.getOperator(), recharge.getRemark(), null);

        User user = this.userDao.findByAccount(m.getAccount());
        Approval approval = new Approval();
        approval.setAmount(m.getAmount());
        approval.setApplyType(Integer.valueOf(99));
        approval.setApplyContent("手工补单[" + m.getId() + "][" + m.getTraceId() + "]");
        approval.setOperator(null);
        approval.setRemark(null);
        approval.setUserName(user.getAccount());
        approval.setUserIdentify(user.getUserMark());
        approval.setCreateTime(new Date());
        approval.setStatus(Integer.valueOf(0));
        approval.setRechargeId(m.getId());
        approval.setReceiveCard(m.getReceiveCard());
        this.approvalDao.save(approval);
    }

    public void updateByAuditSuccess(Approval m) {
        Approval a =  this.approvalDao.findLock(m.getId());

        if (a.getStatus() != 0) {
            throw new BaseCheckException("该记录已被处理！");
        }

        Recharge recharge = new Recharge();
        recharge.setId(a.getRechargeId());
        recharge.setOperator(m.getOperator());
        updateBySuccess(recharge, null);

        this.approvalDao.update(m);
    }

    public void updateByAuditReject(Approval m) {
        Approval a =  this.approvalDao.findLock(m.getId());
        if (a.getStatus() != 0) {
            throw new BaseCheckException("该记录已被处理！");
        }
        Recharge recharge =  this.rechargeDao.findLock(a.getRechargeId());
        if (recharge.getStatus() == 6) {
            doDispose(a.getRechargeId(), Integer.valueOf(0), m.getOperator(), m.getRemark(), null);
        }
        this.approvalDao.update(m);
    }

    public void updateByReject(Recharge recharge) throws BaseCheckException {
        Recharge m = findAndCheck(recharge);

        if (StrUtils.hasEmpty(new Object[]{recharge.getRemark()})) {
            throw new BaseCheckException("请填写备注！");
        }

        doDispose(m.getId(), Integer.valueOf(1), recharge.getOperator(), recharge.getRemark(), null);
    }

    public void updateBySuccess(Recharge recharge, BigDecimal rechargePoundage, BigDecimal amount)
            throws BaseCheckException {
        this.updateBySuccess(recharge, rechargePoundage, amount, "");
    }

    public void updateBySuccess(Recharge recharge, BigDecimal rechargePoundage, BigDecimal amount, String operate)
            throws BaseCheckException {
        recharge = this.rechargeDao.find(recharge.getId());
        recharge.setOperator(operate);
        if ((amount == null) || (recharge.getAmount().compareTo(amount) == 0)) {
            updateBySuccess(recharge, rechargePoundage);
            return;
        }
        recharge.setRemark("AUTO REJECT");
        updateByReject(recharge);


        recharge.setId(IdBuilder.getId("R", 3));
        recharge.setAmount(amount);
        recharge.setTraceId(genTraceId());
        Date now = new Date();
        recharge.setCreateDate(now);
        recharge.setCreateTime(now);
        recharge.setStatus(Integer.valueOf(0));
        recharge.setRemark("AUTO SUCCESS");
        save(recharge);
        updateBySuccess(recharge, rechargePoundage);
    }

    public void updateBySuccess(Recharge recharge, BigDecimal rechargePoundage)
            throws BaseCheckException {
        Recharge m = findAndCheck(recharge);

        if ((rechargePoundage != null) && (BigDecimal.ZERO.compareTo(rechargePoundage) >= 0)) {
            throw new BaseCheckException("充值手续费必须大于0！");
        }

        this.userDao.updateRecharge(m.getAccount(), m.getRealAmount(), m.getAmount(), 1);

        User user = this.userDao.findByAccount(m.getAccount());
        FinanceChange financeChange = new FinanceChange();
        financeChange.setAccountChangeTypeId(m.getRechargeType() == 1 ? 11 : m.getRechargeType() == 0 ? 12 : 12);
        financeChange.setBalance(user.getAmount());
        financeChange.setChangeAmount(m.getRealAmount());
        financeChange.setChangeTime(new Date());
        financeChange.setChangeUser(m.getAccount());
        financeChange.setTest(user.getTest());
        financeChange.setFinanceId(m.getId());
        financeChange.setStatus(2);
        financeChange.setOperator(recharge.getOperator());
        financeChange.setRemark(recharge.getRemark());
        this.financeChangeDao.save(financeChange);

        if (rechargePoundage != null) {
            this.userDao.updateRecharge(m.getAccount(), rechargePoundage, BigDecimal.ZERO, 0);
            BigDecimal balance = this.userDao.getAmount(m.getAccount());

            financeChange.setAccountChangeTypeId(13);
            financeChange.setChangeAmount(rechargePoundage);
            financeChange.setBalance(balance);
            this.financeChangeDao.save(financeChange);
        }

        if (m.getRechargeType() == 0) {
            this.bankSysDao.updateRecharge(m.getReceiveBankId(), m.getAmount(), 1);
        } else if (m.getRechargeType() == 1) {
            this.bankApiDao.updateRecharge(m.getReceiveBankId(), m.getAmount(), 1);
        }

        doDispose(recharge.getId(), 2, recharge.getOperator(), recharge.getRemark(), recharge.getSerialNumber());
    }

    private Recharge findAndCheck(Recharge recharge) {
        Recharge m =  this.rechargeDao.findLock(recharge.getId());
        if (m == null) {
            throw new BaseCheckException("记录不存在！");
        }
        if ((m.getStatus() != 0) && (m.getStatus() != 3) && (m.getStatus() != 4) && (m.getStatus() != 6) && (m.getStatus() != 99) && (m.getStatus() != 20)) {
            throw new UnLogException("该记录已被处理！");
        }
        if (recharge.getOperator() == null) {
            throw new BaseCheckException("操作人不能为空！");
        }
        return m;
    }

    private Recharge findAndCheck_v2(Recharge recharge) {
        Recharge m =  this.rechargeDao.findLock(recharge.getId());
        if (m == null) {
            throw new BaseCheckException("记录不存在！");
        }
        if ((m.getStatus() != 0) && (m.getStatus() != 1) && (m.getStatus() != 3) && (m.getStatus() != 4) && (m.getStatus() != 6) && (m.getStatus() != 99) && (m.getStatus() != 20)) {
            throw new UnLogException("该记录已被处理！");
        }
        if (recharge.getOperator() == null) {
            throw new BaseCheckException("操作人不能为空！");
        }
        return m;
    }

    private void doDispose(String id, Integer status, String operator, String remark, String serialNumber) {
        int i = this.rechargeDao.setDispose(id, status, operator, remark, serialNumber);
        if (i <= 0) {
            throw new BaseCheckException("充值操作处理失败！");
        }
    }

    public List<Recharge> list(Page p) {
        return this.rechargeDao.list(p);
    }

    public List<Recharge> listByCond(Recharge recharge, int operatorType, Date startTime, Date endTime, Integer[] statusArray, Page page) {
        return this.rechargeDao.listByCond(recharge, operatorType, startTime, endTime, statusArray, page);
    }

    public List<Recharge> listByCond(boolean isMaster, Recharge recharge, int operatorType, Date startTime, Date endTime, Integer[] statusArray, Page page) {
        return this.rechargeDao.listByCond(isMaster, recharge, operatorType, startTime, endTime, statusArray, page);
    }

    public void checkUserCanRecharge(User user) {
        if (user.getTest() == 1) {
            FinanceSetting financeSetting = this.financeSettingDao.list(null).get(0);

            if (!financeSetting.isOpenTestUserRecharge()) {
                throw new BaseCheckException("测试用户不能充值！");
            }
        }

        if ((user.getDepositStatus() == 1) || (user.getDepositStatus() == 3)) {
            throw new BaseCheckException("您的充值功能已被冻结！");
        }
    }

    public List<BankApi> listBankApi(String account, BigDecimal rechargeAmount, int regchargeCount) {
        BankLevel bankLevel = findBankLevel(rechargeAmount, regchargeCount);
        return this.bankApiDao.listByAccount(account, bankLevel.getId());
    }

    public Recharge findByTodayTraceId(String traceId, Date day) {
        return this.rechargeDao.findByTodayTraceId(traceId, day);
    }

    private BankLevel findBankLevel(BigDecimal rechargeAmount, int regchargeCount) {
        List<BankLevel> bankLevelList = this.bankLevelDao.listAllOrderByMinAmount();
        return (BankLevel) bankLevelList.get(findBankLevel(bankLevelList, rechargeAmount, regchargeCount));
    }

    public BankLevel findBankLevel_Z(BigDecimal rechargeAmount, int regchargeCount) {
        return findBankLevel(rechargeAmount, regchargeCount);
    }

    private int findBankLevel(List<BankLevel> list, BigDecimal rechargeAmount, int regchargeCount) {
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            BankLevel bankLevel = list.get(i);
            if ((rechargeAmount.compareTo(bankLevel.getMinAmount()) >= 0) && (regchargeCount >= bankLevel.getMinCount())) {
                index = i;
            }
        }

        if (index < 0) {
            throw new BaseCheckException("不能确定用户的银行等级");
        }

        return index;
    }

    private BankSys findBankSys(int bankNameId, User user) throws BaseCheckException {
        List<BankLevel> bankLevelList = this.bankLevelDao.listAllOrderByMinAmount();
        int bankLevelIndex = findBankLevel(bankLevelList, user.getRegchargeAmount(), user.getRegchargeCount());
        BankGroup bankGroup = this.bankGroupDao.findByName(this.userDao.findByAccount(user.getRootAccount()).getBankGroup());
        List<BankSys> bankSysList = this.bankSysDao.listByIds(this.bankGroupDao.listBankIds(bankGroup.getId()));
        int level = bankLevelIndex;

        BankSys bankSys;
        bankSys = findSameBank(bankSysList, bankNameId, ((BankLevel) bankLevelList.get(level)).getId());

        if (bankSys == null) {
            bankSys = findSameCorssBank(bankSysList, ((BankLevel) bankLevelList.get(level)).getId());
        }

        if (bankSys == null) {
            throw new BaseCheckException("没有找到符合条件的银行卡信息");
        }

        return bankSys;
    }

    private BankSys findSameBank(List<BankSys> bankSysList, int bankNameId, int bankLevelId) {
        for (BankSys bankSys : bankSysList) {
            if ((bankSys.getNameId() == bankNameId) && (bankSys.getLevelId() == bankLevelId)) {
                return bankSys;
            }
        }
        return null;
    }

    private BankSys findSameCorssBank(List<BankSys> bankSysList, int bankLevelId) {
        for (BankSys bankSys : bankSysList) {
            if ((bankSys.getCrossStatus() == 1) && (bankSys.getLevelId() == bankLevelId)) {
                return bankSys;
            }
        }
        return null;
    }

    public List<Recharge> listByHome(String account, boolean isIncludeChild, String startTime, String endTime, Page p) {
        return this.rechargeDao.listByHome(account, isIncludeChild, startTime, endTime, p);
    }

    public int updateInfo(String account, String id, String card, String niceName) {
        if (StrUtils.hasEmpty(new Object[]{id, card, niceName})) {
            throw new UnLogException("请填写完整信息");
        }
        if (!StrUtils.isNumber(card, 4, 4)) {
            throw new UnLogException("请输入存款卡,后4位号码");
        }
        if (!StrUtils.isName(niceName, 2, 15)) {
            throw new UnLogException("请认真填写存款卡的开户名");
        }
        Recharge recharge = (Recharge) this.rechargeDao.findLock(id);
        if ((recharge == null) || (!recharge.getAccount().equals(account))) {
            throw new UnLogException("未找到您的充值订单号：" + id);
        }
        if ((recharge.getStatus() != 99) && (recharge.getStatus() != 3)) {
            throw new UnLogException("您的充值订单号已被处理");
        }
        return this.rechargeDao.updateInfo(id, card, niceName);
    }

    //**************************************以下为变更部分*****************************************


    public Recharge saveByBank_Z(User user, BigDecimal money, Integer bankNamdId, String card, String niceName)
            throws BaseCheckException {
        checkUserCanRecharge(user);


        //checkUnRecharge(user.getAccount());


        BankName bankName = findBankName(bankNamdId, money);


        BankSys bankSys = findBankSys(bankName.getId(), user);

        return save_Z(bankName, bankSys, card, niceName, null, money, user, 0);
    }


    private Recharge save_Z(BankName bankName, BankSys bankSys, String card, String niceName, BankApi bankApi, BigDecimal money, User user, int rechargeType) {
        Recharge recharge = new Recharge();
        BigDecimal poundage = BigDecimal.ZERO;
        recharge.setId(IdBuilder.getId(((SysConfig) this.sysConfigDao.find("PROJECT_NAME")).getVal() + "R", 3));
        recharge.setTraceId(genTraceId());
        recharge.setStatus(Integer.valueOf(0));
        recharge.setAmount(money);
        recharge.setCard(card);
        recharge.setNiceName(niceName);
        recharge.setRechargeType(Integer.valueOf(rechargeType));
        recharge.setAccount(user.getAccount());
        recharge.setTest(user.getTest());
        recharge.setCreateTime(new Date());

        recharge.setBankName(bankName.getTitle());
        recharge.setBankNameCode(bankName.getCode());
        recharge.setReceiveLink(bankName.getLink());

        //设置校验码
        int emailCodeInt = (int) ((Math.random() * 9 + 1) * 100000);
        String code = String.valueOf(emailCodeInt);//随机6位数字

        String chars = "abcdefghijklmnopqrstuvwxyz";
        String strCode = String.valueOf(chars.charAt((int) (Math.random() * 26))) + String.valueOf(chars.charAt((int) (Math.random() * 26))) + String.valueOf(chars.charAt((int) (Math.random() * 26)));//随机3位字母

        String checkCode = strCode + code;

        recharge.setCheckCode(checkCode);


        if (bankSys != null) {
            if (bankName.getId() != bankSys.getNameId()) {
                bankName = (BankName) this.bankNameDao.find(bankSys.getNameId());
            }
            recharge.setReceiveBankName(bankName.getTitle());
            recharge.setReceiveBankId(bankSys.getId());
            recharge.setReceiveCard(bankSys.getCard());
            recharge.setReceiveAddress(bankSys.getAddress());
            recharge.setReceiveNiceName(bankSys.getNiceName());
            //recharge.setStatus(Integer.valueOf(99));
        }

        //第三方支付设置Start------------------------------------------------
        if (bankApi != null) {
            recharge.setReceiveBankId(bankApi.getId());
            recharge.setReceiveBankName(bankApi.getTitle());
            recharge.setReceiveCard(bankApi.getMerchantCode());
            if ((bankApi.getPoundage() != null) && (bankApi.getPoundage().compareTo(BigDecimal.ZERO) > 0)) {
                poundage = recharge.getAmount().multiply(bankApi.getPoundage()).divide(new BigDecimal("100"), 4);
                if (poundage.compareTo(new BigDecimal("0.1")) < 0) {
                    poundage = new BigDecimal("0.1");
                }
            }
            if ("alipay2bank".equals(bankApi.getClassKey())) {
                recharge.setRechargeType(Integer.valueOf(0));
                recharge.setStatus(Integer.valueOf(99));
                recharge.setReceiveAddress(bankApi.getSign());
                recharge.setReceiveNiceName(bankApi.getEmail());
            }
        }
        //第三方支付设置End------------------------------------------------

        recharge.setPoundage(poundage);
        recharge.setRealAmount(recharge.getAmount().subtract(poundage));
        save_Z(recharge);
        return recharge;
    }

    public void save_Z(Recharge recharge) throws BaseCheckException {
        this.rechargeDao.save_Z(recharge);
    }

    public List<Recharge> listByCond_Z(boolean isMaster, Recharge recharge, int operatorType, Date startTime, Date endTime, Integer[] statusArray, Page page) {
        return this.rechargeDao.listByCond_Z(isMaster, recharge, operatorType, startTime, endTime, statusArray, page);
    }


    public BankSys findBankSys_Z(Integer rechargeWay, User user) throws BaseCheckException {
        List<BankLevel> bankLevelList = this.bankLevelDao.listAllOrderByMinAmount();

        int bankLevelIndex = findBankLevel(bankLevelList, user.getRegchargeAmount(), user.getRegchargeCount());

        BankGroup bankGroup = this.bankGroupDao.findByName(this.userDao.findByAccount(user.getRootAccount()).getBankGroup());

        List<BankSys> bankSysList = this.bankSysDao.listByIds(this.bankGroupDao.listBankIds(bankGroup.getId()));

        int level = bankLevelIndex;

        BankSys bankSys = null;

        RechargeWay rechargeWayObj = rechargeWayDao.find(rechargeWay);
        Integer waytype = rechargeWayObj.getWaytype();//渠道类型 0：其他、1：扫码、2：看卡、3：线上
        Integer bnId = rechargeWayObj.getBnId();//银行对照 该字段只在扫码类型下需要选择，意义在于匹配现有的等级体系，扫码均是寻找同行卡

        if(waytype != null && waytype != 1 && waytype != 2){
            throw new BaseCheckException("只有扫码或看卡的渠道才需要匹配系统卡！");
        }

        if(waytype == 1){
            if(bnId == null || bnId == 0){
                throw new BaseCheckException("扫码渠道必须先匹配银行对照！");
            }
            bankSys = findSameBank(bankSysList, bnId, ((BankLevel) bankLevelList.get(level)).getId());
        } else if (waytype == 2){
            bankSys = findSameCorssBank(bankSysList, ((BankLevel) bankLevelList.get(level)).getId());
        }

        if (bankSys == null) {
            throw new BaseCheckException("没有找到符合条件的银行卡信息");
        }
        return bankSys;
    }


    public Recharge saveByBank_ZZ(User user, BigDecimal money, Integer receiveBankId, String card, String niceName, String checkCode)
            throws BaseCheckException {
        checkUserCanRecharge(user);

        BankSys bankSys = bankSysDao.find(receiveBankId);

        BankName bankName = findBankName(bankSys.getNameId(), money);

        return save_ZZ(bankName, bankSys, card, niceName, null, money, user, 0, checkCode);
    }


    private Recharge save_ZZ(BankName bankName, BankSys bankSys, String card, String niceName, BankApi bankApi, BigDecimal money, User user, int rechargeType, String checkCode) {
        Recharge recharge = new Recharge();
        BigDecimal poundage = BigDecimal.ZERO;
        recharge.setId(IdBuilder.getId(((SysConfig) this.sysConfigDao.find("PROJECT_NAME")).getVal() + "R", 3));
        recharge.setTraceId(genTraceId());
        recharge.setStatus(Integer.valueOf(0));
        recharge.setAmount(money);
        recharge.setCard(card);
        recharge.setNiceName(niceName);
        recharge.setRechargeType(Integer.valueOf(rechargeType));
        recharge.setAccount(user.getAccount());
        recharge.setTest(user.getTest());
        recharge.setCreateTime(new Date());

        recharge.setBankName(bankName.getTitle());
        recharge.setBankNameCode(bankName.getCode());
        recharge.setReceiveLink(bankName.getLink());

        recharge.setCheckCode(checkCode);


        if (bankSys != null) {
            if (bankName.getId() != bankSys.getNameId()) {
                bankName = (BankName) this.bankNameDao.find(bankSys.getNameId());
            }
            recharge.setReceiveBankName(bankName.getTitle());
            recharge.setReceiveBankId(bankSys.getId());
            recharge.setReceiveCard(bankSys.getCard());
            recharge.setReceiveAddress(bankSys.getAddress());
            recharge.setReceiveNiceName(bankSys.getNiceName());
            //recharge.setStatus(Integer.valueOf(99));
        }

        //第三方支付设置Start------------------------------------------------
        if (bankApi != null) {
            recharge.setReceiveBankId(bankApi.getId());
            recharge.setReceiveBankName(bankApi.getTitle());
            recharge.setReceiveCard(bankApi.getMerchantCode());
            if ((bankApi.getPoundage() != null) && (bankApi.getPoundage().compareTo(BigDecimal.ZERO) > 0)) {
                poundage = recharge.getAmount().multiply(bankApi.getPoundage()).divide(new BigDecimal("100"), 4);
                if (poundage.compareTo(new BigDecimal("0.1")) < 0) {
                    poundage = new BigDecimal("0.1");
                }
            }
            if ("alipay2bank".equals(bankApi.getClassKey())) {
                recharge.setRechargeType(Integer.valueOf(0));
                recharge.setStatus(Integer.valueOf(99));
                recharge.setReceiveAddress(bankApi.getSign());
                recharge.setReceiveNiceName(bankApi.getEmail());
            }
        }
        //第三方支付设置End------------------------------------------------

        recharge.setPoundage(poundage);
        recharge.setRealAmount(recharge.getAmount().subtract(poundage));
        save_Z(recharge);
        return recharge;
    }


    public void updateByReject_Z(Recharge recharge, Integer status)
            throws BaseCheckException {
        Recharge m = findAndCheck_v2(recharge);
        if (StrUtils.hasEmpty(new Object[]{recharge.getRemark()})) {
            throw new BaseCheckException("请填写备注！");
        }
        doDispose(m.getId(), status, recharge.getOperator(), recharge.getRemark(), null);
    }


}
