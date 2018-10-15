package com.hs3.dao.sys;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.sys.Log4j;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class Log4jDao
        extends BaseDao<Log4j> {
    public List<Log4j> list(String lever, String clazz, String method, String message, Date begin, Date end, Page p) {
        SQLUtils su = new SQLUtils(this.tableName).where("1=1");
        List<Object> args = new ArrayList();
        if (begin != null) {
            su.where(" AND createdate>=?");
            args.add(begin);
        }
        if (end != null) {
            su.where(" AND createdate<=?");
            args.add(end);
        }
        if (!StrUtils.hasEmpty(new Object[]{lever})) {
            su.where(" AND lever=?");
            args.add(lever);
        }
        if (!StrUtils.hasEmpty(new Object[]{clazz})) {
            su.where(" AND clazz LIKE ?");
            args.add("%" + clazz);
        }
        if (!StrUtils.hasEmpty(new Object[]{method})) {
            su.where(" AND method=?");
            args.add(method);
        }
        if (!StrUtils.hasEmpty(new Object[]{message})) {
            su.where(" AND message LIKE ?");
            args.add("%" + message + "%");
        }
        su.orderBy("createdate DESC");
        String sql = su.getSelect();
        String pageSql = su.getCount();
        return this.dbSession.listAndPage(sql, args.toArray(), pageSql, args.toArray(), this.cls, p);
    }
}
