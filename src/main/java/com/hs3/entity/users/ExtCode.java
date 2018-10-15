package com.hs3.entity.users;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ExtCode
        implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String account;
    private String parnetAccount;
    private String userType;
    private String code;
    private Integer validTime;
    private Integer bonusGroupid;
    private BigDecimal rebateRatio;
    private String extAddress;
    private String qq;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;
    private String status;
    private String canRegists;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date lastRegist;

    public Date getLastregist() {
        return this.lastRegist;
    }

    public void setLastregist(Date lastregist) {
        this.lastRegist = lastregist;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = (account == null ? null : account.trim());
    }

    public String getParnetaccount() {
        return this.parnetAccount;
    }

    public void setParnetaccount(String parnetaccount) {
        this.parnetAccount = (parnetaccount == null ? null : parnetaccount.trim());
    }

    public String getUsertype() {
        return this.userType;
    }

    public void setUsertype(String usertype) {
        this.userType = (usertype == null ? null : usertype.trim());
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = (code == null ? null : code.trim());
    }

    public Integer getValidtime() {
        return this.validTime;
    }

    public void setValidtime(Integer validtime) {
        this.validTime = validtime;
    }

    public Integer getBonusgroupid() {
        return this.bonusGroupid;
    }

    public void setBonusgroupid(Integer bonusgroupid) {
        this.bonusGroupid = bonusgroupid;
    }

    public BigDecimal getRebateratio() {
        return this.rebateRatio;
    }

    public void setRebateratio(BigDecimal rebateratio) {
        this.rebateRatio = rebateratio;
    }

    public String getExtaddress() {
        return this.extAddress;
    }

    public void setExtaddress(String extaddress) {
        this.extAddress = (extaddress == null ? null : extaddress.trim());
    }

    public String getQq() {
        return this.qq;
    }

    public void setQq(String qq) {
        this.qq = (qq == null ? null : qq.trim());
    }

    public Date getCreatetime() {
        return this.createTime;
    }

    public void setCreatetime(Date createtime) {
        this.createTime = createtime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = (status == null ? null : status.trim());
    }

    public String getCanregists() {
        return this.canRegists;
    }

    public void setCanregists(String canregists) {
        this.canRegists = (canregists == null ? null : canregists.trim());
    }
}
