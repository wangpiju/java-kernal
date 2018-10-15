package com.hs3.entity.sys;

public class SysService {
    private Integer id;
    private String name;
    private Integer status;
    private Integer userMark;
    private String link;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserMark() {
        return this.userMark;
    }

    public void setUserMark(Integer userMark) {
        this.userMark = userMark;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
