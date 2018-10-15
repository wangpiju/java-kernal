package com.hs3.entity.userAgents;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserAgentsDividendLottery implements Serializable {

    private Integer id;
    private String programName; //方案名称
    private String lotteryId; //彩种ID
    private String lotteryName; //彩种名称
    private BigDecimal negativeProfit; //负盈利（元）— 不含代理本人
    private BigDecimal proportion;  //日工资比例
    private Integer activeNumber;   //活跃人数
    private Integer cycle;  //计算周期 1：半月、2：月
    private Integer status; //状态 0：关闭、1：启用
    private String hint;    //提示语

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public BigDecimal getNegativeProfit() {
        return negativeProfit;
    }

    public void setNegativeProfit(BigDecimal negativeProfit) {
        this.negativeProfit = negativeProfit;
    }

    public BigDecimal getProportion() {
        return proportion;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }

    public Integer getActiveNumber() {
        return activeNumber;
    }

    public void setActiveNumber(Integer activeNumber) {
        this.activeNumber = activeNumber;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
