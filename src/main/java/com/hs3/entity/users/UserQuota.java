package com.hs3.entity.users;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class UserQuota {
    private String account;
    private BigDecimal rebateRatio;
    private Integer num;
    private DecimalFormat df = new DecimalFormat("0.0");

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        if (account.length() > 100) {
            this.account = "";
        } else {
            this.account = account;
        }
    }

    public BigDecimal getRebateRatio() {
        return BigDecimal.valueOf(Double.parseDouble(this.df.format(this.rebateRatio)));
    }

    public void setRebateRatio(BigDecimal rebateRatio) {
        this.rebateRatio = rebateRatio;
    }

    public Integer getNum() {
        return this.num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
