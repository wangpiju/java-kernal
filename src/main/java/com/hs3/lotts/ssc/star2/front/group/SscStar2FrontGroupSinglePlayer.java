package com.hs3.lotts.ssc.star2.front.group;

import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SscStar2FrontGroupSinglePlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("100").divide(new BigDecimal(2));
    private String qunName = "前二";
    private String groupName = "组选";
    private String title = "单式";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));

    protected int index() {
        return 0;
    }

    protected void init() {
        setRemark("从0-9中选择2个数字组成一注，所选号码与开奖号码的前二位相同，顺序不限");
        setExample("投注：5,8 开奖：58***(不限顺序) 即中奖");
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

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            if (n.size() != 2) {
                return Integer.valueOf(0);
            }
            if (ListUtils.hasSame(n)) {
                return Integer.valueOf(0);
            }
            if (n.retainAll(this.nums)) {
                return Integer.valueOf(0);
            }
        }
        return Integer.valueOf(lines.size());
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        int o1 = ((Integer) openNums.get(index())).intValue();
        int o2 = ((Integer) openNums.get(index() + 1)).intValue();
        if (o1 == o2) {
            return win;
        }
        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            if ((n.contains(Integer.valueOf(o1))) && (n.contains(Integer.valueOf(o2)))) {
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

        String format = null;
        if (index() == 0) {
            format = "%d%d---";
        } else {
            format = "---%d%d";
        }
        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 1);
            int n1 = ((Integer) n.get(0)).intValue();
            int n2 = ((Integer) n.get(1)).intValue();

            String b1 = String.format(format, new Object[]{Integer.valueOf(n1), Integer.valueOf(n2)});
            String b2 = String.format(format, new Object[]{Integer.valueOf(n2), Integer.valueOf(n1)});
            addMap(result, b1, getBonus());
            addMap(result, b2, getBonus());
        }
        return result;
    }
}
