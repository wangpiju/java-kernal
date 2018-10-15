package com.hs3.lotts.ssc.star3.front.group;

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

public class SscStar3FrontGroupAndPlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("666.66").divide(new BigDecimal(2));
    private BigDecimal bonus2 = new BigDecimal("333.33").divide(new BigDecimal(2));
    private String qunName = "前三";
    private String groupName = "组选";
    private String title = "和值";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26)}));
    private Integer[] betCount = {Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(8), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(15), Integer.valueOf(14), Integer.valueOf(14), Integer.valueOf(13), Integer.valueOf(11), Integer.valueOf(10), Integer.valueOf(8), Integer.valueOf(6), Integer.valueOf(5), Integer.valueOf(4), Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(1)};
    private NumberView[] view = {new NumberView("", this.nums, false)};

    protected void init() {
        setRemark("至少选择一个和值，竞猜开奖号码前三位数字之和(不含豹子号)");
        setExample("投注：和值6 开奖：(1) 015**（不限顺序）中组六 (2)033**（不限顺序），中组三");
    }

    protected int index() {
        return 0;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getBonusStr() {
        return this.bonus2 + " - " + this.bonus;
    }

    public String getTitle() {
        return this.title;
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
            count += this.betCount[(n.intValue() - 1)].intValue();
        }
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        List<Integer> line = ListUtils.toIntList(bets);
        Set<Integer> opens = new HashSet();

        List<Integer> n = new ArrayList();
        n.add((Integer) openNums.get(index()));
        n.add((Integer) openNums.get(index() + 1));
        n.add((Integer) openNums.get(index() + 2));

        opens.addAll(n);
        if (opens.size() == 1) {
            return win;
        }
        boolean group3 = opens.size() == 2;
        Integer sum = Integer.valueOf(0);
        for (Integer o : n) {
            sum = Integer.valueOf(sum.intValue() + o.intValue());
        }
        if (line.contains(sum)) {
            if (group3) {
                win = win.add(this.bonus);
            } else {
                win = win.add(this.bonus2);
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
            format = "%s--";
        } else if (index() == 1) {
            format = "-%s-";
        } else {
            format = "--%s";
        }

        /**jd-gui
         int n1;
         for (Iterator localIterator1 = line.iterator(); localIterator1.hasNext(); n1 <= 9)
         {
         Integer n = (Integer)localIterator1.next();
         n1 = 0; continue;
         for (int n2 = 0; n2 <= 9; n2++)
         {
         int n3 = n.intValue() - (n1 + n2);
         if ((n3 >= 0) && (n3 <= 9) && (
         (n1 != n2) || (n1 != n3)))
         {
         BigDecimal b = null;
         if ((n1 == n2) || (n1 == n3) || (n2 == n3)) {
         b = this.bonus;
         } else {
         b = this.bonus2;
         }
         Set<String> keys1 = PermutateUtils.getPerms(new Integer[] { Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3) });
         for (String key : keys1)
         {
         String k = String.format(format, new Object[] { key });
         result.put(k, b);
         }
         }
         }
         n1++;
         }*/

        for (Iterator iterator = line.iterator(); iterator.hasNext(); ) {
            Integer n = (Integer) iterator.next();
            for (int n1 = 0; n1 <= 9; n1++) {
                for (int n2 = 0; n2 <= 9; n2++) {
                    int n3 = n.intValue() - (n1 + n2);
                    if (n3 >= 0 && n3 <= 9 && (n1 != n2 || n1 != n3)) {
                        BigDecimal b = null;
                        if (n1 == n2 || n1 == n3 || n2 == n3)
                            b = bonus;
                        else
                            b = bonus2;
                        Set<String> keys1 = PermutateUtils.getPerms(new Integer[]{
                                Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3)
                        });
                        String k;
                        for (Iterator iterator1 = keys1.iterator(); iterator1.hasNext(); result.put(k, b)) {
                            String key = (String) iterator1.next();
                            k = String.format(format, new Object[]{
                                    key
                            });
                        }

                    }
                }

            }

        }

        return result;
    }
}
