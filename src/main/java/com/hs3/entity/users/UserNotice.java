package com.hs3.entity.users;

import java.math.BigDecimal;
import java.util.Date;

public class UserNotice {
    private int id;
    private String account;
    private String content;
    private Date createTime;
    private int status;
    private String betId;
    private BigDecimal win;

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

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBetId() {
        return this.betId;
    }

    public void setBetId(String betId) {
        this.betId = betId;
    }

    public BigDecimal getWin() {
        return this.win;
    }

    public void setWin(BigDecimal win) {
        this.win = win;
    }
}
