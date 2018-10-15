package com.hs3.models.lotts;

import com.hs3.entity.lotts.BonusRisk;

import java.util.Date;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-06-26 10:44
 **/
public class BonusRiskEx extends BonusRisk {

    private Date beginTime;
    private Date endTime;

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
