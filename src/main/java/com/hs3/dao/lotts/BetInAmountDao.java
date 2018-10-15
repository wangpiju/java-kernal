package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInAmount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("betInAmountDao")
public class BetInAmountDao
        extends BaseDao<BetInAmount> {
    private static final String SQL_listByRuleId = "SELECT t1.* FROM t_bet_in_amount t1, t_bet_in_price t2 WHERE t1.ruleId = ? AND t1.amount = ? AND t1.priceId = t2.id ORDER BY t2.end DESC";
    private static final String SQL_listAmount = "SELECT DISTINCT amount, name FROM t_bet_in_amount WHERE ruleId = ? ORDER BY amount";

    public List<BetInAmount> listByCond(BetInAmount m, Page p) {
        String sql = "SELECT t1.* FROM t_bet_in_amount t1, t_bet_in_price t2 WHERE t1.priceId = t2.id";

        List<Object> cond = new ArrayList();
        if (m.getRuleId() != null) {
            sql = sql + " AND t1.ruleId = ?";
            cond.add(m.getRuleId());
        }
        sql = sql + " ORDER BY t1.ruleId, t1.amount, t2.end DESC";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, p);
    }

    public List<BetInAmount> listByRuleId(Integer ruleId, BigDecimal amount) {
        return this.dbSession.list("SELECT t1.* FROM t_bet_in_amount t1, t_bet_in_price t2 WHERE t1.ruleId = ? AND t1.amount = ? AND t1.priceId = t2.id ORDER BY t2.end DESC", new Object[]{ruleId, amount}, this.cls);
    }

    public List<BetInAmount> listAmount(Integer ruleId) {
        return this.dbSession.list("SELECT DISTINCT amount, name FROM t_bet_in_amount WHERE ruleId = ? ORDER BY amount", new Object[]{ruleId}, this.cls);
    }

    public void save(BetInAmount m) {
        saveAuto(m);
    }

    public int update(BetInAmount m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"name", "amount", "probability", "priceId", "ruleId"};
    }

    protected Object[] getValues(BetInAmount m) {
        return new Object[]{m.getName(), m.getAmount(), m.getProbability(), m.getPriceId(), m.getRuleId()};
    }
}
