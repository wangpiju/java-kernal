package com.hs3.tasks.report;

import com.hs3.service.report.TeamInReportService;
import com.hs3.service.report.UserInReportService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.sys.WebDateUtils;
import com.hs3.web.utils.SpringContext;

import java.util.Date;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CreateInReportJob
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(CreateInReportJob.class);
    private UserInReportService userInReportService = (UserInReportService) SpringContext.getBean(UserInReportService.class);
    private TeamInReportService teamInReportService = (TeamInReportService) SpringContext.getBean(TeamInReportService.class);

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        try {
            logger.info("创建彩中彩个人盈亏报表任务启动！！！！");
            this.userInReportService.createUserInReport(DateUtils.format(WebDateUtils.getCurTime(new Date())));
        } catch (Exception e) {
            logger.error("创建彩中彩个人盈亏报表异常！" + e.getMessage(), e);
        } finally {
            logger.info("创建彩中彩个人盈亏报表任务结束！！！！");
        }
        try {
            logger.info("创建彩中彩团队盈亏报表任务启动！！！！");
            this.teamInReportService.createTeamInDatas(DateUtils.format(WebDateUtils.getCurTime(new Date())));
        } catch (Exception e) {
            logger.error("创建彩中彩团队盈亏报表异常！" + e.getMessage(), e);
        } finally {
            logger.info("创建彩中彩团队盈亏报表任务结束！！！！");
        }
    }
}
