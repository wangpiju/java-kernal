package com.hs3.dao.sys;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.sys.SysConfig;
import org.springframework.stereotype.Repository;

@Repository("sysConfigDao")
public class SysConfigDao
        extends BaseDao<SysConfig> {
    public void save(SysConfig m) {
        String sql = new SQLUtils(this.tableName).field("val,remark,id").getInsert();
        this.dbSession.update(sql, new Object[]{m.getVal(), m.getRemark(), m.getId()});
    }

    public int update(SysConfig m) {
        String sql = new SQLUtils(this.tableName).field("val,remark").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{m.getVal(), m.getRemark(), m.getId()});
    }
}
