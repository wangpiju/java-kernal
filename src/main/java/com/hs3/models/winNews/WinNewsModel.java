package com.hs3.models.winNews;

import java.math.BigDecimal;

public class WinNewsModel {
    private String account;
    private BigDecimal winAmount;
    private String lotteryName;

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getWinAmount() {
        return this.winAmount;
    }

    public void setWinAmount(BigDecimal winAmount) {
        this.winAmount = winAmount;
    }

    public String getLotteryName() {
        return this.lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }
}
