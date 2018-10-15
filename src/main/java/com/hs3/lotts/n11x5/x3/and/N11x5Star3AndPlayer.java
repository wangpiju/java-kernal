package com.hs3.lotts.n11x5.x3.and;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.k3.star3.K3Star3BigOddPlayer;
import com.hs3.utils.CollectionUtils;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class N11x5Star3AndPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("990");
    private BigDecimal bonusBig = new BigDecimal("1.85");
    private BigDecimal bonusSmall = new BigDecimal("2.17");
    private BigDecimal bonusOdd = new BigDecimal("2.06");
    private BigDecimal bonusEven = new BigDecimal("1.94");

    private NumberView[] view = {new NumberView("中三和值", Arrays.asList("06", "07", "08", "09", "10", "11", "12", "13"), false),
            new NumberView("", Arrays.asList("14", "15", "16", "17", "18", "19", "20", "21"), false),
            new NumberView("", Arrays.asList("22", "23", "24", "25", "26", "27", "28", "29"), false),
            new NumberView("", Arrays.asList("30", "大", "小", "单", "双"), false)};

    private Integer[] betCount =
            //6 ~ 30投注項的注數
            {6, 6, 12, 18, 24, 30,42,48,60,66,72,72,78,72,72,66,60,48,42,30,24,18,12,6,6,
            //大小單雙
            1, 1,
            1, 1};

    protected int index() {
        return 0;
    }

    protected void init() {
        setRemark("至少选择1个和值投注，选号与开奖的中间三个号码相加的数值一致即中奖");
        setExample("投注：06 开奖：X,02,03,01,Y");
    }

    public static void main(String[] args){
        System.out.println(new BigDecimal("7.538").divide(new BigDecimal("2"), 2, BigDecimal.ROUND_DOWN));
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    private BigDecimal getBonusBig() {
        return bonusBig;
    }

    public void setBonusBig(BigDecimal bonusBig) {
        this.bonusBig = bonusBig;
    }

    private BigDecimal getBonusSmall() {
        return bonusSmall;
    }

    public void setBonusSmall(BigDecimal bonusSmall) {
        this.bonusSmall = bonusSmall;
    }

    private BigDecimal getBonusOdd() {
        return bonusOdd;
    }

    public void setBonusOdd(BigDecimal bonusOdd) {
        this.bonusOdd = bonusOdd;
    }

    public BigDecimal getBonusEven() {
        return bonusEven;
    }

    public void setBonusEven(BigDecimal bonusEven) {
        this.bonusEven = bonusEven;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getQunName() {
        return "";
    }

    public String getGroupName() {
        return "中三";
    }

    public String getTitle() {
        return "中三和值";
    }

    public NumberView[] getNumView() {
        return this.view;
    }

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets);

        if (ListUtils.hasSame(lines)) {
            return 0;
        }

        return lines.size();
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<Integer> lines2 = ListUtils.toIntList(bets, ",");
        BigDecimal win = new BigDecimal("0");

        //中三和值
        int all = 0;

        for (int i=0; i< openNums.size();i++ ) {
            //非頭尾
            if(i != 0 && i != 4){
                int o = openNums.get(i);
                all += o;
            }
        }
        //比對大小單雙
        win = win.add(getSideWin(bets, all));
        //比對和值
        if (lines2.contains(all)) {
            try {
                win = win.add(getBonus().divide(new BigDecimal(betCount[all - 6]), 2, BigDecimal.ROUND_DOWN));
            } catch (Exception e) {
               e.printStackTrace();
            }
        }

        return win;

    }

    private BigDecimal getSideWin(String bets, Integer all) {
        List<String> lines = ListUtils.toList(bets);
        BigDecimal win = new BigDecimal(0);
        boolean isOdd = all % 2 != 0;
        boolean isBig = (18 <= all && 30 >= all);
        boolean isSmall = (6 <= all && 17 >= all);

        for (String line: lines) {
            if ((line.equals("大")) && (isBig)) {
                win = win.add(getBonusBig());
            } else if ((line.equals("小")) && (isSmall)) {
                win = win.add(getBonusSmall());
            } else if ((line.equals("单")) && (isOdd)) {
                win = win.add(getBonusOdd());
            } else if ((line.equals("双")) && (!isOdd)) {
                win = win.add(getBonusOdd());
            }
        }
        return win;
    }


    public Map<String, BigDecimal> ifOpenWin(String bets) {
        return new HashMap<>();
    }

}
