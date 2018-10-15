package com.hs3.lotts.ssc.sides;

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

public class SscSideTemaPlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("4.88").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "特码";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("从0-9中至少选择1个号码投注，竞猜开奖号码中包含这个号码，包含即中奖");
        setExample("投注：1 开奖：xx1xx（不限顺序）");
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getQunName() {
        return this.qunName;
    }

    public String getGroupName() {
        return this.groupName;
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
        Set<Integer> nums = new HashSet();
        nums.addAll(openNums);
        for (Integer num : nums) {
            if (line.contains(num)) {
                win = win.add(getBonus());
            }
        }
        return win;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        /**jd-gui
         Map<String, BigDecimal> result = new HashMap();

         List<Integer> line = ListUtils.toIntList(bets);
         int n2;
         for (Iterator localIterator1 = line.iterator(); localIterator1.hasNext(); n2 <= 9)
         {
         int n1 = ((Integer)localIterator1.next()).intValue();
         n2 = 0; continue;
         for (int n3 = 0; n3 <= 9; n3++) {
         for (int n4 = 0; n4 <= 9; n4++) {
         for (int n5 = 0; n5 <= 9; n5++)
         {
         String nK = n1 + n2 +
         n3 + n4 + n5;
         if (!result.containsKey(nK))
         {
         BigDecimal b = getBonus();
         if ((n2 != n1) &&
         (line.contains(Integer.valueOf(n2)))) {
         b = b.add(getBonus());
         }
         if ((n3 != n1) && (n3 != n2) &&
         (line.contains(Integer.valueOf(n3)))) {
         b = b.add(getBonus());
         }
         if ((n4 != n1) && (n4 != n2) && (n4 != n3) &&
         (line.contains(Integer.valueOf(n4)))) {
         b = b.add(getBonus());
         }
         if ((n5 != n1) && (n5 != n2) && (n5 != n3) && (n5 != n4) &&
         (line.contains(Integer.valueOf(n5)))) {
         b = b.add(getBonus());
         }
         Set<String> keys1 =
         PermutateUtils.getPerms(new Integer[] { Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3), Integer.valueOf(n4),
         Integer.valueOf(n5) });
         for (String k : keys1) {
         addMap(result, k, b);
         }
         }
         }
         }
         }
         n2++;
         }
         return result;
         */

        Map<String, BigDecimal> result = new HashMap();
        List<Integer> line = ListUtils.toIntList(bets);
        for (Iterator iterator = line.iterator(); iterator.hasNext(); ) {
            int n1 = ((Integer) iterator.next()).intValue();
            for (int n2 = 0; n2 <= 9; n2++) {
                for (int n3 = 0; n3 <= 9; n3++) {
                    for (int n4 = 0; n4 <= 9; n4++) {
                        for (int n5 = 0; n5 <= 9; n5++) {
                            String nK = (new StringBuilder(String.valueOf(n1))).append(n2).append(n3).append(n4).append(n5).toString();
                            if (!result.containsKey(nK)) {
                                BigDecimal b = getBonus();
                                if (n2 != n1 && line.contains(Integer.valueOf(n2)))
                                    b = b.add(getBonus());
                                if (n3 != n1 && n3 != n2 && line.contains(Integer.valueOf(n3)))
                                    b = b.add(getBonus());
                                if (n4 != n1 && n4 != n2 && n4 != n3 && line.contains(Integer.valueOf(n4)))
                                    b = b.add(getBonus());
                                if (n5 != n1 && n5 != n2 && n5 != n3 && n5 != n4 && line.contains(Integer.valueOf(n5)))
                                    b = b.add(getBonus());
                                Set keys1 = PermutateUtils.getPerms(new Integer[]{
                                        Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3), Integer.valueOf(n4), Integer.valueOf(n5)
                                });
                                String k;
                                for (Iterator iterator1 = keys1.iterator(); iterator1.hasNext(); addMap(result, k, b))
                                    k = (String) iterator1.next();

                            }
                        }

                    }

                }

            }

        }

        return result;

    }
}
