package com.hs3.entity.article;

import com.hs3.json.JsonDateTimeSerializer;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Message {
    private Integer id;
    private String title;
    private String sender;
    private String sendContent;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date sendTime;
    private Integer sendStatus;
    private String rever;
    private String revContent;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date revTime;
    private Integer revStatus;
    private Integer sendType;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSendContent() {
        return this.sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public Date getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getSendStatus() {
        return this.sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getRever() {
        return this.rever;
    }

    public void setRever(String rever) {
        this.rever = rever;
    }

    public String getRevContent() {
        return this.revContent;
    }

    public void setRevContent(String revContent) {
        this.revContent = revContent;
    }

    public Date getRevTime() {
        return this.revTime;
    }

    public void setRevTime(Date revTime) {
        this.revTime = revTime;
    }

    public Integer getRevStatus() {
        return this.revStatus;
    }

    public void setRevStatus(Integer revStatus) {
        this.revStatus = revStatus;
    }

    public Integer getSendType() {
        return this.sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }
}
