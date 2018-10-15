package com.hs3.lotts.k3.star2;

import com.hs3.utils.CollectionUtils;
import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class K3Star2SameNotPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("14.4").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "二不同";
    private List<String> nums = new ArrayList<String>(Arrays.asList(
            new String[]{"12", "13", "14", "15", "16", "23", "24", "25", "26", "34", "35", "36", "45", "46", "56"}));
    private List<String> nums1 = new ArrayList<String>(Arrays.asList(new String[]{"12", "13", "14", "15", "16"}));
    private List<String> nums2 = new ArrayList<String>(Arrays.asList(new String[]{"23", "24", "25", "26", "34"}));
    private List<String> nums3 = new ArrayList<String>(Arrays.asList(new String[]{"35", "36", "45", "46", "56"}));
    private NumberView[] view = {new NumberView("", this.nums1, false), new NumberView("", this.nums2, false),
            new NumberView("", this.nums3, false)};

    protected void init() {
        setRemark("对所有2不同号进行单选或多选，选号与开奖号中任意2个号码相同即中奖");
        setExample("投注：12 开奖：123 即中奖");
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
            return Integer.valueOf(0);
        }
        if (lines.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(lines.size());
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<String> lines = ListUtils.toList(bets);
        BigDecimal win = new BigDecimal("0");

        for (String line : lines) {
            List<Integer> ns = LotteryUtils.toIntListByLength(line, 1);
            boolean isWin = true;
            for (Integer n : ns) {
                if (!openNums.contains(n)) {
                    isWin = false;
                    break;
                }
            }
            if (isWin) {
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
        List<String> lines = ListUtils.toList(bets);
        for (Iterator<String> iterator = lines.iterator(); iterator.hasNext(); ) {
            String line = (String) iterator.next();
            List<Integer> ns = LotteryUtils.toIntListByLength(line, 1);
            int n1 = ((Integer) ns.get(0)).intValue();
            int n2 = ((Integer) ns.get(1)).intValue();

            for (int i = 1; i <= 6; i++) {
                result.put((new StringBuilder(String.valueOf(i))).append(n1).append(n2).toString(), getBonus());
                result.put((new StringBuilder(String.valueOf(i))).append(n2).append(n1).toString(), getBonus());
                result.put((new StringBuilder(String.valueOf(n1))).append(i).append(n2).toString(), getBonus());
                result.put((new StringBuilder(String.valueOf(n2))).append(i).append(n1).toString(), getBonus());
                result.put((new StringBuilder(String.valueOf(n1))).append(n2).append(i).toString(), getBonus());
                result.put((new StringBuilder(String.valueOf(n2))).append(n1).append(i).toString(), getBonus());
            }
        }

        return result;
    }
}
