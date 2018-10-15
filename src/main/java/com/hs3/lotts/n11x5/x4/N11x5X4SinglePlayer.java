package com.hs3.lotts.n11x5.x4;

import com.hs3.lotts.n11x5.nx.N11x5SinglePlayer;

import java.math.BigDecimal;

public class N11x5X4SinglePlayer extends N11x5SinglePlayer {
    private BigDecimal bonus = new BigDecimal("132").divide(new BigDecimal(2));

    protected void init() {
        setRemark("从01-11中手动输入4个号码进行购买，只要当期的5个开奖号码中包含所选号码，即为中奖");
        setExample("投注：01,02,03,04 开奖：01,02,03,04,Z（不限顺序）");
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getQunName() {
        return "任选四";
    }

    protected int getSelectNum() {
        return 4;
    }

    protected int getWinNum() {
        return 4;
    }
}
