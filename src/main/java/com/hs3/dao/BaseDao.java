package com.hs3.dao;

import com.hs3.db.DbSession;
import com.hs3.db.Page;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDao<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected static final String SQL_UPDATE = "UPDATE %s SET %s WHERE %s";
    protected static final String SQL_INSERT = "INSERT INTO %s(%s) VALUES(%s)";
    protected static final String SQL_DELETE = "DELETE FROM %s WHERE id=?";
    protected static final String SQL_DELETE_IN = "DELETE FROM %s WHERE id in(%s)";
    protected static final String SQL_SELECT = "SELECT %s FROM %s %s";
    @Autowired
    protected DbSession dbSession;
    protected Class<T> cls;
    protected String tableName;
    private String sqlSave;
    private String sqlUpdate;

    public BaseDao() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        this.cls = ((Class) parameterizedType.getActualTypeArguments()[0]);

        this.tableName = ("t" + this.cls.getSimpleName().replaceAll("([A-Z])", "_$1").toLowerCase());

        String[] columns = getColumns();
        if (columns != null) {
            String s1 = "";
            String s2 = "";
            String s3 = "";
            for (String s : columns) {
                s1 = s1 + "," + s;
                s2 = s2 + "," + s + "=?";
                s3 = s3 + ",?";
            }
            this.sqlSave = String.format("INSERT INTO %s(%s) VALUES(%s)", new Object[]{this.tableName, s1.substring(1), s3.substring(1)});
            this.sqlUpdate = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, s2.substring(1), "id=?"});
        }
    }

    public int saveAuto(T t) {
        return this.dbSession.update(this.sqlSave, getValues(t));
    }

    public Integer saveAutoReturnId(T t) {
        return Integer.valueOf(this.dbSession.updateKeyHolder(this.sqlSave, getValues(t)));
    }

    public int updateByIdAuto(T t, Serializable id) {
        Object[] objs = getValues(t);
        Object[] newObjes = new Object[objs.length + 1];
        for (int i = 0; i < objs.length; i++) {
            newObjes[i] = objs[i];
        }
        newObjes[objs.length] = id;
        return this.dbSession.update(this.sqlUpdate, newObjes);
    }

    protected String[] getColumns() {
        return null;
    }

    protected Object[] getValues(T t) {
        return null;
    }

    protected String getQuestionNumber(int num) {
        StringBuilder sb = new StringBuilder("?");
        for (int i = 1; i < num; i++) {
            sb.append(",?");
        }
        return sb.toString();
    }

    public List<T> listWithOrder(Page page) {
        String sql = "SELECT * FROM " + this.tableName + " ORDER BY orderId";
        return this.dbSession.list(sql, this.cls, page);
    }

    public List<T> list(Page page) {
        String sql = "SELECT * FROM " + this.tableName;
        return this.dbSession.list(sql, this.cls, page);
    }

    public T find(Serializable id) {
        String sql = "SELECT * FROM " + this.tableName + " WHERE id=?";
        return this.dbSession.getObject(sql, new Object[]{id}, this.cls);
    }

    public T findLock(Serializable id) {
        String sql = "SELECT * FROM " + this.tableName + " WHERE id=? FOR UPDATE";
        return this.dbSession.getObject(sql, new Object[]{id}, this.cls);
    }

    public int delete(Serializable id) {
        String sql = "DELETE FROM " + this.tableName + " WHERE id=?";
        return this.dbSession.update(sql, new Object[]{id});
    }

    public int delete(List<? extends Serializable> ids) {
        if ((ids == null) || (ids.size() == 0)) {
            return 0;
        }
        StringBuilder sb = new StringBuilder().append("DELETE FROM ").append(this.tableName).append(" WHERE id");

        String sql = sb.toString();
        Object[] args = null;
        if (ids.size() == 1) {
            sql += "=?";
            args = new Object[]{ids.get(0)};
        } else {
            sql += " in(" + getQuestionNumber(ids.size()) + ")";
            args = ids.toArray();
        }
        System.out.println("检测删除sql：" + sql);
        return this.dbSession.update(sql, args);
    }
}
