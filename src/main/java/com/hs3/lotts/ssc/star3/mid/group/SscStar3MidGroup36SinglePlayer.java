package com.hs3.lotts.ssc.star3.mid.group;

import com.hs3.lotts.ssc.star3.front.group.SscStar3FrontGroup36SinglePlayer;

public class SscStar3MidGroup36SinglePlayer
        extends SscStar3FrontGroup36SinglePlayer {
    protected void init() {
        setRemark("手动输入号码，3个数字为一注组六,2个数字为一注组三，所选号码与开奖号码的中三位相同，顺序不限，即为中奖");
        setExample("投注：(01),(123) 开奖：(1)*001*（不限顺序）即中组三，(2)*123*（不限顺序）即中组六");
    }

    public String getQunName() {
        return "中三";
    }

    protected int index() {
        return 1;
    }
}
