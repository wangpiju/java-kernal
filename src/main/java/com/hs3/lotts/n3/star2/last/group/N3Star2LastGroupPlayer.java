package com.hs3.lotts.n3.star2.last.group;

import com.hs3.lotts.n3.star2.front.group.N3Star2FrontGroupPlayer;

public class N3Star2LastGroupPlayer extends N3Star2FrontGroupPlayer {
    protected int index() {
        return 1;
    }

    protected void init() {
        setRemark("从0-9中选择2个数字组成一注，所选号码与开奖号码的后二位相同，顺序不限");
        setExample("投注：5,8 开奖：*58(不限顺序) 即中奖");
    }

    public String getQunName() {
        return "后二";
    }
}
