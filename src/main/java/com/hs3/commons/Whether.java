package com.hs3.commons;

/**
 * program: java-kernal
 * des: 是否
 * author: Terra
 * create: 2018-06-15 18:43
 **/
public enum Whether {

    no(0, "否"),
    yes(1, "是");

    private int status;
    private String desc;

    Whether(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
