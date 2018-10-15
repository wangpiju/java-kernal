package com.hs3.lotts.ssc.star4.last.group;

import com.hs3.lotts.ssc.star4.front.group.SscStar4FrontGroup24Player;

public class SscStar4LastGroup24Player
        extends SscStar4FrontGroup24Player {
    protected int index() {
        return 1;
    }

    protected void init() {
        setRemark("至少选择4个号码投注，竞猜开奖号码的后4位，号码一致顺序不限即中奖");
        setExample("投注：*2568 开奖：*2568（不限顺序） 即中奖");
    }

    public String getQunName() {
        return "后四";
    }
}
