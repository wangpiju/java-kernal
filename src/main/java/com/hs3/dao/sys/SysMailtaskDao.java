package com.hs3.dao.sys;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.sys.SysMailtask;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("sysMailtaskDao")
public class SysMailtaskDao
        extends BaseDao<SysMailtask> {
    public List<SysMailtask> listByCond(SysMailtask m, Page page) {
        String sql = "SELECT * FROM t_sys_mailtask WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (m.getId() != null) {
            sql = sql + " AND id = ?";
            cond.add(m.getId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
            sql = sql + " AND account = ?";
            cond.add(m.getAccount());
        }
        if (m.getStatus() != null) {
            sql = sql + " AND status = ?";
            cond.add(m.getStatus());
        }
        if (m.getType() != null) {
            sql = sql + " AND type = ?";
            cond.add(m.getType());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getTitle()})) {
            sql = sql + " AND title = ?";
            cond.add(m.getTitle());
        }
        if (m.getContent() != null) {
            sql = sql + " AND content = ?";
            cond.add(m.getContent());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getImgPath()})) {
            sql = sql + " AND imgPath = ?";
            cond.add(m.getImgPath());
        }
        sql = sql + " ORDER BY id DESC";
        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public int updateByStatus(int id, int status, int preStatus) {
        return this.dbSession.update("UPDATE t_sys_mailtask SET status = ?, changeTime = ? WHERE id = ? AND status = ?", new Object[]{Integer.valueOf(status), new Date(), Integer.valueOf(id), Integer.valueOf(preStatus)});
    }

    public void save(SysMailtask m) {
        saveAuto(m);
    }

    public int update(SysMailtask m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"account", "createTime", "changeTime", "status", "type", "title", "content", "imgPath"};
    }

    protected Object[] getValues(SysMailtask m) {
        return new Object[]{m.getAccount(), m.getCreateTime(), m.getChangeTime(), m.getStatus(), m.getType(), m.getTitle(), m.getContent(), m.getImgPath()};
    }
}
