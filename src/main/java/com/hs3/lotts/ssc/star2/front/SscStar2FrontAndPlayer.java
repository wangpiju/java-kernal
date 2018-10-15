package com.hs3.lotts.ssc.star2.front;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SscStar2FrontAndPlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("200").divide(new BigDecimal(2));
    private String qunName = "前二";
    private String groupName = "直选";
    private String title = "和值";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), Integer.valueOf(18)}));
    private Integer[] betCount = {Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(9), Integer.valueOf(8), Integer.valueOf(7), Integer.valueOf(6), Integer.valueOf(5), Integer.valueOf(4), Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(1)};
    private NumberView[] view = {
            new NumberView("", this.nums, false, 0)};

    protected void init() {
        setRemark("至少选择一个和值，竞猜开奖号码前二位数字之和");
        setExample("投注：和值1 开奖：01***,10*** 即中奖");
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
        int n = ((Integer) openNums.get(index())).intValue() + ((Integer) openNums.get(index() + 1)).intValue();
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
        List<Integer> line = ListUtils.toIntList(bets);

        String format = null;
        if (index() == 0) {
            format = "%d%d---";
        } else {
            format = "---%d%d";
        }
        for (int n1 = 0; n1 <= 9; n1++) {
            for (int n2 = 0; n2 <= 9; n2++) {
                if (line.contains(Integer.valueOf(n1 + n2))) {
                    String k = String.format(format, new Object[]{Integer.valueOf(n1), Integer.valueOf(n2)});
                    result.put(k, getBonus());
                }
            }
        }
        return result;
    }
}
