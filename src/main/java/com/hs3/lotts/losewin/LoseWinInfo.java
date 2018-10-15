package com.hs3.lotts.losewin;

import java.math.BigDecimal;
import java.util.Map;

public class LoseWinInfo {
    private String id;
    private String key;
    private BigDecimal betAmount;
    private Map<String, BigDecimal> content;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BigDecimal getBetAmount() {
        return this.betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public Map<String, BigDecimal> getContent() {
        return this.content;
    }

    public void setContent(Map<String, BigDecimal> content) {
        this.content = content;
    }
}
