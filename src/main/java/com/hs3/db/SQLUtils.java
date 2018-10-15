package com.hs3.db;

import com.hs3.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class SQLUtils {
    private StringBuilder where = new StringBuilder();
    private List<String> field = new ArrayList();
    private StringBuilder order = new StringBuilder();
    private String tableName;

    public SQLUtils(String tableName) {
        this.tableName = tableName;
    }

    public SQLUtils orderBy(String order) {
        this.order.append(order);
        return this;
    }

    public SQLUtils field(String field) {
        this.field.addAll(ListUtils.toList(field));
        return this;
    }

    private String getQuestionNumber(int num) {
        StringBuilder sb = new StringBuilder("?");
        for (int i = 1; i < num; i++) {
            sb.append(",?");
        }
        return sb.toString();
    }

    public SQLUtils where(String where) {
        this.where.append(where).append(" ");
        return this;
    }

    public SQLUtils whereOr(String where) {
        if (this.where.length() > 0) {
            this.where.append(" OR ");
        }
        where(where);
        return this;
    }

    public SQLUtils whereAnd(String where) {
        if (this.where.length() > 0) {
            this.where.append(" AND ");
        }
        return where(where);
    }

    public SQLUtils whereIn(String where, int size) {
        String w = where.replaceAll("\\?", getQuestionNumber(size));
        this.where.append(w);

        return this;
    }

    public SQLUtils whereInOr(String where, int size) {
        if (this.where.length() > 0) {
            this.where.append(" OR ");
        }
        return whereIn(where, size);
    }

    public SQLUtils whereInAnd(String where, int size) {
        if (this.where.length() > 0) {
            this.where.append(" AND ");
        }
        return whereIn(where, size);
    }

    public String getSelect() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        if (this.field.size() == 0) {
            sql.append("* ");
        } else {
            sql.append(ListUtils.toString(this.field)).append(" ");
        }
        sql.append("FROM ").append(this.tableName);
        if (this.where.length() > 0) {
            sql.append(" WHERE ");
            sql.append(this.where);
        }
        if (this.order.length() > 0) {
            sql.append(" ORDER BY ");
            sql.append(this.order);
//            sql.append(" DESC ");
        }
        return sql.toString();
    }

    public String getCount() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) FROM ").append(this.tableName);
        if (this.where.length() > 0) {
            sql.append(" WHERE ");
            sql.append(this.where);
        }
        return sql.toString();
    }

    public String getInsert() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(this.tableName).append("(");
        sql.append(ListUtils.toString(this.field)).append(") VALUES(");
        sql.append(getQuestionNumber(this.field.size())).append(")");
        return sql.toString();
    }

    public String getUpdate() {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(this.tableName).append(" SET ");
        sql.append(ListUtils.toString(this.field, "=?,"));
        sql.append("=?");
        if (this.where.length() > 0) {
            sql.append(" WHERE ");
            sql.append(this.where);
        }
        return sql.toString();
    }

    public String getDelete() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(this.tableName);
        if (this.where.length() > 0) {
            sql.append(" WHERE ");
            sql.append(this.where);
        }
        return sql.toString();
    }
}
