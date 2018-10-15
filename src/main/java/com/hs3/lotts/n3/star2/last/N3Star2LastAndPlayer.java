package com.hs3.lotts.n3.star2.last;

import com.hs3.lotts.n3.star2.front.N3Star2FrontAndPlayer;

public class N3Star2LastAndPlayer extends N3Star2FrontAndPlayer {
    protected int index() {
        return 1;
    }

    protected void init() {
        setRemark("至少选择一个和值，竞猜开奖号码后二位数字之和");
        setExample("投注：和值1 开奖：*01,*10 即中奖");
    }

    public String getQunName() {
        return "后二";
    }
}
