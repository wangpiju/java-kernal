package com.hs3.dao.roles;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.roles.SecondMenu;
import com.hs3.models.roles.SecondMenuEx;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("secondMenuDao")
public class SecondMenuDao
        extends BaseDao<SecondMenu> {
    public List<SecondMenu> list(Integer firstMenuId) {
        String sql = new SQLUtils(this.tableName).where(" firstMenuId=?").getSelect();
        return this.dbSession.list(sql, new Object[]{firstMenuId}, this.cls);
    }

    public void save(SecondMenu m) {
        String sql = new SQLUtils(this.tableName).field("firstMenuId,secondName").getInsert();
        this.dbSession.update(sql, new Object[]{m.getFirstMenuId(), m.getSecondName()});
    }

    public int update(SecondMenu m) {
        String sql = new SQLUtils(this.tableName).field("firstMenuId,secondName").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{m.getFirstMenuId(), m.getSecondName(), m.getId()});
    }

    public List<SecondMenuEx> listEx(Page page) {
        String sql = "SELECT a.id,a.firstMenuId,b.firstName,a.secondName from t_second_menu a LEFT JOIN t_first_menu b on a.firstMenuId=b.id order by b.firstName";
        return this.dbSession.list(sql, SecondMenuEx.class, page);
    }

    public SecondMenuEx findEx(Integer id) {
        String sql = "SELECT a.id,a.firstMenuId,b.firstName,a.secondName from t_second_menu a LEFT JOIN t_first_menu b on a.firstMenuId=b.id where a.id=?";
        return this.dbSession.getObject(sql, new Object[]{id}, SecondMenuEx.class);
    }
}
