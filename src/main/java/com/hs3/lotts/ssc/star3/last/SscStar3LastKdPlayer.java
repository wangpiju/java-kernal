package com.hs3.lotts.ssc.star3.last;

import com.hs3.lotts.ssc.star3.front.SscStar3FrontKdPlayer;

public class SscStar3LastKdPlayer
        extends SscStar3FrontKdPlayer {
    protected void init() {
        setRemark("所选数值等于开奖号码的后3位最大与最小数字相减之差即为中奖");
        setExample("投注：跨度8 开奖：(1)前三数字08x（不限顺序）,x≠9; (2)前三数字19x（不限顺序），x≠0");
    }

    public String getQunName() {
        return "后三";
    }

    protected int index() {
        return 2;
    }
}
