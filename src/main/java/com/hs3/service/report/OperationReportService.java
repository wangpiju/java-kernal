package com.hs3.service.report;

import com.hs3.dao.report.OperationReportDao;
import com.hs3.db.Page;
import com.hs3.entity.report.OperationReport;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.utils.DateUtils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("operationReportService")
public class OperationReportService {
    @Autowired
    private OperationReportDao operationReportDao;

    public List<OperationReport> list(Date reportDateBegin, Date reportDateEnd, Page page)
            throws BaseCheckException {
        if ((reportDateBegin == null) || (reportDateEnd == null)) {
            throw new BaseCheckException("统计时间不能为空！");
        }
        if (reportDateEnd.before(reportDateBegin)) {
            throw new BaseCheckException("统计时间起不能大于统计时间止！");
        }
        String begin = DateUtils.format(reportDateBegin, "yyyy-MM-dd");
        String end = DateUtils.format(reportDateEnd, "yyyy-MM-dd");

        return this.operationReportDao.listByCond(begin, end, page);
    }

    public void addWhenNotExists(String reportDate) {
        if (this.operationReportDao.listByCond(reportDate, reportDate, null).isEmpty()) {
            add(reportDate);
        }
    }

    public void add(String reportDate) {
        this.operationReportDao.delete(reportDate);
        OperationReport m = this.operationReportDao.find(reportDate);
        this.operationReportDao.save(m);
    }

    public OperationReport find(Date reportDate) {
        return this.operationReportDao.find(DateUtils.format(reportDate, "yyyy-MM-dd"));
    }

    public OperationReport find(String reportDate) {
        return this.operationReportDao.find(reportDate);
    }

    public void save(OperationReport m) {
        this.operationReportDao.save(m);
    }

    public List<OperationReport> list(Page p) {
        return this.operationReportDao.list(p);
    }
}
