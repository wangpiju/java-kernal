package com.hs3.dao.bank;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.entity.bank.BankGroup;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("bankGroupDao")
public class BankGroupDao
        extends BaseDao<BankGroup> {
    private static final String INSERT = "INSERT INTO t_bank_group(title) VALUES(?)";
    private static final String UPDATE = "UPDATE t_bank_group SET title=? WHERE id=?";
    private static final String INSERT_BANKS = "INSERT INTO t_bank_group_sys(groupId,bankId) VALUES(?,?)";

    public void save(BankGroup m) {
        this.dbSession.update("INSERT INTO t_bank_group(title) VALUES(?)", new Object[]{m.getTitle()});
    }

    public int update(BankGroup m) {
        return this.dbSession.update("UPDATE t_bank_group SET title=? WHERE id=?", new Object[]{m.getTitle(), m.getId()});
    }

    public int addBankList(Integer groupId, List<Integer> bankIds) {
        int count = 0;
        for (Integer id : bankIds) {
            count += this.dbSession.update("INSERT INTO t_bank_group_sys(groupId,bankId) VALUES(?,?)", new Object[]{groupId, id});
        }
        return count;
    }

    public int deleteBankList(Integer groupId, List<Integer> bankIds) {
        String DELETE_BANKS = "DELETE FROM t_bank_group_sys WHERE groupId=? AND bankId in(" + getQuestionNumber(bankIds.size()) + ")";

        bankIds.add(0, groupId);
        return this.dbSession.update(DELETE_BANKS, bankIds.toArray());
    }

    public List<Integer> listBankIds(Integer id) {
        String sql = "SELECT bankId FROM t_bank_group_sys WHERE groupId=?";
        return this.dbSession.listSerializable(sql, new Object[]{id}, Integer.class);
    }

    public BankGroup findByName(String bankName) {
        String sql = "SELECT id, title FROM t_bank_group WHERE title=?";
        return (BankGroup) this.dbSession.getObject(sql, new Object[]{bankName}, this.cls);
    }
}
