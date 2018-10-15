package com.hs3.lotts.ssc.star5.none;

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

public class SscStar5None3Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("45.98").divide(new BigDecimal(2));
    private String qunName = "五星";
    private String groupName = "";
    private String title = "三码不定位";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {
            new NumberView("", this.nums)};

    protected void init() {
        setRemark("从0-9中至少选择3个号码投注，竞猜开奖号码中包含这3个号码，包含即中奖");
        setExample("投注：1，2，3 开奖：1x2x3（不限顺序）");
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
        if (line.size() < 3) {
            return Integer.valueOf(0);
        }
        if (ListUtils.hasSame(line)) {
            return Integer.valueOf(0);
        }
        if (line.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        int count = LotteryUtils.getCombin(line.size(), 3);
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        List<Integer> line = ListUtils.toIntList(bets);
        Set<Integer> nums = new HashSet();
        nums.addAll(openNums);
        int i = 0;
        for (Integer num : nums) {
            if (line.contains(num)) {
                i++;
            }
        }
        win = getBonus().multiply(new BigDecimal(LotteryUtils.getCombin(i, 3)));
        return win;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        /**jd-gui
         Map<String, BigDecimal> result = new HashMap();

         List<Integer> line = ListUtils.toIntList(bets);
         for (int i1 = 0; i1 < line.size(); i1++)
         {
         int n1 = ((Integer)line.get(i1)).intValue();
         for (int i2 = i1 + 1; i2 < line.size(); i2++)
         {
         int n2 = ((Integer)line.get(i2)).intValue();
         for (int i3 = i2 + 1; i3 < line.size(); i3++)
         {
         int n3 = ((Integer)line.get(i3)).intValue();
         for (int n4 = 0; n4 <= 9; n4++) {
         for (int n5 = 0; n5 <= 9; n5++)
         {
         String nK = n1 + n2 +
         n3 + n4 + n5;
         if (!result.containsKey(nK))
         {
         int count = 3;
         if ((n4 != n1) && (n4 != n2) && (n4 != n3) &&
         (line.contains(Integer.valueOf(n4)))) {
         count++;
         }
         if ((n5 != n1) && (n5 != n2) && (n5 != n3) && (n5 != n4) &&
         (line.contains(Integer.valueOf(n5)))) {
         count++;
         }
         count = LotteryUtils.getCombin(count, 3);
         BigDecimal b = getBonus().multiply(new BigDecimal(count));

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
         }
         }
         return result;*/

        Map<String, BigDecimal> result = new HashMap();
        List<Integer> line = ListUtils.toIntList(bets);
        for (int i1 = 0; i1 < line.size(); i1++) {
            int n1 = ((Integer) line.get(i1)).intValue();
            for (int i2 = i1 + 1; i2 < line.size(); i2++) {
                int n2 = ((Integer) line.get(i2)).intValue();
                for (int i3 = i2 + 1; i3 < line.size(); i3++) {
                    int n3 = ((Integer) line.get(i3)).intValue();
                    for (int n4 = 0; n4 <= 9; n4++) {
                        for (int n5 = 0; n5 <= 9; n5++) {
                            String nK = (new StringBuilder(String.valueOf(n1))).append(n2).append(n3).append(n4).append(n5).toString();
                            if (!result.containsKey(nK)) {
                                int count = 3;
                                if (n4 != n1 && n4 != n2 && n4 != n3 && line.contains(Integer.valueOf(n4)))
                                    count++;
                                if (n5 != n1 && n5 != n2 && n5 != n3 && n5 != n4 && line.contains(Integer.valueOf(n5)))
                                    count++;
                                count = LotteryUtils.getCombin(count, 3);
                                BigDecimal b = getBonus().multiply(new BigDecimal(count));
                                Set<String> keys1 = PermutateUtils.getPerms(new Integer[]{
                                        Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3), Integer.valueOf(n4), Integer.valueOf(n5)
                                });
                                String k;
                                for (Iterator iterator = keys1.iterator(); iterator.hasNext(); addMap(result, k, b))
                                    k = (String) iterator.next();

                            }
                        }

                    }

                }

            }

        }

        return result;

    }
}
