package com.hs3.lotts.pk10.star2;

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

public class Pk10Star2Player extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("180").divide(new BigDecimal(2));
    private String qunName = "前二";
    private String groupName = "";
    private String title = "复式";
    private String basicBet = "01,02";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"}));
    private NumberView[] view = {new NumberView("冠军", this.nums), new NumberView("亚军", this.nums)};

    protected int getLen() {
        return 2;
    }

    protected void init() {
        setRemark("冠军、亚军各选一个号码，所选号码与开奖号码前2名车号相同，且顺序一致，即中奖");
        setExample("投注：01,02 开奖：01,02,*,*,*,*,*,*,*,* 即中奖");
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
        if (lines.size() != getLen()) {
            return Integer.valueOf(0);
        }
        List<List<String>> all = new ArrayList();
        for (String line : lines) {
            List<String> n = LotteryUtils.toListByLength(line, Integer.valueOf(2));
            if (ListUtils.hasSame(n)) {
                return Integer.valueOf(0);
            }
            if (n.retainAll(this.nums)) {
                return Integer.valueOf(0);
            }
            all.add(n);
        }
        int count = LotteryUtils.getCount(all);
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");

        List<String> lines = ListUtils.toList(bets);
        for (int i = 0; i < getLen(); i++) {
            String line = (String) lines.get(i);
            List<Integer> n = LotteryUtils.toIntListByLength(line, 2);
            if (!n.contains(openNums.get(i))) {
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

    protected Set<String> toAllBet(String bets) {
        List<List<Integer>> allNum = new ArrayList();
        List<String> lines = ListUtils.toList(bets);
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 2);
            allNum.add(n);
        }
        Set<String> bs = PermutateUtils.getAllNum(allNum, ",", false);
        return bs;
    }

    private String getKey(String n, int len) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < 10 - len; i++) {
            sb.append(",-");
        }
        return sb.toString();
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();

        Set<String> list = toAllBet(bets);
        for (String n : list) {
            result.put(getKey(n, getLen()), getBonus());
        }
        return result;
    }

    public String getBasicBet() {
        return basicBet;
    }

    public void setBasicBet(String basicBet) {
        this.basicBet = basicBet;
    }
}
