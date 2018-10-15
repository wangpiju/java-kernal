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

public abstract class N11x5SinglePlayer extends PlayerBase {
    private static final List<Integer> INDEXS = Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1),
            Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)});
    private String groupName = "任选";
    private String title = "单式";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"}));

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
        return null;
    }

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets, "\\s+");
        for (String line : lines) {
            List<String> n = LotteryUtils.toListByLength(line, Integer.valueOf(2));
            if (n.size() != getSelectNum()) {
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
            int winNum = 0;
            for (int i = 0; i < n.size(); i++) {
                int num = ((Integer) n.get(i)).intValue();
                if (openNums.contains(Integer.valueOf(num))) {
                    winNum++;
                }
            }
            if (winNum == getWinNum()) {
                win = win.add(getBonus());
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

        List<String> lines = ListUtils.toList(bets, "\\s+");
        List<List<Integer>> allBuy = new ArrayList();
        for (String line : lines) {
            List<Integer> n = LotteryUtils.toIntListByLength(line, 2);
            allBuy.add(n);
        }
        int allNum = getSelectNum();

        /**
         * jd-gui Object indexs = PermutateUtils.getCombinSelect(getIndexs(), allNum);
         * Iterator localIterator3; for (Iterator localIterator2 = allBuy.iterator();
         * localIterator2.hasNext(); localIterator3.hasNext()) { List<Integer> num =
         * (List)localIterator2.next();
         *
         * Set<String> all = PermutateUtils.getPerms(num.toArray(new Object[allNum]),
         * ",");
         *
         * localIterator3 = all.iterator(); continue;String a =
         * (String)localIterator3.next(); Object[] nnn = ListUtils.toList(a).toArray(new
         * Object[allNum]); for (String i : (Set)indexs) { String k =
         * getKey(ListUtils.toIntList(i)); String key = String.format(k, nnn);
         * addMap(result, key, getBonus()); } }
         */

        Set indexs = PermutateUtils.getCombinSelect(getIndexs(), allNum);
        for (Iterator iterator1 = allBuy.iterator(); iterator1.hasNext(); ) {
            List<Integer> num = (List) iterator1.next();
            Set<String> all = PermutateUtils.getPerms(num.toArray(new Object[allNum]), ",");
            for (Iterator iterator2 = all.iterator(); iterator2.hasNext(); ) {
                String a = (String) iterator2.next();
                Object nnn[] = ListUtils.toList(a).toArray(new Object[allNum]);
                String key;
                for (Iterator iterator3 = indexs.iterator(); iterator3.hasNext(); addMap(result, key, getBonus())) {
                    String i = (String) iterator3.next();
                    String k = getKey(ListUtils.toIntList(i));
                    key = String.format(k, nnn);
                }

            }

        }

        return result;
    }
}
