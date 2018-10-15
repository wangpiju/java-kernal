package com.hs3.entity.contract;

import com.hs3.json.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

public class ContractRule {
    private Integer id;
    private String account;
    private String parentAccount;
    private String rootAccount;
    private Integer ruleNum;
    private String ruleName;
    private Double gtdBonuses;
    private Integer gtdBonusesCycle;
    private BigDecimal cumulativeSales;
    private Integer humenNum;
    private Double dividend;
    private Integer contractStatus;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date contractTime;

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

    public String getParentAccount() {
        return this.parentAccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
    }

    public String getRootAccount() {
        return this.rootAccount;
    }

    public void setRootAccount(String rootAccount) {
        this.rootAccount = rootAccount;
    }

    public Integer getRuleNum() {
        return this.ruleNum;
    }

    public void setRuleNum(Integer ruleNum) {
        this.ruleNum = ruleNum;
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Double getGtdBonuses() {
        return this.gtdBonuses;
    }

    public void setGtdBonuses(Double gtdBonuses) {
        this.gtdBonuses = gtdBonuses;
    }

    public Integer getGtdBonusesCycle() {
        return this.gtdBonusesCycle;
    }

    public void setGtdBonusesCycle(Integer gtdBonusesCycle) {
        this.gtdBonusesCycle = gtdBonusesCycle;
    }

    public BigDecimal getCumulativeSales() {
        return this.cumulativeSales;
    }

    public void setCumulativeSales(BigDecimal cumulativeSales) {
        this.cumulativeSales = cumulativeSales;
    }

    public Integer getHumenNum() {
        return this.humenNum;
    }

    public void setHumenNum(Integer humenNum) {
        this.humenNum = humenNum;
    }

    public Double getDividend() {
        return this.dividend;
    }

    public void setDividend(Double dividend) {
        this.dividend = dividend;
    }

    public Integer getContractStatus() {
        return this.contractStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Date getContractTime() {
        return this.contractTime;
    }

    public void setContractTime(Date contractTime) {
        this.contractTime = contractTime;
    }
}
