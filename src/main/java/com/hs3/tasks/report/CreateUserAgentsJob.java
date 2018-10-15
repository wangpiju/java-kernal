package com.hs3.tasks.report;

import com.hs3.service.user.UserAgentsService;
import com.hs3.utils.DateUtils;
import com.hs3.web.utils.SpringContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class CreateUserAgentsJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(CreateNewReportJob.class);
    private UserAgentsService userAgentsService = (UserAgentsService) SpringContext.getBean(UserAgentsService.class);

    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        try {
            logger.info("日工资派发计算任务启动！！！！");
            this.userAgentsService.addUserAgentsDailyWhenNotExists(DateUtils.formatDate(DateUtils.addDay(new Date(), -1)), false);
        } catch (Exception e) {
            logger.error("日工资派发计算任务异常！" + e.getMessage(), e);
        } finally {
            logger.info("日工资派发计算任务结束！！！！");
        }


        try {
            logger.info("日工资彩种加奖计算任务启动！！！！");
            this.userAgentsService.addUserAgentsDailyLotteryWhenNotExists(DateUtils.formatDate(DateUtils.addDay(new Date(), -1)), false);
        } catch (Exception e) {
            logger.error("日工资彩种加奖计算任务异常！" + e.getMessage(), e);
        } finally {
            logger.info("日工资彩种加奖计算任务结束！！！！");
        }


        try {
            logger.info("周期分红派发计算任务启动！！！！");
            this.userAgentsService.addUserAgentsDividendWhenNotExists(DateUtils.toDate(DateUtils.format(DateUtils.addDay(new Date(), -1), "yyyy-MM-dd HH:mm:ss")));
        } catch (Exception e) {
            logger.error("周期分红派发计算任务异常！" + e.getMessage(), e);
        } finally {
            logger.info("周期分红派发计算任务结束！！！！");
        }

        try {
            logger.info("周期分红彩种加奖计算任务启动！！！！");
            this.userAgentsService.addUserAgentsDividendLotteryWhenNotExists(DateUtils.toDate(DateUtils.format(DateUtils.addDay(new Date(), -1), "yyyy-MM-dd HH:mm:ss")));
        } catch (Exception e) {
            logger.error("周期分红彩种加奖计算任务异常！" + e.getMessage(), e);
        } finally {
            logger.info("周期分红彩种加奖计算任务结束！！！！");
        }




    }

}
