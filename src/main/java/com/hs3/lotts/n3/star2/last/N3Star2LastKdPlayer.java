package com.hs3.lotts.n3.star2.last;

import com.hs3.lotts.n3.star2.front.N3Star2FrontKdPlayer;

public class N3Star2LastKdPlayer extends N3Star2FrontKdPlayer {
    protected int index() {
        return 1;
    }

    protected void init() {
        setRemark("所选数值等于开奖号码的后二位最大与最小数字相减之差");
        setExample("投注：跨度8 开奖：*08, *19, *80, *91 即中奖");
    }

    public String getQunName() {
        return "后二";
    }
}
