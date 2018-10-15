package com.hs3.lotts.pk10.sides;

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

public class Pk10SideLhPlayer extends PlayerBase {
    private List<String> titles = new ArrayList(
            Arrays.asList(new String[]{"[1V10]", "[2V9]", "[3V8]", "[4V7]", "[5V6]"}));
    private BigDecimal bonus = new BigDecimal("4").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "龙虎";
    private String basicBet = "[1V10]龙,-,-,-,-";
    private List<String> nums = new ArrayList(Arrays.asList(new String[]{"龙", "虎"}));
    private NumberView[] view = {new NumberView("1V10", this.nums, false), new NumberView("2V9", this.nums, false),
            new NumberView("3V8", this.nums, false), new NumberView("4V7", this.nums, false),
            new NumberView("5V6", this.nums, false)};

    protected void init() {
        setRemark("从对应两个位上选择一个形态组成一注，前者大于后者为“龙”，反之为“虎”");
        setExample("1V10投注：“龙” 开奖：2,*,*,*,*,*,*,*,*,1 即中奖");
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getTitle() {
        return this.title;
    }

    public String getQunName() {
        return this.qunName;
    }

    public String getGroupName() {
        return this.groupName;
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
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            if (!line.equals("-")) {
                String t = (String) this.titles.get(i);
                if (!line.startsWith(t)) {
                    return Integer.valueOf(0);
                }
                line = line.substring(t.length());

                List<String> n = LotteryUtils.toListByLength(line, Integer.valueOf(1));
                if (ListUtils.hasSame(n)) {
                    return Integer.valueOf(0);
                }
                if (n.retainAll(this.nums)) {
                    return Integer.valueOf(0);
                }
                count += n.size();
            }
        }
        return Integer.valueOf(count);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<String> lines = ListUtils.toList(bets);
        BigDecimal win = new BigDecimal("0");
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            if (!line.equals("-")) {
                String t = (String) this.titles.get(i);
                if (!line.startsWith(t)) {
                    return BigDecimal.ZERO;
                }
                line = line.substring(t.length());

                int o1 = ((Integer) openNums.get(i)).intValue();
                int o2 = ((Integer) openNums.get(9 - i)).intValue();
                int o = o1 - o2;
                if ((o > 0) && (line.contains("龙"))) {
                    win = win.add(this.bonus);
                } else if ((o < 0) && (line.contains("虎"))) {
                    win = win.add(this.bonus);
                }
            }
        }
        return win;
    }

    private String getKey(int index, int n1, int n2) {
        StringBuilder sb = new StringBuilder();
        int i1 = index;
        int i2 = 9 - index;
        for (int i = 0; i < 10; i++) {
            if (i == i1) {
                sb.append(",").append(n1);
            } else if (i == i2) {
                sb.append(",").append(n2);
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
                String t = (String) this.titles.get(i);
                line = line.substring(t.length());
                for (int n1 = 1; n1 <= 10; n1++) {
                    for (int n2 = 1; n2 <= 10; n2++) {
                        if (n1 != n2) {
                            int n = n1 - n2;
                            if ((n > 0) && (line.contains("龙"))) {
                                result.put(getKey(i, n1, n2), this.bonus);
                            } else if ((n < 0) && (line.contains("虎"))) {
                                result.put(getKey(i, n1, n2), this.bonus);
                            }
                        }
                    }
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
