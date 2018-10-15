package com.hs3.dao.finance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.hs3.dao.BaseDao;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.lotts.AmountChange;
import com.hs3.entity.report.TeamReport;
import com.hs3.utils.StrUtils;
import com.hs3.utils.sys.WebDateUtils;

@Repository("financeChangeDao")
public class FinanceChangeDao
        extends BaseDao<FinanceChange> {
    private static final String SQL_LIST = "SELECT\t* FROM (\t\tSELECT t.changeTime, t.accountChangeTypeId, t2. NAME AS remark, t.changeAmount FROM\t\t\t(SELECT n1.changeUser, n1.changeTime, n1.accountChangeTypeId, n1.changeAmount FROM t_finance_change n1 WHERE n1.changeUser = ? UNION ALL SELECT n2.changeUser, n2.changeTime, n2.accountChangeTypeId, n2.changeAmount FROM t_amount_change n2 WHERE n2.changeUser = ?) t, t_account_change_type t2\t\tWHERE t.accountChangeTypeId = t2.id ORDER BY t.changeTime DESC\t) o LIMIT ?,?";

    public void save(FinanceChange m) {
        saveAuto(m);
    }

    public int update(FinanceChange m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"changeUser", "test", "financeId", "accountChangeTypeId", "changeTime", "changeAmount", "balance", "status", "remark", "operator"};
    }

    protected Object[] getValues(FinanceChange m) {
        return new Object[]{m.getChangeUser(), m.getTest(), m.getFinanceId(), m.getAccountChangeTypeId(), m.getChangeTime(), m.getChangeAmount(), m.getBalance(), m.getStatus(), m.getRemark(),
                m.getOperator()};
    }

    public int updateByFinanceId(String financeId, String remark) {
        return this.dbSession.update("UPDATE t_finance_change SET remark = ? WHERE financeId = ?", new Object[]{remark, financeId});
    }

    public int updateByFinanceId(String financeId, Integer status, String remark) {
        return this.dbSession.update("UPDATE t_finance_change SET status = ?, remark = ?, changeTime = ? WHERE financeId = ?", new Object[]{status, remark, new Date(), financeId});
    }

    public int updateByFinanceId(String financeId, Integer status, String remark, Integer accountChangeTypeId) {
        return this.dbSession.update("UPDATE t_finance_change SET status = ?, remark = ?, changeTime = ?, accountChangeTypeId = ? WHERE financeId = ?",
                new Object[]{status, remark, new Date(), accountChangeTypeId, financeId});
    }

    public int countByFinanceId(String financeId, Integer accountChangeTypeId) {
        return this.dbSession.getInt("SELECT count(1) FROM t_finance_change WHERE financeId = ? AND accountChangeTypeId = ?", new Object[]{financeId, accountChangeTypeId}).intValue();
    }

    public List<FinanceChange> listByCond(String account, int start, int limit) {
        return this.dbSession.list("SELECT\t* FROM (\t\tSELECT t.changeTime, t.accountChangeTypeId, t2. NAME AS remark, t.changeAmount FROM\t\t\t(SELECT n1.changeUser, n1.changeTime, n1.accountChangeTypeId, n1.changeAmount FROM t_finance_change n1 WHERE n1.changeUser = ? UNION ALL SELECT n2.changeUser, n2.changeTime, n2.accountChangeTypeId, n2.changeAmount FROM t_amount_change n2 WHERE n2.changeUser = ?) t, t_account_change_type t2\t\tWHERE t.accountChangeTypeId = t2.id ORDER BY t.changeTime DESC\t) o LIMIT ?,?", new Object[]{account, account, Integer.valueOf(start), Integer.valueOf(limit)}, this.cls);
    }

    public List<FinanceChange> listByCond(FinanceChange financeChange, Date startTime, Date endTime, boolean isIncludeChild, String[] accountChangeTypes, Page page) {
        String sql = "SELECT * FROM t_finance_change WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (financeChange.getAccountChangeTypeId() != null) {
            sql = sql + " AND accountChangeTypeId = ?";
            cond.add(financeChange.getAccountChangeTypeId());
        }
        if (!StringUtils.isEmpty(financeChange.getChangeUser())) {
            if (isIncludeChild) {
                sql = sql + " AND changeUser in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user WHERE account = ?),'%'))";
            } else {
                sql = sql + " AND changeUser = ?";
            }
            cond.add(financeChange.getChangeUser());
        }
        if (financeChange.getTest() != null) {
            sql = sql + " AND test = ?";
            cond.add(financeChange.getTest());
        }
        if (financeChange.getStatus() != null) {
            sql = sql + " AND status = ?";
            cond.add(financeChange.getStatus());
        }
        if (!StringUtils.isEmpty(financeChange.getFinanceId())) {
            sql = sql + " AND financeId = ?";
            cond.add(financeChange.getFinanceId());
        }
        if (startTime != null) {
            sql = sql + " AND changeTime >= ?";
            cond.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ?";
            cond.add(endTime);
        }
        if ((accountChangeTypes != null) && (accountChangeTypes.length > 0)) {
            sql = sql + " AND accountChangeTypeId in(" + getQuestionNumber(accountChangeTypes.length) + ")";
            for (int i = 0; i < accountChangeTypes.length; i++) {
                cond.add(accountChangeTypes[i]);
            }
        }
        sql = sql + " ORDER BY changeTime desc, id desc";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public List<FinanceChange> getActivityData(TeamReport m) {
        StringBuffer buffer = new StringBuffer(" select  f.changeUser as changeUser ,SUM(f.changeAmount) AS changeAmount ");
        buffer.append(" from  t_finance_change f ");
        buffer.append(" where f.accountChangeTypeId ='19' ");
        buffer.append(" AND f.changeUser IN (SELECT account FROM t_user WHERE ");
        buffer.append(" parentList LIKE CONCAT(( ");
        buffer.append(" SELECT\tparentList FROM  t_user ");
        buffer.append(" WHERE account =? AND test =?),'%')) ");
        buffer.append(" AND f.changeTime BETWEEN ? AND ? ");
        buffer.append(" GROUP BY  f.changeUser ");
        String sql = buffer.toString();
        Object[] argsObjects = null;
        argsObjects = new Object[]{m.getAccount(), m.getTest(), WebDateUtils.getBeginTime(m.getCreateDate()), m.getCreateDate()};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public List<FinanceChange> getAllActivityData(TeamReport m) {
        StringBuffer buffer = new StringBuffer(" select  f.changeUser as changeUser ,SUM(f.changeAmount) AS changeAmount ");
        buffer.append(" from  t_finance_change f ");
        buffer.append(" where f.accountChangeTypeId ='19' ");
        buffer.append(" AND f.changeTime BETWEEN ? AND ? ");
        buffer.append(" GROUP BY  f.changeUser ");
        String sql = buffer.toString();
        Object[] argsObjects = null;
        argsObjects = new Object[]{WebDateUtils.getBeginTime(m.getCreateDate()), m.getCreateDate()};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public boolean getCountFinanceChange(Date changeTime, String key) {
        String selectSQL = new SQLUtils(this.tableName).field("count(1)").where("date(changeTime)=date(?) and financeId=?").getSelect();
        int n = this.dbSession.getInt(selectSQL, new Object[]{changeTime, key}).intValue();
        return n != 0;
    }

    public List<AmountChange> getTeamList(String account, Date b, Date end) {
        String sql = "SELECT * FROM t_amount_change WHERE changeTime>=? AND changeTime<=? AND changeUser in(SELECT account FROM t_user where parentList LIKE CONCAT(SELECT parentList FROM t_user WHERE account = ? ,'%'))";
        return this.dbSession.list(sql, new Object[]{b, end, account}, AmountChange.class);
    }

    //**************************************以下为变更部分*****************************************

    public List<FinanceChange> tradeList(String account, Integer include, Integer accountChangeType, String startTime, String endTime, Integer start, Integer limit) {
        List<Object> args = new ArrayList<Object>();
        String where = " WHERE 1=1 ";

        if (!StrUtils.hasEmpty(new Object[]{include})) {
            if ((include == null) || (include.intValue() == 0)) {
                where = where + " and changeUser =?";
                args.add(account);
            } else if (include.intValue() == 1) {
                where = where + " and changeUser in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%'))";
                args.add(account);
            } else if (include.intValue() == 2) {
                where = where + " and changeUser in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%')) and changeUser !=?";
                args.add(account);
                args.add(account);
            }
        }
        if (!StrUtils.hasEmpty(new Object[]{accountChangeType}) && accountChangeType != 100 && accountChangeType != 0) {
            if (accountChangeType == 1) {
                where = where + " and accountChangeTypeId in (18)";
            } else if (accountChangeType == 2) {
                where = where + " and accountChangeTypeId in (11,12,13,15,20,21,24,26)";
            }
        }
        if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            where = where + " and changeTime >=?";
            args.add(startTime);
        }
        if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            where = where + " and changeTime <=?";
            args.add(endTime);
        }


        String limitStr = "";

        if (!StrUtils.hasEmpty(new Object[]{start}) && !StrUtils.hasEmpty(new Object[]{limit})) {

            limitStr = " LIMIT ?,? ";

            args.add(start);
            args.add(limit);
        }

        Object[] arg = args.toArray();
        String sql = "SELECT id,accountChangeTypeId,changeAmount,changeTime,changeUser FROM " + this.tableName + where + " order by changeTime desc " + limitStr;

        return this.dbSession.list(sql, arg, this.cls);
    }


    //代理分红：周期分红
    public List<Map<String, Object>> getTeamInfo(String account, Date startTime, Date endTime, Integer accountChangeTypeId) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT sum(changeAmount) as sumChangeAmount ";
        sql = sql + " FROM t_finance_change WHERE 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ?";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ?";
            args.add(endTime);
        }
        if (!StrUtils.hasEmpty(new Object[]{accountChangeTypeId})) {
            sql = sql + " and accountChangeTypeId =?";
            args.add(accountChangeTypeId);
        }
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND changeUser =?";
            args.add(account);
        }

        sql = sql + " order by changeTime desc";

        Object[] arg = args.toArray();
        return this.dbSession.listMap(sql, arg, null);
    }

    //根据用户与帐变类型获取非投注帐变的最后一条数据
    public FinanceChange findLastFinanceChange(String account, Integer accountChangeTypeId) {
        String sql = "SELECT * from t_finance_change where changeUser = ? and accountChangeTypeId =? and status = 2 ORDER BY changeTime desc limit 1";
        Object[] args = {account, accountChangeTypeId};
        return (FinanceChange) this.dbSession.getObject(sql, args, FinanceChange.class);
    }

    //根据用户与时间获取非投注帐变（在线充值、 银行充值、充值手续费、现金充值、活动派奖、理赔充值）的变更金额列表
    public List<FinanceChange> findListFinanceChange(String account, Date startTime) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT changeAmount from t_finance_change where changeUser = ? and status = 2 and accountChangeTypeId in(11,12,13,21,19,20)";
        args.add(account);
        if (startTime != null) {
            sql = sql + " AND changeTime >= ?";
            args.add(startTime);
        }
        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, FinanceChange.class);
    }

    //获取非投注帐变涉及综合统计的变更金额列表
    //充值金额类：银行充值、在线充值、 充值手续费、现金充值/12、11、13、21【充值笔数】
    //提现金额类：提款成功/18【提现笔数】
    //活动礼金类：活动派发/19
    //拒绝提款金额类：提款失败/17
    //盈利赢率的计算：系统分红、理赔充值、私返、消费佣金/22、20、41、25
    //管理员扣减类：管理员扣减/37
    public List<FinanceChange> cpsReportFinanceChange(Date startTime, Date endTime) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT changeUser, changeAmount, accountChangeTypeId from t_finance_change where 1 = 1 and test = 0 and status = 2 and accountChangeTypeId in(11,12,13,21,18,19,17,22,20,41,25,37)";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ?";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ?";
            args.add(endTime);
        }
        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, FinanceChange.class);
    }

}
