package com.hs3.tasks.lotts;

import com.hs3.service.lotts.LotteryJobService;
import com.hs3.web.utils.SpringContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LotteryManagerJob
        implements Job {
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        LotteryJobService service = (LotteryJobService) SpringContext.getBean("lotteryJobService");
        service.updateAllJob();
    }
}
