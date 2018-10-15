package com.hs3.lotts.ssc.star2.last;

import com.hs3.lotts.ssc.star2.front.SscStar2FrontSinglePlayer;

public class SscStar2LastSinglePlayer
        extends SscStar2FrontSinglePlayer {
    protected int index() {
        return 3;
    }

    protected void init() {
        setRemark("每位至少选择一个号码，竞猜开奖号码的后二位，号码和位置都对应即中奖");
        setExample("投注：***45 开奖：***45 即中奖");
    }

    public String getQunName() {
        return "后二";
    }
}
