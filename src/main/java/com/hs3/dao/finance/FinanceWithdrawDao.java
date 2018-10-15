package com.hs3.dao.finance;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.entity.finance.FinanceWithdraw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("financeWithdrawDao")
public class FinanceWithdrawDao
        extends BaseDao<FinanceWithdraw> {
    public FinanceWithdraw findByAmount(BigDecimal amount, Integer status) {
        String sql = "SELECT * FROM t_finance_withdraw WHERE ? >= minAmount AND ? < maxAmount";
        List<Object> cond = new ArrayList();
        cond.add(amount);
        cond.add(amount);
        if (status != null) {
            sql = sql + " AND status = ?";
            cond.add(status);
        }
        return (FinanceWithdraw) this.dbSession.getObject(sql, cond.toArray(), this.cls);
    }

    public void save(FinanceWithdraw m) {
        saveAuto(m);
    }

    public int update(FinanceWithdraw m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"classKey", "title", "merchantCode", "email", "sign", "publicKey", "apiUrl", "shopUrl", "createTime", "status", "autoOperator", "minAmount", "maxAmount"};
    }

    protected Object[] getValues(FinanceWithdraw m) {
        return new Object[]{m.getClassKey(), m.getTitle(), m.getMerchantCode(), m.getEmail(), m.getSign(), m.getPublicKey(), m.getApiUrl(), m.getShopUrl(), m.getCreateTime(), m.getStatus(),
                m.getAutoOperator(), m.getMinAmount(), m.getMaxAmount()};
    }
}
