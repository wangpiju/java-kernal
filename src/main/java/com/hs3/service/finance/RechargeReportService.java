package com.hs3.service.finance;

import com.hs3.dao.finance.RechargeReportDao;
import com.hs3.db.Page;
import com.hs3.entity.finance.RechargeReport;
import com.hs3.exceptions.UnLogException;
import com.hs3.utils.DateUtils;
import com.hs3.utils.sys.WebDateUtils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("rechargeReportService")
public class RechargeReportService {
    @Autowired
    private RechargeReportDao rechargeReportDao;

    public int createRecharge(Date date) {
        if (this.rechargeReportDao.count(date) == 0) {
            Date begin = WebDateUtils.getDayBeginTime(date);
            Date end = WebDateUtils.getDayEndTime(date);

            List<RechargeReport> list = this.rechargeReportDao.totalRecharge(begin, end);
            for (RechargeReport d : list) {
                d.setCreateDate(begin);
                this.rechargeReportDao.save(d);
            }
            return list.size();
        }
        throw new UnLogException("日期" + DateUtils.formatDate(date) + "的数据已经存在");
    }

    public List<RechargeReport> list(Date begin, Date end, String receiveName, String receiveCard, Page p) {
        return this.rechargeReportDao.list(begin, end, receiveName, receiveCard, p);
    }
}
