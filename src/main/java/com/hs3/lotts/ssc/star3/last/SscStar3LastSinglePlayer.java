package com.hs3.lotts.ssc.star3.last;

import com.hs3.lotts.ssc.star3.front.SscStar3FrontSinglePlayer;

public class SscStar3LastSinglePlayer
        extends SscStar3FrontSinglePlayer {
    protected void init() {
        setRemark("每位至少选择一个号码，竞猜开奖号码的后三位，号码和位置都对应即中奖");
        setExample("投注：**456 开奖：**456 即中奖");
    }

    public String getQunName() {
        return "后三";
    }

    protected int index() {
        return 2;
    }
}
