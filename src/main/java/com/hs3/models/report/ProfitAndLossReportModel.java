package com.hs3.models.report;

import com.hs3.json.JsonDateSerializer;
import com.hs3.json.JsonDateTimeSerializer;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class ProfitAndLossReportModel {
    private int id;
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
    private BigDecimal marginDollar;
    private BigDecimal marginRatio;
    private BigDecimal profitRatio;
    private BigDecimal rechargeAmount;
    private BigDecimal drawingAmount;
    private BigDecimal wages;
    private int status;
    private String startDate;
    private String endDate;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date curDate;
    private Integer test;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
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

    public BigDecimal getJuniorRebateAmount() {
        return this.juniorRebateAmount;
    }

    public void setJuniorRebateAmount(BigDecimal juniorRebateAmount) {
        this.juniorRebateAmount = juniorRebateAmount;
    }

    public BigDecimal getMarginDollar() {
        return this.marginDollar;
    }

    public void setMarginDollar(BigDecimal marginDollar) {
        this.marginDollar = marginDollar;
    }

    public BigDecimal getMarginRatio() {
        return this.marginRatio;
    }

    public void setMarginRatio(BigDecimal marginRatio) {
        this.marginRatio = marginRatio;
    }

    public BigDecimal getProfitRatio() {
        return this.profitRatio;
    }

    public void setProfitRatio(BigDecimal profitRatio) {
        this.profitRatio = profitRatio;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Date getCurDate() {
        return this.curDate;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
    }

    public Integer getTest() {
        return this.test;
    }

    public void setTest(Integer test) {
        this.test = test;
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

    public BigDecimal getWages() {
        return this.wages;
    }

    public void setWages(BigDecimal wages) {
        this.wages = wages;
    }
}
