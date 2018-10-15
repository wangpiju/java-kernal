package com.hs3.dao.bank;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.bank.BankLevel;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("bankLevelDao")
public class BankLevelDao
        extends BaseDao<BankLevel> {
    private static final String INSERT = "INSERT INTO t_bank_level(title,minAmount,minCount) VALUES(?,?,?)";
    private static final String UPDATE = "UPDATE t_bank_level SET title=?,minAmount=?,minCount=? WHERE id=?";
    private static final String SQL_COUNT = "SELECT COUNT(1) FROM t_bank_level t1 WHERE t1.minCount <= ? AND t1.minAmount <= ? AND EXISTS (SELECT 1 FROM t_bank_sys t2 WHERE t1.id = t2.levelId AND t2.status = 0)";

    public int count(int minCount, BigDecimal minAmount) {
        return this.dbSession.getInt("SELECT COUNT(1) FROM t_bank_level t1 WHERE t1.minCount <= ? AND t1.minAmount <= ? AND EXISTS (SELECT 1 FROM t_bank_sys t2 WHERE t1.id = t2.levelId AND t2.status = 0)", new Object[]{Integer.valueOf(minCount), minAmount}).intValue();
    }

    public void save(BankLevel m) {
        this.dbSession.update("INSERT INTO t_bank_level(title,minAmount,minCount) VALUES(?,?,?)", new Object[]{m.getTitle(), m.getMinAmount(), m.getMinCount()});
    }

    public int update(BankLevel m) {
        return this.dbSession.update("UPDATE t_bank_level SET title=?,minAmount=?,minCount=? WHERE id=?", new Object[]{m.getTitle(), m.getMinAmount(), m.getMinCount(), m.getId()});
    }

    public List<BankLevel> listAllOrderByMinAmount() {
        return this.dbSession.list("SELECT id, title, minAmount, minCount FROM t_bank_level ORDER BY minAmount", this.cls, null);
    }

    public List<BankLevel> listAll(Page p) {
        return this.dbSession.list("SELECT * FROM t_bank_level ORDER BY minAmount DESC, minCount DESC", this.cls, p);
    }
}
