package com.hs3.lotts.n11x5.x1;

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

public class N11x5DwdPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("22").divide(new BigDecimal(2));
    private String qunName = "选一";
    //private String groupName  = "前三";
    private String groupName  = "定位胆";
    private String title = "定位胆";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"}));
    private NumberView[] view = {new NumberView("第1位", this.nums), new NumberView("第2位", this.nums),
            new NumberView("第3位", this.nums), new NumberView("第4位", this.nums), new NumberView("第5位", this.nums)};

    protected void init() {
        setRemark("从第一位、第二位、第三位任意1个位置或多个位置上选择1个号码，所选号码与相同位置上的开奖号码一致，即为中奖");
        setExample("投注：01(第一位) 开奖：01,*,*,*,*");
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getQunName() {
        return this.qunName;
    }

    public String getGroupName() {
        return this.groupName;
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
            if (!line.equals("-")) {
                List<String> n = LotteryUtils.toListByLength(line, Integer.valueOf(2));
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
        for (int i = 0; i < 5; i++) {
            int o = ((Integer) openNums.get(i)).intValue();
            String line = (String) lines.get(i);
            List<Integer> n = LotteryUtils.toIntListByLength(line, 2);
            if (n.contains(Integer.valueOf(o))) {
                win = win.add(this.bonus);
            }
        }
        return win;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();

        int index = -1;
        List<String> lines = ListUtils.toList(bets);
        for (String line : lines) {
            index++;
            List<Integer> n = LotteryUtils.toIntListByLength(line, 2);
            if (n.size() != 0) {
                String format = null;
                if (index == 0) {
                    format = "%d,-,-,-,-";
                } else if (index == 1) {
                    format = "-,%d,-,-,-";
                } else if (index == 2) {
                    format = "-,-,%d,-,-";
                } else if (index == 3) {
                    format = "-,-,-,%d,-";
                } else if (index == 4) {
                    format = "-,-,-,-,%d";
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
