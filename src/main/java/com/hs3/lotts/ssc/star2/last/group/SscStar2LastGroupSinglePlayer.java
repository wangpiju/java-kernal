package com.hs3.lotts.ssc.star2.last.group;

import com.hs3.lotts.ssc.star2.front.group.SscStar2FrontGroupSinglePlayer;

public class SscStar2LastGroupSinglePlayer
        extends SscStar2FrontGroupSinglePlayer {
    protected int index() {
        return 3;
    }

    protected void init() {
        setRemark("从0-9中选择2个数字组成一注，所选号码与开奖号码的后二位相同，顺序不限");
        setExample("投注：5,8 开奖：***58(不限顺序) 即中奖");
    }

    public String getQunName() {
        return "后二";
    }
}
