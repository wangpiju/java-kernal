package com.hs3.lotts.k3.star2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

public class K3Star2SamePlayer extends PlayerBase {
    private BigDecimal bonus;
    private String qunName;
    private String groupName;
    private String title;
    private List<String> nums;
    private List<String> nums1;
    private List<String> nums2;
    private List<String> nums3;
    private List<String> nums4;
    private List<String> nums5;
    private List<String> nums6;
    private NumberView view[];

    public K3Star2SamePlayer() {
        bonus = new BigDecimal("144").divide(new BigDecimal(2));
        qunName = "";
        groupName = "";
        title = "二同号";
        nums = new ArrayList<>(Arrays.asList("112", "113", "114", "115", "116", "221", "223",
                "224", "225", "226", "331", "332", "334", "335", "336", "441", "442", "443", "445", "446", "551", "552",
                "553", "554", "556", "661", "662", "663", "664", "665"));
        nums1 = new ArrayList<>(Arrays.asList("112", "113", "114", "115", "116"));
        nums2 = new ArrayList<>(Arrays.asList("221", "223", "224", "225", "226"));
        nums3 = new ArrayList<>(Arrays.asList("331", "332", "334", "335", "336"));
        nums4 = new ArrayList<>(Arrays.asList("441", "442", "443", "445", "446"));
        nums5 = new ArrayList<>(Arrays.asList("551", "552", "553", "554", "556"));
        nums6 = new ArrayList<>(Arrays.asList("661", "662", "663", "664", "665"));
        view = (new NumberView[]{new NumberView("", nums1, false), new NumberView("", nums2, false),
                new NumberView("", nums3, false), new NumberView("", nums4, false), new NumberView("", nums5, false),
                new NumberView("", nums6, false)});
    }

    protected void init() {
        setRemark("选择1对相同号码和1个不同号码进行单选或者多选投注，选号与开奖号相同（顺序不限）即中奖");
        setExample("投注：122 开奖：221 即中奖");
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
        if (ListUtils.hasSame(lines)) {
            return 0;
        }
        if (lines.retainAll(this.nums)) {
            return 0;
        }
        return lines.size();
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<String> lines = ListUtils.toList(bets);
        BigDecimal win = new BigDecimal("0");

        Map<Integer, Integer> os = new HashMap<Integer, Integer>();
        for (Integer o : openNums) {
            if (os.containsKey(o)) {
                os.put(o,  os.get(o) + 1);
            } else {
                os.put(o, 1);
            }
        }
        for (String line : lines) {
            List<Integer> ns = LotteryUtils.toIntListByLength(line, 1);
            int two = ns.get(0);
            int one = ns.get(2);
            if ((os.containsKey(one)) && (os.containsKey(two))
                    &&  os.get(two) == 2) {
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
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        List<String> lines = ListUtils.toList(bets);
        int n1;
        int n2;
        int n3;
        for (Iterator<String> iterator = lines.iterator(); iterator.hasNext(); result
                .put((new StringBuilder(String.valueOf(n3))).append(n2).append(n1).toString(), getBonus())) {
            String line = (String) iterator.next();
            List<Integer> ns = LotteryUtils.toIntListByLength(line, 1);
            n1 = ns.get(0);
            n2 = ns.get(1);
            n3 = ns.get(2);

            result.put((new StringBuilder(String.valueOf(n1))).append(n2).append(n3).toString(), getBonus());
            result.put((new StringBuilder(String.valueOf(n1))).append(n3).append(n2).toString(), getBonus());
            result.put((new StringBuilder(String.valueOf(n2))).append(n1).append(n3).toString(), getBonus());
            result.put((new StringBuilder(String.valueOf(n2))).append(n3).append(n1).toString(), getBonus());
            result.put((new StringBuilder(String.valueOf(n3))).append(n1).append(n2).toString(), getBonus());

        }

        return result;

    }
}
