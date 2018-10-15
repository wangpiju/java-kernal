package com.hs3.lotts.n11x5.nx;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;
import com.hs3.utils.algorithm.Combination;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author jason.wang
 * 定單雙
 */
public class N11x5OddEvenCountPlayer extends PlayerBase {

    private List<String> nums = Arrays.asList("5单0双", "4单1双", "3单2双", "2单3双", "1单4双", "0单5双");
    private NumberView[] view = {new NumberView("定单双", this.nums)};

    private BigDecimal bonus = new BigDecimal(2);
    private Integer[] betCount =
            //5单0双 ~0单5双
            {6, 6,6,6,6,6};

    public List<String> getNums() {
        return nums;
    }

    public static void main(String[] args) {
        List<Integer> natureList = new ArrayList<>();
        natureList.add(1);
        natureList.add(2);
        natureList.add(3);
        natureList.add(4);
        natureList.add(5);
        List<Integer> temp = new ArrayList<>();
       for(Integer i : natureList){
           if(i == 1)
               temp.add(i);
       }
        natureList.removeAll(temp);
        System.out.println(natureList);

    }
    @Override
    protected void init() {
        setRemark("从6种单双个数组合中选择1种组合,当开奖号码的单双个数与所选单双组合一致");
        setExample("开奖：单双出现次数");
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
        return "趣味";
    }

    public String getTitle() {
        return "定单双";
    }

    @Override
    public String getQunName() {
        return "趣味";
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


    public BigDecimal getWin(String bets, List<Integer> openNums)
    {
        BigDecimal win = new BigDecimal(0);
        Integer oddCount = 0;
        Integer evenCount = 0;
        List<String> lines = ListUtils.toList(bets);

        for(Integer i: openNums){
            if(i%2 == 0)
                evenCount += 1;
            else
                oddCount += 1;
        }
        String target = oddCount + "单" + evenCount + "双";

        for(String s: lines){
            if(s.equals(target)){
                int betCountIndex = nums.indexOf(target);
                win = win.add(getBonus().divide(new BigDecimal(betCount[betCountIndex]), 2, BigDecimal.ROUND_DOWN));
            }
        }

        return win;
    }


    public Map<String, BigDecimal> ifOpenWin(String bets) {
        return null;
    }


}
