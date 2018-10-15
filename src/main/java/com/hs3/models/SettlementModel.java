package com.hs3.models;

import java.math.BigDecimal;
import java.util.Date;

public class SettlementModel {
    private String seasonId;
    private String amountChangeName;
    private String lotteryId;
    private String betId;
    private int status;
    private String changeUser;
    private boolean isLowerLevel;
    private int test;
    private Date startTime;
    private Date endTime;
    private BigDecimal lowerAmount;
    private BigDecimal highAount;
    private String playName;
    private String orderByClassAndStatus;

    public String getSeasonId() {
        return this.seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getAmountChangeName() {
        return this.amountChangeName;
    }

    public void setAmountChangeName(String amountChangeName) {
        this.amountChangeName = amountChangeName;
    }

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getBetId() {
        return this.betId;
    }

    public void setBetId(String betId) {
        this.betId = betId;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getChangeUser() {
        return this.changeUser;
    }

    public void setChangeUser(String changeUser) {
        this.changeUser = changeUser;
    }

    public boolean isLowerLevel() {
        return this.isLowerLevel;
    }

    public void setLowerLevel(boolean isLowerLevel) {
        this.isLowerLevel = isLowerLevel;
    }

    public int getTest() {
        return this.test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getLowerAmount() {
        return this.lowerAmount;
    }

    public void setLowerAmount(BigDecimal lowerAmount) {
        this.lowerAmount = lowerAmount;
    }

    public BigDecimal getHighAount() {
        return this.highAount;
    }

    public void setHighAount(BigDecimal highAount) {
        this.highAount = highAount;
    }

    public String getPlayName() {
        return this.playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public String getOrderByClassAndStatus() {
        return this.orderByClassAndStatus;
    }

    public void setOrderByClassAndStatus(String orderByClassAndStatus) {
        this.orderByClassAndStatus = orderByClassAndStatus;
    }
}
