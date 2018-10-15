package com.hs3.tasks.contract;

import com.hs3.service.contract.ContractBonusService;
import com.hs3.utils.DateUtils;
import com.hs3.web.utils.SpringContext;

import java.util.Date;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class GenerateBonusJob
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(GenerateBonusJob.class);
    private ContractBonusService contractBonusService = (ContractBonusService) SpringContext.getBean("contractBonusService");

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        try {
            logger.info("生成契约分红任务开始！！！！");
            if ((DateUtils.getDayNum(new Date()) == 1) || (DateUtils.getDayNum(new Date()) == 16)) {
                if (!this.contractBonusService.isExistContractBonus(DateUtils.getDate(DateUtils.getToDay(-1)))) {
                    this.contractBonusService.createBonus(new Date());
                }
            }
        } catch (Exception e) {
            logger.error("生成契约分红任务异常！" + e.getMessage(), e);
        } finally {
            logger.info("生成契约分红任务结束！！！！");
        }
    }
}
