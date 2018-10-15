package com.hs3.service.report;

import com.hs3.dao.report.UserReportDao;
import com.hs3.db.Page;
import com.hs3.entity.report.UserReport;
import com.hs3.utils.DateUtils;
import com.hs3.utils.sys.WebDateUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userReportService")
public class UserReportService {
    @Autowired
    private UserReportDao userReportDao;

    private UserReport getMergeUserReport(UserReport... ur) {
        UserReport rel = new UserReport();

        rel.setActivityAndSend(BigDecimal.ZERO);
        rel.setActualSaleAmount(BigDecimal.ZERO);
        rel.setBetAmount(BigDecimal.ZERO);
        rel.setCount(BigDecimal.ZERO);
        rel.setDrawingAmount(BigDecimal.ZERO);
        rel.setJuniorRebateAmount(BigDecimal.ZERO);
        rel.setRebateAmount(BigDecimal.ZERO);
        rel.setRechargeAmount(BigDecimal.ZERO);
        rel.setTigerWinAmount(BigDecimal.ZERO);
        rel.setWinAmount(BigDecimal.ZERO);
        rel.setWages(BigDecimal.ZERO);
        for (UserReport u : ur) {
            if ((u != null) && (u.getAccount() != null)) {
                rel.setActivityAndSend(u.getActivityAndSend().add(rel.getActivityAndSend()));
                rel.setActualSaleAmount(u.getActualSaleAmount().add(rel.getActualSaleAmount()));
                rel.setBetAmount(u.getBetAmount().add(rel.getBetAmount()));
                rel.setCount(u.getCount().add(rel.getCount()));
                rel.setDrawingAmount(u.getDrawingAmount().add(rel.getDrawingAmount()));
                rel.setJuniorRebateAmount(u.getJuniorRebateAmount().add(rel.getJuniorRebateAmount()));
                rel.setRebateAmount(u.getRebateAmount().add(rel.getRebateAmount()).add(u.getJuniorRebateAmount()));
                rel.setRechargeAmount(u.getRechargeAmount().add(rel.getRechargeAmount()));
                rel.setTigerWinAmount(u.getTigerWinAmount().add(rel.getTigerWinAmount()));
                rel.setWinAmount(u.getWinAmount().add(rel.getWinAmount()));
                rel.setWages(u.getWages().add(rel.getWages()));
            }
        }
        return rel;
    }

    public boolean save(UserReport m) {
        return this.userReportDao.save(m);
    }

    public List<UserReport> listTigerWin(String createDate, int limit) {
        return this.userReportDao.listTigerWin(createDate, limit);
    }

    public List<UserReport> adminList(Page p, String account, String startDate, String endDate) {
        return this.userReportDao.adminList(p, account, startDate, endDate);
    }

    public UserReport selfReport(String account, Date startTime, Date endTime, Integer test) {
        startTime = DateUtils.getDate(startTime);
        endTime = DateUtils.AddSecond(DateUtils.getDate(endTime), 86399);

        UserReport userReport = this.userReportDao.totalBySelf(account, startTime, endTime, test);
        Date curDate = this.userReportDao.getMaxDate();
        if (curDate == null) {
            curDate = startTime;
        } else if (curDate.getTime() < startTime.getTime()) {
            curDate = startTime;
        } else {
            curDate = DateUtils.addDay(curDate, 1);
        }
        UserReport curUserReport = null;
        if (curDate.getTime() < endTime.getTime()) {
            curUserReport = this.userReportDao.getOne(account, curDate, endTime, test);
        }
        userReport = getMergeUserReport(new UserReport[]{userReport, curUserReport});
        return userReport;
    }

    public boolean createUserReportData(Date createDate) {
        boolean falg = false;
        if (this.userReportDao.getCountByCreatDate(DateUtils.getDate(createDate)) < 1) {
            Date startDate = WebDateUtils.getDayBeginTime(createDate);

            Date endDate = WebDateUtils.getDayEndTime(createDate);

            List<UserReport> data = this.userReportDao.getList(null, startDate, endDate, 0, null);
            for (UserReport m : data) {
                falg = this.userReportDao.save(m);
            }
        }
        return falg;
    }

    public int deleteByCreateDate(Date createDate) {
        return this.userReportDao.deleteByCreateDate(createDate);
    }
}
