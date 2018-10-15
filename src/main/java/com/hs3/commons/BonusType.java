package com.hs3.commons;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-06-20 18:28
 **/
public enum BonusType {

    high_bonus(0,"高奖"),
    high_rebate(1,"高返");

    private int code;
    private String desc;

    BonusType(int code, String desc) {
        this.code= code;
        this.desc = desc;
    }

    public int code() {
        return code;
    }

    public String desc() {
        return desc;
    }

}
