package com.hs3.entity.lotts;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Bet
        implements Cloneable {
    private String id;
    private String lotteryId;
    private String playerId;
    private String seasonId;
    private Integer userMark;
    private String account;
    private String lotteryName;
    private String playName;
    private String content;
    private Integer price;
    private BigDecimal unit;
    private BigDecimal amount;
    private Integer status;
    private Integer bonusType;
    private Integer betCount;
    private String hashCode;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date lastTime;
    private String openNum;
    private String groupName;
    private BigDecimal win;
    private BigDecimal bonusRate;
    private String traceId;
    private Integer isTrace;
    private Integer test;
    private BigDecimal theoreticalBonus;
    private Integer traceWinStop;
    private BigDecimal ocRebateAmount;

    public Bet clone() {
        Bet o = null;
        try {
            o = (Bet) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getSeasonId() {
        return this.seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public Integer getUserMark() {
        return this.userMark;
    }

    public void setUserMark(Integer userMark) {
        this.userMark = userMark;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLotteryName() {
        return this.lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPrice() {
        return this.price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public BigDecimal getUnit() {
        return this.unit;
    }

    public void setUnit(BigDecimal unit) {
        this.unit = unit;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBonusType() {
        return this.bonusType;
    }

    public void setBonusType(Integer bonusType) {
        this.bonusType = bonusType;
    }

    public Integer getBetCount() {
        return this.betCount;
    }

    public void setBetCount(Integer betCount) {
        this.betCount = betCount;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastTime() {
        return this.lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getOpenNum() {
        return this.openNum;
    }

    public void setOpenNum(String openNum) {
        this.openNum = openNum;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public BigDecimal getWin() {
        return this.win;
    }

    public void setWin(BigDecimal win) {
        this.win = win;
    }

    public String getPlayName() {
        return this.playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public BigDecimal getBonusRate() {
        return this.bonusRate;
    }

    public void setBonusRate(BigDecimal bonusRate) {
        this.bonusRate = bonusRate;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Integer getIsTrace() {
        return this.isTrace;
    }

    public void setIsTrace(Integer isTrace) {
        this.isTrace = isTrace;
    }

    public BigDecimal getTheoreticalBonus() {
        return this.theoreticalBonus;
    }

    public void setTheoreticalBonus(BigDecimal theoreticalBonus) {
        this.theoreticalBonus = theoreticalBonus;
    }

    public Integer getTest() {
        return this.test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public Integer getTraceWinStop() {
        return this.traceWinStop;
    }

    public void setTraceWinStop(Integer traceWinStop) {
        this.traceWinStop = traceWinStop;
    }

    public BigDecimal getOcRebateAmount() {
        return ocRebateAmount;
    }

    public void setOcRebateAmount(BigDecimal ocRebateAmount) {
        this.ocRebateAmount = ocRebateAmount;
    }
}
