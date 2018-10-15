package com.hs3.entity.report;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AgentLotteryReport implements Serializable {

    private Integer id;
    private String reportDate;  //日期
    private String account; //账户
    private String lotteryId; //彩种ID
    private String lotteryName; //彩种名称
    private Integer betPerCount;   //投注人数
    private Integer betPerCountNoOwn;   //投注人数（不含自己）
    private BigDecimal betAmount;//投注金额
    private BigDecimal betAmountNoOwn;//投注金额（不含本人）
    private BigDecimal winningAmount;//中奖金额
    private BigDecimal winningAmountNoOwn;//中奖金额（不含本人）
    private BigDecimal rebateAmount;//返点金额
    private BigDecimal rebateAmountNoOwn;//返点金额（不含本人）
    private BigDecimal profit;//负盈利
    private BigDecimal profitNoOwn;//负盈利（不含本人）

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getProfitNoOwn() {
        return profitNoOwn;
    }

    public void setProfitNoOwn(BigDecimal profitNoOwn) {
        this.profitNoOwn = profitNoOwn;
    }

    public BigDecimal getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(BigDecimal winningAmount) {
        this.winningAmount = winningAmount;
    }

    public BigDecimal getWinningAmountNoOwn() {
        return winningAmountNoOwn;
    }

    public void setWinningAmountNoOwn(BigDecimal winningAmountNoOwn) {
        this.winningAmountNoOwn = winningAmountNoOwn;
    }

    public BigDecimal getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(BigDecimal rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public BigDecimal getRebateAmountNoOwn() {
        return rebateAmountNoOwn;
    }

    public void setRebateAmountNoOwn(BigDecimal rebateAmountNoOwn) {
        this.rebateAmountNoOwn = rebateAmountNoOwn;
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

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public Integer getBetPerCount() {
        return betPerCount;
    }

    public void setBetPerCount(Integer betPerCount) {
        this.betPerCount = betPerCount;
    }

    public Integer getBetPerCountNoOwn() {
        return betPerCountNoOwn;
    }

    public void setBetPerCountNoOwn(Integer betPerCountNoOwn) {
        this.betPerCountNoOwn = betPerCountNoOwn;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public BigDecimal getBetAmountNoOwn() {
        return betAmountNoOwn;
    }

    public void setBetAmountNoOwn(BigDecimal betAmountNoOwn) {
        this.betAmountNoOwn = betAmountNoOwn;
    }
}
