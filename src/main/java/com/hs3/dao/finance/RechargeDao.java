package com.hs3.dao.finance;

import com.hs3.dao.BaseDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.finance.Recharge;
import com.hs3.utils.StrUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("rechargeDao")
public class RechargeDao
        extends BaseDao<Recharge> {
    private static final String SQL_COUNTUSERSTATUS = "SELECT COUNT(1) FROM t_recharge WHERE account = ? and status = ?";
    private static final String SQL_LISTUSERSTATUS = "SELECT * FROM t_recharge WHERE account = ? and status = ?";

    public Integer countUserStatus(String account, Integer status) {
        return this.dbSession.getInt("SELECT COUNT(1) FROM t_recharge WHERE account = ? and status = ?", new Object[]{account, status});
    }

    public List<Recharge> listUserStatus(String account, Integer status) {
        return this.dbSession.list("SELECT * FROM t_recharge WHERE account = ? and status = ?", new Object[]{account, status}, this.cls);
    }

    public List<Recharge> listUnProcess(Date date) {
        return this.dbSession.list("SELECT * FROM t_recharge WHERE   status = 0 and rechargeType = 1  and createTime < ? limit 1000", new Object[]{ date}, this.cls);
    }

    public int updateStatus(String id, Integer status) {
        return this.dbSession.update("UPDATE t_recharge SET status=? WHERE id=?", new Object[]{status, id});
    }

    public List<Recharge> listByCond(boolean isMaster, Recharge recharge, int operatorType, Date startTime, Date endTime, Integer[] statusArray, Page page) {
        String sql = (isMaster ? "/*master*/" : "") + "SELECT *," + UserDao.getMarkSQL() + " FROM t_recharge i WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{recharge.getAccount()})) {
            sql = sql + " AND account = ?";
            cond.add(recharge.getAccount());
        }
        if (recharge.getTest() != null) {
            sql = sql + " AND test = ?";
            cond.add(recharge.getTest());
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
        if (operatorType == 1) {
            sql = sql + " AND operator IS NOT NULL AND operator != ''";
        } else if (operatorType == 2) {
            sql = sql + " AND operator IS NULL OR operator = ''";
        }
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            cond.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            cond.add(endTime);
        }
        if (recharge.getRechargeType() != null) {
            sql = sql + " AND rechargeType = ?";
            cond.add(recharge.getRechargeType());
        }
        if (!StrUtils.hasEmpty(new Object[]{recharge.getReceiveBankName()})) {
            sql = sql + " AND receiveBankName = ?";
            cond.add(recharge.getReceiveBankName());
        }
        if (!StrUtils.hasEmpty(new Object[]{recharge.getReceiveCard()})) {
            sql = sql + " AND receiveCard = ?";
            cond.add(recharge.getReceiveCard());
        }
        if (!StrUtils.hasEmpty(new Object[]{recharge.getTraceId()})) {
            sql = sql + " AND traceId = ?";
            cond.add(recharge.getTraceId());
        }
        if (!StrUtils.hasEmpty(new Object[]{recharge.getSerialNumber()})) {
            sql = sql + " AND serialNumber = ?";
            cond.add(recharge.getSerialNumber());
        }
        Map<String, Object> pageMap = this.dbSession.getMap("SELECT IFNULL(SUM(AMOUNT), 0) as amountSum, IFNULL(SUM(poundage), 0) as poundageSum FROM (" + sql + ") as v", cond.toArray(new Object[cond.size()]));

        page.setObj(pageMap);

        sql = sql + " ORDER BY createTime desc, id desc";


        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public List<Recharge> listByCond(Recharge recharge, int operatorType, Date startTime, Date endTime, Integer[] statusArray, Page page) {
        return listByCond(false, recharge, operatorType, startTime, endTime, statusArray, page);
    }

    public void save(Recharge m) {
        Date now = new Date();
        if (m.getCreateTime() == null) {
            m.setCreateTime(now);
        }
        if (m.getCreateDate() == null) {
            m.setCreateDate(now);
        }
        String sql = new SQLUtils(this.tableName).field("id,status,amount,rechargeType,account,test").field("bankName,bankNameCode,card,address,niceName")
                .field("traceId,receiveBankId,receiveBankName,receiveCard,receiveAddress,receiveNiceName,receiveLink").field("createTime,lastTime,serialNumber,createDate,operator,poundage,realAmount, classKey").getInsert();
        this.dbSession.update(sql,
                new Object[]{m.getId(), m.getStatus(), m.getAmount(), m.getRechargeType(), m.getAccount(), m.getTest(), m.getBankName(), m.getBankNameCode(), m.getCard(), m.getAddress(),
                        m.getNiceName(), m.getTraceId(), m.getReceiveBankId(), m.getReceiveBankName(), m.getReceiveCard(), m.getReceiveAddress(), m.getReceiveNiceName(), m.getReceiveLink(),
                        m.getCreateTime(), m.getLastTime(), m.getSerialNumber(), m.getCreateDate(), m.getOperator(), m.getPoundage(), m.getRealAmount(), m.getClassKey()});
    }

    public int setDispose(String id, Integer status, String operator, String remark, String serialNumber) {
        String sql = new SQLUtils(this.tableName).field("status,operator,remark,lastTime,serialNumber").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{status, operator, remark, new Date(), serialNumber, id});
    }

    public BigDecimal getMaxRecharge(String account, Date date, Date endTime) {
        String sql = String.format("SELECT MAX(amount) FROM %s WHERE account=? AND lastTime>=? AND lastTime<=? AND status=2", new Object[]{this.tableName});
        String n = this.dbSession.getString(sql, new Object[]{account, date, endTime});
        BigDecimal rel = null;
        if (n != null) {
            rel = new BigDecimal(n);
        }
        return rel;
    }

    public BigDecimal getFirstRecharge(String account, Date date, Date endTime) {
        String sql = String.format("SELECT amount FROM %s WHERE account=? AND lastTime>=? AND lastTime<=? AND status=2 ORDER BY lastTime ASC LIMIT 1 ", new Object[]{this.tableName});
        String n = this.dbSession.getString(sql, new Object[]{account, date, endTime});
        BigDecimal rel = null;
        if (n != null) {
            rel = new BigDecimal(n);
        }
        return rel;
    }

    public Recharge findByTodayTraceId(String traceId, Date day) {
        return (Recharge) this.dbSession.getObject("SELECT * from t_recharge WHERE traceId = ? and createDate = ?", new Object[]{traceId, day}, this.cls);
    }

    public int countTraceId(String traceId, Date today, Date ysday) {
        return this.dbSession.getInt("SELECT count(1) FROM t_recharge WHERE traceId = ? AND (createDate = ? OR createDate = ?)", new Object[]{traceId, today, ysday}).intValue();
    }

    public List<Recharge> listRecord(String account, Integer start, Integer limit) {
        return this.dbSession.list("SELECT * FROM " + this.tableName + " WHERE account = ? AND status = 2 ORDER BY createTime DESC  LIMIT ?,?", new Object[]{account, start, limit}, this.cls);
    }

    public List<Recharge> listByHome(String account, boolean isIncludeChild, String startTime, String endTime, Page page) {
        String sql = "SELECT * FROM t_recharge WHERE 1 = 1";
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

        return this.dbSession.list(sql, cond.toArray(), this.cls, page);
    }

    public int updateInfo(String id, String card, String niceName) {
        String sql = new SQLUtils(this.tableName).field("card,niceName,status").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{card, niceName, Integer.valueOf(0), id});
    }

    //**************************************以下为变更部分*****************************************


    public List<Recharge> listRecharge(String account, Integer start, Integer limit) {
        return this.dbSession.list("SELECT * FROM " + this.tableName + " WHERE account = ? ORDER BY createTime DESC  LIMIT ?,?", new Object[]{account, start, limit}, this.cls);
    }


    public List<Recharge> listByCond_Z(boolean isMaster, Recharge recharge, int operatorType, Date startTime, Date endTime, Integer[] statusArray, Page page) {
        String sql = (isMaster ? "/*master*/" : "") + "SELECT *," + UserDao.getMarkSQL() + " FROM t_recharge i WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{recharge.getAccount()})) {
            sql = sql + " AND account = ?";
            cond.add(recharge.getAccount());
        }
        if (recharge.getTest() != null) {
            sql = sql + " AND test = ?";
            cond.add(recharge.getTest());
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
        if (operatorType == 1) {
            sql = sql + " AND operator IS NOT NULL AND operator != ''";
        } else if (operatorType == 2) {
            sql = sql + " AND operator IS NULL OR operator = ''";
        }
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            cond.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            cond.add(endTime);
        }
        if (recharge.getRechargeType() != null) {
            sql = sql + " AND rechargeType = ?";
            cond.add(recharge.getRechargeType());
        }
        if (!StrUtils.hasEmpty(new Object[]{recharge.getReceiveBankName()})) {
            sql = sql + " AND receiveBankName = ?";
            cond.add(recharge.getReceiveBankName());
        }
        if (!StrUtils.hasEmpty(new Object[]{recharge.getReceiveCard()})) {
            sql = sql + " AND receiveCard = ?";
            cond.add(recharge.getReceiveCard());
        }
        if (!StrUtils.hasEmpty(new Object[]{recharge.getTraceId()})) {
            sql = sql + " AND traceId = ?";
            cond.add(recharge.getTraceId());
        }
        if (!StrUtils.hasEmpty(new Object[]{recharge.getSerialNumber()})) {
            sql = sql + " AND serialNumber = ?";
            cond.add(recharge.getSerialNumber());
        }
        if (!StrUtils.hasEmpty(new Object[]{recharge.getCheckCode()})) {
            sql = sql + " AND checkCode like '%"+recharge.getCheckCode()+"%'";
        }

        Map<String, Object> pageMap = this.dbSession.getMap("SELECT IFNULL(SUM(AMOUNT), 0) as amountSum, IFNULL(SUM(poundage), 0) as poundageSum FROM (" + sql + ") as v", cond.toArray(new Object[cond.size()]));

        page.setObj(pageMap);

        sql = sql + " ORDER BY createTime desc, id desc";


        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }


    public void save_Z(Recharge m) {
        Date now = new Date();
        if (m.getCreateTime() == null) {
            m.setCreateTime(now);
        }
        if (m.getCreateDate() == null) {
            m.setCreateDate(now);
        }
        String sql = new SQLUtils(this.tableName).field("id,status,amount,rechargeType,account,test").field("bankName,bankNameCode,card,address,niceName")
                .field("traceId,receiveBankId,receiveBankName,receiveCard,receiveAddress,receiveNiceName,receiveLink").field("createTime,lastTime,serialNumber,createDate,operator,poundage,realAmount")
                .field("checkCode").getInsert();
        this.dbSession.update(sql,
                new Object[]{m.getId(), m.getStatus(), m.getAmount(), m.getRechargeType(), m.getAccount(), m.getTest(), m.getBankName(), m.getBankNameCode(), m.getCard(), m.getAddress(),
                        m.getNiceName(), m.getTraceId(), m.getReceiveBankId(), m.getReceiveBankName(), m.getReceiveCard(), m.getReceiveAddress(), m.getReceiveNiceName(), m.getReceiveLink(),
                        m.getCreateTime(), m.getLastTime(), m.getSerialNumber(), m.getCreateDate(), m.getOperator(), m.getPoundage(), m.getRealAmount(), m.getCheckCode()});
    }


    public List<Map<String, Object>> getTeamInfo(String account, Date startTime, Date endTime, Integer status, Integer levelId) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT account,createTime,status,bankName,bankNameCode,receiveBankId,receiveBankName,receiveCard,receiveAddress,checkCode ";
        sql = sql + " FROM t_recharge WHERE 1=1 ";
        if (!StrUtils.hasEmpty(new Object[]{levelId})) {
            sql = sql + " AND bankNameCode in (select code from t_bank_name where id in (select nameId from t_bank_sys where levelId = ? ) ) ";
            args.add(levelId);
        }
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            args.add(endTime);
        }
        if (!StrUtils.hasEmpty(new Object[]{status})) {
            sql = sql + " and status =?";
            args.add(status);
        }
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND account in (SELECT u.account FROM t_user as u WHERE u.parentList LIKE CONCAT((SELECT us.parentList from t_user as us where us.account=?),'%')) ";
            args.add(account);
        }

        sql = sql + " order by createTime desc";

        Object[] arg = args.toArray();
        return this.dbSession.listMap(sql, arg, null);
    }

    public List<Map<String, Object>> getTeamInfo_DISTINCT(String account, Date startTime, Date endTime, Integer status, Integer levelId) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT DISTINCT account ";
        sql = sql + " FROM t_recharge WHERE 1=1 ";
        if (!StrUtils.hasEmpty(new Object[]{levelId})) {
            sql = sql + " AND receiveBankId in ( select id from t_bank_sys where levelId = ? ) ";
            args.add(levelId);
        }
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            args.add(endTime);
        }
        if (!StrUtils.hasEmpty(new Object[]{status})) {
            sql = sql + " and status =?";
            args.add(status);
        }
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND account in (SELECT u.account FROM t_user as u WHERE u.parentList LIKE CONCAT((SELECT us.parentList from t_user as us where us.account=?),'%')) ";
            args.add(account);
        }

        sql = sql + " order by createTime desc";

        Object[] arg = args.toArray();
        return this.dbSession.listMap(sql, arg, null);
    }

    //获取涉及综合统计的充值信息列表
    //拒绝充值金额类：拒绝充值/1
    public List<Recharge> cpsReportRecharge(Date startTime, Date endTime) {
        String sql = "SELECT amount,account,status from t_recharge where 1 = 1 and test = 0 and status in (1) ";
        List<Object> cond = new ArrayList();
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            cond.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            cond.add(endTime);
        }
        sql = sql + " ORDER BY createTime desc, id desc";
        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls);
    }

    public Recharge firstRechargeByAccount(String account) {
        return (Recharge) this.dbSession.getObject("SELECT account,createTime,lastTime,rechargeType,bankName,status from t_recharge WHERE 1 = 1 and test = 0 and status = 2 and account = ? ORDER BY createTime limit 1 ", new Object[]{account}, this.cls);
    }

}
