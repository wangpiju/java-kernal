package com.hs3.entity.numberSource;

import java.math.BigInteger;

public class Triggers {
    private String JOB_GROUP;
    private String JOB_NAME;
    private String TRIGGER_GROUP;
    private String TRIGGER_NAME;
    private BigInteger NEXT_FIRE_TIME;
    private BigInteger PREV_FIRE_TIME;
    private Integer PRIORITY;
    private String TRIGGER_STATE;
    private BigInteger START_TIME;
    private BigInteger END_TIME;
    private String CRON_EXPRESSION;
    private String JOB_CLASS_NAME;

    public String getJOB_CLASS_NAME() {
        return this.JOB_CLASS_NAME;
    }

    public void setJOB_CLASS_NAME(String jOB_CLASS_NAME) {
        this.JOB_CLASS_NAME = jOB_CLASS_NAME;
    }

    public String getCRON_EXPRESSION() {
        return this.CRON_EXPRESSION;
    }

    public void setCRON_EXPRESSION(String cRON_EXPRESSION) {
        this.CRON_EXPRESSION = cRON_EXPRESSION;
    }

    public String getJOB_GROUP() {
        return this.JOB_GROUP;
    }

    public void setJOB_GROUP(String jOB_GROUP) {
        this.JOB_GROUP = jOB_GROUP;
    }

    public String getJOB_NAME() {
        return this.JOB_NAME;
    }

    public void setJOB_NAME(String jOB_NAME) {
        this.JOB_NAME = jOB_NAME;
    }

    public String getTRIGGER_GROUP() {
        return this.TRIGGER_GROUP;
    }

    public void setTRIGGER_GROUP(String tRIGGER_GROUP) {
        this.TRIGGER_GROUP = tRIGGER_GROUP;
    }

    public String getTRIGGER_NAME() {
        return this.TRIGGER_NAME;
    }

    public void setTRIGGER_NAME(String tRIGGER_NAME) {
        this.TRIGGER_NAME = tRIGGER_NAME;
    }

    public BigInteger getNEXT_FIRE_TIME() {
        return this.NEXT_FIRE_TIME;
    }

    public void setNEXT_FIRE_TIME(BigInteger nEXT_FIRE_TIME) {
        this.NEXT_FIRE_TIME = nEXT_FIRE_TIME;
    }

    public BigInteger getPREV_FIRE_TIME() {
        return this.PREV_FIRE_TIME;
    }

    public void setPREV_FIRE_TIME(BigInteger pREV_FIRE_TIME) {
        this.PREV_FIRE_TIME = pREV_FIRE_TIME;
    }

    public Integer getPRIORITY() {
        return this.PRIORITY;
    }

    public void setPRIORITY(Integer pRIORITY) {
        this.PRIORITY = pRIORITY;
    }

    public String getTRIGGER_STATE() {
        return this.TRIGGER_STATE;
    }

    public void setTRIGGER_STATE(String tRIGGER_STATE) {
        this.TRIGGER_STATE = tRIGGER_STATE;
    }

    public BigInteger getSTART_TIME() {
        return this.START_TIME;
    }

    public void setSTART_TIME(BigInteger sTART_TIME) {
        this.START_TIME = sTART_TIME;
    }

    public BigInteger getEND_TIME() {
        return this.END_TIME;
    }

    public void setEND_TIME(BigInteger eND_TIME) {
        this.END_TIME = eND_TIME;
    }
}
