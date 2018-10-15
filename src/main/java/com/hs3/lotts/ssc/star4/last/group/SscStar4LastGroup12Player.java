package com.hs3.lotts.ssc.star4.last.group;

import com.hs3.lotts.ssc.star4.front.group.SscStar4FrontGroup12Player;

public class SscStar4LastGroup12Player
        extends SscStar4FrontGroup12Player {
    protected int index() {
        return 1;
    }

    protected void init() {
        setRemark("至少选择1个二重号码和2个单号号码，竞猜开奖号码的后四位，号码一致顺序不限即中奖");
        setExample("投注：*2588（8为二重号，2·5为单号）开奖：*2588（不限顺序） 即中奖");
    }

    public String getQunName() {
        return "后四";
    }
}
