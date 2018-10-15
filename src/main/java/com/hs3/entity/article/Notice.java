package com.hs3.entity.article;

import com.hs3.json.JsonDateTimeSerializer;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Notice {
    private Integer id;
    private String title;
    private String content;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date createTime;
    private Integer status;
    private Integer orderId;
    private String author;
    private Integer switching;

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

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getSwitching() {
        return this.switching;
    }

    public void setSwitching(Integer switching) {
        this.switching = switching;
    }
}
