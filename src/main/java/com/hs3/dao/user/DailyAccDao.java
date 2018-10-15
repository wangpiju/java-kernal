package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.users.DailyAcc;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("dailyAccDao")
public class DailyAccDao
        extends BaseDao<DailyAcc> {
    private static final String SQL = "SELECT * FROM t_daily_acc WHERE account = ?";
    private static final String SQL_DELETE_BY_RULEID = "DELETE FROM t_daily_acc WHERE ruleId = ?";
    private static final String SQL_DELETE_BY_ACCOUNT = "DELETE FROM t_daily_acc WHERE account = ?";
    private static final String SQL_DELETE_BY_PARENTACCOUNT = "DELETE FROM t_daily_acc WHERE parentAccount = ?";
    private static final String SQL_DELETE_BY_ROOTACCOUNT = "DELETE FROM t_daily_acc WHERE rootAccount = ?";
    private static final String SQL_TEAM = "SELECT * FROM t_daily_acc t1 WHERE NOT EXISTS (SELECT 1 FROM t_daily_acc t2 WHERE t1.account != t2.account AND t1.parentaccount = t2.account)";

    public List<DailyAcc> listByCond(DailyAcc m, Page page) {
        String sql = "SELECT * FROM t_daily_acc WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (m != null) {
            if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
                sql = sql + " AND account = ?";
                cond.add(m.getAccount());
            }
            if (!StrUtils.hasEmpty(new Object[]{m.getParentAccount()})) {
                sql = sql + " AND parentAccount = ?";
                cond.add(m.getParentAccount());
            }
        }
        return this.dbSession.list(sql, cond.toArray(), this.cls, page);
    }

    public DailyAcc findByAccount(String account) {
        return (DailyAcc) this.dbSession.getObject("SELECT * FROM t_daily_acc WHERE account = ?", new Object[]{account}, this.cls);
    }

    public List<DailyAcc> listTeam() {
        return this.dbSession.list("SELECT * FROM t_daily_acc t1 WHERE NOT EXISTS (SELECT 1 FROM t_daily_acc t2 WHERE t1.account != t2.account AND t1.parentaccount = t2.account)", this.cls);
    }

    public int deleteByRuleId(Integer ruleId) {
        return this.dbSession.update("DELETE FROM t_daily_acc WHERE ruleId = ?", new Object[]{ruleId});
    }

    public int deleteByAccount(String account) {
        return this.dbSession.update("DELETE FROM t_daily_acc WHERE account = ?", new Object[]{account});
    }

    public int deleteByParentAccount(String parentAccount) {
        return this.dbSession.update("DELETE FROM t_daily_acc WHERE parentAccount = ?", new Object[]{parentAccount});
    }

    public int deleteByRootAccount(String rootAccount) {
        return this.dbSession.update("DELETE FROM t_daily_acc WHERE rootAccount = ?", new Object[]{rootAccount});
    }

    public List<DailyAcc> listByParentList(String parentList) {
        return this.dbSession.list("SELECT * FROM t_daily_acc WHERE parentList like ?", new Object[]{parentList + "%"}, this.cls);
    }

    public List<DailyAcc> listByParentAccount(String parentAccount) {
        return this.dbSession.list("SELECT * FROM t_daily_acc WHERE parentAccount = ? AND parentAccount != account", new Object[]{parentAccount}, this.cls);
    }

    public int deleteByParentList(String parentList) {
        return this.dbSession.update("DELETE FROM t_daily_acc WHERE parentList like ?", new Object[]{parentList + "%"});
    }

    public void save(Integer dailyRuleId, BigDecimal rate, BigDecimal betAmount, String account, String parentAccount, String rootAccount, String parentList) {
        Date now = new Date();

        DailyAcc m = new DailyAcc();
        m.setRuleId(dailyRuleId);
        m.setAccount(account);
        m.setParentAccount(parentAccount);
        m.setRootAccount(rootAccount);
        m.setParentList(parentList);
        m.setRate(rate);
        m.setBetAmount(betAmount);
        m.setValidAccountCount(Integer.valueOf(0));
        m.setLimitAmount(BigDecimal.ZERO);
        m.setLossStatus(Integer.valueOf(1));
        m.setCreateTime(now);
        m.setChangeTime(now);
        m.setStatus(Integer.valueOf(0));

        save(m);
    }

    public void save(DailyAcc m) {
        saveAuto(m);
    }

    public int update(DailyAcc m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"ruleId", "account", "parentAccount", "rootAccount", "parentList", "rate", "betAmount", "validAccountCount", "lossStatus", "limitAmount", "createTime", "changeTime",
                "status"};
    }

    protected Object[] getValues(DailyAcc m) {
        return new Object[]{m.getRuleId(), m.getAccount(), m.getParentAccount(), m.getRootAccount(), m.getParentList(), m.getRate(), m.getBetAmount(), m.getValidAccountCount(), m.getLossStatus(),
                m.getLimitAmount(), m.getCreateTime(), m.getChangeTime(), m.getStatus()};
    }
}
