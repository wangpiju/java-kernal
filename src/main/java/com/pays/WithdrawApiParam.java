package com.pays;

public class WithdrawApiParam
        extends ApiParam {
    private String amount;
    private String bank;
    private String orderId;
    private String cardNum;
    private String cardName;
    private String account;
    private String issueBankName;
    private String issueBankAddress;
    private String memo;
    private String withdrawUrl;
    private String extends1;
    private String extends2;
    private String extends3;

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("amount=").append(this.amount).append(",");
        sb.append("bank=").append(this.bank).append(",");
        sb.append("orderId=").append(this.orderId).append(",");
        sb.append("cardNum=").append(this.cardNum).append(",");
        sb.append("cardName=").append(this.cardName).append(",");
        sb.append("account=").append(this.account).append(",");
        sb.append("issueBankName=").append(this.issueBankName).append(",");
        sb.append("issueBankAddress=").append(this.issueBankAddress).append(",");
        sb.append("memo=").append(this.memo).append(",");
        sb.append("shopUrl=").append(this.shopUrl).append(",");
        sb.append("merchantCode=").append(this.merchantCode).append(",");
        sb.append("email=").append(this.email).append(",");
        sb.append("isCredit=").append(this.isCredit).append(",");
        sb.append("extends1=").append(this.extends1).append(",");
        sb.append("extends2=").append(this.extends2).append(",");
        sb.append("extends3=").append(this.extends3);
        sb.append("}");

        return sb.toString();
    }

    public WithdrawApiParam() {
    }

    public WithdrawApiParam(String amount, String bank, String orderId, String cardNum, String cardName, String account, String issueBankName, String issueBankAddress, String memo) {
        this.amount = amount;
        this.bank = bank;
        this.orderId = orderId;
        this.cardNum = cardNum;
        this.cardName = cardName;
        this.account = account;
        this.issueBankName = issueBankName;
        this.issueBankAddress = issueBankAddress;
        this.memo = memo;
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

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCardNum() {
        return this.cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardName() {
        return this.cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIssueBankName() {
        return this.issueBankName;
    }

    public void setIssueBankName(String issueBankName) {
        this.issueBankName = issueBankName;
    }

    public String getIssueBankAddress() {
        return this.issueBankAddress;
    }

    public void setIssueBankAddress(String issueBankAddress) {
        this.issueBankAddress = issueBankAddress;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getWithdrawUrl() {
        return this.withdrawUrl;
    }

    public void setWithdrawUrl(String withdrawUrl) {
        this.withdrawUrl = withdrawUrl;
    }

    public String getExtends1() {
        return this.extends1;
    }

    public void setExtends1(String extends1) {
        this.extends1 = extends1;
    }

    public String getExtends2() {
        return this.extends2;
    }

    public void setExtends2(String extends2) {
        this.extends2 = extends2;
    }

    public String getExtends3() {
        return this.extends3;
    }

    public void setExtends3(String extends3) {
        this.extends3 = extends3;
    }
}
