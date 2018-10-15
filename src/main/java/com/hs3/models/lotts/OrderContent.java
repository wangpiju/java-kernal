package com.hs3.models.lotts;

import java.math.BigDecimal;

public class OrderContent {
    private String playId;
    private String content;
    private Integer betCount;
    private Integer price;
    private BigDecimal unit;

    public String getPlayId() {
        return this.playId;
    }

    public void setPlayId(String playId) {
        this.playId = playId;
    }

    public Integer getBetCount() {
        return this.betCount;
    }

    public void setBetCount(Integer betCount) {
        this.betCount = betCount;
    }

    public Integer getPrice() {
        return this.price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public BigDecimal getUnit() {
        return this.unit;
    }

    public void setUnit(BigDecimal unit) {
        this.unit = unit;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
