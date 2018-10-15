package com.hs3.dao.sys;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.sys.LoginIpBlack;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class LoginIpBlackDao
        extends BaseDao<LoginIpBlack> {
    private static final String SELECT = "SELECT * FROM t_login_ip_black WHERE ip = ?";
    private static final String UPDATE = "UPDATE t_login_ip_black set remark = ? WHERE ip = ?";

    public LoginIpBlack find(String ip) {
        return (LoginIpBlack) this.dbSession.getObject("SELECT * FROM t_login_ip_black WHERE ip = ?", new Object[]{ip}, this.cls);
    }

    public int update(LoginIpBlack black) {
        return this.dbSession.update("UPDATE t_login_ip_black set remark = ? WHERE ip = ?", new Object[]{black.getRemark(), black.getIp()});
    }

    public void save(LoginIpBlack black) {
        String sql = new SQLUtils(this.tableName).field("ip, remark").getInsert();
        this.dbSession.update(sql, new Object[]{black.getIp(), black.getRemark()});
    }

    public List<String> listString(Page p) {
        String sql = new SQLUtils(this.tableName).field("ip").getSelect();
        return this.dbSession.listSerializable(sql, String.class, p);
    }

    public List<LoginIpBlack> listByCond(LoginIpBlack black, Page page) {
        String sql = "SELECT * FROM t_login_ip_black WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{black.getIp()})) {
            sql = sql + " AND ip = ?";
            cond.add(black.getIp());
        }
        if (!StrUtils.hasEmpty(new Object[]{black.getRemark()})) {
            sql = sql + " AND remark like ?";
            cond.add("%" + black.getRemark() + "%");
        }
        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public int deleteByIps(List<String> ips) {
        if ((ips == null) || (ips.size() == 0)) {
            return 0;
        }
        StringBuilder sb = new StringBuilder().append("DELETE FROM ").append(this.tableName).append(" WHERE ip");

        String sql = null;
        Object[] args = null;
        if (ips.size() == 1) {
            sql = "=?";
            args = new Object[]{ips.get(0)};
        } else {
            sql = " in(" + getQuestionNumber(ips.size()) + ")";
            args = ips.toArray();
        }
        return this.dbSession.update(sql, args);
    }
}
