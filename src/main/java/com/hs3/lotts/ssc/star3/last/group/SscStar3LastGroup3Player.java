package com.hs3.lotts.ssc.star3.last.group;

import com.hs3.lotts.ssc.star3.front.group.SscStar3FrontGroup3Player;

public class SscStar3LastGroup3Player
        extends SscStar3FrontGroup3Player {
    protected void init() {
        setRemark("从0-9中选择2个数字组成两注，所选号码与开奖号码的后三位相同，顺序不限");
        setExample("投注：12 开奖：**112（不限顺序）");
    }

    public String getQunName() {
        return "后三";
    }

    protected int index() {
        return 2;
    }
}
