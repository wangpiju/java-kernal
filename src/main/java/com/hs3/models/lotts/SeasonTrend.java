package com.hs3.models.lotts;

import java.util.List;

public class SeasonTrend {
    private String seasonId;
    private String nums;
    private List<SeasonTrendNum> info;

    public String getSeasonId() {
        return this.seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getNums() {
        return this.nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public List<SeasonTrendNum> getInfo() {
        return this.info;
    }

    public void setInfo(List<SeasonTrendNum> info) {
        this.info = info;
    }
}
