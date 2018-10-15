package com.hs3.models.lotts;

import com.hs3.entity.lotts.Player;

import java.util.List;

public class PlayerModel {
    private String lotteryId;
    private List<Player> details;

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public List<Player> getDetails() {
        return this.details;
    }

    public void setDetails(List<Player> details) {
        this.details = details;
    }
}
