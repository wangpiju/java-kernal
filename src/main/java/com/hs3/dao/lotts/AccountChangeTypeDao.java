package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.AccountChangeType;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("accountChangeTypeDao")
public class AccountChangeTypeDao
        extends BaseDao<AccountChangeType> {
    private static final String INSERT = "INSERT INTO t_account_change_type (id,name,type,remark) VALUES(?,?,?,?)";
    private static final String UPDATE = "UPDATE t_account_change_type SET name=?,type=?,remark=? WHERE id=?";

    public List<AccountChangeType> list(Page page) {
        String sql = "SELECT * FROM " + this.tableName + " ORDER BY id";
        return this.dbSession.list(sql, this.cls, page);
    }

    public int update(AccountChangeType m) {
        return this.dbSession.update("UPDATE t_account_change_type SET name=?,type=?,remark=? WHERE id=?", new Object[]{
                m.getName(),
                Integer.valueOf(m.getType()),
                m.getRemark(),
                Integer.valueOf(m.getId())});
    }

    public void save(AccountChangeType m) {
        this.dbSession.update("INSERT INTO t_account_change_type (id,name,type,remark) VALUES(?,?,?,?)", new Object[]{
                Integer.valueOf(m.getId()),
                m.getName(),
                Integer.valueOf(m.getType()),
                m.getRemark()});
    }

    public List<AccountChangeType> listByType(Integer type) {
        SQLUtils u = new SQLUtils(this.tableName);
        if (type == null) {
            String sql = u.orderBy("type,id").getSelect();
            return this.dbSession.list(sql, this.cls);
        }
        String sql = u.where("type=?").orderBy("id").getSelect();
        return this.dbSession.list(sql, new Object[]{type}, this.cls);
    }
}
