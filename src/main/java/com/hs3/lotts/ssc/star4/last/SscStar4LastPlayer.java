package com.hs3.lotts.ssc.star4.last;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.ssc.star4.front.SscStar4FrontPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SscStar4LastPlayer
        extends SscStar4FrontPlayer {
    protected int index() {
        return 1;
    }

    private List<Integer> nums = new ArrayList(
            Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)}));
    private NumberView[] view = {
            new NumberView("千位", this.nums),
            new NumberView("百位", this.nums),
            new NumberView("十位", this.nums),
            new NumberView("个位", this.nums)};

    public NumberView[] getNumView() {
        return this.view;
    }

    protected void init() {
        setRemark("每位至少选择一个号码，竞猜开奖号码的后四位，号码和位置都对应即中奖");
        setExample("投注：*3456 开奖：*3456 即中奖");
    }

    public String getQunName() {
        return "后四";
    }
}
