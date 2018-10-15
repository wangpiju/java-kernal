package com.hs3.dao.sys;

import com.hs3.dao.LogBaseDao;
import com.hs3.db.Page;
import com.hs3.entity.sys.LogAll;
import com.hs3.utils.StrUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("logAllDao")
public class LogAllDao
        extends LogBaseDao<LogAll> {

    public List<LogAll> listByCond(LogAll m, Date startTime, Date endTime, Page page) {
        String sql = "SELECT * FROM t_log_all WHERE 1 = 1";
        List<Object> cond = new ArrayList<>();
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            cond.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            cond.add(endTime);
        }
        if (m.getId() != null) {
            sql = sql + " AND id = ?";
            cond.add(m.getId());
        }
        if (m.getBeginTime() != null) {
            sql = sql + " AND beginTime = ?";
            cond.add(m.getBeginTime());
        }
        if (m.getEndTime() != null) {
            sql = sql + " AND endTime = ?";
            cond.add(m.getEndTime());
        }
        if (m.getDuringTime() != null) {
            sql = sql + " AND duringTime >= ?";
            cond.add(m.getDuringTime());
        }
        if (m.getMaxMemory() != null) {
            sql = sql + " AND maxMemory = ?";
            cond.add(m.getMaxMemory());
        }
        if (m.getTotalMemory() != null) {
            sql = sql + " AND totalMemory = ?";
            cond.add(m.getTotalMemory());
        }
        if (m.getFreeMemory() != null) {
            sql = sql + " AND freeMemory = ?";
            cond.add(m.getFreeMemory());
        }
        if (!StrUtils.hasEmpty(m.getAccount())) {
            sql = sql + " AND account = ?";
            cond.add(m.getAccount());
        }
        if (!StrUtils.hasEmpty(m.getRemoteAddr())) {
            sql = sql + " AND remoteAddr = ?";
            cond.add(m.getRemoteAddr());
        }
        if (!StrUtils.hasEmpty(m.getUserAgent())) {
            sql = sql + " AND userAgent = ?";
            cond.add(m.getUserAgent());
        }
        if (!StrUtils.hasEmpty(m.getRequestUri())) {
            sql = sql + " AND requestUri like ?";
            cond.add("%" + m.getRequestUri() + "%");
        }
        if (!StrUtils.hasEmpty(m.getMethod())) {
            sql = sql + " AND method = ?";
            cond.add(m.getMethod());
        }
        if (m.getParams() != null) {
            sql = sql + " AND params = ?";
            cond.add(m.getParams());
        }
        if (m.getException() != null) {
            sql = sql + " AND exception = ?";
            cond.add(m.getException());
        }
        if (!StrUtils.hasEmpty(m.getK1())) {
            sql = sql + " AND k1 = ?";
            cond.add(m.getK1());
        }
        if (!StrUtils.hasEmpty(m.getK2())) {
            sql = sql + " AND k2 = ?";
            cond.add(m.getK2());
        }
        if (!StrUtils.hasEmpty(m.getK3())) {
            sql = sql + " AND k3 = ?";
            cond.add(m.getK3());
        }
        if (!StrUtils.hasEmpty(m.getK4())) {
            sql = sql + " AND k4 = ?";
            cond.add(m.getK4());
        }
        if (!StrUtils.hasEmpty(m.getK5())) {
            sql = sql + " AND k5 = ?";
            cond.add(m.getK5());
        }
        if (!StrUtils.hasEmpty(m.getKext())) {
            sql = sql + " AND kext = ?";
            cond.add(m.getKext());
        }
        if (!StrUtils.hasEmpty(m.getC1())) {
            sql = sql + " AND c1 = ?";
            cond.add(m.getC1());
        }
        if (!StrUtils.hasEmpty(m.getC2())) {
            sql = sql + " AND c2 = ?";
            cond.add(m.getC2());
        }
        if (!StrUtils.hasEmpty(m.getC3())) {
            sql = sql + " AND c3 = ?";
            cond.add(m.getC3());
        }
        if (!StrUtils.hasEmpty(m.getC4())) {
            sql = sql + " AND c4 = ?";
            cond.add(m.getC4());
        }
        if (!StrUtils.hasEmpty(m.getC5())) {
            sql = sql + " AND c5 = ?";
            cond.add(m.getC5());
        }
        if (!StrUtils.hasEmpty(m.getCext())) {
            sql = sql + " AND cext = ?";
            cond.add(m.getCext());
        }
        if (!StrUtils.hasEmpty(m.getExtends1())) {
            sql = sql + " AND extends1 = ?";
            cond.add(m.getExtends1());
        }
        if (!StrUtils.hasEmpty(m.getExtends2())) {
            sql = sql + " AND extends2 = ?";
            cond.add(m.getExtends2());
        }
        if (!StrUtils.hasEmpty(m.getExtends3())) {
            sql = sql + " AND extends3 = ?";
            cond.add(m.getExtends3());
        }
        sql = sql + " ORDER BY createTime desc";
        return this.logDbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public void save(LogAll m) {
        saveAuto(m);
    }

    public int update(LogAll m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"beginTime", "endTime", "duringTime", "maxMemory", "totalMemory", "freeMemory", "account", "remoteAddr", "userAgent", "requestUri", "method", "params", "exception",
                "createTime", "k1", "k2", "k3", "k4", "k5", "kext", "c1", "c2", "c3", "c4", "c5", "cext", "extends1", "extends2", "extends3"};
    }

    protected Object[] getValues(LogAll m) {
        return new Object[]{m.getBeginTime(), m.getEndTime(), m.getDuringTime(), m.getMaxMemory(), m.getTotalMemory(), m.getFreeMemory(), m.getAccount(), m.getRemoteAddr(), m.getUserAgent(),
                m.getRequestUri(), m.getMethod(), m.getParams(), m.getException(), m.getCreateTime(), m.getK1(), m.getK2(), m.getK3(), m.getK4(), m.getK5(), m.getKext(), m.getC1(), m.getC2(),
                m.getC3(), m.getC4(), m.getC5(), m.getCext(), m.getExtends1(), m.getExtends2(), m.getExtends3()};
    }


    //**************************************以下为变更部分*****************************************

    public LogAll findByAccountAndCreateTime(String account, Date createTime) {
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE account=? AND createTime=?");
        Object[] args = {account, createTime};
        List<LogAll> listBet = this.logDbSession.list(sql, args, this.cls);
        LogAll logAll = null;
        if (listBet.size() > 0) {
            logAll = listBet.get(0);
        }
        return logAll;
    }


}
