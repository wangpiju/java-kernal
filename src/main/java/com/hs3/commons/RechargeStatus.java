package com.hs3.commons;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-08-01 14:40
 **/
public enum RechargeStatus {


    no_process(0, "未处理"),
    reject(1, "拒绝"),
    completed(2, "完成"),
    expire(3, "已过期"),
    cancel(4, "已撤销"),
    processing(5, "正在处理"),
    review(6, "审核中"),
    hang_up(20, "挂起"),
    empty_info(99, "无信息");

    private int status;
    private String desc;

    RechargeStatus(int status, String desc) {
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
