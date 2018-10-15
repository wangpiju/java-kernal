package com.hs3.models.roles;

import com.hs3.entity.roles.Jurisdiction;

public class JurisdictionEx
        extends Jurisdiction {
    private Integer firstMenuId;
    private String firstName;
    private String secondName;
    private Integer role_jur_id;

    public Integer getFirstMenuId() {
        return this.firstMenuId;
    }

    public void setFirstMenuId(Integer firstMenuId) {
        this.firstMenuId = firstMenuId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Integer getRole_jur_id() {
        return this.role_jur_id;
    }

    public void setRole_jur_id(Integer role_jur_id) {
        this.role_jur_id = role_jur_id;
    }
}
