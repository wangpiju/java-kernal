package com.hs3.lotts.n3.star3.group;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PermutateUtils;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

public class N3Star3GroupContainsPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("666.66").divide(new BigDecimal(2));
    private BigDecimal bonus2 = new BigDecimal("333.33").divide(new BigDecimal(2));
    private String qunName = "三星";
    private String groupName = "组选";
    private String title = "包胆";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1),
            Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6),
            Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums, false, 1)};

    protected void init() {
        setRemark("从0-9中任意选择1个包胆号码，开奖号码中任意1位与所选包胆号码相同(不含豹子号)，即为中奖");
        setExample("投注：包胆3开奖：(1) 3xx或者33x（不限顺序）,中组三 (2)3xy（不限顺序）,中组六   注：x≠y≠3");
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
        count = 54 * line.size();
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        List<Integer> line = ListUtils.toIntList(bets);
        Set<Integer> opens = new HashSet();
        opens.add((Integer) openNums.get(0));
        opens.add((Integer) openNums.get(1));
        opens.add((Integer) openNums.get(2));
        if (opens.size() == 1) {
            return win;
        }
        boolean group3 = opens.size() == 2;
        for (Integer o : opens) {
            if (line.contains(o)) {
                if (group3) {
                    win = win.add(this.bonus);
                } else {
                    win = win.add(this.bonus2);
                }
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

        int n1 = Integer.parseInt(bets);
        for (int n2 = 0; n2 <= 9; n2++) {
            for (int n3 = 0; n3 <= 9; n3++) {
                if ((n1 != n2) || (n1 != n3)) {
                    BigDecimal b = null;
                    if ((n1 == n2) || (n1 == n3) || (n2 == n3)) {
                        b = this.bonus;
                    } else {
                        b = this.bonus2;
                    }
                    Set<String> keys1 = PermutateUtils
                            .getPerms(new Integer[]{Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3)});
                    for (String key : keys1) {
                        result.put(key, b);
                    }
                }
            }
        }
        return result;
    }
}
