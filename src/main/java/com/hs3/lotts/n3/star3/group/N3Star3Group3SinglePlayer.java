package com.hs3.lotts.n3.star3.group;

import com.hs3.lotts.LotteryUtils;
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

public class N3Star3Group3SinglePlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("666.66").divide(new BigDecimal(2));
    private String qunName = "三星";
    private String groupName = "";
    private String title = "组三单式";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1),
            Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6),
            Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));

    protected void init() {
        setRemark("从0-9中选择2个数字组成两注，所选号码与开奖号码相同，顺序不限");
        setExample("投注：112 开奖：112（不限顺序）");
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getTitle() {
        return this.title;
    }

    public NumberView[] getNumView() {
        return null;
    }

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            if (n.size() != 2) {
                return Integer.valueOf(0);
            }
            if (ListUtils.hasSame(n)) {
                return Integer.valueOf(0);
            }
            if (n.retainAll(this.nums)) {
                return Integer.valueOf(0);
            }
        }
        return Integer.valueOf(lines.size() * 2);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");

        Set<Integer> opens = new HashSet();
        opens.add((Integer) openNums.get(0));
        opens.add((Integer) openNums.get(1));
        opens.add((Integer) openNums.get(2));
        if (opens.size() != 2) {
            return win;
        }
        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            if ((opens.contains(n.get(0))) && (opens.contains(n.get(1)))) {
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

        List<String> lines = ListUtils.toList(bets, "\\s+");

        /**
         * jd-gui Iterator localIterator2; for (Iterator localIterator1 =
         * lines.iterator(); localIterator1.hasNext(); localIterator2.hasNext()) {
         * String lineStr = (String)localIterator1.next(); List<Integer> line =
         * LotteryUtils.toIntListByLength(lineStr, 1); int n1 =
         * ((Integer)line.get(0)).intValue(); int n2 =
         * ((Integer)line.get(1)).intValue();
         *
         * Set<String> keys1 = PermutateUtils.getPerms(new Integer[] {
         * Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n2) }); Set<String>
         * keys2 = PermutateUtils.getPerms(new Integer[] { Integer.valueOf(n1),
         * Integer.valueOf(n2), Integer.valueOf(n2) }); keys1.addAll(keys2);
         *
         * localIterator2 = keys1.iterator(); continue;String key =
         * (String)localIterator2.next(); addMap(result, key, getBonus()); }
         */

        for (Iterator iterator = lines.iterator(); iterator.hasNext(); ) {
            String lineStr = (String) iterator.next();
            List<Integer> line = LotteryUtils.toIntListByLength(lineStr, 1);
            int n1 = ((Integer) line.get(0)).intValue();
            int n2 = ((Integer) line.get(1)).intValue();
            Set<String> keys1 = PermutateUtils
                    .getPerms(new Integer[]{Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n2)});
            Set<String> keys2 = PermutateUtils
                    .getPerms(new Integer[]{Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n2)});
            keys1.addAll(keys2);
            String key;
            for (Iterator iterator1 = keys1.iterator(); iterator1.hasNext(); addMap(result, key, getBonus()))
                key = (String) iterator1.next();

        }

        return result;
    }
}
