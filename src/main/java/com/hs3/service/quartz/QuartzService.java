package com.hs3.service.quartz;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class QuartzService {
    @Autowired
    private Scheduler quartzScheduler;
    private static final Logger logger = LoggerFactory.getLogger(QuartzService.class);

    public void addJob(String jobName, String groupName, String cronExpression, Class<? extends Job> jobClass) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
        try {
            CronTrigger trigger = (CronTrigger) this.quartzScheduler.getTrigger(triggerKey);
            if (trigger == null) {
                JobDetail jobDetail = JobBuilder.newJob(jobClass)
                        .withIdentity(jobName, groupName).build();

                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

                trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).withSchedule(scheduleBuilder).build();
                this.quartzScheduler.scheduleJob(jobDetail, trigger);
            } else {
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

                this.quartzScheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        CronTrigger trigger;
    }

    public void addNewJob(String jobName, String groupName, String cronExpression, Class<? extends Job> jobClass, Map<String, Object> map) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, groupName).build();
            jobDetail.getJobDataMap().putAll(map);

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).withSchedule(scheduleBuilder).build();
            this.quartzScheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void addJob(String jobName, String groupName, Map<String, Object> map, Date begin, Date end, int intervalInSeconds, Class<? extends Job> jobClass) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);

        try {
            SimpleTrigger trigger = (SimpleTrigger) this.quartzScheduler.getTrigger(triggerKey);

            if (trigger == null) {
                JobDetail jobDetail = JobBuilder.newJob(jobClass)
                        .withIdentity(jobName, groupName).build();
                if (map != null) {
                    jobDetail.getJobDataMap().putAll(map);
                }
                trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).startAt(begin).endAt(end).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(intervalInSeconds).withRepeatCount(-1)).build();
                this.quartzScheduler.scheduleJob(jobDetail, trigger);
            } else {
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).startAt(begin).endAt(end).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(intervalInSeconds).withRepeatCount(-1)).build();
                if (map != null) {
                    trigger.getJobDataMap().putAll(map);
                }
                this.quartzScheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        SimpleTrigger trigger;
    }

    public boolean addNewJob(String jobName, String groupName, Map<String, Object> map, Date begin, Date end, int intervalInSeconds, Class<? extends Job> jobClass) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
            logger.info("dfk3 job name a" + jobName);
            SimpleTrigger trigger = (SimpleTrigger) this.quartzScheduler.getTrigger(triggerKey);
            if (trigger == null) {
                trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).startAt(begin).endAt(end).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(intervalInSeconds).withRepeatCount(-1)).build();

                JobDetail jobDetail = JobBuilder.newJob(jobClass)
                        .withIdentity(jobName, groupName).build();
                if (map != null) {
                    jobDetail.getJobDataMap().putAll(map);
                }
                this.quartzScheduler.scheduleJob(jobDetail, trigger);
                return true;
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void setPauseJob(String jobName, String groupName)
            throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, groupName);
        this.quartzScheduler.pauseJob(jobKey);
    }

    public void setResumeJob(String jobName, String groupName)
            throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, groupName);
        this.quartzScheduler.resumeJob(jobKey);
    }

    public void deleteJob(String jobName, String groupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, groupName);
            this.quartzScheduler.deleteJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getCurrentlyExecutingNums() {
        try {
            return this.quartzScheduler.getCurrentlyExecutingJobs().size();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isStarting() {
        try {
            return !this.quartzScheduler.isInStandbyMode();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public int startJobs() {
        try {
            if (this.quartzScheduler.isInStandbyMode()) {
                this.quartzScheduler.start();
                return 1;
            }
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int shutdownJobs() {
        try {
            if (!this.quartzScheduler.isInStandbyMode()) {
                this.quartzScheduler.standby();
                return 1;
            }
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
