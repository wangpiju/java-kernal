package com.hs3.models.lotts;

import java.math.BigDecimal;

public class TraceModel {
    private String account;
    private Integer test;
    private String startTime;
    private String endTime;
    private Integer status;
    private Integer isWinStop;
    private String lotteryId;
    private String startSeason;
    private boolean isLowerLevel;
    private BigDecimal lowerWinAmount;
    private BigDecimal highWinAmount;
    private Integer include;
    private String playerId;

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getTest() {
        return this.test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsWinStop() {
        return this.isWinStop;
    }

    public void setIsWinStop(Integer isWinStop) {
        this.isWinStop = isWinStop;
    }

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getStartSeason() {
        return this.startSeason;
    }

    public void setStartSeason(String startSeason) {
        this.startSeason = startSeason;
    }

    public boolean isLowerLevel() {
        return this.isLowerLevel;
    }

    public void setLowerLevel(boolean isLowerLevel) {
        this.isLowerLevel = isLowerLevel;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public BigDecimal getLowerWinAmount() {
        return this.lowerWinAmount;
    }

    public void setLowerWinAmount(BigDecimal lowerWinAmount) {
        this.lowerWinAmount = lowerWinAmount;
    }

    public BigDecimal getHighWinAmount() {
        return this.highWinAmount;
    }

    public void setHighWinAmount(BigDecimal highWinAmount) {
        this.highWinAmount = highWinAmount;
    }

    public Integer getInclude() {
        return this.include;
    }

    public void setInclude(Integer include) {
        this.include = include;
    }
}
