package com.hs3.models.lotts;

import com.hs3.entity.lotts.LotterySeason;

import java.util.Date;

public class LotterySeasonAndAfterTime {
    Date afterDate;
    LotterySeason lotterySeason;

    public Date getAfterDate() {
        return this.afterDate;
    }

    public void setAfterDate(Date afterDate) {
        this.afterDate = afterDate;
    }

    public LotterySeason getLotterySeason() {
        return this.lotterySeason;
    }

    public void setLotterySeason(LotterySeason lotterySeason) {
        this.lotterySeason = lotterySeason;
    }
}
