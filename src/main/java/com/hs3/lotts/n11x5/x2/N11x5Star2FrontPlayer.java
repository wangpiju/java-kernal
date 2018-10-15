package com.hs3.lotts.n11x5.x2;

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

public class N11x5Star2FrontPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("220").divide(new BigDecimal(2));
    private String qunName = "前二";
    private String groupName = "直选";
    private String title = "复式";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"}));
    private NumberView[] view = {new NumberView("第1位", this.nums), new NumberView("第2位", this.nums)};

    protected int index() {
        return 0;
    }

    protected int numLen() {
        return 2;
    }

    protected void init() {
        setRemark("从01-11共11个号码中选择2个不重复的号码组成一注，所选号码与当期顺序摇出的5个号码中 的前2个号码相同，且顺序一致，即为中奖");
        setExample("投注：01,02 开奖：01,02,*,*,*");
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
        List<String> lines = ListUtils.toList(bets);
        if (lines.size() != 5) {
            return Integer.valueOf(0);
        }
        List<List<String>> allList = new ArrayList();
        for (int i = 0; i < 5; i++) {
            String line = (String) lines.get(i);
            if ((i >= index()) && (i < numLen() + index())) {
                List<String> n = LotteryUtils.toListByLength(line, Integer.valueOf(2));
                if (ListUtils.hasSame(n)) {
                    return Integer.valueOf(0);
                }
                if (n.retainAll(this.nums)) {
                    return Integer.valueOf(0);
                }
                allList.add(n);
            } else if (!line.equals("-")) {
                return Integer.valueOf(0);
            }
        }
        return Integer.valueOf(LotteryUtils.getCount(allList));
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<String> lines = ListUtils.toList(bets);
        BigDecimal win = new BigDecimal("0");
        for (int i = 0; i < numLen(); i++) {
            List<Integer> line = LotteryUtils.toIntListByLength((String) lines.get(i + index()), 2);
            int o = ((Integer) openNums.get(i + index())).intValue();
            if (!line.contains(Integer.valueOf(o))) {
                return win;
            }
        }
        win = win.add(getBonus());
        return win;
    }

    private String getKey(String n) {
        List<String> list = ListUtils.toList(n);
        int len = 5 - list.size();
        for (int i = 0; i < len; i++) {
            if (index() > i) {
                list.add(i, "-");
            } else {
                list.add("-");
            }
        }
        return ListUtils.toString(list);
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();
        List<String> lines = ListUtils.toList(bets);

        List<List<Integer>> allBuy = new ArrayList();
        List<Integer> n;
        for (String line : lines) {
            if (!line.equals("-")) {
                n = LotteryUtils.toIntListByLength(line, 2);
                allBuy.add(n);
            }
        }
        Set<String> all = PermutateUtils.getAllNum(allBuy, ",", false);
        for (String a : all) {
            String k = getKey(a);
            result.put(k, getBonus());
        }
        return result;
    }
}
