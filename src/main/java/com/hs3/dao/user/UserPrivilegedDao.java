package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.entity.users.UserPrivileged;
import com.hs3.utils.StrUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userPrivilegedDao")
public class UserPrivilegedDao extends BaseDao<UserPrivileged> {


    public List<UserPrivileged> listUserPrivileged(Integer type, String account) {

        String sql = "SELECT * FROM t_user_privileged where 1=1 ";

        if (!StrUtils.hasEmpty(new Object[]{type})) {
            sql = sql + " AND type = " + type;
        }

        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND account = '" + account + "'";
        }

        return this.dbSession.list(sql, this.cls);
    }





}
