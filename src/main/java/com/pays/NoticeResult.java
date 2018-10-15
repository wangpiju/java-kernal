package com.pays;

import java.math.BigDecimal;

public class NoticeResult {
    private boolean status;
    private int notifyType = 0;
    private String orderId;
    private String apiOrderId;
    private BigDecimal amount;

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getNotifyType() {
        return this.notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    public String getApiOrderId() {
        return this.apiOrderId;
    }

    public void setApiOrderId(String apiOrderId) {
        this.apiOrderId = apiOrderId;
    }

    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
