package com.hs3.lotts.ssc.star3.any;

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

public class SscStar3AnySinglePlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("2000").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "任三";
    private String title = "直选单式";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private List<String> titleList = new ArrayList(Arrays.asList(new String[]{"万", "千", "百", "十", "个"}));

    public List<String> getAnyList() {
        return this.titleList;
    }

    public Integer getAnySelect() {
        return Integer.valueOf(3);
    }

    protected void init() {
        setRemark("每位至少选择一个号码，竞猜开奖号码的对应的三位，号码和位置都对应即中奖");
        setExample("投注：456** 开奖：456** 即中奖");
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

    public String getQunName() {
        return this.qunName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Integer getCount(String bets) {
        /**jd-gui
         String[] betContent = bets.split("\\|");
         if (betContent.length != 2) {
         return Integer.valueOf(0);
         }
         List<Integer> indexs = LotteryUtils.getSscIndexs(betContent[0]);
         if (indexs.size() < 3) {
         return Integer.valueOf(0);
         }
         for (Iterator localIterator1 = indexs.iterator(); localIterator1.hasNext();)
         {
         int i = ((Integer)localIterator1.next()).intValue();
         if ((i < 0) || (i > 4)) {
         return Integer.valueOf(0);
         }
         }
         int nowIndex = -1;
         for (Iterator localIterator2 = indexs.iterator(); localIterator2.hasNext();)
         {
         int n = ((Integer)localIterator2.next()).intValue();
         if (n <= nowIndex) {
         return Integer.valueOf(0);
         }
         nowIndex = n;
         }
         Object lines = ListUtils.toList(betContent[1], "\\s+");
         for (String line : (List)lines)
         {
         List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
         if (n.size() != 3) {
         return Integer.valueOf(0);
         }
         if (n.retainAll(this.nums)) {
         return Integer.valueOf(0);
         }
         }
         return Integer.valueOf(((List)lines).size() * LotteryUtils.getCombin(indexs.size(), 3));
         */

        String betContent[] = bets.split("\\|");
        if (betContent.length != 2)
            return Integer.valueOf(0);
        List<Integer> indexs = LotteryUtils.getSscIndexs(betContent[0]);
        if (indexs.size() < 3)
            return Integer.valueOf(0);
        for (Iterator iterator = indexs.iterator(); iterator.hasNext(); ) {
            int i = ((Integer) iterator.next()).intValue();
            if (i < 0 || i > 4)
                return Integer.valueOf(0);
        }

        int nowIndex = -1;
        for (Iterator iterator1 = indexs.iterator(); iterator1.hasNext(); ) {
            int n = ((Integer) iterator1.next()).intValue();
            if (n <= nowIndex)
                return Integer.valueOf(0);
            nowIndex = n;
        }

        List lines = ListUtils.toList(betContent[1], "\\s+");
        for (Iterator iterator2 = lines.iterator(); iterator2.hasNext(); ) {
            String line = (String) iterator2.next();
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            if (n.size() != 3)
                return Integer.valueOf(0);
            if (n.retainAll(nums))
                return Integer.valueOf(0);
        }

        return Integer.valueOf(lines.size() * LotteryUtils.getCombin(indexs.size(), 3));

    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        String[] betContent = bets.split("\\|");

        List<Integer> indexs = LotteryUtils.getSscIndexs(betContent[0]);

        List<String> lines = ListUtils.toList(betContent[1], "\\s+");
        List<List<Integer>> allBuy = new ArrayList();
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            allBuy.add(n);
        }
        for (int i = 0; i < indexs.size(); i++) {
            int o1 = ((Integer) openNums.get(((Integer) indexs.get(i)).intValue())).intValue();
            for (int j = i + 1; j < indexs.size(); j++) {
                int o2 = ((Integer) openNums.get(((Integer) indexs.get(j)).intValue())).intValue();
                for (int k = j + 1; k < indexs.size(); k++) {
                    int o3 = ((Integer) openNums.get(((Integer) indexs.get(k)).intValue())).intValue();
                    for (List<Integer> line : allBuy) {
                        if ((((Integer) line.get(0)).intValue() == o1) && (((Integer) line.get(1)).intValue() == o2) && (((Integer) line.get(2)).intValue() == o3)) {
                            win = win.add(getBonus());
                        }
                    }
                }
            }
        }
        return win;
    }

    private String getKey(List<Integer> ns) {
        String format = "";
        for (int i = 0; i < 5; i++) {
            if (ns.contains(Integer.valueOf(i))) {
                format = format + "%s";
            } else {
                format = format + "-";
            }
        }
        return format;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();

        String[] betContent = bets.split("\\|");
        List<Integer> indexs = LotteryUtils.getSscIndexs(betContent[0]);
        List<String> lines = ListUtils.toList(betContent[1], "\\s+");


        Set<String> ins = PermutateUtils.getCombinSelect(indexs, 3);

        /**jd-gui
         Iterator localIterator2;
         for (Iterator localIterator1 = lines.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         String line = (String)localIterator1.next();

         Object[] n = LotteryUtils.toListByLength(line, Integer.valueOf(1)).toArray(new Object[0]);


         localIterator2 = ins.iterator(); continue;String i = (String)localIterator2.next();
         String k = getKey(ListUtils.toIntList(i));
         String nn = String.format(k, n);

         addMap(result, nn, getBonus());
         }*/

        for (Iterator iterator = lines.iterator(); iterator.hasNext(); ) {
            String line = (String) iterator.next();
            Object n[] = LotteryUtils.toListByLength(line, Integer.valueOf(1)).toArray(new Object[0]);
            String nn;
            for (Iterator iterator1 = ins.iterator(); iterator1.hasNext(); addMap(result, nn, getBonus())) {
                String i = (String) iterator1.next();
                String k = getKey(ListUtils.toIntList(i));
                nn = String.format(k, n);
            }

        }

        return result;
    }
}
