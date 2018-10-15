package com.hs3.service.activity;

import com.hs3.commons.ActivityType;
import com.hs3.dao.activity.ActivityDao;
import com.hs3.dao.activity.ActivityUserDao;
import com.hs3.dao.bank.BankUserDao;
import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.user.UserDao;
import com.hs3.entity.activity.Activity;
import com.hs3.entity.activity.ActivityUser;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.users.User;
import com.hs3.models.Jsoner;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityRegisterService")
public class ActivityRegisterService
        extends ActivityBaseService {
    private static final String KEY = "register";
    @Autowired
    private ActivityUserDao activityUserDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BankUserDao bankUserDao;
    @Autowired
    private FinanceChangeDao financeChangeDao;

    public Jsoner add(String account, boolean isAgent) {
        return Jsoner.error("无须参加,达到条件直接申请即可");
    }

    public int addBonusToUser(String account, boolean isAgent) {
        User user = this.userDao.findByAccountMaster(account);

        Activity activity = this.activityDao.find(ActivityType.register.getType());
        if (activity == null) {
            return 300;
        }
        if ((activity.getActivityUser() == 1) && (isAgent)) {
            return 0;
        }
        if ((activity.getActivityUser() == 2) && (!isAgent)) {
            return 1;
        }
        ActivityUser ac = this.activityUserDao.findByActivityId(ActivityType.register.getType(), account);
        if (ac != null) {
            if (ac.getStatus() == 1) {
                return 2;
            }
            if (ac.getStatus() == 2) {
                return 3;
            }
            if (ac.getStatus() == 3) {
                return 4;
            }
        }
        if ((activity.getBeginTime().getTime() >= user.getRegTime().getTime()) || (activity.getEndTime().getTime() <= user.getRegTime().getTime())) {
            return 5;
        }
        if (this.bankUserDao.findBankCount(account) == 0) {
            return 6;
        }
        if ((activity.getPrizeType() == 0) || (activity.getPrizeType() == 1)) {
            if (ac == null) {
                ActivityUser act = new ActivityUser();
                act.setAccount(account);
                act.setActivityId(ActivityType.register.getType());
                act.setStatus(1);
                act.setOperator("");
                act.setCreateTime(new Date());
                act.setValidTime(null);
                this.activityUserDao.save(act);
            } else {
                ac.setStatus(1);
                ac.setCreateTime(new Date());
                this.activityUserDao.update(ac);
            }
        } else {
            if (ac == null) {
                ActivityUser act = new ActivityUser();
                act.setAccount(account);
                act.setActivityId(ActivityType.register.getType());
                act.setStatus(3);
                act.setOperator("");
                act.setCreateTime(new Date());
                act.setValidTime(null);
                this.activityUserDao.save(act);
            } else {
                ac.setStatus(3);
                ac.setCreateTime(new Date());
                this.activityUserDao.update(ac);
            }
            BigDecimal bonus = activity.getAmount();


            this.userDao.updateAmount(account, bonus);

            String remark = getRemark(activity);

            FinanceChange m = new FinanceChange();
            m.setAccountChangeTypeId(19);
            m.setTest(user.getTest());
            m.setBalance(this.userDao.getAmount(account));
            m.setChangeAmount(bonus);
            m.setChangeTime(new Date());
            m.setChangeUser(account);
            m.setRemark(remark);
            m.setStatus(2);
            m.setFinanceId(ActivityType.register.getType() + "");
            this.financeChangeDao.save(m);
        }
        return 200;
    }

    public Jsoner addBonus(String account, boolean isAgent, String item) {
        int rel = addBonusToUser(account, isAgent);
        if (rel == 0) {
            return Jsoner.error("改活动只针对会员，您不能参与。");
        }
        if (rel == 1) {
            return Jsoner.error("改活动只针对代理，您不能参与。");
        }
        if (rel == 2) {
            return Jsoner.error("您已申请奖金，请等待工作人员处理。");
        }
        if (rel == 3) {
            return Jsoner.error("您的申请正在处理，请稍等。");
        }
        if (rel == 4) {
            return Jsoner.error("您已领取奖金,不能重复领取。");
        }
        if (rel == 5) {
            return Jsoner.error("您注册时间不符合领奖条件");
        }
        if (rel == 6) {
            return Jsoner.error("您未绑定银行卡，请先绑定银行卡");
        }
        if (rel == 200) {
            return Jsoner.success("您的申请已受理。");
        }
        return Jsoner.error("错误代码:" + rel);
    }

    public void setStatus(Activity a, String account, boolean isAgent) {
        ActivityUser ac = this.activityUserDao.findByActivityId(ActivityType.register.getType(), account);
        a.setNeedPrize(0);
        a.setNeedAttend(0);
        if ((a.getPrizeType() != 0) && (a.getPrizeType() != 4) && (
                (ac == null) || (ac.getStatus() == 4))) {
            if ((a.getActivityUser() == 0) ||
                    ((a.getActivityUser() == 1) && (!isAgent)) || (
                    (a.getActivityUser() == 2) && (isAgent))) {
                a.setNeedPrize(1);
            }
        }
    }
}
