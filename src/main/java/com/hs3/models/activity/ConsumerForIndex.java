package com.hs3.models.activity;

import java.math.BigDecimal;
import java.util.List;

public class ConsumerForIndex {
    private BigDecimal consumerAmount;
    private BigDecimal giveAmount;
    private List<String> hasGiveItmes;
    private List<String> canGiveItems;

    public BigDecimal getConsumerAmount() {
        return this.consumerAmount;
    }

    public void setConsumerAmount(BigDecimal consumerAmount) {
        this.consumerAmount = consumerAmount;
    }

    public BigDecimal getGiveAmount() {
        return this.giveAmount;
    }

    public void setGiveAmount(BigDecimal giveAmount) {
        this.giveAmount = giveAmount;
    }

    public List<String> getHasGiveItmes() {
        return this.hasGiveItmes;
    }

    public void setHasGiveItmes(List<String> hasGiveItmes) {
        this.hasGiveItmes = hasGiveItmes;
    }

    public List<String> getCanGiveItems() {
        return this.canGiveItems;
    }

    public void setCanGiveItems(List<String> canGiveItems) {
        this.canGiveItems = canGiveItems;
    }
}
