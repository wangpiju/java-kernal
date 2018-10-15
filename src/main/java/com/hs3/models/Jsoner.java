package com.hs3.models;

import com.hs3.utils.StrUtils;

public class Jsoner {
    private int status;
    private Object content;
    private String url;

    private Jsoner(int status, Object content) {
        this.status = status;
        this.content = content;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getContent() {
        return this.content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static Jsoner getByResult(boolean isSuccess) {
        if (isSuccess) {
            return success();
        }
        return error();
    }

    public static Jsoner success() {
        return success("操作成功");
    }

    public static Jsoner success(Object content) {
        return new Jsoner(200, content);
    }

    public static Jsoner success(Object content, String url) {
        Jsoner rel = new Jsoner(200, content);
        rel.setUrl(url);
        return rel;
    }

    public static Jsoner error() {
        return error("操作失败");
    }

    public static Jsoner noLogin() {
        return new Jsoner(302, "请重新登录");
    }

    public static Jsoner noLogin(String msg) {
        return new Jsoner(302, msg);
    }

    public static Jsoner error(String s) {
        return new Jsoner(501, s);
    }

    public static Jsoner getInstance(int status, Object content) {
        return new Jsoner(status, content);
    }

    public String toString() {
        return StrUtils.toJson(this);
    }
}
