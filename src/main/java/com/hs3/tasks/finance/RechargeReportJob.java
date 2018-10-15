package com.hs3.tasks.finance;

import com.hs3.service.finance.RechargeReportService;
import com.hs3.utils.DateUtils;
import com.hs3.web.utils.SpringContext;

import java.util.Date;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RechargeReportJob
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(RechargeReportJob.class);
    private static RechargeReportService rechargeReportService = (RechargeReportService) SpringContext.getBean(RechargeReportService.class);

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        try {
            int n = rechargeReportService.createRecharge(DateUtils.addDay(new Date(), -1));
            logger.info("充值统计任务生成：" + n + "条记录");
        } catch (Exception e) {
            logger.error("充值统计任务异常:" + e.getMessage(), e);
        }
    }
}
