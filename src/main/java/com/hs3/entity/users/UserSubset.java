package com.hs3.entity.users;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserSubset implements Serializable {

    private Integer id;
    private String account;
    private String subSetAccount;

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

    public String getSubSetAccount() {
        return subSetAccount;
    }

    public void setSubSetAccount(String subSetAccount) {
        this.subSetAccount = subSetAccount;
    }
}
