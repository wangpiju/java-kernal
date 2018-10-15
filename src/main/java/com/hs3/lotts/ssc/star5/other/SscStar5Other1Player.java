package com.hs3.lotts.ssc.star5.other;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SscStar5Other1Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("4.88").divide(new BigDecimal(2));
    private String qunName = "五星";
    private String groupName = "";
    private String title = "一帆风顺";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {
            new NumberView("", this.nums)};

    protected void init() {
        setRemark("从0-9中至少选择1个号码投注，竞猜开奖号码中包含这个号码，包含即中奖");
        setExample("投注：1 开奖：xx1xx（不限顺序）");
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
        return Integer.valueOf(line.size());
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        List<Integer> line = ListUtils.toIntList(bets);
        Set<Integer> nums = new HashSet();
        nums.addAll(openNums);
        for (Integer num : nums) {
            if (line.contains(num)) {
                win = win.add(getBonus());
            }
        }
        return win;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        List<String> line = ListUtils.toList(bets);
        Map<String, BigDecimal> result = new HashMap();
        for (int i = 0; i < 100000; i++) {
            String open = StrUtils.parseLength(Integer.valueOf(i), 5);
            for (String n : line) {
                if (open.contains(n.toString())) {
                    result.put(open, getBonus());
                }
            }
        }
        return result;
    }
}
