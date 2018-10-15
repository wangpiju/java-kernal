package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.entity.users.UserSafe;
import com.hs3.utils.StrUtils;
import org.springframework.stereotype.Repository;
import com.hs3.db.SQLUtils;

import java.util.Date;

@Repository("userSafeDao")
public class UserSafeDao
        extends BaseDao<UserSafe> {
    private static final String INSERT = "insert into t_user_safe values(?,?,?,?,?,?,?,?)";
    private static final String DELETE_ACCOUNT = "DELETE FROM t_user_safe WHERE account=?";
    private static final String SELECT_BY_ANSWER = "SELECT COUNT(1) FROM t_user_safe WHERE account = ? AND qsType1 = ? AND answer1 = ? AND qsType2 = ? AND answer2 = ?";
    private static final String SELECT_BY_ACCOUNT = "SELECT COUNT(1) FROM t_user_safe WHERE account = ?";

    public void save(UserSafe us) {
        Object[] a = {0, us.getAccount(), us.getQstype1(),
                StrUtils.MD5(us.getAnswer1()), us.getQstype2(),
                StrUtils.MD5(us.getAnswer2()), us.getCreatetime(), new Date()};
        this.dbSession.update("insert into t_user_safe values(?,?,?,?,?,?,?,?)", a);
    }

    public int findByQuestionAndAnswer(String account, String qsType1, String answer1, String qsType2, String answer2) {
        Object[] args = {account, qsType1, StrUtils.MD5(answer1),
                qsType2, StrUtils.MD5(answer2)};
        return this.dbSession.getInt("SELECT COUNT(1) FROM t_user_safe WHERE account = ? AND qsType1 = ? AND answer1 = ? AND qsType2 = ? AND answer2 = ?", args).intValue();
    }

    public int findByAccount(String account) {
        Object[] args = {account};
        return this.dbSession.getInt("SELECT COUNT(1) FROM t_user_safe WHERE account = ?", args).intValue();
    }

    public int deleteAccount(String account) {
        return this.dbSession.update("DELETE FROM t_user_safe WHERE account=?", new Object[]{account});
    }

    //**************************************以下为变更部分*****************************************

    public void saveZ(UserSafe us) {
        Object[] a = {0, us.getAccount(), us.getQstype1(),
                us.getAnswer1(), us.getQstype2(),
                us.getAnswer2(), us.getCreatetime(), us.getLastmodify()};
        this.dbSession.update("insert into t_user_safe values(?,?,?,?,?,?,?,?)", a);
    }

    public UserSafe findByAccountZ(String account) {
        String sql = new SQLUtils(this.tableName).where("account=?").getSelect();
        Object[] args = {account};
        return (UserSafe) this.dbSession.getObject(sql, args, this.cls);
    }

}
