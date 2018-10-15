package com.hs3.entity.article;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MessageContent {
    private Integer id;
    private Integer messageId;
    private String sender;
    private String rever;
    private String content;
    private Date createTime;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMessageId() {
        return this.messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRever() {
        return this.rever;
    }

    public void setRever(String rever) {
        this.rever = rever;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
