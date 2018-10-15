package com.hs3.dao.finance;

import com.hs3.dao.BaseDao;
import com.hs3.entity.finance.FinanceSetting;
import org.springframework.stereotype.Repository;

@Repository("financeSettingDao")
public class FinanceSettingDao
        extends BaseDao<FinanceSetting> {
    public void save(FinanceSetting m) {
        saveAuto(m);
    }

    public int update(FinanceSetting m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"depositMaxCount", "depositMinMoney", "depositMaxMoney", "rechargeLowerMaxMoney", "rechargeLowerNotAudit", "rechargeLowerHours", "testUserRechargeStatus",
                "testUserDepositStatus", "depositMinBindCardHours", "depositAuto", "depositAutoApi", "depositAutoOperator", "depositAutoAmount", "depositSplitMaxMoney"};
    }

    protected Object[] getValues(FinanceSetting m) {
        return new Object[]{m.getDepositMaxCount(), m.getDepositMinMoney(), m.getDepositMaxMoney(), m.getRechargeLowerMaxMoney(), m.getRechargeLowerNotAudit(), m.getRechargeLowerHours(),
                m.getTestUserRechargeStatus(), m.getTestUserDepositStatus(), m.getDepositMinBindCardHours(), m.getDepositAuto(), m.getDepositAutoApi(), m.getDepositAutoOperator(),
                m.getDepositAutoAmount(), m.getDepositSplitMaxMoney()};
    }
}
