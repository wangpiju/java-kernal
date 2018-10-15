package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.users.DaliyWages;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("daliyWagesDao")
public class DaliyWagesDao
        extends BaseDao<DaliyWages> {
    private static final String SQL_TERM = "SELECT IFNULL(SUM(w.betAmount), 0) betAmount, IFNULL(SUM(IF(w.betAmount >= ?, 1, 0)), 0) validAccountCount FROM ( SELECT SUM(- t.changeAmount) betAmount, t.changeUser FROM t_amount_change t WHERE t.betId IN (SELECT t2.id FROM t_bet t2 WHERE t2. STATUS IN(1, 2) AND t2.lastTime BETWEEN ? AND ?) AND t.accountChangeTypeId = 1 AND t.changeUser IN (SELECT t3.account FROM t_user t3 WHERE t3.parentList LIKE CONCAT((SELECT t4.parentList FROM t_user t4 WHERE t4.account = ?), '%')) GROUP BY t.changeUser ) w";

    public DaliyWages findTeam(String account, int validAccountCount, Date beginTime, Date endTime) {
        return (DaliyWages) this.dbSession.getObject("SELECT IFNULL(SUM(w.betAmount), 0) betAmount, IFNULL(SUM(IF(w.betAmount >= ?, 1, 0)), 0) validAccountCount FROM ( SELECT SUM(- t.changeAmount) betAmount, t.changeUser FROM t_amount_change t WHERE t.betId IN (SELECT t2.id FROM t_bet t2 WHERE t2. STATUS IN(1, 2) AND t2.lastTime BETWEEN ? AND ?) AND t.accountChangeTypeId = 1 AND t.changeUser IN (SELECT t3.account FROM t_user t3 WHERE t3.parentList LIKE CONCAT((SELECT t4.parentList FROM t_user t4 WHERE t4.account = ?), '%')) GROUP BY t.changeUser ) w", new Object[]{Integer.valueOf(validAccountCount), beginTime, endTime, account}, this.cls);
    }

    public List<DaliyWages> list(Page page) {
        String sql = "SELECT * FROM " + this.tableName + " ORDER BY rate DESC";
        return this.dbSession.list(sql, this.cls, page);
    }

    public void save(DaliyWages m) {
        saveAuto(m);
    }

    public int update(DaliyWages m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"betAmount", "validAccountCount", "rate"};
    }

    protected Object[] getValues(DaliyWages m) {
        return new Object[]{m.getBetAmount(), m.getValidAccountCount(), m.getRate()};
    }
}
