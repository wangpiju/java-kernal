package com.hs3.models.activity;

import java.util.List;

public class ActivitySignForIndex {
    private boolean signToday;
    private Integer days;
    private List<String> signDays;

    public boolean isSignToday() {
        return this.signToday;
    }

    public void setSignToday(boolean signToday) {
        this.signToday = signToday;
    }

    public Integer getDays() {
        return this.days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public List<String> getSignDays() {
        return this.signDays;
    }

    public void setSignDays(List<String> signDays) {
        this.signDays = signDays;
    }
}
