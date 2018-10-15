package com.hs3.lotts.n11x5.nx;

import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PermutateUtils;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class N11x5Player extends PlayerBase {
    private static final List<Integer> INDEXS = Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1),
            Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)});
    private String groupName = "任选";
    private String title = "复式";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"}));
    private NumberView[] view = {new NumberView("", this.nums)};

    protected abstract int getSelectNum();

    protected abstract int getWinNum();

    protected List<Integer> getIndexs() {
        return INDEXS;
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
        List<String> line = ListUtils.toList(bets);
        if (ListUtils.hasSame(line)) {
            return Integer.valueOf(0);
        }
        if (line.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(LotteryUtils.getCombin(line.size(), getSelectNum()));
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<Integer> line = ListUtils.toIntList(bets);
        BigDecimal win = new BigDecimal("0");

        int needWinNum = getWinNum();

        int winNum = 0;
        for (Iterator localIterator = line.iterator(); localIterator.hasNext(); ) {
            int o = ((Integer) localIterator.next()).intValue();
            if (openNums.contains(Integer.valueOf(o))) {
                winNum++;
            }
        }
        if (winNum >= needWinNum) {
            int select = line.size();
            int needSelect = getSelectNum();
            Integer s = null;
            if (needSelect == needWinNum) {
                s = Integer.valueOf(LotteryUtils.getCombin(winNum, needWinNum));
            } else {
                s = Integer.valueOf(LotteryUtils.getCombin(select - winNum, needSelect - winNum));
            }
            win = getBonus().multiply(new BigDecimal(s.toString()));
        }
        return win;
    }

    private String getKey(List<Integer> ns) {
        String format = "";
        for (int i = 0; i < 5; i++) {
            if (ns.contains(Integer.valueOf(i))) {
                format = format + ",%s";
            } else {
                format = format + ",-";
            }
        }
        return format.substring(1);
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();

        List<Integer> line = ListUtils.toIntList(bets);

        Set<String> indexs = PermutateUtils.getCombinSelect(getIndexs(), getSelectNum());
        Set<String> nums = PermutateUtils.getCombinSelect(line, getSelectNum());

        /**
         * jd-gui Iterator localIterator2; for (Iterator localIterator1 =
         * nums.iterator(); localIterator1.hasNext(); localIterator2.hasNext()) { String
         * n = (String)localIterator1.next(); Object[] nList = n.split(",");
         *
         *
         * Set<String> all = PermutateUtils.getPerms(nList, ",");
         *
         * localIterator2 = all.iterator(); continue;String a =
         * (String)localIterator2.next(); Object[] nnn = ListUtils.toList(a).toArray(new
         * Object[0]); for (String i : indexs) { String k =
         * getKey(ListUtils.toIntList(i)); String key = String.format(k, nnn);
         * result.put(key, getBonus()); } }
         */

        for (Iterator iterator = nums.iterator(); iterator.hasNext(); ) {
            String n = (String) iterator.next();
            Object nList[] = n.split(",");
            Set<String> all = PermutateUtils.getPerms(nList, ",");
            for (Iterator iterator1 = all.iterator(); iterator1.hasNext(); ) {
                String a = (String) iterator1.next();
                Object nnn[] = ListUtils.toList(a).toArray(new Object[0]);
                String key;
                for (Iterator iterator2 = indexs.iterator(); iterator2.hasNext(); result.put(key, getBonus())) {
                    String i = (String) iterator2.next();
                    String k = getKey(ListUtils.toIntList(i));
                    key = String.format(k, nnn);
                }

            }

        }

        return result;
    }
}
