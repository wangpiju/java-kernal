package com.hs3.lotts.ssc.star3.front.group;

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

public class SscStar3FrontGroup6SinglePlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("333.33").divide(new BigDecimal(2));
    private String qunName = "前三";
    private String groupName = "";
    private String title = "组六单式";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));

    protected void init() {
        setRemark("从0-9中任意选择3个号码组成一注，所选号码与开奖号码的前三位相同，顺序不限");
        setExample("投注：123** 开奖：123**（不限顺序）");
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
        return null;
    }

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            if (n.size() != 3) {
                return Integer.valueOf(0);
            }
            if (ListUtils.hasSame(n)) {
                return Integer.valueOf(0);
            }
            if (n.retainAll(this.nums)) {
                return Integer.valueOf(0);
            }
        }
        return Integer.valueOf(lines.size());
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        Set<Integer> opens = new HashSet();
        opens.add((Integer) openNums.get(index()));
        opens.add((Integer) openNums.get(index() + 1));
        opens.add((Integer) openNums.get(index() + 2));

        BigDecimal win = new BigDecimal("0");
        if (opens.size() != 3) {
            return win;
        }
        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            if ((opens.contains(n.get(0))) && (opens.contains(n.get(1))) && (opens.contains(n.get(2)))) {
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

        List<String> lines = ListUtils.toList(bets, "\\s+");

        String format = null;
        if (index() == 0) {
            format = "%s--";
        } else if (index() == 1) {
            format = "-%s-";
        } else {
            format = "--%s";
        }

        /**jd-gui
         Iterator localIterator2;
         for (Iterator localIterator1 = lines.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         String lineStr = (String)localIterator1.next();
         List<Integer> line = LotteryUtils.toIntListByLength(lineStr, 1);
         int n1 = ((Integer)line.get(0)).intValue();
         int n2 = ((Integer)line.get(1)).intValue();
         int n3 = ((Integer)line.get(2)).intValue();

         Set<String> keys1 = PermutateUtils.getPerms(new Integer[] { Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3) });

         localIterator2 = keys1.iterator(); continue;String key = (String)localIterator2.next();
         String k = String.format(format, new Object[] { key });
         addMap(result, k, getBonus());
         }*/

        for (Iterator iterator = lines.iterator(); iterator.hasNext(); ) {
            String lineStr = (String) iterator.next();
            List<Integer> line = LotteryUtils.toIntListByLength(lineStr, 1);
            int n1 = ((Integer) line.get(0)).intValue();
            int n2 = ((Integer) line.get(1)).intValue();
            int n3 = ((Integer) line.get(2)).intValue();
            Set<String> keys1 = PermutateUtils.getPerms(new Integer[]{
                    Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3)
            });
            String k;
            for (Iterator iterator1 = keys1.iterator(); iterator1.hasNext(); addMap(result, k, getBonus())) {
                String key = (String) iterator1.next();
                k = String.format(format, new Object[]{
                        key
                });
            }

        }

        return result;
    }
}
