package com.hs3.lotts.ssc.star4.front;

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

public class SscStar4FrontPlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("20000").divide(new BigDecimal(2));
    private String qunName = "前四";
    private String groupName = "直选";
    private String title = "复式";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {
            new NumberView("万位", this.nums),
            new NumberView("千位", this.nums),
            new NumberView("百位", this.nums),
            new NumberView("十位", this.nums)};

    protected void init() {
        setRemark("每位至少选择一个号码，竞猜开奖号码的前四位，号码和位置都对应即中奖");
        setExample("投注：3456* 开奖：3456* 即中奖");
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
        if (lines.size() != 5) {
            return Integer.valueOf(0);
        }
        int count = 1;
        for (int i = 0; i < 4; i++) {
            List<Integer> n = LotteryUtils.toIntListByLength((String) lines.get(i + index()), 1);
            if (ListUtils.hasSame(n)) {
                return Integer.valueOf(0);
            }
            if (n.retainAll(this.nums)) {
                return Integer.valueOf(0);
            }
            count *= n.size();
        }
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        List<String> lines = ListUtils.toList(bets);
        for (int i = 0; i < 4; i++) {
            List<Integer> n = LotteryUtils.toIntListByLength((String) lines.get(i + index()), 1);
            int o = ((Integer) openNums.get(i + index())).intValue();
            if (!n.contains(Integer.valueOf(o))) {
                return win;
            }
        }
        win = win.add(getBonus());
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
        String format = null;
        if (index() == 0) {
            format = "%d%d%d%d-";
        } else {
            format = "-%d%d%d%d";
        }
        List<String> lines = ListUtils.toList(bets);
        List<Integer> i = LotteryUtils.toIntListByLength((String) lines.get(index()), 1);
        List<Integer> j = LotteryUtils.toIntListByLength((String) lines.get(index() + 1), 1);
        List<Integer> k = LotteryUtils.toIntListByLength((String) lines.get(index() + 2), 1);
        List<Integer> h = LotteryUtils.toIntListByLength((String) lines.get(index() + 3), 1);

        /**jd-gui
         Iterator localIterator2;
         for (Iterator localIterator1 = i.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         int n1 = ((Integer)localIterator1.next()).intValue();
         localIterator2 = j.iterator(); continue;int n2 = ((Integer)localIterator2.next()).intValue();
         Iterator localIterator4;
         for (Iterator localIterator3 = k.iterator(); localIterator3.hasNext(); localIterator4.hasNext())
         {
         int n3 = ((Integer)localIterator3.next()).intValue();
         localIterator4 = h.iterator(); continue;int n4 = ((Integer)localIterator4.next()).intValue();
         String b = String.format(format, new Object[] { Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3), Integer.valueOf(n4) });
         result.put(b, getBonus());
         }
         }*/

        for (Iterator iterator = i.iterator(); iterator.hasNext(); ) {
            int n1 = ((Integer) iterator.next()).intValue();
            for (Iterator iterator1 = j.iterator(); iterator1.hasNext(); ) {
                int n2 = ((Integer) iterator1.next()).intValue();
                for (Iterator iterator2 = k.iterator(); iterator2.hasNext(); ) {
                    int n3 = ((Integer) iterator2.next()).intValue();
                    String b;
                    for (Iterator iterator3 = h.iterator(); iterator3.hasNext(); result.put(b, getBonus())) {
                        int n4 = ((Integer) iterator3.next()).intValue();
                        b = String.format(format, new Object[]{
                                Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3), Integer.valueOf(n4)
                        });
                    }

                }

            }

        }

        return result;
    }
}
