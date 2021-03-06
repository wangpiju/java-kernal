package com.hs3.lotts.ssc.star4.last.none;

import com.hs3.lotts.ssc.star4.front.none.SscStar4FrontNone2Player;

public class SscStar4LastNone2Player
        extends SscStar4FrontNone2Player {
    protected int index() {
        return 1;
    }

    protected void init() {
        setRemark("从0-9中至少选择2个号码投注，竞猜开奖号码后四位中包含这2个号码，包含即中奖");
        setExample("投注：1 开奖：*1xxx（不限顺序） 即中奖");
    }

    public String getQunName() {
        return "后四";
    }
}
