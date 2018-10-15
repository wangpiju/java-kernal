package com.hs3.models.lotts;

import com.hs3.entity.lotts.LotterySaleTime;

import java.util.List;

public class SaleTimeAndSeason {
    private List<LotterySaleTime> lisLotterySaleTimes;
    private String seasonId;

    public List<LotterySaleTime> getLisLotterySaleTimes() {
        return this.lisLotterySaleTimes;
    }

    public void setLisLotterySaleTimes(List<LotterySaleTime> lisLotterySaleTimes) {
        this.lisLotterySaleTimes = lisLotterySaleTimes;
    }

    public String getSeasonId() {
        return this.seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }
}
