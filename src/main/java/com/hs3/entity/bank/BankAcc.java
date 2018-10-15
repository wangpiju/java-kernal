package com.hs3.entity.bank;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BankAcc {
    private Integer id;
    private Integer bankApiId;
    private Integer type;
    private String account;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBankApiId() {
        return this.bankApiId;
    }

    public void setBankApiId(Integer bankApiId) {
        this.bankApiId = bankApiId;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
