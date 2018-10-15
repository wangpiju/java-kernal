package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.entity.lotts.BetInConfig;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

@Repository("betInConfigDao")
public class BetInConfigDao
        extends BaseDao<BetInConfig> {
    private static final String SQL_updateTotalAmount = "UPDATE t_bet_in_config SET totalAmount = totalAmount + ? WHERE id = ?";

    public int updateTotalAmount(Integer id, BigDecimal amount) {
        return this.dbSession.update("UPDATE t_bet_in_config SET totalAmount = totalAmount + ? WHERE id = ?", new Object[]{amount, id});
    }

    public void save(BetInConfig m) {
        saveAuto(m);
    }

    public int update(BetInConfig m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"status", "amountMin", "amountMax", "priceDefaultMin", "priceDefaultMax", "totalAmount", "gameCountMax", "gameSecondMax", "ruleFirstCount"};
    }

    protected Object[] getValues(BetInConfig m) {
        return new Object[]{m.getStatus(), m.getAmountMin(), m.getAmountMax(), m.getPriceDefaultMin(), m.getPriceDefaultMax(), m.getTotalAmount(), m.getGameCountMax(), m.getGameSecondMax(),
                m.getRuleFirstCount()};
    }
}
