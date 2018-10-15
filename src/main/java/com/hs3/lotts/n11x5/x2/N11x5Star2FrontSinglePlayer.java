package com.hs3.lotts.n11x5.x2;

import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class N11x5Star2FrontSinglePlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("220").divide(new BigDecimal(2));
    private String qunName = "前二";
    private String groupName = "直选";
    private String title = "单式";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"}));

    protected void init() {
        setRemark("手动输入2个号码组成一注，所输入的号码与当期顺序摇出的5个号码中 的前2个号码相同，且顺序一致，即为中奖");
        setExample("投注：01,02 开奖：01,02,*,*,*");
    }

    protected int index() {
        return 0;
    }

    protected int numLen() {
        return 2;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getTitle() {
        return this.title;
    }

    public NumberView[] getNumView() {
        return null;
    }

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<String> n = LotteryUtils.toListByLength(line, Integer.valueOf(2));
            if (n.size() != numLen()) {
                return Integer.valueOf(0);
            }
            if (ListUtils.hasSame(n)) {
                return Integer.valueOf(0);
            }
            if (n.retainAll(this.nums)) {
                return Integer.valueOf(0);
            }
        }
        return Integer.valueOf(lines.size());
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<String> lines = ListUtils.toList(bets, "\\s+");
        BigDecimal win = new BigDecimal("0");
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 2);
            boolean isWin = true;
            for (int i = 0; i < numLen(); i++) {
                int o = ((Integer) openNums.get(i + index())).intValue();
                if (!((Integer) n.get(i)).equals(Integer.valueOf(o))) {
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
        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 2);
            String k = getKey(ListUtils.toString(n));
            addMap(result, k, getBonus());
        }
        return result;
    }
}
