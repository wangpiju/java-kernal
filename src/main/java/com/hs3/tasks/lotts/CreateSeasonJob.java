package com.hs3.tasks.lotts;

import com.hs3.entity.lotts.Lottery;
import com.hs3.service.lotts.CreateSeasonService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.web.utils.SpringContext;

import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CreateSeasonJob
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(CreateSeasonJob.class);

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        CreateSeasonService reateSeasonService = (CreateSeasonService) SpringContext.getBean("createSeasonService");
        LotteryService lotteryService = (LotteryService) SpringContext.getBean("lotteryService");

        List<Lottery> lotterys = lotteryService.list(null);
        for (Lottery lottery : lotterys) {
            try {
                if (!lottery.getId().equals("jnd3_5")) {
                    reateSeasonService.saveSeasonAuto(lottery);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
