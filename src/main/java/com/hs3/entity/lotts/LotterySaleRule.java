package com.hs3.entity.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LotterySaleRule {
    private Integer id;
    private String lotteryId;
    private Integer status;
    private Integer openCycle;
    private Integer beforeClose;
    private Integer openAfter;
    private String weeks;
    private String saleTime;
    private String firstTime;
    private String lastTime;
    private Integer orderId;
    private Integer beforeDay;

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

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOpenCycle() {
        return this.openCycle;
    }

    public void setOpenCycle(Integer openCycle) {
        this.openCycle = openCycle;
    }

    public Integer getBeforeClose() {
        return this.beforeClose;
    }

    public void setBeforeClose(Integer beforeClose) {
        this.beforeClose = beforeClose;
    }

    public Integer getOpenAfter() {
        return this.openAfter;
    }

    public void setOpenAfter(Integer openAfter) {
        this.openAfter = openAfter;
    }

    public String getWeeks() {
        return this.weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getSaleTime() {
        return this.saleTime;
    }

    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime;
    }

    public String getFirstTime() {
        return this.firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getLastTime() {
        return this.lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getBeforeDay() {
        return this.beforeDay;
    }

    public void setBeforeDay(Integer beforeDay) {
        this.beforeDay = beforeDay;
    }
}
