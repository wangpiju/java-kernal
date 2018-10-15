package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInRuleKill;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("betInRuleKillDao")
public class BetInRuleKillDao
        extends BaseDao<BetInRuleKill> {
    private static final String SQL_updateHadKill = "UPDATE t_bet_in_rule_kill SET hadKillAmount = hadKillAmount + ?, hadKillCount = hadKillCount + ?, status = ? WHERE id = ?";

    public List<BetInRuleKill> listByCond(BetInRuleKill m, Page p) {
        String sql = "SELECT * FROM t_bet_in_rule_kill WHERE 1 = 1";

        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
            sql = sql + " AND account = ?";
            cond.add(m.getAccount());
        }
        if (m.getType() != null) {
            sql = sql + " AND type = ?";
            cond.add(m.getType());
        }
        if (m.getStatus() != null) {
            sql = sql + " AND status = ?";
            cond.add(m.getStatus());
        }
        sql = sql + " ORDER BY id DESC";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, p);
    }

    public int updateHadKill(BigDecimal hadKillAmount, BigDecimal hadKillCount, Integer status, Integer id) {
        return this.dbSession.update("UPDATE t_bet_in_rule_kill SET hadKillAmount = hadKillAmount + ?, hadKillCount = hadKillCount + ?, status = ? WHERE id = ?", new Object[]{hadKillAmount, hadKillCount, status, id});
    }

    public void save(BetInRuleKill m) {
        saveAuto(m);
    }

    public int update(BetInRuleKill m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"account", "killAmount", "killCount", "bothStatus", "killPriceMin", "killPriceMax", "type", "hadKillAmount", "hadKillCount", "status"};
    }

    protected Object[] getValues(BetInRuleKill m) {
        return new Object[]{m.getAccount(), m.getKillAmount(), m.getKillCount(), m.getBothStatus(), m.getKillPriceMin(), m.getKillPriceMax(), m.getType(), m.getHadKillAmount(), m.getHadKillCount(),
                m.getStatus()};
    }
}
