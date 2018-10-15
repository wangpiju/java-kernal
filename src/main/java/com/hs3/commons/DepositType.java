package com.hs3.commons;

/**
 * program: java-kernal
 * des:
 * author: Jason
 * create: 2018-07-07 18:28
 **/
public enum DepositType {
    OFFLINE(0,"银行充值"),
    ONLINE(1,"第三方充值"),
    CASH(2,"现金充值");

    private int code;
    private String desc;

    DepositType(int code, String desc) {
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
