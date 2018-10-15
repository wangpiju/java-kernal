package com.hs3.commons;

/**
 * program: java-kernal
 * des: 开奖状态
 * author: Terra
 * create: 2018-06-15 18:43
 **/
public enum OpenStatus {

    non_execution(0, "未执行"),
    executed(1, "已执行"),
    completed(2, "已完成"),
    lottery_advance(3, "提前开奖"),
    lottery_manual(4, "人工开奖"),
    lottery_timeout(5, "开奖超时"),
    system_withdraw(6, "系统撤单"),
    lottery_repeat(7, "重复开奖");

    private int status;
    private String desc;

    OpenStatus(int status, String desc) {
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
