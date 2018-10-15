package com.hs3.dao.finance;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.finance.FinanceMission;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("financeMissionDao")
public class FinanceMissionDao
        extends BaseDao<FinanceMission> {
    public void save(FinanceMission m) {
        saveAuto(m);
    }

    public int update(FinanceMission m) {
        return updateByIdAuto(m, m.getId());
    }

    public List<FinanceMission> list(Page page) {
        String sql = "SELECT * FROM " + this.tableName + " ORDER BY maxAmount";
        return this.dbSession.list(sql, this.cls, page);
    }

    protected String[] getColumns() {
        return new String[]{"title", "minAmount", "maxAmount"};
    }

    protected Object[] getValues(FinanceMission m) {
        return new Object[]{m.getTitle(), m.getMinAmount(), m.getMaxAmount()};
    }
}
