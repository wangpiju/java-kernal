package com.hs3.lotts.n11x5.x3;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PermutateUtils;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.*;

public class N11x5Front3Nx1Player extends PlayerBase {

    private BigDecimal bonus = new BigDecimal("7.33").divide(new BigDecimal(2));
    private String qunName = "选一";
    //private String groupName = "前三";
    private String groupName = "不定位";
    private String title = "不定位";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"}));

    private NumberView[] view = {new NumberView("", this.nums)};

    public static void main(String[] args) {
        N11x5Front3Nx1Player N11x5Front3Nx1Player = new N11x5Front3Nx1Player();
        System.out.println(N11x5Front3Nx1Player.getBonus());
    }

    protected int index() {
        return 0;
    }

    protected void init() {
        setRemark("从01-11共11个号码中选择1个号码，每注由1个号码组成，只要当期顺序摇出的第一位、第二位、第三位开奖号码中包含所选号码，即为中奖");
        setExample("投注：01(第一位) 开奖：01,X,Y,*,*");
    }

    public String getQunName() {
        return "";
    }

    public String getGroupName() {
        return "前三";
    }

    public String getTitle() {
        return "不定位";
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public NumberView[] getNumView() {
        return this.view;
    }

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets);
        if (ListUtils.hasSame(lines)) {
            return 0;
        }
        if (lines.retainAll(this.nums)) {
            return 0;
        }
        return lines.size();
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        Set<Integer> opens = new HashSet<>();
        opens.add(openNums.get(index()));
        opens.add(openNums.get(index() + 1));
        opens.add(openNums.get(index() + 2));

        List<Integer> lines = ListUtils.toIntList(bets);

        BigDecimal win = new BigDecimal("0");
        for (Integer n : lines) {
            if (opens.contains(n)) {
                win = win.add(getBonus());
            }
        }
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
        Map<String, BigDecimal> result = new HashMap<>();
        List<Integer> lines = ListUtils.toIntList(bets);

        for (Integer line : lines) {
            int n;
            n = (line);
            for (int i = 1; i <= 11; i++)
                if (i != n) {
                    for (int j = 1; j <= 11; j++)
                        if (j != n && j != i) {
                            List<Integer> num = new ArrayList<>();
                            num.add(n);
                            num.add(i);
                            num.add(j);
                            Set<String> nums = PermutateUtils.getPerms(num.toArray(new Object[3]), ",");
                            String nnn;
                            for (Iterator iterator1 = nums.iterator(); iterator1.hasNext(); addMap(result, getKey(nnn),
                                    getBonus()))
                                nnn = (String) iterator1.next();

                        }

                }

        }

        return result;
    }
}
