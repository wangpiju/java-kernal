package com.hs3.service.activity;

import com.hs3.commons.ActivityType;
import com.hs3.dao.activity.ActivityBetconsumerDao;
import com.hs3.dao.activity.ActivityDao;
import com.hs3.dao.activity.ActivityUserDao;
import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.lotts.BetDao;
import com.hs3.dao.user.UserDao;
import com.hs3.entity.activity.Activity;
import com.hs3.entity.activity.ActivityBetconsumer;
import com.hs3.entity.activity.ActivityUser;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.users.User;
import com.hs3.models.Jsoner;
import com.hs3.models.activity.AcivityBetconsumerModel;
import com.hs3.models.activity.ConsumerForIndex;
import com.hs3.utils.DateUtils;
import com.hs3.utils.sys.WebDateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityBetconsumerService")
public class ActivityBetconsumerService
        extends ActivityBaseService {
    private static final String KEY = "betconsumer";
    @Autowired
    private ActivityBetconsumerDao activityBetconsumerDao;
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

    public void save(ActivityBetconsumer m) {
        this.activityBetconsumerDao.save(m);
    }

    public int delete(Integer activityId) {
        return this.activityBetconsumerDao.deleteByActivityId(activityId);
    }

    public List<ActivityBetconsumer> findEntityByActivityId(Integer activityId) {
        return this.activityBetconsumerDao.findEntityByActivityId(activityId);
    }

    public AcivityBetconsumerModel findFull() {
        AcivityBetconsumerModel m = this.activityDao.findToClass(ActivityType.betconsumer.getType(), AcivityBetconsumerModel.class);
        if (m == null) {
            return null;
        }
        List<ActivityBetconsumer> list = this.activityBetconsumerDao.findEntityByActivityId(ActivityType.betconsumer.getType());
        m.setItems(list);
        return m;
    }

    public void saveOrUpdate(AcivityBetconsumerModel m) {
        if (m == null) {
            return;
        }
        m.setUseRange(0);
        m.setNeedAttend(0);
        m.setNeedPrize(0);
        this.activityDao.update(m);
        this.activityBetconsumerDao.deleteByActivityId(ActivityType.betconsumer.getType());
        for (ActivityBetconsumer rec : m.getItems()) {
            rec.setActivityId(ActivityType.betconsumer.getType());
            this.activityBetconsumerDao.save(rec);
        }
    }

    public Jsoner add(String account, boolean isAgent) {
        return null;
    }

    public void setStatus(Activity a, String account, boolean isAgent) {
    }

    public Jsoner addBonus(String account, boolean isAgent, String item) {
        User user = this.userDao.findByAccountMaster(account);
        Activity activity = this.activityDao.find(ActivityType.betconsumer.getType());
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
        Date beginTime = WebDateUtils.getDayBeginTime(curDate);
        Date endTime = WebDateUtils.getEndTime(curDate);
        ActivityUser activityUser = this.activityUserDao
                .hasAttendOrPrizeByItem(ActivityType.betconsumer.getType(), DateUtils.format(beginTime), DateUtils.format(endTime), account, item);
        if ((activityUser != null) &&
                (activityUser.getStatus() == 3)) {
            return Jsoner.error("已领取");
        }
        List<ActivityBetconsumer> activityBetconsumers = this.activityBetconsumerDao.getHasGiveList(account, ActivityType.betconsumer.getType(), beginTime, endTime);
        BigDecimal tmpBetAmount = new BigDecimal("0");
        for (ActivityBetconsumer abc : activityBetconsumers) {
            tmpBetAmount = tmpBetAmount.add(abc.getBetAmount());
        }
        BigDecimal betAmount = this.betDao.getBetAmountByDay(account, beginTime, endTime);
        ActivityBetconsumer abc = this.activityBetconsumerDao.getObject(item);
        if (abc == null) {
            return Jsoner.error("您提交数据有误");
        }
        if (abc.getBetAmount().compareTo(betAmount.subtract(tmpBetAmount)) > 0) {
            return Jsoner.error("您今天的投注量不足" + abc.getBetAmount());
        }
        if ((activity.getPrizeType() != 0) && (activity.getPrizeType() != 1)) {
            ActivityUser act = new ActivityUser();
            act.setAccount(account);
            act.setActivityId(ActivityType.betconsumer.getType());
            act.setStatus(3);
            act.setOperator("");
            act.setItem(item);
            act.setCreateTime(new Date());
            act.setValidTime(null);
            this.activityUserDao.save(act);


            BigDecimal bonus = abc.getGiveAmount();


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
            m.setFinanceId(ActivityType.betconsumer.getType()+"");
            this.financeChangeDao.save(m);
        }
        return Jsoner.success();
    }

    public ConsumerForIndex forIndex(String account) {
        Date curDate = new Date();
        Date beginTime = WebDateUtils.getDayBeginTime(curDate);
        Date endTime = curDate;
        List<ActivityBetconsumer> activityBetconsumers = this.activityBetconsumerDao.getHasGiveList(account, ActivityType.betconsumer.getType(), beginTime, endTime);
        BigDecimal hasGiveAmount = new BigDecimal("0");
        BigDecimal hasBetAmount = new BigDecimal("0");
        List<String> hasGiveItmes = new ArrayList<>();
        List<String> canGiveItmes = new ArrayList<>();
        for (ActivityBetconsumer abc : activityBetconsumers) {
            hasGiveAmount = hasGiveAmount.add(abc.getGiveAmount());
            hasBetAmount = hasBetAmount.add(abc.getBetAmount());
            hasGiveItmes.add(abc.getItemId());
        }
        BigDecimal betAmount = this.betDao.getBetAmountByDay(account, beginTime, endTime);
        BigDecimal actAmount = betAmount.subtract(hasBetAmount);
        List<ActivityBetconsumer> canBetconsumers = this.activityBetconsumerDao.getCanGiveList(actAmount);
        for (ActivityBetconsumer abc : canBetconsumers) {
            if (!hasGiveItmes.contains(abc.getItemId())) {
                canGiveItmes.add(abc.getItemId());
            }
        }
        ConsumerForIndex cfi = new ConsumerForIndex();
        cfi.setGiveAmount(hasGiveAmount);
        cfi.setHasGiveItmes(hasGiveItmes);
        cfi.setCanGiveItems(canGiveItmes);
        cfi.setConsumerAmount(betAmount.setScale(0, 1));
        return cfi;
    }
}
