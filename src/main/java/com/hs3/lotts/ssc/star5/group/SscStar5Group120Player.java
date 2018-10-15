package com.hs3.lotts.ssc.star5.group;

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

public class SscStar5Group120Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("1666.66").divide(new BigDecimal(2));
    private String qunName = "五星";
    private String groupName = "";
    private String title = "组选120";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("至少选择五个号码投注，竞猜开奖号码的全部五位，号码一致顺序不限即中奖");
        setExample("投注：02568 开奖：02568（不限顺序）");
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
        List<Integer> line = ListUtils.toIntList(bets);
        if (ListUtils.hasSame(line)) {
            return Integer.valueOf(0);
        }
        if (line.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(LotteryUtils.getCombin(line.size(), 5));
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        Set<Integer> nums = new HashSet();
        nums.addAll(openNums);
        if (nums.size() != 5) {
            return win;
        }
        List<Integer> line = ListUtils.toIntList(bets);
        for (int i = 0; i < 5; i++) {
            int o = ((Integer) openNums.get(i)).intValue();
            if (!line.contains(Integer.valueOf(o))) {
                return win;
            }
        }
        win = win.add(getBonus());
        return win;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();

        List<Integer> line = ListUtils.toIntList(bets);
        for (int i1 = 0; i1 < line.size(); i1++) {
            int n1 = ((Integer) line.get(i1)).intValue();
            for (int i2 = i1 + 1; i2 < line.size(); i2++) {
                int n2 = ((Integer) line.get(i2)).intValue();
                for (int i3 = i2 + 1; i3 < line.size(); i3++) {
                    int n3 = ((Integer) line.get(i3)).intValue();
                    for (int i4 = i3 + 1; i4 < line.size(); i4++) {
                        int n4 = ((Integer) line.get(i4)).intValue();
                        for (int i5 = i4 + 1; i5 < line.size(); i5++) {
                            int n5 = ((Integer) line.get(i5)).intValue();

                            Set<String> keys1 = PermutateUtils.getPerms(new Integer[]{Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3), Integer.valueOf(n4), Integer.valueOf(n5)});
                            for (String k : keys1) {
                                result.put(k, getBonus());
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
