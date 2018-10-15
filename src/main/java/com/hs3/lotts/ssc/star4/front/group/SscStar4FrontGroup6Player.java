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
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SscStar4FrontGroup6Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("3333.33").divide(new BigDecimal(2));
    private String qunName = "前四";
    private String groupName = "";
    private String title = "组选6";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("至少选择2个二重号码，竞猜开奖号码的前四位，号码一致顺序不限即中奖");
        setExample("投注：0088*（0·8为二重号） 开奖：0088*（不限顺序） 即中奖");
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

        Integer o1 = (Integer) openNums.get(index());
        Integer o2 = (Integer) openNums.get(index() + 1);
        Integer o3 = (Integer) openNums.get(index() + 2);
        Integer o4 = (Integer) openNums.get(index() + 3);

        Integer oo1 = null;
        Integer oo2 = null;
        if ((o1 == o2) && (o3 == o4) && (o1 != o3)) {
            oo1 = o1;
            oo2 = o3;
        } else if ((o1 == o3) && (o2 == o4) && (o1 != o2)) {
            oo1 = o1;
            oo2 = o2;
        } else if ((o1 == o4) && (o2 == o3) && (o1 != o2)) {
            oo1 = o1;
            oo2 = o2;
        }
        if ((oo1 == null) || (oo2 == null)) {
            return win;
        }
        List<Integer> line = ListUtils.toIntList(bets);
        if ((line.contains(oo1)) && (line.contains(oo2))) {
            win = win.add(getBonus());
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

                Set<String> keys1 = PermutateUtils.getPerms(new Integer[]{Integer.valueOf(n1),
                        Integer.valueOf(n2), Integer.valueOf(n1), Integer.valueOf(n2)});
                for (String key : keys1) {
                    String k = String.format(format, new Object[]{key});
                    result.put(k, getBonus());
                }
            }
        }
        return result;
    }
}
