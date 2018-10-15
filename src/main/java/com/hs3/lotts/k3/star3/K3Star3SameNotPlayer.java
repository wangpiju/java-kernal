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

public class K3Star3SameNotPlayer extends PlayerBase {

    private BigDecimal bonus;
    private String qunName;
    private String groupName;
    private String title;
    private List<String> nums;
    private List<String> nums1;
    private List<String> nums2;
    private List<String> nums3;
    private List<String> nums4;
    private NumberView view[];

    public K3Star3SameNotPlayer() {
        bonus = new BigDecimal("72").divide(new BigDecimal(2));
        qunName = "";
        groupName = "";
        title = "三不同";
        nums = new ArrayList<>(Arrays.asList("123", "124", "125", "126", "134", "135", "136",
                "145", "146", "156", "234", "235", "236", "245", "246", "256", "345", "346", "356", "456"));
        nums1 = new ArrayList<>(Arrays.asList("123", "124", "125", "126", "134"));
        nums2 = new ArrayList<>(Arrays.asList("135", "136", "145", "146", "156"));
        nums3 = new ArrayList<>(Arrays.asList("234", "235", "236", "245", "246"));
        nums4 = new ArrayList<>(Arrays.asList("256", "345", "346", "356", "456"));
        view = (new NumberView[]{new NumberView("", nums1, false), new NumberView("", nums2, false),
                new NumberView("", nums3, false), new NumberView("", nums4, false)});
    }

    protected void init() {
        setRemark("对所有3不同号进行单选或多选，选号与开奖号相同（顺序不限）即中奖");
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
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        List<String> lines = ListUtils.toList(bets);
        int n1;
        int n2;
        int n3;
        for (String line : lines) {
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
