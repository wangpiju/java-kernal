package com.hs3.entity.users;

import java.math.BigDecimal;

public class DaliyWages {
    private Integer id;
    private BigDecimal betAmount;
    private Integer validAccountCount;
    private BigDecimal rate;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getBetAmount() {
        return this.betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public Integer getValidAccountCount() {
        return this.validAccountCount;
    }

    public void setValidAccountCount(Integer validAccountCount) {
        this.validAccountCount = validAccountCount;
    }

    public BigDecimal getRate() {
        return this.rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String toString() {
        return "{" + this.id + ":" + this.betAmount + ":" + this.validAccountCount + ":" + this.rate + "}";
    }
}
