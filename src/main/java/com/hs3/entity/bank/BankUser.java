package com.hs3.entity.bank;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BankUser {
    private String id;
    private Integer userMark;
    private String account;
    private Integer bankNameId;
    private String card;
    private String address;
    private String niceName;
    private Integer status;
    private String parentAccount;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;

    public String getParentAccount() {
        return this.parentAccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserMark() {
        return this.userMark;
    }

    public void setUserMark(Integer userMark) {
        this.userMark = userMark;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getBankNameId() {
        return this.bankNameId;
    }

    public void setBankNameId(Integer bankNameId) {
        this.bankNameId = bankNameId;
    }

    public String getCard() {
        return this.card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNiceName() {
        return this.niceName;
    }

    public void setNiceName(String niceName) {
        this.niceName = niceName;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
