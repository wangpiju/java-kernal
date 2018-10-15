package com.hs3.tasks.user;

import com.hs3.service.user.UserService;
import com.hs3.web.utils.SpringContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ClearUserDepositJob
        implements Job {
    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        ((UserService) SpringContext.getBean(UserService.class)).updateUserClear();
    }
}
