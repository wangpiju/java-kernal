package com.hs3.commons;

/**
 * program: java-kernal
 * des: 追号状态
 * author: Terra
 * create: 2018-06-15 18:43
 **/
public enum PlanStatus {

    non_execution(0, "未执行"),
    executed(1, "已执行"),
    completed(2, "已完成"),
    executing(3, "正在执行"),
    system_withdraw(6, "系统撤单");

    private int status;
    private String desc;

    PlanStatus(int status, String desc) {
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
