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

public class Pk10SideDsPlayer extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("4").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "";
    private String title = "大小";
    private String basicBet = "大,-,-,-,-,-,-,-,-,-";
    private List<String> nums = new ArrayList(Arrays.asList(new String[]{"大", "小", "单", "双"}));
    private NumberView[] view = {new NumberView("第一名", this.nums, false, 0, true),
            new NumberView("第二名", this.nums, false, 0, true), new NumberView("第三名", this.nums, false, 0, true),
            new NumberView("第四名", this.nums, false, 0, true), new NumberView("第五名", this.nums, false, 0, true),
            new NumberView("第六名", this.nums, false, 0, true), new NumberView("第七名", this.nums, false, 0, true),
            new NumberView("第八名", this.nums, false, 0, true), new NumberView("第九名", this.nums, false, 0, true),
            new NumberView("第十名", this.nums, false, 0, true)};

    protected void init() {
        setRemark("从对应位置上选择一个形态组成一注，1-5为“小”，6-10为“大” 1,3,5,7,9为“单”，2,4,6,8,10 为“双”");
        setExample("第一名位置投注：“大” 开奖：6**** 即中奖");
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
        if (lines.size() != 10) {
            return Integer.valueOf(0);
        }
        int count = 0;
        for (String line : lines) {
            List<String> n = LotteryUtils.toListByLength(line, Integer.valueOf(1));
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

        int index = 0;
        for (String line : lines) {
            int o = ((Integer) openNums.get(index)).intValue();
            boolean isSingle = o % 2 != 0;
            if ((o > 5) && (line.contains("大"))) {
                win = win.add(this.bonus);
            } else if ((o <= 5) && (line.contains("小"))) {
                win = win.add(this.bonus);
            }
            if ((isSingle) && (line.contains("单"))) {
                win = win.add(this.bonus);
            } else if ((!isSingle) && (line.contains("双"))) {
                win = win.add(this.bonus);
            }
            index++;
        }
        return win;
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
                if (line.contains("大")) {
                    for (int n = 6; n <= 10; n++) {
                        result.put(getKey(i, n), this.bonus);
                    }
                }
                if (line.contains("小")) {
                    for (int n = 1; n <= 5; n++) {
                        result.put(getKey(i, n), this.bonus);
                    }
                }
                if (line.contains("单")) {
                    for (int n = 1; n <= 10; n += 2) {
                        result.put(getKey(i, n), this.bonus);
                    }
                }
                if (line.contains("双")) {
                    for (int n = 2; n <= 10; n += 2) {
                        result.put(getKey(i, n), this.bonus);
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
}
