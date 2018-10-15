package com.hs3.lotts.k3.star3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.cookie.PublicSuffixListParser;

import com.hs3.utils.CollectionUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

public class K3Star3AndPlayer extends PlayerBase {
    public static void main(String[] args) {
        BigDecimal win = new BigDecimal(0);
        System.out.println(win = win.add(new BigDecimal("216").divide(new BigDecimal(15), 2, 3)));
    }

    private Integer[] betCount = {1,3, 6, // Integer.valueOf(10),
            // 6
            9, 15, 21,
            // 9
            24, 27, 27,
            // 12
            24, 21, 15,
            // 15
            9, 6, 3,1, 1, 1,
            1, 1};
    private BigDecimal bonus = new BigDecimal("216");
    private BigDecimal bonus1 = new BigDecimal("2.05"); // 大小單雙理論獎金
    public static String defaultDisplayBonusNum = "04";    //默认最高奖金的号码
    private String qunName = "";
    private String groupName = "";
    private String title = "和值";
    private List<String> nums = new ArrayList<>(Arrays.asList("03","04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18","大", "小", "单", "双"));
    private List<String> nums1 = new ArrayList<>(
            Arrays.asList("03","04", "05", "06", "07", "08", "09", "10"));
    private List<String> nums2 = new ArrayList<>(
            Arrays.asList("11", "12", "13", "14", "15", "16", "17", "18"));
    private List<String> nums3 = new ArrayList<>(Arrays.asList("大", "小", "单", "双"));

    private NumberView[] view = {new NumberView("", this.nums1, false), new NumberView("", this.nums2, false),
            new NumberView("", this.nums3, false)};


    protected void init() {
        setRemark("至少选择1个和值（3个号码之和）进行投注，所选和值与开奖的3个号码的和值相同即中奖");
        setExample("投注：15 开奖：456 即中奖");
    }

    private enum NumCountEnum {
        three("03", 1, "k3_star3_and"),
        four("04", 3, "k3_star3_and"),
        five("05", 6, "k3_star3_and"),
        six("06", 9, "k3_star3_and"),
        seven("07", 15, "k3_star3_and"),
        eight("08", 21, "k3_star3_and"),
        nine("09", 24, "k3_star3_and"),
        ten("10", 27, "k3_star3_and"),
        eleven("11", 27, "k3_star3_and"),
        twelve("12", 24, "k3_star3_and"),
        thirteen("13", 21, "k3_star3_and"),
        fourteen("14", 15, "k3_star3_and"),
        fifteen("15", 9, "k3_star3_and"),
        sixteen("16", 6, "k3_star3_and"),
        seventeen("17", 3, "k3_star3_and"),
        eighteen("18", 1, "k3_star3_and"),
        da("大", 1, "k3_star3_big_odd"),
        xiao("小", 1, "k3_star3_big_odd"),
        dan("单", 1, "k3_star3_big_odd"),
        shuang("双", 1, "k3_star3_big_odd");

        private String num;
        private Integer count;
        private String groupId;

        NumCountEnum(String num, int count, String groupId) {
            this.num = num;
            this.count = count;
            this.groupId = groupId;
        }

        private static Map<String, NumCountEnum> map = new HashMap<>();

        static {
            for (NumCountEnum nc : NumCountEnum.values()) {
                map.put(nc.num, nc);
            }
        }

        public static NumCountEnum parse(String num) {
            return map.get(num);
        }

        public Integer getCount() {
            return count;
        }

        public String getGroupId() {
            return groupId;
        }
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

        return lines.size();
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<String> lines = ListUtils.toList(bets, ",");
        List<Integer> lines2 = ListUtils.toIntList(bets, ",");
        BigDecimal win = new BigDecimal("0");
        // 豹子通殺
        if (CollectionUtils.checkAllSame(openNums))
            return win;

        // 和值
        int all = 0;

        for (int o: openNums ) {
            all += o;
        }
        win = win.add(getSideWin(bets, all));

        if (lines2.contains(all)) {
            try {
                win = win.add(getBonus().divide(new BigDecimal(betCount[all - 3]), 2, 3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return win;
    }

    /**
     * 宏发快3取消豹子通杀，其他快三保留
     * @param bets
     * @param openNums
     * @return
     */
    public BigDecimal getWinPlus(String bets, List<Integer> openNums) {
        List<Integer> lines2 = ListUtils.toIntList(bets, ",");
        BigDecimal win = new BigDecimal("0");
        // TODO  取消掉 豹子通殺
//        if (CollectionUtils.checkAllSame(openNums))
//            return win;

        // 和值
        int all = 0;

        for (Integer o : openNums) {
            all += o;
        }
        win = win.add(getSideWin(bets, all));

        if (lines2.contains(all)) {
            try {
                win = win.add(getBonus().divide(new BigDecimal(betCount[all - 3]), 2, 3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return win;
    }

    public BigDecimal getSideWin(String bets, Integer all) {
        K3Star3BigOddPlayer bigOdd = new K3Star3BigOddPlayer();
        BigDecimal win = new BigDecimal("0");
        win = win.add(bigOdd.getSideWin(bets, all));
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
                // e.printStackTrace();
            }

            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 6; j++) {
                    for (int k = 1; k <= 6; k++) {
                        int and = i + j + k;
                        if (and == n) {
                            if (i == j && i == k)
                                result.put((new StringBuilder(String.valueOf(i))).append(j).append(k).toString(),
                                        BigDecimal.ZERO);
                            else
                                result.put((new StringBuilder(String.valueOf(i))).append(j).append(k).toString(),
                                        getBonus());
                        }

                    }

                }

            }
        }

        return result;

    }

    public BigDecimal getBonus1() {
        return bonus1;
    }

    public void setBonus1(BigDecimal bonus1) {
        this.bonus1 = bonus1;
    }

    public Integer[] getBetCount() {
        return betCount;
    }

    public void setBetCount(Integer[] betCount) {
        this.betCount = betCount;
    }

    public static NumCountEnum getNumCount(String num) {
        return NumCountEnum.parse(num);
    }

    public static Integer getSingleBetCount(String num) {
        return NumCountEnum.parse(num).getCount();
    }

    public static String getSingleGroupId(String num) {
        return NumCountEnum.parse(num).getGroupId();
    }

}
