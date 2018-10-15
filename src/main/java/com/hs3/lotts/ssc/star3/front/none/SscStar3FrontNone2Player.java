package com.hs3.lotts.ssc.star3.front.none;

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

public class SscStar3FrontNone2Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("37.04").divide(new BigDecimal(2));
    private String qunName = "前三";
    private String groupName = "";
    private String title = "二码不定位";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("从0-9中至少选择2个号码投注，竞猜开奖号码前三位中包含这2个号码，包含即中奖");
        setExample("投注：1，2  开奖：12x**（不限顺序） 即中奖");
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
        return Integer.valueOf(LotteryUtils.getCombin(line.size(), 2));
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        List<Integer> line = ListUtils.toIntList(bets);
        Set<Integer> opens = new HashSet();
        opens.add((Integer) openNums.get(index()));
        opens.add((Integer) openNums.get(index() + 1));
        opens.add((Integer) openNums.get(index() + 2));
        int winCount = 0;
        for (Integer n : opens) {
            if (line.contains(n)) {
                winCount++;
            }
        }
        Integer count = Integer.valueOf(LotteryUtils.getCombin(winCount, 2));
        win = getBonus().multiply(new BigDecimal(count.toString()));
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
                for (int n3 = 0; n3 <= 9; n3++) {
                    String nK = String.format(format, new Object[]{n1 + n2 + n3});
                    if (!result.containsKey(nK)) {
                        BigDecimal b = getBonus();
                        if ((line.contains(Integer.valueOf(n3))) &&
                                (n3 != n1) && (n3 != n2)) {
                            b = b.multiply(new BigDecimal("3"));
                        }
                        Set<String> keys1 = PermutateUtils.getPerms(new Integer[]{Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3)});
                        for (String key : keys1) {
                            String k = String.format(format, new Object[]{key});
                            result.put(k, b);
                        }
                    }
                }
            }
        }
        return result;
    }
}
