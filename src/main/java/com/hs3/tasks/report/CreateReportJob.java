package com.hs3.tasks.report;

import com.hs3.entity.sys.SysConfig;
import com.hs3.entity.users.DailyAcc;
import com.hs3.service.report.OperationReportService;
import com.hs3.service.report.TeamReportService;
import com.hs3.service.report.UserReportService;
import com.hs3.service.sys.SysConfigService;
import com.hs3.service.user.DailyAccService;
import com.hs3.service.user.PrivateRatioService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.sys.WebDateUtils;
import com.hs3.web.utils.SpringContext;

import java.util.Date;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CreateReportJob
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(CreateReportJob.class);
    public static final String DEFAULT_KEY = "daliyWagesKey";
    public static final int DEFAULT_COUNT = 500;
    private OperationReportService operationReportService = (OperationReportService) SpringContext.getBean(OperationReportService.class);
    private UserReportService userReportService = (UserReportService) SpringContext.getBean(UserReportService.class);
    private TeamReportService teamReportService = (TeamReportService) SpringContext.getBean(TeamReportService.class);
    private SysConfigService sysConfigService = (SysConfigService) SpringContext.getBean(SysConfigService.class);
    private PrivateRatioService privateRatioService = (PrivateRatioService) SpringContext.getBean(PrivateRatioService.class);
    private DailyAccService dailyAccService = (DailyAccService) SpringContext.getBean(DailyAccService.class);

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        Date now = new Date();
        Date beforDay = DateUtils.addDay(now, -1);
        int ok = 0;
        try {
            logger.info("处理日工资任务启动");
            SysConfig sysConfig = this.sysConfigService.find("daliyWagesKey");

            //jd-gui
            //validAccountCount = sysConfig == null ? 500 : Integer.parseInt(sysConfig.getVal());
            int validAccountCount = sysConfig != null ? Integer.parseInt(sysConfig.getVal()) : 500;

            Date beginTime = WebDateUtils.getBeginTime(beforDay);
            Date endTime = WebDateUtils.getEndTime(beforDay);
            for (DailyAcc da : this.dailyAccService.listTeam()) {
                this.dailyAccService.updateTeamDailyData(da.getAccount(), beginTime, endTime, validAccountCount);
            }
        } catch (Exception e) {
            logger.error("处理日工资异常！" + e.getMessage(), e);
        } finally {
            logger.info("处理日工资结束！");
        }
        try {
            logger.info("创建个人盈亏报表任务启动！！！！");
            this.userReportService.createUserReportData(DateUtils.addDay(new Date(), -1));
            ok = 1;
        } catch (Exception e) {
            logger.error("生成个人盈亏报表异常！" + e.getMessage(), e);
        } finally {
            logger.info("创建个人盈亏报表任务结束！！！！");
        }
        if (ok == 1) {
            ok = 0;
            try {
                logger.info("创建团队盈亏报表任务启动！！！！");
                this.teamReportService.createTeamReportData(DateUtils.addDay(new Date(), -1));
                ok = 1;
            } catch (Exception e) {
                logger.error("生成团队盈亏报表异常！" + e.getMessage(), e);
            } finally {
                logger.info("创建团队盈亏报表任务结束！！！！");
            }
        } else {
            logger.info("个人盈亏报表异常，不执行团队盈亏任务");
        }
        if (ok == 1) {
            ok = 0;
            try {
                logger.info("私返任务启动！！！！");
                this.privateRatioService.addRatioToUser(beforDay);
                ok = 1;
            } catch (Exception e) {
                logger.error("私返任务异常！" + e.getMessage(), e);
            } finally {
                logger.info("私返任务结束！！！！");
            }
        } else {
            logger.info("团队盈亏报表异常，不执行私返任务");
        }
        try {
            logger.info("生成运营统计报表任务启动！！！！");
            this.operationReportService.addWhenNotExists(DateUtils.format(DateUtils.addDay(new Date(), -1), "yyyy-MM-dd"));
        } catch (Exception e) {
            logger.error("生成运营统计报表异常！" + e.getMessage(), e);
        } finally {
            logger.info("生成运营统计报表任务结束！！！！");
        }
    }
}
