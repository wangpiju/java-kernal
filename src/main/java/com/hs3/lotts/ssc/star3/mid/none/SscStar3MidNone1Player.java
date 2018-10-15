package com.hs3.lotts.ssc.star3.mid.none;

import com.hs3.lotts.ssc.star3.front.none.SscStar3FrontNone1Player;

public class SscStar3MidNone1Player
        extends SscStar3FrontNone1Player {
    protected void init() {
        setRemark("从0-9中至少选择1个号码投注，竞猜开奖号码中三位中包含这个号码，包含即中奖");
        setExample("投注：1 开奖：*1xx*（不限顺序） 即中奖");
    }

    public String getQunName() {
        return "中三";
    }

    protected int index() {
        return 1;
    }
}
