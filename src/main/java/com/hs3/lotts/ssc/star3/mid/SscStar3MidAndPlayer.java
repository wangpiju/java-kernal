package com.hs3.lotts.ssc.star3.mid;

import com.hs3.lotts.ssc.star3.front.SscStar3FrontAndPlayer;

public class SscStar3MidAndPlayer
        extends SscStar3FrontAndPlayer {
    protected void init() {
        setRemark("至少选择一个和值，竞猜开奖号码中三位数字之和即中奖");
        setExample("投注：和值1 开奖：*001*,*010*,*100* 即中奖");
    }

    public String getQunName() {
        return "中三";
    }

    protected int index() {
        return 1;
    }
}
