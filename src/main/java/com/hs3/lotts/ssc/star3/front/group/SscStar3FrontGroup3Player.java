package com.hs3.lotts.ssc.star3.front.group;

import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PermutateUtils;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SscStar3FrontGroup3Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("666.66").divide(new BigDecimal(2));
    private String qunName = "前三";
    private String groupName = "";
    private String title = "组三";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("从0-9中选择2个数字组成两注，所选号码与开奖号码的前三位相同，顺序不限");
        setExample("投注：112** 开奖：112**（不限顺序）");
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
        List<Integer> line = ListUtils.toIntList(bets);
        if (ListUtils.hasSame(line)) {
            return Integer.valueOf(0);
        }
        if (line.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        int count = LotteryUtils.getCombin(line.size(), 2) * 2;
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        Set<Integer> opens = new HashSet();
        opens.add((Integer) openNums.get(index()));
        opens.add((Integer) openNums.get(index() + 1));
        opens.add((Integer) openNums.get(index() + 2));
        if (opens.size() != 2) {
            return win;
        }
        List<Integer> line = ListUtils.toIntList(bets);
        for (Integer o : opens) {
            if (!line.contains(o)) {
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

        List<Integer> line = ListUtils.toIntList(bets);

        String format = null;
        if (index() == 0) {
            format = "%s--";
        } else if (index() == 1) {
            format = "-%s-";
        } else {
            format = "--%s";
        }
        for (int i = 0; i < line.size(); i++) {
            int n1 = ((Integer) line.get(i)).intValue();
            for (int j = i + 1; j < line.size(); j++) {
                int n2 = ((Integer) line.get(j)).intValue();
                Set<String> keys1 = PermutateUtils.getPerms(new Integer[]{Integer.valueOf(n1), Integer.valueOf(n1), Integer.valueOf(n2)});
                Set<String> keys2 = PermutateUtils.getPerms(new Integer[]{Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n2)});
                keys1.addAll(keys2);
                for (String key : keys1) {
                    String k = String.format(format, new Object[]{key});
                    result.put(k, getBonus());
                }
            }
        }
        return result;
    }
}
