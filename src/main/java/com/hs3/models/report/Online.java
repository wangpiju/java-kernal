package com.hs3.models.report;

import java.util.Date;

public class Online {
    private String account;
    private Integer UserType;
    private String ip;
    private String ipInfo;
    private Date loginTime;

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getUserType() {
        return this.UserType;
    }

    public void setUserType(Integer userType) {
        this.UserType = userType;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpInfo() {
        return this.ipInfo;
    }

    public void setIpInfo(String ipInfo) {
        this.ipInfo = ipInfo;
    }

    public Date getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}
