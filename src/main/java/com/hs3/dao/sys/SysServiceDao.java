package com.hs3.dao.sys;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.sys.SysService;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("sysServiceDao")
public class SysServiceDao
        extends BaseDao<SysService> {
    public List<SysService> listByCond(SysService m, Page page) {
        String sql = "SELECT * FROM t_sys_service WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{m.getName()})) {
            sql = sql + " AND name = ?";
            cond.add(m.getName());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStatus()})) {
            sql = sql + " AND status = ?";
            cond.add(m.getStatus());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getUserMark()})) {
            sql = sql + " AND userMark = ?";
            cond.add(m.getUserMark());
        }
        sql = sql + " ORDER BY id";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public void save(SysService m) {
        saveAuto(m);
    }

    public int update(SysService m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"name", "status", "userMark", "link"};
    }

    protected Object[] getValues(SysService m) {
        return new Object[]{m.getName(), m.getStatus(), m.getUserMark(), m.getLink()};
    }
}
