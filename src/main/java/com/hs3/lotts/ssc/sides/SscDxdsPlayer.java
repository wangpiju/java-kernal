package com.hs3.lotts.ssc.sides;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SscDxdsPlayer
        extends PlayerBase {
    private BigDecimal bonus2 = new BigDecimal("10").divide(new BigDecimal(2));
    private BigDecimal bonus = new BigDecimal("4").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "大小单双";
    private List<String> nums = new ArrayList(Arrays.asList(new String[]{"大", "小", "单", "双", "和", "大单", "小单", "大双", "小双"}));
    private NumberView[] view = {
            new NumberView("万", this.nums, false),
            new NumberView("千", this.nums, false),
            new NumberView("百", this.nums, false),
            new NumberView("十", this.nums, false),
            new NumberView("个", this.nums, false)};

    protected void init() {
        setRemark("从万位、千位、百位、十位、个位任意位置上至少选择1个形态，所选形态与相同位置上的开奖号码一致");
        setExample("投注：和**** 开奖：0****或5**** 即中奖");
    }

    public String getBonusStr() {
        return this.bonus + " - " + this.bonus2;
    }

    public BigDecimal getBonus() {
        return this.bonus2;
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
                List<String> n = ListUtils.toList(line, "\\+");
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
                int openNum = ((Integer) openNums.get(i)).intValue();
                boolean isOdd = openNum % 2 != 0;
                boolean isHe = (openNum == 0) || (openNum == 5);
                boolean isBig = openNum >= 5;
                boolean isSmall = openNum < 5;

                List<String> nums = ListUtils.toList(line, "\\+");
                for (String num : nums) {
                    if ((num.equals("大")) && (isBig)) {
                        win = win.add(this.bonus);
                    } else if ((num.equals("小")) && (isSmall)) {
                        win = win.add(this.bonus);
                    } else if ((num.equals("单")) && (isOdd)) {
                        win = win.add(this.bonus);
                    } else if ((num.equals("双")) && (!isOdd)) {
                        win = win.add(this.bonus);
                    } else if ((num.equals("大单")) && (isOdd) && (isBig) && (!isHe)) {
                        win = win.add(this.bonus2);
                    } else if ((num.equals("小单")) && (isOdd) && (isSmall) && (!isHe)) {
                        win = win.add(this.bonus2);
                    } else if ((num.equals("大双")) && (!isOdd) && (isBig) && (!isHe)) {
                        win = win.add(this.bonus2);
                    } else if ((num.equals("小双")) && (!isOdd) && (isSmall) && (!isHe)) {
                        win = win.add(this.bonus2);
                    } else if ((num.equals("和")) && (isHe)) {
                        win = win.add(this.bonus2);
                    }
                }
            }
        }
        return win;
    }

    private String getKey(int index, int num) {
        String k = "";
        for (int i = 0; i < 5; i++) {
            if (i == index) {
                k = k + num;
            } else {
                k = k + "-";
            }
        }
        return k;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();

        List<String> lines = ListUtils.toList(bets);
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            if (!line.equals("-")) {
                List<String> nums = ListUtils.toList(line, "\\+");
                for (String num : nums) {
                    if (num.equals("大")) {
                        result.put(getKey(i, 5), this.bonus);
                        result.put(getKey(i, 6), this.bonus);
                        result.put(getKey(i, 7), this.bonus);
                        result.put(getKey(i, 8), this.bonus);
                        result.put(getKey(i, 9), this.bonus);
                    } else if (num.equals("小")) {
                        result.put(getKey(i, 0), this.bonus);
                        result.put(getKey(i, 1), this.bonus);
                        result.put(getKey(i, 2), this.bonus);
                        result.put(getKey(i, 3), this.bonus);
                        result.put(getKey(i, 4), this.bonus);
                    } else if (num.equals("单")) {
                        result.put(getKey(i, 1), this.bonus);
                        result.put(getKey(i, 3), this.bonus);
                        result.put(getKey(i, 5), this.bonus);
                        result.put(getKey(i, 7), this.bonus);
                        result.put(getKey(i, 9), this.bonus);
                    } else if (num.equals("双")) {
                        result.put(getKey(i, 0), this.bonus);
                        result.put(getKey(i, 2), this.bonus);
                        result.put(getKey(i, 4), this.bonus);
                        result.put(getKey(i, 6), this.bonus);
                        result.put(getKey(i, 8), this.bonus);
                    } else if (num.equals("大单")) {
                        result.put(getKey(i, 7), this.bonus2);
                        result.put(getKey(i, 9), this.bonus2);
                    } else if (num.equals("小单")) {
                        result.put(getKey(i, 1), this.bonus2);
                        result.put(getKey(i, 3), this.bonus2);
                    } else if (num.equals("大双")) {
                        result.put(getKey(i, 6), this.bonus2);
                        result.put(getKey(i, 8), this.bonus2);
                    } else if (num.equals("小双")) {
                        result.put(getKey(i, 2), this.bonus2);
                        result.put(getKey(i, 4), this.bonus2);
                    } else if (num.equals("和")) {
                        result.put(getKey(i, 0), this.bonus2);
                        result.put(getKey(i, 5), this.bonus2);
                    }
                }
            }
        }
        return result;
    }
}
