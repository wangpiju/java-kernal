package com.hs3.dao.user;

import org.springframework.stereotype.Repository;

import com.hs3.dao.BaseDao;
import com.hs3.entity.users.UserCode;

/**
 * 用户验证码
 *
 * @author Stephen Zhou
 */
@Repository("userCodeDao")
public class UserCodeDao extends BaseDao<UserCode> {

    public void save(UserCode uc) {

        Object[] a = {
                0,
                uc.getAccount(),
                uc.getType(),
                uc.getCode(),
                uc.getCreateTime(),
                uc.getStatus(),
                uc.getComments()
        };

        this.dbSession.update("insert into t_user_code values(?,?,?,?,?,?,?)", a);
    }

    //取是否存在该用户的验证码数据
    public int findByAccount(String account) {
        Object[] args = {account};
        return this.dbSession.getInt("SELECT COUNT(1) FROM t_user_code WHERE account = ?", args).intValue();
    }

    //获取该用户最新的一条验证码对象
    public UserCode findTopByAccount(String account, Integer type) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE account=? AND type=? ORDER BY id DESC limit 1"});
        Object[] args = {account, type};
        return (UserCode) this.dbSession.getObject(sql, args, this.cls);
    }

    //修改用户验证码信息
    public int updateInfo(Integer id, Integer status, String comments) {
        return this.dbSession.update("UPDATE t_user_code SET status=?,comments=? WHERE id=?", new Object[]{status, comments, id});
    }


}
