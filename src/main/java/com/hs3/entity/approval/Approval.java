package com.hs3.entity.approval;

import com.hs3.json.JsonDateTimeSerializer;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Approval {
    private Integer id;
    private Integer status;
    private String userName;
    private Integer userIdentify;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date handleTime;
    private String applyContent;
    private BigDecimal amount;
    private String password;
    private String operator;
    private String remark;
    private Integer applyType;
    private String rechargeId;
    private String receiveCard;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserIdentify() {
        return this.userIdentify;
    }

    public void setUserIdentify(Integer userIdentify) {
        this.userIdentify = userIdentify;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getHandleTime() {
        return this.handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getApplyContent() {
        return this.applyContent;
    }

    public void setApplyContent(String applyContent) {
        this.applyContent = applyContent;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getApplyType() {
        return this.applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public String getRechargeId() {
        return this.rechargeId;
    }

    public void setRechargeId(String rechargeId) {
        this.rechargeId = rechargeId;
    }

    public String getReceiveCard() {
        return this.receiveCard;
    }

    public void setReceiveCard(String receiveCard) {
        this.receiveCard = receiveCard;
    }
}
