package com.hs3.entity.report;

import com.hs3.json.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

public class UserReport {
    private Integer id;
    private String account;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date createDate;
    private BigDecimal betAmount;
    private BigDecimal rebateAmount;
    private BigDecimal actualSaleAmount;
    private BigDecimal winAmount;
    private BigDecimal count;
    private BigDecimal activityAndSend;
    private BigDecimal juniorRebateAmount;
    private BigDecimal rechargeAmount;
    private BigDecimal drawingAmount;
    private Integer test;
    private BigDecimal tigerWinAmount;
    private BigDecimal wages;

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

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getBetAmount() {
        return this.betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public BigDecimal getRebateAmount() {
        return this.rebateAmount;
    }

    public void setRebateAmount(BigDecimal rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public BigDecimal getActualSaleAmount() {
        return this.actualSaleAmount;
    }

    public void setActualSaleAmount(BigDecimal actualSaleAmount) {
        this.actualSaleAmount = actualSaleAmount;
    }

    public BigDecimal getWinAmount() {
        return this.winAmount;
    }

    public void setWinAmount(BigDecimal winAmount) {
        this.winAmount = winAmount;
    }

    public BigDecimal getCount() {
        return this.count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public BigDecimal getActivityAndSend() {
        return this.activityAndSend;
    }

    public void setActivityAndSend(BigDecimal activityAndSend) {
        this.activityAndSend = activityAndSend;
    }

    public Integer getTest() {
        return this.test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public BigDecimal getJuniorRebateAmount() {
        return this.juniorRebateAmount;
    }

    public void setJuniorRebateAmount(BigDecimal juniorRebateAmount) {
        this.juniorRebateAmount = juniorRebateAmount;
    }

    public BigDecimal getRechargeAmount() {
        return this.rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public BigDecimal getDrawingAmount() {
        return this.drawingAmount;
    }

    public void setDrawingAmount(BigDecimal drawingAmount) {
        this.drawingAmount = drawingAmount;
    }

    public BigDecimal getTigerWinAmount() {
        return this.tigerWinAmount;
    }

    public void setTigerWinAmount(BigDecimal tigerWinAmount) {
        this.tigerWinAmount = tigerWinAmount;
    }

    public BigDecimal getWages() {
        return this.wages;
    }

    public void setWages(BigDecimal wages) {
        this.wages = wages;
    }
}
