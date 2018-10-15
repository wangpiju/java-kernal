package com.hs3.service.report;

import com.hs3.dao.report.UserInReportDao;
import com.hs3.entity.report.UserInReport;
import com.hs3.utils.DateUtils;
import com.hs3.utils.sys.WebDateUtils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userInReportService")
public class UserInReportService {
    @Autowired
    private UserInReportDao userInReportDao;

    public List<UserInReport> getAccountByCreateTime(String createTime) {
        return this.userInReportDao.getAccountByCreateTime(createTime);
    }

    public boolean createUserInReport(String createTime) {
        boolean falg = false;
        if (this.userInReportDao.getCountByCreatDate(createTime) < 1) {
            Date startTime = DateUtils.addDay(new Date(), -1);
            List<UserInReport> userReportList = this.userInReportDao.getListByCreateTime(DateUtils.format(WebDateUtils.getDayBeginTime(startTime)),
                    DateUtils.format(WebDateUtils.getDayEndTime(startTime)));
            for (UserInReport m : userReportList) {
                falg = this.userInReportDao.save(m);
            }
        }
        return falg;
    }
}
