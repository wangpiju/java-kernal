package com.hs3.entity.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BetInConfig {
    private Integer id;
    private Integer status;
    //最小額度
    private BigDecimal amountMin;
    //最大額度
    private BigDecimal amountMax;
    //默認最小倍數
    private BigDecimal priceDefaultMin;
    //默認最大倍數
    private BigDecimal priceDefaultMax;
    //累積額度
    private BigDecimal totalAmount;
    //遊戲最多次數
    private Integer gameCountMax;
    //遊戲倒數計時秒數
    private Integer gameSecondMax;
    //首玩刺數
    private Integer ruleFirstCount;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getAmountMin() {
        return this.amountMin;
    }

    public void setAmountMin(BigDecimal amountMin) {
        this.amountMin = amountMin;
    }

    public BigDecimal getAmountMax() {
        return this.amountMax;
    }

    public void setAmountMax(BigDecimal amountMax) {
        this.amountMax = amountMax;
    }

    public BigDecimal getPriceDefaultMin() {
        return this.priceDefaultMin;
    }

    public void setPriceDefaultMin(BigDecimal priceDefaultMin) {
        this.priceDefaultMin = priceDefaultMin;
    }

    public BigDecimal getPriceDefaultMax() {
        return this.priceDefaultMax;
    }

    public void setPriceDefaultMax(BigDecimal priceDefaultMax) {
        this.priceDefaultMax = priceDefaultMax;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getGameCountMax() {
        return this.gameCountMax;
    }

    public void setGameCountMax(Integer gameCountMax) {
        this.gameCountMax = gameCountMax;
    }

    public Integer getGameSecondMax() {
        return this.gameSecondMax;
    }

    public void setGameSecondMax(Integer gameSecondMax) {
        this.gameSecondMax = gameSecondMax;
    }

    public Integer getRuleFirstCount() {
        return this.ruleFirstCount;
    }

    public void setRuleFirstCount(Integer ruleFirstCount) {
        this.ruleFirstCount = ruleFirstCount;
    }
}
