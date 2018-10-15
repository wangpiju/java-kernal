package com.hs3.lotts.pk10.star5;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.pk10.star2.Pk10Star2DjPlayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Pk10Star5DjPlayer extends Pk10Star2DjPlayer {
    private BigDecimal bonus = new BigDecimal("1020").divide(new BigDecimal(2));
    private BigDecimal bonus2 = new BigDecimal("60").divide(new BigDecimal(2));
    private BigDecimal bonus3 = new BigDecimal("16").divide(new BigDecimal(2));
    private BigDecimal bonus4 = new BigDecimal("6").divide(new BigDecimal(2));
    private BigDecimal bonus5 = new BigDecimal("4").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "猜前五";
    private String basicBet = "01,02,03,04,05";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"}));
    private NumberView[] view = {new NumberView("冠军", this.nums), new NumberView("亚军", this.nums),
            new NumberView("三名", this.nums), new NumberView("四名", this.nums), new NumberView("五名", this.nums)};

    protected void init() {
        setRemark("冠军~第五名,各选一个号码，所选号码与开奖号码前5名车号任意一个相同，且顺序一致，即中奖：中1个5等奖，中2个4等奖，中3个3等奖，中4个2等奖，中5个1等奖");
        setExample("开奖：01,02,03,04,05,*,*,*,*,* 投注：01,02,03,04,05 中1等奖");
    }

    protected int getLen() {
        return 5;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getBonusStr() {
        return this.bonus5 + " - " + this.bonus;
    }

    public String getTitle() {
        return this.title;
    }

    public NumberView[] getNumView() {
        return this.view;
    }

    protected BigDecimal getWin(HashMap<Integer, Integer> m) {
        BigDecimal win = new BigDecimal("0");
        for (Integer winNum : m.keySet()) {
            Integer winCount = (Integer) m.get(winNum);
            if (winNum.intValue() == 1) {
                win = win.add(new BigDecimal(winCount.intValue()).multiply(this.bonus5));
            } else if (winNum.intValue() == 2) {
                win = win.add(new BigDecimal(winCount.intValue()).multiply(this.bonus4));
            } else if (winNum.intValue() == 3) {
                win = win.add(new BigDecimal(winCount.intValue()).multiply(this.bonus3));
            } else if (winNum.intValue() == 4) {
                win = win.add(new BigDecimal(winCount.intValue()).multiply(this.bonus2));
            } else if (winNum.intValue() == 5) {
                win = win.add(new BigDecimal(winCount.intValue()).multiply(this.bonus));
            }
        }
        return win;
    }

    public String getQunName() {
        return this.qunName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();
        Set<String> list = toAllBet(bets);

        BigDecimal allBonus4 = this.bonus4.subtract(this.bonus5.multiply(new BigDecimal("2")));

        BigDecimal allBonus3 = this.bonus3.subtract(this.bonus5.add(this.bonus4).multiply(new BigDecimal("3")));

        BigDecimal allBonus2 = this.bonus2.subtract(this.bonus5.add(this.bonus3).multiply(new BigDecimal("4")))
                .subtract(this.bonus4.multiply(new BigDecimal("6")));

        BigDecimal maxBonus = this.bonus.subtract(this.bonus5.add(this.bonus2).multiply(new BigDecimal("5")))
                .subtract(this.bonus4.add(this.bonus3).multiply(new BigDecimal("10")));
        for (String n : list) {
            addMap(result, getKey(n, new int[]{0}), this.bonus5);
            addMap(result, getKey(n, new int[]{1}), this.bonus5);
            addMap(result, getKey(n, new int[]{2}), this.bonus5);
            addMap(result, getKey(n, new int[]{3}), this.bonus5);
            addMap(result, getKey(n, new int[]{4}), this.bonus5);

            addMap(result, getKey(n, new int[]{0, 1}), allBonus4);
            addMap(result, getKey(n, new int[]{0, 2}), allBonus4);
            addMap(result, getKey(n, new int[]{0, 3}), allBonus4);
            addMap(result, getKey(n, new int[]{0, 4}), allBonus4);
            addMap(result, getKey(n, new int[]{1, 2}), allBonus4);
            addMap(result, getKey(n, new int[]{1, 3}), allBonus4);
            addMap(result, getKey(n, new int[]{1, 4}), allBonus4);
            addMap(result, getKey(n, new int[]{2, 3}), allBonus4);
            addMap(result, getKey(n, new int[]{2, 4}), allBonus4);
            addMap(result, getKey(n, new int[]{3, 4}), allBonus4);

            addMap(result, getKey(n, new int[]{0, 1, 2}), allBonus3);
            addMap(result, getKey(n, new int[]{0, 1, 3}), allBonus3);
            addMap(result, getKey(n, new int[]{0, 1, 4}), allBonus3);
            addMap(result, getKey(n, new int[]{0, 2, 3}), allBonus3);
            addMap(result, getKey(n, new int[]{0, 2, 4}), allBonus3);
            addMap(result, getKey(n, new int[]{0, 3, 4}), allBonus3);
            addMap(result, getKey(n, new int[]{1, 2, 3}), allBonus3);
            addMap(result, getKey(n, new int[]{1, 2, 4}), allBonus3);
            addMap(result, getKey(n, new int[]{1, 3, 4}), allBonus3);
            addMap(result, getKey(n, new int[]{2, 3, 4}), allBonus3);

            addMap(result, getKey(n, new int[]{0, 1, 2, 3}), allBonus2);
            addMap(result, getKey(n, new int[]{0, 1, 2, 4}), allBonus2);
            addMap(result, getKey(n, new int[]{0, 1, 3, 4}), allBonus2);
            addMap(result, getKey(n, new int[]{0, 2, 3, 4}), allBonus2);
            addMap(result, getKey(n, new int[]{1, 2, 3, 4}), allBonus2);

            addMap(result, getKey(n, new int[]{0, 1, 2, 3, 4}), maxBonus);
        }
        return result;
    }

    public String getBasicBet() {
        return basicBet;
    }

    public void setBasicBet(String basicBet) {
        this.basicBet = basicBet;
    }
}
