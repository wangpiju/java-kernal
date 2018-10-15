package com.hs3.dao.finance;

import com.hs3.dao.BaseDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.finance.RechargeLower;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("rechargeLowerDao")
public class RechargeLowerDao
        extends BaseDao<RechargeLower> {
    public List<RechargeLower> listByCond(RechargeLower m, Date startTime, Date endTime, Page page) {
        String sql = "SELECT *," + UserDao.getMarkSQL("i.sourceAccount") + " FROM t_recharge_lower i WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{m.getSourceAccount()})) {
            sql = sql + " AND sourceAccount = ?";
            cond.add(m.getSourceAccount());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getTargetAccount()})) {
            sql = sql + " AND targetAccount = ?";
            cond.add(m.getTargetAccount());
        }
        if (m.getStatus() != null) {
            sql = sql + " AND status = ?";
            cond.add(m.getStatus());
        }
        if (m.getTest() != null) {
            sql = sql + " AND test = ?";
            cond.add(m.getTest());
        }
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            cond.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            cond.add(endTime);
        }
        sql = sql + " ORDER BY createTime desc, id desc";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public void save(RechargeLower m) {
        saveAuto(m);
    }

    public int update(RechargeLower m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"id", "status", "amount", "sourceAccount", "targetAccount", "createTime", "lastTime", "operator", "remark", "test"};
    }

    protected Object[] getValues(RechargeLower m) {
        return new Object[]{m.getId(), m.getStatus(), m.getAmount(), m.getSourceAccount(), m.getTargetAccount(), m.getCreateTime(), m.getLastTime(), m.getOperator(), m.getRemark(), m.getTest()};
    }

    public RechargeLower findByAccount(String account, Integer status) {
        String sql = new SQLUtils(this.tableName).where("targetAccount=? AND status=? LIMIT 1").getSelect();
        Object[] args = {account, status};
        return (RechargeLower) this.dbSession.getObject(sql, args, this.cls);
    }
}
