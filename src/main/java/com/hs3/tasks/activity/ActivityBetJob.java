package com.hs3.tasks.activity;

import com.hs3.service.activity.ActivityBetService;
import com.hs3.utils.DateUtils;
import com.hs3.web.utils.SpringContext;

import java.util.Date;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ActivityBetJob
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ActivityBetJob.class);
    private ActivityBetService activityBetService = (ActivityBetService) SpringContext.getBean("activityBetService");

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        boolean ok = false;
        int times = 0;
        do {
            ok = false;
            try {
                logger.info("生成投注佣金任务启动！！！！");


                this.activityBetService.updateAmountByBet(DateUtils.getToDay(-1), new Date());
                logger.info(DateUtils.formatDate(DateUtils.getToDay(-1)) + "的投注佣金已经下发。不重复执行！");
            } catch (Exception e) {
                times++;
                ok = true;
                try {
                    Thread.sleep(300000L);
                } catch (InterruptedException localInterruptedException) {
                }
                logger.error("投注佣金：次数" + times + e.getMessage(), e);
            } finally {
                logger.info("生成投注佣金任务结束！！！！");
            }
        } while ((ok) && (times < 10));
    }
}
