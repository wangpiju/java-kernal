package com.hs3.entity.bank;

import com.hs3.json.JsonDateTimeSerializer;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BankApi {
    private Integer id;
    private String title;
    private Integer orderId;
    private String classKey;
    private String merchantCode;
    private String email;
    private String sign;
    private String publicKey;
    private String levelId;
    private String specialAccount;
    private String proxyLine;
    private Integer status;
    private BigDecimal rechargeAmount;
    private Integer rechargeNum;
    private String twoCode;
    private String icoCode;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String shopUrl;
    private Integer isCredit;
    private Integer isSupportMobile;
    private Integer articleId;
    private String remark;
    private BigDecimal poundage;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;
    private Integer depositMode;


    public boolean isCredit() {
        return (this.isCredit != null) && (this.isCredit.intValue() == 1);
    }

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

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getMerchantCode() {
        return this.merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getLevelId() {
        return this.levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public void setLevelIds(Integer[] levelIds) {
        setLevelId(null);
        if ((levelIds != null) && (levelIds.length > 0)) {
            String s = "";
            for (Integer levelId : levelIds) {
                s = s + "," + levelId;
            }
            setLevelId(s.substring(1));
        }
    }

    public Integer[] getLevelIds() {
        Integer[] levelIds = null;
        if (this.levelId != null) {
            String[] ss = this.levelId.split("\\,");
            levelIds = new Integer[ss.length];
            for (int i = 0; i < levelIds.length; i++) {
                levelIds[i] = Integer.valueOf(Integer.parseInt(ss[i]));
            }
        }
        return levelIds;
    }

    public String getSpecialAccount() {
        return this.specialAccount;
    }

    public void setSpecialAccount(String specialAccount) {
        this.specialAccount = specialAccount;
    }

    public String getProxyLine() {
        return this.proxyLine;
    }

    public void setProxyLine(String proxyLine) {
        this.proxyLine = proxyLine;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getRechargeAmount() {
        return this.rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Integer getRechargeNum() {
        return this.rechargeNum;
    }

    public void setRechargeNum(Integer rechargeNum) {
        this.rechargeNum = rechargeNum;
    }

    public String getClassKey() {
        return this.classKey;
    }

    public void setClassKey(String classKey) {
        this.classKey = classKey;
    }

    public String getTwoCode() {
        return this.twoCode;
    }

    public void setTwoCode(String twoCode) {
        this.twoCode = twoCode;
    }

    public String getIcoCode() {
        return this.icoCode;
    }

    public void setIcoCode(String icoCode) {
        this.icoCode = icoCode;
    }

    public BigDecimal getMinAmount() {
        return this.minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return this.maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getShopUrl() {
        return this.shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public Integer getIsCredit() {
        return this.isCredit;
    }

    public void setIsCredit(Integer isCredit) {
        this.isCredit = isCredit;
    }

    public Integer getIsSupportMobile() {
        return this.isSupportMobile;
    }

    public void setIsSupportMobile(Integer isSupportMobile) {
        this.isSupportMobile = isSupportMobile;
    }

    public Integer getArticleId() {
        return this.articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getPoundage() {
        return this.poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public Integer getDepositMode() {
        return depositMode;
    }

    public void setDepositMode(Integer depositMode) {
        this.depositMode = depositMode;
    }
}
