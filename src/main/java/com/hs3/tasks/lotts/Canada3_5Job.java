package com.hs3.tasks.lotts;

import com.hs3.entity.lotts.LotteryCrawlerConfig;
import com.hs3.entity.lotts.LotterySeason;
import com.hs3.entity.sys.SysConfig;
import com.hs3.lotts.config.CrawlerUrlBuilderFactory;
import com.hs3.lotts.config.ICrawlerUrlBuilder;
import com.hs3.lotts.crawler.Canada3_5CrawlerThread;
import com.hs3.service.lotts.LotteryCrawlerConfigService;
import com.hs3.service.lotts.LotterySaleTimeService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.sys.SysConfigService;
import com.hs3.utils.DateUtils;
import com.hs3.web.utils.SpringContext;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Canada3_5Job
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(Canada3_5Job.class);
    private static final String LOTTERY_ID = "jnd3_5";
    private static final String NAME = "修复奖期：";
    private LotterySaleTimeService lotterySaleTimeService = (LotterySaleTimeService) SpringContext.getBean(LotterySaleTimeService.class);
    private LotteryCrawlerConfigService lotteryCrawlerConfigService = (LotteryCrawlerConfigService) SpringContext.getBean(LotteryCrawlerConfigService.class);
    private LotteryService lotteryService = (LotteryService) SpringContext.getBean(LotteryService.class);
    private SysConfigService sysConfigService = (SysConfigService) SpringContext.getBean(SysConfigService.class);

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        SysConfig sc = this.sysConfigService.find("CANADA3_5");
        sc.setVal("1");
        this.sysConfigService.update(sc);

        this.lotteryService.updateStatus("jnd3_5", Integer.valueOf(1));
        logger.info("修复奖期：jnd3_5：自动停售");

        Date begin = DateUtils.addHour(DateUtils.getToDay(), 19);


        String seasonId = this.lotterySaleTimeService.minDaySeason("jnd3_5", begin);
        logger.info("修复奖期：中断奖期：" + seasonId);


        List<LotteryCrawlerConfig> configs = this.lotteryCrawlerConfigService.listByLotteryIdAndStatus("jnd3_5", Integer.valueOf(0), Integer.valueOf(0));
        if (configs.size() > 0) {
            LotteryCrawlerConfig config = (LotteryCrawlerConfig) configs.get(0);

            ICrawlerUrlBuilder builder = CrawlerUrlBuilderFactory.getInstance(config.getUrlBuilder());
            String url = config.getUrl();
            if (builder != null) {
                url = builder.createUrl(url, seasonId);
            }
            Canada3_5CrawlerThread crawler = new Canada3_5CrawlerThread(config.getUrl(), config.getRegex(), seasonId, "jnd3_5", Integer.valueOf(10000));
            crawler.run();
            LotterySeason season = crawler.await(DateUtils.addHour(begin, 3));
            if (season == null) {
                crawler.stop();
                logger.info("修复奖期：未能抓取号码");
            } else {
                logger.info("修复奖期：" + season.getLotteryId() + "  " + season.getSeasonId() + "  " + DateUtils.format(season.getOpenTime()));
                this.lotterySaleTimeService.saveCanada(season);
                sc.setVal("0");
                this.sysConfigService.update(sc);
            }
        }
    }
}
