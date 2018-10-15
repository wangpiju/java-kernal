package com.hs3.entity.article;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-08-07 14:13
 **/
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class NoticeRead {

    private int id;
    private int noticeId;
    private String account;
    private Date createTime;
    /**
     * 1.systemNotice
     * 2.userNotice
     */
    private int type;

    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
