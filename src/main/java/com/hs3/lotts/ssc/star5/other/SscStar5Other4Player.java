package com.hs3.lotts.ssc.star5.other;

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

public class SscStar5Other4Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("4347.83").divide(new BigDecimal(2));
    private String qunName = "五星";
    private String groupName = "";
    private String title = "四季发财";
    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("从0-9中至少选择1个号码投注，竞猜开奖号码中包含这个号码且出现4次，即中奖");
        setExample("投注：8 开奖：x8888（不限顺序）");
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getQunName() {
        return this.qunName;
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
        List<Integer> line = ListUtils.toIntList(bets);
        if (ListUtils.hasSame(line)) {
            return Integer.valueOf(0);
        }
        if (line.retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(line.size());
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");

        Map<Integer, Integer> opens = new HashMap();
        Integer o;
        for (int i = 0; i < 5; i++) {
            Integer n = Integer.valueOf(1);
            o = (Integer) openNums.get(i);
            if (opens.containsKey(o)) {
                n = (Integer) opens.get(o);
                n = Integer.valueOf(n.intValue() + 1);
            }
            opens.put(o, n);
        }
        if (opens.size() > 2) {
            return win;
        }
        List<Integer> doubleNumList = new ArrayList();
        int n;
        for (Integer k : opens.keySet()) {
            n = ((Integer) opens.get(k)).intValue();
            if (n > 3) {
                doubleNumList.add(k);
            }
        }
        List<Integer> line = ListUtils.toIntList(bets);
        for (Integer winNum : doubleNumList) {
            if (line.contains(winNum)) {
                win = win.add(getBonus());
            }
        }
        return win;
    }

    private String getKey(int num, int index) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i == index) {
                sb.append("%s");
            } else {
                sb.append(num);
            }
        }
        return sb.toString();
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        List<Integer> line = ListUtils.toIntList(bets);
        Map<String, BigDecimal> result = new HashMap();


        /**jd-gui
         int i;
         for (Iterator localIterator = line.iterator(); localIterator.hasNext(); i < 5)
         {
         Integer n = (Integer)localIterator.next();
         i = 0; continue;
         String k = getKey(n.intValue(), i);
         for (int s = 0; s < 10; s++)
         {
         String key = String.format(k, new Object[] { Integer.valueOf(s) });
         result.put(key, getBonus());
         }
         i++;
         }*/

        for (Iterator iterator = line.iterator(); iterator.hasNext(); ) {
            Integer n = (Integer) iterator.next();
            for (int i = 0; i < 5; i++) {
                String k = getKey(n.intValue(), i);
                for (int s = 0; s < 10; s++) {
                    String key = String.format(k, new Object[]{
                            Integer.valueOf(s)
                    });
                    result.put(key, getBonus());
                }

            }

        }


        return result;
    }
}
