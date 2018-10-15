package com.hs3.dao.roles;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.roles.Role;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("roleDao")
public class RoleDao
        extends BaseDao<Role> {
    public List<Role> listByIds(List<Integer> ids) {
        String sql = new SQLUtils(this.tableName).where("id in (" + getQuestionNumber(ids.size()) + ")").getSelect();
        return this.dbSession.list(sql, ids.toArray(), this.cls);
    }

    public void save(Role m) {
        String sql = new SQLUtils(this.tableName).field("name,remark").getInsert();
        this.dbSession.update(sql, new Object[]{
                m.getName(),
                m.getRemark()});
    }

    public int update(Role m) {
        String sql = new SQLUtils(this.tableName).field("name,remark").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{
                m.getName(),
                m.getRemark(),
                m.getId()});
    }
}
