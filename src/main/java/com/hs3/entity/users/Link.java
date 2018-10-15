package com.hs3.entity.users;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

public class Link {
    private int id;
    private String account;
    private String parentAccount;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createUrlTime;
    private int registerCount;
    private int rechargeCount;
    private String url;
    private String bonusGroupName;
    private BigDecimal bonusRatio;
    private int status;
    private Date lastRegisterTime;

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getParentAccount() {
        return this.parentAccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
    }

    public Date getCreateUrlTime() {
        return this.createUrlTime;
    }

    public void setCreateUrlTime(Date createUrlTime) {
        this.createUrlTime = createUrlTime;
    }

    public int getRegisterCount() {
        return this.registerCount;
    }

    public void setRegisterCount(int registerCount) {
        this.registerCount = registerCount;
    }

    public int getRechargeCount() {
        return this.rechargeCount;
    }

    public void setRechargeCount(int rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBonusGroupName() {
        return this.bonusGroupName;
    }

    public void setBonusGroupName(String bonusGroupName) {
        this.bonusGroupName = bonusGroupName;
    }

    public BigDecimal getBonusRatio() {
        return this.bonusRatio;
    }

    public void setBonusRatio(BigDecimal bonusRatio) {
        this.bonusRatio = bonusRatio;
    }

    public Date getLastRegisterTime() {
        return this.lastRegisterTime;
    }

    public void setLastRegisterTime(Date lastRegisterTime) {
        this.lastRegisterTime = lastRegisterTime;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
