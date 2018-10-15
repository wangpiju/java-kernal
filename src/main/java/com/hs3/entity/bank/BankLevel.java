package com.hs3.entity.bank;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BankLevel {
    private Integer id;
    private String title;
    private BigDecimal minAmount;
    private Integer minCount;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getMinAmount() {
        return this.minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getMinCount() {
        return this.minCount;
    }

    public void setMinCount(Integer minCount) {
        this.minCount = minCount;
    }
}
