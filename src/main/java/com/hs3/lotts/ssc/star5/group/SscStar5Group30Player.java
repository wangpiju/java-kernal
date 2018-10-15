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

public class SscStar5Group30Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("6666.66").divide(new BigDecimal(2));
    private String qunName = "五星";
    private String groupName = "";
    private String title = "组选30";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {
            new NumberView("重号", this.nums),
            new NumberView("单号", this.nums)};

    protected void init() {
        setRemark("至少选择2个二重号码和1个单号号码组成一注，竞猜开奖号码的全部五位，号码一致顺序不限即中奖");
        setExample("投注：00588（0·8为二重号，5为单号） 开奖：00588（不限顺序）");
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
        for (int i = 0; i < 2; i++) {
            List<Integer> n = LotteryUtils.toIntListByLength((String) lines.get(i), 1);
            if (ListUtils.hasSame(n)) {
                return Integer.valueOf(0);
            }
            if (n.retainAll(this.nums)) {
                return Integer.valueOf(0);
            }
        }
        List<Integer> n1 = LotteryUtils.toIntListByLength((String) lines.get(0), 1);
        List<Integer> n2 = LotteryUtils.toIntListByLength((String) lines.get(1), 1);

        int count = 0;
        for (Integer n : n2) {
            int nowCount = n1.size();
            if (n1.contains(n)) {
                nowCount--;
            }
            count += LotteryUtils.getCombin(nowCount, 2);
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
        if (opens.size() != 3) {
            return win;
        }
        Integer doubleNum1 = null;
        Integer doubleNum2 = null;
        Integer singleNum = null;
        for (Integer k : opens.keySet()) {
            int n = ((Integer) opens.get(k)).intValue();
            if (n == 1) {
                singleNum = k;
            } else if (n == 2) {
                if (doubleNum1 == null) {
                    doubleNum1 = k;
                } else {
                    doubleNum2 = k;
                }
            }
        }
        if ((doubleNum1 == null) || (doubleNum2 == null) || (singleNum == null)) {
            return win;
        }
        List<String> lines = ListUtils.toList(bets);

        Object n1 = LotteryUtils.toIntListByLength((String) lines.get(0), 1);
        List<Integer> n2 = LotteryUtils.toIntListByLength((String) lines.get(1), 1);
        if ((((List) n1).contains(doubleNum1)) && (((List) n1).contains(doubleNum2)) && (n2.contains(singleNum))) {
            win = win.add(getBonus());
        }
        return win;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();
        List<String> lines = ListUtils.toList(bets);

        List<Integer> n1List = LotteryUtils.toIntListByLength((String) lines.get(0), 1);
        List<Integer> line = LotteryUtils.toIntListByLength((String) lines.get(1), 1);

        /**jd-gui
         for (int i = 0; i < n1List.size(); i++)
         {
         int n1 = ((Integer)n1List.get(i)).intValue();
         for (int j = i + 1; j < n1List.size(); j++)
         {
         int n2 = ((Integer)n1List.get(j)).intValue();
         Iterator localIterator2;
         label234:
         for (Iterator localIterator1 = line.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         int n3 = ((Integer)localIterator1.next()).intValue();
         if ((n3 == n1) || (n3 == n2)) {
         break label234;
         }
         Set<String> keys = PermutateUtils.getPerms(new Integer[] { Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n2), Integer.valueOf(n3) });
         localIterator2 = keys.iterator(); continue;String k = (String)localIterator2.next();
         result.put(k, getBonus());
         }
         }
         }*/

        for (int i = 0; i < n1List.size(); i++) {
            int n1 = ((Integer) n1List.get(i)).intValue();
            for (int j = i + 1; j < n1List.size(); j++) {
                int n2 = ((Integer) n1List.get(j)).intValue();
                for (Iterator iterator = line.iterator(); iterator.hasNext(); ) {
                    int n3 = ((Integer) iterator.next()).intValue();
                    if (n3 != n1 && n3 != n2) {
                        Set<String> keys = PermutateUtils.getPerms(new Integer[]{
                                Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n2), Integer.valueOf(n3)
                        });
                        String k;
                        for (Iterator iterator1 = keys.iterator(); iterator1.hasNext(); result.put(k, getBonus()))
                            k = (String) iterator1.next();

                    }
                }

            }

        }


        return result;
    }
}
