package com.hs3.lotts.rule.impl;

import com.hs3.entity.lotts.Lottery;
import com.hs3.entity.lotts.LotteryCloseRule;
import com.hs3.entity.lotts.LotterySaleRule;
import com.hs3.entity.lotts.LotterySaleTime;
import com.hs3.entity.lotts.SeasonForDate;
import com.hs3.lotts.rule.SeasonBuilderBase;
import com.hs3.service.lotts.SeasonForDateService;
import com.hs3.utils.DateUtils;
import com.hs3.web.utils.SpringContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Num6AddSeasonBuilder
        extends SeasonBuilderBase {
    private SeasonForDateService seasonForDateService = (SeasonForDateService) SpringContext.getBean("seasonForDateService");

    public String getTitle() {
        return "num6add";
    }

    public String getRemark() {
        return "6位数累加";
    }

    public List<LotterySaleTime> create(Lottery lottery, List<LotterySaleRule> rules, List<LotteryCloseRule> closes, Date begin, int days) {

        /**jd-gui
         SeasonForDate seasonForDate = this.seasonForDateService.getByLotteryId(lottery.getId());

         Long season = Long.valueOf(Long.parseLong(seasonForDate.getFirstSeason()));
         Date openDate = seasonForDate.getSeasonDate();

         int len = getDayLen(rules);
         String weeks = ((LotterySaleRule)rules.get(0)).getWeeks();
         while (openDate.getTime() < begin.getTime())
         {
         openDate = getNextDate(closes, openDate, weeks);

         season = Long.valueOf(season.longValue() + len);
         }
         List<LotterySaleTime> result = new ArrayList();
         for (int i = 0; i < days; i++)
         {
         String lastDate = DateUtils.format(openDate, "yyyy-MM-dd ");
         Date d1;
         Date d2;
         for (Iterator localIterator = rules.iterator(); localIterator.hasNext();
         d1.getTime() <= d2.getTime())
         {
         LotterySaleRule rule = (LotterySaleRule)localIterator.next();
         int beforClose = -rule.getBeforeClose().intValue();
         Date sale = DateUtils.toDateNull(lastDate + rule.getSaleTime());
         d1 = DateUtils.toDateNull(lastDate + rule.getFirstTime());
         d2 = DateUtils.toDateNull(lastDate + rule.getLastTime());
         if (sale.getTime() > d1.getTime()) {
         sale = DateUtils.addDay(sale, -1);
         }
         sale = DateUtils.AddSecond(sale, beforClose);


         sale = DateUtils.addDay(sale, rule.getBeforeDay().intValue());
         d1 = DateUtils.addDay(d1, rule.getBeforeDay().intValue());
         d2 = DateUtils.addDay(d2, rule.getBeforeDay().intValue());
         if (d1.getTime() >= d2.getTime()) {
         d2 = DateUtils.addDay(d2, 1);
         }
         LotterySaleTime saleTime = new LotterySaleTime();
         saleTime.setSeasonId(season.toString());
         saleTime.setLotteryId(lottery.getId());
         saleTime.setBeginTime(sale);
         saleTime.setEndTime(DateUtils.AddSecond(d1, beforClose));
         saleTime.setOpenStatus(Integer.valueOf(0));
         saleTime.setSettleStatus(Integer.valueOf(0));
         saleTime.setPlanStatus(Integer.valueOf(0));
         saleTime.setCurDate(openDate);
         saleTime.setOpenTime(d1);
         saleTime.setOpenAfterTime(DateUtils.AddSecond(d1, rule.getOpenAfter().intValue()));

         result.add(saleTime);


         sale = DateUtils.AddSecond(d1, beforClose);
         d1 = DateUtils.AddSecond(d1, rule.getOpenCycle().intValue());
         season = Long.valueOf(season.longValue() + 1L);
         }
         openDate = getNextDate(closes, openDate, weeks);
         }
         return result;*/

        SeasonForDate seasonForDate = seasonForDateService.getByLotteryId(lottery.getId());
        Long season = Long.valueOf(Long.parseLong(seasonForDate.getFirstSeason()));
        Date openDate = seasonForDate.getSeasonDate();
        int len = getDayLen(rules);
        String weeks = ((LotterySaleRule) rules.get(0)).getWeeks();
        while (openDate.getTime() < begin.getTime()) {
            openDate = getNextDate(closes, openDate, weeks);
            season = Long.valueOf(season.longValue() + (long) len);
        }
        List<LotterySaleTime> result = new ArrayList();
        for (int i = 0; i < days; i++) {
            String lastDate = DateUtils.format(openDate, "yyyy-MM-dd ");
            for (Iterator iterator = rules.iterator(); iterator.hasNext(); ) {
                LotterySaleRule rule = (LotterySaleRule) iterator.next();
                int beforClose = -rule.getBeforeClose().intValue();
                Date sale = DateUtils.toDateNull((new StringBuilder(String.valueOf(lastDate))).append(rule.getSaleTime()).toString());
                Date d1 = DateUtils.toDateNull((new StringBuilder(String.valueOf(lastDate))).append(rule.getFirstTime()).toString());
                Date d2 = DateUtils.toDateNull((new StringBuilder(String.valueOf(lastDate))).append(rule.getLastTime()).toString());
                if (sale.getTime() > d1.getTime())
                    sale = DateUtils.addDay(sale, -1);
                sale = DateUtils.AddSecond(sale, beforClose);
                sale = DateUtils.addDay(sale, rule.getBeforeDay().intValue());
                d1 = DateUtils.addDay(d1, rule.getBeforeDay().intValue());
                d2 = DateUtils.addDay(d2, rule.getBeforeDay().intValue());
                if (d1.getTime() >= d2.getTime())
                    d2 = DateUtils.addDay(d2, 1);
                do {
                    LotterySaleTime saleTime = new LotterySaleTime();
                    saleTime.setSeasonId(season.toString());
                    saleTime.setLotteryId(lottery.getId());
                    saleTime.setBeginTime(sale);
                    saleTime.setEndTime(DateUtils.AddSecond(d1, beforClose));
                    saleTime.setOpenStatus(Integer.valueOf(0));
                    saleTime.setSettleStatus(Integer.valueOf(0));
                    saleTime.setPlanStatus(Integer.valueOf(0));
                    saleTime.setCurDate(openDate);
                    saleTime.setOpenTime(d1);
                    saleTime.setOpenAfterTime(DateUtils.AddSecond(d1, rule.getOpenAfter().intValue()));
                    result.add(saleTime);
                    sale = DateUtils.AddSecond(d1, beforClose);
                    d1 = DateUtils.AddSecond(d1, rule.getOpenCycle().intValue());
                    season = Long.valueOf(season.longValue() + 1L);
                } while (d1.getTime() <= d2.getTime());
            }

            openDate = getNextDate(closes, openDate, weeks);
        }

        return result;

    }
}
