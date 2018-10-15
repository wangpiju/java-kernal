package com.hs3.service.report;

import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.lotts.BetDao;
import com.hs3.dao.lotts.SettlementDao;
import com.hs3.dao.report.TeamReportDao;
import com.hs3.dao.report.UserReportDao;
import com.hs3.dao.sys.SysConfigDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.AmountChange;
import com.hs3.entity.report.TeamReport;
import com.hs3.entity.report.UserReport;
import com.hs3.entity.sys.SysConfig;
import com.hs3.entity.users.User;
import com.hs3.models.user.UserTeamInfo;
import com.hs3.utils.DateUtils;
import com.hs3.utils.PagerUtils;
import com.hs3.utils.SortTeamReport;
import com.hs3.utils.StrUtils;
import com.hs3.utils.sys.WebDateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teamReportService")
public class TeamReportService {
    private static final Logger logger = LoggerFactory.getLogger(TeamReportService.class);
    @Autowired
    private TeamReportDao teamReportDao;
    @Autowired
    private UserReportDao userReportDao;
    @Autowired
    private BetDao betDao;
    @Autowired
    private SettlementDao settlementDao;
    @Autowired
    private FinanceChangeDao financeChangeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SysConfigDao sysConfigDao;

    public boolean save(TeamReport m) {
        return this.teamReportDao.save(m);
    }

    private TeamReport getMergeUserReport(List<UserReport> list, String account) {
        TeamReport rel = new TeamReport();
        rel.setAccount(account);
        rel.setActivityAndSend(BigDecimal.ZERO);
        rel.setActualSaleAmount(BigDecimal.ZERO);
        rel.setBetAmount(BigDecimal.ZERO);
        rel.setCount(BigDecimal.ZERO);
        rel.setDrawingAmount(BigDecimal.ZERO);
        rel.setJuniorRebateAmount(BigDecimal.ZERO);
        rel.setRebateAmount(BigDecimal.ZERO);
        rel.setRechargeAmount(BigDecimal.ZERO);
        rel.setWinAmount(BigDecimal.ZERO);
        rel.setWages(BigDecimal.ZERO);
        for (UserReport u : list) {
            if (u != null) {
                rel.setActivityAndSend(u.getActivityAndSend().add(rel.getActivityAndSend()));
                rel.setActualSaleAmount(u.getActualSaleAmount().add(rel.getActualSaleAmount()));
                rel.setBetAmount(u.getBetAmount().add(rel.getBetAmount()));
                rel.setCount(u.getCount().add(rel.getCount()));
                rel.setDrawingAmount(u.getDrawingAmount().add(rel.getDrawingAmount()));
                rel.setRebateAmount(u.getRebateAmount().add(u.getJuniorRebateAmount()).add(rel.getRebateAmount()));
                rel.setRechargeAmount(u.getRechargeAmount().add(rel.getRechargeAmount()));
                rel.setWinAmount(u.getWinAmount().add(rel.getWinAmount()));
                rel.setWages(u.getWages().add(rel.getWages()));
            }
        }
        return rel;
    }

    private TeamReport getMergeTeamReport(String account, TeamReport... ul) {
        TeamReport rel = new TeamReport();
        rel.setAccount(account);
        rel.setActivityAndSend(BigDecimal.ZERO);
        rel.setActualSaleAmount(BigDecimal.ZERO);
        rel.setBetAmount(BigDecimal.ZERO);
        rel.setCount(BigDecimal.ZERO);
        rel.setDrawingAmount(BigDecimal.ZERO);
        rel.setJuniorRebateAmount(BigDecimal.ZERO);
        rel.setRebateAmount(BigDecimal.ZERO);
        rel.setRechargeAmount(BigDecimal.ZERO);
        rel.setWinAmount(BigDecimal.ZERO);
        rel.setWages(BigDecimal.ZERO);
        for (TeamReport u : ul) {
            if (u != null) {
                rel.setActivityAndSend(u.getActivityAndSend().add(rel.getActivityAndSend()));
                rel.setActualSaleAmount(u.getActualSaleAmount().add(rel.getActualSaleAmount()));
                rel.setBetAmount(u.getBetAmount().add(rel.getBetAmount()));
                rel.setCount(u.getCount().add(rel.getCount()));
                rel.setDrawingAmount(u.getDrawingAmount().add(rel.getDrawingAmount()));
                rel.setRebateAmount(u.getRebateAmount().add(rel.getRebateAmount()));
                rel.setRechargeAmount(u.getRechargeAmount().add(rel.getRechargeAmount()));
                rel.setWinAmount(u.getWinAmount().add(rel.getWinAmount()));
                rel.setWages(u.getWages().add(rel.getWages()));
            }
        }
        return rel;
    }

    public List<TeamReport> newHistoryStatistics(Page p, String account, Date startTime, Date endTime) {
        return this.teamReportDao.newHistoryStatistics(p, account, startTime, endTime);
    }

    public boolean isExist(String account, Date createDate) {
        return this.teamReportDao.isExist(account, createDate);
    }

    public int getCountByCreatDate(Date createDate) {
        return this.teamReportDao.getCountByCreatDate(createDate);
    }

    public int deleteByCreateDate(Date createDate) {
        return this.teamReportDao.deleteByCreateDate(createDate);
    }

    public List<UserTeamInfo> findTeamInfo(String account, int status, Date begin, Date end, Page p) {
	  
	  /*jd-gui
    SysConfig sys = (SysConfig)this.sysConfigDao.find("daliyWagesKey");
    int defaultKEY = 500;
    if (sys != null) {
      defaultKEY = Integer.parseInt(sys.getVal());
    }
    List<UserTeamInfo> uList = this.userReportDao.getUserInfo(account, begin, end, defaultKEY);
    BigDecimal m;
    if (end.getTime() >= DateUtils.getToDay().getTime())
    {
      Date b = WebDateUtils.getYesterdayTime(new Date());
      b = DateUtils.getDate(b);
      
      List<AmountChange> toDayList1 = this.settlementDao.getTeamList(account, b, end);
      for (AmountChange c : toDayList1)
      {
        UserTeamInfo info = null;
        for (UserTeamInfo u : uList) {
          if (u.getAccount().equals(c.getChangeUser()))
          {
            info = u;
            break;
          }
        }
        if (info == null)
        {
          logger.debug("盈亏有遗漏");
        }
        else
        {
          m = c.getChangeAmount();
          int type = c.getAccountChangeTypeId();
          if ((type == 11) || (type == 12))
          {
            info.setRechargeAmount(info.getRechargeAmount().add(m));
          }
          else if (type == 18)
          {
            info.setDrawingAmount(info.getDrawingAmount().add(m.abs()));
          }
          else if (type == 1)
          {
            if (m.abs().compareTo(new BigDecimal(defaultKEY)) >= 0) {
              info.setUserCount(1);
            }
            info.setWin(info.getWin().add(m));
          }
          else
          {
            info.setWin(info.getWin().add(m));
          }
        }
      }
    }
    List<UserTeamInfo> list = new ArrayList();
    if (status == 0)
    {
      info = new UserTeamInfo();
      info.setAccount(account);
      info.setTeamCount(uList.size());
      for (??? = uList.iterator(); ???.hasNext();)
      {
        u = (UserTeamInfo)???.next();
        if (u.getAccount().equals(info.getAccount()))
        {
          info.setParentAccount(u.getParentAccount());
          info.setParentList(u.getParentList());
          info.setUserType(u.getUserType());
          info.setTeamAmount(u.getTeamAmount());
        }
        info.setRechargeAmount(info.getRechargeAmount().add(u.getRechargeAmount()));
        info.setDrawingAmount(info.getDrawingAmount().add(u.getDrawingAmount()));
        info.setWin(info.getWin().add(u.getWin()));
        info.setRegisterNum(info.getRegisterNum() + u.getRegisterNum());
        info.setFirstRechargeNum(info.getFirstRechargeNum() + u.getFirstRechargeNum());
        info.setUserCount(info.getUserCount() + u.getUserCount());
      }
      list.add(info);
    }
    else
    {
      int i;
      for (; uList.size() > 0; i >= 0)
      {
        UserTeamInfo info;
        UserTeamInfo u;
        i = uList.size() - 1; continue;
        UserTeamInfo u = (UserTeamInfo)uList.get(i);
        if (u.getAccount().equals(account))
        {
          uList.remove(i);
        }
        else if (u.getParentAccount().equals(account))
        {
          list.add(u);
          uList.remove(i);
        }
        else
        {
          UserTeamInfo info = null;
          for (UserTeamInfo k : list) {
            if (u.getParentList().startsWith(k.getParentList()))
            {
              info = k;
              break;
            }
          }
          if (info != null)
          {
            info.setRechargeAmount(info.getRechargeAmount().add(u.getRechargeAmount()));
            info.setDrawingAmount(info.getDrawingAmount().add(u.getDrawingAmount()));
            info.setWin(info.getWin().add(u.getWin()));
            info.setRegisterNum(info.getRegisterNum() + u.getRegisterNum());
            info.setFirstRechargeNum(info.getFirstRechargeNum() + u.getFirstRechargeNum());
            info.setUserCount(info.getUserCount() + u.getUserCount());
            



            uList.remove(i);
          }
        }
        i--;
      }
    }
    Collections.reverse(list);
    
    p.setRowCount(list.size());
    int beginIndex = p.getStartIndex();
    int endIndex = p.getStartIndex() + p.getPageSize();
    
    Object uInfoList = new ArrayList();
    for (int i = beginIndex; i < endIndex; i++)
    {
      if (i > list.size() - 1) {
        break;
      }
      ((List)uInfoList).add((UserTeamInfo)list.get(i));
    }
    return uInfoList;
  */

        SysConfig sys = (SysConfig) sysConfigDao.find("daliyWagesKey");
        int defaultKEY = 500;
        if (sys != null)
            defaultKEY = Integer.parseInt(sys.getVal());
        List<UserTeamInfo> uList = userReportDao.getUserInfo(account, begin, end, defaultKEY);
        if (end.getTime() >= DateUtils.getToDay().getTime()) {
            Date b = WebDateUtils.getYesterdayTime(new Date());
            b = DateUtils.getDate(b);
            List<AmountChange> toDayList1 = settlementDao.getTeamList(account, b, end);
            for (Iterator iterator = toDayList1.iterator(); iterator.hasNext(); ) {
                AmountChange c = (AmountChange) iterator.next();
                UserTeamInfo info = null;
                for (Iterator iterator3 = uList.iterator(); iterator3.hasNext(); ) {
                    UserTeamInfo u = (UserTeamInfo) iterator3.next();
                    if (u.getAccount().equals(c.getChangeUser())) {
                        info = u;
                        break;
                    }
                }

                if (info == null) {
                    logger.debug("盈亏有遗漏");
                } else {
                    BigDecimal m = c.getChangeAmount();
                    int type = c.getAccountChangeTypeId();
                    if (type == 11 || type == 12)
                        info.setRechargeAmount(info.getRechargeAmount().add(m));
                    else if (type == 18)
                        info.setDrawingAmount(info.getDrawingAmount().add(m.abs()));
                    else if (type == 1) {
                        if (m.abs().compareTo(new BigDecimal(defaultKEY)) >= 0)
                            info.setUserCount(1);
                        info.setWin(info.getWin().add(m));
                    } else {
                        info.setWin(info.getWin().add(m));
                    }
                }
            }

        }
        List<UserTeamInfo> list = new ArrayList();
        if (status == 0) {
            UserTeamInfo info = new UserTeamInfo();
            info.setAccount(account);
            info.setTeamCount(uList.size());
            UserTeamInfo u;
            for (Iterator iterator1 = uList.iterator(); iterator1.hasNext(); info.setUserCount(info.getUserCount() + u.getUserCount())) {
                u = (UserTeamInfo) iterator1.next();
                if (u.getAccount().equals(info.getAccount())) {
                    info.setParentAccount(u.getParentAccount());
                    info.setParentList(u.getParentList());
                    info.setUserType(u.getUserType());
                    info.setTeamAmount(u.getTeamAmount());
                }
                info.setRechargeAmount(info.getRechargeAmount().add(u.getRechargeAmount()));
                info.setDrawingAmount(info.getDrawingAmount().add(u.getDrawingAmount()));
                info.setWin(info.getWin().add(u.getWin()));
                info.setRegisterNum(info.getRegisterNum() + u.getRegisterNum());
                info.setFirstRechargeNum(info.getFirstRechargeNum() + u.getFirstRechargeNum());
            }

            list.add(info);
        } else {
            while (uList.size() > 0) {
                for (int i = uList.size() - 1; i >= 0; i--) {
                    UserTeamInfo u = (UserTeamInfo) uList.get(i);
                    if (u.getAccount().equals(account))
                        uList.remove(i);
                    else if (u.getParentAccount().equals(account)) {
                        list.add(u);
                        uList.remove(i);
                    } else {
                        UserTeamInfo info = null;
                        for (Iterator iterator2 = list.iterator(); iterator2.hasNext(); ) {
                            UserTeamInfo k = (UserTeamInfo) iterator2.next();
                            if (u.getParentList().startsWith(k.getParentList())) {
                                info = k;
                                break;
                            }
                        }

                        if (info != null) {
                            info.setRechargeAmount(info.getRechargeAmount().add(u.getRechargeAmount()));
                            info.setDrawingAmount(info.getDrawingAmount().add(u.getDrawingAmount()));
                            info.setWin(info.getWin().add(u.getWin()));
                            info.setRegisterNum(info.getRegisterNum() + u.getRegisterNum());
                            info.setFirstRechargeNum(info.getFirstRechargeNum() + u.getFirstRechargeNum());
                            info.setUserCount(info.getUserCount() + u.getUserCount());
                            uList.remove(i);
                        }
                    }
                }

            }
        }
        Collections.reverse(list);
        p.setRowCount(list.size());
        int beginIndex = p.getStartIndex();
        int endIndex = p.getStartIndex() + p.getPageSize();
        List uInfoList = new ArrayList();
        for (int i = beginIndex; i < endIndex; i++) {
            if (i > list.size() - 1)
                break;
            uInfoList.add((UserTeamInfo) list.get(i));
        }

        return uInfoList;

    }

    public boolean createTeamReportData(Date createDate) {
        boolean successFalg = true;
        if (this.teamReportDao.getCountByCreatDate(createDate) < 1) {
            List<String> userList = this.userReportDao.getAccountList(createDate);
            for (String strAccount : userList) {
                String account = strAccount;
                User u = null;
                do {
                    u = this.userDao.findByAccount(account);
                    if (this.teamReportDao.isExist(account, createDate)) {
                        break;
                    }
                    TeamReport superiorTeamReport = this.teamReportDao
                            .getTeamReportByAccount(account, createDate);
                    successFalg = this.teamReportDao.save(superiorTeamReport);


                    account = u.getParentAccount();
                } while (!u.getAccount().equals(u.getParentAccount()));
            }
        }
        return successFalg;
    }

    public Map<String, Object> getTeamList(Page p, String account, Date begin, Date end, Integer test) {


        /**jd-gui
         begin = DateUtils.getDate(begin);
         end = DateUtils.AddSecond(DateUtils.getDate(end), 86399);

         List<TeamReport> teamReportHis = this.teamReportDao.totalByChild(account, begin, end, test);

         Date curDate = this.teamReportDao.getMaxDate();
         if (curDate == null) {
         curDate = begin;
         } else if (curDate.getTime() < begin.getTime()) {
         curDate = begin;
         } else {
         curDate = DateUtils.addDay(curDate, 1);
         }
         List<TeamReport> todayList = new ArrayList();
         List<User> nextList;
         if (curDate.getTime() < end.getTime())
         {
         List<UserReport> todayData = this.userReportDao.getList(account, curDate, end, 1, test);

         List<User> userList = this.userDao.listByParent(account);

         nextList = this.userDao.listUserAndSelfByParent(account);
         for (User user : nextList)
         {
         List<UserReport> temp = new ArrayList();

         Map<String, String> map = new HashMap();
         String parentList = user.getParentList();
         for (User u : userList) {
         if ((!StrUtils.hasEmpty(new Object[] { parentList })) && (u.getParentList().startsWith(parentList))) {
         map.put(u.getAccount(), u.getAccount());
         }
         }
         for (UserReport data : todayData)
         {
         Object bt = map.get(data.getAccount());
         if (bt != null) {
         temp.add(data);
         }
         }
         if (!temp.isEmpty()) {
         todayList.add(getMergeUserReport(temp, user.getAccount()));
         }
         }
         }
         Set<TeamReport> tempList = new LinkedHashSet();
         TeamReport b;
         if (!todayList.isEmpty()) {
         for (TeamReport a : todayList)
         {
         for (??? = teamReportHis.iterator(); ???.hasNext();)
         {
         b = (TeamReport)???.next();
         if (b.getAccount().equals(a.getAccount()))
         {
         b.setBetAmount(b.getBetAmount().add(a.getBetAmount()));
         b.setRebateAmount(b.getRebateAmount().add(
         a.getRebateAmount()));
         b.setActualSaleAmount(b.getActualSaleAmount().add(
         a.getActualSaleAmount()));
         b.setWinAmount(b.getWinAmount().add(a.getWinAmount()));
         b.setActivityAndSend(b.getActivityAndSend().add(
         a.getActivityAndSend()));
         b.setDrawingAmount(b.getDrawingAmount().add(
         a.getDrawingAmount()));
         b.setRechargeAmount(b.getRechargeAmount().add(
         a.getRechargeAmount()));
         b.setCount(b.getCount().add(a.getCount()));
         b.setWages(b.getWages().add(a.getWages()));
         break;
         }
         }
         tempList.add(a);
         }
         }
         if (!tempList.isEmpty()) {
         for (TeamReport t : tempList) {
         teamReportHis.add(t);
         }
         }
         TeamReport curTeamData = null;
         for (TeamReport tr : teamReportHis) {
         if (account.equals(tr.getAccount()))
         {
         curTeamData = tr;
         teamReportHis.remove(tr);
         break;
         }
         }
         if (curTeamData == null)
         {
         curTeamData = new TeamReport();
         curTeamData.setActivityAndSend(BigDecimal.ZERO);
         curTeamData.setActualSaleAmount(BigDecimal.ZERO);
         curTeamData.setBetAmount(BigDecimal.ZERO);
         curTeamData.setCount(BigDecimal.ZERO);
         curTeamData.setDrawingAmount(BigDecimal.ZERO);
         curTeamData.setJuniorRebateAmount(BigDecimal.ZERO);
         curTeamData.setRebateAmount(BigDecimal.ZERO);
         curTeamData.setRechargeAmount(BigDecimal.ZERO);
         curTeamData.setWinAmount(BigDecimal.ZERO);
         curTeamData.setWages(BigDecimal.ZERO);
         }
         SortTeamReport comparator = new SortTeamReport();
         Collections.sort(teamReportHis, comparator);


         List<TeamReport> resultList = new ArrayList();
         if (teamReportHis.size() > 0)
         {
         Object pager = PagerUtils.create(teamReportHis,
         p.getPageSize());
         p.setRowCount(teamReportHis.size());
         resultList = ((PagerUtils)pager).getPagedList(p.getNowPage());
         }
         Object resultMap = new HashMap();
         ((Map)resultMap).put("curTeamData", curTeamData);
         ((Map)resultMap).put("resultList", resultList);
         return resultMap;
         */

        begin = DateUtils.getDate(begin);
        end = DateUtils.AddSecond(DateUtils.getDate(end), 0x1517f);
        List<TeamReport> teamReportHis = teamReportDao.totalByChild(account, begin, end, test);
        Date curDate = teamReportDao.getMaxDate();
        if (curDate == null)
            curDate = begin;
        else if (curDate.getTime() < begin.getTime())
            curDate = begin;
        else
            curDate = DateUtils.addDay(curDate, 1);
        List<TeamReport> todayList = new ArrayList();
        if (curDate.getTime() < end.getTime()) {
            List<UserReport> todayData = userReportDao.getList(account, curDate, end, 1, test);
            List<User> userList = userDao.listByParent(account);
            List<User> nextList = userDao.listUserAndSelfByParent(account);
            for (Iterator iterator3 = nextList.iterator(); iterator3.hasNext(); ) {
                User user = (User) iterator3.next();
                List<UserReport> temp = new ArrayList();
                Map<String, String> map = new HashMap();
                String parentList = user.getParentList();
                for (Iterator iterator5 = userList.iterator(); iterator5.hasNext(); ) {
                    User u = (User) iterator5.next();
                    if (!StrUtils.hasEmpty(new Object[]{
                            parentList
                    }) && u.getParentList().startsWith(parentList))
                        map.put(u.getAccount(), u.getAccount());
                }

                for (Iterator iterator6 = todayData.iterator(); iterator6.hasNext(); ) {
                    UserReport data = (UserReport) iterator6.next();
                    Object bt = map.get(data.getAccount());
                    if (bt != null)
                        temp.add(data);
                }

                if (!temp.isEmpty())
                    todayList.add(getMergeUserReport(temp, user.getAccount()));
            }

        }
        Set<TeamReport> tempList = new LinkedHashSet();
        if (!todayList.isEmpty()) {
            Iterator iterator = todayList.iterator();
            label0:
            while (iterator.hasNext()) {
                TeamReport a = (TeamReport) iterator.next();
                for (Iterator iterator4 = teamReportHis.iterator(); iterator4.hasNext(); ) {
                    TeamReport b = (TeamReport) iterator4.next();
                    if (b.getAccount().equals(a.getAccount())) {
                        b.setBetAmount(b.getBetAmount().add(a.getBetAmount()));
                        b.setRebateAmount(b.getRebateAmount().add(a.getRebateAmount()));
                        b.setActualSaleAmount(b.getActualSaleAmount().add(a.getActualSaleAmount()));
                        b.setWinAmount(b.getWinAmount().add(a.getWinAmount()));
                        b.setActivityAndSend(b.getActivityAndSend().add(a.getActivityAndSend()));
                        b.setDrawingAmount(b.getDrawingAmount().add(a.getDrawingAmount()));
                        b.setRechargeAmount(b.getRechargeAmount().add(a.getRechargeAmount()));
                        b.setCount(b.getCount().add(a.getCount()));
                        b.setWages(b.getWages().add(a.getWages()));
                        continue label0;
                    }
                }

                tempList.add(a);
            }
        }
        if (!tempList.isEmpty()) {
            TeamReport t;
            for (Iterator iterator1 = tempList.iterator(); iterator1.hasNext(); teamReportHis.add(t))
                t = (TeamReport) iterator1.next();

        }
        TeamReport curTeamData = null;
        for (Iterator iterator2 = teamReportHis.iterator(); iterator2.hasNext(); ) {
            TeamReport tr = (TeamReport) iterator2.next();
            if (account.equals(tr.getAccount())) {
                curTeamData = tr;
                teamReportHis.remove(tr);
                break;
            }
        }

        if (curTeamData == null) {
            curTeamData = new TeamReport();
            curTeamData.setActivityAndSend(BigDecimal.ZERO);
            curTeamData.setActualSaleAmount(BigDecimal.ZERO);
            curTeamData.setBetAmount(BigDecimal.ZERO);
            curTeamData.setCount(BigDecimal.ZERO);
            curTeamData.setDrawingAmount(BigDecimal.ZERO);
            curTeamData.setJuniorRebateAmount(BigDecimal.ZERO);
            curTeamData.setRebateAmount(BigDecimal.ZERO);
            curTeamData.setRechargeAmount(BigDecimal.ZERO);
            curTeamData.setWinAmount(BigDecimal.ZERO);
            curTeamData.setWages(BigDecimal.ZERO);
        }
        SortTeamReport comparator = new SortTeamReport();
        Collections.sort(teamReportHis, comparator);
        List<TeamReport> resultList = new ArrayList();
        if (teamReportHis.size() > 0) {
            PagerUtils pager = PagerUtils.create(teamReportHis, p.getPageSize());
            p.setRowCount(teamReportHis.size());
            resultList = pager.getPagedList(p.getNowPage());
        }
        Map resultMap = new HashMap();
        resultMap.put("curTeamData", curTeamData);
        resultMap.put("resultList", resultList);
        return resultMap;

    }

    public List<TeamReport> getHisTeamDetails(Page p, String account, Date begin, Date end, Integer test) {
        return this.teamReportDao.getHisTeamDetails(p, account, begin, end, test);
    }

    public TeamReport selfReport(String account, Date begin, Date end, Integer test) {
        begin = DateUtils.getDate(begin);
        end = DateUtils.AddSecond(DateUtils.getDate(end), 86399);

        TeamReport teamReport = this.teamReportDao.totalBySelf(account, begin, end, test);
        Date curDate = this.teamReportDao.getMaxDate();
        if (curDate == null) {
            curDate = DateUtils.getToDay();
        } else {
            curDate = DateUtils.addDay(curDate, 1);
        }
        TeamReport curTeamReport = null;
        if (curDate.getTime() < end.getTime()) {
            List<UserReport> userList = this.userReportDao.getList(account, curDate, end, 1, test);
            curTeamReport = getMergeUserReport(userList, account);
        }
        teamReport = getMergeTeamReport(account, new TeamReport[]{teamReport, curTeamReport});
        return teamReport;
    }

    public List<TeamReport> getAdminTeamData(Page p, String account, Date begin, Date end, Integer test) {

        /**jd-gui
         begin = DateUtils.getDate(begin);
         end = DateUtils.AddSecond(DateUtils.getDate(end), 86399);

         List<TeamReport> teamReportHis = this.teamReportDao.totalByChild(account, begin, end, test);

         Date curDate = this.teamReportDao.getMaxDate();
         if (curDate == null) {
         curDate = begin;
         } else if (curDate.getTime() < begin.getTime()) {
         curDate = begin;
         } else {
         curDate = DateUtils.addDay(curDate, 1);
         }
         List<TeamReport> todayList = new ArrayList();
         List<User> nextList;
         if (curDate.getTime() < end.getTime())
         {
         List<UserReport> todayData = null;

         List<User> userList = null;

         nextList = null;
         if (!StrUtils.hasEmpty(new Object[] { account }))
         {
         userList = this.userDao.listByParent(account);
         nextList = this.userDao.listUserAndSelfByParent(account);
         todayData = this.userReportDao.getList(account, curDate, end, 1, test);
         }
         else
         {
         userList = this.userDao.getAllUserList(test);
         nextList = this.userDao.getAccountList(test);
         todayData = this.userReportDao.getList(null, curDate, end, 1, test);
         }
         for (User user : nextList)
         {
         List<UserReport> temp = new ArrayList();

         Map<String, String> map = new HashMap();
         String parentList = user.getParentList();
         for (User u : userList) {
         if ((!StrUtils.hasEmpty(new Object[] { parentList })) && (u.getParentList().startsWith(parentList))) {
         map.put(u.getAccount(), u.getAccount());
         }
         }
         for (UserReport data : todayData)
         {
         Object bt = map.get(data.getAccount());
         if (bt != null) {
         temp.add(data);
         }
         }
         if (!temp.isEmpty()) {
         todayList.add(getMergeUserReport(temp, user.getAccount()));
         }
         }
         }
         Set<TeamReport> tempList = new LinkedHashSet();
         TeamReport b;
         if (!todayList.isEmpty()) {
         for (TeamReport a : todayList)
         {
         for (??? = teamReportHis.iterator(); ???.hasNext();)
         {
         b = (TeamReport)???.next();
         if (b.getAccount().equals(a.getAccount()))
         {
         b.setBetAmount(b.getBetAmount().add(a.getBetAmount()));
         b.setRebateAmount(b.getRebateAmount().add(
         a.getRebateAmount()));
         b.setActualSaleAmount(b.getActualSaleAmount().add(
         a.getActualSaleAmount()));
         b.setWinAmount(b.getWinAmount().add(a.getWinAmount()));
         b.setActivityAndSend(b.getActivityAndSend().add(
         a.getActivityAndSend()));
         b.setDrawingAmount(b.getDrawingAmount().add(
         a.getDrawingAmount()));
         b.setRechargeAmount(b.getRechargeAmount().add(
         a.getRechargeAmount()));
         b.setCount(b.getCount().add(a.getCount()));
         b.setWages(b.getWages().add(a.getWages()));
         break;
         }
         }
         tempList.add(a);
         }
         }
         if (!tempList.isEmpty()) {
         for (TeamReport t : tempList) {
         teamReportHis.add(t);
         }
         }
         TeamReport curTeamData = null;
         if (!StrUtils.hasEmpty(new Object[] { account })) {
         for (TeamReport tr : teamReportHis) {
         if (account.equals(tr.getAccount()))
         {
         curTeamData = tr;
         teamReportHis.remove(tr);
         break;
         }
         }
         }
         SortTeamReport comparator = new SortTeamReport();
         Collections.sort(teamReportHis, comparator);
         if ((!StrUtils.hasEmpty(new Object[] { account })) && (curTeamData != null)) {
         teamReportHis.add(0, curTeamData);
         }
         List<TeamReport> resultList = new ArrayList();
         if (teamReportHis.size() > 0)
         {
         Object pager = PagerUtils.create(teamReportHis,
         p.getPageSize());
         p.setRowCount(teamReportHis.size());
         resultList = ((PagerUtils)pager).getPagedList(p.getNowPage());
         }
         return resultList;
         */

        begin = DateUtils.getDate(begin);
        end = DateUtils.AddSecond(DateUtils.getDate(end), 0x1517f);
        List<TeamReport> teamReportHis = teamReportDao.totalByChild(account, begin, end, test);
        Date curDate = teamReportDao.getMaxDate();
        if (curDate == null)
            curDate = begin;
        else if (curDate.getTime() < begin.getTime())
            curDate = begin;
        else
            curDate = DateUtils.addDay(curDate, 1);
        List<TeamReport> todayList = new ArrayList();
        if (curDate.getTime() < end.getTime()) {
            List<UserReport> todayData = null;
            List<User> userList = null;
            List<User> nextList = null;
            if (!StrUtils.hasEmpty(new Object[]{
                    account
            })) {
                userList = userDao.listByParent(account);
                nextList = userDao.listUserAndSelfByParent(account);
                todayData = userReportDao.getList(account, curDate, end, 1, test);
            } else {
                userList = userDao.getAllUserList(test);
                nextList = userDao.getAccountList(test);
                todayData = userReportDao.getList(null, curDate, end, 1, test);
            }
            for (Iterator iterator3 = nextList.iterator(); iterator3.hasNext(); ) {
                User user = (User) iterator3.next();
                List<UserReport> temp = new ArrayList();
                Map<String, String> map = new HashMap();
                String parentList = user.getParentList();
                for (Iterator iterator5 = userList.iterator(); iterator5.hasNext(); ) {
                    User u = (User) iterator5.next();
                    if (!StrUtils.hasEmpty(new Object[]{
                            parentList
                    }) && u.getParentList().startsWith(parentList))
                        map.put(u.getAccount(), u.getAccount());
                }

                for (Iterator iterator6 = todayData.iterator(); iterator6.hasNext(); ) {
                    UserReport data = (UserReport) iterator6.next();
                    Object bt = map.get(data.getAccount());
                    if (bt != null)
                        temp.add(data);
                }

                if (!temp.isEmpty())
                    todayList.add(getMergeUserReport(temp, user.getAccount()));
            }

        }
        Set<TeamReport> tempList = new LinkedHashSet();
        if (!todayList.isEmpty()) {
            Iterator iterator = todayList.iterator();
            label0:
            while (iterator.hasNext()) {
                TeamReport a = (TeamReport) iterator.next();
                for (Iterator iterator4 = teamReportHis.iterator(); iterator4.hasNext(); ) {
                    TeamReport b = (TeamReport) iterator4.next();
                    if (b.getAccount().equals(a.getAccount())) {
                        b.setBetAmount(b.getBetAmount().add(a.getBetAmount()));
                        b.setRebateAmount(b.getRebateAmount().add(a.getRebateAmount()));
                        b.setActualSaleAmount(b.getActualSaleAmount().add(a.getActualSaleAmount()));
                        b.setWinAmount(b.getWinAmount().add(a.getWinAmount()));
                        b.setActivityAndSend(b.getActivityAndSend().add(a.getActivityAndSend()));
                        b.setDrawingAmount(b.getDrawingAmount().add(a.getDrawingAmount()));
                        b.setRechargeAmount(b.getRechargeAmount().add(a.getRechargeAmount()));
                        b.setCount(b.getCount().add(a.getCount()));
                        b.setWages(b.getWages().add(a.getWages()));
                        continue label0;
                    }
                }

                tempList.add(a);
            }
        }
        if (!tempList.isEmpty()) {
            TeamReport t;
            for (Iterator iterator1 = tempList.iterator(); iterator1.hasNext(); teamReportHis.add(t))
                t = (TeamReport) iterator1.next();

        }
        TeamReport curTeamData = null;
        if (!StrUtils.hasEmpty(new Object[]{
                account
        })) {
            for (Iterator iterator2 = teamReportHis.iterator(); iterator2.hasNext(); ) {
                TeamReport tr = (TeamReport) iterator2.next();
                if (account.equals(tr.getAccount())) {
                    curTeamData = tr;
                    teamReportHis.remove(tr);
                    break;
                }
            }

        }
        SortTeamReport comparator = new SortTeamReport();
        Collections.sort(teamReportHis, comparator);
        if (!StrUtils.hasEmpty(new Object[]{
                account
        }) && curTeamData != null)
            teamReportHis.add(0, curTeamData);
        List<TeamReport> resultList = new ArrayList();
        if (teamReportHis.size() > 0) {
            PagerUtils pager = PagerUtils.create(teamReportHis, p.getPageSize());
            p.setRowCount(teamReportHis.size());
            resultList = pager.getPagedList(p.getNowPage());
        }
        return resultList;

    }
}
