package com.hs3.entity.report;

import java.io.Serializable;
import java.math.BigDecimal;

public class AgentReport implements Serializable {

    private Integer id;
    private String reportDate;
    private String account;//账户
    private String parentAccount;//上级
    private BigDecimal amount;//余额
    private Integer test;//是否正式用户 0为正式账户
    private Integer userType;//用户类型 1：代理、0：会员
    private Integer lowerCount;//下级人数
    private Integer regCount;//注册人数
    private Integer firstChargeCount;//首充人数
    private BigDecimal teamAmount;//团队余额
    private Integer teamCount;//团队人数
    private BigDecimal rechargeAmount;//充值金额
    private Integer teamRechargeCount;//团队充值人数
    private BigDecimal withdrawAmount;//提现金额
    private BigDecimal activityAmount;//活动金额
    private BigDecimal dailyAmount;//日工资
    private BigDecimal dividendAmount;//周期分红
    private BigDecimal winningAmount;//中奖金额
    private BigDecimal rebateAmount;//代理返点金额
    private BigDecimal rebateAmountL;//团队返点金额
    private BigDecimal betAmount;//投注金额
    private Integer betPerCount;//投注人数
    private BigDecimal profit;//盈利
    private BigDecimal betAmountNoOwn;//投注金额（不含本人）
    private Integer betPerCountNoOwn;//投注人数（不含本人）
    private BigDecimal profitNoOwn;//盈利（不含本人）

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public BigDecimal getBetAmountNoOwn() {
        return betAmountNoOwn;
    }

    public void setBetAmountNoOwn(BigDecimal betAmountNoOwn) {
        this.betAmountNoOwn = betAmountNoOwn;
    }

    public Integer getBetPerCountNoOwn() {
        return betPerCountNoOwn;
    }

    public void setBetPerCountNoOwn(Integer betPerCountNoOwn) {
        this.betPerCountNoOwn = betPerCountNoOwn;
    }

    public BigDecimal getProfitNoOwn() {
        return profitNoOwn;
    }

    public void setProfitNoOwn(BigDecimal profitNoOwn) {
        this.profitNoOwn = profitNoOwn;
    }

    public Integer getTeamRechargeCount() {
        return teamRechargeCount;
    }

    public void setTeamRechargeCount(Integer teamRechargeCount) {
        this.teamRechargeCount = teamRechargeCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTest() {
        return test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getLowerCount() {
        return lowerCount;
    }

    public void setLowerCount(Integer lowerCount) {
        this.lowerCount = lowerCount;
    }

    public Integer getRegCount() {
        return regCount;
    }

    public void setRegCount(Integer regCount) {
        this.regCount = regCount;
    }

    public Integer getFirstChargeCount() {
        return firstChargeCount;
    }

    public void setFirstChargeCount(Integer firstChargeCount) {
        this.firstChargeCount = firstChargeCount;
    }

    public BigDecimal getTeamAmount() {
        return teamAmount;
    }

    public void setTeamAmount(BigDecimal teamAmount) {
        this.teamAmount = teamAmount;
    }

    public Integer getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(Integer teamCount) {
        this.teamCount = teamCount;
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public BigDecimal getActivityAmount() {
        return activityAmount;
    }

    public void setActivityAmount(BigDecimal activityAmount) {
        this.activityAmount = activityAmount;
    }

    public BigDecimal getDailyAmount() {
        return dailyAmount;
    }

    public void setDailyAmount(BigDecimal dailyAmount) {
        this.dailyAmount = dailyAmount;
    }

    public BigDecimal getDividendAmount() {
        return dividendAmount;
    }

    public void setDividendAmount(BigDecimal dividendAmount) {
        this.dividendAmount = dividendAmount;
    }

    public BigDecimal getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(BigDecimal winningAmount) {
        this.winningAmount = winningAmount;
    }

    public BigDecimal getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(BigDecimal rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public BigDecimal getRebateAmountL() {
        return rebateAmountL;
    }

    public void setRebateAmountL(BigDecimal rebateAmountL) {
        this.rebateAmountL = rebateAmountL;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public Integer getBetPerCount() {
        return betPerCount;
    }

    public void setBetPerCount(Integer betPerCount) {
        this.betPerCount = betPerCount;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}
