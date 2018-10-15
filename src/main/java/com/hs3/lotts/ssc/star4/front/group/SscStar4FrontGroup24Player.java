package com.hs3.lotts.ssc.star4.front.group;

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

public class SscStar4FrontGroup24Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("833.33").divide(new BigDecimal(2));
    private String qunName = "前四";
    private String groupName = "";
    private String title = "组选24";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("至少选择4个号码投注，竞猜开奖号码的前4位，号码一致顺序不限即中奖");
        setExample("投注：2568* 开奖：2568*（不限顺序） 即中奖");
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
        return Integer.valueOf(LotteryUtils.getCombin(line.size(), 4));
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        Set<Integer> opens = new HashSet();
        opens.add((Integer) openNums.get(index()));
        opens.add((Integer) openNums.get(index() + 1));
        opens.add((Integer) openNums.get(index() + 2));
        opens.add((Integer) openNums.get(index() + 3));
        if (opens.size() != 4) {
            return win;
        }
        List<Integer> line = ListUtils.toIntList(bets);
        for (Integer n : opens) {
            if (!line.contains(n)) {
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
            format = "%s-";
        } else {
            format = "-%s";
        }
        for (int i = 0; i < line.size(); i++) {
            int n1 = ((Integer) line.get(i)).intValue();
            for (int j = i + 1; j < line.size(); j++) {
                int n2 = ((Integer) line.get(j)).intValue();
                for (int h = j + 1; h < line.size(); h++) {
                    int n3 = ((Integer) line.get(h)).intValue();
                    for (int m = h + 1; m < line.size(); m++) {
                        int n4 = ((Integer) line.get(m)).intValue();

                        Set<String> keys1 =
                                PermutateUtils.getPerms(new Integer[]{Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3), Integer.valueOf(n4)});
                        for (String key : keys1) {
                            String k = String.format(format, new Object[]{key});
                            result.put(k, getBonus());
                        }
                    }
                }
            }
        }
        return result;
    }
}
