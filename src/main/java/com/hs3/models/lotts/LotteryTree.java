package com.hs3.models.lotts;

import com.hs3.entity.lotts.Lottery;

import java.util.List;

public class LotteryTree {
    private String key;
    private String icon;
    private List<List<Lottery>> value;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<List<Lottery>> getValue() {
        return this.value;
    }

    public void setValue(List<List<Lottery>> value) {
        this.value = value;
    }
}
