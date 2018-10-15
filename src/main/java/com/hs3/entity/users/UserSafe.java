package com.hs3.entity.users;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

public class UserSafe
        implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String account;
    private String qsType1;
    private String answer1;
    private String qsType2;
    private String answer2;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date lastModify;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = (account == null ? null : account.trim());
    }

    public String getQstype1() {
        return this.qsType1;
    }

    public void setQstype1(String qstype1) {
        this.qsType1 = (qstype1 == null ? null : qstype1.trim());
    }

    public String getAnswer1() {
        return this.answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = (answer1 == null ? null : answer1.trim());
    }

    public String getQstype2() {
        return this.qsType2;
    }

    public void setQstype2(String qstype2) {
        this.qsType2 = (qstype2 == null ? null : qstype2.trim());
    }

    public String getAnswer2() {
        return this.answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = (answer2 == null ? null : answer2.trim());
    }

    public Date getCreatetime() {
        return this.createTime;
    }

    public void setCreatetime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastmodify() {
        return this.lastModify;
    }

    public void setLastmodify(Date lastModify) {
        this.lastModify = lastModify;
    }
}
