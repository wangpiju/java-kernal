package com.hs3.entity.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Lottery {
    private String id;
    private String title;
    private Integer status;
    private Integer orderId;
    private String groupName;
    private String groupId;
    private String seasonRule;
    private Integer maxPlan;
    private Integer weight;
    private Integer isHot;
    private Integer isSelf;
    private Integer isNew;
    private Integer showGroup;
    private Integer mobileStatus;
    private Integer betInStatus;
    private Integer ReSettleTime;
    private Integer lotteryGroupId;
    private String remark;

    public Integer getReSettleTime() {
        return this.ReSettleTime;
    }

    public void setReSettleTime(Integer reSettleTime) {
        this.ReSettleTime = reSettleTime;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSeasonRule() {
        return this.seasonRule;
    }

    public void setSeasonRule(String seasonRule) {
        this.seasonRule = seasonRule;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getMaxPlan() {
        return this.maxPlan;
    }

    public void setMaxPlan(Integer maxPlan) {
        this.maxPlan = maxPlan;
    }

    public Integer getIsHot() {
        return this.isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public Integer getIsSelf() {
        return this.isSelf;
    }

    public void setIsSelf(Integer isSelf) {
        this.isSelf = isSelf;
    }

    public Integer getIsNew() {
        return this.isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getShowGroup() {
        return this.showGroup;
    }

    public void setShowGroup(Integer showGroup) {
        this.showGroup = showGroup;
    }

    public Integer getMobileStatus() {
        return this.mobileStatus;
    }

    public void setMobileStatus(Integer mobileStatus) {
        this.mobileStatus = mobileStatus;
    }

    public Integer getBetInStatus() {
        return this.betInStatus;
    }

    public void setBetInStatus(Integer betInStatus) {
        this.betInStatus = betInStatus;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getLotteryGroupId() {
        return this.lotteryGroupId;
    }

    public void setLotteryGroupId(Integer lotteryGroupId) {
        this.lotteryGroupId = lotteryGroupId;
    }
}
