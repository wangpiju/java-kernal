package com.hs3.lotts.ssc.star2.any;

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

public class SscStar2AnyGroupPlayer
        extends PlayerBase {
    private BigDecimal bonus = new BigDecimal("100").divide(new BigDecimal(2));
    private String qunName = "";
    private String groupName = "任二";
    private String title = "组选复式";
    private List<String> titleList = new ArrayList(Arrays.asList(new String[]{"万", "千", "百", "十", "个"}));
    private List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {new NumberView("", this.nums)};

    protected void init() {
        setRemark("从0-9中选择2个数字组成一注，所选号码与开奖号码的对应的二位相同，顺序不限");
        setExample("投注：5,8 开奖：58***(不限顺序) 即中奖");
    }

    public List<String> getAnyList() {
        return this.titleList;
    }

    public Integer getAnySelect() {
        return Integer.valueOf(2);
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
        if (indexs.size() < 2) {
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
        int size = ((List) line).size();
        return Integer.valueOf(LotteryUtils.getCombin(size, 2) * LotteryUtils.getCombin(indexs.size(), 2));
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");

        String[] betContent = bets.split("\\|");
        List<Integer> indexs = LotteryUtils.getSscIndexs(betContent[0]);
        List<Integer> line = ListUtils.toIntList(betContent[1]);
        for (int i = 0; i < indexs.size(); i++) {
            int o1 = ((Integer) openNums.get(((Integer) indexs.get(i)).intValue())).intValue();
            if (line.contains(Integer.valueOf(o1))) {
                for (int j = i + 1; j < indexs.size(); j++) {
                    int o2 = ((Integer) openNums.get(((Integer) indexs.get(j)).intValue())).intValue();
                    if (o1 != o2) {
                        if (line.contains(Integer.valueOf(o2))) {
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

        Set<String> ins = PermutateUtils.getCombinSelect(indexs, 2);
        Set<String> num = PermutateUtils.getCombinSelect(line, 2);

        /**jd-gui
         Iterator localIterator2;
         for (Iterator localIterator1 = num.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         String n = (String)localIterator1.next();

         Set<String> keys = PermutateUtils.getPerms(n.split(","), ",");
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
            Set<String> keys = PermutateUtils.getPerms(n.split(","), ",");
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
