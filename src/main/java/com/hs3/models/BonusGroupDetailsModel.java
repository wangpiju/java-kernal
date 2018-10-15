package com.hs3.models;

import com.hs3.entity.lotts.BonusGroupDetails;

import java.util.List;

public class BonusGroupDetailsModel {
    private Integer bonusGroupId;
    private String lotteryId;
    private List<BonusGroupDetails> details;

    public Integer getBonusGroupId() {
        return this.bonusGroupId;
    }

    public void setBonusGroupId(Integer bonusGroupId) {
        this.bonusGroupId = bonusGroupId;
    }

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public List<BonusGroupDetails> getDetails() {
        return this.details;
    }

    public void setDetails(List<BonusGroupDetails> details) {
        this.details = details;
    }
}
