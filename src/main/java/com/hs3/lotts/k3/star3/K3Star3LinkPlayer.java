package com.hs3.lotts.k3.star3;

import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class K3Star3LinkPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("72").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "三连号";
    private List<String> nums = new ArrayList<>(Arrays.asList("123", "234", "345", "456"));
    private NumberView[] view = {new NumberView("", this.nums, false)};

    protected void init() {
        setRemark("对所有3个相连的号码（123，234，345，456)进行单选或多选投注，选号与开奖号相同（顺序不限）即中奖");
        setExample("投注：123 开奖：123 即中奖");
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getTitle() {
        return this.title;
    }

    public NumberView[] getNumView() {
        return this.view;
    }

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets);
        if (ListUtils.hasSame(lines)) {
            return 0;
        }
        if (lines.retainAll(this.nums)) {
            return 0;
        }
        return lines.size();
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<String> lines = ListUtils.toList(bets);
        BigDecimal win = new BigDecimal("0");

        Set<Integer> n = new HashSet<>(openNums);
        if (n.size() != 3) {
            return win;
        }
        for (String line : lines) {
            List<Integer> ns = LotteryUtils.toIntListByLength(line, 1);
            if (n.containsAll(ns)) {
                win = win.add(getBonus());
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
        /**
         * jd-gui Map<String, BigDecimal> result = new HashMap();
         *
         * List<String> lines = ListUtils.toList(bets); for (String line : lines) {
         * List<Integer> ns = LotteryUtils.toIntListByLength(line, 1);
         *
         * int n1 = ((Integer)ns.get(0)); int n2 =
         * ((Integer)ns.get(1)); int n3 = ((Integer)ns.get(2));
         *
         * result.put(n1 + n2 + n3, getBonus()); result.put(n1 + n3 + n2, getBonus());
         * result.put(n2 + n1 + n3, getBonus()); result.put(n2 + n3 + n1, getBonus());
         * result.put(n3 + n1 + n2, getBonus()); result.put(n3 + n2 + n1, getBonus()); }
         * return result;
         */

        Map<String, BigDecimal> result = new HashMap<>();
        List<String> lines = ListUtils.toList(bets);
        int n1;
        int n2;
        int n3;
        for (Iterator iterator = lines.iterator(); iterator.hasNext(); result
                .put((new StringBuilder(String.valueOf(n3))).append(n2).append(n1).toString(), getBonus())) {
            String line = (String) iterator.next();
            List<Integer> ns = LotteryUtils.toIntListByLength(line, 1);
            n1 = ns.get(0);
            n2 = ns.get(1);
            n3 = ns.get(2);
            result.put((new StringBuilder(String.valueOf(n1))).append(n2).append(n3).toString(), getBonus());
            result.put((new StringBuilder(String.valueOf(n1))).append(n3).append(n2).toString(), getBonus());
            result.put((new StringBuilder(String.valueOf(n2))).append(n1).append(n3).toString(), getBonus());
            result.put((new StringBuilder(String.valueOf(n2))).append(n3).append(n1).toString(), getBonus());
            result.put((new StringBuilder(String.valueOf(n3))).append(n1).append(n2).toString(), getBonus());
        }

        return result;

    }
}
