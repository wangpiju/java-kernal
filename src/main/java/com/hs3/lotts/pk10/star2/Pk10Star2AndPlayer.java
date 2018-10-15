package com.hs3.lotts.pk10.star2;

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

public class Pk10Star2AndPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("180").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "冠亚和";
    private String basicBet = "03";
    private List<String> nums = new ArrayList(Arrays.asList(new String[]{"03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"}));
    private int[] betCount = {2, 2, 4, 4, 6, 6, 8, 8, 10, 8, 8, 6, 6, 4, 4, 2, 2};
    private NumberView[] view = {new NumberView("", this.nums, false)};

    protected void init() {
        setRemark("从03-19至少选择1个号码，所选号码与开奖号码前二名车号之和相等，即中奖");
        setExample("投注：04 开奖：01,03,*,*,*,*,*,*,*,* 即中奖");
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
        if (ListUtils.hasSame(lines)) {
            return Integer.valueOf(0);
        }
        if (lines.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        int count = 0;
        for (String n : lines) {
            Integer i = Integer.valueOf(Integer.parseInt(n));
            count += this.betCount[(i.intValue() - 3)];
        }
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        Integer n = Integer.valueOf(((Integer) openNums.get(0)).intValue() + ((Integer) openNums.get(1)).intValue());

        List<Integer> lines = ListUtils.toIntList(bets);
        if (lines.contains(n)) {
            win = win.add(this.bonus);
        }
        return win;
    }

    public String getQunName() {
        return this.qunName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    private String getKey(int n1, int n2) {
        StringBuilder sb = new StringBuilder();
        sb.append(n1).append(",").append(n2).append(",-,-,-,-,-,-,-,-");
        return sb.toString();
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();

        List<Integer> nums = ListUtils.toIntList(bets);

        /**
         * jd-gui int i1; for (Iterator localIterator = nums.iterator();
         * localIterator.hasNext(); i1 <= 10) { int n =
         * ((Integer)localIterator.next()).intValue(); i1 = 1; continue; int i2 = n -
         * i1; if ((i2 != i1) && (i2 >= 1) && (i2 <= 10)) { result.put(getKey(i1, i2),
         * this.bonus); } i1++; }
         */

        for (Iterator iterator = nums.iterator(); iterator.hasNext(); ) {
            int n = ((Integer) iterator.next()).intValue();
            for (int i1 = 1; i1 <= 10; i1++) {
                int i2 = n - i1;
                if (i2 != i1 && i2 >= 1 && i2 <= 10)
                    result.put(getKey(i1, i2), bonus);
            }

        }

        return result;
    }

    public String getBasicBet() {
        return basicBet;
    }

    public void setBasicBet(String basicBet) {
        this.basicBet = basicBet;
    }
}
