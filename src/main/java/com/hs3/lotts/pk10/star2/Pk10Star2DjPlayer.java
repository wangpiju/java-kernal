package com.hs3.lotts.pk10.star2;

import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PermutateUtils;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Pk10Star2DjPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("113").divide(new BigDecimal(2));
    private BigDecimal bonus2 = new BigDecimal("4").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "猜前二";
    private String basicBet = "01,02";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"}));
    private NumberView[] view = {new NumberView("冠军", this.nums), new NumberView("亚军", this.nums)};

    protected void init() {
        setRemark("冠军、亚军各选一个号码，所选号码与开奖号码前2名车号任意一个相同，且顺序一致，即中奖");
        setExample("开奖：01,02,*,*,*,*,*,*,*,* 投注：01,02 中1等奖  投注:01,03或04,02中2等奖");
    }

    protected int getLen() {
        return 2;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getBonusStr() {
        return this.bonus2 + " - " + this.bonus;
    }

    public String getTitle() {
        return this.title;
    }

    public NumberView[] getNumView() {
        return this.view;
    }

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets);
        if (lines.size() != getLen()) {
            return Integer.valueOf(0);
        }
        List<List<String>> all = new ArrayList();
        for (String line : lines) {
            List<String> n = LotteryUtils.toListByLength(line, Integer.valueOf(2));
            if (ListUtils.hasSame(n)) {
                return Integer.valueOf(0);
            }
            if (n.retainAll(this.nums)) {
                return Integer.valueOf(0);
            }
            all.add(n);
        }
        int count = LotteryUtils.getCount(all);
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        Set<String> bs = toAllBet(bets);

        HashMap<Integer, Integer> m = new HashMap();
        for (String b : bs) {
            List<Integer> list = ListUtils.toIntList(b);
            int n = 0;
            for (int i = 0; i < getLen(); i++) {
                Integer n1 = (Integer) list.get(i);
                Integer o1 = (Integer) openNums.get(i);
                if (n1 == o1) {
                    n++;
                }
            }
            if (n > 0) {
                Integer winNum = (Integer) m.get(Integer.valueOf(n));
                if (winNum == null) {
                    winNum = Integer.valueOf(1);
                } else {
                    winNum = Integer.valueOf(winNum.intValue() + 1);
                }
                m.put(Integer.valueOf(n), winNum);
            }
        }
        return getWin(m);
    }

    protected BigDecimal getWin(HashMap<Integer, Integer> m) {
        BigDecimal win = new BigDecimal("0");
        for (Integer winNum : m.keySet()) {
            Integer winCount = (Integer) m.get(winNum);
            if (winNum.intValue() == 1) {
                win = win.add(new BigDecimal(winCount.intValue()).multiply(this.bonus2));
            } else if (winNum.intValue() == 2) {
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

    protected Set<String> toAllBet(String bets) {
        List<List<Integer>> allNum = new ArrayList();
        List<String> lines = ListUtils.toList(bets);
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 2);
            allNum.add(n);
        }
        Set<String> bs = PermutateUtils.getAllNum(allNum, ",", false);
        return bs;
    }

    protected String getKey(String ns, int... len) {
        List<Integer> num = ListUtils.toIntList(ns);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            boolean has = false;
            for (int n : len) {
                if (n == i) {
                    has = true;
                    break;
                }
            }
            if (has) {
                sb.append(",").append(num.get(i));
            } else {
                sb.append(",-");
            }
        }
        return sb.substring(1).toString();
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();
        Set<String> list = toAllBet(bets);
        BigDecimal maxBonus = this.bonus.subtract(this.bonus2.multiply(new BigDecimal("2")));
        for (String n : list) {
            addMap(result, getKey(n, new int[]{0}), this.bonus2);
            addMap(result, getKey(n, new int[]{1}), this.bonus2);

            addMap(result, getKey(n, new int[]{0, 1}), maxBonus);
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
