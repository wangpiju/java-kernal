package com.hs3.models.lotts;

import com.hs3.entity.lotts.Player;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PlayerBonus
        extends Player {
    private BigDecimal bonusRatio;
    private BigDecimal rebateRatio;
    private BigDecimal bonus;

    public BigDecimal getBonusRatio() {
        return this.bonusRatio;
    }

    public void setBonusRatio(BigDecimal bonusRatio) {
        this.bonusRatio = bonusRatio;
    }

    public BigDecimal getRebateRatio() {
        return this.rebateRatio;
    }

    public void setRebateRatio(BigDecimal rebateRatio) {
        this.rebateRatio = rebateRatio;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }
}
