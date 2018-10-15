package com.hs3.lotts.pk10.star1;

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

public class Pk10Star1DwdLastPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("20").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "后五定位胆";
    private String basicBet = "01,-,-,-,-";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"}));
    private NumberView[] view = {new NumberView("六名", this.nums), new NumberView("七名", this.nums),
            new NumberView("八名", this.nums), new NumberView("九名", this.nums), new NumberView("十名", this.nums)};

    protected void init() {
        setRemark("从任意位置上至少选择1个号码，选号与相同位置上的开奖号码一致");
        setExample("投注：X,X,X,X,X,01,*,*,*,* 开奖：X,X,X,X,X,01,*,*,*,* 即中奖");
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
        int count = 0;
        for (String line : lines) {
            List<String> n = LotteryUtils.toListByLength(line, Integer.valueOf(2));
            if (ListUtils.hasSame(n)) {
                return Integer.valueOf(0);
            }
            if (n.retainAll(this.nums)) {
                return Integer.valueOf(0);
            }
            count += n.size();
        }
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<String> lines = ListUtils.toList(bets);
        BigDecimal win = new BigDecimal("0");
        for (int i = 0; i < 5; i++) {
            int o = ((Integer) openNums.get(i + 5)).intValue();
            String line = (String) lines.get(i);
            List<Integer> n = LotteryUtils.toIntListByLength(line, 2);
            if (n.contains(Integer.valueOf(o))) {
                win = win.add(this.bonus);
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

    private String getKey(int index, int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            if (i == index) {
                sb.append(",").append(num);
            } else {
                sb.append(",-");
            }
        }
        return sb.substring(1).toString();
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();

        List<String> lines = ListUtils.toList(bets);
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            if (!line.equals("-")) {
                List<Integer> list = LotteryUtils.toIntListByLength(line, 2);
                for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) {
                    int n = ((Integer) localIterator.next()).intValue();
                    result.put(getKey(i + 5, n), this.bonus);
                }
            }
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
