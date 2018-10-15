package com.hs3.service.activity;

import com.hs3.commons.ActivityType;
import com.hs3.dao.activity.ActivityBetDao;
import com.hs3.dao.activity.ActivityDao;
import com.hs3.dao.activity.ActivityUserDao;
import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.report.UserReportDao;
import com.hs3.dao.sys.SysConfigDao;
import com.hs3.dao.user.UserDao;
import com.hs3.entity.activity.Activity;
import com.hs3.entity.activity.ActivityBet;
import com.hs3.entity.activity.ActivityUser;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.report.UserReport;
import com.hs3.entity.sys.SysConfig;
import com.hs3.entity.users.User;
import com.hs3.models.Jsoner;
import com.hs3.models.activity.ActivityBetModel;
import com.hs3.utils.DateUtils;
import com.hs3.utils.sys.WebDateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityBetService")
public class ActivityBetService
        extends ActivityBaseService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityBetService.class);
    private static final String KEY = "bet";
    private static final String SYS_BET_KEY = "BET_KEY";
    @Autowired
    private ActivityBetDao activityBetDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private UserReportDao userReportDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FinanceChangeDao financeChangeDao;
    @Autowired
    private ActivityUserDao activityUserDao;
    @Autowired
    private SysConfigDao sysConfigDao;

    public ActivityBetModel findFullForBet() {
        ActivityBetModel m =  this.activityDao.findToClass(ActivityType.bet.getType(), ActivityBetModel.class);
        if (m == null) {
            return null;
        }
        List<ActivityBet> list = this.activityBetDao.findEntityByActivityId(ActivityType.bet.getType());

        m.setBonusList(list);
        return m;
    }

    public void saveOrUpdateForBet(ActivityBetModel m) {
        if (m == null) {
            return;
        }
        m.setUseRange(0);
        m.setNeedAttend(0);
        m.setNeedPrize(0);
        this.activityDao.update(m);
        this.activityBetDao.deleteByActivityId(ActivityType.bet.getType());
        for (ActivityBet rec : m.getBonusList()) {
            rec.setActivityId(ActivityType.bet.getType());
            this.activityBetDao.save(rec);
        }
    }

    public void updateAmountByBet(Date dDate, Date taskDate) {
        String createDate = DateUtils.formatDate(dDate);
        SysConfig sys = this.sysConfigDao.findLock("BET_KEY");
        if (!createDate.equals(sys.getVal())) {
            sys.setVal(createDate);
            this.sysConfigDao.update(sys);
        } else {
            return;
        }
        Activity activity =  this.activityDao.find(ActivityType.bet.getType());
        if ((activity == null) || activity.getStatus() > 0) {
            logger.info("没有投注佣金活动或活动被禁用");
            return;
        }
        if (activity.getBeginPrizeTime().getTime() > dDate.getTime()) {
            logger.info("投注佣金活动未开始，活动开始时间:" + activity.getBeginPrizeTime());
            return;
        }
        if (activity.getEndPrizeTime().getTime() < dDate.getTime()) {
            logger.info("投注佣金活动已过期，活动结束时间:" + activity.getEndPrizeTime());
            return;
        }
        List<UserReport> userReportList = this.userReportDao.findBetByCreateDate(createDate);
        Map<String, List<BigDecimal>> map = new HashMap<>();
        Map<String, User> userList = new HashMap<>();


        ActivityBet loss;
        for (UserReport u : userReportList) {
            loss = this.activityBetDao.findEntityByMaxAmount(u.getBetAmount());
            if (loss != null) {
                User user = this.userDao.findByAccount(u.getAccount());

                BigDecimal[] amounts = {loss.getGiveFatherAmount(), loss.getGiveGrandpaAmount()};
                for (BigDecimal amount : amounts) {
                    if (user.getAccount().equals(user.getParentAccount())) {
                        break;
                    }
                    User user1 = this.userDao.findByAccount(user.getParentAccount());
                    if (user1.getAccount().equals(user1.getParentAccount())) {
                        break;
                    }
                    List<BigDecimal> olds = null;
                    if (map.containsKey(user.getParentAccount())) {
                        olds = map.get(user.getParentAccount());
                    } else {
                        olds = new ArrayList<>();
                    }
                    olds.add(amount);
                    map.put(user.getParentAccount(), olds);

                    user = this.userDao.findByAccount(user.getParentAccount());
                    userList.put(user.getAccount(), user);
                }
            }
        }


        String remark = getRemark(activity);


        for (String k : userList.keySet()) {
            User user = userList.get(k);
            List<BigDecimal> amounts = map.get(k);

            BigDecimal old = user.getAmount();
            BigDecimal all = BigDecimal.ZERO;

            //jd-gui
      /*for (??? = amounts.iterator(); ((Iterator)???).hasNext();)
      {
        BigDecimal a = (BigDecimal)((Iterator)???).next();*/


            ActivityUser aUser;
            for (Iterator iterator2 = amounts.iterator(); iterator2.hasNext(); activityUserDao.save(aUser)) {
                BigDecimal a = (BigDecimal) iterator2.next();

                //jd-gui
	        /*all = all.add(a);
	        
	        old = old.add(a);
	        
	        FinanceChange m = new FinanceChange();
	        
	        m.setAccountChangeTypeId(Integer.valueOf(19));
	        m.setBalance(old);
	        m.setChangeAmount(a);
	        m.setChangeTime(WebDateUtils.getActivityStartTime(taskDate));
	        m.setChangeUser(user.getAccount());
	        m.setTest(user.getTest());
	        m.setRemark(remark);
	        m.setStatus(Integer.valueOf(2));
	        m.setOperator("");
	        m.setFinanceId("bet");
	        this.financeChangeDao.save(m);
	        ActivityUser aUser = new ActivityUser();
	        aUser.setAccount(user.getAccount());
	        aUser.setActivityId("bet");
	        aUser.setCreateTime(WebDateUtils.getActivityStartTime(taskDate));
	        aUser.setStatus(Integer.valueOf(2));
	        aUser.setOperator("");
	        this.activityUserDao.save(aUser);*/
                all = all.add(a);
                old = old.add(a);
                FinanceChange m = new FinanceChange();
                m.setAccountChangeTypeId(19);
                m.setBalance(old);
                m.setChangeAmount(a);
                m.setChangeTime(WebDateUtils.getActivityStartTime(taskDate));
                m.setChangeUser(user.getAccount());
                m.setTest(user.getTest());
                m.setRemark(remark);
                m.setStatus(2);
                m.setOperator("");
                m.setFinanceId(ActivityType.bet.getType()+"");
                financeChangeDao.save(m);
                aUser = new ActivityUser();
                aUser.setAccount(user.getAccount());
                aUser.setActivityId(ActivityType.bet.getType());
                aUser.setCreateTime(WebDateUtils.getActivityStartTime(taskDate));
                aUser.setStatus(2);
                aUser.setOperator("");
            }
            this.userDao.updateAmount(k, all);
        }


    }

    public Jsoner add(String account, boolean isAgent) {
        return Jsoner.error("无需参加");
    }

    public Jsoner addBonus(String account, boolean isAgent, String item) {
        return Jsoner.error("无需申请");
    }

    public void setStatus(Activity a, String account, boolean isAgent) {
        a.setNeedAttend(0);
        a.setNeedPrize(0);
    }
}
