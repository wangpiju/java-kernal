package com.hs3.lotts.ssc.star2.front.group;

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

public class SscStar2FrontGroupAndPlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("100").divide(new BigDecimal(2));
    private String qunName = "前二";
    private String groupName = "组选";
    private String title = "和值";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11),
                    Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17)}));
    private Integer[] betCount = {Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(4),
            Integer.valueOf(4), Integer.valueOf(3), Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(1)};
    private NumberView[] view = {new NumberView("", this.nums, false)};

    protected void init() {
        setRemark("所选数值等于开奖号码的前二位数字相加之和（不含对子）");
        setExample("投注：和值1 开奖：10***（不限顺序） 即中奖");
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
        int o1 = ((Integer) openNums.get(index())).intValue();
        int o2 = ((Integer) openNums.get(index() + 1)).intValue();
        if (o1 == o2) {
            return win;
        }
        List<Integer> line = ListUtils.toIntList(bets);
        if (line.contains(Integer.valueOf(o1 + o2))) {
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
        List<Integer> line = ListUtils.toIntList(bets);

        String format = null;
        if (index() == 0) {
            format = "%d%d---";
        } else {
            format = "---%d%d";
        }

        /**jd-gui
         int i;
         int n1;
         for (Iterator localIterator = line.iterator(); localIterator.hasNext(); n1 <= i)
         {
         i = ((Integer)localIterator.next()).intValue();
         n1 = 0; continue;
         int n2 = i - n1;
         if (n1 != n2)
         {
         String k = String.format(format, new Object[] { Integer.valueOf(n1), Integer.valueOf(n2) });
         result.put(k, getBonus());
         }
         n1++;
         }*/

        for (Iterator iterator = line.iterator(); iterator.hasNext(); ) {
            int i = ((Integer) iterator.next()).intValue();
            for (int n1 = 0; n1 <= i; n1++) {
                int n2 = i - n1;
                if (n1 != n2) {
                    String k = String.format(format, new Object[]{
                            Integer.valueOf(n1), Integer.valueOf(n2)
                    });
                    result.put(k, getBonus());
                }
            }

        }

        return result;
    }
}
