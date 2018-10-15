package com.hs3.models.report;

import com.hs3.entity.report.UserReport;

import java.math.BigDecimal;

public class WinLoseRank
        extends UserReport {
    private BigDecimal WinRatio;
    private Integer userMark;

    public Integer getUserMark() {
        return this.userMark;
    }

    public void setUserMark(Integer userMark) {
        this.userMark = userMark;
    }

    public BigDecimal getWinRatio() {
        return this.WinRatio;
    }

    public void setWinRatio(BigDecimal winRatio) {
        this.WinRatio = winRatio;
    }
}
