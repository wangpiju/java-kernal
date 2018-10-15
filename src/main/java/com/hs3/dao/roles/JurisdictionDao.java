package com.hs3.dao.roles;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.roles.Jurisdiction;
import com.hs3.models.roles.JurisdictionEx;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("jurisdictionDao")
public class JurisdictionDao
        extends BaseDao<Jurisdiction> {
    public List<Jurisdiction> listByIds(List<Integer> ids) {
        String sql = new SQLUtils(this.tableName).where("id in (" + getQuestionNumber(ids.size()) + ")").getSelect();
        return this.dbSession.list(sql, ids.toArray(), this.cls);
    }

    public List<Jurisdiction> listNoByIds(List<Integer> ids) {
        String sql = new SQLUtils(this.tableName).where("id NOT IN (" + getQuestionNumber(ids.size()) + ")").getSelect();
        return this.dbSession.list(sql, ids.toArray(), this.cls);
    }

    public List<String> listPathByIds(List<Integer> ids) {
        String sql = new SQLUtils(this.tableName).field("path").where("id in (" + getQuestionNumber(ids.size()) + ")").getSelect();
        return this.dbSession.listSerializable(sql, ids.toArray(), String.class);
    }

    public List<String> listById(Integer id) {
        String sql = new SQLUtils(this.tableName).field("path").where("id=?").getSelect();
        return this.dbSession.listSerializable(sql, new Object[]{id}, String.class);
    }

    public void save(Jurisdiction m) {
        String sql = new SQLUtils(this.tableName).field("firstMenuId,secondMenuId,path,remark").getInsert();
        this.dbSession.update(sql, new Object[]{m.getFirstMenuId(), m.getSecondMenuId(), m.getPath(), m.getRemark()});
    }

    public int update(Jurisdiction m) {
        String sql = new SQLUtils(this.tableName).field("firstMenuId,secondMenuId,path,remark").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{m.getFirstMenuId(), m.getSecondMenuId(), m.getPath(), m.getRemark(), m.getId()});
    }

    public List<JurisdictionEx> listEx(Page page) {
        String sql = "select a.id,c.firstName,b.secondName,a.firstMenuId,a.secondMenuId,path,remark from t_jurisdiction a left join t_second_Menu b on a.secondMenuId=b.id left join t_first_Menu c on a.firstMenuId=c.id order by c.firstName,b.secondName,remark ";
        return this.dbSession.list(sql, JurisdictionEx.class, page);
    }

    public JurisdictionEx findEx(String id) {
        String sql = "select a.id,c.firstName,b.secondName,a.firstMenuId,a.secondMenuId,path,remark from t_jurisdiction a left join t_second_Menu b on a.secondMenuId=b.id left join t_first_Menu c on a.firstMenuId=c.id where a.id=?";
        return (JurisdictionEx) this.dbSession.getObject(sql, new Object[]{id}, JurisdictionEx.class);
    }

    public List<Jurisdiction> listAndOrder(Page p) {
        String sql = new SQLUtils(this.tableName).orderBy("path").getSelect();
        return this.dbSession.list(sql, this.cls, p);
    }
}
