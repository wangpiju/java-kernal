package com.hs3.commons;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-06-20 17:24
 **/
public enum BetStatus {

    no_lottery(0, "等待开奖"),
    win(1, "已中奖"),
    no_win(2, "未中奖"),
    no_start(3, "未开始"),
    user_withdraw(4, "个人撤单"),
    system_withdraw(5, "系统撤单"),
    no_lottery_dup(6, "未开奖"),
    anomaly(7, "恶意注单"),
    stop(8, "暂停"),
    win_stop(9, "追中即停");

    private int status;
    private String desc;

    BetStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getStatus() {
        return status;
    }

}
