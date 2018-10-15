package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.users.Manager;
import com.hs3.utils.RedisUtils;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("managerDao")
public class ManagerDao
        extends BaseDao<Manager> {
    public Manager findByAccountAndPassword(String account, String password) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE account=? AND password=?"});
        Object[] args = {account, StrUtils.MD5(password)};
        return this.dbSession.getObject(sql, args, this.cls);
    }

    public Manager findByAccount(String account) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE account=? "});
        Object[] args = {account};
        return this.dbSession.getObject(sql, args, this.cls);
    }

    public void save(Manager m) {
        String sql = new SQLUtils(this.tableName).field("account,niceName,password,role,mac,status").getInsert();
        this.dbSession.update(sql, new Object[]{
                m.getAccount(),
                m.getNiceName(),
                StrUtils.MD5(m.getPassword()),
                m.getRole(),
                m.getMac(),
                m.getStatus()});
    }

    public int update(Manager m) {
        SQLUtils sl = new SQLUtils(this.tableName).field("niceName,role,mac,status").where("id=?");
        List<Object> args = new ArrayList();
        args.add(m.getNiceName());
        args.add(m.getRole());
        args.add(m.getMac());
        args.add(m.getStatus());
        if (!StrUtils.hasEmpty(new Object[]{m.getPassword()})) {
            sl.field("password");
            args.add(StrUtils.MD5(m.getPassword()));
        }
        args.add(m.getId());
        String sql = sl.getUpdate();
        return this.dbSession.update(sql, args.toArray());
    }

    public int updatePassword(String account, String old, String password) {
        String sql = new SQLUtils(this.tableName).field("password").where("account=? and password=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{
                StrUtils.MD5(password),
                account,
                StrUtils.MD5(old)});
    }

    public boolean setSessionId(String account, String sessionId) {
        return RedisUtils.hset("MANAGER_SESSION_ID", account, sessionId);
    }

    public String getSessionId(String account) {
        return RedisUtils.hget("MANAGER_SESSION_ID", account);
    }

    public int updateAuthKey(String account, String key) {
        String sql = new SQLUtils(this.tableName).field("authKey").where("account=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{key, account});
    }

    public String getAuthKey(String account) {
        String sql = new SQLUtils(this.tableName).field("authKey").where("account=?").getSelect();
        return this.dbSession.getString(sql, new Object[]{account});
    }
}
