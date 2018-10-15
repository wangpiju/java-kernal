package com.hs3.entity.userAgents;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserAgentsDividendLotteryMg implements Serializable {

    private Integer id;
    private String reportDate;  //日期
    private String account; //账户
    private String parentAccount;   //上级
    private String lotteryId; //彩种ID
    private String lotteryName; //彩种名称
    private BigDecimal distributionAmount;  //派发金额
    private BigDecimal actualNegativeProfit; //负盈利（元）— 不含代理本人
    private Integer actualActiveNumber;   //活跃人数
    private Integer programId;  //方案ID
    private String programName; //方案名称
    private BigDecimal negativeProfit; //方案负盈利（元）— 不含代理本人
    private BigDecimal proportion;  //方案周期分红比例
    private Integer activeNumber;   //方案活跃人数
    private Integer cycle;  //方案计算周期 1：半月、2：月
    private Integer status; //状态 0：待处理、1：派发、2：不予派发
    private Date createTime;    //创建时间
    private Date modifyTime;    //修改时间
    private String operator;    //操作员
    private String remarks;     //备注

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
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

    public BigDecimal getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(BigDecimal distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    public BigDecimal getActualNegativeProfit() {
        return actualNegativeProfit;
    }

    public void setActualNegativeProfit(BigDecimal actualNegativeProfit) {
        this.actualNegativeProfit = actualNegativeProfit;
    }

    public Integer getActualActiveNumber() {
        return actualActiveNumber;
    }

    public void setActualActiveNumber(Integer actualActiveNumber) {
        this.actualActiveNumber = actualActiveNumber;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
