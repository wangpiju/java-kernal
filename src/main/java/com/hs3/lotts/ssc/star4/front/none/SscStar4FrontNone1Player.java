package com.hs3.lotts.ssc.star4.front.none;

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

public class SscStar4FrontNone1Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("5.82").divide(new BigDecimal(2));
    private String qunName = "前四";
    private String groupName = "";
    private String title = "一码不定位";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("从0-9中至少选择1个号码投注，竞猜开奖号码前四位中包含这个号码，包含即中奖");
        setExample("投注：1 开奖：1xxx*（不限顺序） 即中奖");
    }

    protected int index() {
        return 0;
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
        opens.add((Integer) openNums.get(index()));
        opens.add((Integer) openNums.get(index() + 1));
        opens.add((Integer) openNums.get(index() + 2));
        opens.add((Integer) openNums.get(index() + 3));
        for (Integer n : opens) {
            if (line.contains(n)) {
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
        Map<String, BigDecimal> result = new HashMap();

        List<Integer> line = ListUtils.toIntList(bets);

        String format = null;
        if (index() == 0) {
            format = "%s-";
        } else {
            format = "-%s";
        }

        /**jd-gui
         int n2;
         for (Iterator localIterator1 = line.iterator(); localIterator1.hasNext(); n2 <= 9)
         {
         int n1 = ((Integer)localIterator1.next()).intValue();
         n2 = 0; continue;
         for (int n3 = 0; n3 <= 9; n3++) {
         for (int n4 = 0; n4 <= 9; n4++)
         {
         String nK = String.format(format, new Object[] {n1 + n2 +
         n3 + n4 });
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
         Set<String> keys1 =
         PermutateUtils.getPerms(new Integer[] { Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3), Integer.valueOf(n4) });
         for (String key : keys1)
         {
         String k = String.format(format, new Object[] { key });
         addMap(result, k, b);
         }
         }
         }
         }
         n2++;
         }*/

        for (Iterator iterator = line.iterator(); iterator.hasNext(); ) {
            int n1 = ((Integer) iterator.next()).intValue();
            for (int n2 = 0; n2 <= 9; n2++) {
                for (int n3 = 0; n3 <= 9; n3++) {
                    for (int n4 = 0; n4 <= 9; n4++) {
                        String nK = String.format(format, new Object[]{
                                (new StringBuilder(String.valueOf(n1))).append(n2).append(n3).append(n4).toString()
                        });
                        if (!result.containsKey(nK)) {
                            BigDecimal b = getBonus();
                            if (n2 != n1 && line.contains(Integer.valueOf(n2)))
                                b = b.add(getBonus());
                            if (n3 != n1 && n3 != n2 && line.contains(Integer.valueOf(n3)))
                                b = b.add(getBonus());
                            if (n4 != n1 && n4 != n2 && n4 != n3 && line.contains(Integer.valueOf(n4)))
                                b = b.add(getBonus());
                            Set<String> keys1 = PermutateUtils.getPerms(new Integer[]{
                                    Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3), Integer.valueOf(n4)
                            });
                            String k;
                            for (Iterator iterator1 = keys1.iterator(); iterator1.hasNext(); addMap(result, k, b)) {
                                String key = (String) iterator1.next();
                                k = String.format(format, new Object[]{
                                        key
                                });
                            }

                        }
                    }

                }

            }

        }

        return result;
    }
}
