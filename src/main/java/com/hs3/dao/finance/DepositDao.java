package com.hs3.dao.finance;

import com.hs3.dao.BaseDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.finance.Deposit;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("depositDao")
public class DepositDao
        extends BaseDao<Deposit> {
    public void save(String id, BigDecimal amount, String account, Integer test, int withdrawalTimes, String adminRemark, String splitId, String bankCode, String bankName, String card, String address, String niceName) {
        Date d = new Date();
        String sql = new SQLUtils(this.tableName).field("id,amount,account,test,withdrawalTimes,adminRemark,splitId").field("bankCode,bankName,card,address,niceName").field("createTime,lastTime")
                .getInsert();
        this.dbSession.update(sql, new Object[]{id, amount, account, test, Integer.valueOf(withdrawalTimes), adminRemark, splitId, bankCode, bankName, card, address, niceName, d, d});
    }

    public int countSplitSuccess(String account, String splitId) {
        return this.dbSession.getInt("SELECT count(*) FROM t_deposit WHERE account = ? AND splitId = ? AND status = 2", new Object[]{account, splitId}).intValue();
    }

    public int updateStatus(String id, Integer status) {
        return this.dbSession.update("UPDATE t_deposit SET status=? WHERE id=?", new Object[]{status, id});
    }

    public int setDispose(String id, Integer status, String operator, String remark, String serialNumber, String lastOperator) {
        String sql = new SQLUtils(this.tableName).field("status,operator,remark,lastTime,serialNumber,lastOperator").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{status, operator, remark, new Date(), serialNumber, lastOperator, id});
    }

    public List<Deposit> listByCond(boolean isMaster, Deposit deposit, Date startTime, Date endTime, BigDecimal minAmount, BigDecimal maxAmount, Integer[] statusArray, Page page) {
        String sql = (isMaster ? "/*master*/" : "") + "SELECT *," + UserDao.getMarkSQL() + " FROM t_deposit i WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{deposit.getAccount()})) {
            sql = sql + " AND account = ?";
            cond.add(deposit.getAccount());
        }
        if (deposit.getTest() != null) {
            sql = sql + " AND test = ?";
            cond.add(deposit.getTest());
        }
        if ((statusArray != null) && (statusArray.length > 0)) {
            sql = sql + " AND status IN (";
            for (Integer status : statusArray) {
                if (status != null) {
                    sql = sql + "?,";
                    cond.add(status);
                }
            }
            sql = sql + "-1)";
        }
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            cond.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            cond.add(endTime);
        }
        if (minAmount != null) {
            sql = sql + " AND amount >= ?";
            cond.add(minAmount);
        }
        if (maxAmount != null) {
            sql = sql + " AND amount < ?";
            cond.add(maxAmount);
        }
        if (!StrUtils.hasEmpty(new Object[]{deposit.getTraceId()})) {
            sql = sql + " AND traceId = ?";
            cond.add(deposit.getTraceId());
        }
        if (!StrUtils.hasEmpty(new Object[]{deposit.getSerialNumber()})) {
            sql = sql + " AND serialNumber = ?";
            cond.add(deposit.getSerialNumber());
        }
        if (!StrUtils.hasEmpty(new Object[]{deposit.getOperator()})) {
            sql = sql + " AND operator = ?";
            cond.add(deposit.getOperator());
        }
        if (!StrUtils.hasEmpty(new Object[]{deposit.getLastOperator()})) {
            sql = sql + " AND lastOperator = ?";
            cond.add(deposit.getLastOperator());
        }
        sql = sql + " ORDER BY CASE STATUS WHEN 99 THEN 99 ELSE 98 END desc, createTime desc, id desc";

        page.setObj(this.dbSession.getString("SELECT IFNULL(SUM(AMOUNT), 0) FROM (" + sql + ") as v", cond.toArray(new Object[cond.size()])));

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public List<Deposit> listByCond(Deposit deposit, Date startTime, Date endTime, BigDecimal minAmount, BigDecimal maxAmount, Integer[] statusArray, Page page) {
        return listByCond(false, deposit, startTime, endTime, minAmount, maxAmount, statusArray, page);
    }

    public List<Deposit> listByHome(String account, boolean isIncludeChild, String startTime, String endTime, Page p) {
        String sql = "SELECT * FROM t_deposit WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            if (!isIncludeChild) {
                sql = sql + " AND account = ?";
                cond.add(account);
            } else {
                sql = sql + " AND account in (SELECT account FROM t_user\tWHERE parentList LIKE CONCAT((\tSELECT\tparentList\tFROM t_user WHERE\taccount =?\t),'%')) ";
                cond.add(account);
            }
        }
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            cond.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            cond.add(endTime);
        }
        sql = sql + " ORDER BY createTime desc";

        return this.dbSession.list(sql, cond.toArray(), this.cls, p);
    }

    public List<Deposit> listRecord(String account, Integer start, Integer limit) {
        return this.dbSession.list("SELECT * FROM " + this.tableName + " WHERE account = ? AND status = 2 ORDER BY createTime DESC  LIMIT ?,?", new Object[]{account, start, limit}, this.cls);
    }

    public Deposit findToDoing() {
        return (Deposit) this.dbSession.getObject(
                "SELECT t1.* FROM t_deposit t1, (SELECT IFNULL(MIN(minAmount),0) minAmount, IFNULL(MAX(maxAmount), 0) maxAmount FROM t_finance_withdraw WHERE status = 0) t2 WHERE t1.status = 7 AND t1.test = 0 AND t1.amount >= t2.minAmount AND t1.amount < t2.maxAmount ORDER BY t1.createTime limit 1",
                this.cls);
    }

    public void save_Z(String id, BigDecimal amount, String account, Integer test, int withdrawalTimes, String adminRemark, String splitId, String bankCode, String bankName, String card, String address, String niceName, Integer withdrawType) {
        Date d = new Date();
        String sql = new SQLUtils(this.tableName).field("id,amount,account,test,withdrawalTimes,adminRemark,splitId").field("bankCode,bankName,card,address,niceName").field("createTime,lastTime").field("withdrawType")
                .getInsert();
        this.dbSession.update(sql, new Object[]{id, amount, account, test, Integer.valueOf(withdrawalTimes), adminRemark, splitId, bankCode, bankName, card, address, niceName, d, d, withdrawType});
    }


    public int getUserDepositNSCount(String account) {
        return this.dbSession.getInt("SELECT count(*) FROM t_deposit WHERE account = ? AND status in (0,5,6,7,99)", new Object[]{account}).intValue();
    }

}
