package com.hs3.models.report;

import java.math.BigDecimal;

public class SaleSeasonReport {
    private String lotteryId;
    private String seasonId;
    private BigDecimal amount;
    private BigDecimal win;
    private BigDecimal winLose;
    private Integer betNum;
    private Integer userNum;

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getSeasonId() {
        return this.seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getWin() {
        return this.win;
    }

    public void setWin(BigDecimal win) {
        this.win = win;
    }

    public BigDecimal getWinLose() {
        return this.winLose;
    }

    public void setWinLose(BigDecimal winLose) {
        this.winLose = winLose;
    }

    public Integer getBetNum() {
        return this.betNum;
    }

    public void setBetNum(Integer betNum) {
        this.betNum = betNum;
    }

    public Integer getUserNum() {
        return this.userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }
}
