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

public class SscStar3AnyPlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("2000").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "任三";
    private String title = "直选复式";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {
            new NumberView("万位", this.nums),
            new NumberView("千位", this.nums),
            new NumberView("百位", this.nums),
            new NumberView("十位", this.nums),
            new NumberView("个位", this.nums)};

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
        return this.view;
    }

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets);
        if (lines.size() != 5) {
            return Integer.valueOf(0);
        }
        List<Integer> nList = new ArrayList();
        for (int i = 0; i < 5; i++) {
            List<Integer> n = LotteryUtils.toIntListByLength((String) lines.get(i), 1);
            if (n.size() > 0) {
                if (ListUtils.hasSame(n)) {
                    return Integer.valueOf(0);
                }
                if (n.retainAll(this.nums)) {
                    return Integer.valueOf(0);
                }
                nList.add(Integer.valueOf(n.size()));
            }
        }
        return Integer.valueOf(LotteryUtils.mulAll(nList, 3));
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        List<String> lines = ListUtils.toList(bets);
        int count = 0;
        for (int i = 0; i < 5; i++) {
            List<Integer> n = LotteryUtils.toIntListByLength((String) lines.get(i), 1);
            int o = ((Integer) openNums.get(i)).intValue();
            if (n.contains(Integer.valueOf(o))) {
                count++;
            }
        }
        Integer winNum = Integer.valueOf(LotteryUtils.getCombin(count, 3));
        win = getBonus().multiply(new BigDecimal(winNum.toString()));
        return win;
    }

    public String getQunName() {
        return this.qunName;
    }

    public String getGroupName() {
        return this.groupName;
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

        List<String> lines = ListUtils.toList(bets);
        List<List<Integer>> allBuy = new ArrayList();
        List<Integer> indexs = new ArrayList();
        for (int i = 0; i < 5; i++) {
            List<Integer> n = LotteryUtils.toIntListByLength((String) lines.get(i), 1);
            allBuy.add(n);
            indexs.add(Integer.valueOf(i));
        }
        Set<String> ins = PermutateUtils.getCombinSelect(indexs, 3);

        /**jd-gui
         for (String i : ins)
         {
         List<Integer> nn = ListUtils.toIntList(i);
         List<List<Integer>> aaa = new ArrayList(3);
         int count = 1;
         for (Iterator localIterator2 = nn.iterator(); localIterator2.hasNext();)
         {
         int n = ((Integer)localIterator2.next()).intValue();

         count *= ((List)allBuy.get(n)).size();
         if (count == 0) {
         break;
         }
         aaa.add((List)allBuy.get(n));
         }
         if (count != 0)
         {
         String key = getKey(nn);
         Object num = PermutateUtils.getAllNum(aaa, ",", true);
         for (String n : (Set)num)
         {
         Object[] nnn = ListUtils.toList(n).toArray(new Object[0]);
         String k = String.format(key, nnn);
         result.put(k, getBonus());
         }
         }
         }*/

        for (Iterator iterator = ins.iterator(); iterator.hasNext(); ) {
            String i = (String) iterator.next();
            List<Integer> nn = ListUtils.toIntList(i);
            List<List<Integer>> aaa = new ArrayList(3);
            int count = 1;
            int n;
            for (Iterator iterator1 = nn.iterator(); iterator1.hasNext(); aaa.add((List) allBuy.get(n))) {
                n = ((Integer) iterator1.next()).intValue();
                count *= ((List) allBuy.get(n)).size();
                if (count == 0)
                    break;
            }

            if (count != 0) {
                String key = getKey(nn);
                Set num = PermutateUtils.getAllNum(aaa, ",", true);
                String k;
                for (Iterator iterator2 = num.iterator(); iterator2.hasNext(); result.put(k, getBonus())) {
                    String n2 = (String) iterator2.next();
                    Object nnn[] = ListUtils.toList(n2).toArray(new Object[0]);
                    k = String.format(key, nnn);
                }

            }
        }

        return result;
    }
}
