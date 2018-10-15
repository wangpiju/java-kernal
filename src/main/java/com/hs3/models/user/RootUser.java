package com.hs3.models.user;

import com.hs3.entity.users.User;

import java.math.BigDecimal;

public class RootUser
        extends User {
    private Integer childCount;
    private Integer teamCount;
    private BigDecimal teamAmount;

    public Integer getChildCount() {
        return this.childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

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
}
