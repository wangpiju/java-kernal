package com.hs3.lotts;

import java.util.List;

public class NumberView {
    private String title;
    private List<?> nums;
    private List<String[]> numBonus;
    private Boolean hasBut;
    private int maxSelect;
    private Boolean towLine = Boolean.valueOf(false);

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<?> getNums() {
        return this.nums;
    }

    public void setNums(List<?> nums) {
        this.nums = nums;
    }

    public NumberView(String title, List<?> nums) {
        this.title = title;
        this.nums = nums;
        this.hasBut = Boolean.valueOf(true);
    }

    public NumberView(String title, List<?> nums, boolean hasBut) {
        this.title = title;
        this.nums = nums;
        this.hasBut = Boolean.valueOf(hasBut);
    }

    public NumberView(String title, List<?> nums, boolean hasBut, int maxSelect) {
        this.title = title;
        this.nums = nums;
        this.hasBut = Boolean.valueOf(hasBut);
        this.maxSelect = maxSelect;
    }

    public NumberView(String title, List<?> nums, boolean hasBut, int maxSelect, boolean towLine) {
        this.title = title;
        this.nums = nums;
        this.hasBut = Boolean.valueOf(hasBut);
        this.maxSelect = maxSelect;

        this.towLine = Boolean.valueOf(towLine);
    }

    public Boolean getHasBut() {
        return this.hasBut;
    }

    public void setHasBut(Boolean hasBut) {
        this.hasBut = hasBut;
    }

    public int getMaxSelect() {
        return this.maxSelect;
    }

    public void setMaxSelect(int maxSelect) {
        this.maxSelect = maxSelect;
    }

    public Boolean getTowLine() {
        return this.towLine;
    }

    public void setTowLine(Boolean towLine) {
        this.towLine = towLine;
    }

    public List<String[]> getNumBonus() {
        return numBonus;
    }

    public void setNumBonus(List<String[]> numBonus) {
        this.numBonus = numBonus;
    }
}
