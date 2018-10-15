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

public abstract class N11x5DtPlayer extends PlayerBase {
    private static final List<Integer> INDEXS = Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1),
            Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)});
    private String groupName = "任选";
    private String title = "胆拖";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"}));
    private NumberView[] view = {new NumberView("胆码", this.nums, false, getSelectNum() - 1),
            new NumberView("拖码", this.nums, true)};

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
        List<String> line = ListUtils.toList(bets, ";");
        if (line.size() != 2) {
            return Integer.valueOf(0);
        }
        List<String> dan = ListUtils.toList(((String) line.get(0)).substring(1));
        if (ListUtils.hasSame(dan)) {
            return Integer.valueOf(0);
        }
        if (dan.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        List<String> tuo = ListUtils.toList((String) line.get(1));
        if (ListUtils.hasSame(tuo)) {
            return Integer.valueOf(0);
        }
        if (tuo.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        int allSize = 0;
        for (String d : dan) {
            int s = tuo.size();
            if (tuo.contains(d)) {
                s--;
            }
            int ls = LotteryUtils.getCombin(s, getSelectNum() - 1);
            if (ls == 0) {
                return Integer.valueOf(0);
            }
            allSize += ls;
        }
        return Integer.valueOf(allSize);
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");

        List<String> lines = ListUtils.toList(bets, ";");
        List<Integer> dan = ListUtils.toIntList(((String) lines.get(0)).substring(1));
        List<Integer> tuo = ListUtils.toIntList((String) lines.get(1));
        for (Integer d : dan) {
            int winNum = 0;
            if (openNums.contains(d)) {
                for (Integer n : tuo) {
                    if ((n != d) && (openNums.contains(n))) {
                        winNum++;
                    }
                }
            }
            int needWinNum = getWinNum() - 1;
            if (winNum >= needWinNum) {
                int needSelect = getSelectNum() - 1;
                Integer s = null;
                if (needSelect == needWinNum) {
                    s = Integer.valueOf(LotteryUtils.getCombin(winNum, needWinNum));
                } else {
                    int select = tuo.size();
                    if (tuo.contains(d)) {
                        select--;
                    }
                    s = Integer.valueOf(LotteryUtils.getCombin(select - winNum, needSelect - winNum));
                }
                win = win.add(getBonus().multiply(new BigDecimal(s.toString())));
            }
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

        List<String> lines = ListUtils.toList(bets, ";");
        List<Integer> dans = ListUtils.toIntList(((String) lines.get(0)).substring(1));
        List<Integer> tuo = ListUtils.toIntList((String) lines.get(1));

        Set<String> indexs = PermutateUtils.getCombinSelect(getIndexs(), getSelectNum());
        Set<String> nums = PermutateUtils.getCombinSelect(tuo, getSelectNum() - 1);

        /**
         * jd-gui Iterator localIterator2; for (Iterator localIterator1 =
         * dans.iterator(); localIterator1.hasNext(); localIterator2.hasNext()) {
         * Integer dan = (Integer)localIterator1.next(); localIterator2 =
         * nums.iterator(); continue;String n = (String)localIterator2.next();
         * List<Integer> nList = ListUtils.toIntList(n); if (!nList.contains(dan)) {
         * nList.add(dan); } Set<String> all = PermutateUtils.getPerms(nList.toArray(),
         * ","); Iterator localIterator4; for (Iterator localIterator3 = all.iterator();
         * localIterator3.hasNext(); localIterator4.hasNext()) { String a =
         * (String)localIterator3.next(); Object[] nnn = ListUtils.toList(a).toArray(new
         * Object[0]); localIterator4 = indexs.iterator(); continue;String i =
         * (String)localIterator4.next(); String k = getKey(ListUtils.toIntList(i));
         * String key = String.format(k, nnn); addMap(result, key, getBonus()); } }
         */

        for (Iterator iterator = dans.iterator(); iterator.hasNext(); ) {
            Integer dan = (Integer) iterator.next();
            for (Iterator iterator1 = nums.iterator(); iterator1.hasNext(); ) {
                String n = (String) iterator1.next();
                List<Integer> nList = ListUtils.toIntList(n);
                if (!nList.contains(dan))
                    nList.add(dan);
                Set<String> all = PermutateUtils.getPerms(nList.toArray(), ",");
                for (Iterator iterator2 = all.iterator(); iterator2.hasNext(); ) {
                    String a = (String) iterator2.next();
                    Object nnn[] = ListUtils.toList(a).toArray(new Object[0]);
                    String key;
                    for (Iterator iterator3 = indexs.iterator(); iterator3.hasNext(); addMap(result, key, getBonus())) {
                        String i = (String) iterator3.next();
                        String k = getKey(ListUtils.toIntList(i));
                        key = String.format(k, nnn);
                    }

                }

            }

        }

        return result;
    }
}
