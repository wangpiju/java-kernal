package com.hs3.entity.finance;

import com.hs3.json.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RechargeReport {
    private Integer id;
    private String receiveName;
    private String receiveCard;
    private BigDecimal amount;
    private BigDecimal poundage;
    private BigDecimal realAmount;
    private Integer num;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date createDate;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReceiveName() {
        return this.receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveCard() {
        return this.receiveCard;
    }

    public void setReceiveCard(String receiveCard) {
        this.receiveCard = receiveCard;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Integer getNum() {
        return this.num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
