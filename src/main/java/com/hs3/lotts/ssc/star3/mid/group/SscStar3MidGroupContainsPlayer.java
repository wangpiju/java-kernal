package com.hs3.lotts.ssc.star3.mid.group;

import com.hs3.lotts.ssc.star3.front.group.SscStar3FrontGroupContainsPlayer;

public class SscStar3MidGroupContainsPlayer
        extends SscStar3FrontGroupContainsPlayer {
    protected void init() {
        setRemark("从0-9中任意选择1个包胆号码，开奖号码的中三位中任意1位与所选包胆号码相同(不含豹子号)，即为中奖");
        setExample("投注：包胆3开奖：(1) *3xx*或者*33x*（不限顺序）,中组三 (2)*3xy*（不限顺序）,中组六   注：x≠y≠3");
    }

    public String getQunName() {
        return "中三";
    }

    protected int index() {
        return 1;
    }
}
