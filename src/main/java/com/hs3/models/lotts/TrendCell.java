package com.hs3.models.lotts;

public class TrendCell {
    private int maxOpen;
    private int maxLost;
    private int maxLian;
    private int nowLost;
    private int nowLian;

    public int getMaxOpen() {
        return this.maxOpen;
    }

    public void setMaxOpen(int maxOpen) {
        this.maxOpen = maxOpen;
    }

    public int getMaxLost() {
        return this.maxLost;
    }

    public void setMaxLost(int maxLost) {
        this.maxLost = maxLost;
    }

    public int getMaxLian() {
        return this.maxLian;
    }

    public void setMaxLian(int maxLian) {
        this.maxLian = maxLian;
    }

    public int getNowLost() {
        return this.nowLost;
    }

    public void setNowLost(int nowLost) {
        this.nowLost = nowLost;
    }

    public int getNowLian() {
        return this.nowLian;
    }

    public void setNowLian(int nowLian) {
        this.nowLian = nowLian;
    }
}
