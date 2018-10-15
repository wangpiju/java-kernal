package com.hs3.entity.lotts;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LotterySeason
        implements Serializable {
    private static final long serialVersionUID = 3687311623830196034L;
    private String lotteryId;
    private String seasonId;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date openTime;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date addTime;
    private Integer n1;
    private Integer n2;
    private Integer n3;
    private Integer n4;
    private Integer n5;
    private Integer n6;
    private Integer n7;
    private Integer n8;
    private Integer n9;
    private Integer n10;
    private Integer restSeconds;
    private String name;
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(Integer restSeconds) {
        this.restSeconds = restSeconds;
    }

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

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

    public Date getAddTime() {
        return this.addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getN1() {
        return this.n1;
    }

    public void setN1(Integer n1) {
        this.n1 = n1;
    }

    public Integer getN2() {
        return this.n2;
    }

    public void setN2(Integer n2) {
        this.n2 = n2;
    }

    public Integer getN3() {
        return this.n3;
    }

    public void setN3(Integer n3) {
        this.n3 = n3;
    }

    public Integer getN4() {
        return this.n4;
    }

    public void setN4(Integer n4) {
        this.n4 = n4;
    }

    public Integer getN5() {
        return this.n5;
    }

    public void setN5(Integer n5) {
        this.n5 = n5;
    }

    public Integer getN6() {
        return this.n6;
    }

    public void setN6(Integer n6) {
        this.n6 = n6;
    }

    public Integer getN7() {
        return this.n7;
    }

    public void setN7(Integer n7) {
        this.n7 = n7;
    }

    public Integer getN8() {
        return this.n8;
    }

    public void setN8(Integer n8) {
        this.n8 = n8;
    }

    public Integer getN9() {
        return this.n9;
    }

    public void setN9(Integer n9) {
        this.n9 = n9;
    }

    public Integer getN10() {
        return this.n10;
    }

    public void setN10(Integer n10) {
        this.n10 = n10;
    }
}
