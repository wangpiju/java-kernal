package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInTime;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("betInTimeDao")
public class BetInTimeDao
        extends BaseDao<BetInTime> {
    private static final String SQL_listByTime = "SELECT * FROM t_bet_in_time WHERE startTime <= ? AND endTime >= ?";

    public List<BetInTime> listByCond(BetInTime m, Page p) {
        String sql = "SELECT * FROM t_bet_in_time WHERE 1 = 1";

        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{m.getStartTime()})) {
            sql = sql + " AND startTime = ?";
            cond.add(m.getStartTime());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getEndTime()})) {
            sql = sql + " AND endTime = ?";
            cond.add(m.getEndTime());
        }
        sql = sql + " ORDER BY startTime, ruleId";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, p);
    }

    public List<BetInTime> listByTime(String time) {
        return this.dbSession.list("SELECT * FROM t_bet_in_time WHERE startTime <= ? AND endTime >= ?", new Object[]{time, time}, this.cls);
    }

    public int delete(String startTime, String endTime) {
        String sql = "DELETE FROM " + this.tableName + " WHERE startTime=? AND endTime = ?";
        return this.dbSession.update(sql, new Object[]{startTime, endTime});
    }

    public void save(BetInTime m) {
        saveAuto(m);
    }

    public int update(BetInTime m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"startTime", "endTime", "ruleId", "probability"};
    }

    protected Object[] getValues(BetInTime m) {
        return new Object[]{m.getStartTime(), m.getEndTime(), m.getRuleId(), m.getProbability()};
    }
}
