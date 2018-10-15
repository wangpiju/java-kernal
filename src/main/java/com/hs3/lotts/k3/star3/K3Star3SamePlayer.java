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
import java.util.List;
import java.util.Map;
import java.util.Set;

public class K3Star3SamePlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("432").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "三同号";
    private List<String> nums = new ArrayList<String>(
            Arrays.asList("111", "222", "333", "444", "555", "666"));
    private List<String> nums1 = new ArrayList<String>(Arrays.asList("111", "222", "333"));
    private List<String> nums2 = new ArrayList<String>(Arrays.asList("444", "555", "666"));
    private NumberView[] view = {new NumberView("", this.nums1, false), new NumberView("", this.nums2, false)};

    protected void init() {
        setRemark("对豹子号（111，222，333，444，555，666）进行单选或通选投注，选号与开奖号相同即中奖");
        setExample("投注：111 开奖：111 即中奖");
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
        if (n.size() != 1) {
            return win;
        }
        for (String line : lines) {
            List<Integer> ns = LotteryUtils.toIntListByLength(line, 1);
            int one = ns.get(0);
            if (n.contains(one)) {
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
        Map<String, BigDecimal> result = new HashMap<>();

        List<String> lines = ListUtils.toList(bets);
        for (String line : lines) {
            result.put(line, getBonus());
        }
        return result;
    }
}
