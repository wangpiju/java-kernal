package com.hs3.service.activity;

import com.hs3.commons.ActivityType;
import com.hs3.dao.activity.ActivityDao;
import com.hs3.dao.activity.ActivityFirstrechargeDao;
import com.hs3.dao.activity.ActivityUserDao;
import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.finance.RechargeDao;
import com.hs3.dao.user.UserDao;
import com.hs3.entity.activity.Activity;
import com.hs3.entity.activity.ActivityFirstrecharge;
import com.hs3.entity.activity.ActivityUser;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.users.User;
import com.hs3.models.Jsoner;
import com.hs3.models.activity.ActivityFirstrechargeModel;
import com.hs3.utils.DateUtils;
import com.hs3.utils.sys.WebDateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityFirstrechargeService")
public class ActivityFirstrechargeService
        extends ActivityBaseService {
    private static final String KEY = "firstrecharge";
    @Autowired
    private ActivityFirstrechargeDao activityFirstRechargeDao;
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

    public ActivityFirstrechargeModel findFull() {
        ActivityFirstrechargeModel m = this.activityDao.findToClass(ActivityType.firstrecharge.getType(), ActivityFirstrechargeModel.class);
        if (m != null) {
            m.setBonusList(this.activityFirstRechargeDao.list(null));
        }
        return m;
    }

    public void saveOrUpdate(ActivityFirstrechargeModel m) {
        if (m == null) {
            return;
        }
        this.activityDao.update(m);
        List<Object> amounts = new ArrayList<>();
        for (ActivityFirstrecharge rec : m.getBonusList()) {
            if (this.activityFirstRechargeDao.hasRecord(rec.getAmount())) {
                this.activityFirstRechargeDao.update(rec);
            } else {
                this.activityFirstRechargeDao.save(rec);
            }
            amounts.add(rec.getAmount());
        }
        this.activityFirstRechargeDao.deleteNotInAmount(amounts);
    }

    public int addBonusToUser(String account) {
        Activity activity = this.activityDao.find(ActivityType.firstrecharge.getType());
        if (activity == null) {
            return 300;
        }
        Integer amountType = activity.getAmountType();
        Date beginTime = null;
        Date endTime = null;

        Date curDate = new Date();
        if (amountType == 0) {
            beginTime = WebDateUtils.getBeginTime(curDate);
            endTime = WebDateUtils.getEndTime(curDate);
        } else {
            beginTime = activity.getBeginTime();
            endTime = activity.getEndTime();
        }
        if (activity.getBeginPrizeTime().getTime() > curDate.getTime()) {
            return 1;
        }
        if (activity.getEndPrizeTime().getTime() < curDate.getTime()) {
            return 2;
        }
        long beginReg = 0L;
        long endReg = 9223372036854775807L;
        if (activity.getBeginRegTime() != null) {
            beginReg = activity.getBeginRegTime().getTime();
        }
        if (activity.getEndRegTime() != null) {
            endReg = activity.getEndRegTime().getTime();
        }
        User u = this.userDao.findByAccount(account);
        long regTime = u.getRegTime().getTime();
        if ((regTime < beginReg) || (regTime > endReg)) {
            return 6;
        }
        ActivityUser ac = this.activityUserDao.hasAttendOrPrize(ActivityType.firstrecharge.getType(), DateUtils.format(beginTime), DateUtils.format(endTime), account);
        if (ac != null) {
            return 3;
        }
        BigDecimal maxAmount = this.rechargeDao.getFirstRecharge(account, beginTime, endTime);
        if (maxAmount == null) {
            return 4;
        }
        BigDecimal bonus = this.activityFirstRechargeDao.getBonus(maxAmount);
        if (bonus == null) {
            return 5;
        }
        if ((activity.getPrizeType() == 2) || (activity.getPrizeType() == 3)) {
            ActivityUser activityUser = new ActivityUser();
            activityUser.setAccount(account);
            activityUser.setCreateTime(new Date());
            activityUser.setValidTime(null);
            activityUser.setStatus(3);
            activityUser.setActivityId(ActivityType.firstrecharge.getType());
            activityUser.setOperator("");
            this.activityUserDao.save(activityUser);
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
            m.setFinanceId(ActivityType.firstrecharge.getType()+"");
            this.financeChangeDao.save(m);
            return 200;
        }
        ActivityUser activityUser = new ActivityUser();
        activityUser.setAccount(account);
        activityUser.setCreateTime(new Date());
        activityUser.setValidTime(null);
        activityUser.setStatus(1);
        activityUser.setActivityId(ActivityType.firstrecharge.getType());
        activityUser.setOperator("");
        this.activityUserDao.save(activityUser);
        return 300;
    }

    public Jsoner addBonus(String account, boolean isAgent, String item) {
        int rel = addBonusToUser(account);
        if (rel == 1) {
            return Jsoner.error("请君莫着急,活动未开始哦!");
        }
        if (rel == 2) {
            return Jsoner.error("活动已过期拉!下次记得快点哦");
        }
        if (rel == 3) {
            return Jsoner.error("您今日已领取过聚宝盆礼金，明日再来找我吧!");
        }
        if (rel == 4) {
            return Jsoner.error("您未充值，请充值后再操作。");
        }
        if (rel == 5) {
            return Jsoner.error("很遗憾，您今日的首笔存款未达到本宝盆的要求，明日加大存款再来领取呀");
        }
        if (rel == 6) {
            return Jsoner.error("很抱歉，您已不是“新手”玩家啦，您可移步参与其他活动哦~");
        }
        if (rel == 100) {
            return Jsoner.error("您当前状态不能申请奖金.");
        }
        if (rel == 200) {
            return Jsoner.success("聚宝盆彩金领取成功，完成充值金额3倍流水后即可提款哦~");
        }
        if (rel == 300) {
            return Jsoner.success("聚宝盆礼金申请成功,请等待工作人员审核");
        }
        return Jsoner.error("活动未找到");
    }

    public Jsoner add(String account, boolean isAgent) {
        return Jsoner.error("本活动无需参加，达到条件请直接领取");
    }

    public void setStatus(Activity a, String account, boolean isAgent) {
        a.setNeedAttend(0);
        a.setNeedPrize(0);
        if ((a.getActivityUser() == 0) || ((a.getActivityUser() == 1) && (!isAgent)) || ((a.getActivityUser() == 2) && (isAgent))) {
            a.setNeedPrize(1);
        }
    }
}
