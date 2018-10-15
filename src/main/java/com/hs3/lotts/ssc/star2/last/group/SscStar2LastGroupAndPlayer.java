package com.hs3.lotts.ssc.star2.last.group;

import com.hs3.lotts.ssc.star2.front.group.SscStar2FrontGroupAndPlayer;

public class SscStar2LastGroupAndPlayer
        extends SscStar2FrontGroupAndPlayer {
    protected int index() {
        return 3;
    }

    protected void init() {
        setRemark("所选数值等于开奖号码的后二位数字相加之和（不含对子）");
        setExample("投注：和值1 开奖：***10（不限顺序） 即中奖");
    }

    public String getQunName() {
        return "后二";
    }
}
