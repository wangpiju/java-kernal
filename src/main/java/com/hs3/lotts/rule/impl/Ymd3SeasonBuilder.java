package com.hs3.lotts.rule.impl;

import com.alibaba.fastjson.JSON;
import com.hs3.entity.lotts.Lottery;
import com.hs3.entity.lotts.LotteryCloseRule;
import com.hs3.entity.lotts.LotterySaleRule;
import com.hs3.entity.lotts.LotterySaleTime;
import com.hs3.lotts.rule.SeasonBuilderBase;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Ymd3SeasonBuilder
        extends SeasonBuilderBase {

    private static final Logger logger = LoggerFactory.getLogger(Ymd3SeasonBuilder.class);
    public String getTitle() {
        return "ymd3";
    }

    public String getRemark() {
        return "日期-3位数";
    }

    protected int indexLen() {
        return 3;
    }

    public List<LotterySaleTime> create(Lottery lottery, List<LotterySaleRule> rules, List<LotteryCloseRule> closes, Date begin, int days) {
        /**jd-gui
         List<LotterySaleTime> result = new ArrayList();
         String weeks = ((LotterySaleRule)rules.get(0)).getWeeks();

         begin = DateUtils.addDay(begin, -1);
         Date openDate = getNextDate(closes, begin, weeks);
         for (int i = 0; i < days; i++)
         {
         int index = 1;

         String lastDate = DateUtils.format(openDate, "yyyy-MM-dd ");
         String fix = DateUtils.format(openDate, "yyyyMMdd-");
         Date d1;
         Date d2;
         for (Iterator localIterator = rules.iterator(); localIterator.hasNext();
         d1.getTime() <= d2.getTime())
         {
         LotterySaleRule rule = (LotterySaleRule)localIterator.next();
         int beforClose = -rule.getBeforeClose();
         Date sale = DateUtils.toDateNull(lastDate + rule.getSaleTime());
         d1 = DateUtils.toDateNull(lastDate + rule.getFirstTime());
         d2 = DateUtils.toDateNull(lastDate + rule.getLastTime());
         if (sale.getTime() > d1.getTime()) {
         sale = DateUtils.addDay(sale, -1);
         }
         sale = DateUtils.AddSecond(sale, beforClose);


         sale = DateUtils.addDay(sale, rule.getBeforeDay());
         d1 = DateUtils.addDay(d1, rule.getBeforeDay());
         d2 = DateUtils.addDay(d2, rule.getBeforeDay());
         if (d1.getTime() >= d2.getTime()) {
         d2 = DateUtils.addDay(d2, 1);
         }
         String season = fix + StrUtils.parseLength(Integer.valueOf(index), indexLen());

         LotterySaleTime saleTime = new LotterySaleTime();
         saleTime.setSeasonId(season);
         saleTime.setLotteryId(lottery.getId());
         saleTime.setBeginTime(sale);
         saleTime.setEndTime(DateUtils.AddSecond(d1, beforClose));
         saleTime.setOpenStatus(Integer.valueOf(0));
         saleTime.setSettleStatus(Integer.valueOf(0));
         saleTime.setPlanStatus(Integer.valueOf(0));
         saleTime.setCurDate(openDate);
         saleTime.setOpenTime(d1);
         saleTime.setOpenAfterTime(DateUtils.AddSecond(d1, rule.getOpenAfter()));

         result.add(saleTime);

         sale = DateUtils.AddSecond(d1, beforClose);
         d1 = DateUtils.AddSecond(d1, rule.getOpenCycle());
         index++;
         }
         openDate = getNextDate(closes, openDate, weeks);
         }
         return result;*/
        logger.info("--> create ymd3Season , lottery:{}, rules:{}, closes:{}, begin:{}, days:{}",
                new Object[]{JSON.toJSONString(lottery), JSON.toJSONString(rules),JSON.toJSONString(closes), begin, days});
        List<LotterySaleTime> result = new ArrayList<>();
        String weeks = rules.get(0).getWeeks(); //180
        begin = DateUtils.addDay(begin, -1);    //2018-6-15 00:00:00
        Date openDate = getNextDate(closes, begin, weeks);

        for (int i = 0; i < days; i++) {
            int index = 1;
            String lastDate = DateUtils.format(openDate, "yyyy-MM-dd ");
            String fix = DateUtils.format(openDate, "yyyyMMdd");
            for (LotterySaleRule rule: rules ) {
                int beforClose = -rule.getBeforeClose();//180
                Date sale = DateUtils.toDateNull((new StringBuilder(String.valueOf(lastDate))).append(rule.getSaleTime()).toString());
                Date d1 = DateUtils.toDateNull((new StringBuilder(String.valueOf(lastDate))).append(rule.getFirstTime()).toString());
                Date d2 = DateUtils.toDateNull((new StringBuilder(String.valueOf(lastDate))).append(rule.getLastTime()).toString());
                if (sale.getTime() > d1.getTime())
                    sale = DateUtils.addDay(sale, -1);
                sale = DateUtils.AddSecond(sale, beforClose);
                sale = DateUtils.addDay(sale, rule.getBeforeDay());
                d1 = DateUtils.addDay(d1, rule.getBeforeDay());
                d2 = DateUtils.addDay(d2, rule.getBeforeDay());
                if (d1.getTime() >= d2.getTime())
                    d2 = DateUtils.addDay(d2, 1);
                do {
                    String season = (new StringBuilder(String.valueOf(fix))).append(StrUtils.parseLength(index, indexLen())).toString();
                    LotterySaleTime saleTime = new LotterySaleTime();
                    saleTime.setSeasonId(season);
                    saleTime.setLotteryId(lottery.getId());
                    saleTime.setBeginTime(sale);
                    saleTime.setEndTime(DateUtils.AddSecond(d1, beforClose));
                    saleTime.setOpenStatus(0);
                    saleTime.setSettleStatus(0);
                    saleTime.setPlanStatus(0);
                    saleTime.setCurDate(openDate);
                    saleTime.setOpenTime(d1);
                    saleTime.setOpenAfterTime(DateUtils.AddSecond(d1, rule.getOpenAfter()));
                    result.add(saleTime);
                    sale = DateUtils.AddSecond(d1, beforClose);
                    d1 = DateUtils.AddSecond(d1, rule.getOpenCycle());
                    index++;
//                    logger.info("--> create saleTime : {}"+JSON.toJSONString(saleTime));
                } while (d1.getTime() <= d2.getTime());
            }

            openDate = getNextDate(closes, openDate, weeks);
        }
        return result;

    }
}
