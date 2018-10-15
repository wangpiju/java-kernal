package com.hs3.dao.contract;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.contract.ContractRule;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("contractRuleDao")
public class ContractRuleDao
        extends BaseDao<ContractRule> {
    public void save(ContractRule m) {
        String sql = new SQLUtils(this.tableName)
                .field("account,parentAccount,rootAccount,ruleNum,ruleName,gtdBonuses,gtdBonusesCycle,cumulativeSales,humenNum,dividend,contractStatus,contractTime")
                .getInsert();
        this.dbSession.update(
                sql,
                new Object[]{m.getAccount(), m.getParentAccount(), m.getRootAccount(), m.getRuleNum(),
                        m.getRuleNum(), m.getGtdBonuses(), m.getGtdBonusesCycle(), m.getCumulativeSales(),
                        m.getHumenNum(), m.getDividend(), m.getContractStatus(), m.getContractTime()});
    }

    public int delete(String account, Integer contractStatus) {
        String sql = "DELETE FROM " + this.tableName + " WHERE account=? and contractStatus=? ";
        return this.dbSession.update(sql, new Object[]{account, contractStatus});
    }

    public List<ContractRule> findEntityByAccount(String account, Integer contractStatus) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE account =? and contractStatus=? "});
        argsObjects = new Object[]{account, contractStatus};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public boolean checkContractRule(String account, Integer contractStatus) {
        StringBuffer buffer = new StringBuffer("select count(1) from " + this.tableName + " t where account=? and contractStatus=?");
        String sql = buffer.toString();
        Object[] argsObjects = {account, contractStatus};
        return this.dbSession.getInt(sql, argsObjects).intValue() > 0;
    }

    public int updateStatus(String account, Integer status, Date contractTime) {
        String sql = new SQLUtils(this.tableName).field("contractStatus,contractTime").where(" account=? ").getUpdate();
        return this.dbSession.update(sql, new Object[]{status, contractTime, account});
    }

    public List<ContractRule> accountList(Integer contractStatus) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"DISTINCT account,parentAccount,rootAccount", this.tableName, "WHERE contractStatus =?"});
        return this.dbSession.list(sql, new Object[]{contractStatus}, this.cls);
    }

    public List<ContractRule> contractRuleList(Page p, String account) {
        List<Object> args = new ArrayList();
        String sql = "SELECT distinct account,parentAccount,rootAccount,contractStatus,contractTime FROM " + this.tableName;
        StringBuffer sb = new StringBuffer(sql);
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sb.append(" where account =? or parentAccount =?");
            args.add(account);
            args.add(account);
        }
        sb.append(" ORDER BY contractTime  ");
        return this.dbSession.list(sb.toString(), args.toArray(), this.cls, p);
    }

    public List<ContractRule> findContractRuleByOrder(String account, Integer contractStatus) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE account =? and contractStatus=? ORDER BY id desc "});
        argsObjects = new Object[]{account, contractStatus};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public int updateStatusByBefore(String account, Integer status0, Integer status2, Date contractTime) {
        String sql = new SQLUtils(this.tableName).field("contractStatus,contractTime").where(" account=?  and contractStatus = ? ").getUpdate();
        return this.dbSession.update(sql, new Object[]{status2, contractTime, account, status0});
    }

    public int findByAccount(String account) {
        StringBuffer buffer = new StringBuffer("select count(1) from " + this.tableName + " where account=? ");
        String sql = buffer.toString();
        Object[] argsObjects = {account};
        return this.dbSession.getInt(sql, argsObjects).intValue();
    }

    public int deleteByAccount(String account) {
        String sql = "DELETE FROM " + this.tableName + " WHERE account=? ";
        return this.dbSession.update(sql, new Object[]{account});
    }

    public ContractRule checkHasSys(String account, Integer contractStatus) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"DISTINCT account,parentAccount,rootAccount", this.tableName, "WHERE account =? and contractStatus =?"});
        return (ContractRule) this.dbSession.getObject(sql, new Object[]{account, contractStatus}, this.cls);
    }
}
