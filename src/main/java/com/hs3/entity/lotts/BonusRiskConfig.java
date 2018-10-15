package com.hs3.entity.lotts;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

/**
 * program: java-kernal
 * des:奖金风控配置
 * author: Terra
 * create: 2018-06-26 10:09
 **/
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BonusRiskConfig {


    private Integer id;
    /**
     * 彩种id
     */
    private String lotteryId;
    private String lotteryName;
    /**
     * 日均流水
     */
    private BigDecimal dayFlowWater;
    /**
     * 毛利率
     */
    private BigDecimal revenueRate;
    /**
     * 亏损下限
     */
    private BigDecimal loseRate;
    /**
     * 是否可用，0.否，1.是，默认1
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;
    /**
     * 奖池初始化金额
     */
    private BigDecimal initBonusPool;
    /**
     * 奖池回收迭代次数
     */
    private Integer collectCount;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public BigDecimal getDayFlowWater() {
        return dayFlowWater;
    }

    public void setDayFlowWater(BigDecimal dayFlowWater) {
        this.dayFlowWater = dayFlowWater;
    }

    public BigDecimal getRevenueRate() {
        return revenueRate;
    }

    public void setRevenueRate(BigDecimal revenueRate) {
        this.revenueRate = revenueRate;
    }

    public BigDecimal getLoseRate() {
        return loseRate;
    }

    public void setLoseRate(BigDecimal loseRate) {
        this.loseRate = loseRate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public BigDecimal getInitBonusPool() {
        return initBonusPool;
    }

    public void setInitBonusPool(BigDecimal initBonusPool) {
        this.initBonusPool = initBonusPool;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }
}
