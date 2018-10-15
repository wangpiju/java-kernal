package com.hs3.entity.lotts;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AmountChange {
    private int id;
    private String changeUser;
    private BigDecimal changeAmount;
    private BigDecimal balance;
    private String changeSource;
    private String betId;
    private String seasonId;
    private String lotteryId;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date changeTime;
    private String remark;
    private String handlers;
    private String playName;
    private int accountChangeTypeId;
    private int test;
    private String playerId;
    private String groupName;
    private String lotteryName;
    private String name;
    private BigDecimal unit;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChangeUser() {
        return this.changeUser;
    }

    public void setChangeUser(String changeUser) {
        this.changeUser = changeUser;
    }

    public BigDecimal getChangeAmount() {
        return this.changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getChangeSource() {
        return this.changeSource;
    }

    public void setChangeSource(String changeSource) {
        this.changeSource = changeSource;
    }

    public String getBetId() {
        return this.betId;
    }

    public void setBetId(String betId) {
        this.betId = betId;
    }

    public String getSeasonId() {
        return this.seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public Date getChangeTime() {
        return this.changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHandlers() {
        return this.handlers;
    }

    public void setHandlers(String handlers) {
        this.handlers = handlers;
    }

    public String getPlayName() {
        return this.playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public int getAccountChangeTypeId() {
        return this.accountChangeTypeId;
    }

    public void setAccountChangeTypeId(int accountChangeTypeId) {
        this.accountChangeTypeId = accountChangeTypeId;
    }

    public int getTest() {
        return this.test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLotteryName() {
        return this.lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public BigDecimal getUnit() {
        return this.unit;
    }

    public void setUnit(BigDecimal unit) {
        this.unit = unit;
    }
}
