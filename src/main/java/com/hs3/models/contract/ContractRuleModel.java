package com.hs3.models.contract;

import com.hs3.entity.contract.ContractRule;
import com.hs3.json.JsonDateSerializer;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class ContractRuleModel {
    private String account;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date contractTime;
    private Integer contractStatus;
    private Integer type;
    private List<ContractRule> contractRuleList;

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<ContractRule> getContractRuleList() {
        return this.contractRuleList;
    }

    public void setContractRuleList(List<ContractRule> contractRuleList) {
        this.contractRuleList = contractRuleList;
    }

    public Date getContractTime() {
        return this.contractTime;
    }

    public void setContractTime(Date contractTime) {
        this.contractTime = contractTime;
    }

    public Integer getContractStatus() {
        return this.contractStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
