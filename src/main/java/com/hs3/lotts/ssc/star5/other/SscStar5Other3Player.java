package com.hs3.lotts.ssc.star5.other;

import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SscStar5Other3Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("233.64").divide(new BigDecimal(2));
    private String qunName = "五星";
    private String groupName = "";
    private String title = "三星报喜";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("从0-9中至少选择1个号码投注，竞猜开奖号码中包含这个号码且出现3次，即中奖");
        setExample("投注：8 开奖：xx888（不限顺序）");
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

        Map<Integer, Integer> opens = new HashMap();
        Integer o;
        for (int i = 0; i < 5; i++) {
            Integer n = Integer.valueOf(1);
            o = (Integer) openNums.get(i);
            if (opens.containsKey(o)) {
                n = (Integer) opens.get(o);
                n = Integer.valueOf(n.intValue() + 1);
            }
            opens.put(o, n);
        }
        if (opens.size() > 3) {
            return win;
        }
        List<Integer> doubleNumList = new ArrayList();
        int n;
        for (Integer k : opens.keySet()) {
            n = ((Integer) opens.get(k)).intValue();
            if (n > 2) {
                doubleNumList.add(k);
            }
        }
        List<Integer> line = ListUtils.toIntList(bets);
        for (Integer winNum : doubleNumList) {
            if (line.contains(winNum)) {
                win = win.add(getBonus());
            }
        }
        return win;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        List<Integer> line = ListUtils.toIntList(bets);
        Map<String, BigDecimal> result = new HashMap();
        for (int i = 0; i < 100000; i++) {
            String str = StrUtils.parseLength(Integer.valueOf(i), 5);
            List<Integer> open = LotteryUtils.toIntListByLength(str, 1);
            for (Integer n : line) {
                int winCount = 0;
                for (Integer o : open) {
                    if (n.equals(o)) {
                        winCount++;
                    }
                    if (winCount > 2) {
                        break;
                    }
                }
                if (winCount > 2) {
                    result.put(str, getBonus());
                }
            }
        }
        return result;
    }
}
