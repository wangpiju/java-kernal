package com.hs3.lotts.pk10.star3;

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

public class Pk10Star3SinglePlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("1440").divide(new BigDecimal(2));
    private String qunName = "前三";
    private String groupName = "";
    private String title = "直选单式";
    private String basicBet = "01,02,03";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"}));

    protected void init() {
        setRemark("冠军、亚军、第三名各选一个号码，所选号码与开奖号码前3名车号相同，且顺序一致，即中奖");
        setExample("投注：01,02,03 开奖：01,02,03,*,*,*,*,*,*,* 即中奖");
    }

    public int getLen() {
        return 3;
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
            if (n.size() != getLen()) {
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
            for (int i = 0; i < getLen(); i++) {
                if (!((Integer) n.get(i)).equals(openNums.get(i))) {
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
        Map<String, BigDecimal> result = new HashMap();
        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<Integer> nums = LotteryUtils.toIntListByLength(line, 2);
            String n = ListUtils.toString(nums);
            addMap(result, n, getBonus());
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
