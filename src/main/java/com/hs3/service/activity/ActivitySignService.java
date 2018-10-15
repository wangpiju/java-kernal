package com.hs3.service.activity;

import com.hs3.commons.ActivityType;
import com.hs3.dao.activity.ActivityDao;
import com.hs3.dao.activity.ActivitySignDao;
import com.hs3.dao.activity.ActivityUserDao;
import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.lotts.BetDao;
import com.hs3.dao.user.UserDao;
import com.hs3.entity.activity.Activity;
import com.hs3.entity.activity.ActivitySign;
import com.hs3.entity.activity.ActivityUser;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.users.User;
import com.hs3.models.Jsoner;
import com.hs3.models.activity.ActivitySignForIndex;
import com.hs3.models.activity.ActivitySignModel;
import com.hs3.utils.DateUtils;
import com.hs3.utils.sys.WebDateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activitySignService")
public class ActivitySignService
        extends ActivityBaseService {
    private static final String KEY = "sign";
    @Autowired
    private ActivitySignDao activitySignDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityUserDao activityUserDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FinanceChangeDao financeChangeDao;
    @Autowired
    private BetDao betDao;

    public void save(ActivitySign m) {
        this.activitySignDao.save(m);
    }

    public int delete(Integer activityId) {
        return this.activitySignDao.deleteByActivityId(activityId);
    }

    public List<ActivitySign> findEntityByActivityId(Integer activityId) {
        return this.activitySignDao.findEntityByActivityId(activityId);
    }

    public ActivitySignModel findFull() {
        ActivitySignModel m = this.activityDao.findToClass(ActivityType.sign.getType(), ActivitySignModel.class);
        if (m == null) {
            return m;
        }
        List<ActivitySign> list = this.activitySignDao.findEntityByActivityId(ActivityType.sign.getType());

        m.setDaysList(list);
        return m;
    }

    public void saveOrUpdate(ActivitySignModel m) {
        if (m == null) {
            return;
        }
        m.setUseRange(0);
        m.setNeedAttend(0);
        m.setNeedPrize(0);
        this.activityDao.update(m);
        this.activitySignDao.deleteByActivityId(ActivityType.sign.getType());
        if (m.getDaysList() != null) {
            for (ActivitySign rec : m.getDaysList()) {
                rec.setActivityId(ActivityType.sign.getType());
                this.activitySignDao.save(rec);
            }
        }
    }

    public Jsoner add(String account, boolean isAgent) {
        return null;
    }

    public Jsoner addBonus(String account, boolean isAgent, String item) {
        User user = this.userDao.findByAccountMaster(account);
        Activity activity = this.activityDao.find(ActivityType.sign.getType());
        if (activity == null) {
            return Jsoner.error("错误代码：300");
        }
        if (activity.getStatus() == 1) {
            return Jsoner.error("该活动已经结束");
        }
        Date curDate = new Date();
        if ((activity.getBeginPrizeTime().getTime() > curDate.getTime()) || (activity.getEndPrizeTime().getTime() < curDate.getTime())) {
            return Jsoner.error("对不起！不在有效时间内");
        }
        if (user.getRegchargeCount() < 1) {
            return Jsoner.error("对不起，您未进行过充值，请先充值再来签到！");
        }
        Date beginTime = WebDateUtils.getDayBeginTime(curDate);
        Date endTime = WebDateUtils.getDayEndTime(curDate);
        ActivityUser activityUser = this.activityUserDao.hasAttendOrPrize(ActivityType.sign.getType(), DateUtils.format(beginTime), DateUtils.format(endTime), account);
        if ((activityUser != null) &&
                (activityUser.getStatus() == 3)) {
            return Jsoner.error("已领取");
        }
        List<ActivityUser> activityUsers = this.activityUserDao.listByUserEx(ActivityType.sign.getType(), account, 3);
        int days = signOfDay(activityUsers);
        ActivitySign activitySign = this.activitySignDao.getObject(days);
        BigDecimal betAmount = this.betDao.getBetAmountByDay(account, beginTime, endTime);
        if (activitySign.getBetAmount().compareTo(betAmount) > 0) {
            return Jsoner.error("您今天的投注量不足" + activitySign.getBetAmount());
        }
        if ((activity.getPrizeType() != 0) && (activity.getPrizeType() != 1)) {
            ActivityUser act = new ActivityUser();
            act.setAccount(account);
            act.setActivityId(ActivityType.sign.getType());
            act.setStatus(3);
            act.setOperator("");
            act.setCreateTime(new Date());
            act.setValidTime(null);
            this.activityUserDao.save(act);


            BigDecimal bonus = activitySign.getGiveAmount();


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
            m.setFinanceId(ActivityType.sign.getType() + "");
            this.financeChangeDao.save(m);
        }
        return Jsoner.success();
    }

    public void setStatus(Activity a, String account, boolean isAgent) {
    }

    public int signOfDay(List<ActivityUser> activityUsers) {
        int days = 1;
        if ((activityUsers == null) || (activityUsers.size() == 0)) {
            return days;
        }
        List<ActivitySign> activitySigns = this.activitySignDao.list(null);
        Date curdate = activityUsers.get(0).getCreateTime();
        if (curdate.getTime() < WebDateUtils.getDayBeginTime(DateUtils.addDay(new Date(), -1)).getTime()) {
            return 1;
        }
        for (ActivityUser activityuser : activityUsers) {
            if ((activityuser.getCreateTime().getTime() >= WebDateUtils.getDayBeginTime(curdate).getTime()) &&
                    (activityuser.getCreateTime().getTime() <= WebDateUtils.getDayEndTime(curdate).getTime())) {
                days++;
            } else {
                if (days > 1) {
                    break;
                }
                return 1;
            }
            curdate = DateUtils.addDay(curdate, -1);
        }
        if (days > activitySigns.size()) {
            days %= activitySigns.size();
            if (days == 0) {
                days = activitySigns.size();
            }
        }
        return days;
    }

    public ActivitySignForIndex forIndex(String account, Date selectMonth) {
        Date curDate = new Date();
        if (selectMonth == null) {
            selectMonth = curDate;
        }
        ActivitySignForIndex m = new ActivitySignForIndex();
        m.setSignToday(false);
        List<ActivityUser> activityUsers = this.activityUserDao.listByUserEx(ActivityType.sign.getType(), account, 3);
        if ((activityUsers.size() > 0) &&
                (((ActivityUser) activityUsers.get(0)).getCreateTime().getTime() >= WebDateUtils.getDayBeginTime(curDate).getTime()) &&
                (((ActivityUser) activityUsers.get(0)).getCreateTime().getTime() <= WebDateUtils.getDayEndTime(curDate).getTime())) {
            m.setSignToday(true);
        }
        int days = signOfDayEx(activityUsers, m.isSignToday());
        m.setDays(days);
        if (curDate.getTime() > WebDateUtils.getMonthBeginTime(selectMonth).getTime()) {
            activityUsers = this.activityUserDao.listByUser(ActivityType.recharge.getType(), account, 3, DateUtils.addDay(WebDateUtils.getMonthBeginTime(selectMonth), -7),
                    WebDateUtils.getMonthEndTime(selectMonth));
        }
        List<String> signList = new ArrayList<>();
        for (ActivityUser a : activityUsers) {
            signList.add(DateUtils.format(a.getCreateTime(), "yyyyMMdd"));
        }
        m.setSignDays(signList);
        return m;
    }

    public int signOfDayEx(List<ActivityUser> activityUsers, boolean signoday) {
        int days = 0;
        if ((activityUsers == null) || (activityUsers.size() == 0)) {
            return 0;
        }
        List<ActivitySign> activitySigns = this.activitySignDao.list(null);
        Date curdate = ((ActivityUser) activityUsers.get(0)).getCreateTime();
        if (curdate.getTime() < WebDateUtils.getDayBeginTime(DateUtils.addDay(new Date(), -1)).getTime()) {
            return 0;
        }
        for (ActivityUser activityuser : activityUsers) {
            if ((activityuser.getCreateTime().getTime() >= WebDateUtils.getDayBeginTime(curdate).getTime()) &&
                    (activityuser.getCreateTime().getTime() <= WebDateUtils.getDayEndTime(curdate).getTime())) {
                days++;
            } else {
                if (days > 0) {
                    break;
                }
                if (signoday) {
                    return 1;
                }
                return 0;
            }
            curdate = DateUtils.addDay(curdate, -1);
        }
        if (days >= activitySigns.size()) {
            days %= activitySigns.size();
            if (days == 0) {
                if (signoday) {
                    days = activitySigns.size();
                } else {
                    days = 0;
                }
            }
        }
        return days;
    }
}
