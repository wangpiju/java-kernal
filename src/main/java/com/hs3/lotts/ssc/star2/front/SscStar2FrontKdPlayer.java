package com.hs3.lotts.ssc.star2.front;

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

public class SscStar2FrontKdPlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("200").divide(new BigDecimal(2));
    private String qunName = "前二";
    private String groupName = "直选";
    private String title = "跨度";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private Integer[] betCount = {Integer.valueOf(10), Integer.valueOf(18), Integer.valueOf(16), Integer.valueOf(14), Integer.valueOf(12), Integer.valueOf(10), Integer.valueOf(8), Integer.valueOf(6), Integer.valueOf(4), Integer.valueOf(2)};
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("所选数值等于开奖号码的前二位最大与最小数字相减之差");
        setExample("投注：跨度8 开奖：08***, 19***, 80***, 91*** 即中奖");
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
            count += this.betCount[n.intValue()].intValue();
        }
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        List<Integer> line = ListUtils.toIntList(bets);
        int n = Math.abs(((Integer) openNums.get(index())).intValue() - ((Integer) openNums.get(index() + 1)).intValue());
        if (line.contains(Integer.valueOf(n))) {
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
        String format = null;
        if (index() == 0) {
            format = "%d%d---";
        } else {
            format = "---%d%d";
        }
        List<Integer> line = ListUtils.toIntList(bets);

        /**jd-gui
         int n1;
         for (Iterator localIterator = line.iterator(); localIterator.hasNext(); n1 <= 9)
         {
         Integer n = (Integer)localIterator.next();
         n1 = n.intValue(); continue;
         Integer n2 = Integer.valueOf(n1 - n.intValue());
         String b1 = String.format(format, new Object[] { Integer.valueOf(n1), n2 });
         String b2 = String.format(format, new Object[] { n2, Integer.valueOf(n1) });
         result.put(b1, getBonus());
         result.put(b2, getBonus());n1++;
         }*/

        for (Iterator iterator = line.iterator(); iterator.hasNext(); ) {
            Integer n = (Integer) iterator.next();
            for (int n1 = n.intValue(); n1 <= 9; n1++) {
                Integer n2 = Integer.valueOf(n1 - n.intValue());
                String b1 = String.format(format, new Object[]{
                        Integer.valueOf(n1), n2
                });
                String b2 = String.format(format, new Object[]{
                        n2, Integer.valueOf(n1)
                });
                result.put(b1, getBonus());
                result.put(b2, getBonus());
            }

        }

        return result;
    }
}
