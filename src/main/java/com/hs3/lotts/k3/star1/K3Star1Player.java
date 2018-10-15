package com.hs3.lotts.k3.star1;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.*;

public class K3Star1Player extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("4.74").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "单挑一骰";
    private List<Integer> nums = new ArrayList<Integer>(Arrays.asList(new Integer[]{Integer.valueOf(1),
            Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6)}));
    private NumberView[] view = {new NumberView("", this.nums, false)};

    protected void init() {
        setRemark("选择1个或者多个骰号，如果开奖号码中包含该号（顺序不限）即中奖");
        setExample("投注：1** 开奖：1** 即中奖");
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
        List<Integer> lines = ListUtils.toIntList(bets);
        if (ListUtils.hasSame(lines)) {
            return Integer.valueOf(0);
        }
        if (lines.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(lines.size());
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<Integer> lines = ListUtils.toIntList(bets);
        BigDecimal win = new BigDecimal("0");

        for (Integer n : lines) {
            if (openNums.contains(n)) {
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
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();

        List<Integer> lines = ListUtils.toIntList(bets);
        for (int n1 = 1; n1 <= 6; n1++) {
            for (int n2 = 1; n2 <= 6; n2++) {
                for (int n3 = 1; n3 <= 6; n3++) {
                    BigDecimal mon = BigDecimal.ZERO;
                    if (lines.contains(Integer.valueOf(n1))) {
                        mon = mon.add(getBonus());
                    }
                    if (lines.contains(Integer.valueOf(n2))) {
                        mon = mon.add(getBonus());
                    }
                    if (lines.contains(Integer.valueOf(n3))) {
                        mon = mon.add(getBonus());
                    }
                    if (mon.compareTo(BigDecimal.ZERO) > 0) {
                        result.put((new StringBuilder(String.valueOf(n1))).append(n2).append(n3).toString(), mon);
                    }
                }
            }
        }
        return result;
    }
}
