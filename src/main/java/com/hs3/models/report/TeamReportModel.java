package com.hs3.models.report;

import com.hs3.entity.report.TeamReport;
import com.hs3.json.JsonDateSerializer;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class TeamReportModel
        extends TeamReport {
    private BigDecimal marginDollar;
    private BigDecimal marginRatio;
    private BigDecimal profitRatio;
    private Integer status;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date begin;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date end;

    public BigDecimal getMarginDollar() {
        return this.marginDollar;
    }

    public void setMarginDollar(BigDecimal marginDollar) {
        this.marginDollar = marginDollar;
    }

    public BigDecimal getMarginRatio() {
        return this.marginRatio;
    }

    public void setMarginRatio(BigDecimal marginRatio) {
        this.marginRatio = marginRatio;
    }

    public BigDecimal getProfitRatio() {
        return this.profitRatio;
    }

    public void setProfitRatio(BigDecimal profitRatio) {
        this.profitRatio = profitRatio;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getBegin() {
        return this.begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return this.end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
