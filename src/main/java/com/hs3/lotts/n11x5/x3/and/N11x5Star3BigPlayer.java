package com.hs3.lotts.n11x5.x3.and;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class N11x5Star3BigPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("1.85");


    private NumberView[] view = {new NumberView("", Arrays.asList("06", "07", "08", "09", "10", "11", "12", "13"), false),
            new NumberView("", Arrays.asList("14", "15", "16", "17", "18", "19", "20", "21"), false),
            new NumberView("", Arrays.asList("22", "23", "24", "25", "26", "27", "28", "29"), false),
            new NumberView("", Arrays.asList("30", "大", "小", "单", "双"), false)};


    protected int index() {
        return 0;
    }

    protected void init() {
        setRemark("至少选择1个和值投注，选号与开奖的中间三个号码相加的数值一致即中奖");
        setExample("投注：06 开奖：X,02,03,01,Y");
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal("7.538").divide(new BigDecimal("2"), 2, BigDecimal.ROUND_DOWN));
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
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
        return "大";
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
        List<String> lines = ListUtils.toList(bets);
        BigDecimal win = new BigDecimal(0);
        //中三和值
        int all = 0;

        for (int i=0; i< openNums.size();i++ ) {
            //非頭尾
            if(i != 0 && i != 4){
                int o = openNums.get(i);
                all += o;
            }
        }

        boolean isBig = (18 <= all && 30 >= all);

        for (String line: lines) {
            if ((line.equals("大")) && (isBig)) {
                win = win.add(getBonus());
            }
        }
        return win;

    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        return new HashMap<>();
    }

}
