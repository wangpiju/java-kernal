package com.hs3.entity.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BonusGroupDetails {
    private String id;
    private String lotteryId;
    private Integer bonusGroupId;
    private BigDecimal bonusRatio;
    private BigDecimal rebateRatio;
    private BigDecimal bonus;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public Integer getBonusGroupId() {
        return this.bonusGroupId;
    }

    public void setBonusGroupId(Integer bonusGroupId) {
        this.bonusGroupId = bonusGroupId;
    }

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
