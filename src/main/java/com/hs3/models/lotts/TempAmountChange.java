package com.hs3.models.lotts;

import com.hs3.entity.lotts.AmountChange;

public class TempAmountChange
        extends AmountChange {
    private Integer status;

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
