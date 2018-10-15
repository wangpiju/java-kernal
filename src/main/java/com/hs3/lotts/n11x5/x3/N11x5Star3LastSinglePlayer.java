package com.hs3.lotts.n11x5.x3;

public class N11x5Star3LastSinglePlayer extends N11x5Star3FrontSinglePlayer {
    private String qunName = "后三";

    protected void init() {
        setRemark("手动输入3个号码组成一注，所输入的号码与当期顺序摇出的5个号码中 的后3个号码相同，且顺序一致，即为中奖");
        setExample("投注：01,02,03 开奖：*,*,01,02,03");
    }

    protected int index() {
        return 2;
    }

    protected int numLen() {
        return 3;
    }

    public String getQunName() {
        return this.qunName;
    }
}
