package com.hs3.entity.users;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户验证码
 *
 * @author Stephen Zhou
 */
public class UserCode implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String account;
    private Integer type;    //类型：1、手机验证码，2、邮箱验证码
    private String code;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;
    private Integer status;    //状态：1、未使用，2、已使用，3、已销毁
    private String comments;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


}
