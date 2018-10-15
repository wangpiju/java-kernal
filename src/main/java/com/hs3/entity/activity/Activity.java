package com.hs3.entity.activity;

import com.hs3.json.JsonDateTimeSerializer;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Activity {
    private Integer id;
    private String activityType;
    private String icon;
    private String title;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date beginTime;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date endTime;
    private Integer status;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date beginPrizeTime;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date endPrizeTime;
    private Integer activityUser;
    private Integer prizeType;
    private Integer useRange;
    private Integer amountType;
    private BigDecimal amount;
    private BigDecimal maxAmount;
    private String remark;
    private Integer needAttend;
    private Integer needPrize;
    private Integer visibleRange;
    private Integer orderId;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date beginRegTime;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date endRegTime;
    private String changeRemark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getBeginPrizeTime() {
        return this.beginPrizeTime;
    }

    public void setBeginPrizeTime(Date beginPrizeTime) {
        this.beginPrizeTime = beginPrizeTime;
    }

    public Date getEndPrizeTime() {
        return this.endPrizeTime;
    }

    public void setEndPrizeTime(Date endPrizeTime) {
        this.endPrizeTime = endPrizeTime;
    }

    public Integer getActivityUser() {
        return this.activityUser;
    }

    public void setActivityUser(Integer activityUser) {
        this.activityUser = activityUser;
    }

    public Integer getPrizeType() {
        return this.prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public Integer getUseRange() {
        return this.useRange;
    }

    public void setUseRange(Integer useRange) {
        this.useRange = useRange;
    }

    public Integer getAmountType() {
        return this.amountType;
    }

    public void setAmountType(Integer amountType) {
        this.amountType = amountType;
    }

    public BigDecimal getMaxAmount() {
        return this.maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getNeedAttend() {
        return this.needAttend;
    }

    public void setNeedAttend(Integer needAttend) {
        this.needAttend = needAttend;
    }

    public Integer getNeedPrize() {
        return this.needPrize;
    }

    public void setNeedPrize(Integer needPrize) {
        this.needPrize = needPrize;
    }

    public Integer getVisibleRange() {
        return this.visibleRange;
    }

    public void setVisibleRange(Integer visibleRange) {
        this.visibleRange = visibleRange;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getBeginRegTime() {
        return this.beginRegTime;
    }

    public void setBeginRegTime(Date beginRegTime) {
        this.beginRegTime = beginRegTime;
    }

    public Date getEndRegTime() {
        return this.endRegTime;
    }

    public void setEndRegTime(Date endRegTime) {
        this.endRegTime = endRegTime;
    }

    public String getChangeRemark() {
        return this.changeRemark;
    }

    public void setChangeRemark(String changeRemark) {
        this.changeRemark = changeRemark;
    }
}
