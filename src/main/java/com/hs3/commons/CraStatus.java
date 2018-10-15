package com.hs3.commons;

/**
 * program: java-kernal
 * des: 号源状态
 * author: Terra
 * create: 2018-06-18 15:13
 **/
public enum CraStatus {

    enable(0), //正常
    disable(1); //禁用

    private int code;

    CraStatus(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}

