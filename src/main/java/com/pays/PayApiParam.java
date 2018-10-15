package com.pays;

public class PayApiParam
        extends ApiParam {
    private String amount;
    private String bank;
    private String payNoticeUrl;
    private String shopNoticeUrl;
    private String payReturnUrl;
    private String shopReturnUrl;
    private String domain;
    private String orderId;
    private String ip;
    private Boolean isMobile = false;
    private String traceId;
    private String depositMode;

    public PayApiParam() {
    }

    public PayApiParam(String amount, String bank, String shopUrl, String payNoticeUrl, String shopNoticeUrl, String payReturnUrl, String shopReturnUrl, String domain, String orderId, String merchantCode, String key, String publicKey, String ip, String email, Boolean isCredit, Boolean isMobile, String traceId) {
        this.amount = amount;
        this.bank = bank;
        this.shopUrl = shopUrl;
        this.payNoticeUrl = payNoticeUrl;
        this.shopNoticeUrl = shopNoticeUrl;
        this.payReturnUrl = payReturnUrl;
        this.shopReturnUrl = shopReturnUrl;
        this.domain = domain;
        this.orderId = orderId;
        this.merchantCode = merchantCode;
        this.key = key;
        this.publicKey = publicKey;
        this.ip = ip;
        this.email = email;
        this.isCredit = isCredit;
        this.isMobile = isMobile;
        this.traceId = traceId;
    }

    public PayApiParam(String amount, String bank, String shopUrl, String payNoticeUrl, String shopNoticeUrl, String payReturnUrl, String shopReturnUrl, String domain, String orderId, String merchantCode, String key, String publicKey, String ip, String email, Boolean isCredit, Boolean isMobile, String traceId, String depositMode) {
        this.amount = amount;
        this.bank = bank;
        this.shopUrl = shopUrl;
        this.payNoticeUrl = payNoticeUrl;
        this.shopNoticeUrl = shopNoticeUrl;
        this.payReturnUrl = payReturnUrl;
        this.shopReturnUrl = shopReturnUrl;
        this.domain = domain;
        this.orderId = orderId;
        this.merchantCode = merchantCode;
        this.key = key;
        this.publicKey = publicKey;
        this.ip = ip;
        this.email = email;
        this.isCredit = isCredit;
        this.isMobile = isMobile;
        this.traceId = traceId;
        this.depositMode = depositMode;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBank() {
        return this.bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getPayNoticeUrl() {
        return this.payNoticeUrl;
    }

    public void setPayNoticeUrl(String payNoticeUrl) {
        this.payNoticeUrl = payNoticeUrl;
    }

    public String getShopNoticeUrl() {
        return this.shopNoticeUrl;
    }

    public void setShopNoticeUrl(String shopNoticeUrl) {
        this.shopNoticeUrl = shopNoticeUrl;
    }

    public String getPayReturnUrl() {
        return this.payReturnUrl;
    }

    public void setPayReturnUrl(String payReturnUrl) {
        this.payReturnUrl = payReturnUrl;
    }

    public String getShopReturnUrl() {
        return this.shopReturnUrl;
    }

    public void setShopReturnUrl(String shopReturnUrl) {
        this.shopReturnUrl = shopReturnUrl;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getIsMobile() {
        return this.isMobile;
    }

    public void setIsMobile(Boolean isMobile) {
        this.isMobile = isMobile;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getDepositMode() {
        return depositMode;
    }

    public void setDepositMode(String depositMode) {
        this.depositMode = depositMode;
    }
}
