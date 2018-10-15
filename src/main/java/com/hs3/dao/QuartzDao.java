package com.hs3.dao;

public class QuartzDao<T>
        extends BaseDao<T> {
    public QuartzDao() {
        this.tableName = ("qrtz" + this.cls.getSimpleName().replaceAll("([A-Z])", "_$1").toLowerCase());
    }
}
