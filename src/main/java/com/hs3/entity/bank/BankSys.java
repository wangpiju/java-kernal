package com.hs3.entity.bank;

import com.hs3.json.JsonDateTimeSerializer;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BankSys {
    private Integer id;
    private Integer nameId;
    private String card;
    private String niceName;
    private String address;
    private Integer levelId;
    private BigDecimal rechargeAmount;
    private Integer rechargeNum;
    private Integer status;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;
    private Integer crossStatus;
    private String remark;
    private String sign;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNameId() {
        return this.nameId;
    }

    public void setNameId(Integer nameId) {
        this.nameId = nameId;
    }

    public String getCard() {
        return this.card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getNiceName() {
        return this.niceName;
    }

    public void setNiceName(String niceName) {
        this.niceName = niceName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getLevelId() {
        return this.levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public BigDecimal getRechargeAmount() {
        return this.rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Integer getRechargeNum() {
        return this.rechargeNum;
    }

    public void setRechargeNum(Integer rechargeNum) {
        this.rechargeNum = rechargeNum;
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

    public Integer getCrossStatus() {
        return this.crossStatus;
    }

    public void setCrossStatus(Integer crossStatus) {
        this.crossStatus = crossStatus;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
