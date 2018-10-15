package com.hs3.tasks.contract;

import com.hs3.entity.contract.ContractConfig;
import com.hs3.service.contract.ContractBadrecordService;
import com.hs3.service.contract.ContractBonusService;
import com.hs3.service.contract.ContractConfigService;
import com.hs3.utils.DateUtils;
import com.hs3.web.utils.SpringContext;

import java.util.Date;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CheckContractBonusJob
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(CheckContractBonusJob.class);
    private ContractBonusService contractBonusService = (ContractBonusService) SpringContext.getBean("contractBonusService");
    private ContractConfigService contractConfigService = (ContractConfigService) SpringContext.getBean("contractConfigService");
    private ContractBadrecordService contractBadrecordService = (ContractBadrecordService) SpringContext.getBean("contractBadrecordService");

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        ContractConfig config = this.contractConfigService.findEntity();
        int n = config.getDayNum().intValue() + 1;
        Date endDate = DateUtils.getDate(DateUtils.addDay(new Date(), -n));
        try {
            logger.info("检查契约分红逾期派发任务启动！！！！");
            if (!this.contractBadrecordService.isExistRecord(endDate)) {
                this.contractBonusService.createToCheck(endDate);
            }
        } catch (Exception e) {
            logger.error("检查契约分红逾期派发任务异常！" + e.getMessage(), e);
        } finally {
            logger.info("检查契约分红逾期派发任务结束！！！！");
        }
    }
}
