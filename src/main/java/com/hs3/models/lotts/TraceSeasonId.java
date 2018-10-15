package com.hs3.models.lotts;

import com.hs3.json.JsonDateTimeSerializer;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class TraceSeasonId {
    private String seasonId;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date openTime;

    public String getSeasonId() {
        return this.seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public Date getOpenTime() {
        return this.openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }
}
