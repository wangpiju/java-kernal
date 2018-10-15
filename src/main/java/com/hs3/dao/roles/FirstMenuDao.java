package com.hs3.dao.roles;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.roles.FirstMenu;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("firstMenuDao")
public class FirstMenuDao
        extends BaseDao<FirstMenu> {
    public List<FirstMenu> list() {
        String sql = new SQLUtils(this.tableName).getSelect();
        return this.dbSession.list(sql, this.cls);
    }

    public void save(FirstMenu m) {
        String sql = new SQLUtils(this.tableName).field("firstName").getInsert();
        this.dbSession.update(sql, new Object[]{m.getFirstName()});
    }

    public int update(FirstMenu m) {
        String sql = new SQLUtils(this.tableName).field("firstName").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{m.getFirstName(), m.getId()});
    }
}
