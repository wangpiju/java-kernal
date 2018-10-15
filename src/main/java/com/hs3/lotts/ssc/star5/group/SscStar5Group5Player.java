package com.hs3.lotts.ssc.star5.group;

import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PermutateUtils;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SscStar5Group5Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("40000").divide(new BigDecimal(2));
    private String qunName = "五星";
    private String groupName = "";
    private String title = "组选5";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {
            new NumberView("重号", this.nums),
            new NumberView("单号", this.nums)};

    protected void init() {
        setRemark("至少选择1个四重号码和1个单号号码组成一注，竞猜开奖号码的全部五位，号码一致顺序不限即中奖");
        setExample("投注：08888（8为四重号，0为单号） 开奖：08888（不限顺序）");
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
        List<String> lines = ListUtils.toList(bets);
        if (lines.size() != 2) {
            return Integer.valueOf(0);
        }
        List<Integer> n1 = LotteryUtils.toIntListByLength((String) lines.get(0), 1);
        if (ListUtils.hasSame(n1)) {
            return Integer.valueOf(0);
        }
        if (n1.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        List<Integer> n2 = LotteryUtils.toIntListByLength((String) lines.get(1), 1);
        if (ListUtils.hasSame(n2)) {
            return Integer.valueOf(0);
        }
        if (n2.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        int count = 0;
        for (Integer n : n1) {
            count += n2.size();
            if (n2.contains(n)) {
                count--;
            }
        }
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");

        Map<Integer, Integer> opens = new HashMap();
        for (int i = 0; i < 5; i++) {
            Integer n = Integer.valueOf(1);
            Integer o = (Integer) openNums.get(i);
            if (opens.containsKey(o)) {
                n = (Integer) opens.get(o);
                n = Integer.valueOf(n.intValue() + 1);
            }
            opens.put(o, n);
        }
        if (opens.size() != 2) {
            return win;
        }
        Integer num1 = null;
        Integer num2 = null;
        for (Integer k : opens.keySet()) {
            int n = ((Integer) opens.get(k)).intValue();
            if (n == 4) {
                num1 = k;
            } else if (n == 1) {
                num2 = k;
            }
        }
        if ((num1 == null) || (num2 == null)) {
            return win;
        }
        List<String> lines = ListUtils.toList(bets);

        Object n1 = LotteryUtils.toIntListByLength((String) lines.get(0), 1);
        List<Integer> n2 = LotteryUtils.toIntListByLength((String) lines.get(1), 1);
        if ((((List) n1).contains(num1)) && (n2.contains(num2))) {
            win = win.add(getBonus());
        }
        return win;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();
        List<String> lines = ListUtils.toList(bets);

        List<Integer> line1 = LotteryUtils.toIntListByLength((String) lines.get(0), 1);
        List<Integer> line2 = LotteryUtils.toIntListByLength((String) lines.get(1), 1);

        /**jd-gui
         Iterator localIterator2;
         for (Iterator localIterator1 = line1.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         int n1 = ((Integer)localIterator1.next()).intValue();
         localIterator2 = line2.iterator(); continue;int n2 = ((Integer)localIterator2.next()).intValue();
         if (n2 != n1)
         {
         Set<String> keys = PermutateUtils.getPerms(new Integer[] { Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n2) });
         for (String k : keys) {
         result.put(k, getBonus());
         }
         }
         }*/

        for (Iterator iterator = line1.iterator(); iterator.hasNext(); ) {
            int n1 = ((Integer) iterator.next()).intValue();
            for (Iterator iterator1 = line2.iterator(); iterator1.hasNext(); ) {
                int n2 = ((Integer) iterator1.next()).intValue();
                if (n2 != n1) {
                    Set<String> keys = PermutateUtils.getPerms(new Integer[]{
                            Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n2)
                    });
                    String k;
                    for (Iterator iterator2 = keys.iterator(); iterator2.hasNext(); result.put(k, getBonus()))
                        k = (String) iterator2.next();

                }
            }

        }

        return result;
    }
}
