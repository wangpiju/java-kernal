package com.hs3.lotts.n11x5.nx;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class N11x5DragonLionPlayer extends PlayerBase {

    private List<String> nums = Arrays.asList("龙", "虎");
    private NumberView[] view = {new NumberView("头尾龙虎", this.nums)};

    private BigDecimal bonus = new BigDecimal(2);


    public List<String> getNums() {
        return nums;
    }

    @Override
    protected void init() {
        setRemark("开奖号码中所选位置的数字,第一位大于最后一位,即为中奖");
        setExample("投注：龙 开奖：04,02,03,05,01");
    }

    @Override
    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public void setNums(List<String> nums) {
        this.nums = nums;
    }

    public NumberView[] getView() {
        return view;
    }

    public void setView(NumberView[] view) {
        this.view = view;
    }

    public String getGroupName() {
        return "龙虎斗";
    }

    public String getTitle() {
        return "头尾龙虎";
    }

    @Override
    public String getQunName() {
        return  "龙虎斗";
    }

    public NumberView[] getNumView() {
        return this.view;
    }

    public Integer getCount(String bets) {
        List<String> lines = ListUtils.toList(bets);

        if (ListUtils.hasSame(lines)) {
            return 0;
        }

        return lines.size();
    }


    public BigDecimal getWin(String bets, List<Integer> openNums) {
        Integer first = openNums.get(0);
        Integer last = openNums.get(4);
        //龍
        if (first > last) {
            if (bets.contains(nums.get(0)))
                return getBonus();
        }
        //虎
        else {
            if (bets.contains(nums.get(1)))
                return getBonus();
        }

        return new BigDecimal(0);
    }


    public Map<String, BigDecimal> ifOpenWin(String bets) {
//        Map<String, BigDecimal> result = new HashMap();
//
//        List<String> lines = ListUtils.toList(bets, ";");
//        List<Integer> dans = ListUtils.toIntList(((String) lines.get(0)).substring(1));
//        List<Integer> tuo = ListUtils.toIntList((String) lines.get(1));
//
//        //Set<String> indexs = PermutateUtils.getCombinSelect(getIndexs(), getSelectNum());
//        Set<String> nums = PermutateUtils.getCombinSelect(tuo, getSelectNum() - 1);
//
//
//        for (Iterator iterator = dans.iterator(); iterator.hasNext(); ) {
//            Integer dan = (Integer) iterator.next();
//            for (Iterator iterator1 = nums.iterator(); iterator1.hasNext(); ) {
//                String n = (String) iterator1.next();
//                List<Integer> nList = ListUtils.toIntList(n);
//                if (!nList.contains(dan))
//                    nList.add(dan);
//                Set<String> all = PermutateUtils.getPerms(nList.toArray(), ",");
//                for (Iterator iterator2 = all.iterator(); iterator2.hasNext(); ) {
//                    String a = (String) iterator2.next();
//                    Object nnn[] = ListUtils.toList(a).toArray(new Object[0]);
//                    String key;
//                    for (Iterator iterator3 = indexs.iterator(); iterator3.hasNext(); addMap(result, key, getBonus())) {
//                        String i = (String) iterator3.next();
//                        String k = getKey(ListUtils.toIntList(i));
//                        key = String.format(k, nnn);
//                    }
//
//                }
//
//            }
//
//        }

        //return result;
        return null;
    }
}
