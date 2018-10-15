package com.hs3.entity.finance;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FinanceChange {
    private Integer id;
    private String changeUser;
    private Integer test;
    private String financeId;
    private Integer accountChangeTypeId;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date changeTime;
    private BigDecimal changeAmount;
    private BigDecimal balance;
    private Integer status;
    private String remark;
    private String operator;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTest() {
        return this.test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public String getChangeUser() {
        return this.changeUser;
    }

    public void setChangeUser(String changeUser) {
        this.changeUser = changeUser;
    }

    public String getFinanceId() {
        return this.financeId;
    }

    public void setFinanceId(String financeId) {
        this.financeId = financeId;
    }

    public Integer getAccountChangeTypeId() {
        return this.accountChangeTypeId;
    }

    public void setAccountChangeTypeId(Integer accountChangeTypeId) {
        this.accountChangeTypeId = accountChangeTypeId;
    }

    public Date getChangeTime() {
        return this.changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public BigDecimal getChangeAmount() {
        return this.changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
