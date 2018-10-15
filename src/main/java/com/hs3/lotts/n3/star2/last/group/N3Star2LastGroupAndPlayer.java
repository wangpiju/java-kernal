package com.hs3.lotts.n3.star2.last.group;

import com.hs3.lotts.n3.star2.front.group.N3Star2FrontGroupAndPlayer;

public class N3Star2LastGroupAndPlayer extends N3Star2FrontGroupAndPlayer {
    protected int index() {
        return 1;
    }

    protected void init() {
        setRemark("所选数值等于开奖号码的后二位数字相加之和（不含对子）");
        setExample("投注：和值1 开奖：*10（不限顺序） 即中奖");
    }

    public String getQunName() {
        return "后二";
    }
}
