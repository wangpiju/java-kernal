package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.users.PrivateRatioRuleDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class PrivateRatioRuleDetailsDao
        extends BaseDao<PrivateRatioRuleDetails> {
    private static final String[] columns = {"pid", "amount", "ratio"};

    protected String[] getColumns() {
        return columns;
    }

    protected Object[] getValues(PrivateRatioRuleDetails t) {
        return new Object[]{t.getPid(), t.getAmount(), t.getRatio()};
    }

    public int deleteByPids(List<Integer> id) {
        String sql = new SQLUtils(this.tableName).whereIn("pid in(?)", id.size()).getDelete();
        List<Object> args = new ArrayList();
        args.addAll(id);
        return this.dbSession.update(sql, args.toArray());
    }

    public List<PrivateRatioRuleDetails> listByPid(Integer pid) {
        String sql = new SQLUtils(this.tableName).where("pid=?").orderBy("amount DESC").getSelect();
        return this.dbSession.list(sql, new Object[]{pid}, this.cls);
    }

    public PrivateRatioRuleDetails listByPidAndAmount(Integer pid, BigDecimal amount) {
        String sql = new SQLUtils(this.tableName).where("pid=? AND amount<=?").orderBy("amount DESC LIMIT 1").getSelect();
        return this.dbSession.getObject(sql, new Object[]{pid, amount}, this.cls);
    }
}
