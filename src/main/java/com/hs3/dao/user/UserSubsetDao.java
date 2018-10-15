package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.entity.users.UserSubset;
import com.hs3.utils.StrUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("userSubsetDao")
public class UserSubsetDao extends BaseDao<UserSubset> {

    public List<UserSubset> listUserSubsetByAccount(String account) {
        String sql = "SELECT subSetAccount FROM t_user_subset where 1=1 ";
        List<Object> args = new ArrayList<>();
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND account=? ";
            args.add(account);
        }
        return this.dbSession.list(sql, args.toArray(), this.cls);
    }

    public void save(UserSubset m) {
        this.dbSession.update("INSERT INTO t_user_subset(account,subSetAccount) VALUES(?,?)",
                new Object[]{m.getAccount(), m.getSubSetAccount()});
    }


    public List<UserSubset> listUserSubsetByAccountArr(String[] accountArr) {
        String sql = "SELECT * FROM t_user_subset where 1=1 ";
        if (!StrUtils.hasEmpty(new Object[]{accountArr})) {
            sql = sql + " AND account in (";
            for(int i = 0; i<accountArr.length; i++){
                if(i == (accountArr.length - 1)) {
                    sql = sql + "'" + accountArr[i] + "'";
                }else {
                    sql = sql + "'" + accountArr[i] + "'" + ",";
                }
            }
            sql = sql + ") ";
        }
        return this.dbSession.list(sql, this.cls);
    }

    public List<UserSubset> listUserSubsetBySubSetAccountArr(String[] subSetAccountArr) {
        String sql = "SELECT * FROM t_user_subset where 1=1 ";
        if (!StrUtils.hasEmpty(new Object[]{subSetAccountArr})) {
            sql = sql + " AND subSetAccount in (";
            for(int i = 0; i<subSetAccountArr.length; i++){
                if(i == (subSetAccountArr.length - 1)) {
                    sql = sql + "'" + subSetAccountArr[i] + "'";
                }else {
                    sql = sql + "'" + subSetAccountArr[i] + "'" + ",";
                }
            }
            sql = sql + ") ";
        }
        return this.dbSession.list(sql, this.cls);
    }



}
