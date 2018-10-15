package com.hs3.service.report;

import com.hs3.dao.report.SeasonReportDao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("seasonReportService")
public class SeasonReportService {
    @Autowired
    private SeasonReportDao seasonReportDao;

    public List<Map<String, Object>> listLost(String lotteryId, Integer num) {
        return this.seasonReportDao.listLost(lotteryId, null, num.intValue());
    }

    public List<Map<String, Object>> listLost(String lotteryId, String seasonId, Integer num) {
        return this.seasonReportDao.listLost(lotteryId, seasonId, num.intValue());
    }

    public List<Map<String, Object>> listHost(String lotteryId, Integer num, Integer len) {
        return this.seasonReportDao.listHost(lotteryId, num.intValue(), len.intValue());
    }

    public List<Map<String, Object>> listLostTiger(String account, Integer num) {
        return this.seasonReportDao.listLostTiger(account, num.intValue());
    }

    public List<Map<String, Object>> listHostTiger(String account, Integer num, Integer len) {
        return this.seasonReportDao.listHostTiger(account, num.intValue(), len.intValue());
    }
}
