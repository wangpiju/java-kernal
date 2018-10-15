package com.hs3.lotts.ssc.star4.last.group;

import com.hs3.lotts.ssc.star4.front.group.SscStar4FrontGroup6Player;

public class SscStar4LastGroup6Player
        extends SscStar4FrontGroup6Player {
    protected int index() {
        return 1;
    }

    protected void init() {
        setRemark("至少选择2个二重号码，竞猜开奖号码的后四位，号码一致顺序不限即中奖");
        setExample("投注：*0088（0·8为二重号） 开奖：*0088（不限顺序） 即中奖");
    }

    public String getQunName() {
        return "后四";
    }
}
