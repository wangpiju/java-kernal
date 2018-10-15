package com.hs3.commons;

/**
 * program: java-kernal
 * des: 号源类型
 * author: Terra
 * create: 2018-06-18 15:06
 **/
public enum CraType {

    crawler(0), //爬虫
    manual(1), //人工
    random(2); //随机

    private int code;

    CraType(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

}
