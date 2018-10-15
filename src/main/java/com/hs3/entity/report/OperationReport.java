package com.hs3.entity.report;

import java.math.BigDecimal;

public class OperationReport {
    public static final String REPORT_DATE_FORMAT = "yyyy-MM-dd";
    private String reportDate;
    private Integer userRegisteredNum;
    private Integer userActiveNum;
    private BigDecimal userActivityAmount;
    private BigDecimal gameValidAmount;
    private Integer gameBetNum;
    private BigDecimal gameRebate;
    private BigDecimal gameHigherRebate;
    private BigDecimal gameProfit;
    private BigDecimal game3DAmount;
    private Integer rechaegeFirstNum;
    private BigDecimal rechargeFirstAmount;
    private Integer rechargeAccountNum;
    private Integer rechargeNum;
    private BigDecimal rechargePayAmount;
    private BigDecimal rechargeBankAmount;
    private Integer depositAccountNum;
    private Integer depositNum;
    private BigDecimal depositAmount;
    private BigDecimal allUserAmount;
    private BigDecimal allOperationAmount;

    public String getReportDate() {
        return this.reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getUserRegisteredNum() {
        return this.userRegisteredNum;
    }

    public void setUserRegisteredNum(Integer userRegisteredNum) {
        this.userRegisteredNum = userRegisteredNum;
    }

    public Integer getUserActiveNum() {
        return this.userActiveNum;
    }

    public void setUserActiveNum(Integer userActiveNum) {
        this.userActiveNum = userActiveNum;
    }

    public BigDecimal getUserActivityAmount() {
        return this.userActivityAmount;
    }

    public void setUserActivityAmount(BigDecimal userActivityAmount) {
        this.userActivityAmount = userActivityAmount;
    }

    public BigDecimal getGameValidAmount() {
        return this.gameValidAmount;
    }

    public void setGameValidAmount(BigDecimal gameValidAmount) {
        this.gameValidAmount = gameValidAmount;
    }

    public Integer getGameBetNum() {
        return this.gameBetNum;
    }

    public void setGameBetNum(Integer gameBetNum) {
        this.gameBetNum = gameBetNum;
    }

    public BigDecimal getGameRebate() {
        return this.gameRebate;
    }

    public void setGameRebate(BigDecimal gameRebate) {
        this.gameRebate = gameRebate;
    }

    public BigDecimal getGameProfit() {
        return this.gameProfit;
    }

    public void setGameProfit(BigDecimal gameProfit) {
        this.gameProfit = gameProfit;
    }

    public BigDecimal getGameHigherRebate() {
        return this.gameHigherRebate;
    }

    public void setGameHigherRebate(BigDecimal gameHigherRebate) {
        this.gameHigherRebate = gameHigherRebate;
    }

    public BigDecimal getGame3DAmount() {
        return this.game3DAmount;
    }

    public void setGame3DAmount(BigDecimal game3dAmount) {
        this.game3DAmount = game3dAmount;
    }

    public Integer getRechaegeFirstNum() {
        return this.rechaegeFirstNum;
    }

    public void setRechaegeFirstNum(Integer rechaegeFirstNum) {
        this.rechaegeFirstNum = rechaegeFirstNum;
    }

    public BigDecimal getRechargeFirstAmount() {
        return this.rechargeFirstAmount;
    }

    public void setRechargeFirstAmount(BigDecimal rechargeFirstAmount) {
        this.rechargeFirstAmount = rechargeFirstAmount;
    }

    public Integer getRechargeAccountNum() {
        return this.rechargeAccountNum;
    }

    public void setRechargeAccountNum(Integer rechargeAccountNum) {
        this.rechargeAccountNum = rechargeAccountNum;
    }

    public Integer getRechargeNum() {
        return this.rechargeNum;
    }

    public void setRechargeNum(Integer rechargeNum) {
        this.rechargeNum = rechargeNum;
    }

    public BigDecimal getRechargePayAmount() {
        return this.rechargePayAmount;
    }

    public void setRechargePayAmount(BigDecimal rechargePayAmount) {
        this.rechargePayAmount = rechargePayAmount;
    }

    public BigDecimal getRechargeBankAmount() {
        return this.rechargeBankAmount;
    }

    public void setRechargeBankAmount(BigDecimal rechargeBankAmount) {
        this.rechargeBankAmount = rechargeBankAmount;
    }

    public Integer getDepositAccountNum() {
        return this.depositAccountNum;
    }

    public void setDepositAccountNum(Integer depositAccountNum) {
        this.depositAccountNum = depositAccountNum;
    }

    public Integer getDepositNum() {
        return this.depositNum;
    }

    public void setDepositNum(Integer depositNum) {
        this.depositNum = depositNum;
    }

    public BigDecimal getDepositAmount() {
        return this.depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getAllUserAmount() {
        return this.allUserAmount;
    }

    public void setAllUserAmount(BigDecimal allUserAmount) {
        this.allUserAmount = allUserAmount;
    }

    public BigDecimal getAllOperationAmount() {
        return this.allOperationAmount;
    }

    public void setAllOperationAmount(BigDecimal allOperationAmount) {
        this.allOperationAmount = allOperationAmount;
    }
}
