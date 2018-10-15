package com.hs3.lotts.pk10.star3;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.pk10.star2.Pk10Star2DjPlayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Pk10Star3DjPlayer extends Pk10Star2DjPlayer {
    private BigDecimal bonus = new BigDecimal("323").divide(new BigDecimal(2));
    private BigDecimal bonus2 = new BigDecimal("20").divide(new BigDecimal(2));
    private BigDecimal bonus3 = new BigDecimal("4").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "猜前三";
    private String basicBet = "01,02,03";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"}));
    private NumberView[] view = {new NumberView("冠军", this.nums), new NumberView("亚军", this.nums),
            new NumberView("三名", this.nums)};

    protected void init() {
        setRemark("冠军~第三名,各选一个号码，所选号码与开奖号码前3名车号任意一个相同，且顺序一致，即中奖：中1个3等奖，中2个2等奖，中3个1等奖");
        setExample("开奖：01,02,03,*,*,*,*,*,*,* 投注：01,02,03 中1等奖");
    }

    protected int getLen() {
        return 3;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getBonusStr() {
        return this.bonus3 + " - " + this.bonus;
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
                win = win.add(new BigDecimal(winCount.intValue()).multiply(this.bonus3));
            } else if (winNum.intValue() == 2) {
                win = win.add(new BigDecimal(winCount.intValue()).multiply(this.bonus2));
            } else if (winNum.intValue() == 3) {
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
        BigDecimal maxBonus = this.bonus.subtract(this.bonus2.add(this.bonus3).multiply(new BigDecimal("2")));
        BigDecimal allBonus2 = this.bonus2.subtract(this.bonus3.multiply(new BigDecimal("2")));
        for (String n : list) {
            addMap(result, getKey(n, new int[]{0}), this.bonus3);
            addMap(result, getKey(n, new int[]{1}), this.bonus3);
            addMap(result, getKey(n, new int[]{2}), this.bonus3);

            addMap(result, getKey(n, new int[]{0, 1}), allBonus2);
            addMap(result, getKey(n, new int[]{0, 2}), allBonus2);
            addMap(result, getKey(n, new int[]{1, 2}), allBonus2);

            addMap(result, getKey(n, new int[]{0, 1, 2}), maxBonus);
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
