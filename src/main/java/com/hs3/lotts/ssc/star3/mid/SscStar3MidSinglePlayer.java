package com.hs3.lotts.ssc.star3.mid;

import com.hs3.lotts.ssc.star3.front.SscStar3FrontSinglePlayer;

public class SscStar3MidSinglePlayer
        extends SscStar3FrontSinglePlayer {
    protected void init() {
        setRemark("每位至少选择一个号码，竞猜开奖号码的中三位，号码和位置都对应即中奖");
        setExample("投注：*456* 开奖：*456* 即中奖");
    }

    public String getQunName() {
        return "中三";
    }

    protected int index() {
        return 1;
    }
}
