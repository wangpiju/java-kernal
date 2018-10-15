package com.hs3.service.lotts;

import com.alibaba.fastjson.JSON;
import com.hs3.dao.lotts.LotterySaleRuleDao;
import com.hs3.entity.lotts.LotterySaleRule;
import com.hs3.service.third.DhThirdService;
import com.hs3.tasks.lotts.OpenCrawlerJob;
import com.hs3.utils.DateUtils;
import com.hs3.utils.ListUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LotteryJobService {
    @Autowired
    private DhThirdService dhThirdService;
    @Autowired
    private LotterySaleRuleDao lotterySaleRuleDao;
    private static final Logger logger = LoggerFactory.getLogger(LotteryJobService.class);

    public void addJob(LotterySaleRule rule) {
        if (null == rule) {
            logger.error("找不到獎期");
            return;
        } else {
            logger.info("開始-----" + rule.getLotteryId() + "任務-----------");
        }
        if (rule.getLotteryId().equals("dfk3")) {
            logger.info("dfk3 已找到 LotterySaleRule");
        }

        List<Integer> weeks = ListUtils.toIntList(rule.getWeeks());
        String first = (new StringBuilder("yyyy-MM-dd ")).append(rule.getFirstTime()).toString();
        String last = (new StringBuilder("yyyy-MM-dd ")).append(rule.getLastTime()).toString();
        int cycle = rule.getOpenCycle();
        int beforeClose = rule.getBeforeClose();
        Date starDate;
        Date endDate;
        try {
            starDate = DateUtils.toDate(DateUtils.format(new Date(), first));
            endDate = DateUtils.toDate(DateUtils.format(new Date(), last));
        } catch (ParseException e) {
            throw new RuntimeException("时间格式错误");
        }
        if (starDate.getTime() > endDate.getTime())
            endDate = DateUtils.addDay(endDate, 1);
        long start = starDate.getTime();
        long end = endDate.getTime();
        long now = (new Date()).getTime();
        Map<String, Object> map = new HashMap<>();
        map.put("lotteryId", rule.getLotteryId());
        String groupName = (new StringBuilder("抓号 - ")).append(rule.getLotteryId()).toString();

        String jobName;
        if (rule.getLotteryId().equals("dfk3")) {
            logger.info("dfk3 now: " + now + ", start: " + start + " end: " + end);
        }
        if (now >= start) {
            if (now < end) {
                jobName = (new StringBuilder(String.valueOf(DateUtils.format(starDate)))).append(" - ").append(DateUtils.format(endDate)).toString();
                if (rule.getLotteryId().equals("dfk3")) {
                    logger.info("dfk3 job name a" + jobName);
                }

                Date d1 = DateUtils.AddSecond(starDate, -beforeClose);
                Date d2 = DateUtils.AddSecond(endDate, -beforeClose + 1);
                if (rule.getLotteryId().equals("dfk3"))
                    logger.info("dfk3 before add new job");
                if (weeks.contains(DateUtils.getWeekOfDate(d1))) {
                    boolean flag = dhThirdService.quartzAddNewJobWithStartEnd(jobName, groupName, JSON.toJSONString(map), DateUtils.format(d1, "yyyy-MM-dd HH:mm:ss"), DateUtils.format(d2, "yyyy-MM-dd HH:mm:ss"), cycle, OpenCrawlerJob.class.getName());
                    if (rule.getLotteryId().equals("dfk3"))
                        logger.info("dfk3 add new job success");
                }
            }
            starDate = DateUtils.addDay(starDate, 1);
            endDate = DateUtils.addDay(endDate, 1);
        }
        jobName = (new StringBuilder(String.valueOf(DateUtils.format(starDate)))).append(" - ").append(DateUtils.format(endDate)).toString();

        if (rule.getLotteryId().equals("dfk3"))
            logger.info("dfk3 job name b" + jobName);
        starDate = DateUtils.AddSecond(starDate, -beforeClose);
        endDate = DateUtils.AddSecond(endDate, -beforeClose + 1);
        if (weeks.contains(DateUtils.getWeekOfDate(starDate))) {
            dhThirdService.quartzAddJobWithStartEnd(jobName, groupName, JSON.toJSONString(map), DateUtils.format(starDate, "yyyy-MM-dd HH:mm:ss"), DateUtils.format(endDate, "yyyy-MM-dd HH:mm:ss"), cycle, OpenCrawlerJob.class.getName());
        }

    }

    public void deleteJob(LotterySaleRule rule) {
        String first = "yyyy-MM-dd " + rule.getFirstTime();
        String last = "yyyy-MM-dd " + rule.getLastTime();

        Date d1 = DateUtils.toDateNull(DateUtils.format(new Date(), first));
        Date d2 = DateUtils.toDateNull(DateUtils.format(new Date(), last));

        String groupName = "抓号 - " + rule.getLotteryId();
        String jobName1 = DateUtils.format(d1) + " - " + DateUtils.format(d2);

        d1 = DateUtils.addDay(d1, 1);
        d2 = DateUtils.addDay(d2, 1);

        String jobName2 = DateUtils.format(d1) + " - " + DateUtils.format(d2);
        dhThirdService.quartzDeleteJob(jobName1, groupName);
        dhThirdService.quartzDeleteJob(jobName2, groupName);

    }

    public void updateAllJob() {
        List<LotterySaleRule> rules = this.lotterySaleRuleDao.list(null);
        for (LotterySaleRule rule : rules) {
            addJob(rule);
        }
    }

}
