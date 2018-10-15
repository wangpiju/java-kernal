package com.hs3.lotts.n3.star2.front.group;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class N3Star2FrontGroupContainsPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("100").divide(new BigDecimal(2));
    private String qunName = "前二";
    private String groupName = "组选";
    private String title = "包胆";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1),
            Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6),
            Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums, false, 1)};

    protected void init() {
        setRemark("从0-9中任意选择1个号码，开奖号码的前二位中任意1位包含所选的包胆号码相同（不含对子）");
        setExample("投注：包胆8；开奖：x8*（不限顺序，x≠8） 即中奖");
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
        count = line.size() * 9;
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
        if (line.contains(Integer.valueOf(o1))) {
            win = win.add(this.bonus);
        }
        if (line.contains(Integer.valueOf(o2))) {
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

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();
        int n = Integer.parseInt(bets);

        String format = null;
        if (index() == 0) {
            format = "%d%d-";
        } else {
            format = "-%d%d";
        }
        for (int n1 = 9; n1 >= 0; n1--) {
            if (n1 != n) {
                String b1 = String.format(format, new Object[]{Integer.valueOf(n), Integer.valueOf(n1)});
                String b2 = String.format(format, new Object[]{Integer.valueOf(n1), Integer.valueOf(n)});
                result.put(b1, getBonus());
                result.put(b2, getBonus());
            }
        }
        return result;
    }
}
