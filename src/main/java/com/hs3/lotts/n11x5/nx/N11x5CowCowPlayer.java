package com.hs3.lotts.n11x5.nx;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;
import com.hs3.utils.MathUtils;
import com.hs3.utils.algorithm.Calculator;
import com.hs3.utils.algorithm.Combination;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author jason.wang
 * 趣味牛牛
 */
public class N11x5CowCowPlayer extends PlayerBase {

    private List<String> nums = Arrays.asList("牛1", "牛2", "牛3", "牛4", "牛5", "牛6", "牛7", "牛8", "牛9", "牛牛", "无点");
    private NumberView[] view = {new NumberView("牛牛", this.nums)};

    private BigDecimal bonus = new BigDecimal(2);

    private Integer[] betCount =
            //牛1~牛牛
            {6, 6,6,6,6,6,6,6,6,6,6};



    public List<String> getNums() {
        return nums;
    }

    public static void main(String[] args) {

        Integer temp = 8;
        System.out.println("牛8".contains(temp.toString()));
        String bets = "牛8";
        List<Integer> openNums = new ArrayList<>();
        openNums.add(3);
        openNums.add(2);
        openNums.add(5);
        openNums.add(7);
        openNums.add(1);
        System.out.println(new N11x5CowCowPlayer().getWin(bets, openNums));


    }
    @Override
    protected void init() {
        setRemark("开奖的五个数字中若有任意三个数字相加之和为10的倍数，其余下另两个数字之和的个位数作为对奖基准，11作为11点计算");
        setExample("开奖：任三奖号相加为10的倍數，且另二奖号相加个位数为1");
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
        return "牛牛";
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


    public  BigDecimal getWin(String bets, List<Integer> openNums) {
        List<String> lines = ListUtils.toList(bets);
        List<Integer> temp = new ArrayList<>();
        BigDecimal win = new BigDecimal(0);
        //任三數和是否為十的倍數
        boolean isTen = false;
        Integer n1 = 0;
        Integer n2 = 0;
        Integer n3 = 0;

        for(List<Integer> list :  Combination.of(openNums, 3)){
            Integer all = 0;

            for(Integer s: list){
                all += s;
            }

            if(all % 10 == 0){
                isTen = true;
                n1 = list.get(0);
                n2 = list.get(1);
                n3 = list.get(2);
                break;
            }
        }

        if(isTen){
            for(Integer s: openNums) {
                if (Objects.equals(Integer.valueOf(s), n1) || Objects.equals(Integer.valueOf(s), n2) || Objects.equals(Integer.valueOf(s), n3)) {
                    temp.add(s);
                }
            }
            openNums.removeAll(temp);
            //其餘兩數
            Integer final1 = openNums.get(0);
            Integer final2 = openNums.get(1);
            Integer sum = final1 + final2;
            Integer result = sum % 10;
            //牛X
            for(String s: lines){
                if(s.contains(result.toString())){
                    try {
                        win = win.add(getBonus().divide(new BigDecimal(betCount[result - 6]), 2, BigDecimal.ROUND_DOWN));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //牛牛
                else if(result == 0){
                    win = win.add(getBonus().divide(new BigDecimal(betCount[9]), 2, BigDecimal.ROUND_DOWN));
                }
            }
        }
        else{
            if(lines.contains("无点")){
                win = win.add(getBonus().divide(new BigDecimal(betCount[10]), 2, BigDecimal.ROUND_DOWN));
            }
        }

        return win;
    }


    public Map<String, BigDecimal> ifOpenWin(String bets) {
        return null;
    }


}
