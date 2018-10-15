package com.hs3.lotts.ssc.star5;

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

public class SscStar5Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("200000").divide(new BigDecimal(2));
    private String qunName = "五星";
    private String groupName = "";
    private String title = "复式";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {
            new NumberView("万位", this.nums),
            new NumberView("千位", this.nums),
            new NumberView("百位", this.nums),
            new NumberView("十位", this.nums),
            new NumberView("个位", this.nums)};

    protected void init() {
        setRemark("每位至少选择一个号码，竞猜开奖号码的全部五位，号码和位置都对应即中奖");
        setExample("投注：23456 开奖：23456");
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
        if (lines.size() != 5) {
            return Integer.valueOf(0);
        }
        int count = 1;
        for (int i = 0; i < 5; i++) {
            List<Integer> n = LotteryUtils.toIntListByLength((String) lines.get(i), 1);
            if (ListUtils.hasSame(n)) {
                return Integer.valueOf(0);
            }
            if (n.retainAll(this.nums)) {
                return Integer.valueOf(0);
            }
            count *= n.size();
        }
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        List<String> lines = ListUtils.toList(bets);
        for (int i = 0; i < 5; i++) {
            int o = ((Integer) openNums.get(i)).intValue();
            List<Integer> n = LotteryUtils.toIntListByLength((String) lines.get(i), 1);
            if (!n.contains(Integer.valueOf(o))) {
                return win;
            }
        }
        win = win.add(getBonus());
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

        List<String> lines = ListUtils.toList(bets);
        List<List<Integer>> allBuy = new ArrayList();
        for (int i = 0; i < 5; i++) {
            List<Integer> n = LotteryUtils.toIntListByLength((String) lines.get(i), 1);
            allBuy.add(n);
        }
        Set<String> num = PermutateUtils.getAllNum(allBuy, "", true);
        for (String k : num) {
            result.put(k, getBonus());
        }
        return result;
    }
}
