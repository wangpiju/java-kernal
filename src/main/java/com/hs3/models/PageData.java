package com.hs3.models;

import java.util.ArrayList;

public class PageData {
    private static PageData instance = new PageData(0, new ArrayList());
    private Integer total;
    private Object rows;
    private Object obj;

    public PageData(int total, Object rows) {
        this.total = Integer.valueOf(total);
        this.rows = rows;
    }

    public PageData(int total, Object obj, Object rows) {
        this.total = Integer.valueOf(total);
        this.obj = obj;
        this.rows = rows;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Object getObj() {
        return this.obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getRows() {
        return this.rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public static PageData defaultInstance() {
        return instance;
    }
}
