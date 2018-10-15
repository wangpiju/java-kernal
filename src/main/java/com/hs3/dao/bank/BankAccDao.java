package com.hs3.dao.bank;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.entity.bank.BankAcc;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("bankAccDao")
public class BankAccDao
        extends BaseDao<BankAcc> {
    private static final String SQL_SELECT = "SELECT * FROM t_bank_acc WHERE bankApiId = ?";
    private static final String SQL_DELETE = "DELETE FROM t_bank_acc WHERE bankApiId = ?";

    public List<BankAcc> listByBankApiId(Integer bankApiId) {
        return this.dbSession.list("SELECT * FROM t_bank_acc WHERE bankApiId = ?", new Object[]{bankApiId}, this.cls);
    }

    public int deleteByBankApiId(Integer bankApiId) {
        return this.dbSession.update("DELETE FROM t_bank_acc WHERE bankApiId = ?", new Object[]{bankApiId});
    }

    public void save(BankAcc m) {
        saveAuto(m);
    }

    public int update(BankAcc m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"bankApiId", "type", "account"};
    }

    protected Object[] getValues(BankAcc m) {
        return new Object[]{m.getBankApiId(), m.getType(), m.getAccount()};
    }
}
