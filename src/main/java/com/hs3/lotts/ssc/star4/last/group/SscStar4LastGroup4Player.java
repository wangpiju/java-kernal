package com.hs3.lotts.ssc.star4.last.group;

import com.hs3.lotts.ssc.star4.front.group.SscStar4FrontGroup4Player;

public class SscStar4LastGroup4Player
        extends SscStar4FrontGroup4Player {
    protected int index() {
        return 1;
    }

    protected void init() {
        setRemark("至少选择1个三重号码和1个单号号码，竞猜开奖号码的后四位，号码一致顺序不限即中奖");
        setExample("投注：*2888（8为三重号，2为单号） 开奖：*2888（不限顺序） 即中奖");
    }

    public String getQunName() {
        return "后四";
    }
}
