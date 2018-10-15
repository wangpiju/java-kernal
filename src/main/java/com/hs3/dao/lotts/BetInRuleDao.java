package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.entity.lotts.BetInRule;
import org.springframework.stereotype.Repository;

@Repository("betInRuleDao")
public class BetInRuleDao
        extends BaseDao<BetInRule> {
    public void save(BetInRule m) {
        saveAuto(m);
    }

    public int update(BetInRule m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"name"};
    }

    protected Object[] getValues(BetInRule m) {
        return new Object[]{m.getName()};
    }
}
