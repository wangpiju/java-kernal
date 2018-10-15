package com.hs3.tasks.activity;

import com.hs3.service.activity.ActivityLossService;
import com.hs3.utils.DateUtils;
import com.hs3.web.utils.SpringContext;

import java.util.Date;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ActivityLossJob
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ActivityLossJob.class);
    private ActivityLossService activityLossService = (ActivityLossService) SpringContext.getBean("activityLossService");

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        boolean ok = false;
        int times = 0;
        do {
            ok = false;
            try {
                logger.info("生成亏损佣金任务启动！！！！");


                this.activityLossService.updateAmountByLoss(DateUtils.getToDay(-1), new Date());
                logger.info(DateUtils.formatDate(DateUtils.getToDay(-1)) + "的消费佣金已经下发。不重复执行！");
            } catch (Exception e) {
                times++;
                ok = true;
                try {
                    Thread.sleep(300000L);
                } catch (InterruptedException localInterruptedException) {
                }
                logger.error("亏损佣金：次数" + times + e.getMessage(), e);
            } finally {
                logger.info("生成亏损佣金任务结束！！！！");
            }
        } while ((ok) && (times < 10));
    }
}
