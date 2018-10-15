package com.hs3.dao;

import com.hs3.db.LogDbSession;
import com.hs3.db.Page;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class LogBaseDao<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected static final String SQL_UPDATE = "UPDATE %s SET %s WHERE %s";
    protected static final String SQL_INSERT = "INSERT INTO %s(%s) VALUES(%s)";
    protected static final String SQL_DELETE = "DELETE FROM %s WHERE id=?";
    protected static final String SQL_DELETE_IN = "DELETE FROM %s WHERE id in(%s)";
    protected static final String SQL_SELECT = "SELECT %s FROM %s %s";
    @Autowired
    protected LogDbSession logDbSession;
    protected Class<T> cls;
    protected String tableName;
    private String sqlSave;
    private String sqlUpdate;

    public LogBaseDao() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        this.cls = ((Class) parameterizedType.getActualTypeArguments()[0]);

        this.tableName = ("t" + this.cls.getSimpleName().replaceAll("([A-Z])", "_$1").toLowerCase());

        String[] columns = getColumns();
        if (columns != null) {
            StringBuilder s1 = new StringBuilder();
            StringBuilder s2 = new StringBuilder();
            StringBuilder s3 = new StringBuilder();
            for (String s : columns) {
                s1.append(",").append(s);
                s2.append(",").append(s).append("=?");
                s3.append(",?");
            }
            this.sqlSave = String.format("INSERT INTO %s(%s) VALUES(%s)", this.tableName, s1.substring(1), s3.substring(1));
            this.sqlUpdate = String.format("UPDATE %s SET %s WHERE %s", this.tableName, s2.substring(1), "id=?");
        }
        if (logDbSession != null) {
            System.out.println("---------------");
        }
    }

    public int saveAuto(T t) {
        return this.logDbSession.update(this.sqlSave, getValues(t));
    }

    public Integer saveAutoReturnId(T t) {
        return this.logDbSession.updateKeyHolder(this.sqlSave, getValues(t));
    }

    public int updateByIdAuto(T t, Serializable id) {
        Object[] objs = getValues(t);
        Object[] newObjes = new Object[objs.length + 1];
        System.arraycopy(objs, 0, newObjes, 0, objs.length);
        newObjes[objs.length] = id;
        return this.logDbSession.update(this.sqlUpdate, newObjes);
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
        return this.logDbSession.list(sql, this.cls, page);
    }

    public List<T> list(Page page) {
        String sql = "SELECT * FROM " + this.tableName;
        return this.logDbSession.list(sql, this.cls, page);
    }

    public T find(Serializable id) {
        String sql = "SELECT * FROM " + this.tableName + " WHERE id=?";
        return this.logDbSession.getObject(sql, new Object[]{id}, this.cls);
    }

    public T findLock(Serializable id) {
        String sql = "SELECT * FROM " + this.tableName + " WHERE id=? FOR UPDATE";
        return this.logDbSession.getObject(sql, new Object[]{id}, this.cls);
    }

    public int delete(Serializable id) {
        String sql = "DELETE FROM " + this.tableName + " WHERE id=?";
        return this.logDbSession.update(sql, new Object[]{id});
    }

    public int delete(List<? extends Serializable> ids) {
        if ((ids == null) || (ids.size() == 0)) {
            return 0;
        }
        String sql = "DELETE FROM " + this.tableName + " WHERE id";
        Object[] args = null;
        if (ids.size() == 1) {
            sql += "=?";
            args = new Object[]{ids.get(0)};
        } else {
            sql += " in(" + getQuestionNumber(ids.size()) + ")";
            args = ids.toArray();
        }
        System.out.println("检测删除sql：" + sql);
        return this.logDbSession.update(sql, args);
    }

}
