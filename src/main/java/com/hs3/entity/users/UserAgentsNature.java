package com.hs3.entity.users;


import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import java.io.Serializable;
import java.util.Date;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserAgentsNature implements Serializable {

    private Integer id;
    private String account;
    private String parentAccount;
    private Integer isA;    //是否A类代理
    private Integer isDaily;    //是否日工资
    private Integer isDividend; //是否周期分红
    private Integer isDailyLottery; //是否日工资彩种加奖
    private Integer isDividendLottery;  //是否周期分红彩种加奖
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date modifyTime;
    private String operator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getIsA() {
        return isA;
    }

    public void setIsA(Integer isA) {
        this.isA = isA;
    }

    public Integer getIsDaily() {
        return isDaily;
    }

    public void setIsDaily(Integer isDaily) {
        this.isDaily = isDaily;
    }

    public Integer getIsDividend() {
        return isDividend;
    }

    public void setIsDividend(Integer isDividend) {
        this.isDividend = isDividend;
    }

    public Integer getIsDailyLottery() {
        return isDailyLottery;
    }

    public void setIsDailyLottery(Integer isDailyLottery) {
        this.isDailyLottery = isDailyLottery;
    }

    public Integer getIsDividendLottery() {
        return isDividendLottery;
    }

    public void setIsDividendLottery(Integer isDividendLottery) {
        this.isDividendLottery = isDividendLottery;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
    }
}
