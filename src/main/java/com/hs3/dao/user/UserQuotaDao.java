package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.entity.users.UserQuota;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("userQuotaDao")
public class UserQuotaDao
        extends BaseDao<UserQuota> {
    private static final String INSERT = "INSERT INTO t_user_quota(account,rebateRatio,num) VALUES(?,?,?)";
    private static final String UPDATE = "UPDATE t_user_quota SET num=num+? WHERE account=? AND rebateRatio=?";
    private static final String DELETE = "DELETE FROM t_user_quota WHERE account=? AND rebateRatio=?";
    private static final String DELETE_MAX = "DELETE FROM t_user_quota WHERE account=? AND rebateRatio>?";
    private static final String SELECT_ACCOUNT = "SELECT * FROM t_user_quota WHERE account=? order by rebateRatio desc";
    private static final String SELECT_LIMIT = "SELECT * FROM t_user_quota WHERE account=? and rebateRatio <= ? order by rebateRatio desc";

    public void save(UserQuota m) {
        this.dbSession.update("INSERT INTO t_user_quota(account,rebateRatio,num) VALUES(?,?,?)", new Object[]{m.getAccount(), m.getRebateRatio(), m.getNum()});
    }

    public int update(UserQuota m) {
        return this.dbSession.update("UPDATE t_user_quota SET num=num+? WHERE account=? AND rebateRatio=?", new Object[]{m.getNum(), m.getAccount(), m.getRebateRatio()});
    }

    public int updateNum(String account, BigDecimal rebateRatio, int count) {
        StringBuffer sb = new StringBuffer();
        sb.append(" UPDATE                    ");
        sb.append("   t_user_quota            ");
        sb.append(" SET                       ");
        sb.append("   num = num - ?           ");
        sb.append(" WHERE account = ?         ");
        sb.append("   AND rebateRatio = ? and num >= ?   ");
        return this.dbSession.update(sb.toString(), new Object[]{Integer.valueOf(count), account, rebateRatio, Integer.valueOf(count)});
    }

    public int updateAndAddNum(String account, String parent, Double rebateRatio, int count) {
        StringBuffer sb = new StringBuffer();

        sb.append(" UPDATE                               ");
        sb.append("   t_user_quota a,                    ");
        sb.append("   t_user_quota b                     ");
        sb.append(" SET                                  ");
        sb.append("   a.num = a.num - ?,               ");
        sb.append("   b.num = b.num + ?                ");
        sb.append(" WHERE a.rebateRatio = b.rebateRatio  ");
        sb.append("   AND a.num >= ?                   ");
        sb.append("   AND                                ");
        sb.append("   CASE                               ");
        sb.append("     WHEN b.num + ? <= 0            ");
        sb.append("     THEN b.num >= ABS(?)           ");
        sb.append("     ELSE 1 = 1                       ");
        sb.append("   END                                ");
        sb.append("   AND a.account = ?           ");
        sb.append("   AND b.account = ?           ");
        sb.append("   AND b.rebateRatio = ?           ");

        return this.dbSession.update(sb.toString(), new Object[]{Integer.valueOf(count), Integer.valueOf(count), Integer.valueOf(count), Integer.valueOf(count), Integer.valueOf(count), parent, account, rebateRatio});
    }

    public int findRecordByAccount(String account, BigDecimal num) {
        String sql = "SELECT COUNT(1) FROM t_user_quota WHERE account = ? AND rebateRatio = ? AND num > 0";
        Object[] args = {account, num};
        return this.dbSession.getInt(sql, args).intValue();
    }

    public int findNum(String account, Double num) {
        String sql = "SELECT num FROM t_user_quota WHERE account = ? AND rebateRatio = ?";
        Object[] args = {account, num};
        return this.dbSession.getInt(sql, args).intValue();
    }

    public int findRecord(String account, BigDecimal num) {
        String sql = "SELECT count(1) FROM t_user_quota WHERE account = ? AND rebateRatio = ?";
        Object[] args = {account, num};
        return this.dbSession.getInt(sql, args).intValue();
    }

    public int delete(String account, BigDecimal rebateRatio) {
        return this.dbSession.update("DELETE FROM t_user_quota WHERE account=? AND rebateRatio=?", new Object[]{account, rebateRatio});
    }

    public int deleteMaxRebateRatio(String account, BigDecimal rebateRatio) {
        return this.dbSession.update("DELETE FROM t_user_quota WHERE account=? AND rebateRatio>?", new Object[]{account, rebateRatio});
    }

    public List<UserQuota> listByAccount(String account) {
        return this.dbSession.list("SELECT * FROM t_user_quota WHERE account=? order by rebateRatio desc", new Object[]{account}, this.cls);
    }

    public List<UserQuota> listByAccountLimit(String account, double limit) {
        return this.dbSession.list("SELECT * FROM t_user_quota WHERE account=? and rebateRatio <= ? order by rebateRatio desc", new Object[]{account, Double.valueOf(limit)}, this.cls);
    }

    public List<Map<String, Object>> listQuotaByAccount(String account) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT                                      ");
        sb.append("   a.rebateRatio,a.num selfNum,              ");
        sb.append("   (SELECT                                   ");
        sb.append("     b.num                                   ");
        sb.append("   FROM                                      ");
        sb.append("     t_user_quota b                          ");
        sb.append("   WHERE b.account =                         ");
        sb.append("     (SELECT                                 ");
        sb.append("       c.parentAccount                       ");
        sb.append("     FROM                                    ");
        sb.append("       t_user c                              ");
        sb.append("     WHERE c.account = a.account)            ");
        sb.append("     AND b.rebateRatio = a.rebateRatio) num  ");
        sb.append(" FROM                                        ");
        sb.append("   t_user_quota a                            ");
        sb.append(" WHERE account = ? order by a.rebateRatio desc  ");
        return this.dbSession.listMap(sb.toString(), new Object[]{account}, null);
    }
}
