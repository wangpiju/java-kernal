package com.hs3.entity.activity;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ActivityLoss {
    private int id;
    private Integer activityId;
    private BigDecimal maxAmount;
    private BigDecimal giveFatherAmount;
    private BigDecimal giveGrandpaAmount;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public BigDecimal getMaxAmount() {
        return this.maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public BigDecimal getGiveFatherAmount() {
        return this.giveFatherAmount;
    }

    public void setGiveFatherAmount(BigDecimal giveFatherAmount) {
        this.giveFatherAmount = giveFatherAmount;
    }

    public BigDecimal getGiveGrandpaAmount() {
        return this.giveGrandpaAmount;
    }

    public void setGiveGrandpaAmount(BigDecimal giveGrandpaAmount) {
        this.giveGrandpaAmount = giveGrandpaAmount;
    }
}
