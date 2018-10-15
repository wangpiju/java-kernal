package com.hs3.service.report;

import com.hs3.dao.report.WinLoseRankReportDao;
import com.hs3.db.Page;
import com.hs3.models.report.WinLoseRank;
import com.hs3.utils.sys.WebDateUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("WinLoseRankService")
public class WinLoseRankService {
    @Autowired
    private WinLoseRankReportDao winLoseRankReportDao;

    public List<WinLoseRank> list(Page page, String historyAndNow, String account, BigDecimal count, Integer order, Integer test) {
        Date startDate = WebDateUtils.getBeginTime(new Date());
        Date endDate = new Date();
        return this.winLoseRankReportDao.list(page, account, count, order, test, startDate, endDate);
    }

    public List<WinLoseRank> historyList(Page page, String historyAndNow, String account, BigDecimal count, Integer order, Date startTime, Date endTime, Integer test) {
        return this.winLoseRankReportDao.historyList(page, account, count, order, startTime, endTime, test);
    }
}
