package com.hs3.lotts.ssc.star2.last;

import com.hs3.lotts.ssc.star2.front.SscStar2FrontAndPlayer;

public class SscStar2LastAndPlayer
        extends SscStar2FrontAndPlayer {
    protected int index() {
        return 3;
    }

    protected void init() {
        setRemark("至少选择一个和值，竞猜开奖号码后二位数字之和");
        setExample("投注：和值1 开奖：***01,***10 即中奖");
    }

    public String getQunName() {
        return "后二";
    }
}
