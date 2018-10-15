package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.users.UserLoginIp;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class UserLoginIpDao
        extends BaseDao<UserLoginIp> {
    private static final String LastIp = "SELECT ip FROM t_user_login_ip WHERE account = ? ORDER BY loginTime DESC LIMIT 1";

    public List<UserLoginIp> listByIp(String ip, String account, String ipInfo, Page p) {
        SQLUtils s = new SQLUtils(this.tableName + " as i").field("i.*," + UserDao.getMarkSQL());
        List<Object> args = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{ip})) {
            s.where("ip=?");
            args.add(ip);
        }
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            s.whereAnd("account=?");
            args.add(account);
        }
        if (!StrUtils.hasEmpty(new Object[]{ipInfo})) {
            s.whereAnd("ipInfo like CONCAT('%', ?, '%')");
            args.add(ipInfo);
        }
        s.orderBy("loginTime DESC");
        String sql = s.getSelect();
        return this.dbSession.list(sql, args.toArray(), this.cls, p);
    }

    public int countByLogin(String account) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"count(1)", this.tableName, "WHERE date(loginTime) = curdate() and account=?"});
        Object[] args = {account};
        return this.dbSession.getInt(sql, args).intValue();
    }

    public String getLastIp(String account) {
        Object[] args = {account};
        return this.dbSession.getString("SELECT ip FROM t_user_login_ip WHERE account = ? ORDER BY loginTime DESC LIMIT 1", args);
    }

    public int countByFirstLogin(String account, Date date) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"count(1)", this.tableName, "WHERE account=? and loginTime< ? "});
        Object[] args = {account, date};
        return this.dbSession.getInt(sql, args).intValue();
    }

    public int updateIpInfo(String ipInfo, String account,String ip) {
        String sql= String.format("update %s set ipInfo=? where account=? and ip=? ", this.tableName);
        return this.dbSession.update(sql, new Object[]{ipInfo, account, ip});
    }
    public int updateIpInfoByIp(String ipInfo,String ip, String oldIpInfo) {
        String sql= String.format("update %s set ipInfo=? where ip=? and ipInfo=?", this.tableName);
        return this.dbSession.update(sql, new Object[]{ipInfo, ip, oldIpInfo});
    }
}
