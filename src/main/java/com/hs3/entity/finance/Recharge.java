package com.hs3.entity.finance;

import com.hs3.json.JsonDateSerializer;
import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Recharge {
    private String id;
    private Integer status;
    private BigDecimal amount;
    private Integer rechargeType;
    private String traceId;
    private Integer userMark;
    private String account;
    private Integer test;
    private String bankName;
    private String bankNameCode;
    private String card;
    private String address;
    private String niceName;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date lastTime;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date createDate;
    private String operator;
    private Integer receiveBankId;
    private String receiveBankName;
    private String receiveCard;
    private String receiveAddress;
    private String receiveNiceName;
    private String receiveLink;
    private String remark;
    private String serialNumber;
    private BigDecimal poundage;
    private BigDecimal realAmount;
    private String classKey;

    //**************************************以下为变更部分*****************************************
    private String checkCode;

    public String getClassKey() {
        return classKey;
    }

    public void setClassKey(String classKey) {
        this.classKey = classKey;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getRechargeType() {
        return this.rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
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

    public Integer getTest() {
        return this.test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNameCode() {
        return this.bankNameCode;
    }

    public void setBankNameCode(String bankNameCode) {
        this.bankNameCode = bankNameCode;
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

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastTime() {
        return this.lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getReceiveBankId() {
        return this.receiveBankId;
    }

    public void setReceiveBankId(Integer receiveBankId) {
        this.receiveBankId = receiveBankId;
    }

    public String getReceiveBankName() {
        return this.receiveBankName;
    }

    public void setReceiveBankName(String receiveBankName) {
        this.receiveBankName = receiveBankName;
    }

    public String getReceiveCard() {
        return this.receiveCard;
    }

    public void setReceiveCard(String receiveCard) {
        this.receiveCard = receiveCard;
    }

    public String getReceiveAddress() {
        return this.receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getReceiveNiceName() {
        return this.receiveNiceName;
    }

    public void setReceiveNiceName(String receiveNiceName) {
        this.receiveNiceName = receiveNiceName;
    }

    public String getReceiveLink() {
        return this.receiveLink;
    }

    public void setReceiveLink(String receiveLink) {
        this.receiveLink = receiveLink;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getPoundage() {
        return this.poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public BigDecimal getRealAmount() {
        return this.realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

}
