package com.hs3.entity.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BonusGroup {
    private Integer id;
    private String title;
    private BigDecimal bonusRatio;
    private BigDecimal rebateRatio;
    private BigDecimal noneMinRatio;
    private BigDecimal userMinRatio;
    private BigDecimal playerMaxRatio;

    public BigDecimal getPlayerMaxRatio() {
        return this.playerMaxRatio;
    }

    public void setPlayerMaxRatio(BigDecimal playerMaxRatio) {
        this.playerMaxRatio = playerMaxRatio;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getBonusRatio() {
        return this.bonusRatio;
    }

    public void setBonusRatio(BigDecimal bonusRatio) {
        this.bonusRatio = bonusRatio;
    }

    public BigDecimal getRebateRatio() {
        return this.rebateRatio;
    }

    public void setRebateRatio(BigDecimal rebateRatio) {
        this.rebateRatio = rebateRatio;
    }

    public BigDecimal getNoneMinRatio() {
        return this.noneMinRatio;
    }

    public void setNoneMinRatio(BigDecimal noneMinRatio) {
        this.noneMinRatio = noneMinRatio;
    }

    public BigDecimal getUserMinRatio() {
        return this.userMinRatio;
    }

    public void setUserMinRatio(BigDecimal userMinRatio) {
        this.userMinRatio = userMinRatio;
    }
}
