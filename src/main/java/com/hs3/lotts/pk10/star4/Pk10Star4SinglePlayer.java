package com.hs3.lotts.pk10.star4;

import com.hs3.lotts.pk10.star3.Pk10Star3SinglePlayer;

import java.math.BigDecimal;

public class Pk10Star4SinglePlayer
        extends Pk10Star3SinglePlayer {
    private BigDecimal bonus = new BigDecimal("10080").divide(new BigDecimal(2));
    private String qunName = "前四";

    protected void init() {
        setRemark("冠军、亚军、第三名、第四名各选一个号码，所选号码与开奖号码前4名车号相同，且顺序一致，即中奖");
        setExample("投注：01,02,03,04 开奖：01,02,03,04,*,*,*,*,*,* 即中奖");
    }

    public int getLen() {
        return 4;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getQunName() {
        return this.qunName;
    }
}
