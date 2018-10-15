package com.hs3.lotts.n11x5.nx;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.ListUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author jason.wang
 * 猜中位
 */
public class N11x5GuessMiddlePlayer extends PlayerBase {

    private List<String> nums = Arrays.asList("03", "04", "05", "06", "07", "08", "09");
    private NumberView[] view = {new NumberView("猜中位", this.nums)};

    private BigDecimal bonus = new BigDecimal(2);

    private Integer[] betCount =
            //03 ~ 09
            {6, 6, 6, 6, 6, 6, 6};


    public List<String> getNums() {
        return nums;
    }

    public static void main(String[] args) {
        //System.out.println(new N11x5GuessMiddlePlayer().getWin(bets, openNums));
    }

    @Override
    protected void init() {
        setRemark("从3-9选择一个号码进行购买,所选号码与5个开奖号码按照大小顺序排列后的第三个号码相同,即为中奖");
        setExample("开奖：9,6,5,7,3，若投注6则中奖");
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
        return "猜中位";
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


    public BigDecimal getWin(String bets, List<Integer> openNums) {
        //sort
        Collections.sort(openNums);
        List<Integer> lines = ListUtils.toIntList(bets);
        BigDecimal win = new BigDecimal(0);

        for (Integer i : lines) {
            if (Objects.equals(i, openNums.get(2))) {
                win = win.add(getBonus().divide(new BigDecimal(betCount[i-3]), 2, BigDecimal.ROUND_DOWN));
                break;
            }
        }

        return win;
    }


    public Map<String, BigDecimal> ifOpenWin(String bets) {
        return null;
    }


}
