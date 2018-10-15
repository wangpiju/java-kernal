package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInTotal;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("betInTotalDao")
public class BetInTotalDao
        extends BaseDao<BetInTotal> {
    private static final String SQL_updateGame = "UPDATE t_bet_in_total SET gameCount = gameCount + ?, gameAmount = gameAmount + ?, winAmount = winAmount + ? WHERE id = ?";

    public List<BetInTotal> findByCond(boolean hasMark, BetInTotal m, Date beginTime, Date endTime, boolean isIncludeChild, Page p) {
        String sql = null;
        if (hasMark) {
            sql = "SELECT i.*," + UserDao.getMarkSQL() + " FROM t_bet_in_total as i WHERE 1 = 1";
        } else {
            sql = "SELECT * FROM t_bet_in_total WHERE 1 = 1";
        }
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
            if (isIncludeChild) {
                sql = sql + " AND account in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user WHERE account = ?),'%'))";
            } else {
                sql = sql + " AND account = ?";
            }
            cond.add(m.getAccount());
        }
        if (m.getTest() != null) {
            sql = sql + " AND test = ?";
            cond.add(m.getTest());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getLotteryName()})) {
            sql = sql + " AND lotteryName = ?";
            cond.add(m.getLotteryName());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getPlayName()})) {
            sql = sql + " AND playName = ?";
            cond.add(m.getPlayName());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getBetId()})) {
            sql = sql + " AND betId = ?";
            cond.add(m.getBetId());
        }
        if (beginTime != null) {
            sql = sql + " AND startTime >= ?";
            cond.add(beginTime);
        }
        if (endTime != null) {
            sql = sql + " AND startTime <= ?";
            cond.add(endTime);
        }
        sql = sql + " ORDER BY id DESC";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, p);
    }

    public int updateGame(Integer gameCount, BigDecimal gameAmount, BigDecimal winAmount, Integer id) {
        return this.dbSession.update("UPDATE t_bet_in_total SET gameCount = gameCount + ?, gameAmount = gameAmount + ?, winAmount = winAmount + ? WHERE id = ?", new Object[]{gameCount, gameAmount, winAmount, id});
    }

    public void save(BetInTotal m) {
        saveAuto(m);
    }

    public int update(BetInTotal m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"account", "test", "betId", "lotteryName", "playName", "startAmount", "startTime", "gameCount", "gameAmount", "winAmount"};
    }

    protected Object[] getValues(BetInTotal m) {
        return new Object[]{m.getAccount(), m.getTest(), m.getBetId(), m.getLotteryName(), m.getPlayName(), m.getStartAmount(), m.getStartTime(), m.getGameCount(), m.getGameAmount(),
                m.getWinAmount()};
    }
}
