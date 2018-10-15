package com.hs3.lotts.ssc.star4.last;

import com.hs3.lotts.ssc.star4.front.SscStar4FrontSinglePlayer;

public class SscStar4LastSinglePlayer
        extends SscStar4FrontSinglePlayer {
    protected int index() {
        return 1;
    }

    protected void init() {
        setRemark("每位至少选择一个号码，竞猜开奖号码的后四位，号码和位置都对应即中奖");
        setExample("投注：*3456 开奖：*3456 即中奖");
    }

    public String getQunName() {
        return "后四";
    }
}
