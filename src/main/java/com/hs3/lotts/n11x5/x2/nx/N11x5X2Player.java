package com.hs3.lotts.n11x5.x2.nx;

import com.hs3.lotts.n11x5.nx.N11x5Player;

import java.math.BigDecimal;

public class N11x5X2Player extends N11x5Player {
    private BigDecimal bonus = new BigDecimal("11").divide(new BigDecimal(2));

    protected void init() {
        setRemark("从01-11共11个号码中选择2个号码进行购买，只要当期的5个开奖号码中包含所选号码，即为中奖");
        setExample("投注：01,02 开奖：01,02,X,Y,Z（不限顺序）");
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    protected int getSelectNum() {
        return 2;
    }

    protected int getWinNum() {
        return 2;
    }

    public String getQunName() {
        return "任选二";
    }
}
