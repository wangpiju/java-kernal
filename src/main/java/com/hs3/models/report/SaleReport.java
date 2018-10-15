package com.hs3.models.report;

import com.hs3.json.JsonDateSerializer;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class SaleReport {
    private String lotteryId;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date date;
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

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
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
