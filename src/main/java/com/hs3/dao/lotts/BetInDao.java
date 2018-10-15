package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetIn;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("betInDao")
public class BetInDao
        extends BaseDao<BetIn> {
    public void save(BetIn m) {
        saveAuto(m);
    }

    public int update(BetIn m) {
        return updateByIdAuto(m, m.getId());
    }

    public int countByAccount(String account) {
        return this.dbSession.getInt("SELECT count(1) FROM t_bet_in WHERE account = ?", new Object[]{account}).intValue();
    }

    public int deleteByBetId(String betId) {
        String sql = "DELETE FROM " + this.tableName + " WHERE betId=?";
        return this.dbSession.update(sql, new Object[]{betId});
    }

    public List<BetIn> listByCond(BetIn m, Date startTime, Date endTime, Page page) {
        String sql = "SELECT * FROM t_bet_in WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
            sql = sql + " AND account = ?";
            cond.add(m.getAccount());
        }
        if (m.getTest() != null) {
            sql = sql + " AND test = ?";
            cond.add(m.getTest());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getBetId()})) {
            sql = sql + " AND betId = ?";
            cond.add(m.getBetId());
        }
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            cond.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            cond.add(endTime);
        }
        sql = sql + " ORDER BY createTime desc";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    protected String[] getColumns() {
        return new String[]{"account", "test", "betId", "amount", "multiples", "win", "createTime"};
    }

    protected Object[] getValues(BetIn m) {
        return new Object[]{m.getAccount(), m.getTest(), m.getBetId(), m.getAmount(), m.getMultiples(), m.getWin(), m.getCreateTime()};
    }
}
