package com.hs3.lotts.crawler;

import com.hs3.commons.CraStatus;
import com.hs3.commons.CraType;
import com.hs3.dao.lotts.LotterySeasonDao;
import com.hs3.entity.lotts.*;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryFactory;
import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.config.CrawlerUrlBuilderFactory;
import com.hs3.lotts.config.ICrawlerUrlBuilder;
import com.hs3.models.Jsoner;
import com.hs3.service.lotts.LotteryCrawlerConfigService;
import com.hs3.service.lotts.LotterySaleTimeService;
import com.hs3.service.lotts.LotterySeasonWeightService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.quartz.TaskLogic;
import com.hs3.utils.ListUtils;
import com.hs3.web.utils.SpringContext;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebCrawlerManager
        implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(WebCrawlerManager.class);
    private static final long serialVersionUID = 1L;
    private LotterySaleTimeService lotterySaleTimeService = (LotterySaleTimeService) SpringContext.getBean("lotterySaleTimeService");
    private LotteryCrawlerConfigService lotteryCrawlerConfigService = (LotteryCrawlerConfigService) SpringContext.getBean("lotteryCrawlerConfigService");
    private LotterySeasonWeightService lotterySeasonWeightService = (LotterySeasonWeightService) SpringContext.getBean("lotterySeasonWeightService");
    private LotteryService lotteryService = (LotteryService) SpringContext.getBean("lotteryService");
    private LotterySeasonDao lotterySeasonDao = (LotterySeasonDao) SpringContext.getBean("lotterySeasonDao");
    private List<WebCrawlerThread> crawlers = new ArrayList<>();
    private TaskLogic taskLogic = (TaskLogic) SpringContext.getBean(TaskLogic.class);

    public LotterySeason run(Lottery lottery, LotterySaleTime sale, List<LotteryCrawlerConfig> configs) {
        //jd_gui
        //int i;
        try {

            LotteryBase lott = LotteryFactory.getInstance(lottery.getGroupName());
            String lotteryId = sale.getLotteryId();
            String seasonId = sale.getSeasonId();
            String info = lotteryId + " " + seasonId;

            if (configs.size() <= 0) {
                return null;
            }

            for (LotteryCrawlerConfig config : configs) {
                if (config.getCraType() == CraType.crawler.code() && config.getStatus() == CraStatus.enable.code()) {
                    ICrawlerUrlBuilder builder = CrawlerUrlBuilderFactory.getInstance(config.getUrlBuilder());
                    String url = config.getUrl();
                    if (builder != null) {
                        url = builder.createUrl(url, sale.getSeasonId());
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug(info + " 启动抓号线程：URL:" + url);
                    }
                    WebCrawlerThread crawler = new WebCrawlerThread(url, config.getRegex(), config.getWeight(), seasonId);

                    this.crawlers.add(crawler);
                    crawler.run();
                }
            }

            do {
                for (int i = 0; i < this.crawlers.size(); i++) {
                    WebCrawlerThread c = (WebCrawlerThread) this.crawlers.get(i);
                    LotterySeason season = c.getSeason();
                    if (c.getSeason() != null) {
                        season.setLotteryId(lotteryId);
                        season.setSeasonId(seasonId);
                        season.setOpenTime(sale.getOpenTime());
                        season.setAddTime(new Date());
                        String openNum = ListUtils.toString(lott.getSeasonOpen(c.getSeason()).getNums());
                        logger.info(info + " 号码:" + openNum);

                        Integer weight = c.getWeight();
                        LotterySeasonWeight lsw = new LotterySeasonWeight();
                        lsw.setLotteryId(lotteryId);
                        lsw.setSeasonId(seasonId);
                        lsw.setNums(openNum);
                        lsw.setCreateTime(new Date());
                        lsw.setWeightType(c.getUrl());
                        lsw.setWeight(weight);
                        if (this.lotterySeasonWeightService.saveProcessWeight(lsw, lottery.getWeight()) == 2) {
                            return season;
                        }
                        c.stop();
                        this.crawlers.remove(c);

                        break;
                    }
                }

                try {
                    Thread.sleep(300L);
                } catch (InterruptedException localInterruptedException) {
                }

            } while (
                    sale.getOpenAfterTime().getTime() > new Date().getTime()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            //jd_gui
            //int i;
            for (WebCrawlerThread crawler : crawlers) {
                crawler.stop();
            }
            this.crawlers.clear();
        }
        return this.lotterySeasonDao.getBylotteryIdAndSeason(sale.getLotteryId(), sale.getSeasonId());
    }

    public Object runByUser(LotterySeasonWeight lotterySeasonWeight, int totalWeight) {
        LotterySaleTime sale = this.lotterySaleTimeService.getByLotteryIdAndSeasonId(lotterySeasonWeight.getLotteryId(), lotterySeasonWeight.getSeasonId());
        if (sale.getOpenAfterTime().getTime() < new Date().getTime()) {
            return Jsoner.error("人工开奖时间已过");
        }
        int sign = this.lotterySeasonWeightService.saveProcessWeight(lotterySeasonWeight, totalWeight);
        if (sign == 2) {
            String openNum = lotterySeasonWeight.getNums();
            logger.info(sale.getLotteryId() + " " + sale.getSeasonId() + " 权重累加成功：" + " 号码:" + openNum);
            List<Integer> nums = ListUtils.toIntList(openNum);
            LotterySeason lotterySeason = LotteryUtils.numToSeason(sale, nums);
            this.lotterySeasonDao.save(lotterySeason);
            this.taskLogic.openTaskByUser(lotterySeason);
        } else {
            return Jsoner.error("保存号码成功！但是权重累加未达标！");
        }
        return Jsoner.success();
    }

    public Object openByUser(String lotteryId, String seasonId, String openNum, String user) {
        LotterySaleTime lotterySaleTime = this.lotterySaleTimeService.find(seasonId, lotteryId);
        if (lotterySaleTime == null) {
            return Jsoner.error("填写的期号有误");
        }
        List<LotteryCrawlerConfig> lotteryCrawlerConfigs = this.lotteryCrawlerConfigService.listByLotteryIdAndStatus(lotteryId, 0, 1);
        if (lotteryCrawlerConfigs.size() == 0) {
            return Jsoner.error("没有人工号源配置");
        }
        LotterySeasonWeight lotterySeasonWeight = new LotterySeasonWeight();
        lotterySeasonWeight.setCreateTime(new Date());
        lotterySeasonWeight.setLotteryId(lotteryId);
        lotterySeasonWeight.setSeasonId(seasonId);
        lotterySeasonWeight.setNums(openNum);
        lotterySeasonWeight.setWeight(((LotteryCrawlerConfig) lotteryCrawlerConfigs.get(0)).getWeight());
        lotterySeasonWeight.setWeightType("p_" + user);
        Lottery lottery = this.lotteryService.find(lotteryId);
        return runByUser(lotterySeasonWeight, lottery.getWeight());
    }
}
