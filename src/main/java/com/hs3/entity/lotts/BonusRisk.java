package com.hs3.entity.lotts;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * program: java-kernal
 * des:奖金风控
 * author: Terra
 * create: 2018-06-26 10:09
 **/
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BonusRisk {


    private Long id;

    private String lotteryId;

    private String seasonId;

    private String initNum;

    private String endNum;

    private Integer createNumCount = 0;

    private BigDecimal validBetAmount = BigDecimal.ZERO;

    private BigDecimal rebate =  BigDecimal.ZERO;

    private BigDecimal initBonus = BigDecimal.ZERO;

    private BigDecimal initGains = BigDecimal.ZERO;

    private BigDecimal endBonus = BigDecimal.ZERO;

    private BigDecimal endGains = BigDecimal.ZERO;

    private BigDecimal bonusPoolLeft = BigDecimal.ZERO;

    private BigDecimal secondBonusPoolLeft = BigDecimal.ZERO;


    private Boolean stopOpen = Boolean.FALSE;


    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;

    private Integer isChangeNum;

    private Integer collectCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getInitNum() {
        return initNum;
    }

    public void setInitNum(String initNum) {
        this.initNum = initNum;
    }

    public String getEndNum() {
        return endNum;
    }

    public void setEndNum(String endNum) {
        this.endNum = endNum;
    }

    public Integer getCreateNumCount() {
        return createNumCount;
    }

    public void setCreateNumCount(Integer createNumCount) {
        this.createNumCount = createNumCount;
    }

    public BigDecimal getValidBetAmount() {
        return validBetAmount;
    }

    public void setValidBetAmount(BigDecimal validBetAmount) {
        this.validBetAmount = validBetAmount;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public BigDecimal getInitBonus() {
        return initBonus;
    }

    public void setInitBonus(BigDecimal initBonus) {
        this.initBonus = initBonus;
    }

    public BigDecimal getInitGains() {
        return initGains;
    }

    public void setInitGains(BigDecimal initGains) {
        this.initGains = initGains;
    }

    public BigDecimal getEndBonus() {
        return endBonus;
    }

    public void setEndBonus(BigDecimal endBonus) {
        this.endBonus = endBonus;
    }

    public BigDecimal getEndGains() {
        return endGains;
    }

    public void setEndGains(BigDecimal endGains) {
        this.endGains = endGains;
    }

    public BigDecimal getBonusPoolLeft() {
        return bonusPoolLeft;
    }

    public void setBonusPoolLeft(BigDecimal bonusPoolLeft) {
        this.bonusPoolLeft = bonusPoolLeft;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public Integer getIsChangeNum() {
        return isChangeNum;
    }

    public void setIsChangeNum(Integer isChangeNum) {
        this.isChangeNum = isChangeNum;
    }

    public BigDecimal getSecondBonusPoolLeft() {
        return secondBonusPoolLeft;
    }

    public void setSecondBonusPoolLeft(BigDecimal secondBonusPoolLeft) {
        this.secondBonusPoolLeft = secondBonusPoolLeft;
    }

    public Boolean getStopOpen() {
        return stopOpen;
    }

    public void setStopOpen(Boolean stopOpen) {
        this.stopOpen = stopOpen;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }
}
