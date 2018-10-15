package com.hs3.lotts.ssc.trend;

import com.hs3.lotts.LotteryTrend;
import com.hs3.models.lotts.SeasonOpen;
import com.hs3.models.lotts.SeasonTrend;
import com.hs3.models.lotts.SeasonTrendNum;
import com.hs3.models.lotts.TrendCell;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SscStar5Trend
        extends LotteryTrend {
    private static final String NAME = "五星";
    private static final String[] TITLES = {"万位", "千位", "百位", "十位", "个位", "号码分布"};
    private static final Integer[] NUMS = {Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)};
    private static final int NUMS_LEN = 10;

    public String getName() {
        return "五星";
    }

    public String[] getTitles() {
        return TITLES;
    }

    public Integer[] getNums() {
        return NUMS;
    }

    public int getNumLen() {
        return 10;
    }

    public int getOpenLen() {
        return 5;
    }

    private List<SeasonTrend> trends = null;
    private List<SeasonTrend> allTrends = null;

    private void setCell(boolean has, TrendCell last) {
        if (has) {
            last.setMaxOpen(last.getMaxOpen() + 1);
            last.setNowLian(last.getNowLian() + 1);
            if (last.getNowLian() > last.getMaxLian()) {
                last.setMaxLian(last.getNowLian());
            }
            last.setNowLost(0);
        } else {
            last.setNowLost(last.getNowLost() + 1);
            last.setNowLian(0);
            if (last.getNowLost() > last.getMaxLost()) {
                last.setMaxLost(last.getNowLost());
            }
        }
    }

    public void setData(List<SeasonOpen> seasonList, List<Map<String, Object>> lost) {

        /**jd-gui
         this.trends = new ArrayList();
         this.allTrends = new ArrayList();


         List<TrendCell> lasts = new ArrayList();


         List<Integer> firstFB = new ArrayList();
         for (int row = 0; row < getTitles().length; row++) {
         for (int col = 0; col < getNums().length; col++)
         {
         TrendCell last = new TrendCell();
         if (row < getTitles().length - 1)
         {
         int lostNum = 0;
         try
         {
         lostNum = new BigDecimal(((Map)lost.get(col)).get("o" + (row + 1)).toString()).intValue() - 1;
         }
         catch (Exception localException) {}
         last.setNowLost(lostNum);
         lasts.add(last);
         if (firstFB.size() <= col) {
         firstFB.add(Integer.valueOf(lostNum));
         } else if (((Integer)firstFB.get(col)).intValue() > lostNum) {
         firstFB.set(col, Integer.valueOf(lostNum));
         }
         }
         else
         {
         last.setNowLost(((Integer)firstFB.get(col)).intValue());
         lasts.add(last);
         }
         }
         }
         int maxIndex = seasonList.size() - 1;
         Map<Integer, Integer> openMap;
         for (int index = maxIndex; index >= 0; index--)
         {
         SeasonOpen open = (SeasonOpen)seasonList.get(index);
         st = new SeasonTrend();
         st.setSeasonId(open.getSeasonId());
         st.setNums(ListUtils.toString(open.getNums()));
         info = new ArrayList();
         st.setInfo(info);
         this.trends.add(st);


         openMap = new HashMap();
         for (int nnn = 0; nnn < getOpenLen(); nnn++)
         {
         String n = (String)open.getNums().get(nnn);
         int nowN = Integer.parseInt(n);
         if (openMap.containsKey(Integer.valueOf(nowN))) {
         openMap.put(Integer.valueOf(nowN), Integer.valueOf(((Integer)openMap.get(Integer.valueOf(nowN))).intValue() + 1));
         } else {
         openMap.put(Integer.valueOf(nowN), Integer.valueOf(1));
         }
         }
         for (int row = 0; row < getTitles().length; row++)
         {
         Integer openNum = null;
         if (row < getTitles().length - 1) {
         openNum = Integer.valueOf(Integer.parseInt((String)open.getNums().get(row)));
         }
         for (int col = 0; col < getNums().length; col++)
         {
         int fullCol = row * getNums().length + col;
         TrendCell last = (TrendCell)lasts.get(fullCol);

         String clazz = "";
         SeasonTrendNum stn = new SeasonTrendNum();
         if (row < getTitles().length - 1)
         {
         if (openNum == getNums()[col])
         {
         setCell(true, last);
         stn.setNum(getNums()[col]);
         clazz = "trendBall-" + (row + 1);
         }
         else
         {
         setCell(false, last);
         stn.setNum(Integer.valueOf(last.getNowLost()));
         }
         }
         else if (row == getTitles().length - 1) {
         if (openMap.containsKey(getNums()[col]))
         {
         setCell(true, last);
         stn.setNum(getNums()[col]);
         if (((Integer)openMap.get(getNums()[col])).intValue() > 1) {
         clazz = "clustNumBallWarm";
         } else {
         clazz = "clustNumBallOverlap";
         }
         }
         else
         {
         setCell(false, last);
         stn.setNum(Integer.valueOf(last.getNowLost()));
         }
         }
         if (col == 0) {
         clazz = clazz + " trendBorder-l";
         }
         stn.setClazz(clazz);
         info.add(stn);
         }
         }
         }
         String[] foots = { "出现总次数", "最大遗漏值", "最大连出值" };

         List<SeasonTrendNum> info = (openMap = foots).length;
         for (SeasonTrend st = 0; st < info; st++)
         {
         String title = openMap[st];
         SeasonTrend st = new SeasonTrend();
         st.setSeasonId(title);
         List<SeasonTrendNum> info = new ArrayList();
         st.setInfo(info);
         this.allTrends.add(st);
         for (int row = 0; row < getTitles().length; row++) {
         for (int col = 0; col < getNums().length; col++)
         {
         int fullCol = row * getNums().length + col;
         TrendCell last = (TrendCell)lasts.get(fullCol);

         SeasonTrendNum stn = new SeasonTrendNum();
         if ("出现总次数".equals(title)) {
         stn.setNum(Integer.valueOf(last.getMaxOpen()));
         } else if ("最大遗漏值".equals(title)) {
         stn.setNum(Integer.valueOf(last.getMaxLost()));
         } else {
         stn.setNum(Integer.valueOf(last.getMaxLian()));
         }
         if (col == 0) {
         stn.setClazz("trendBorder-l");
         }
         info.add(stn);
         }
         }
         }
         */

        trends = new ArrayList();
        allTrends = new ArrayList();
        List<TrendCell> lasts = new ArrayList();
        List<Integer> firstFB = new ArrayList();
        for (int row = 0; row < getTitles().length; row++) {
            for (int col = 0; col < getNums().length; col++) {
                TrendCell last = new TrendCell();
                if (row < getTitles().length - 1) {
                    int lostNum = 0;
                    try {
                        lostNum = (new BigDecimal(((Map) lost.get(col)).get((new StringBuilder("o")).append(row + 1).toString()).toString())).intValue() - 1;
                    } catch (Exception exception) {
                    }
                    last.setNowLost(lostNum);
                    lasts.add(last);
                    if (firstFB.size() <= col)
                        firstFB.add(Integer.valueOf(lostNum));
                    else if (((Integer) firstFB.get(col)).intValue() > lostNum)
                        firstFB.set(col, Integer.valueOf(lostNum));
                } else {
                    last.setNowLost(((Integer) firstFB.get(col)).intValue());
                    lasts.add(last);
                }
            }

        }

        int maxIndex = seasonList.size() - 1;
        for (int index = maxIndex; index >= 0; index--) {
            SeasonOpen open = (SeasonOpen) seasonList.get(index);
            SeasonTrend st = new SeasonTrend();
            st.setSeasonId(open.getSeasonId());
            st.setNums(ListUtils.toString(open.getNums()));
            List info = new ArrayList();
            st.setInfo(info);
            trends.add(st);
            Map<Integer, Integer> openMap = new HashMap();
            for (int nnn = 0; nnn < getOpenLen(); nnn++) {
                String n = (String) open.getNums().get(nnn);
                int nowN = Integer.parseInt(n);
                if (openMap.containsKey(Integer.valueOf(nowN)))
                    openMap.put(Integer.valueOf(nowN), Integer.valueOf(((Integer) openMap.get(Integer.valueOf(nowN))).intValue() + 1));
                else
                    openMap.put(Integer.valueOf(nowN), Integer.valueOf(1));
            }

            for (int row = 0; row < getTitles().length; row++) {
                Integer openNum = null;
                if (row < getTitles().length - 1)
                    openNum = Integer.valueOf(Integer.parseInt((String) open.getNums().get(row)));
                for (int col = 0; col < getNums().length; col++) {
                    int fullCol = row * getNums().length + col;
                    TrendCell last = (TrendCell) lasts.get(fullCol);
                    String clazz = "";
                    SeasonTrendNum stn = new SeasonTrendNum();
                    if (row < getTitles().length - 1) {
                        if (openNum == getNums()[col]) {
                            setCell(true, last);
                            stn.setNum(getNums()[col]);
                            clazz = (new StringBuilder("trendBall-")).append(row + 1).toString();
                        } else {
                            setCell(false, last);
                            stn.setNum(Integer.valueOf(last.getNowLost()));
                        }
                    } else if (row == getTitles().length - 1)
                        if (openMap.containsKey(getNums()[col])) {
                            setCell(true, last);
                            stn.setNum(getNums()[col]);
                            if (((Integer) openMap.get(getNums()[col])).intValue() > 1)
                                clazz = "clustNumBallWarm";
                            else
                                clazz = "clustNumBallOverlap";
                        } else {
                            setCell(false, last);
                            stn.setNum(Integer.valueOf(last.getNowLost()));
                        }
                    if (col == 0)
                        clazz = (new StringBuilder(String.valueOf(clazz))).append(" trendBorder-l").toString();
                    stn.setClazz(clazz);
                    info.add(stn);
                }

            }

        }

        String foots[] = {
                "出现总次数", "最大遗漏值", "最大连出值"
        };
        String as[];
        int j = (as = foots).length;
        for (int i = 0; i < j; i++) {
            String title = as[i];
            SeasonTrend st = new SeasonTrend();
            st.setSeasonId(title);
            List<SeasonTrendNum> info = new ArrayList();
            st.setInfo(info);
            allTrends.add(st);
            for (int row = 0; row < getTitles().length; row++) {
                for (int col = 0; col < getNums().length; col++) {
                    int fullCol = row * getNums().length + col;
                    TrendCell last = (TrendCell) lasts.get(fullCol);
                    SeasonTrendNum stn = new SeasonTrendNum();
                    if ("出现总次数".equals(title))
                        stn.setNum(Integer.valueOf(last.getMaxOpen()));
                    else if ("最大遗漏值".equals(title))
                        stn.setNum(Integer.valueOf(last.getMaxLost()));
                    else
                        stn.setNum(Integer.valueOf(last.getMaxLian()));
                    if (col == 0)
                        stn.setClazz("trendBorder-l");
                    info.add(stn);
                }

            }

        }

    }

    public List<SeasonTrend> getTrends() {
        return this.trends;
    }

    public List<SeasonTrend> getAllTrends() {
        return this.allTrends;
    }
}
