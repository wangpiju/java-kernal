package com.hs3.tasks.report;

import com.hs3.service.newReport.CpsReportService;
import com.hs3.utils.DateUtils;
import com.hs3.web.utils.SpringContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class CreateNewReportJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(CreateNewReportJob.class);
    private CpsReportService cpsReportService = (CpsReportService) SpringContext.getBean(CpsReportService.class);

    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        try {
            logger.info("生成综合报表任务启动！！！！");
            this.cpsReportService.addWhenNotExists(DateUtils.format(DateUtils.addDay(new Date(), -1), "yyyy-MM-dd"));
        } catch (Exception e) {
            logger.error("生成综合报表任务异常！" + e.getMessage(), e);
        } finally {
            logger.info("生成综合报表任务结束！！！！");
        }

    }

}
