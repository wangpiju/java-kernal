package com.hs3.lotts.ssc.star4.front.group;

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

public class SscStar4FrontGroup12Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("1666.66").divide(new BigDecimal(2));
    private String qunName = "前四";
    private String groupName = "";
    private String title = "组选12";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {
            new NumberView("重号", this.nums),
            new NumberView("单号", this.nums)};

    protected void init() {
        setRemark("至少选择1个二重号码和2个单号号码，竞猜开奖号码的前四位，号码一致顺序不限即中奖");
        setExample("投注：2588*（8为二重号，2·5为单号）开奖：2588*（不限顺序） 即中奖");
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
        List<String> lines = ListUtils.toList(bets);
        if (lines.size() != 2) {
            return Integer.valueOf(0);
        }
        List<Integer> n1 = LotteryUtils.toIntListByLength((String) lines.get(0), 1);
        List<Integer> n2 = LotteryUtils.toIntListByLength((String) lines.get(1), 1);
        if (n1.size() < 1) {
            return Integer.valueOf(0);
        }
        if (n2.size() < 2) {
            return Integer.valueOf(0);
        }
        if (ListUtils.hasSame(n1)) {
            return Integer.valueOf(0);
        }
        if (ListUtils.hasSame(n2)) {
            return Integer.valueOf(0);
        }
        if (n1.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        if (n2.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        int count = 0;
        for (Integer n : n1) {
            int a = n2.size();
            if (n2.contains(n)) {
                a--;
            }
            count += LotteryUtils.getCombin(a, 2);
        }
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");

        Set<Integer> opens = new HashSet();
        opens.add((Integer) openNums.get(index()));
        opens.add((Integer) openNums.get(index() + 1));
        opens.add((Integer) openNums.get(index() + 2));
        opens.add((Integer) openNums.get(index() + 3));

        Integer o1 = (Integer) openNums.get(index());
        Integer o2 = (Integer) openNums.get(index() + 1);
        Integer o3 = (Integer) openNums.get(index() + 2);
        Integer o4 = (Integer) openNums.get(index() + 3);
        if (opens.size() != 3) {
            return win;
        }
        Integer doubleNum = null;
        Integer singelNum1 = null;
        Integer singelNum2 = null;
        if (o1 == o2) {
            doubleNum = o1;
            singelNum1 = o3;
            singelNum2 = o4;
        } else if (o1 == o3) {
            doubleNum = o1;
            singelNum1 = o2;
            singelNum2 = o4;
        } else if (o1 == o4) {
            doubleNum = o1;
            singelNum1 = o2;
            singelNum2 = o3;
        } else if (o2 == o3) {
            doubleNum = o2;
            singelNum1 = o1;
            singelNum2 = o4;
        } else if (o2 == o4) {
            doubleNum = o2;
            singelNum1 = o1;
            singelNum2 = o3;
        } else if (o3 == o4) {
            doubleNum = o3;
            singelNum1 = o1;
            singelNum2 = o2;
        }
        if ((doubleNum == null) || (singelNum1 == null) || (singelNum2 == null)) {
            return win;
        }
        List<String> lines = ListUtils.toList(bets);

        List<Integer> n1 = LotteryUtils.toIntListByLength((String) lines.get(0), 1);
        List<Integer> n2 = LotteryUtils.toIntListByLength((String) lines.get(1), 1);
        if ((n1.contains(doubleNum)) && (n2.contains(singelNum1)) && (n2.contains(singelNum2))) {
            win = win.add(getBonus());
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
        List<String> lines = ListUtils.toList(bets);

        List<Integer> n1List = LotteryUtils.toIntListByLength((String) lines.get(0), 1);
        List<Integer> line = LotteryUtils.toIntListByLength((String) lines.get(1), 1);

        String format = null;
        if (index() == 0) {
            format = "%s-";
        } else {
            format = "-%s";
        }


        /**jd-gui
         int i;
         for (Iterator localIterator1 = n1List.iterator(); localIterator1.hasNext(); i < line.size())
         {
         int n1 = ((Integer)localIterator1.next()).intValue();
         i = 0; continue;
         int n2 = ((Integer)line.get(i)).intValue();
         if (n2 != n1) {
         for (int j = i + 1; j < line.size(); j++)
         {
         int n3 = ((Integer)line.get(j)).intValue();
         if (n3 != n1)
         {
         Set<String> keys = PermutateUtils.getPerms(new Integer[] { Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3) });
         for (String key : keys)
         {
         String k = String.format(format, new Object[] { key });
         result.put(k, getBonus());
         }
         }
         }
         }
         i++;
         }*/

        for (Iterator iterator = n1List.iterator(); iterator.hasNext(); ) {
            int n1 = ((Integer) iterator.next()).intValue();
            for (int i = 0; i < line.size(); i++) {
                int n2 = ((Integer) line.get(i)).intValue();
                if (n2 != n1) {
                    for (int j = i + 1; j < line.size(); j++) {
                        int n3 = ((Integer) line.get(j)).intValue();
                        if (n3 != n1) {
                            Set<String> keys = PermutateUtils.getPerms(new Integer[]{
                                    Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3)
                            });
                            String k;
                            for (Iterator iterator1 = keys.iterator(); iterator1.hasNext(); result.put(k, getBonus())) {
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
