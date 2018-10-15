package com.hs3.service.third;

import com.hs3.utils.AssembleHttpUtils;
import com.hs3.utils.ResponseData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-07-03 17:34
 **/
@Service
public class DhThirdService {

    private AssembleHttpUtils assembleHttpUtils = AssembleHttpUtils.instance();

    @Value("#{prop['dhPrizeHost'] ?:'http://localhost:8081'}")
    private String dhHost;

    /*------------------------------------------------- quartzService -------------------------------------------------*/

    public void quartzAddJobCron(String jobName, String groupName, String cronExpression, String jobClassName) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_addJobCron,
                new Object[]{jobName, groupName, cronExpression, jobClassName});
    }


    public void quartzAddNewJobCron(String jobName, String groupName, String cronExpression, String jobClassName, String map) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_addNewJobCron,
                new Object[]{jobName, groupName, cronExpression, jobClassName, map});
    }


    public void quartzAddJobWithStartEnd(String jobName, String groupName, String map, String begin, String end, int intervalInSeconds, String jobClassName) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_addJobWithStartEnd,
                new Object[]{jobName, groupName, map, begin, end, intervalInSeconds, jobClassName});
    }


    public boolean quartzAddNewJobWithStartEnd(String jobName, String groupName, String map, String begin, String end, int intervalInSeconds, String jobClassName) {
        ResponseData responseData = assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_addNewJobWithStartEnd,
                new Object[]{jobName, groupName, map, begin, end, intervalInSeconds, jobClassName});
        return responseData.isFlag() && Boolean.parseBoolean(responseData.getResult());
    }


    public void quartzPauseJob(String jobName, String groupName) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_pauseJob,
                new Object[]{jobName, groupName});
    }


    public void quartzResumeJob(String jobName, String groupName) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_resumeJob,
                new Object[]{jobName, groupName});
    }


    public void quartzDeleteJob(String jobName, String groupName) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_deleteJob,
                new Object[]{jobName, groupName});
    }


    public int quartzGetCurrentlyExecutingNums() {
        ResponseData responseData = assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_getCurrentlyExecutingNums, new Object[]{});
        if (responseData.isFlag()) {
            return Integer.parseInt(responseData.getResult());
        }
        return 0;
    }


    public boolean quartzIsStarting() {
        ResponseData responseData = assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_isStarting, new Object[]{});
        return responseData.isFlag() && Boolean.parseBoolean(responseData.getResult());
    }


    public int quartzStartJobs() {
        ResponseData responseData = assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_startJobs, new Object[]{});
        if (responseData.isFlag()) {
            return Integer.parseInt(responseData.getResult());
        }
        return 0;
    }


    public int quartzShutdownJobs() {
        ResponseData responseData = assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_shutdownJobs, new Object[]{});
        if (responseData.isFlag()) {
            return Integer.parseInt(responseData.getResult());
        }
        return 0;
    }


    /*------------------------------------------------- quartzManager -------------------------------------------------*/

    public void managerAddJob(String jobName, String claName, String time) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_addJob,
                new Object[]{jobName, claName, time});
    }


    public void managerManagerAddJobCron(String jobName, String groupName, String cronExpression, String jobClassName) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_addJobCron,
                new Object[]{jobName, groupName, cronExpression, jobClassName});
    }


    public void managerAddJobTrigger(String jobGroupName, String jobName, String triggerGroupName, String triggerName, String jobClassName, String time) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_addJobTrigger,
                new Object[]{jobGroupName, jobName, triggerGroupName, triggerName, jobClassName, time});
    }


    public void managerAddJobTriggerEx(String jobGroupName, String jobName, String triggerGroupName, String triggerName, String jobClassName, String time, String map) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_addJobTriggerEx,
                new Object[]{jobGroupName, jobName, triggerGroupName, triggerName, jobClassName, time, map});
    }


    public void managerModifyJobTimeEx(String jobName, String triggerName, String time) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_modifyJobTimeEx, new Object[]{jobName, triggerName, time});
    }


    public void managerModifyJobTime(String triggerGroupName, String triggerName, String time) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_modifyJobTime,
                new Object[]{triggerGroupName, triggerName, time});
    }


    public void managerRemoveJobWithName(String jobName) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_removeJobWithName, new Object[]{jobName});
    }


    public void managerRemoveJobWithGroup(String jobName, String groupName) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_removeJobWithGroup, new Object[]{jobName, groupName});
    }


    public void managerRemoveJobWithTrigger(String jobGroupName, String jobName, String triggerGroupName, String triggerName) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_removeJobWithTrigger,
                new Object[]{jobGroupName, jobName, triggerGroupName, triggerName});
    }

    public void managerStartJobs() {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_startJobs, new Object[]{});
    }


    public void managerShutdownJobs() {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_shutdownJobs, new Object[]{});
    }


    public void managerPauseJob(String groupName, String name) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_pauseJob, new Object[]{groupName, name});
    }


    public void managerPauseTrigger(String triggerGroup, String triggerName) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_pauseTrigger, new Object[]{triggerGroup, triggerName});
    }


    public void managerResumeJob(String groupName, String name) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_resumeJob, new Object[]{groupName, name});
    }


    public void managerResumeTrigger(String triggerGroup, String triggerName) {
        assembleHttpUtils.execRequest(dhHost, AssembleHttpUtils.SendReq.quartz_manager_resumeTrigger, new Object[]{triggerGroup, triggerName});
    }
}
