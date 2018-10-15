package com.hs3.models.lotts;

import com.hs3.entity.lotts.Bet;

import java.math.BigDecimal;

public class AdminBetDetail
        extends Bet {
    private BigDecimal rebateRatio;

    public BigDecimal getRebateRatio() {
        return this.rebateRatio;
    }

    public void setRebateRatio(BigDecimal rebateRatio) {
        this.rebateRatio = rebateRatio;
    }
}
