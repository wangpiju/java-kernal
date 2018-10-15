package com.hs3.lotts.ssc.star3.last.group;

import com.hs3.lotts.ssc.star3.front.group.SscStar3FrontGroup6SinglePlayer;

public class SscStar3LastGroup6SinglePlayer
        extends SscStar3FrontGroup6SinglePlayer {
    protected void init() {
        setRemark("从0-9中任意选择3个号码组成一注，所选号码与开奖号码的后三位相同，顺序不限");
        setExample("投注：**123 开奖：**123（不限顺序）");
    }

    public String getQunName() {
        return "后三";
    }

    protected int index() {
        return 2;
    }
}
