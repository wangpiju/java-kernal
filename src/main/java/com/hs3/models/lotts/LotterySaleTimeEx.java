package com.hs3.models.lotts;

import com.hs3.entity.lotts.LotterySaleTime;

public class LotterySaleTimeEx
        extends LotterySaleTime {
    private static final long serialVersionUID = 1L;
    private String serviceTime;

    public String getServiceTime() {
        return this.serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }
}
