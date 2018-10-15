package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.users.UserToken;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("userTokenDao")
public class UserTokenDao
        extends BaseDao<UserToken> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(UserToken m) {
        String sql = new SQLUtils(this.tableName).field("account,tokenKey,tokenValue").getInsert();
        this.dbSession.update(sql, new Object[]{m.getAccount(), m.getTokenKey(), m.getTokenValue()});
    }

    public int[] executeBatch(List<UserToken> list) {
        final List<UserToken> userToken = list;
        String sql = "insert into t_user_token (account,tokenKey,tokenValue) values (?,?,?)";
        int[] a = this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                String account = ((UserToken) userToken.get(i)).getAccount();
                String tokenKey = ((UserToken) userToken.get(i)).getTokenKey();
                String tokenValue = ((UserToken) userToken.get(i)).getTokenValue();
                ps.setString(1, account);
                ps.setString(2, tokenKey);
                ps.setString(3, tokenValue);
            }

            public int getBatchSize() {
                return userToken.size();
            }
        });
        return a;
    }

    public int deleteByAccount(String account) {
        String sql = "DELETE FROM " + this.tableName + " WHERE account=?";
        return this.dbSession.update(sql, new Object[]{account});
    }

    public List<UserToken> getUserToken(String account) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE account =? ORDER BY rand() LIMIT 3 "});
        return this.dbSession.list(sql, new Object[]{account}, this.cls);
    }

    public List<UserToken> listByAccount(String account) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE account =? "});
        return this.dbSession.list(sql, new Object[]{account}, this.cls);
    }

    public String getTokenValue(String account, String[] tokenKey) {
        String sql = "select * from t_user_token where account =? and tokenKey in (?,?,?)";
        Object[] args = {account, tokenKey[0], tokenKey[1], tokenKey[2]};
        List<UserToken> vals = this.dbSession.list(sql, args, this.cls);
        String rel = "";
        for (String k : tokenKey) {
            for (UserToken v : vals) {
                if (v.getTokenKey().equals(k)) {
                    rel = rel + v.getTokenValue();
                    break;
                }
            }
        }
        return rel;
    }
}
