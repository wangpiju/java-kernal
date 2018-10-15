package com.hs3.tasks.sys;

import com.hs3.entity.sys.SysClear;
import com.hs3.service.sys.SysClearService;
import com.hs3.utils.StrUtils;
import com.hs3.web.auth.ThreadLog;
import com.hs3.web.utils.SpringContext;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SysClearJob
        implements Job {
    private static final int DEFAULT_LIMIT = 500;

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        ThreadLog.begin("SysClearJob.execute");
        SysClear sysClear = (SysClear) arg0.getJobDetail().getJobDataMap().get("sys_clear");
        if (sysClear != null) {
            doClear(sysClear);
        }
        ThreadLog.end();
    }

    protected void doClear(SysClear sysClear) {
        SysClearService sysClearService = (SysClearService) SpringContext.getBean(SysClearService.class);
        int limit = StrUtils.hasEmpty(new Object[]{sysClear.getExtends1()}) ? 500 : Integer.parseInt(sysClear.getExtends1());

        SysClearJobEnum sysClearJobEnum = SysClearJobFactory.getSysClearJobEnum(sysClear.getJob());

        long endTime = new Date().getTime() + 7200000L;

        int count = 0;
        int delete;
        do {
            delete = sysClearService.deleteForClear(sysClearJobEnum.getTable(), sysClearJobEnum.getColumn(), sysClearJobEnum.getOrder(), sysClearJobEnum.getObj(sysClear), limit, sysClear.getClearMode().intValue() == 2);
            count += delete;
        } while ((delete == limit) && (new Date().getTime() <= endTime));
        ThreadLog.log(sysClear.getTitle() + "," + sysClear.getClearMode() + "," + sysClear.getBeforeDays() + "," + sysClear.getBeforeDaysDefault() + " delete records:" + count);
    }
}
