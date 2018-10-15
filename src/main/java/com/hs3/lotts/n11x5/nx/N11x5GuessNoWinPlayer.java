package com.hs3.lotts.n11x5.nx;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author jason
 * 猜必不中
 */
public class N11x5GuessNoWinPlayer extends PlayerBase {

    private List<String> nums = Arrays.asList("01", "02","03", "04","05", "06","07", "08","09", "10","11");
    private NumberView[] view = {new NumberView("猜必不中", this.nums)};

    private BigDecimal bonus = new BigDecimal(2);


    public List<String> getNums() {
        return nums;
    }

    @Override
    protected void init() {
        setRemark("从01-11共11个号码中选择1个号码进行购买,当期的5个开奖号码中不包含所选号码,即为中奖");
        setExample("投注：01 开奖：04,02,03,05,06");
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
        return "猜必不中";
    }

    @Override
    public String getQunName() {
        return  "趣味";
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
        BigDecimal win = new BigDecimal(0);
        List<Integer> lines = ListUtils.toIntList(bets);

        for(Integer i: lines){
            boolean isMatch = false;
            for (Integer y: openNums){
                if(y.equals(i)){
                    isMatch = true;
                    break;
                }
            }
            //如果都沒中則中一次獎
            if(!isMatch){
                win = win.add(getBonus());
            }
        }

        return win;
    }


    public Map<String, BigDecimal> ifOpenWin(String bets) {
        return null;
    }
}
