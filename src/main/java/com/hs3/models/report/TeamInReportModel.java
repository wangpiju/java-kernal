package com.hs3.models.report;

import com.hs3.json.JsonDateSerializer;
import com.hs3.json.JsonDateTimeSerializer;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class TeamInReportModel {
    private int id;
    private String account;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date createDate;
    private BigDecimal betAmount;
    private BigDecimal winAmount;
    private BigDecimal totalAmount;
    private BigDecimal marginDollar;
    private BigDecimal marginRatio;
    private BigDecimal profitRatio;
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

    public BigDecimal getWinAmount() {
        return this.winAmount;
    }

    public void setWinAmount(BigDecimal winAmount) {
        this.winAmount = winAmount;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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
}
