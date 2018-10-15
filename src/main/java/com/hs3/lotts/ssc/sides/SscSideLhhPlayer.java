package com.hs3.lotts.ssc.sides;

import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SscSideLhhPlayer
        extends PlayerBase {
    private List<String> titles = new ArrayList(
            Arrays.asList(new String[]{"[万千]", "[万百]", "[万十]", "[万个]", "[千百]", "[千十]", "[千个]", "[百十]", "[百个]", "[十个]"}));
    private BigDecimal bonus2 = new BigDecimal("4.44").divide(new BigDecimal(2));
    private BigDecimal bonus = new BigDecimal("20").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "龙虎和";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"龙", "虎", "和"}));
    private NumberView[] view = {
            new NumberView("万千", this.nums, false, 0, true),
            new NumberView("万百", this.nums, false, 0, true),
            new NumberView("万十", this.nums, false, 0, true),
            new NumberView("万个", this.nums, false, 0, true),
            new NumberView("千百", this.nums, false, 0, true),
            new NumberView("千十", this.nums, false, 0, true),
            new NumberView("千个", this.nums, false, 0, true),
            new NumberView("百十", this.nums, false, 0, true),
            new NumberView("百个", this.nums, false, 0, true),
            new NumberView("十个", this.nums, false, 0, true)};

    protected void init() {
        setRemark("从对应两个位上选择一个形态组成一注，前者大于后者为“龙”，反之为“虎”，相等为“和”");
        setExample("万千位置投注：“龙” 开奖：21*** 即中奖");
    }

    public String getBonusStr() {
        return this.bonus2 + " - " + this.bonus;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getTitle() {
        return this.title;
    }

    public String getQunName() {
        return this.qunName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public NumberView[] getNumView() {
        return this.view;
    }

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets);
        if (lines.size() != 10) {
            return Integer.valueOf(0);
        }
        int count = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            if (!line.equals("-")) {
                String t = (String) this.titles.get(i);
                if (!line.startsWith(t)) {
                    return Integer.valueOf(0);
                }
                line = line.substring(t.length());

                List<String> n = LotteryUtils.toListByLength(line, Integer.valueOf(1));
                if (ListUtils.hasSame(n)) {
                    return Integer.valueOf(0);
                }
                if (n.retainAll(this.nums)) {
                    return Integer.valueOf(0);
                }
                count += n.size();
            }
        }
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<String> lines = ListUtils.toList(bets);
        BigDecimal win = new BigDecimal("0");

        int index1 = 0;
        int index2 = 0;
        for (int i = 0; i < lines.size(); i++) {
            index2++;
            if (index2 > 4) {
                index1++;
                index2 = index1 + 1;
            }
            String line = (String) lines.get(i);
            if (!line.equals("-")) {
                String t = (String) this.titles.get(i);
                if (!line.startsWith(t)) {
                    return BigDecimal.ZERO;
                }
                line = line.substring(t.length());

                int o1 = ((Integer) openNums.get(index1)).intValue();
                int o2 = ((Integer) openNums.get(index2)).intValue();
                int o = o1 - o2;
                if ((o > 0) && (line.contains("龙"))) {
                    win = win.add(this.bonus2);
                } else if ((o < 0) && (line.contains("虎"))) {
                    win = win.add(this.bonus2);
                } else if ((o == 0) && (line.contains("和"))) {
                    win = win.add(this.bonus);
                }
            }
        }
        return win;
    }

    private String getKey(int index1, int index2) {
        String k = "";
        for (int i = 0; i < 5; i++) {
            if ((i == index1) || (i == index2)) {
                k = k + "%d";
            } else {
                k = k + "-";
            }
        }
        return k;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {

        /**jd-gui
         Map<String, BigDecimal> result = new HashMap();

         List<String> lines = ListUtils.toList(bets);

         int index1 = 0;
         int index2 = 0;
         for (String line : lines)
         {
         index2++;
         if (index2 > 4)
         {
         index1++;
         index2 = index1 + 1;
         }
         if (!line.equals("-"))
         {
         String z = line.substring(4);
         List<String> ns = LotteryUtils.toListByLength(z, Integer.valueOf(1));
         int x;
         for (Iterator localIterator2 = ns.iterator(); localIterator2.hasNext(); x < 10)
         {
         String n = (String)localIterator2.next();

         x = 0; continue;
         for (int y = 0; y < 10; y++) {
         if ((x > y) && (n.equals("龙")))
         {
         String b = String.format(
         getKey(index1, index2), new Object[] { Integer.valueOf(x), Integer.valueOf(y) });
         result.put(b, this.bonus2);
         }
         else if ((x < y) && (n.equals("虎")))
         {
         String b = String.format(
         getKey(index1, index2), new Object[] { Integer.valueOf(x), Integer.valueOf(y) });
         result.put(b, this.bonus2);
         }
         else if ((x == y) && (n.equals("和")))
         {
         String b = String.format(
         getKey(index1, index2), new Object[] { Integer.valueOf(x), Integer.valueOf(y) });
         result.put(b, getBonus());
         }
         }
         x++;
         }
         }
         }
         return result;
         */
        Map<String, BigDecimal> result = new HashMap();
        List<String> lines = ListUtils.toList(bets);
        int index1 = 0;
        int index2 = 0;
        for (Iterator iterator = lines.iterator(); iterator.hasNext(); ) {
            String line = (String) iterator.next();
            if (++index2 > 4)
                index2 = ++index1 + 1;
            if (!line.equals("-")) {
                String z = line.substring(4);
                List<String> ns = LotteryUtils.toListByLength(z, Integer.valueOf(1));
                for (Iterator iterator1 = ns.iterator(); iterator1.hasNext(); ) {
                    String n = (String) iterator1.next();
                    for (int x = 0; x < 10; x++) {
                        for (int y = 0; y < 10; y++)
                            if (x > y && n.equals("龙")) {
                                String b = String.format(getKey(index1, index2), new Object[]{
                                        Integer.valueOf(x), Integer.valueOf(y)
                                });
                                result.put(b, bonus2);
                            } else if (x < y && n.equals("虎")) {
                                String b = String.format(getKey(index1, index2), new Object[]{
                                        Integer.valueOf(x), Integer.valueOf(y)
                                });
                                result.put(b, bonus2);
                            } else if (x == y && n.equals("和")) {
                                String b = String.format(getKey(index1, index2), new Object[]{
                                        Integer.valueOf(x), Integer.valueOf(y)
                                });
                                result.put(b, getBonus());
                            }

                    }

                }

            }
        }

        return result;

    }
}
