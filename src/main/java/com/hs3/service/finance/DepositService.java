package com.hs3.service.finance;

import com.hs3.dao.bank.BankNameDao;
import com.hs3.dao.bank.BankUserDao;
import com.hs3.dao.finance.DepositDao;
import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.finance.FinanceSettingDao;
import com.hs3.dao.lotts.AfcCalculationDao;
import com.hs3.dao.sys.SysConfigDao;
import com.hs3.dao.user.UserDao;
import com.hs3.dao.user.UserNoticeDao;
import com.hs3.db.Page;
import com.hs3.entity.bank.BankLevel;
import com.hs3.entity.bank.BankName;
import com.hs3.entity.bank.BankUser;
import com.hs3.entity.finance.Deposit;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.finance.FinanceSetting;
import com.hs3.entity.lotts.AfcCalculation;
import com.hs3.entity.sys.SysConfig;
import com.hs3.entity.users.User;
import com.hs3.entity.users.UserNotice;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("depositService")
public class DepositService {
    @Autowired
    private DepositDao depositDao;
    @Autowired
    private FinanceChangeDao financeChangeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BankUserDao bankUserDao;
    @Autowired
    private BankNameDao bankNameDao;
    @Autowired
    private FinanceSettingDao financeSettingDao;
    @Autowired
    private SysConfigDao sysConfigDao;

    @Autowired
    private UserNoticeDao userNoticeDao;
    @Autowired
    private AfcCalculationDao afcCalculationDao;

    @Autowired
    private RechargeService rechargeService;

    public void saveSplitAmount(String account, BigDecimal amount, BigDecimal maxDepositAmount, Integer bankCardId)
            throws BaseCheckException {
        User user = this.userDao.findByAccountMaster(account);
        if (user.getAmount().compareTo(amount) < 0) {
            throw new BaseCheckException("余额不够！");
        }

        int getUserDepositNSCount = getUserDepositNSCount(account);
        if(getUserDepositNSCount > 0){
            throw new BaseCheckException("您有未完结的提现申请单，暂时不能提交新的请求！");
        }

        String splitId = IdBuilder.getId("P", 3);
        do {
            BigDecimal splitAmount = amount;
            if (amount.compareTo(maxDepositAmount) > 0) {
                splitAmount = maxDepositAmount;
            }
            amount = amount.subtract(splitAmount);

            save(user, splitAmount, bankCardId, splitId);
        } while (amount.compareTo(BigDecimal.ZERO) > 0);
    }

    public void save(User user, BigDecimal amount, Integer bankCardId, String splitId)
            throws BaseCheckException {
        user = this.userDao.findByAccountMaster(user.getAccount());


        BankUser bankUser = (BankUser) this.bankUserDao.find(bankCardId);
        if (bankUser == null) {
            throw new BaseCheckException("银行卡[" + bankCardId + "]不存在！");
        }
        if (!bankUser.getAccount().equals(user.getAccount())) {
            throw new BaseCheckException("银行卡[" + bankCardId + "]没有绑定！");
        }
        checkUserCanDeposit(user, bankUser);
        if (user.getAmount().compareTo(amount) < 0) {
            throw new BaseCheckException("余额不够！");
        }


        HashMap<String, BigDecimal> userDepositAmount = getUserDepositAmount(user.getAccount());
        BigDecimal accountBalanceZ = userDepositAmount.get("accountBalanceZ"); //本次可提现帐变余额
        BigDecimal actualBalanceZ = userDepositAmount.get("actualBalanceZ");//本次可提现实际余额
        if (actualBalanceZ.compareTo(amount) < 0) {
            throw new BaseCheckException("可提现余额不够！");
        }


        BankName bankName = (BankName) this.bankNameDao.find(bankUser.getBankNameId());

        String id = IdBuilder.getId(((SysConfig) this.sysConfigDao.find("PROJECT_NAME")).getVal() + "T", 3);
        this.depositDao.save(id, amount, user.getAccount(), user.getTest(), user.getWithdrawalTimes().intValue(), user.getAdminRemark(), splitId, bankName.getCode(), bankName.getTitle(), bankUser.getCard(),
                bankUser.getAddress(), bankUser.getNiceName());


        FinanceChange financeChange = new FinanceChange();
        financeChange.setAccountChangeTypeId(Integer.valueOf(16));
        financeChange.setTest(user.getTest());
        financeChange.setBalance(user.getAmount().subtract(amount));
        financeChange.setChangeAmount(new BigDecimal("0").subtract(amount));
        financeChange.setChangeTime(new Date());
        financeChange.setChangeUser(user.getAccount());
        financeChange.setFinanceId(id);
        financeChange.setStatus(Integer.valueOf(0));
        this.financeChangeDao.save(financeChange);

        //新增提现计数数据
        AfcCalculation afcCalculation = new AfcCalculation();
        afcCalculation.setChangeUser(user.getAccount());
        afcCalculation.setTest(user.getTest());
        afcCalculation.setAfId(id);
        afcCalculation.setChangeType(1);
        afcCalculation.setStatus(0);
        afcCalculation.setAmount(user.getAmount());//余额
        afcCalculation.setCashableBalance(accountBalanceZ);//可提现的帐变余额
        afcCalculation.setChangeAmount(actualBalanceZ);//实际可提现余额
        afcCalculation.setRemainAmount(actualBalanceZ.subtract(amount));//剩余
        afcCalculationDao.save(afcCalculation);

        int i = this.userDao.updateAmount(user.getAccount(), financeChange.getChangeAmount()).intValue();
        if (i != 1) {
            throw new BaseCheckException("更新余额失败！");
        }
        BigDecimal balance = this.userDao.getAmount(user.getAccount());
        if (balance.compareTo(financeChange.getBalance()) != 0) {
            throw new BaseCheckException("提款操作失败！");
        }
    }



    public void saveSplitAmount_Z(String account, BigDecimal amount, BigDecimal maxDepositAmount, Integer bankCardId)
            throws BaseCheckException {

        User user = this.userDao.findByAccountMaster(account);
        if(amount.compareTo(user.getAmount()) > 0){
            throw new BaseCheckException("余额不足！");
        }
        saveSplitAmount_X(account, amount, maxDepositAmount, bankCardId);
    }

    public void saveSplitAmount_X(String account, BigDecimal amount, BigDecimal maxDepositAmount, Integer bankCardId)
            throws BaseCheckException {
        User user = this.userDao.findByAccountMaster(account);
        String splitId = IdBuilder.getId("P", 3);
        do {
            BigDecimal splitAmount = amount;
            if (amount.compareTo(maxDepositAmount) > 0) {
                splitAmount = maxDepositAmount;
            }
            amount = amount.subtract(splitAmount);

            save(user, splitAmount, bankCardId, splitId);
        } while (amount.compareTo(BigDecimal.ZERO) > 0);
    }


    public void save_Z(User user, BigDecimal amount, Integer bankCardId, String splitId)
            throws BaseCheckException {
        user = this.userDao.findByAccountMaster(user.getAccount());


        BankUser bankUser = (BankUser) this.bankUserDao.find(bankCardId);
        if (bankUser == null) {
            throw new BaseCheckException("银行卡[" + bankCardId + "]不存在！");
        }
        if (!bankUser.getAccount().equals(user.getAccount())) {
            throw new BaseCheckException("银行卡[" + bankCardId + "]没有绑定！");
        }
        checkUserCanDeposit(user, bankUser);

        BigDecimal u_amount = user.getAmount();

        if (u_amount.compareTo(amount) < 0) {
            throw new BaseCheckException("余额不够！");
        }
        BankName bankName = (BankName) this.bankNameDao.find(bankUser.getBankNameId());

        String id = IdBuilder.getId(((SysConfig) this.sysConfigDao.find("PROJECT_NAME")).getVal() + "T", 3);
        this.depositDao.save(id, amount, user.getAccount(), user.getTest(), user.getWithdrawalTimes().intValue(), user.getAdminRemark(), splitId, bankName.getCode(), bankName.getTitle(), bankUser.getCard(),
                bankUser.getAddress(), bankUser.getNiceName());

        int accountChangeTypeId = 16;

        FinanceChange financeChange = new FinanceChange();
        financeChange.setAccountChangeTypeId(Integer.valueOf(accountChangeTypeId));
        financeChange.setTest(user.getTest());
        financeChange.setBalance(user.getAmount().subtract(amount));
        financeChange.setChangeAmount(new BigDecimal("0").subtract(amount));
        financeChange.setChangeTime(new Date());
        financeChange.setChangeUser(user.getAccount());
        financeChange.setFinanceId(id);
        financeChange.setStatus(Integer.valueOf(0));
        this.financeChangeDao.save(financeChange);

        int i = this.userDao.updateAmount(user.getAccount(), financeChange.getChangeAmount()).intValue();

        if (i != 1) {
            throw new BaseCheckException("更新余额失败！");
        }
        BigDecimal balance = this.userDao.getAmount(user.getAccount());
        if (balance.compareTo(financeChange.getBalance()) != 0) {
            throw new BaseCheckException("提款操作失败！");
        }
    }

    public int update(Deposit deposit) {
        return this.depositDao.updateByIdAuto(deposit, deposit.getId());
    }

    public Deposit find(String id) {
        return (Deposit) this.depositDao.find(id);
    }

    public void updateByReject(Deposit deposit)
            throws BaseCheckException {
        doReject(deposit, Integer.valueOf(1));
    }

    public void updateBySuccess(Deposit deposit)
            throws BaseCheckException {
        Deposit m = findAndCheck(deposit);
        if (m.getStatus().intValue() != 5) {
            throw new BaseCheckException("当前状态不能处理完成！");
        }

        int depositCount = this.depositDao.countSplitSuccess(m.getAccount(), m.getSplitId()) == 0 ? 1 : 0;

        Integer accountChangeTypeId = 18;
        this.userDao.updateDeposit(m.getAccount(), new BigDecimal(0), m.getAmount(), Integer.valueOf(depositCount));

        this.financeChangeDao.updateByFinanceId(deposit.getId(), Integer.valueOf(2), deposit.getRemark(), Integer.valueOf(accountChangeTypeId));

        AfcCalculation afcCalculation = afcCalculationDao.findTopByAccount(m.getAccount(), deposit.getId());
        if(afcCalculation!=null) afcCalculationDao.updateInfo(afcCalculation.getId(), 2);

        doDispose(m.getId(), Integer.valueOf(2), null, deposit.getRemark(), deposit.getSerialNumber(), m.getOperator());

        //**************************************以下为变更部分*****************************************

        UserNotice userNotice = new UserNotice();
        userNotice.setAccount(m.getAccount());
        userNotice.setContent("恭喜您在" + DateUtils.format(m.getCreateTime()) + "提交的[" + m.getAmount() + "]元的提款申请已完成提款操作！");
        userNotice.setBetId("");
        userNotice.setWin(BigDecimal.ZERO);
        userNoticeDao.save(userNotice);

    }

    public void updateBySuccess(Deposit deposit, BigDecimal amount)
            throws BaseCheckException {
        updateByReject(deposit);

        amount = BigDecimal.ZERO.subtract(amount.abs());

        Integer accountChangeTypeId = 18;

        this.userDao.updateAmount(deposit.getAccount(), amount);
        BigDecimal balance = this.userDao.getAmount(deposit.getAccount());
        FinanceChange m = new FinanceChange();
        m.setChangeUser(deposit.getAccount());
        m.setChangeAmount(amount);
        m.setAccountChangeTypeId(Integer.valueOf(accountChangeTypeId));
        m.setBalance(balance);
        m.setChangeTime(new Date());
        m.setStatus(Integer.valueOf(2));
        m.setTest(deposit.getTest());
        m.setRemark("AUTO[" + deposit.getId() + "]");

        this.financeChangeDao.save(m);
    }

    public void updateByDoing(Deposit deposit) {
        Deposit m = findAndCheck(deposit, false);
        if (m.getStatus().intValue() != 7) {
            throw new BaseCheckException("未审核通过不允许处理！");
        }
        this.financeChangeDao.updateByFinanceId(deposit.getId(), Integer.valueOf(5), deposit.getRemark());

        AfcCalculation afcCalculation = afcCalculationDao.findTopByAccount(m.getAccount(), deposit.getId());
        if(afcCalculation!=null) afcCalculationDao.updateInfo(afcCalculation.getId(), 5);

        doDispose(m.getId(), Integer.valueOf(5), deposit.getOperator(), deposit.getRemark(), null, m.getLastOperator());
    }

    public void updateByAudit(Deposit deposit) {
        Deposit m = findAndCheck(deposit, false);
        if (m.getStatus().intValue() != 0) {
            throw new BaseCheckException("非未处理状态不允许审核！");
        }
        this.financeChangeDao.updateByFinanceId(deposit.getId(), Integer.valueOf(6), deposit.getRemark());

        AfcCalculation afcCalculation = afcCalculationDao.findTopByAccount(m.getAccount(), deposit.getId());
        if(afcCalculation!=null) afcCalculationDao.updateInfo(afcCalculation.getId(), 6);

        doDispose(m.getId(), Integer.valueOf(6), deposit.getOperator(), deposit.getRemark(), null, null);
    }

    public void updateByAuditSuccess(Deposit deposit) {
        Deposit m = findAndCheck(deposit);
        if (m.getStatus().intValue() != 6) {
            throw new BaseCheckException("非审核中状态不允许审核通过！");
        }
        this.financeChangeDao.updateByFinanceId(deposit.getId(), Integer.valueOf(7), deposit.getRemark());

        AfcCalculation afcCalculation = afcCalculationDao.findTopByAccount(m.getAccount(), deposit.getId());
        if(afcCalculation!=null) afcCalculationDao.updateInfo(afcCalculation.getId(), 7);

        doDispose(m.getId(), Integer.valueOf(7), null, deposit.getRemark(), null, m.getOperator());
    }

    public void updateByAuditReject(Deposit deposit) {
        doReject(deposit, Integer.valueOf(8));
    }

    private void doReject(Deposit deposit, Integer status) {
        Deposit m = findAndCheck(deposit);
        if (StrUtils.hasEmpty(new Object[]{deposit.getRemark()})) {
            throw new BaseCheckException("请填写备注！");
        }
        if ((status.intValue() != 1) && (status.intValue() != 8)) {
            throw new BaseCheckException("拒绝状态不正确！");
        }
        if ((m.getStatus().intValue() != 5) && (m.getStatus().intValue() != 6) && (m.getStatus().intValue() != 7)) {
            throw new BaseCheckException("该状态下不能拒绝操作！");
        }

        this.userDao.updateAmount(m.getAccount(), m.getAmount());
        User user = this.userDao.findByAccount(m.getAccount());
        FinanceChange financeChange = new FinanceChange();
        financeChange.setAccountChangeTypeId(Integer.valueOf(17));
        financeChange.setBalance(user.getAmount());
        financeChange.setChangeAmount(m.getAmount());
        financeChange.setChangeTime(new Date());
        financeChange.setChangeUser(m.getAccount());
        financeChange.setTest(user.getTest());
        financeChange.setFinanceId(m.getId());
        financeChange.setStatus(status);
        financeChange.setOperator(deposit.getOperator());
        financeChange.setRemark(deposit.getRemark());
        this.financeChangeDao.save(financeChange);

        this.financeChangeDao.updateByFinanceId(deposit.getId(), status, deposit.getRemark());

        AfcCalculation afcCalculation = afcCalculationDao.findTopByAccount(user.getAccount(), deposit.getId());
        if(afcCalculation!=null) afcCalculationDao.updateInfo(afcCalculation.getId(), status);

        doDispose(m.getId(), status, null, deposit.getRemark(), null, deposit.getOperator());
    }

    private Deposit findAndCheck(Deposit deposit, boolean checkOnlyOperator) {
        Deposit m = (Deposit) this.depositDao.findLock(deposit.getId());
        if (checkStatusHadDone(m.getStatus())) {
            throw new BaseCheckException("该记录已被处理！");
        }
        if ((checkOnlyOperator) &&
                (m.getOperator() != null) && (!m.getOperator().equals(deposit.getOperator()))) {
            throw new BaseCheckException("管理员[" + m.getOperator() + "]正在处理该订单，不允许操作！");
        }
        return m;
    }

    private Deposit findAndCheck(Deposit deposit) {
        return findAndCheck(deposit, true);
    }

    private boolean checkStatusHadDone(Integer status) {
        return (status.intValue() != 0) && (status.intValue() != 5) && (status.intValue() != 6) && (status.intValue() != 7) && (status.intValue() != 99);
    }

    private void doDispose(String id, Integer status, String operator, String remark, String serialNumber, String lastOperator) {
        int i = this.depositDao.setDispose(id, status, operator, remark, serialNumber, lastOperator);
        if (i <= 0) {
            throw new BaseCheckException("提款操作处理失败！");
        }
    }

    public List<Deposit> list(Page p) {
        return this.depositDao.list(p);
    }

    public List<Deposit> listByCond(Deposit deposit, Date startTime, Date endTime, BigDecimal minAmount, BigDecimal maxAmount, Integer[] statusArray, Page page) {
        return this.depositDao.listByCond(deposit, startTime, endTime, minAmount, maxAmount, statusArray, page);
    }

    public List<Deposit> listByCond(boolean isMaster, Deposit deposit, Date startTime, Date endTime, BigDecimal minAmount, BigDecimal maxAmount, Integer[] statusArray, Page page) {
        return this.depositDao.listByCond(isMaster, deposit, startTime, endTime, minAmount, maxAmount, statusArray, page);
    }


    public List<Map<String, Object>> listByCond_Z(boolean isMaster, Deposit deposit, Date startTime, Date endTime, BigDecimal minAmount, BigDecimal maxAmount, Integer[] statusArray, Page page) {

        List<Deposit> depositList = depositDao.listByCond(isMaster, deposit, startTime, endTime, minAmount, maxAmount, statusArray, page);

        List<Map<String, Object>> mapList = null;
        if (depositList.size() > 0) {
            mapList = new ArrayList<Map<String, Object>>();
            Map<String, Object> mapObj = null;
            for (Deposit depositObj : depositList) {
                mapObj = BeanZUtils.transBeanMap(depositObj);

                String account = depositObj.getAccount();
                User user = this.userDao.findByAccount(account);
                BankLevel bankLevel = rechargeService.findBankLevel_Z(user.getRegchargeAmount(), user.getRegchargeCount());
                mapObj.put("bankLevelTitle", bankLevel.getTitle());

                mapList.add(mapObj);
            }

        }

        return mapList;
    }


    public int updateStatus(String id, Integer status) {
        return this.depositDao.updateStatus(id, status);
    }

    public void checkUserCanDeposit(User user, BankUser bu, FinanceSetting financeSetting) {
        if ((user.getDepositStatus().intValue() == 2) || (user.getDepositStatus().intValue() == 3)) {
            throw new BaseCheckException("您的提现功能已被冻结！");
        }
        if ((user.getTest().intValue() == 1) &&
                (!financeSetting.isOpenTestUserDeposit())) {
            throw new BaseCheckException("测试用户不能提现！");
        }
        if (financeSetting.getDepositMinBindCardHours().intValue() > 0) {
            boolean bindCardDuring = false;
            Date now = new Date();
            int hours = financeSetting.getDepositMinBindCardHours().intValue();
            if (bu == null) {
                List<BankUser> bankUserList = this.bankUserDao.listByAccount(0, user.getAccount(), null, null, null, null, null);
                if (!bankUserList.isEmpty()) {
                    for (BankUser bankUser : bankUserList) {
                        if (DateUtils.addHour(bankUser.getCreateTime(), hours).getTime() < now.getTime()) {
                            bindCardDuring = true;
                            break;
                        }
                    }
                } else {
                    bindCardDuring = true;
                }
            } else {
                bindCardDuring = DateUtils.addHour(bu.getCreateTime(), hours).getTime() < now.getTime();
            }
            if (!bindCardDuring) {
                throw new BaseCheckException("绑卡未超过" + hours + "小时，不允许提现！");
            }
        }
    }

    public void checkUserCanDeposit(User user, BankUser bu) {
        checkUserCanDeposit(user, bu, (FinanceSetting) this.financeSettingDao.list(null).get(0));
    }

    public void updateByHandUp(Deposit deposit) {
        Deposit m = findAndCheck(deposit);
        if (m.getStatus().intValue() != 6) {
            throw new BaseCheckException("[" + deposit.getId() + "]该状态下不能提款挂起操作！");
        }
        this.depositDao.updateStatus(m.getId(), Integer.valueOf(99));
    }

    public void updateByHandDown(Deposit deposit) {
        Deposit m = findAndCheck(deposit);
        if (m.getStatus().intValue() != 99) {
            throw new BaseCheckException("[" + deposit.getId() + "]该状态下不能挂起解除操作！");
        }
        this.depositDao.updateStatus(m.getId(), Integer.valueOf(6));
    }

    public List<Deposit> listByHome(String account, boolean isIncludeChild, String startTime, String endTime, Page p) {
        return this.depositDao.listByHome(account, isIncludeChild, startTime, endTime, p);
    }

    public List<Deposit> listRecord(String account, Integer start, Integer limit) {
        return this.depositDao.listRecord(account, start, limit);
    }

    public Deposit findToDoing() {
        return this.depositDao.findToDoing();
    }


    public HashMap<String, BigDecimal> getUserDepositAmount(String account){

        if (account == null || account.trim().equals("")) {
            throw new BaseCheckException("账户不能为空！");
        }

        User user = this.userDao.findByAccount(account);

        HashMap<String, BigDecimal> userDepositAmount = new HashMap<String, BigDecimal>();
        BigDecimal amount = user.getAmount(); //用户余额
        BigDecimal accountBalanceZ = BigDecimal.ZERO; //本次可提现帐变余额
        BigDecimal actualBalanceZ = BigDecimal.ZERO; //本次可提现实际余额
        //BigDecimal lastRemainAmountZ = BigDecimal.ZERO; //上次提现后的可提现余额

        List<Map<String, Object>> cwbrReport = userDao.cashWithdrawalBalanceReport(account, null);
        if(cwbrReport!=null && cwbrReport.size()>0){
            Map<String, Object> cwbrUser = cwbrReport.get(0);

            BigDecimal basisAvailable = BigDecimal.ZERO; //基础可提余额
            //BigDecimal betDeduction = BigDecimal.ZERO; //投注扣款
            //BigDecimal bonusDelivery = BigDecimal.ZERO; //奖金派送
            //BigDecimal nwRunAmount = BigDecimal.ZERO; //未、已中奖投注金额
            BigDecimal noWinRunAmount = BigDecimal.ZERO; //未中奖流水
            BigDecimal winRunAmount = BigDecimal.ZERO; //中奖流水
            BigDecimal subordinateRebate = BigDecimal.ZERO; //下级投注返点
            BigDecimal dailyWage = BigDecimal.ZERO; //日工资
            BigDecimal cycleDividend = BigDecimal.ZERO; //周期分红
            BigDecimal adminDispatch = BigDecimal.ZERO; //管理员派发金额
            BigDecimal lastRemainAmount = BigDecimal.ZERO; //上次提现后的可提现余额
            BigDecimal sysWithdrawalPoint = BigDecimal.ZERO; //系统撤单返点金额
            BigDecimal cancelRunAward = BigDecimal.ZERO; //撤销派奖流水
            //BigDecimal cancelAward = BigDecimal.ZERO; //撤销派奖金额
            BigDecimal activityDispatch = BigDecimal.ZERO; //活动派发

            //betDeduction = MathUtils.getBigDecimal(cwbrUser.get("betDeduction"));
            //bonusDelivery = MathUtils.getBigDecimal(cwbrUser.get("bonusDelivery"));
            //nwRunAmount = MathUtils.getBigDecimal(cwbrUser.get("nwRunAmount"));
            noWinRunAmount = MathUtils.getBigDecimal(cwbrUser.get("noWinRunAmount"));
            winRunAmount = MathUtils.getBigDecimal(cwbrUser.get("winRunAmount"));
            subordinateRebate = MathUtils.getBigDecimal(cwbrUser.get("subordinateRebate"));
            dailyWage = MathUtils.getBigDecimal(cwbrUser.get("dailyWage"));
            cycleDividend = MathUtils.getBigDecimal(cwbrUser.get("cycleDividend"));
            adminDispatch = MathUtils.getBigDecimal(cwbrUser.get("adminDispatch"));
            lastRemainAmount = MathUtils.getBigDecimal(cwbrUser.get("lastRemainAmount"));
            sysWithdrawalPoint = MathUtils.getBigDecimal(cwbrUser.get("sysWithdrawalPoint"));
            cancelRunAward = MathUtils.getBigDecimal(cwbrUser.get("cancelRunAward"));
            //cancelAward = MathUtils.getBigDecimal(cwbrUser.get("cancelAward"));
            activityDispatch = MathUtils.getBigDecimal(cwbrUser.get("activityDispatch"));

            //lastRemainAmount = lastRemainAmount.subtract(nwRunAmount);
            //int lastRemainAmountFlag = lastRemainAmount.compareTo(BigDecimal.ZERO);
            //lastRemainAmountZ = lastRemainAmountFlag < 0 ? BigDecimal.ZERO : lastRemainAmount;

            basisAvailable = noWinRunAmount.add(winRunAmount);

            accountBalanceZ = basisAvailable.add(subordinateRebate).add(dailyWage).add(cycleDividend).add(adminDispatch).add(lastRemainAmount).add(sysWithdrawalPoint).add(cancelRunAward).add(activityDispatch);

            int balFlag = accountBalanceZ.compareTo(amount);
            actualBalanceZ = balFlag < 0 ? accountBalanceZ : amount;

        }

        userDepositAmount.put("amount",amount);//余额
        userDepositAmount.put("accountBalanceZ",accountBalanceZ);//帐变余额
        userDepositAmount.put("actualBalanceZ",actualBalanceZ);//实际可提现余额
        //userDepositAmount.put("lastRemainAmountZ",lastRemainAmountZ);//上次提现后的可提现余额
        return userDepositAmount;
    }

    public int getUserDepositNSCount(String account){
        int getUserDepositNSCount = depositDao.getUserDepositNSCount(account);
        return getUserDepositNSCount;
    }



}
