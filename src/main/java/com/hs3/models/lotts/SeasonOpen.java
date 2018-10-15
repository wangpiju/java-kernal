package com.hs3.models.lotts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeasonOpen {
    private String seasonId;
    private Date openTime;
    private List<String> nums = new ArrayList();

    public String getSeasonId() {
        return this.seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public Date getOpenTime() {
        return this.openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public List<String> getNums() {
        return this.nums;
    }

    public void setNums(List<String> nums) {
        this.nums = nums;
    }
}
