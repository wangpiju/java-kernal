package com.hs3.entity.users;

import java.math.BigDecimal;

public class DailyRule {
    private Integer id;
    private String name;
    private BigDecimal rate;
    private BigDecimal minRate;
    private BigDecimal maxRate;
    private BigDecimal limitAmount;
    private Integer validAccountCount;
    private BigDecimal betAmount;
    private Integer lossStatus;
    private Integer level;
    private Integer status;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRate() {
        return this.rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getMinRate() {
        return this.minRate;
    }

    public void setMinRate(BigDecimal minRate) {
        this.minRate = minRate;
    }

    public BigDecimal getMaxRate() {
        return this.maxRate;
    }

    public void setMaxRate(BigDecimal maxRate) {
        this.maxRate = maxRate;
    }

    public BigDecimal getLimitAmount() {
        return this.limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public Integer getValidAccountCount() {
        return this.validAccountCount;
    }

    public void setValidAccountCount(Integer validAccountCount) {
        this.validAccountCount = validAccountCount;
    }

    public BigDecimal getBetAmount() {
        return this.betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public Integer getLossStatus() {
        return this.lossStatus;
    }

    public void setLossStatus(Integer lossStatus) {
        this.lossStatus = lossStatus;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
