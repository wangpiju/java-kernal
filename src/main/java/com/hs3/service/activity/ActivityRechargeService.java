package com.hs3.service.activity;

import com.hs3.commons.ActivityType;
import com.hs3.dao.activity.ActivityDao;
import com.hs3.dao.activity.ActivityRechargeDao;
import com.hs3.dao.activity.ActivityUserDao;
import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.finance.RechargeDao;
import com.hs3.dao.user.UserDao;
import com.hs3.entity.activity.Activity;
import com.hs3.entity.activity.ActivityRecharge;
import com.hs3.entity.activity.ActivityUser;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.users.User;
import com.hs3.models.Jsoner;
import com.hs3.models.activity.ActivityRechargeModel;
import com.hs3.utils.DateUtils;
import com.hs3.utils.sys.WebDateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityRechargeService")
public class ActivityRechargeService
        extends ActivityBaseService {
    private static final String KEY = "recharge";
    @Autowired
    private ActivityRechargeDao activityRechargeDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityUserDao activityUserDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RechargeDao rechargeDao;
    @Autowired
    private FinanceChangeDao financeChangeDao;

    public ActivityRechargeModel findFull() {
        ActivityRechargeModel m = this.activityDao.findToClass(ActivityType.recharge.getType(), ActivityRechargeModel.class);
        if (m != null) {
            m.setBonusList(this.activityRechargeDao.list(null));
        }
        return m;
    }

    public void saveOrUpdate(ActivityRechargeModel m) {
        if (m == null) {
            return;
        }
        this.activityDao.update(m);
        List<Object> amounts = new ArrayList<>();
        for (ActivityRecharge rec : m.getBonusList()) {
            if (this.activityRechargeDao.hasRecord(rec.getAmount())) {
                this.activityRechargeDao.update(rec);
            } else {
                this.activityRechargeDao.save(rec);
            }
            amounts.add(rec.getAmount());
        }
        this.activityRechargeDao.deleteNotInAmount(amounts);
    }

    public int addBonusToUser(String account) {
        Activity activity = this.activityDao.find(ActivityType.recharge.getType());
        if (activity == null) {
            return 300;
        }
        Integer amountType = activity.getAmountType();
        Date beginTime = null;
        Date endTime = null;
        if (amountType == 0) {
            beginTime = WebDateUtils.getBeginTime(new Date());
            endTime = WebDateUtils.getEndTime(new Date());
        } else {
            beginTime = activity.getBeginTime();
            endTime = activity.getEndTime();
        }
        ActivityUser ac = this.activityUserDao.hasAttendOrPrize(ActivityType.recharge.getType(), DateUtils.format(beginTime), DateUtils.format(endTime), account);
        if (ac == null) {
            return 0;
        }
        if (ac.getStatus() == 1) {
            return 1;
        }
        if (ac.getStatus() == 2) {
            return 2;
        }
        if (ac.getStatus() == 3) {
            return 3;
        }
        BigDecimal maxAmount = this.rechargeDao.getMaxRecharge(account, ac.getCreateTime(), endTime);
        if (maxAmount == null) {
            return 4;
        }
        BigDecimal bonus = this.activityRechargeDao.getBonus(maxAmount);
        if (bonus == null) {
            return 5;
        }
        if ((activity.getPrizeType() == 2) || (activity.getPrizeType() == 3)) {
            if (this.activityUserDao.setStatus(ac.getId(), 0, 3, "") != 1) {
                return 100;
            }
            if (this.userDao.updateAmount(account, bonus) != 1) {
                return 100;
            }
            String remark = getRemark(activity);

            FinanceChange m = new FinanceChange();
            m.setAccountChangeTypeId(19);
            m.setTest(this.userDao.findByAccount(account).getTest());
            m.setBalance(this.userDao.getAmount(account));
            m.setChangeAmount(bonus);
            m.setChangeTime(new Date());
            m.setChangeUser(account);
            m.setRemark(remark);
            m.setStatus(2);
            m.setFinanceId(ActivityType.recharge.getType() + "");
            this.financeChangeDao.save(m);
            return 200;
        }
        if (this.activityUserDao.setStatus(ac.getId(), 4, 1, "") != 1) {
            return 100;
        }
        return 300;
    }

    public Jsoner addBonus(String account, boolean isAgent, String item) {
        int rel = addBonusToUser(account);
        if (rel == 0) {
            return Jsoner.error("您还没有参加活动，不能领取彩金。");
        }
        if (rel == 1) {
            return Jsoner.error("您已申请奖金，请等待工作人员处理。");
        }
        if (rel == 2) {
            return Jsoner.error("您的申请正在处理，请稍等。");
        }
        if (rel == 3) {
            return Jsoner.error("您已领取奖金，不能重复领取。");
        }
        if (rel == 4) {
            return Jsoner.error("您未充值，请充值后再操作。");
        }
        if (rel == 5) {
            return Jsoner.error("您的充值未符合要求额度，请达到充值要求后再操作。");
        }
        if (rel == 100) {
            return Jsoner.error("您当前状态不能申请奖金.");
        }
        if (rel == 200) {
            return Jsoner.success("彩金领取成功");
        }
        if (rel == 300) {
            return Jsoner.success("彩金申请成功,请等待工作人员审核");
        }
        return Jsoner.error("活动未找到");
    }

    public Jsoner add(String account, boolean isAgent) {
        Activity activity = this.activityDao.find(ActivityType.recharge.getType());
        if (activity == null) {
            return Jsoner.error("活动未找到");
        }
        Integer amountType = activity.getAmountType();
        Date beginTime = null;
        Date endTime = null;
        if (amountType == 0) {
            beginTime = WebDateUtils.getBeginTime(new Date());
            endTime = WebDateUtils.getEndTime(new Date());
        } else {
            beginTime = activity.getBeginTime();
            endTime = activity.getEndTime();
        }
        ActivityUser m = this.activityUserDao.hasAttendOrPrize(ActivityType.recharge.getType(), DateUtils.format(beginTime), DateUtils.format(endTime), account);
        if (m != null) {
            return Jsoner.error("您已参加该活动,不能重复参加");
        }
        if ((activity.getActivityUser() == 1) && (isAgent)) {
            return Jsoner.error("该活动只针对会员");
        }
        if ((activity.getActivityUser() == 2) && (!isAgent)) {
            return Jsoner.error("该活动只针对代理");
        }
        ActivityUser act = new ActivityUser();
        act.setAccount(account);
        act.setActivityId(ActivityType.recharge.getType());
        act.setStatus(0);
        act.setOperator("");
        act.setCreateTime(new Date());
        act.setValidTime(new Date());
        this.activityUserDao.save(act);
        return Jsoner.success();
    }

    public void setStatus(Activity a, String account, boolean isAgent) {
        Integer amountType = a.getAmountType();
        Date beginTime = null;
        Date endTime = null;
        if (amountType == 0) {
            beginTime = WebDateUtils.getBeginTime(new Date());
            endTime = WebDateUtils.getEndTime(new Date());
        } else {
            beginTime = a.getBeginTime();
            endTime = a.getEndTime();
        }
        ActivityUser m = this.activityUserDao.hasAttendOrPrize(a.getId(), DateUtils.format(beginTime), DateUtils.format(endTime), account);


        a.setNeedAttend(0);
        a.setNeedPrize(0);
        if ((a.getPrizeType() != 0) && (a.getPrizeType() != 4)) {
            if (m == null) {
                if ((a.getActivityUser() == 0) || ((a.getActivityUser() == 1) && (!isAgent)) || ((a.getActivityUser() == 2) && (isAgent))) {
                    a.setNeedAttend(1);
                }
            } else if (((m.getStatus() == 0) || (m.getStatus() == 4)) && (
                    (a.getActivityUser() == 0) || ((a.getActivityUser() == 1) && (!isAgent)) || ((a.getActivityUser() == 2) && (isAgent)))) {
                a.setNeedPrize(1);
            }
        }
    }
}
