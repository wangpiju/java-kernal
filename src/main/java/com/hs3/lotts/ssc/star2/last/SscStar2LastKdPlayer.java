package com.hs3.lotts.ssc.star2.last;

import com.hs3.lotts.ssc.star2.front.SscStar2FrontKdPlayer;

public class SscStar2LastKdPlayer
        extends SscStar2FrontKdPlayer {
    protected int index() {
        return 3;
    }

    protected void init() {
        setRemark("所选数值等于开奖号码的后二位最大与最小数字相减之差");
        setExample("投注：跨度8 开奖：***08, ***19, ***80, ***91 即中奖");
    }

    public String getQunName() {
        return "后二";
    }
}
