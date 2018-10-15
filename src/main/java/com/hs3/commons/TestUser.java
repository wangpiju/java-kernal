package com.hs3.commons;

/**
 * program: java-kernal
 * des: 真实用户还是测试用户
 * author: Terra
 * create: 2018-06-23 23:55
 **/
public enum TestUser {
    real(0),
    test(1);
    private int code;

    TestUser(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
