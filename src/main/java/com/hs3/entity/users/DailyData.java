package com.hs3.entity.users;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

public class DailyData {
    private Integer id;
    private String account;
    private String parentAccount;
    private String rootAccount;
    private String parentList;
    private Integer userMark;
    private Integer test;
    private BigDecimal ruleRate;
    private Integer ruleValidAccountCount;
    private BigDecimal ruleLimitAmount;
    private Integer ruleLossStatus;
    private Integer validAccountCount;
    private Integer lossStatus;
    private BigDecimal betAmount;
    private BigDecimal winAmount;
    private BigDecimal dailyAmount;
    private String remark;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date changeTime;

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

    public String getParentList() {
        return this.parentList;
    }

    public void setParentList(String parentList) {
        this.parentList = parentList;
    }

    public Integer getUserMark() {
        return this.userMark;
    }

    public void setUserMark(Integer userMark) {
        this.userMark = userMark;
    }

    public Integer getTest() {
        return this.test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public BigDecimal getRuleRate() {
        return this.ruleRate;
    }

    public void setRuleRate(BigDecimal ruleRate) {
        this.ruleRate = ruleRate;
    }

    public Integer getRuleValidAccountCount() {
        return this.ruleValidAccountCount;
    }

    public void setRuleValidAccountCount(Integer ruleValidAccountCount) {
        this.ruleValidAccountCount = ruleValidAccountCount;
    }

    public BigDecimal getRuleLimitAmount() {
        return this.ruleLimitAmount;
    }

    public void setRuleLimitAmount(BigDecimal ruleLimitAmount) {
        this.ruleLimitAmount = ruleLimitAmount;
    }

    public Integer getRuleLossStatus() {
        return this.ruleLossStatus;
    }

    public void setRuleLossStatus(Integer ruleLossStatus) {
        this.ruleLossStatus = ruleLossStatus;
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

    public BigDecimal getBetAmount() {
        return this.betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public BigDecimal getWinAmount() {
        return this.winAmount;
    }

    public void setWinAmount(BigDecimal winAmount) {
        this.winAmount = winAmount;
    }

    public BigDecimal getDailyAmount() {
        return this.dailyAmount;
    }

    public void setDailyAmount(BigDecimal dailyAmount) {
        this.dailyAmount = dailyAmount;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
