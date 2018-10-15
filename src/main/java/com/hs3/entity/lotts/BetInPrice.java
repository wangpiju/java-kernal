package com.hs3.entity.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BetInPrice {
    private Integer id;
    private BigDecimal start;
    private BigDecimal end;
    private Integer surplusNum;
    private Integer warnNum;
    private BigDecimal totalAmount;
    private Integer addNum;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getStart() {
        return this.start;
    }

    public void setStart(BigDecimal start) {
        this.start = start;
    }

    public BigDecimal getEnd() {
        return this.end;
    }

    public void setEnd(BigDecimal end) {
        this.end = end;
    }

    public Integer getSurplusNum() {
        return this.surplusNum;
    }

    public void setSurplusNum(Integer surplusNum) {
        this.surplusNum = surplusNum;
    }

    public Integer getWarnNum() {
        return this.warnNum;
    }

    public void setWarnNum(Integer warnNum) {
        this.warnNum = warnNum;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getAddNum() {
        return this.addNum;
    }

    public void setAddNum(Integer addNum) {
        this.addNum = addNum;
    }
}
