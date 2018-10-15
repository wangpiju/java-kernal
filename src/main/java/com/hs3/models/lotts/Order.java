package com.hs3.models.lotts;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private String lotteryId;
    private Integer isTrace;
    private Integer traceWinStop;
    private List<OrderContent> order;
    private List<TraceOrder> traceOrders;
    private Integer count;
    private BigDecimal amount;
    private Integer bounsType;

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public Integer getIsTrace() {
        return this.isTrace;
    }

    public void setIsTrace(Integer isTrace) {
        this.isTrace = isTrace;
    }

    public Integer getTraceWinStop() {
        return this.traceWinStop;
    }

    public void setTraceWinStop(Integer traceWinStop) {
        this.traceWinStop = traceWinStop;
    }

    public List<OrderContent> getOrder() {
        return this.order;
    }

    public List<TraceOrder> getTraceOrders() {
        return this.traceOrders;
    }

    public void setTraceOrders(List<TraceOrder> traceOrders) {
        this.traceOrders = traceOrders;
    }

    public void setOrder(List<OrderContent> order) {
        this.order = order;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getBounsType() {
        return this.bounsType;
    }

    public void setBounsType(Integer bounsType) {
        this.bounsType = bounsType;
    }
}
