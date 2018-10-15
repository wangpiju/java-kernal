package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.entity.lotts.BetTigerConfig;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

@Repository("betTigerConfigDao")
public class BetTigerConfigDao
        extends BaseDao<BetTigerConfig> {
    private static final String SQL_updateAmount = "UPDATE t_bet_tiger_config SET winAmount = winAmount + ?, betAmount = betAmount + ? WHERE id = ?";

    public void updateAmount(Integer id, BigDecimal winAmount, BigDecimal betAmount) {
        this.dbSession.update("UPDATE t_bet_tiger_config SET winAmount = winAmount + ?, betAmount = betAmount + ? WHERE id = ?", new Object[]{winAmount, betAmount, id});
    }

    public void save(BetTigerConfig m) {
        saveAuto(m);
    }

    public int update(BetTigerConfig m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"status", "ratio", "deviation", "winAmount", "betAmount"};
    }

    protected Object[] getValues(BetTigerConfig m) {
        return new Object[]{m.getStatus(), m.getRatio(), m.getDeviation(), m.getWinAmount(), m.getBetAmount()};
    }
}
