package com.hs3.dao.sys;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.sys.LoginIpWhite;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class LoginIpWhiteDao
        extends BaseDao<LoginIpWhite> {
    public void save(String ip) {
        String sql = new SQLUtils(this.tableName).field("ip").getInsert();
        this.dbSession.update(sql, new Object[]{ip});
    }

    public List<String> listString(Page p) {
        String sql = new SQLUtils(this.tableName).field("ip").getSelect();
        return this.dbSession.listSerializable(sql, String.class, p);
    }

    public List<LoginIpWhite> listByCond(LoginIpWhite white, Page page) {
        String sql = "SELECT * FROM t_login_ip_white WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{white.getIp()})) {
            sql = sql + " AND ip = ?";
            cond.add(white.getIp());
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
