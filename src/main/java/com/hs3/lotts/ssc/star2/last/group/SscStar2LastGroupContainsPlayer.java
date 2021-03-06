package com.hs3.lotts.ssc.star2.last.group;

import com.hs3.lotts.ssc.star2.front.group.SscStar2FrontGroupContainsPlayer;

public class SscStar2LastGroupContainsPlayer
        extends SscStar2FrontGroupContainsPlayer {
    protected int index() {
        return 3;
    }

    protected void init() {
        setRemark("从0-9中任意选择1个号码，开奖号码的后二位中任意1位包含所选的包胆号码相同（不含对子）");
        setExample("投注：包胆8；开奖：***x8（不限顺序，x≠8） 即中奖");
    }

    public String getQunName() {
        return "后二";
    }
}
