package com.hs3.tasks.lotts;

import com.hs3.entity.lotts.LotterySeason;
import com.hs3.service.quartz.QuartzManagerService;
import com.hs3.service.quartz.TaskLogic;
import com.hs3.utils.DateUtils;
import com.hs3.web.utils.SpringContext;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

public class CancelOrderJob
        implements Job {
    private TaskLogic taskLogic = (TaskLogic) SpringContext.getBean("taskLogic");
    private QuartzManagerService quartzManagerService = (QuartzManagerService) SpringContext.getBean("quartzManagerService");

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        JobDataMap jobDataMap = arg0.getJobDetail().getJobDataMap();
        if (DateUtils.addMinute((Date) jobDataMap.get("startDateTime"), 3).getTime() < new Date().getTime()) {
            this.quartzManagerService.removeJob(arg0.getJobDetail().getKey().getName(), arg0.getJobDetail().getKey().getGroup(), arg0.getTrigger()
                    .getKey().getGroup(), arg0.getTrigger().getKey().getName());
        } else {
            this.taskLogic.saveAdvanceOpenTask((LotterySeason) jobDataMap.get("lotterySeason"));
        }
    }
}
