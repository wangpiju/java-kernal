package com.hs3.lotts.ssc.star3.any;

import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PermutateUtils;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SscStar3AnyGroup6Player
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("333.33").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "任三";
    private String title = "组六";
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums)};
    private List<String> titleList = new ArrayList(Arrays.asList(new String[]{"万", "千", "百", "十", "个"}));

    protected void init() {
        setRemark("从0-9中任意选择3个号码组成一注，所选号码与开奖号码的对应的三位相同，顺序不限");
        setExample("投注：123** 开奖：123**（不限顺序）");
    }

    public List<String> getAnyList() {
        return this.titleList;
    }

    public Integer getAnySelect() {
        return Integer.valueOf(3);
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

    public String getQunName() {
        return this.qunName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Integer getCount(String bets) {
        String[] betContent = bets.split("\\|");
        if (betContent.length != 2) {
            return Integer.valueOf(0);
        }
        List<Integer> indexs = LotteryUtils.getSscIndexs(betContent[0]);
        if (indexs.size() < 3) {
            return Integer.valueOf(0);
        }
        for (Iterator localIterator1 = indexs.iterator(); localIterator1.hasNext(); ) {
            int i = ((Integer) localIterator1.next()).intValue();
            if ((i < 0) || (i > 4)) {
                return Integer.valueOf(0);
            }
        }
        int nowIndex = -1;
        for (Iterator localIterator2 = indexs.iterator(); localIterator2.hasNext(); ) {
            int n = ((Integer) localIterator2.next()).intValue();
            if (n <= nowIndex) {
                return Integer.valueOf(0);
            }
            nowIndex = n;
        }
        Object line = ListUtils.toIntList(betContent[1]);
        if (ListUtils.hasSame((List) line)) {
            return Integer.valueOf(0);
        }
        if (((List) line).retainAll(this.nums)) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(LotteryUtils.getCombin(((List) line).size(), 3) * LotteryUtils.getCombin(indexs.size(), 3));
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        String[] betContent = bets.split("\\|");
        List<Integer> indexs = LotteryUtils.getSscIndexs(betContent[0]);
        List<Integer> line = ListUtils.toIntList(betContent[1]);
        for (int i = 0; i < indexs.size(); i++) {
            int o1 = ((Integer) openNums.get(((Integer) indexs.get(i)).intValue())).intValue();
            for (int j = i + 1; j < indexs.size(); j++) {
                int o2 = ((Integer) openNums.get(((Integer) indexs.get(j)).intValue())).intValue();
                for (int k = j + 1; k < indexs.size(); k++) {
                    int o3 = ((Integer) openNums.get(((Integer) indexs.get(k)).intValue())).intValue();

                    Set<Integer> opens = new HashSet();
                    opens.add(Integer.valueOf(o1));
                    opens.add(Integer.valueOf(o2));
                    opens.add(Integer.valueOf(o3));
                    if (opens.size() == 3) {
                        boolean isWin = true;
                        for (Integer o : opens) {
                            if (!line.contains(o)) {
                                isWin = false;
                                break;
                            }
                        }
                        if (isWin) {
                            win = win.add(getBonus());
                        }
                    }
                }
            }
        }
        return win;
    }

    private String getKey(List<Integer> ns) {
        String format = "";
        for (int i = 0; i < 5; i++) {
            if (ns.contains(Integer.valueOf(i))) {
                format = format + "%s";
            } else {
                format = format + "-";
            }
        }
        return format;
    }

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        Map<String, BigDecimal> result = new HashMap();

        String[] betContent = bets.split("\\|");
        List<Integer> indexs = LotteryUtils.getSscIndexs(betContent[0]);
        List<Integer> line = ListUtils.toIntList(betContent[1]);

        Set<String> ins = PermutateUtils.getCombinSelect(indexs, 3);
        Set<String> num = PermutateUtils.getCombinSelect(line, 3);

        /**jd-gui
         Iterator localIterator2;
         for (Iterator localIterator1 = num.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         String n = (String)localIterator1.next();
         String[] nList = n.split(",");
         Set<String> keys = PermutateUtils.getPerms(nList, ",");


         localIterator2 = keys.iterator(); continue;String key = (String)localIterator2.next();
         Object[] nnn = ListUtils.toList(key).toArray(new Object[0]);
         for (String i : ins)
         {
         String k = getKey(ListUtils.toIntList(i));
         String nn = String.format(k, nnn);
         result.put(nn, getBonus());
         }
         }*/

        for (Iterator iterator = num.iterator(); iterator.hasNext(); ) {
            String n = (String) iterator.next();
            String nList[] = n.split(",");
            Set<String> keys = PermutateUtils.getPerms(nList, ",");
            for (Iterator iterator1 = keys.iterator(); iterator1.hasNext(); ) {
                String key = (String) iterator1.next();
                Object nnn[] = ListUtils.toList(key).toArray(new Object[0]);
                String nn;
                for (Iterator iterator2 = ins.iterator(); iterator2.hasNext(); result.put(nn, getBonus())) {
                    String i = (String) iterator2.next();
                    String k = getKey(ListUtils.toIntList(i));
                    nn = String.format(k, nnn);
                }

            }

        }


        return result;
    }
}
