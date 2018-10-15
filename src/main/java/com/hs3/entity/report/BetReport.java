package com.hs3.entity.report;

import java.io.Serializable;
import java.math.BigDecimal;

public class BetReport implements Serializable {

    private Integer id;
    private String reportDate;
    private String lotteryId;
    private String lotteryName;
    private Integer betPerNum;
    private BigDecimal betAmount;
    private BigDecimal winningAmount;
    private BigDecimal rebateAmount;
    private BigDecimal profit;
    private BigDecimal earningsRatio;

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

    public Integer getBetPerNum() {
        return betPerNum;
    }

    public void setBetPerNum(Integer betPerNum) {
        this.betPerNum = betPerNum;
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
}
