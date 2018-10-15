package com.hs3.lotts.pk10.sides;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pk10SideGyAndPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("4.5").divide(new BigDecimal(2));
    private BigDecimal bonus2 = new BigDecimal("3.6").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "冠亚和大小单双";
    private String basicBet = "大";
    private List<String> nums = new ArrayList(Arrays.asList(new String[]{"大", "小", "单", "双"}));
    private NumberView[] view = {new NumberView("", this.nums, false)};

    protected void init() {
        setRemark("3-11为小，12-19为大。投注形态与冠亚军2个数字的和符合，即为中奖。");
        setExample("投注：“大” 开奖：2,10,*,*,*,*,*,*,*,* 即中奖");
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getBonusStr() {
        return this.bonus2 + " - " + this.bonus;
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
        List<String> n = ListUtils.toList(bets);
        if (ListUtils.hasSame(n)) {
            return Integer.valueOf(0);
        }
        if (n.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(n.size());
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<String> lines = ListUtils.toList(bets);
        BigDecimal win = new BigDecimal("0");

        int o = ((Integer) openNums.get(0)).intValue() + ((Integer) openNums.get(1)).intValue();
        boolean isBig = o > 11;
        boolean isSingle = o % 2 != 0;
        for (String line : lines) {
            if ((isBig) && (line.contains("大"))) {
                win = win.add(this.bonus);
            } else if ((!isBig) && (line.contains("小"))) {
                win = win.add(this.bonus2);
            }
            if ((isSingle) && (line.contains("单"))) {
                win = win.add(this.bonus2);
            } else if ((!isSingle) && (line.contains("双"))) {
                win = win.add(this.bonus);
            }
        }
        return win;
    }

    private String getKey(int n1, int n2) {
        StringBuilder sb = new StringBuilder();
        sb.append(n1).append(",").append(n2).append(",-,-,-,-,-,-,-,-");
        return sb.toString();
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();

        List<String> lines = ListUtils.toList(bets);
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            if (!line.equals("-")) {
                for (int n1 = 1; n1 <= 10; n1++) {
                    for (int n2 = 1; n2 <= 10; n2++) {
                        if (n1 != n2) {
                            int n = n1 + n2;
                            boolean isBig = n > 11;
                            boolean isSingle = n % 2 != 0;
                            if ((isBig) && (line.contains("大"))) {
                                result.put(getKey(n1, n2), this.bonus);
                            } else if ((!isBig) && (line.contains("小"))) {
                                result.put(getKey(n1, n2), this.bonus);
                            }
                            if ((isSingle) && (line.contains("单"))) {
                                result.put(getKey(n1, n2), this.bonus);
                            } else if ((!isSingle) && (line.contains("双"))) {
                                result.put(getKey(n1, n2), this.bonus);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String getBasicBet() {
        return basicBet;
    }

    public void setBasicBet(String basicBet) {
        this.basicBet = basicBet;
    }
}
