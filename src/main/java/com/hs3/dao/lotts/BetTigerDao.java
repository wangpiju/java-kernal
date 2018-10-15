package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetTiger;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("betTigerDao")
public class BetTigerDao
        extends BaseDao<BetTiger> {
    public static final String SQL_listByAccount = "SELECT * FROM t_bet_tiger WHERE account = ? ORDER BY createTime desc limit ?";

    public void save(BetTiger m) {
        saveAuto(m);
    }

    public int update(BetTiger m) {
        return updateByIdAuto(m, m.getId());
    }

    public List<BetTiger> listByAccount(String account, int count) {
        return this.dbSession.list("SELECT * FROM t_bet_tiger WHERE account = ? ORDER BY createTime desc limit ?", new Object[]{account, Integer.valueOf(count)}, this.cls);
    }

    public List<BetTiger> listByCond(BetTiger m, Date startTime, Date endTime, Page page) {
        String sql = "SELECT * FROM t_bet_tiger WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
            sql = sql + " AND account = ?";
            cond.add(m.getAccount());
        }
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            cond.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            cond.add(endTime);
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getOpenNum()})) {
            sql = sql + " AND openNum = ?";
            cond.add(m.getOpenNum());
        }
        sql = sql + " ORDER BY createTime desc";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    protected String[] getColumns() {
        return new String[]{"account", "createTime", "openNum"};
    }

    protected Object[] getValues(BetTiger m) {
        return new Object[]{m.getAccount(), m.getCreateTime(), m.getOpenNum()};
    }
}
