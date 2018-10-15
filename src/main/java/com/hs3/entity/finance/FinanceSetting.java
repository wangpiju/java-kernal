package com.hs3.entity.finance;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FinanceSetting {
    private Integer id;
    private Integer depositMaxCount;
    private BigDecimal depositMinMoney;
    private BigDecimal depositMaxMoney;
    private BigDecimal rechargeLowerMaxMoney;
    private BigDecimal rechargeLowerNotAudit;
    private Integer rechargeLowerHours;
    private Integer testUserRechargeStatus;
    private Integer testUserDepositStatus;
    private Integer depositMinBindCardHours;
    private Integer depositAuto;
    private Integer depositAutoApi;
    private BigDecimal depositAutoAmount;
    private String depositAutoOperator;
    private BigDecimal depositSplitMaxMoney;

    public boolean isOpenTestUserRecharge() {
        return this.testUserRechargeStatus.intValue() == 1;
    }

    public boolean isOpenTestUserDeposit() {
        return this.testUserDepositStatus.intValue() == 1;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDepositMaxCount() {
        return this.depositMaxCount;
    }

    public void setDepositMaxCount(Integer depositMaxCount) {
        this.depositMaxCount = depositMaxCount;
    }

    public BigDecimal getDepositMinMoney() {
        return this.depositMinMoney;
    }

    public void setDepositMinMoney(BigDecimal depositMinMoney) {
        this.depositMinMoney = depositMinMoney;
    }

    public BigDecimal getDepositMaxMoney() {
        return this.depositMaxMoney;
    }

    public void setDepositMaxMoney(BigDecimal depositMaxMoney) {
        this.depositMaxMoney = depositMaxMoney;
    }

    public BigDecimal getRechargeLowerMaxMoney() {
        return this.rechargeLowerMaxMoney;
    }

    public void setRechargeLowerMaxMoney(BigDecimal rechargeLowerMaxMoney) {
        this.rechargeLowerMaxMoney = rechargeLowerMaxMoney;
    }

    public BigDecimal getRechargeLowerNotAudit() {
        return this.rechargeLowerNotAudit;
    }

    public void setRechargeLowerNotAudit(BigDecimal rechargeLowerNotAudit) {
        this.rechargeLowerNotAudit = rechargeLowerNotAudit;
    }

    public Integer getTestUserRechargeStatus() {
        return this.testUserRechargeStatus;
    }

    public void setTestUserRechargeStatus(Integer testUserRechargeStatus) {
        this.testUserRechargeStatus = testUserRechargeStatus;
    }

    public Integer getTestUserDepositStatus() {
        return this.testUserDepositStatus;
    }

    public void setTestUserDepositStatus(Integer testUserDepositStatus) {
        this.testUserDepositStatus = testUserDepositStatus;
    }

    public Integer getDepositMinBindCardHours() {
        return this.depositMinBindCardHours;
    }

    public void setDepositMinBindCardHours(Integer depositMinBindCardHours) {
        this.depositMinBindCardHours = depositMinBindCardHours;
    }

    public Integer getRechargeLowerHours() {
        return this.rechargeLowerHours;
    }

    public void setRechargeLowerHours(Integer rechargeLowerHours) {
        this.rechargeLowerHours = rechargeLowerHours;
    }

    public Integer getDepositAuto() {
        return this.depositAuto;
    }

    public void setDepositAuto(Integer depositAuto) {
        this.depositAuto = depositAuto;
    }

    public Integer getDepositAutoApi() {
        return this.depositAutoApi;
    }

    public void setDepositAutoApi(Integer depositAutoApi) {
        this.depositAutoApi = depositAutoApi;
    }

    public BigDecimal getDepositAutoAmount() {
        return this.depositAutoAmount;
    }

    public void setDepositAutoAmount(BigDecimal depositAutoAmount) {
        this.depositAutoAmount = depositAutoAmount;
    }

    public String getDepositAutoOperator() {
        return this.depositAutoOperator;
    }

    public void setDepositAutoOperator(String depositAutoOperator) {
        this.depositAutoOperator = depositAutoOperator;
    }

    public BigDecimal getDepositSplitMaxMoney() {
        return this.depositSplitMaxMoney;
    }

    public void setDepositSplitMaxMoney(BigDecimal depositSplitMaxMoney) {
        this.depositSplitMaxMoney = depositSplitMaxMoney;
    }
}
