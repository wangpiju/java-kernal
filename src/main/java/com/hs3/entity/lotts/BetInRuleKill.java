package com.hs3.entity.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BetInRuleKill {
    private Integer id;
    private String account;
    private BigDecimal killAmount;
    private Integer killCount;
    private Integer bothStatus;
    private BigDecimal killPriceMin;
    private BigDecimal killPriceMax;
    private Integer type;
    private BigDecimal hadKillAmount;
    private BigDecimal hadKillCount;
    private Integer status;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getKillAmount() {
        return this.killAmount;
    }

    public void setKillAmount(BigDecimal killAmount) {
        this.killAmount = killAmount;
    }

    public Integer getKillCount() {
        return this.killCount;
    }

    public void setKillCount(Integer killCount) {
        this.killCount = killCount;
    }

    public Integer getBothStatus() {
        return this.bothStatus;
    }

    public void setBothStatus(Integer bothStatus) {
        this.bothStatus = bothStatus;
    }

    public BigDecimal getKillPriceMin() {
        return this.killPriceMin;
    }

    public void setKillPriceMin(BigDecimal killPriceMin) {
        this.killPriceMin = killPriceMin;
    }

    public BigDecimal getKillPriceMax() {
        return this.killPriceMax;
    }

    public void setKillPriceMax(BigDecimal killPriceMax) {
        this.killPriceMax = killPriceMax;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getHadKillAmount() {
        return this.hadKillAmount;
    }

    public void setHadKillAmount(BigDecimal hadKillAmount) {
        this.hadKillAmount = hadKillAmount;
    }

    public BigDecimal getHadKillCount() {
        return this.hadKillCount;
    }

    public void setHadKillCount(BigDecimal hadKillCount) {
        this.hadKillCount = hadKillCount;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
