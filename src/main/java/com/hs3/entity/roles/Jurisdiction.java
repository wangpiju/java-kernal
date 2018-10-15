package com.hs3.entity.roles;

public class Jurisdiction {
    private Integer id;
    private Integer firstMenuId;
    private Integer secondMenuId;
    private String path;
    private String remark;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFirstMenuId() {
        return this.firstMenuId;
    }

    public void setFirstMenuId(Integer firstMenuId) {
        this.firstMenuId = firstMenuId;
    }

    public Integer getSecondMenuId() {
        return this.secondMenuId;
    }

    public void setSecondMenuId(Integer secondMenuId) {
        this.secondMenuId = secondMenuId;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
