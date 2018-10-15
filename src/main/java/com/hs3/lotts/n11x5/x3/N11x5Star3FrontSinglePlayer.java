package com.hs3.lotts.n11x5.x3;

import com.hs3.lotts.n11x5.x2.N11x5Star2FrontSinglePlayer;

import java.math.BigDecimal;

public class N11x5Star3FrontSinglePlayer extends N11x5Star2FrontSinglePlayer {
    private BigDecimal bonus = new BigDecimal("1980").divide(new BigDecimal(2));
    private String qunName = "前三";

    protected void init() {
        setRemark("手动输入3个号码组成一注，所输入的号码与当期顺序摇出的5个号码中 的前3个号码相同，且顺序一致，即为中奖");
        setExample("投注：01,02,03 开奖：01,02,03,*,*");
    }

    protected int index() {
        return 0;
    }

    protected int numLen() {
        return 3;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getQunName() {
        return this.qunName;
    }
}
