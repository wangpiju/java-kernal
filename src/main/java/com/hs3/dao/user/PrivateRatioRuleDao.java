package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.entity.users.PrivateRatioRule;
import org.springframework.stereotype.Repository;

@Repository
public class PrivateRatioRuleDao
        extends BaseDao<PrivateRatioRule> {
    private static final String[] columns = {"name", "status"};

    protected String[] getColumns() {
        return columns;
    }

    protected Object[] getValues(PrivateRatioRule t) {
        return new Object[]{t.getName(), t.getStatus()};
    }
}
