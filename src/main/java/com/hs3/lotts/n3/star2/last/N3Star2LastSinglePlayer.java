package com.hs3.lotts.n3.star2.last;

import com.hs3.lotts.n3.star2.front.N3Star2FrontSinglePlayer;

public class N3Star2LastSinglePlayer extends N3Star2FrontSinglePlayer {
    protected int index() {
        return 1;
    }

    protected void init() {
        setRemark("每位至少选择一个号码，竞猜开奖号码的后二位，号码和位置都对应即中奖");
        setExample("投注：*45 开奖：*45 即中奖");
    }

    public String getQunName() {
        return "后二";
    }
}
