package com.hs3.service.finance;

import com.hs3.dao.approval.ApprovalDao;
import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.finance.RechargeDao;
import com.hs3.dao.lotts.AccountChangeTypeDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.approval.Approval;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.finance.Recharge;
import com.hs3.entity.lotts.AccountChangeType;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.exceptions.UnLogException;
import com.hs3.utils.IdBuilder;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("financeChangeService")
public class FinanceChangeService {
    @Autowired
    private FinanceChangeDao financeChangeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountChangeTypeDao accountChangeTypeDao;
    @Autowired
    private ApprovalDao approvalDao;
    @Autowired
    private RechargeDao rechargeDao;

    public List<FinanceChange> list(Page p) {
        return this.financeChangeDao.list(p);
    }

    public FinanceChange find(Integer id) {
        return (FinanceChange) this.financeChangeDao.find(id);
    }

    public void save(FinanceChange m) {
        this.financeChangeDao.save(m);
    }

    public void saveByOperator(FinanceChange m, Approval approval)
            throws BaseCheckException {
        if (StrUtils.hasEmpty(new Object[]{m.getChangeUser(), m.getAccountChangeTypeId(), m.getChangeAmount()})) {
            throw new BaseCheckException("参数不正确！");
        }
        BigDecimal poundage = BigDecimal.ZERO;
        BigDecimal realAmount = m.getChangeAmount();

        User user = this.userDao.findByAccountMaster(m.getChangeUser());
        if (user == null) {
            throw new BaseCheckException("[" + m.getChangeUser() + "]不存在！");
        }
        AccountChangeType act = (AccountChangeType) this.accountChangeTypeDao.find(m.getAccountChangeTypeId());
        if (act == null) {
            throw new BaseCheckException("选择帐变类型不存在！");
        }
        if (act.getId() == 23 || act.getId() == 37 || act.getId() == 38) {
            if(m.getChangeAmount().abs().compareTo(user.getAmount()) > 0){
                throw new BaseCheckException("使用者余额不足");
            }
            realAmount = BigDecimal.ZERO.subtract(m.getChangeAmount().abs());
        } else if (act.getId() == 21) {
            if (!StrUtils.hasEmpty(new Object[]{approval.getPassword()})) {
                poundage = new BigDecimal(approval.getPassword());
            }
            realAmount = m.getChangeAmount().subtract(poundage);
        }
        if ((act.getId() != 19) && (act.getId() != 23) && (act.getId() != 37) && (act.getId() != 38)) {
            this.userDao.updateRecharge(user.getAccount(), realAmount, m.getChangeAmount(), Integer.valueOf(1));
        } else {
            this.userDao.updateAmount(user.getAccount(), realAmount);
        }
        BigDecimal balance = this.userDao.getAmount(user.getAccount());
        m.setBalance(balance);
        m.setChangeTime(new Date());
        m.setStatus(Integer.valueOf(2));
        m.setTest(user.getTest());

        this.financeChangeDao.save(m);
        if (act.getId() == 21) {
            Recharge recharge = new Recharge();
            recharge.setId(IdBuilder.getId("R", 3));
            recharge.setTraceId(IdBuilder.createRom(3, 2));
            recharge.setStatus(Integer.valueOf(2));
            recharge.setAmount(m.getChangeAmount());
            recharge.setRechargeType(Integer.valueOf(2));
            recharge.setAccount(user.getAccount());
            recharge.setTest(user.getTest());
            recharge.setCreateTime(approval.getCreateTime());
            recharge.setLastTime(approval.getHandleTime());
            recharge.setOperator(approval.getOperator());
            recharge.setCreateDate(approval.getCreateTime());
            recharge.setBankName("现金充值");
            recharge.setReceiveBankId(Integer.valueOf(0));
            recharge.setReceiveCard(approval.getReceiveCard());
            recharge.setPoundage(poundage);
            recharge.setRealAmount(realAmount);

            this.rechargeDao.save(recharge);
        }
    }

    public void saveToApproval(FinanceChange m, BigDecimal poundage, String receiveCard)
            throws BaseCheckException {
        if (StrUtils.hasEmpty(new Object[]{m.getChangeUser(), m.getAccountChangeTypeId(), m.getChangeAmount()})) {
            throw new BaseCheckException("参数不正确！");
        }
        User user = this.userDao.findByAccount(m.getChangeUser());
        if (user == null) {
            throw new BaseCheckException("[" + m.getChangeUser() + "]不存在！");
        }
        AccountChangeType act = (AccountChangeType) this.accountChangeTypeDao.find(m.getAccountChangeTypeId());
        if (act == null) {
            throw new BaseCheckException("选择帐变类型不存在！");
        }

        //限制只有测试账户才可进行平台充值
        Integer test = user.getTest();
        if(test==0 && act.getId() == 24){
            throw new BaseCheckException("只有测试账户才可进行平台充值！");
        }

        //如果是扣款行为先检查余额是否足够
        if(act.getId() == 23 || act.getId() == 37 || act.getId() == 38){
            if(m.getChangeAmount().compareTo(user.getAmount()) > 0){
                throw new BaseCheckException("使用者余额不足");
            }
        }

        if (act.getId() == 21) {
            /*if (StrUtils.hasEmpty(new Object[]{poundage, receiveCard})) {
                throw new UnLogException("现金充值请填写手续费和收款银行卡号");
            }*/
            if (StrUtils.hasEmpty(new Object[]{poundage})) {
                poundage = BigDecimal.ZERO;
            }
            if (receiveCard == null) {
                receiveCard = "";
            }
        }
        Approval approval = new Approval();
        approval.setAmount(m.getChangeAmount());
        approval.setApplyType(m.getAccountChangeTypeId());
        approval.setApplyContent(act.getName());
        approval.setOperator(m.getOperator());
        approval.setRemark(m.getRemark());
        approval.setUserName(m.getChangeUser());
        approval.setUserIdentify(user.getUserMark());
        approval.setCreateTime(new Date());
        approval.setStatus(Integer.valueOf(0));
        approval.setRechargeId(IdBuilder.getId("A", 3));
        approval.setReceiveCard(receiveCard);
        if (act.getId() == 21) {
            if (poundage.compareTo(BigDecimal.ZERO) < 0) {
                throw new UnLogException("手续费不能小于0");
            }
            approval.setReceiveCard(receiveCard);
            approval.setPassword(poundage.toString());
        }
        this.approvalDao.save(approval);
    }

    public void saveByApproval(Approval m) {
        Approval a = (Approval) this.approvalDao.findLock(m.getId());
        if (a.getStatus().intValue() != 0) {
            throw new BaseCheckException("该记录已被处理！");
        }
        FinanceChange financeChange = new FinanceChange();
        financeChange.setChangeUser(m.getUserName());
        financeChange.setAccountChangeTypeId(m.getApplyType());
        financeChange.setChangeAmount(m.getAmount());
        financeChange.setOperator(m.getOperator());
        financeChange.setRemark(m.getRemark());
        financeChange.setFinanceId(m.getRechargeId());
        saveByOperator(financeChange, m);

        this.approvalDao.update(m);
    }

    public List<FinanceChange> listByCond(FinanceChange financeChange, Date startTime, Date endTime, boolean isIncludeChild, String[] accountChangeTypes, Page page) {
        return this.financeChangeDao.listByCond(financeChange, startTime, endTime, isIncludeChild, accountChangeTypes, page);
    }

    public List<FinanceChange> listByCond(String account, int start, int limit) {
        return this.financeChangeDao.listByCond(account, start, limit);
    }

    public boolean isExistFinanceChange(Date changeDate, String key) {
        return this.financeChangeDao.getCountFinanceChange(changeDate, key);
    }
}
