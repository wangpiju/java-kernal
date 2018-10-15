package com.hs3.entity.users;

import java.math.BigDecimal;
import java.util.Date;

public class DailyAcc {
    private Integer id;
    private Integer ruleId;
    private String account;
    private String parentAccount;
    private String rootAccount;
    private String parentList;
    private BigDecimal rate;
    private BigDecimal betAmount;
    private Integer validAccountCount;
    private Integer lossStatus;
    private BigDecimal limitAmount;
    private Date createTime;
    private Date changeTime;
    private Integer status;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRuleId() {
        return this.ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
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

    public String getParentList() {
        return this.parentList;
    }

    public void setParentList(String parentList) {
        this.parentList = parentList;
    }

    public BigDecimal getRate() {
        return this.rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
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

    public Integer getLossStatus() {
        return this.lossStatus;
    }

    public void setLossStatus(Integer lossStatus) {
        this.lossStatus = lossStatus;
    }

    public BigDecimal getLimitAmount() {
        return this.limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getChangeTime() {
        return this.changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
