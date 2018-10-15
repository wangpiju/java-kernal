package com.hs3.models.user;

import com.hs3.entity.users.User;

import java.math.BigDecimal;

public class UserModel
        extends User {
    private Integer teamCount;
    private BigDecimal teamAmount;
    private BigDecimal dailyRate;

    public Integer getTeamCount() {
        return this.teamCount;
    }

    public void setTeamCount(Integer teamCount) {
        this.teamCount = teamCount;
    }

    public BigDecimal getTeamAmount() {
        return this.teamAmount;
    }

    public void setTeamAmount(BigDecimal teamAmount) {
        this.teamAmount = teamAmount;
    }

    public BigDecimal getDailyRate() {
        return this.dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }
}
