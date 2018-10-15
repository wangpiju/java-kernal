package com.hs3.lotts.rule.impl;

public class Ymd4SeasonBuilder
        extends Ymd3SeasonBuilder {
    public String getTitle() {
        return "ymd4";
    }

    public String getRemark() {
        return "日期-4位数";
    }

    protected int indexLen() {
        return 4;
    }
}
