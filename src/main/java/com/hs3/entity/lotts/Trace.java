package com.hs3.entity.lotts;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Trace
        implements Cloneable {
    private String id;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;
    private Integer userMark;
    private String account;
    private String lotteryId;
    private String lotteryName;
    private String startSeason;
    private Integer traceNum;
    private Integer finishTraceNum;
    private Integer cancelTraceNum;
    private BigDecimal traceAmount;
    private BigDecimal finishTraceAmount;
    private BigDecimal cancelTraceAmount;
    private Integer isWinStop;
    private Integer status;
    private Integer test;
    private BigDecimal winAmount;

    public Object clone()
            throws CloneNotSupportedException {
        return super.clone();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserMark() {
        return this.userMark;
    }

    public void setUserMark(Integer userMark) {
        this.userMark = userMark;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getLotteryName() {
        return this.lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public String getStartSeason() {
        return this.startSeason;
    }

    public void setStartSeason(String startSeason) {
        this.startSeason = startSeason;
    }

    public Integer getTraceNum() {
        return this.traceNum;
    }

    public void setTraceNum(Integer traceNum) {
        this.traceNum = traceNum;
    }

    public Integer getFinishTraceNum() {
        return this.finishTraceNum;
    }

    public void setFinishTraceNum(Integer finishTraceNum) {
        this.finishTraceNum = finishTraceNum;
    }

    public Integer getCancelTraceNum() {
        return this.cancelTraceNum;
    }

    public void setCancelTraceNum(Integer cancelTraceNum) {
        this.cancelTraceNum = cancelTraceNum;
    }

    public Integer getIsWinStop() {
        return this.isWinStop;
    }

    public void setIsWinStop(Integer isWinStop) {
        this.isWinStop = isWinStop;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTest() {
        return this.test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public BigDecimal getTraceAmount() {
        return this.traceAmount;
    }

    public void setTraceAmount(BigDecimal traceAmount) {
        this.traceAmount = traceAmount;
    }

    public BigDecimal getFinishTraceAmount() {
        return this.finishTraceAmount;
    }

    public void setFinishTraceAmount(BigDecimal finishTraceAmount) {
        this.finishTraceAmount = finishTraceAmount;
    }

    public BigDecimal getCancelTraceAmount() {
        return this.cancelTraceAmount;
    }

    public void setCancelTraceAmount(BigDecimal cancelTraceAmount) {
        this.cancelTraceAmount = cancelTraceAmount;
    }

    public BigDecimal getWinAmount() {
        return this.winAmount;
    }

    public void setWinAmount(BigDecimal winAmount) {
        this.winAmount = winAmount;
    }
}
