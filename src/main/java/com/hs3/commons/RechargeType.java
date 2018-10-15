package com.hs3.commons;

/**
 * program: java-kernal
 * des:
 * author: Jason
 * create: 2018-07-07 18:28
 **/
public enum RechargeType {
    AUDIT(6,"审核中"),
    COMPLETE(2,"完成"),
    REJECT(1,"拒绝");

    private int code;
    private String desc;

    RechargeType(int code, String desc) {
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
