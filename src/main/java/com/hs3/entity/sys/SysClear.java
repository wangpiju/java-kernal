package com.hs3.entity.sys;

import java.io.Serializable;
import java.math.BigDecimal;

public class SysClear
        implements Serializable {
    private static final long serialVersionUID = 203526829173690723L;
    private Integer id;
    private String category;
    private String title;
    private Integer job;
    private Integer beforeDays;
    private Integer beforeDaysDefault;
    private String executeTime;
    private Integer status;
    private Integer clearMode;
    private BigDecimal deleteMinAmount;
    private BigDecimal deleteMaxAmount;
    private BigDecimal freezeMixAmount;
    private BigDecimal freezeMaxAmount;
    private String extends1;
    private String extends2;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getJob() {
        return this.job;
    }

    public void setJob(Integer job) {
        this.job = job;
    }

    public Integer getBeforeDays() {
        return this.beforeDays;
    }

    public void setBeforeDays(Integer beforeDays) {
        this.beforeDays = beforeDays;
    }

    public Integer getBeforeDaysDefault() {
        return this.beforeDaysDefault;
    }

    public void setBeforeDaysDefault(Integer beforeDaysDefault) {
        this.beforeDaysDefault = beforeDaysDefault;
    }

    public String getExecuteTime() {
        return this.executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getClearMode() {
        return this.clearMode;
    }

    public void setClearMode(Integer clearMode) {
        this.clearMode = clearMode;
    }

    public BigDecimal getDeleteMinAmount() {
        return this.deleteMinAmount;
    }

    public void setDeleteMinAmount(BigDecimal deleteMinAmount) {
        this.deleteMinAmount = deleteMinAmount;
    }

    public BigDecimal getDeleteMaxAmount() {
        return this.deleteMaxAmount;
    }

    public void setDeleteMaxAmount(BigDecimal deleteMaxAmount) {
        this.deleteMaxAmount = deleteMaxAmount;
    }

    public BigDecimal getFreezeMixAmount() {
        return this.freezeMixAmount;
    }

    public void setFreezeMixAmount(BigDecimal freezeMixAmount) {
        this.freezeMixAmount = freezeMixAmount;
    }

    public BigDecimal getFreezeMaxAmount() {
        return this.freezeMaxAmount;
    }

    public void setFreezeMaxAmount(BigDecimal freezeMaxAmount) {
        this.freezeMaxAmount = freezeMaxAmount;
    }

    public String getExtends1() {
        return this.extends1;
    }

    public void setExtends1(String extends1) {
        this.extends1 = extends1;
    }

    public String getExtends2() {
        return this.extends2;
    }

    public void setExtends2(String extends2) {
        this.extends2 = extends2;
    }
}
