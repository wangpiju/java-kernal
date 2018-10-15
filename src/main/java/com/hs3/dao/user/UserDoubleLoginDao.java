package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.users.UserDoubleLogin;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class UserDoubleLoginDao
        extends BaseDao<UserDoubleLogin> {
    private static final String[] COLUMNS = {"account", "status"};

    protected String[] getColumns() {
        return COLUMNS;
    }

    protected Object[] getValues(UserDoubleLogin t) {
        return new Object[]{t.getAccount(), t.getStatus()};
    }

    public List<UserDoubleLogin> listStatus() {
        String s = new SQLUtils(this.tableName).where("status<=1").getSelect();
        return this.dbSession.list(s, this.cls);
    }

    public int deleteByAccount(List<String> accounts) {
        String s = new SQLUtils(this.tableName).whereIn("account in(?)", accounts.size()).getDelete();
        return this.dbSession.update(s, accounts.toArray(new Object[accounts.size()]));
    }

    public List<UserDoubleLogin> list(String account, Integer status, Page p) {
        List<Object> args = new ArrayList();
        SQLUtils s = new SQLUtils(this.tableName);
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            s.whereAnd("account=?");
            args.add(account);
        }
        if (status != null) {
            s.whereAnd("status=?");
            args.add(status);
        }
        return this.dbSession.list(s.getSelect(), args.toArray(), this.cls, p);
    }
}
