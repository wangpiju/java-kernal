package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.users.PrivateRatio;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class PrivateRatioDao
        extends BaseDao<PrivateRatio> {
    private static final String[] columns = {
            "createDate",
            "account",
            "parentAccount",
            "remark",
            "betAmount",
            "amount",
            "ratio"};

    protected String[] getColumns() {
        return columns;
    }

    protected Object[] getValues(PrivateRatio t) {
        return new Object[]{
                t.getCreateDate(),
                t.getAccount(),
                t.getParentAccount(),
                t.getRemark(),
                t.getBetAmount(),
                t.getAmount(),
                t.getRatio()};
    }

    public List<PrivateRatio> list(String account, BigDecimal ratio, Date begin, Date end, Page p) {
        SQLUtils su = new SQLUtils(this.tableName);
        List<Object> args = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            su.whereAnd("account=?");
            args.add(account);
        }
        if (ratio != null) {
            su.whereAnd("ratio>=?");
            args.add(ratio);
        }
        if (begin != null) {
            su.whereAnd("createDate>=?");
            args.add(begin);
        }
        if (end != null) {
            su.whereAnd("createDate<=?");
            args.add(end);
        }
        su.orderBy("createDate DESC");
        return this.dbSession.list(su.getSelect(), args.toArray(), this.cls, p);
    }
}
