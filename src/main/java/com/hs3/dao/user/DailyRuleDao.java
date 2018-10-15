package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.users.DailyRule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("dailyRuleDao")
public class DailyRuleDao
        extends BaseDao<DailyRule> {
    public List<DailyRule> listByCond(DailyRule m, Page page) {
        String sql = "SELECT * FROM t_daily_rule WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if ((m != null) &&
                (m.getStatus() != null)) {
            sql = sql + " AND status = ?";
            cond.add(m.getStatus());
        }
        return this.dbSession.list(sql, cond.toArray(), this.cls, page);
    }

    public void save(DailyRule m) {
        saveAuto(m);
    }

    public int update(DailyRule m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"name", "rate", "minRate", "maxRate", "limitAmount", "validAccountCount", "betAmount", "lossStatus", "level", "status"};
    }

    protected Object[] getValues(DailyRule m) {
        return new Object[]{m.getName(), m.getRate(), m.getMinRate(), m.getMaxRate(), m.getLimitAmount(), m.getValidAccountCount(), m.getBetAmount(), m.getLossStatus(), m.getLevel(),
                m.getStatus()};
    }
}
