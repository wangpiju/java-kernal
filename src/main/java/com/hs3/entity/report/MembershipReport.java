package com.hs3.entity.report;

import java.io.Serializable;
import java.math.BigDecimal;

public class MembershipReport implements Serializable {

    private Integer id;
    private String reportDate;
    private String account;
    private String parentAccount;
    private String rootAccount;
    private BigDecimal amount;
    private String regTime;
    private BigDecimal rechargeAmount;
    private Integer rechargeAmountNum;
    private BigDecimal withdrawAmount;
    private Integer withdrawAmountNum;
    private BigDecimal activityAmount;
    private BigDecimal dailyAmount;
    private BigDecimal dividendAmount;
    private BigDecimal betAmount;
    private BigDecimal winningAmount;
    private BigDecimal rebateAmount;
    private BigDecimal withdrawalSysAmount;
    private BigDecimal profitAndLoss;
    private BigDecimal grossRate;
    private BigDecimal profit;
    private BigDecimal earningsRatio;

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

    public BigDecimal getProfitAndLoss() {
        return profitAndLoss;
    }

    public void setProfitAndLoss(BigDecimal profitAndLoss) {
        this.profitAndLoss = profitAndLoss;
    }

    public BigDecimal getGrossRate() {
        return grossRate;
    }

    public void setGrossRate(BigDecimal grossRate) {
        this.grossRate = grossRate;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getEarningsRatio() {
        return earningsRatio;
    }

    public void setEarningsRatio(BigDecimal earningsRatio) {
        this.earningsRatio = earningsRatio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
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

    public String getRootAccount() {
        return rootAccount;
    }

    public void setRootAccount(String rootAccount) {
        this.rootAccount = rootAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Integer getRechargeAmountNum() {
        return rechargeAmountNum;
    }

    public void setRechargeAmountNum(Integer rechargeAmountNum) {
        this.rechargeAmountNum = rechargeAmountNum;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public Integer getWithdrawAmountNum() {
        return withdrawAmountNum;
    }

    public void setWithdrawAmountNum(Integer withdrawAmountNum) {
        this.withdrawAmountNum = withdrawAmountNum;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
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

    public BigDecimal getWithdrawalSysAmount() {
        return withdrawalSysAmount;
    }

    public void setWithdrawalSysAmount(BigDecimal withdrawalSysAmount) {
        this.withdrawalSysAmount = withdrawalSysAmount;
    }
}
