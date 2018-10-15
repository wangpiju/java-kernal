package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.users.Domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("domainDao")
public class DomainDao
        extends BaseDao<Domain> {
    public void save(Domain m) {
        String sql = new SQLUtils(this.tableName).field("url").getInsert();
        this.dbSession.update(sql, new Object[]{m.getUrl()});
    }

    public void addByAccount(String account, List<Integer> ids) {
        String sql = new SQLUtils("t_user_domain").field("id,account").getInsert();
        for (Integer id : ids) {
            this.dbSession.update(sql, new Object[]{id, account});
        }
    }

    public int update(Domain m) {
        String sql = new SQLUtils(this.tableName).field("url").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{m.getUrl(), m.getId()});
    }

    public int setStatus(Integer id, Integer status) {
        String sql = new SQLUtils(this.tableName).field("status").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{status, id});
    }

    public List<Domain> listByAccount(String account) {
        String sql = "SELECT * FROM t_domain WHERE id in(SELECT id FROM t_user_domain WHERE account=?)";
        return this.dbSession.list(sql, new Object[]{account}, this.cls);
    }

    public Object listByAccountNot(String account) {
        String sql = "SELECT * FROM t_domain WHERE id not in(SELECT id FROM t_user_domain WHERE account=?)";
        return this.dbSession.list(sql, new Object[]{account}, this.cls);
    }

    public int deleteByAccount(String account, List<Integer> ids) {
        String sql = "DELETE FROM t_user_domain WHERE account=? AND id in(" + getQuestionNumber(ids.size()) + ")";
        List<Object> args = new ArrayList();
        args.add(account);
        args.addAll(ids);
        return this.dbSession.update(sql, args.toArray());
    }
}
