package com.hs3.service.quartz;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("quartzManagerService")
public class QuartzManagerService {
    private String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
    private String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";
    @Autowired
    private Scheduler quartzScheduler;

    public void addJob(String jobName, Class cls, String time) {
        try {
            JobDetail jobDetail = new JobDetailImpl(this.JOB_GROUP_NAME, jobName, cls);

            CronTrigger trigger = new CronTriggerImpl(this.TRIGGER_GROUP_NAME, jobName);
            ((CronTriggerImpl) trigger).setCronExpression(time);
            this.quartzScheduler.scheduleJob(jobDetail, trigger);
            if (!this.quartzScheduler.isShutdown()) {
                this.quartzScheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addJob(String jobName, String groupName, String cronExpression, Class<? extends Job> jobClass) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
        try {
            CronTrigger trigger = (CronTrigger) this.quartzScheduler.getTrigger(triggerKey);
            if (trigger == null) {
                JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, groupName).build();

                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

                trigger = (CronTrigger) TriggerBuilder.newTrigger().withIdentity(jobName, groupName).withSchedule(scheduleBuilder).build();
                this.quartzScheduler.scheduleJob(jobDetail, trigger);
            } else {
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

                trigger = (CronTrigger) trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

                this.quartzScheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        CronTrigger trigger;
    }

    public void addJob(String jobGroupName, String jobName, String triggerGroupName, String triggerName, Class jobClass, String time) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            JobDetail jobDetail = new JobDetailImpl(jobName, jobGroupName, jobClass);

            CronTrigger trigger = new CronTriggerImpl(triggerName, triggerGroupName);
            ((CronTriggerImpl) trigger).setCronExpression(time);
            this.quartzScheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addJobEx(String jobGroupName, String jobName, String triggerGroupName, String triggerName, Class jobClass, String time, Map<String, Object> map) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        try {
            CronTrigger trigger = (CronTrigger) this.quartzScheduler.getTrigger(triggerKey);
            if (trigger == null) {
                JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
                JobDetail jobDetail = new JobDetailImpl(jobName, jobGroupName, jobClass);
                jobDetail.getJobDataMap().putAll(map);

                trigger = new CronTriggerImpl(triggerName, triggerGroupName);
                ((CronTriggerImpl) trigger).setCronExpression(time);
                this.quartzScheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CronTrigger trigger;
    }

    public void modifyJobTimeEx(String jobName, String trigggername, String time) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
            CronTrigger trigger = (CronTrigger) this.quartzScheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobKey jobKey = JobKey.jobKey(jobName);
                JobDetail jobDetail = this.quartzScheduler.getJobDetail(jobKey);
                Class objJobClass = jobDetail.getJobClass();
                removeJob(jobName);
                addJob(jobName, objJobClass, time);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void modifyJobTime(String triggerGroupName, String triggerName, String time) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) this.quartzScheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);


                trigger = (CronTrigger) trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

                this.quartzScheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeJob(String jobName) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, this.TRIGGER_GROUP_NAME);
            this.quartzScheduler.pauseTrigger(triggerKey);
            this.quartzScheduler.unscheduleJob(triggerKey);
            JobKey jobKey = JobKey.jobKey(jobName, this.TRIGGER_GROUP_NAME);
            this.quartzScheduler.deleteJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeJob(String jobName, String groupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, groupName);
            this.quartzScheduler.deleteJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeJob(String jobGroupName, String jobName, String triggerGroupName, String triggerName) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            this.quartzScheduler.pauseTrigger(triggerKey);
            this.quartzScheduler.unscheduleJob(triggerKey);
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            this.quartzScheduler.deleteJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startJobs() {
        try {
            this.quartzScheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdownJobs() {
        try {
            if (!this.quartzScheduler.isShutdown()) {
                this.quartzScheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setPauseJob(String groupname, String name)
            throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(name, groupname);
        this.quartzScheduler.pauseJob(jobKey);
    }

    public void setPauseTrigger(String triggergroup, String triggername) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggername, triggergroup);
        try {
            this.quartzScheduler.pauseTrigger(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void setResumeJob(String groupname, String name)
            throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(name, groupname);
        this.quartzScheduler.resumeJob(jobKey);
    }

    public void setResumeTrigger(String triggergroup, String triggername) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggername, triggergroup);
        try {
            this.quartzScheduler.resumeTrigger(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
