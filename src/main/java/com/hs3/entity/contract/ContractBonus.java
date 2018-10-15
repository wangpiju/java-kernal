package com.hs3.entity.contract;

import com.hs3.json.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

public class ContractBonus {
    private Integer id;
    private String userMark;
    private String account;
    private String parentAccount;
    private String rootAccount;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date startDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date endDate;
    private BigDecimal cumulativeSales;
    private BigDecimal cumulativeProfit;
    private BigDecimal dividendAmount;
    private Double dividend;
    private Integer status;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserMark() {
        return this.userMark;
    }

    public void setUserMark(String userMark) {
        this.userMark = userMark;
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

    public String getRootAccount() {
        return this.rootAccount;
    }

    public void setRootAccount(String rootAccount) {
        this.rootAccount = rootAccount;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getCumulativeSales() {
        return this.cumulativeSales;
    }

    public void setCumulativeSales(BigDecimal cumulativeSales) {
        this.cumulativeSales = cumulativeSales;
    }

    public BigDecimal getCumulativeProfit() {
        return this.cumulativeProfit;
    }

    public void setCumulativeProfit(BigDecimal cumulativeProfit) {
        this.cumulativeProfit = cumulativeProfit;
    }

    public BigDecimal getDividendAmount() {
        return this.dividendAmount;
    }

    public void setDividendAmount(BigDecimal dividendAmount) {
        this.dividendAmount = dividendAmount;
    }

    public Double getDividend() {
        return this.dividend;
    }

    public void setDividend(Double dividend) {
        this.dividend = dividend;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
