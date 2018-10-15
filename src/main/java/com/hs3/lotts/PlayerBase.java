package com.hs3.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public abstract class PlayerBase {
    private String id;
    private String remark;
    private String example;
    private String displayBonus;
    private String basicBet = "";

    public List<String> getAnyList() {
        return null;
    }
    public Integer getAnySelect() {
        return null;
    }


    public PlayerBase() {
        init();
    }

    protected abstract void init();

    public abstract BigDecimal getBonus();

    public String getBonusStr() {
        return getBonus().toString();
    }

    public abstract String getTitle();

    public String getFullTitle() {
        return getQunName() + "-"+getGroupName() + "-" +getTitle();
    }

    public abstract String getQunName();

    public abstract String getGroupName();

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExample() {
        return this.example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getId() {
        if (this.id == null) {
            String t = getClass().getSimpleName().replaceAll("([A-Z])", "_$1").toLowerCase();
            this.id = t.substring(1, t.length() - 7);
        }
        return this.id;
    }

    public abstract NumberView[] getNumView();

    public String getBasicBet() {
        return basicBet;
    }

    public abstract Integer getCount(String paramString);

    public Map<String, BigDecimal> ifOpenWin(String bets) {
        return null;
    }

    public abstract BigDecimal getWin(String paramString, List<Integer> paramList);

    /**
     * 由于宏发快三要取消豹子通杀，所以此处单独提方法
     */
    public BigDecimal getWinPlus(String paramString, List<Integer> paramList) {
        return null;
    }

    protected void addMap(Map<String, BigDecimal> map, String k, BigDecimal b) {
        if (map.containsKey(k)) {
            b = b.add(map.get(k));
        }
        map.put(k, b);
    }

    public String getDisplayBonus() {
        return displayBonus;
    }

    public void setDisplayBonus(String displayBonus) {
        this.displayBonus = displayBonus;
    }
}
