package com.hs3.entity.contract;

public class ContractConfig {
    private Integer id;
    private Integer ruleNum;
    private Integer dayNum;
    private Integer bonusCycle;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRuleNum() {
        return this.ruleNum;
    }

    public void setRuleNum(Integer ruleNum) {
        this.ruleNum = ruleNum;
    }

    public Integer getDayNum() {
        return this.dayNum;
    }

    public void setDayNum(Integer dayNum) {
        this.dayNum = dayNum;
    }

    public Integer getBonusCycle() {
        return this.bonusCycle;
    }

    public void setBonusCycle(Integer bonusCycle) {
        this.bonusCycle = bonusCycle;
    }
}
