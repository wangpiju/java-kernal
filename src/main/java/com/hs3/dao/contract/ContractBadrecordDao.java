package com.hs3.dao.contract;

import com.hs3.dao.BaseDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.contract.ContractBadrecord;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("contractBadrecordDao")
public class ContractBadrecordDao
        extends BaseDao<ContractBadrecord> {
    public void save(ContractBadrecord m) {
        String sql = new SQLUtils(this.tableName)
                .field("account,parentAccount,rootAccount,startDate,endDate,cumulativeSales,cumulativeProfit,dividendAmount,dividend,status")
                .getInsert();
        this.dbSession.update(
                sql,
                new Object[]{m.getAccount(), m.getParentAccount(), m.getRootAccount(),
                        m.getStartDate(), m.getEndDate(), m.getCumulativeSales(), m.getCumulativeProfit(),
                        m.getDividendAmount(), m.getDividend(), m.getStatus()});
    }

    public List<ContractBadrecord> findEntityByAccount(String account) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE account =?"});
        argsObjects = new Object[]{account};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public List<ContractBadrecord> listByCondition(ContractBadrecord m, Page p) {
        List<Object> args = new ArrayList();
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"i.*," + UserDao.getMarkSQL(), this.tableName + " as i", "WHERE 1=1 "});
        StringBuffer sb = new StringBuffer(sql);
        if (!StrUtils.hasEmpty(new Object[]{m.getParentAccount()})) {
            sb.append(" and parentAccount =?");
            args.add(m.getParentAccount());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getRootAccount()})) {
            sb.append(" and rootAccount =?");
            args.add(m.getRootAccount());
        }
        if ((!StrUtils.hasEmpty(new Object[]{m.getStatus()})) && (m.getStatus().intValue() != 0)) {
            sb.append(" and status =?");
            args.add(m.getStatus());
        }
        return this.dbSession.list(sb.toString(), args.toArray(), this.cls, p);
    }

    public boolean isExistRecord(Date endDate) {
        String selectSQL = new SQLUtils(this.tableName).field("count(1)").where("endDate=?").getSelect();
        int n = this.dbSession.getInt(selectSQL, new Object[]{endDate}).intValue();
        return n != 0;
    }

    public boolean deleteBadrecordByDate(Date endDate) {
        String sql = "DELETE FROM " + this.tableName + " WHERE endDate=?";
        return this.dbSession.update(sql, new Object[]{endDate}) > 0;
    }
}
