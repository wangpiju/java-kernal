package com.hs3.lotts.k3.star3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hs3.utils.CollectionUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

public class K3Star3BigOddPlayer extends PlayerBase {
    // private Integer[] betCount = { Integer.valueOf(1), Integer.valueOf(1),
    // Integer.valueOf(1),Integer.valueOf(1) };
    private BigDecimal bonus = new BigDecimal("2.05");
    private String qunName = "";
    private String groupName = "";
    private String title = "大小单双";
    private List<String> nums = new ArrayList<>(Arrays.asList("大", "小", "单", "双"));

    private List<String> nums1 = new ArrayList<>(Arrays.asList("大", "小", "单", "双"));
    private NumberView[] view = {new NumberView("", this.nums1, false)};

    protected void init() {
        setRemark("至少选择1个和值（3个号码之和）进行投注，所选和值与开奖的3个号码的和值相同即中奖");
        setExample("投注：15 开奖：456 即中奖");
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
        List<String> lines = ListUtils.toList(bets);
        if (ListUtils.hasSame(lines)) {
            return 0;
        }
        if (lines.retainAll(this.nums)) {
            return 0;
        }

        return lines.size();
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        //TODO 取消掉和值和大小单双的 豹子通殺
        if (CollectionUtils.checkAllSame(openNums))
            return win;

        // 和值
        int all = 0;

        for (Integer integer : openNums) {
            int o = integer;
            all += o;
        }
        // 大小單雙
        win = getSideWin(bets, all);
        return win;
    }

    /**
     * 宏发快三取消豹子通杀，其他快三保留
     */
    public BigDecimal getWinPlus(String bets, List<Integer> openNums) {
        BigDecimal win;
        //TODO 取消掉和值和大小单双的 豹子通殺
//        if (CollectionUtils.checkAllSame(openNums))
//            return win;

        // 和值
        int all = 0;

        for (Integer integer : openNums) {
            int o = integer;
            all += o;
        }
        // 大小單雙
        win = getSideWin(bets, all);
        return win;
    }

    public BigDecimal getSideWin(String bets, Integer all) {
        List<String> lines = ListUtils.toList(bets);
        BigDecimal win = new BigDecimal("0");

        for (String line: lines) {

            boolean isOdd = all % 2 != 0;
//            boolean isBig = (11 <= all && 17 >= all);
//            boolean isSmall = (4 <= all && 10 >= all);
            boolean isBig = (11 <= all && 18 >= all);
            boolean isSmall = (3 <= all && 10 >= all);

            if ((line.equals("大")) && (isBig)) {
                win = win.add(getBonus());
            } else if ((line.equals("小")) && (isSmall)) {
                win = win.add(getBonus());
            } else if ((line.equals("单")) && (isOdd)) {
                win = win.add(getBonus());
            } else if ((line.equals("双")) && (!isOdd)) {
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
        Map<String, BigDecimal> result = new HashMap<>();
        List<String> lines = ListUtils.toList(bets);
        for (String line : lines) {
            int n = 0;
            try {
                n = new Integer(line);
            } catch (Exception e) {// do nothing

            }

            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 6; j++) {
                    for (int k = 1; k <= 6; k++) {
                        int and = i + j + k;
                        if (and == n)
                            result.put((new StringBuilder(String.valueOf(i))).append(j).append(k).toString(),
                                    getBonus());
                    }

                }

            }

        }

        return result;

    }

}
