package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.entity.lotts.BetChange;
import org.springframework.stereotype.Repository;

@Repository("betChangeDao")
public class BetChangeDao
        extends BaseDao<BetChange> {
    public void save(BetChange m) {
        saveAuto(m);
    }

    public int update(BetChange m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"account", "lotteryId", "playerId", "status"};
    }

    protected Object[] getValues(BetChange m) {
        return new Object[]{m.getAccount(), m.getLotteryId(), m.getPlayerId(), m.getStatus()};
    }
}
