package com.hs3.dao.contract;

import com.hs3.dao.BaseDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.contract.ContractBonus;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("contractBonusDao")
public class ContractBonusDao
        extends BaseDao<ContractBonus> {
    public void save(ContractBonus m) {
        String sql = new SQLUtils(this.tableName)
                .field("account,parentAccount,rootAccount,startDate,endDate,cumulativeSales,cumulativeProfit,dividendAmount,dividend,status")
                .getInsert();
        this.dbSession.update(
                sql,
                new Object[]{m.getAccount(), m.getParentAccount(), m.getRootAccount(),
                        m.getStartDate(), m.getEndDate(), m.getCumulativeSales(), m.getCumulativeProfit(),
                        m.getDividendAmount(), m.getDividend(), m.getStatus()});
    }

    public List<ContractBonus> findEntityByAccount(String account) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"i", this.tableName, "WHERE account =?"});
        argsObjects = new Object[]{account};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public List<ContractBonus> listByCondition(ContractBonus m, Page p) {
        List<Object> args = new ArrayList();
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"i.*," + UserDao.getMarkSQL(), this.tableName + " as i", "WHERE 1=1 "});
        StringBuffer sb = new StringBuffer(sql);
        if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
            sb.append(" and account =?");
            args.add(m.getAccount());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getRootAccount()})) {
            sb.append(" and rootAccount =?");
            args.add(m.getRootAccount());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStatus()})) {
            sb.append(" and status =?");
            args.add(m.getStatus());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStartDate()})) {
            sb.append(" and startDate =?");
            args.add(m.getStartDate());
        }
        sb.append(" order by startDate desc ");
        return this.dbSession.list(sb.toString(), args.toArray(), this.cls, p);
    }

    public List<ContractBonus> findContractBonus(String account) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE account =? order by startDate desc LIMIT 2 "});
        argsObjects = new Object[]{account};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public int updateStatus(Integer id, Integer status) {
        String sql = new SQLUtils(this.tableName).field("status").where(" id=? ").getUpdate();
        return this.dbSession.update(sql, new Object[]{status, id});
    }

    public List<ContractBonus> findOverdueList(Date endDate, Integer status) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE endDate=? and status =?"});
        argsObjects = new Object[]{endDate, status};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public boolean isExistContractBonus(Date endDate) {
        String selectSQL = new SQLUtils(this.tableName).field("count(1)").where("endDate=?").getSelect();
        int n = this.dbSession.getInt(selectSQL, new Object[]{endDate}).intValue();
        return n != 0;
    }

    public List<ContractBonus> list(ContractBonus m, Page p) {
        List<Object> args = new ArrayList();
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE parentAccount =? "});
        StringBuffer sb = new StringBuffer(sql);
        args.add(m.getParentAccount());
        if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
            sb.append(" and account =?");
            args.add(m.getAccount());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStatus()})) {
            sb.append(" and status =?");
            args.add(m.getStatus());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStartDate()})) {
            sb.append(" and startDate =? and endDate =? ");
            args.add(m.getStartDate());
            args.add(m.getEndDate());
        }
        return this.dbSession.list(sb.toString(), args.toArray(), this.cls, p);
    }

    public boolean deleteBonusByDate(Date endDate) {
        String sql = "DELETE FROM " + this.tableName + " WHERE endDate=?";
        return this.dbSession.update(sql, new Object[]{endDate}) > 0;
    }

    public boolean isExistBonus(String account) {
        String selectSQL = new SQLUtils(this.tableName).field("count(1)").where("parentAccount=? and status in (0,3)").getSelect();
        int n = this.dbSession.getInt(selectSQL, new Object[]{account}).intValue();
        return n != 0;
    }
}
