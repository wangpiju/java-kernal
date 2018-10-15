package com.hs3.entity.sys;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

public class LogAll {
    private Integer id;
    private Long beginTime;
    private Long endTime;
    private Long duringTime;
    private Long maxMemory;
    private Long totalMemory;
    private Long freeMemory;
    private String account;
    private String remoteAddr;
    private String userAgent;
    private String requestUri;
    private String method;
    private String params;
    private String exception;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;
    private String k1;
    private String k2;
    private String k3;
    private String k4;
    private String k5;
    private String kext;
    private String c1;
    private String c2;
    private String c3;
    private String c4;
    private String c5;
    private String cext;
    private Integer resCode;


    private String extends1;
    private String extends2;
    private String extends3;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getDuringTime() {
        return this.duringTime;
    }

    public void setDuringTime(Long duringTime) {
        this.duringTime = duringTime;
    }

    public Long getMaxMemory() {
        return this.maxMemory;
    }

    public void setMaxMemory(Long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public Long getTotalMemory() {
        return this.totalMemory;
    }

    public void setTotalMemory(Long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Long getFreeMemory() {
        return this.freeMemory;
    }

    public void setFreeMemory(Long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRequestUri() {
        return this.requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return this.params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getException() {
        return this.exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getK1() {
        return this.k1;
    }

    public void setK1(String k1) {
        this.k1 = k1;
    }

    public String getK2() {
        return this.k2;
    }

    public void setK2(String k2) {
        this.k2 = k2;
    }

    public String getK3() {
        return this.k3;
    }

    public void setK3(String k3) {
        this.k3 = k3;
    }

    public String getK4() {
        return this.k4;
    }

    public void setK4(String k4) {
        this.k4 = k4;
    }

    public String getK5() {
        return this.k5;
    }

    public void setK5(String k5) {
        this.k5 = k5;
    }

    public String getKext() {
        return this.kext;
    }

    public void setKext(String kext) {
        this.kext = kext;
    }

    public String getC1() {
        return this.c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return this.c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public String getC3() {
        return this.c3;
    }

    public void setC3(String c3) {
        this.c3 = c3;
    }

    public String getC4() {
        return this.c4;
    }

    public void setC4(String c4) {
        this.c4 = c4;
    }

    public String getC5() {
        return this.c5;
    }

    public void setC5(String c5) {
        this.c5 = c5;
    }

    public String getCext() {
        return this.cext;
    }

    public void setCext(String cext) {
        this.cext = cext;
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

    public String getExtends3() {
        return this.extends3;
    }

    public void setExtends3(String extends3) {
        this.extends3 = extends3;
    }
}
