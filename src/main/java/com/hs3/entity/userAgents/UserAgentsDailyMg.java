package com.hs3.entity.userAgents;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserAgentsDailyMg implements Serializable {

    private Integer id;
    private String reportDate;  //日期
    private String account; //账户
    private String parentAccount;   //上级
    private BigDecimal distributionAmount;  //派发金额
    private BigDecimal actualSalesVolume; //团队销量（元）— 不含代理本人
    private Integer actualActiveNumber;   //活跃人数
    private Integer programId;  //方案ID
    private String programName; //方案名称
    private BigDecimal salesVolume; //方案团队销量（元）— 不含代理本人
    private BigDecimal proportion;  //方案日工资比例
    private Integer activeNumber;   //方案活跃人数
    private Integer cycle;  //方案计算周期
    private Integer status; //状态 0：待处理、1：派发、2：不予派发
    private Date createTime;    //创建时间
    private Date modifyTime;    //修改时间
    private String operator;    //操作员
    private String remarks;     //备注

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

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

    public BigDecimal getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(BigDecimal distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    public BigDecimal getActualSalesVolume() {
        return actualSalesVolume;
    }

    public void setActualSalesVolume(BigDecimal actualSalesVolume) {
        this.actualSalesVolume = actualSalesVolume;
    }

    public Integer getActualActiveNumber() {
        return actualActiveNumber;
    }

    public void setActualActiveNumber(Integer actualActiveNumber) {
        this.actualActiveNumber = actualActiveNumber;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public BigDecimal getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(BigDecimal salesVolume) {
        this.salesVolume = salesVolume;
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
