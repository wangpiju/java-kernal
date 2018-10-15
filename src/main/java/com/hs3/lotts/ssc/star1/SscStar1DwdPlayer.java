package com.hs3.lotts.ssc.star1;

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

public class SscStar1DwdPlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("20").divide(new BigDecimal(2));
    private String qunName = "一星";
    private String groupName = "";
    private String title = "定位胆";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {
            new NumberView("万位", this.nums),
            new NumberView("千位", this.nums),
            new NumberView("百位", this.nums),
            new NumberView("十位", this.nums),
            new NumberView("个位", this.nums)};

    protected void init() {
        setRemark("从万位、千位、百位、十位、个位任意位置上至少选择1个号码，选号与相同位置上的开奖号码一致");
        setExample("投注：1**** 开奖：1**** 即中奖");
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
        int count = 0;
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            if (ListUtils.hasSame(n)) {
                return Integer.valueOf(0);
            }
            if (n.retainAll(this.nums)) {
                return Integer.valueOf(0);
            }
            count += n.size();
        }
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<String> lines = ListUtils.toList(bets);
        BigDecimal win = new BigDecimal("0");
        for (int i = 0; i < 5; i++) {
            int o = ((Integer) openNums.get(i)).intValue();
            String line = (String) lines.get(i);
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            if (n.contains(Integer.valueOf(o))) {
                win = win.add(getBonus());
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

        int index = -1;
        List<String> lines = ListUtils.toList(bets);
        for (String line : lines) {
            index++;
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            if (n.size() != 0) {
                String format = null;
                if (index == 0) {
                    format = "%d----";
                } else if (index == 1) {
                    format = "-%d---";
                } else if (index == 2) {
                    format = "--%d--";
                } else if (index == 3) {
                    format = "---%d-";
                } else if (index == 4) {
                    format = "----%d";
                }
                for (Iterator localIterator2 = n.iterator(); localIterator2.hasNext(); ) {
                    int num = ((Integer) localIterator2.next()).intValue();
                    String b = String.format(format, new Object[]{Integer.valueOf(num)});
                    result.put(b, getBonus());
                }
            }
        }
        return result;
    }
}
