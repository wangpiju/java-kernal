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
import java.util.Map.Entry;
import java.util.Set;

public class SscStar3FrontGroup36SinglePlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("666.66").divide(new BigDecimal(2));
    private BigDecimal bonus2 = new BigDecimal("333.33").divide(new BigDecimal(2));
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private String qunName = "前三";
    private String groupName = "";
    private String title = "混合组选";

    protected void init() {
        setRemark("手动输入号码，3个数字为一注组六,两个数字为一注组三，所选号码与开奖号码的前三位相同，顺序不限，即为中奖");
        setExample("投注：(01),(123) 开奖：(1)001**（不限顺序）即中组三，(2)123**（不限顺序）即中组六");
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
        return null;
    }

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            if (n.size() != 3) {
                return Integer.valueOf(0);
            }
            if (n.retainAll(this.nums)) {
                return Integer.valueOf(0);
            }
            Set<Integer> opens = new HashSet();
            opens.addAll(n);
            if (opens.size() == 1) {
                return Integer.valueOf(0);
            }
        }
        return Integer.valueOf(lines.size());
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        int o1 = ((Integer) openNums.get(index())).intValue();
        int o2 = ((Integer) openNums.get(index() + 1)).intValue();
        int o3 = ((Integer) openNums.get(index() + 2)).intValue();


        Map<Integer, Integer> opens = LotteryUtils.getNumInfo(new int[]{o1, o2, o3});
        if (opens.size() == 1) {
            return win;
        }
        boolean group3 = opens.size() == 2;

        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            Map<Integer, Integer> buys = LotteryUtils.getNumInfo(n);
            boolean isWin = true;
            for (Map.Entry<Integer, Integer> v : opens.entrySet()) {
                if (!((Integer) v.getValue()).equals(buys.get(v.getKey()))) {
                    isWin = false;
                    break;
                }
            }
            if (isWin) {
                if (group3) {
                    win = win.add(this.bonus);
                } else {
                    win = win.add(this.bonus2);
                }
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

         BigDecimal b = null;
         if ((n1 == n2) || (n1 == n3) || (n2 == n3)) {
         b = getBonus();
         } else {
         b = this.bonus2;
         }
         Set<String> list = PermutateUtils.getPerms(new Integer[] { Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3) });

         localIterator2 = list.iterator(); continue;String key = (String)localIterator2.next();
         String k = String.format(format, new Object[] { key });
         addMap(result, k, b);
         }*/

        for (Iterator iterator = lines.iterator(); iterator.hasNext(); ) {
            String lineStr = (String) iterator.next();
            List<Integer> line = LotteryUtils.toIntListByLength(lineStr, 1);
            int n1 = ((Integer) line.get(0)).intValue();
            int n2 = ((Integer) line.get(1)).intValue();
            int n3 = ((Integer) line.get(2)).intValue();
            BigDecimal b = null;
            if (n1 == n2 || n1 == n3 || n2 == n3)
                b = getBonus();
            else
                b = bonus2;
            Set<String> list = PermutateUtils.getPerms(new Integer[]{
                    Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3)
            });
            String k;
            for (Iterator iterator1 = list.iterator(); iterator1.hasNext(); addMap(result, k, b)) {
                String key = (String) iterator1.next();
                k = String.format(format, new Object[]{
                        key
                });
            }

        }

        return result;
    }
}
