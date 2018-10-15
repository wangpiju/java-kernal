package com.hs3.tasks.lotts;

import com.alibaba.fastjson.JSON;
import com.hs3.commons.CraStatus;
import com.hs3.commons.CraType;
import com.hs3.commons.OpenStatus;
import com.hs3.dao.lotts.LotterySeasonDao;
import com.hs3.entity.lotts.*;
import com.hs3.entity.sys.SysConfig;
import com.hs3.lotts.crawler.WebCrawlerManager;
import com.hs3.lotts.open.INumberBuilder;
import com.hs3.lotts.open.NumberBuilderFactory;
import com.hs3.service.lotts.*;
import com.hs3.service.quartz.TaskLogic;
import com.hs3.service.sys.SysConfigService;
import com.hs3.utils.DateUtils;
import com.hs3.web.utils.SpringContext;
import com.hs.comm.service.lotts.BonusRiskService;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class OpenCrawlerJob
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(OpenCrawlerJob.class);
    private LotteryService lotteryService = (LotteryService) SpringContext.getBean(LotteryService.class);
    private LotteryCrawlerConfigService lotteryCrawlerConfigService = (LotteryCrawlerConfigService) SpringContext.getBean(LotteryCrawlerConfigService.class);
    private LotterySeasonService lotterySeasonService = (LotterySeasonService) SpringContext.getBean(LotterySeasonService.class);
    private LotterySaleTimeService lotterySaleTimeService = (LotterySaleTimeService) SpringContext.getBean(LotterySaleTimeService.class);
    private BetService betService = (BetService) SpringContext.getBean(BetService.class);
    private LotterySeasonDao lotterySeasonDao = (LotterySeasonDao) SpringContext.getBean(LotterySeasonDao.class);
    private LotteryLockService lotteryLockService = (LotteryLockService) SpringContext.getBean(LotteryLockService.class);
    private SysConfigService sysConfigService = (SysConfigService) SpringContext.getBean(SysConfigService.class);
    private TaskLogic taskLogic = (TaskLogic) SpringContext.getBean(TaskLogic.class);
    private BonusRiskService bonusRiskService = (BonusRiskService) SpringContext.getBean(BonusRiskService.class);
    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        JobDataMap jobDataMap = arg0.getJobDetail().getJobDataMap();
        String lotteryId = jobDataMap.getString("lotteryId");
        logger.info((new StringBuilder("启动任务")).append(lotteryId).toString());
        try {
            if (lotteryId.equals("jnd3_5")) {
                long jndTimeOut = DateUtils.AddSecond(new Date(), 210).getTime();
                do {
                    SysConfig sc = sysConfigService.find("CANADA3_5");
                    String v = sc.getVal();
                    if (!v.equals("1"))
                        break;
                    try {
                        Thread.sleep(10000L);
                    } catch (InterruptedException interruptedexception) {
                    }
                } while ((new Date()).getTime() < jndTimeOut);
                if ((new Date()).getTime() >= jndTimeOut) {
                    logger.info((new StringBuilder("加拿大等待退出")).append(lotteryId).toString());
                    return;
                }
            }
        } catch (Exception e) {
            logger.error((new StringBuilder(String.valueOf(lotteryId))).append("加拿大3.5暂停抓号异常：").append(e.getMessage()).toString(), e);
        }
        Date date = new Date();
        LotterySaleTime sale = lotterySaleTimeService.getPreviousByLotteryId(lotteryId, date);
//        logger.info(String.format("--> get lotterySaleTime by lotteryId, lotteryId:%s, date:%s, sale:%s ", lotteryId, DateUtils.format(date, "yyyy-MM-dd HH:mm:ss"), com.alibaba.fastjson.JSON.toJSONString(sale)));

        String seasonId = "";

        if (null != sale) {
            seasonId = sale.getSeasonId();
        }

        try {
            int n = betService.updateClose(lotteryId, seasonId);
            logger.info((new StringBuilder("封盘")).append(lotteryId).append("期号：").append(seasonId).append(",封盘数量：").append(n).toString());
        } catch (Exception e) {
            logger.error((new StringBuilder(String.valueOf(lotteryId))).append("封单异常：").append(e.getMessage()).toString(), e);
        }
        try {
            BetSendEmail.send(lotteryId, seasonId);
            List<LotteryCrawlerConfig> configs = lotteryCrawlerConfigService.listByLotteryId(lotteryId, CraStatus.enable.code());

            if (configs.size() > 0) {
                LotteryCrawlerConfig config = configs.get(0);

                if (config.getCraType() == CraType.random.code()) {
                    do
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    while (sale.getOpenTime().getTime() > (new Date()).getTime());
                    LotterySeason lotterySeason = lotterySeasonDao.getBylotteryIdAndSeason(lotteryId, seasonId);
                    //logger.info("lotterySeason: " + lotterySeason.getSeasonId());
                    if (lotterySeason == null) {
                        INumberBuilder iNumberBuilder = NumberBuilderFactory.getInstance(config.getUrl());
                        LotteryLock lock = lotteryLockService.findByLotteryId(lotteryId);
                        BigDecimal ratio = null;
                        BigDecimal deviation = null;
                        if (lock != null && lock.getStatus() == 0) {
                            if (lock.getCloseTime() != null && lock.getCloseTime().compareTo(new Date()) < 0) {
                                lock.setStatus(1);
                                lotteryLockService.update(lock);
                            } else {
                                ratio = lock.getRatio();
                                deviation = lock.getDeviation();
                            }
                        }

                        lotterySeason = iNumberBuilder.create(lotteryId, seasonId, ratio, deviation);
                        lotterySeason = bonusRiskService.getLotterySeason(lotterySeason, iNumberBuilder, lotteryId, seasonId);
                        logger.info("--> 最终 lotterySeason : "+ JSON.toJSONString(lotterySeason));
                        if (lotterySeason != null) {
                            lotterySeason.setAddTime(new Date());
                            lotterySeason.setOpenTime(sale.getOpenTime());
                            lotterySeasonService.save(lotterySeason);
                            taskLogic.openTaskByUser(lotterySeason);
                        } else {
                            logger.warn(String.format("--> lotterySeason is null, lotteryId : %s - seasonId : %s", lock, seasonId));
                        }
                    } else {
                        logger.warn(String.format("--> %s-%s 已经开奖...", lotteryId, seasonId));
                    }
                } else {
                    Lottery lottery = lotteryService.find(lotteryId);
                    LotterySeason season = (new WebCrawlerManager()).run(lottery, sale, configs);
                    logger.info("--> 最终 lotterySeason : "+ JSON.toJSONString(season));
                    if (season != null) {
                        lotterySeasonDao.save(season);
                        if (season.getAddTime().getTime() < season.getOpenTime().getTime()) {
                            lotteryService.updateStatus(season.getLotteryId(), 1);
                            lotterySaleTimeService.updateOpenStatus(season.getSeasonId(), season.getLotteryId(), OpenStatus.lottery_advance.getStatus());
                            taskLogic.saveAdvanceOpenTask(season);
                        }
                        else
                            taskLogic.openTaskByUser(season);
                    } else {
                        taskLogic.saveNoOpenTask(lotteryId, seasonId);
                    }
                }
            }
        } catch (Exception e) {
            logger.error((new StringBuilder(String.valueOf(lotteryId))).append("结算异常：").append(e.getMessage()).toString(), e);
        }
    }
}
