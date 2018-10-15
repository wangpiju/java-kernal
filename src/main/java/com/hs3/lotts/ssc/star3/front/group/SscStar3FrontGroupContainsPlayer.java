package com.hs3.lotts.ssc.star3.front.group;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PermutateUtils;
import com.hs3.lotts.PlayerBase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SscStar3FrontGroupContainsPlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("666.66").divide(new BigDecimal(2));
    private BigDecimal bonus2 = new BigDecimal("333.33").divide(new BigDecimal(2));
    private String qunName = "前三";
    private String groupName = "组选";
    private String title = "包胆";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums, false, 1)};

    protected void init() {
        setRemark("从0-9中任意选择1个包胆号码，开奖号码的前三位中任意1位与所选包胆号码相同(不含豹子号)，即为中奖");
        setExample("投注：包胆3开奖：(1) 3xx**或者33x**（不限顺序）,中组三 (2)3xy**（不限顺序）,中组六   注：x≠y≠3");
    }

    protected int index() {
        return 0;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getBonusStr() {
        return this.bonus2 + " - " + this.bonus;
    }

    public String getTitle() {
        return this.title;
    }

    public NumberView[] getNumView() {
        return this.view;
    }

    public Integer getCount(String bets) {
        try {
            int betNum = Integer.parseInt(bets);
            if ((betNum < 0) || (betNum > 9)) {
                return Integer.valueOf(0);
            }
            return Integer.valueOf(54);
        } catch (NumberFormatException e) {
        }
        return Integer.valueOf(0);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        int betNum = -1;
        try {
            betNum = Integer.parseInt(bets);
        } catch (NumberFormatException e) {
            return win;
        }
        Set<Integer> opens = new HashSet();
        opens.add((Integer) openNums.get(index()));
        opens.add((Integer) openNums.get(index() + 1));
        opens.add((Integer) openNums.get(index() + 2));
        if (opens.size() == 1) {
            return win;
        }
        boolean group3 = opens.size() == 2;
        if (opens.contains(Integer.valueOf(betNum))) {
            if (group3) {
                win = win.add(this.bonus);
            } else {
                win = win.add(this.bonus2);
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

        String format = null;
        if (index() == 0) {
            format = "%s--";
        } else if (index() == 1) {
            format = "-%s-";
        } else {
            format = "--%s";
        }
        for (int n2 = 0; n2 <= 9; n2++) {
            for (int n3 = 0; n3 <= 9; n3++) {
                if ((n1 != n2) || (n1 != n3)) {
                    BigDecimal b = null;
                    if ((n1 == n2) || (n1 == n3) || (n2 == n3)) {
                        b = this.bonus;
                    } else {
                        b = this.bonus2;
                    }
                    Set<String> keys1 = PermutateUtils.getPerms(new Integer[]{Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3)});
                    for (String key : keys1) {
                        String k = String.format(format, new Object[]{key});
                        result.put(k, b);
                    }
                }
            }
        }
        return result;
    }
}
