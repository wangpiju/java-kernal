package com.hs3.service.report;

import com.hs3.dao.report.TeamInReportDao;
import com.hs3.dao.report.UserInReportDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.report.TeamInReport;
import com.hs3.entity.report.UserInReport;
import com.hs3.entity.users.User;
import com.hs3.models.report.TeamInReportModel;
import com.hs3.utils.PagerUtils;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teamInReportService")
public class TeamInReportService {
    @Autowired
    private TeamInReportDao teamInReportDao;
    @Autowired
    private UserInReportDao userInReportDao;
    @Autowired
    private UserDao userDao;

    public List<TeamInReport> newHistoryStatistics(Page p, TeamInReport m, String startTime, String endTime) {
        return this.teamInReportDao.newHistoryStatistics(p, m, startTime, endTime);
    }

    public List<TeamInReport> newHistoryDetails(Page p, TeamInReport m, String startTime, String endTime) {
        return this.teamInReportDao.newHistoryDetails(p, m, startTime, endTime);
    }

    public List<TeamInReport> todayDataList(Page p, TeamInReport m) {
        List<TeamInReport> list = new ArrayList();
        List<User> userList = this.userDao.listByParent(m.getAccount());
        List<TeamInReport> dataList = this.teamInReportDao.getTodayDataList(m);

        TeamInReport dataBySelf = new TeamInReport();

        BigDecimal betAmount = new BigDecimal("0");

        BigDecimal winAmount = new BigDecimal("0");

        BigDecimal totalAmount = new BigDecimal("0");

        /**jd-gui
         for (TeamInReport b : dataList)
         {
         betAmount = betAmount.add(b.getBetAmount());
         winAmount = winAmount.add(b.getWinAmount());
         }
         totalAmount = totalAmount.add(winAmount).subtract(betAmount);

         dataBySelf.setBetAmount(betAmount);
         dataBySelf.setWinAmount(winAmount);
         dataBySelf.setAccount(m.getAccount());
         dataBySelf.setCreateDate(m.getCreateDate());
         dataBySelf.setTest(m.getTest());
         dataBySelf.setTotalAmount(totalAmount);
         list.add(dataBySelf);

         List<User> userData = this.userDao.listUserAndSelfByParent(m.getAccount());
         for (User u : userData)
         {
         TeamInReport nextTeamData = new TeamInReport();
         BigDecimal nBetAmount = new BigDecimal("0");
         BigDecimal nWinAmount = new BigDecimal("0");
         BigDecimal nTotalAmount = new BigDecimal("0");
         if (!m.getAccount().equals(u.getAccount()))
         {
         Map<String, String> map = new HashMap();
         for (User nut : userList) {
         if (u.getAccount().equals(nut.getAccount()))
         {
         String parentList = nut.getParentList();
         for (User nt : userList) {
         if ((!StrUtils.hasEmpty(new Object[] { parentList })) && (nt.getParentList().startsWith(parentList))) {
         map.put(nt.getAccount(), nt.getAccount());
         }
         }
         }
         }
         for (TeamInReport nb : dataList)
         {
         Object bt = map.get(nb.getAccount());
         if (bt != null)
         {
         nBetAmount = nBetAmount.add(nb.getBetAmount());
         nWinAmount = nWinAmount.add(nb.getWinAmount());
         }
         }
         nTotalAmount = nTotalAmount.add(nWinAmount).subtract(nBetAmount);
         if (nBetAmount.compareTo(new BigDecimal("0")) != 0)
         {
         nextTeamData.setBetAmount(nBetAmount);
         nextTeamData.setWinAmount(nWinAmount);
         nextTeamData.setAccount(u.getAccount());
         nextTeamData.setCreateDate(m.getCreateDate());
         nextTeamData.setTest(m.getTest());
         nextTeamData.setTotalAmount(nTotalAmount);
         list.add(nextTeamData);
         }
         }
         }
         Object page1 = new ArrayList();
         if (list.size() > 0)
         {
         Object pager = PagerUtils.create(list, p.getPageSize());
         p.setRowCount(list.size());
         page1 = ((PagerUtils)pager).getPagedList(p.getNowPage());
         }
         return page1;*/

        for (Iterator iterator = dataList.iterator(); iterator.hasNext(); ) {
            TeamInReport b = (TeamInReport) iterator.next();
            betAmount = betAmount.add(b.getBetAmount());
            winAmount = winAmount.add(b.getWinAmount());
        }

        totalAmount = totalAmount.add(winAmount).subtract(betAmount);
        dataBySelf.setBetAmount(betAmount);
        dataBySelf.setWinAmount(winAmount);
        dataBySelf.setAccount(m.getAccount());
        dataBySelf.setCreateDate(m.getCreateDate());
        dataBySelf.setTest(m.getTest());
        dataBySelf.setTotalAmount(totalAmount);
        list.add(dataBySelf);
        List<User> userData = userDao.listUserAndSelfByParent(m.getAccount());
        for (Iterator iterator1 = userData.iterator(); iterator1.hasNext(); ) {
            User u = (User) iterator1.next();
            TeamInReport nextTeamData = new TeamInReport();
            BigDecimal nBetAmount = new BigDecimal("0");
            BigDecimal nWinAmount = new BigDecimal("0");
            BigDecimal nTotalAmount = new BigDecimal("0");
            if (!m.getAccount().equals(u.getAccount())) {
                Map<String, String> map = new HashMap();
                for (Iterator iterator2 = userList.iterator(); iterator2.hasNext(); ) {
                    User nut = (User) iterator2.next();
                    if (u.getAccount().equals(nut.getAccount())) {
                        String parentList = nut.getParentList();
                        for (Iterator iterator4 = userList.iterator(); iterator4.hasNext(); ) {
                            User nt = (User) iterator4.next();
                            if (!StrUtils.hasEmpty(new Object[]{parentList}) && nt.getParentList().startsWith(parentList))
                                map.put(nt.getAccount(), nt.getAccount());
                        }

                    }
                }

                for (Iterator iterator3 = dataList.iterator(); iterator3.hasNext(); ) {
                    TeamInReport nb = (TeamInReport) iterator3.next();
                    Object bt = map.get(nb.getAccount());
                    if (bt != null) {
                        nBetAmount = nBetAmount.add(nb.getBetAmount());
                        nWinAmount = nWinAmount.add(nb.getWinAmount());
                    }
                }

                nTotalAmount = nTotalAmount.add(nWinAmount).subtract(nBetAmount);
                if (nBetAmount.compareTo(new BigDecimal("0")) != 0) {
                    nextTeamData.setBetAmount(nBetAmount);
                    nextTeamData.setWinAmount(nWinAmount);
                    nextTeamData.setAccount(u.getAccount());
                    nextTeamData.setCreateDate(m.getCreateDate());
                    nextTeamData.setTest(m.getTest());
                    nextTeamData.setTotalAmount(nTotalAmount);
                    list.add(nextTeamData);
                }
            }
        }

        List page1 = new ArrayList();
        if (list.size() > 0) {
            PagerUtils pager = PagerUtils.create(list, p.getPageSize());
            p.setRowCount(list.size());
            page1 = pager.getPagedList(p.getNowPage());
        }
        return page1;

    }

    public int getCountByCreatDate(String createDate) {
        return this.teamInReportDao.getCountByCreatDate(createDate);
    }

    public boolean isExist(String account, String createTime) {
        return this.teamInReportDao.isExist(account, createTime);
    }

    public TeamInReport getNewCommonTeamReport(String account, String createTime) {
        return this.teamInReportDao.getNewCommonTeamReport(account, createTime);
    }

    public void save(TeamInReport m) {
        this.teamInReportDao.save(m);
    }

    public List<TeamInReport> adminNewHistoryStatistics(Page p, TeamInReport m, String startTime, String endTime) {
        return this.teamInReportDao.adminNewHistoryStatistics(p, m, startTime, endTime);
    }

    public List<TeamInReport> adminNewHistoryDetails(Page p, TeamInReport m, String startTime, String endTime) {
        return this.teamInReportDao.adminNewHistoryDetails(p, m, startTime, endTime);
    }

    public List<TeamInReportModel> getTodayProxyTeamData(TeamInReport m, String startDate, String endDate, int status) {
        List<TeamInReportModel> list = new ArrayList();

        List<User> allUserList = this.userDao.getAllUserList(m.getTest());

        List<TeamInReport> dataList = this.teamInReportDao.getTodayAllDataList(m);


        List<User> accountList = this.userDao.getAccountList(m.getTest());
        for (User u : accountList) {
            TeamInReportModel proxyTeamData = new TeamInReportModel();
            BigDecimal nBetAmount = new BigDecimal("0");
            BigDecimal nWinAmount = new BigDecimal("0");
            BigDecimal nTotalAmount = new BigDecimal("0");
            BigDecimal nMarginDollar = new BigDecimal("0");
            BigDecimal nMarginRatio = new BigDecimal("0");
            BigDecimal nProfitRatio = new BigDecimal("0");

            Map<String, String> map = new HashMap();
            for (User nut : allUserList) {
                if (u.getAccount().equals(nut.getAccount())) {
                    String parentList = nut.getParentList();
                    for (User nt : allUserList) {
                        if ((!StrUtils.hasEmpty(new Object[]{parentList})) && (nt.getParentList().startsWith(parentList))) {
                            map.put(nt.getAccount(), nt.getAccount());
                        }
                    }
                }
            }
            for (TeamInReport nb : dataList) {
                Object bt = map.get(nb.getAccount());
                if (bt != null) {
                    nBetAmount = nBetAmount.add(nb.getBetAmount());
                    nWinAmount = nWinAmount.add(nb.getWinAmount());
                }
            }
            nMarginDollar = nMarginDollar.add(nBetAmount).subtract(nWinAmount);

            nTotalAmount = nTotalAmount.add(nWinAmount).subtract(nBetAmount);
            if (nBetAmount.compareTo(new BigDecimal("0")) != 0) {
                if (nBetAmount.compareTo(new BigDecimal("0")) == 0) {
                    nMarginRatio = nMarginRatio.add(new BigDecimal("0"));
                    nProfitRatio = nProfitRatio.add(new BigDecimal("0"));
                } else {
                    nMarginRatio = nMarginRatio.add(nMarginDollar.divide(nBetAmount, 4, 0).multiply(new BigDecimal("100")));
                    nProfitRatio = nProfitRatio.add(nTotalAmount.divide(nBetAmount, 4, 0).multiply(new BigDecimal("100")));
                }
                proxyTeamData.setBetAmount(nBetAmount);
                proxyTeamData.setWinAmount(nWinAmount);
                proxyTeamData.setTotalAmount(nTotalAmount);
                proxyTeamData.setAccount(u.getAccount());
                proxyTeamData.setMarginDollar(nMarginDollar);
                proxyTeamData.setProfitRatio(nProfitRatio);
                proxyTeamData.setMarginRatio(nMarginRatio);
                proxyTeamData.setCreateDate(m.getCreateDate());
                proxyTeamData.setStartDate(startDate);
                proxyTeamData.setEndDate(endDate);
                proxyTeamData.setStatus(status);
                proxyTeamData.setTest(m.getTest());
                proxyTeamData.setCurDate(m.getCreateDate());
                list.add(proxyTeamData);
            }
        }
        return list;
    }

    public List<TeamInReport> list(Page p, TeamInReport m) {
        return this.teamInReportDao.list(p, m);
    }

    public List<TeamInReportModel> getTodayTeamData(TeamInReport m, String startDate, String endDate, Integer status) {
        List<TeamInReportModel> list = new ArrayList();
        List<User> userList = this.userDao.listByParent(m.getAccount());
        List<TeamInReport> dataList = this.teamInReportDao.getTodayDataListByTeam(m);

        TeamInReportModel dataBySelf = new TeamInReportModel();

        BigDecimal betAmount = new BigDecimal("0");

        BigDecimal winAmount = new BigDecimal("0");

        BigDecimal totalAmount = new BigDecimal("0");

        BigDecimal marginDollar = new BigDecimal("0");

        BigDecimal marginRatio = new BigDecimal("0");

        BigDecimal profitRatio = new BigDecimal("0");
        for (TeamInReport b : dataList) {
            betAmount = betAmount.add(b.getBetAmount());
            winAmount = winAmount.add(b.getWinAmount());
        }
        marginDollar = marginDollar.add(betAmount).subtract(winAmount);

        totalAmount = totalAmount.add(winAmount).subtract(betAmount);
        if (betAmount.compareTo(new BigDecimal("0")) == 0) {
            marginRatio = marginRatio.add(new BigDecimal("0"));
            profitRatio = profitRatio.add(new BigDecimal("0"));
        } else {
            marginRatio = marginRatio.add(marginDollar.divide(betAmount, 4, 0).multiply(new BigDecimal("100")));
            profitRatio = profitRatio.add(totalAmount.divide(betAmount, 4, 0).multiply(new BigDecimal("100")));
        }
        dataBySelf.setBetAmount(betAmount);
        dataBySelf.setWinAmount(winAmount);
        dataBySelf.setTotalAmount(totalAmount);
        dataBySelf.setAccount(m.getAccount());
        dataBySelf.setMarginDollar(marginDollar);
        dataBySelf.setProfitRatio(profitRatio);
        dataBySelf.setMarginRatio(marginRatio);

        dataBySelf.setCreateDate(m.getCreateDate());
        dataBySelf.setStartDate(startDate);
        dataBySelf.setEndDate(endDate);
        dataBySelf.setStatus(status.intValue());
        dataBySelf.setTest(m.getTest());
        dataBySelf.setCurDate(m.getCreateDate());

        list.add(dataBySelf);
        List<User> userData = this.userDao.listUserAndSelfByParent(m.getAccount());
        for (User u : userData) {
            TeamInReportModel nextTeamData = new TeamInReportModel();
            BigDecimal nBetAmount = new BigDecimal("0");
            BigDecimal nWinAmount = new BigDecimal("0");
            BigDecimal nTotalCount = new BigDecimal("0");
            BigDecimal nMarginDollar = new BigDecimal("0");
            BigDecimal nMarginRatio = new BigDecimal("0");
            BigDecimal nProfitRatio = new BigDecimal("0");
            if (!m.getAccount().equals(u.getAccount())) {
                Map<String, String> map = new HashMap();
                for (User nut : userList) {
                    if (u.getAccount().equals(nut.getAccount())) {
                        String parentList = nut.getParentList();
                        for (User nt : userList) {
                            if ((!StrUtils.hasEmpty(new Object[]{parentList})) && (nt.getParentList().startsWith(parentList))) {
                                map.put(nt.getAccount(), nt.getAccount());
                            }
                        }
                    }
                }
                for (TeamInReport nb : dataList) {
                    Object bt = map.get(nb.getAccount());
                    if (bt != null) {
                        nBetAmount = nBetAmount.add(nb.getBetAmount());
                        nWinAmount = nWinAmount.add(nb.getWinAmount());
                    }
                }
                nMarginDollar = nMarginDollar.add(nBetAmount).subtract(nWinAmount);

                nTotalCount = nTotalCount.add(nWinAmount).subtract(nBetAmount);
                if (nBetAmount.compareTo(new BigDecimal("0")) != 0) {
                    if (nBetAmount.compareTo(new BigDecimal("0")) == 0) {
                        nMarginRatio = nMarginRatio.add(new BigDecimal("0"));
                        nProfitRatio = nProfitRatio.add(new BigDecimal("0"));
                    } else {
                        nMarginRatio = nMarginRatio.add(nMarginDollar.divide(nBetAmount, 4, 0).multiply(new BigDecimal("100")));
                        nProfitRatio = nProfitRatio.add(nTotalCount.divide(nBetAmount, 4, 0).multiply(new BigDecimal("100")));
                    }
                    nextTeamData.setBetAmount(nBetAmount);
                    nextTeamData.setWinAmount(nWinAmount);
                    nextTeamData.setTotalAmount(nTotalCount);
                    nextTeamData.setAccount(u.getAccount());
                    nextTeamData.setMarginDollar(nMarginDollar);
                    nextTeamData.setProfitRatio(nProfitRatio);
                    nextTeamData.setMarginRatio(nMarginRatio);
                    nextTeamData.setCreateDate(m.getCreateDate());
                    nextTeamData.setStartDate(startDate);
                    nextTeamData.setEndDate(endDate);
                    nextTeamData.setStatus(status.intValue());
                    nextTeamData.setTest(m.getTest());
                    nextTeamData.setCurDate(m.getCreateDate());

                    list.add(nextTeamData);
                }
            }
        }
        return list;
    }

    public boolean createTeamData(String date) {
        boolean successFalg = false;
        if (this.teamInReportDao.getCountByCreatDate(date) < 1) {
            List<UserInReport> userList = this.userInReportDao.getAccountByCreateTime(date);
            for (UserInReport user : userList) {
                String account = user.getAccount();
                User u = null;
                do {
                    u = this.userDao.findByAccount(account);
                    if (this.teamInReportDao.isExist(account, date)) {
                        break;
                    }
                    TeamInReport superiorTeamReport = this.teamInReportDao.getNewCommonTeamReport(account, date);
                    this.teamInReportDao.save(superiorTeamReport);
                    successFalg = true;


                    account = u.getParentAccount();
                } while (!u.getAccount().equals(u.getParentAccount()));
            }
        }
        return successFalg;
    }

    public void createTeamInDatas(String date) {
        if (this.teamInReportDao.getCountByCreatDate(date) < 1) {
            List<UserInReport> userList = this.userInReportDao.getAccountByCreateTime(date);
            for (UserInReport user : userList) {
                String account = user.getAccount();
                User u = null;
                do {
                    u = this.userDao.findByAccount(account);
                    if (this.teamInReportDao.isExist(account, date)) {
                        break;
                    }
                    TeamInReport superiorTeamReport = this.teamInReportDao.getNewCommonTeamReport(account, date);
                    this.teamInReportDao.save(superiorTeamReport);


                    account = u.getParentAccount();
                } while (!u.getAccount().equals(u.getParentAccount()));
            }
        }
    }
}
