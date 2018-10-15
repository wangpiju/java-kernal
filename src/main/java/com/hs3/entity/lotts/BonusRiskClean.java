package com.hs3.entity.lotts;

import java.math.BigDecimal;
import java.util.Date;

/**
 * program: java-kernal
 * des: 奖池手动清除记录
 * author: Terra
 * create: 2018-08-25 15:51
 **/
public class BonusRiskClean {

    private int id;
    private String lotteryId;
    private BigDecimal cleanAmount;
    private BigDecimal beforeBonusPool;
    private BigDecimal afterBonusPool;
    private String operateUser;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public BigDecimal getBeforeBonusPool() {
        return beforeBonusPool;
    }

    public void setBeforeBonusPool(BigDecimal beforeBonusPool) {
        this.beforeBonusPool = beforeBonusPool;
    }

    public BigDecimal getAfterBonusPool() {
        return afterBonusPool;
    }

    public void setAfterBonusPool(BigDecimal afterBonusPool) {
        this.afterBonusPool = afterBonusPool;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getCleanAmount() {
        return cleanAmount;
    }

    public void setCleanAmount(BigDecimal cleanAmount) {
        this.cleanAmount = cleanAmount;
    }
}
