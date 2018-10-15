package com.hs3.entity.lotts;

import com.hs3.json.JsonDateSerializer;
import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SeasonForDate {
    private Integer id;
    private String lotteryId;
    private String seasonRule;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date seasonDate;
    private String firstSeason;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date lastTime;
    private Integer autoCreateDay;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getSeasonRule() {
        return this.seasonRule;
    }

    public void setSeasonRule(String seasonRule) {
        this.seasonRule = seasonRule;
    }

    public String getFirstSeason() {
        return this.firstSeason;
    }

    public void setFirstSeason(String firstSeason) {
        this.firstSeason = firstSeason;
    }

    public Date getSeasonDate() {
        return this.seasonDate;
    }

    public void setSeasonDate(Date seasonDate) {
        this.seasonDate = seasonDate;
    }

    public Date getLastTime() {
        return this.lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Integer getAutoCreateDay() {
        return this.autoCreateDay;
    }

    public void setAutoCreateDay(Integer autoCreateDay) {
        this.autoCreateDay = autoCreateDay;
    }
}
