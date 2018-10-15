package com.hs3.models.lotts;

import com.hs3.entity.lotts.LotterySaleTime;

import java.util.Date;

public class LotterySaleTimeAndCloseDt {
    private Date closeDateTime;
    private LotterySaleTime lotterySaleTime;

    public Date getCloseDateTime() {
        return this.closeDateTime;
    }

    public void setCloseDateTime(Date closeDateTime) {
        this.closeDateTime = closeDateTime;
    }

    public LotterySaleTime getLotterySaleTime() {
        return this.lotterySaleTime;
    }

    public void setLotterySaleTime(LotterySaleTime lotterySaleTime) {
        this.lotterySaleTime = lotterySaleTime;
    }
}
