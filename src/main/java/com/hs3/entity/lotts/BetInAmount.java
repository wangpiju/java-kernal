package com.hs3.entity.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BetInAmount
        extends BetInProbability {
    private Integer id;
    private String name;
    private BigDecimal amount;
    private BigDecimal probability;
    private Integer priceId;
    private Integer ruleId;

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

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getProbability() {
        return this.probability;
    }

    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }

    public Integer getPriceId() {
        return this.priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
    }

    public Integer getRuleId() {
        return this.ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }
}
