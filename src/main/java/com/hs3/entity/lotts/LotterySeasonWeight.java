package com.hs3.entity.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LotterySeasonWeight {
    private Integer id;
    private String lotteryId;
    private String weightType;
    private String seasonId;
    private String nums;
    private Integer weight;
    private Date createTime;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getWeightType() {
        return this.weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    public String getSeasonId() {
        return this.seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getNums() {
        return this.nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
