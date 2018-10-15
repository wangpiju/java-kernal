package com.hs3.lotts.rule;

import com.hs3.entity.lotts.LotteryCloseRule;
import com.hs3.entity.lotts.LotterySaleRule;
import com.hs3.utils.DateUtils;
import com.hs3.utils.ListUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public abstract class SeasonBuilderBase
        implements ISeasonBuilder {
    protected Date getNextDate(List<LotteryCloseRule> closes, Date d, String weeks) {
        Date result = DateUtils.addDay(d, 1);
        List<Integer> n = ListUtils.toIntList(weeks);
        for (LotteryCloseRule close : closes) {
            long begin = close.getStartTime().getTime();
            long end = close.getEndTime().getTime();

            result = toWeekDate(result, n);
            long now = result.getTime();
            if ((now >= begin) && (now <= end)) {
                result = DateUtils.addDay(close.getEndTime(), 1);
            }
        }
        result = toWeekDate(result, n);

        return result;
    }

    protected Date toWeekDate(Date d, List<Integer> n) {
        while (!n.contains(DateUtils.getWeekOfDate(d))) {
            d = DateUtils.addDay(d, 1);
        }
        return d;
    }

    protected int getDayLen(List<LotterySaleRule> rules) {
        int dayLen = 0;


        /**jd-gui
         Date d1;
         Date d2;
         for (Iterator localIterator = rules.iterator(); localIterator.hasNext(); d1.getTime() <= d2.getTime())
         {
         LotterySaleRule rule = (LotterySaleRule)localIterator.next();
         d1 = DateUtils.toDateNull("2016-01-01 " + rule.getFirstTime());
         d2 = DateUtils.toDateNull("2016-01-01 " + rule.getLastTime());
         if (d1.getTime() >= d2.getTime())
         {
         d2 = DateUtils.addDay(d2, 1);


         continue;
         dayLen++;
         d1 = DateUtils.AddSecond(d1, rule.getOpenCycle().intValue());
         }
         }*/

        for (Iterator iterator = rules.iterator(); iterator.hasNext(); ) {
            LotterySaleRule rule = (LotterySaleRule) iterator.next();
            Date d1 = DateUtils.toDateNull((new StringBuilder("2016-01-01 ")).append(rule.getFirstTime()).toString());
            Date d2 = DateUtils.toDateNull((new StringBuilder("2016-01-01 ")).append(rule.getLastTime()).toString());
            if (d1.getTime() >= d2.getTime())
                d2 = DateUtils.addDay(d2, 1);
            for (; d1.getTime() <= d2.getTime(); d1 = DateUtils.AddSecond(d1, rule.getOpenCycle().intValue()))
                dayLen++;

        }


        return dayLen;
    }
}
