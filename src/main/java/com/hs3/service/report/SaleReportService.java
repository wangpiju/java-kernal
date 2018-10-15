package com.hs3.service.report;

import com.hs3.dao.report.SaleReportDao;
import com.hs3.models.report.SaleReport;
import com.hs3.models.report.SaleSeasonReport;
import com.hs3.utils.sys.WebDateUtils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("saleReportService")
public class SaleReportService {
    @Autowired
    private SaleReportDao saleReportDao;

    public List<SaleReport> getReport(Date begin, Date end, String lotteryId, Integer test) {
        begin = WebDateUtils.getBeginTime(begin);
        end = WebDateUtils.getEndTime(end);
        return this.saleReportDao.getReport(begin, end, lotteryId, test);
    }

    public List<SaleSeasonReport> getSeasonReport(Date date, String lotteryId, Integer test) {
        Date begin = WebDateUtils.getBeginTime(date);
        Date end = WebDateUtils.getEndTime(date);
        return this.saleReportDao.getSeasonReport(begin, end, lotteryId, test);
    }
}
