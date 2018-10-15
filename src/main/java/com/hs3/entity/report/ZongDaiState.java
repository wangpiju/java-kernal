package com.hs3.entity.report;

import java.io.Serializable;

public class ZongDaiState
        implements Serializable {
    private static final long serialVersionUID = 1L;
    private String account;
    private int player;
    private int daili;
    private int player2;
    private int daili2;
    private int alls;
    private int alls2;

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getPlayer() {
        return this.player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getDaili() {
        return this.daili;
    }

    public void setDaili(int daili) {
        this.daili = daili;
    }

    public int getPlayer2() {
        return this.player2;
    }

    public void setPlayer2(int player2) {
        this.player2 = player2;
    }

    public int getDaili2() {
        return this.daili2;
    }

    public void setDaili2(int daili2) {
        this.daili2 = daili2;
    }

    public int getAlls() {
        return this.alls;
    }

    public void setAlls(int alls) {
        this.alls = alls;
    }

    public int getAlls2() {
        return this.alls2;
    }

    public void setAlls2(int alls2) {
        this.alls2 = alls2;
    }
}
