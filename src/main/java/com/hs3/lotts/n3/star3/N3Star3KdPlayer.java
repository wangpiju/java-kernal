package com.hs3.lotts.n3.star3;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PermutateUtils;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;
import com.hs3.utils.NumUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class N3Star3KdPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("2000").divide(new BigDecimal(2));
    private String qunName = "三星";
    private String groupName = "直选";
    private String title = "跨度";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1),
            Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6),
            Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private Integer[] betCount = {Integer.valueOf(10), Integer.valueOf(54), Integer.valueOf(96), Integer.valueOf(126),
            Integer.valueOf(144), Integer.valueOf(150), Integer.valueOf(144), Integer.valueOf(126), Integer.valueOf(96),
            Integer.valueOf(54)};
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("所选数值等于开奖号码的最大与最小数字相减之差，即为中奖");
        setExample("投注：跨度8 开奖：(1)数字08x（不限顺序）,x≠9; (2)数字19x（不限顺序），x≠0");
    }

    public String getTitle() {
        return this.title;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public NumberView[] getNumView() {
        return this.view;
    }

    public Integer getCount(String bets) {
        int count = 0;
        List<Integer> line = ListUtils.toIntList(bets);
        if (ListUtils.hasSame(line)) {
            return Integer.valueOf(0);
        }
        if (line.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        for (Integer n : line) {
            count += this.betCount[n.intValue()].intValue();
        }
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        List<Integer> line = ListUtils.toIntList(bets);
        Integer n1 = (Integer) openNums.get(0);
        Integer n2 = (Integer) openNums.get(1);
        Integer n3 = (Integer) openNums.get(2);
        Integer max = NumUtils.getMax(n1.intValue(), new int[]{n2.intValue(), n3.intValue()});
        Integer min = NumUtils.getMin(n1.intValue(), new int[]{n2.intValue(), n3.intValue()});

        int n = max.intValue() - min.intValue();
        if (line.contains(Integer.valueOf(n))) {
            win = win.add(this.bonus);
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
         * jd-gui int n1; for (Iterator localIterator1 = line.iterator();
         * localIterator1.hasNext(); n1 <= 9) { Integer n =
         * (Integer)localIterator1.next(); n1 = n.intValue(); continue; Integer n2 =
         * Integer.valueOf(n1 - n.intValue());
         *
         * int max = NumUtils.getMax(n1, new int[] { n2.intValue() }).intValue(); int
         * min = NumUtils.getMin(n1, new int[] { n2.intValue() }).intValue(); for (int
         * n3 = min; n3 <= max; n3++) { Set<String> keys1 = PermutateUtils.getPerms(new
         * Integer[] { Integer.valueOf(n1), n2, Integer.valueOf(n3) }); for (String key
         * : keys1) { result.put(key, getBonus()); } } n1++; }
         */

        for (Iterator iterator = line.iterator(); iterator.hasNext(); ) {
            Integer n = (Integer) iterator.next();
            for (int n1 = n.intValue(); n1 <= 9; n1++) {
                Integer n2 = Integer.valueOf(n1 - n.intValue());
                int max = NumUtils.getMax(n1, new int[]{n2.intValue()}).intValue();
                int min = NumUtils.getMin(n1, new int[]{n2.intValue()}).intValue();
                for (int n3 = min; n3 <= max; n3++) {
                    Set keys1 = PermutateUtils.getPerms(new Integer[]{Integer.valueOf(n1), n2, Integer.valueOf(n3)});
                    String key;
                    for (Iterator iterator1 = keys1.iterator(); iterator1.hasNext(); result.put(key, getBonus()))
                        key = (String) iterator1.next();

                }

            }

        }

        return result;
    }
}
