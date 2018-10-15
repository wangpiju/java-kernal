package com.hs3.lotts.n11x5.x2;

public class N11x5Star2LastSinglePlayer extends N11x5Star2FrontSinglePlayer {
    private String qunName = "后二";

    protected void init() {
        setRemark("手动输入2个号码组成一注，所输入的号码与当期顺序摇出的5个号码中 的后2个号码相同，且顺序一致，即为中奖");
        setExample("投注：01,02 开奖：*,*,*,01,02");
    }

    protected int index() {
        return 3;
    }

    protected int numLen() {
        return 2;
    }

    public String getQunName() {
        return this.qunName;
    }
}
