package com.hs3.lotts.n3.star3.none;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PermutateUtils;
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

public class N3Star3None1Player extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("7.38").divide(new BigDecimal(2));
    private String qunName = "三星";
    private String groupName = "";
    private String title = "一码不定位";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1),
            Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6),
            Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("从0-9中至少选择1个号码投注，竞猜开奖号码中包含这个号码，包含即中奖");
        setExample("投注：1 开奖：1xx（不限顺序） 即中奖");
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
        List<Integer> line = ListUtils.toIntList(bets);
        if (ListUtils.hasSame(line)) {
            return Integer.valueOf(0);
        }
        if (line.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(line.size());
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        List<Integer> line = ListUtils.toIntList(bets);
        Set<Integer> opens = new HashSet();
        opens.add((Integer) openNums.get(0));
        opens.add((Integer) openNums.get(1));
        opens.add((Integer) openNums.get(2));
        for (Integer n : opens) {
            if (line.contains(n)) {
                win = win.add(this.bonus);
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

        List<Integer> line = ListUtils.toIntList(bets);

        /**
         * jd-gui int n2; for (Iterator localIterator1 = line.iterator();
         * localIterator1.hasNext(); n2 <= 9) { int n1 =
         * ((Integer)localIterator1.next()).intValue(); n2 = 0; continue; for (int n3 =
         * 0; n3 <= 9; n3++) { String nK = n1 + n2 + n3; if (!result.containsKey(nK)) {
         * BigDecimal b = getBonus(); if ((line.contains(Integer.valueOf(n2))) && (n2 !=
         * n1)) { b = b.add(getBonus()); } if ((line.contains(Integer.valueOf(n3))) &&
         * (n3 != n1) && (n3 != n2)) { b = b.add(getBonus()); } Set<String> keys1 =
         * PermutateUtils.getPerms(new Integer[] { Integer.valueOf(n1),
         * Integer.valueOf(n2), Integer.valueOf(n3) }); for (String key : keys1) {
         * addMap(result, key, b); } } } n2++; }
         */

        for (Iterator iterator = line.iterator(); iterator.hasNext(); ) {
            int n1 = ((Integer) iterator.next()).intValue();
            for (int n2 = 0; n2 <= 9; n2++) {
                for (int n3 = 0; n3 <= 9; n3++) {
                    String nK = (new StringBuilder(String.valueOf(n1))).append(n2).append(n3).toString();
                    if (!result.containsKey(nK)) {
                        BigDecimal b = getBonus();
                        if (line.contains(Integer.valueOf(n2)) && n2 != n1)
                            b = b.add(getBonus());
                        if (line.contains(Integer.valueOf(n3)) && n3 != n1 && n3 != n2)
                            b = b.add(getBonus());
                        Set<String> keys1 = PermutateUtils.getPerms(
                                new Integer[]{Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3)});
                        String key;
                        for (Iterator iterator1 = keys1.iterator(); iterator1.hasNext(); addMap(result, key, b))
                            key = (String) iterator1.next();

                    }
                }

            }

        }

        return result;
    }
}
