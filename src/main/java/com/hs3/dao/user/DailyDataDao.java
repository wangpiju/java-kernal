package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.users.DailyData;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("dailyDataDao")
public class DailyDataDao
        extends BaseDao<DailyData> {
    public static final String SQL = "SELECT count(1) FROM t_daily_data WHERE account = ? AND createTime BETWEEN ? AND ?";
    private static final String SQL_TERM = "SELECT IFNULL(SUM(w.betAmount), 0) betAmount, IFNULL(SUM(w.winAmount), 0) winAmount, IFNULL(SUM(IF(w.betAmountReal >= ?, 1, 0)), 0) validAccountCount FROM ( SELECT SUM(IF(t.accountChangeTypeId = 1 OR t.accountChangeTypeId = 2 OR t.accountChangeTypeId = 3, - t.changeAmount, 0)) betAmount, SUM(IF(t.accountChangeTypeId = 1, - t.changeAmount, 0)) betAmountReal, SUM(IF(t.accountChangeTypeId = 4, t.changeAmount, 0)) winAmount, t.changeUser FROM t_amount_change t WHERE t.betId IN (SELECT t2.id FROM t_bet t2 WHERE t2. STATUS IN(1, 2) AND t2.lastTime BETWEEN ? AND ?) AND t.accountChangeTypeId IN (1, 2, 3, 4) AND t.changeUser IN (SELECT t3.account FROM t_user t3 WHERE t3.parentList LIKE CONCAT((SELECT t4.parentList FROM t_user t4 WHERE t4.account = ?), '%')) GROUP BY t.changeUser ) w";

    public List<DailyData> listByCond(DailyData dailyData, String parentList, Date begin, Date end, Page page) {
        String sql = "SELECT * FROM t_daily_data WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{dailyData.getAccount()})) {
            sql = sql + " AND account = ?";
            cond.add(dailyData.getAccount());
        }
        if (!StrUtils.hasEmpty(new Object[]{parentList})) {
            sql = sql + " AND parentList like ?";
            cond.add(parentList + "%");
        }
        if (begin != null) {
            sql = sql + " AND createTime >= ?";
            cond.add(begin);
        }
        if (end != null) {
            sql = sql + " AND createTime <= ?";
            cond.add(end);
        }
        if (dailyData.getDailyAmount() != null) {
            sql = sql + " AND dailyAmount != ?";
            cond.add(dailyData.getDailyAmount());
        }
        sql = sql + " ORDER BY id DESC";

        return this.dbSession.list(sql, cond.toArray(), this.cls, page);
    }

    public DailyData findTeam(String account, int validAccountCount, Date beginTime, Date endTime) {
        return (DailyData) this.dbSession.getObject("SELECT IFNULL(SUM(w.betAmount), 0) betAmount, IFNULL(SUM(w.winAmount), 0) winAmount, IFNULL(SUM(IF(w.betAmountReal >= ?, 1, 0)), 0) validAccountCount FROM ( SELECT SUM(IF(t.accountChangeTypeId = 1 OR t.accountChangeTypeId = 2 OR t.accountChangeTypeId = 3, - t.changeAmount, 0)) betAmount, SUM(IF(t.accountChangeTypeId = 1, - t.changeAmount, 0)) betAmountReal, SUM(IF(t.accountChangeTypeId = 4, t.changeAmount, 0)) winAmount, t.changeUser FROM t_amount_change t WHERE t.betId IN (SELECT t2.id FROM t_bet t2 WHERE t2. STATUS IN(1, 2) AND t2.lastTime BETWEEN ? AND ?) AND t.accountChangeTypeId IN (1, 2, 3, 4) AND t.changeUser IN (SELECT t3.account FROM t_user t3 WHERE t3.parentList LIKE CONCAT((SELECT t4.parentList FROM t_user t4 WHERE t4.account = ?), '%')) GROUP BY t.changeUser ) w", new Object[]{Integer.valueOf(validAccountCount), beginTime, endTime, account}, this.cls);
    }

    public int count(String account, Date beginTime, Date endTime) {
        return this.dbSession.getInt("SELECT count(1) FROM t_daily_data WHERE account = ? AND createTime BETWEEN ? AND ?", new Object[]{account, beginTime, endTime}).intValue();
    }

    public void save(DailyData m) {
        saveAuto(m);
    }

    public int update(DailyData m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"account", "parentAccount", "rootAccount", "parentList", "userMark", "test", "ruleRate", "ruleValidAccountCount", "ruleLimitAmount", "ruleLossStatus",
                "validAccountCount", "lossStatus", "betAmount", "winAmount", "dailyAmount", "remark", "createTime", "changeTime"};
    }

    protected Object[] getValues(DailyData m) {
        return new Object[]{m.getAccount(), m.getParentAccount(), m.getRootAccount(), m.getParentList(), m.getUserMark(), m.getTest(), m.getRuleRate(), m.getRuleValidAccountCount(),
                m.getRuleLimitAmount(), m.getRuleLossStatus(), m.getValidAccountCount(), m.getLossStatus(), m.getBetAmount(), m.getWinAmount(), m.getDailyAmount(), m.getRemark(), m.getCreateTime(),
                m.getChangeTime()};
    }
}
