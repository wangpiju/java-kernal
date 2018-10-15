package com.hs3.models.user;

import java.math.BigDecimal;

public class UserTeamInfo
        implements Comparable {
    private String account;
    private String parentList;
    private String parentAccount;
    private int userType;
    private int teamCount;
    private BigDecimal teamAmount = BigDecimal.ZERO;
    private BigDecimal rechargeAmount = BigDecimal.ZERO;
    private BigDecimal drawingAmount = BigDecimal.ZERO;
    private BigDecimal wages = BigDecimal.ZERO;
    private BigDecimal win = BigDecimal.ZERO;
    private int registerNum;
    private int firstRechargeNum;
    private int userCount;

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getParentList() {
        return this.parentList;
    }

    public void setParentList(String parentList) {
        this.parentList = parentList;
    }

    public String getParentAccount() {
        return this.parentAccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
    }

    public int getTeamCount() {
        return this.teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    public BigDecimal getTeamAmount() {
        return this.teamAmount;
    }

    public void setTeamAmount(BigDecimal teamAmount) {
        this.teamAmount = teamAmount;
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

    public BigDecimal getWin() {
        return this.win;
    }

    public void setWin(BigDecimal win) {
        this.win = win;
    }

    public int getRegisterNum() {
        return this.registerNum;
    }

    public void setRegisterNum(int registerNum) {
        this.registerNum = registerNum;
    }

    public int getFirstRechargeNum() {
        return this.firstRechargeNum;
    }

    public void setFirstRechargeNum(int firstRechargeNum) {
        this.firstRechargeNum = firstRechargeNum;
    }

    public int getUserCount() {
        return this.userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getUserType() {
        return this.userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int compareTo(Object o) {
        UserTeamInfo u = (UserTeamInfo) o;
        int n = this.rechargeAmount.compareTo(u.rechargeAmount);
        if (n == 0) {
            return this.win.compareTo(u.win);
        }
        return n;
    }
}
