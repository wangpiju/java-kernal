package com.hs3.entity.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BetInTime
        extends BetInProbability {
    private Integer id;
    private String startTime;
    private String endTime;
    private Integer ruleId;
    private BigDecimal probability;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getRuleId() {
        return this.ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public BigDecimal getProbability() {
        return this.probability;
    }

    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }
}
