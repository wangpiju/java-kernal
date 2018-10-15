package com.hs3.entity.lotts;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LotterySaleTime
        implements Serializable {
    private static final long serialVersionUID = 846281233720553667L;
    private String seasonId;
    private String lotteryId;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date beginTime;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date endTime;
    private Integer openStatus;
    private Integer settleStatus;
    private Integer planStatus;
    private Date curDate;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date openTime;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date openAfterTime;

    public String getSeasonId() {
        return this.seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
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

    public Integer getOpenStatus() {
        return this.openStatus;
    }

    public void setOpenStatus(Integer openStatus) {
        this.openStatus = openStatus;
    }

    public Integer getSettleStatus() {
        return this.settleStatus;
    }

    public void setSettleStatus(Integer settleStatus) {
        this.settleStatus = settleStatus;
    }

    public Integer getPlanStatus() {
        return this.planStatus;
    }

    public void setPlanStatus(Integer planStatus) {
        this.planStatus = planStatus;
    }

    public Date getCurDate() {
        return this.curDate;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
    }

    public Date getOpenTime() {
        return this.openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getOpenAfterTime() {
        return this.openAfterTime;
    }

    public void setOpenAfterTime(Date openAfterTime) {
        this.openAfterTime = openAfterTime;
    }
}
