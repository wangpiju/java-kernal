package com.hs3.dao.roles;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.roles.RoleMapJurisdiction;
import com.hs3.models.roles.JurisdictionEx;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("roleMapJurisdictionDao")
public class RoleMapJurisdictionDao
        extends BaseDao<RoleMapJurisdiction> {
    public List<Integer> listJurisdictionIds(Integer roleId, Page p) {
        String sql = new SQLUtils(this.tableName).field("jurisdictionId").where("roleId=?").getSelect();
        return this.dbSession.listSerializable(sql, new Object[]{roleId}, Integer.class, p);
    }

    public List<Integer> listJurisdictionIds(List<Integer> roleId, Page p) {
        String sql = new SQLUtils(this.tableName).field("jurisdictionId").where("roleId in(" + getQuestionNumber(roleId.size()) + ")").getSelect();
        return this.dbSession.listSerializable(sql, roleId.toArray(), Integer.class, p);
    }

    public boolean delete(Integer roleId, List<Integer> jurIds) {
        String sql = new SQLUtils(this.tableName).where("jurisdictionId in(" + getQuestionNumber(jurIds.size()) + ")").where(" AND roleId=?")
                .getDelete();
        jurIds.add(roleId);
        return this.dbSession.update(sql, jurIds.toArray()) > 0;
    }

    public boolean delete(Integer roleId) {
        String sql = new SQLUtils(this.tableName).where(" roleId=?")
                .getDelete();
        return this.dbSession.update(sql, new Object[]{roleId}) > 0;
    }

    public void save(RoleMapJurisdiction jur) {
        save(jur.getRoleId(), jur.getJurisdictionId());
    }

    public void save(Integer roleId, Integer jurId) {
        String sql = new SQLUtils(this.tableName).field("jurisdictionId,roleId").getInsert();
        this.dbSession.update(sql, new Object[]{jurId, roleId});
    }

    public List<JurisdictionEx> listRJEx(Page page, Integer roleId) {
        String sql = "select a.id,c.firstName,b.secondName,a.secondMenuId,path,remark,d.jurisdictionId as role_jur_id  from t_jurisdiction a left join t_second_Menu b on a.secondMenuId=b.id left join t_first_Menu c on a.firstMenuId=c.id LEFT JOIN t_role_map_jurisdiction d on a.id=d.jurisdictionId and d.roleId=? order by c.firstName,b.secondName,a.remark";


        return this.dbSession.list(sql, new Object[]{roleId}, JurisdictionEx.class);
    }
}
