package com.hs3.lotts.n11x5.x4;

import com.hs3.lotts.n11x5.nx.N11x5DtPlayer;

import java.math.BigDecimal;

public class N11x5X4DtPlayer extends N11x5DtPlayer {
    private BigDecimal bonus = new BigDecimal("132").divide(new BigDecimal(2));

    protected void init() {
        setRemark("从01-11共11个号码中至少选择4个以上号码进行投注，每注需至少包括1个胆码及3个拖码。只要当期的5个开奖号码中有4个包含所选号码（每注包含4个号码），即为中奖");
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
