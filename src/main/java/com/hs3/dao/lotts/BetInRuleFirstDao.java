package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInRuleFirst;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("betInRuleFirstDao")
public class BetInRuleFirstDao
        extends BaseDao<BetInRuleFirst> {
    public List<BetInRuleFirst> listByCond(BetInRuleFirst m, Page p) {
        String sql = "SELECT * FROM t_bet_in_rule_first WHERE 1 = 1";

        List<Object> cond = new ArrayList();
        if (m.getAmount() != null) {
            sql = sql + " AND amount >= ?";
            cond.add(m.getAmount());
        }
        sql = sql + " ORDER BY amount";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, p);
    }

    public void save(BetInRuleFirst m) {
        saveAuto(m);
    }

    public int update(BetInRuleFirst m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"amount", "priceMin", "priceMax"};
    }

    protected Object[] getValues(BetInRuleFirst m) {
        return new Object[]{m.getAmount(), m.getPriceMin(), m.getPriceMax()};
    }
}
