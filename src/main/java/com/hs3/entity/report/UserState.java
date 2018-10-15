package com.hs3.entity.report;

import java.io.Serializable;

public class UserState
        implements Serializable {
    private static final long serialVersionUID = 1L;
    private int notLogin;
    private int notBind;
    private int notRecharge;
    private int notBet;
    private int player;
    private int daili;
    private int alls;

    public int getNotLogin() {
        return this.notLogin;
    }

    public void setNotLogin(int notLogin) {
        this.notLogin = notLogin;
    }

    public int getNotBind() {
        return this.notBind;
    }

    public int getPlayer() {
        return this.player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void setNotBind(int notBind) {
        this.notBind = notBind;
    }

    public int getNotRecharge() {
        return this.notRecharge;
    }

    public void setNotRecharge(int notRecharge) {
        this.notRecharge = notRecharge;
    }

    public int getNotBet() {
        return this.notBet;
    }

    public void setNotBet(int notBet) {
        this.notBet = notBet;
    }

    public int getDaili() {
        return this.daili;
    }

    public void setDaili(int daili) {
        this.daili = daili;
    }

    public int getAlls() {
        return this.alls;
    }

    public void setAlls(int alls) {
        this.alls = alls;
    }
}
