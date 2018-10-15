package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.report.UserReport;
import com.hs3.models.user.UserTeamInfo;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("userReportDao")
public class UserReportDao
        extends BaseDao<UserReport> {
    public boolean save(UserReport m) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", new Object[]{this.tableName,
                "account,createDate,betAmount,rebateAmount,juniorRebateAmount,actualSaleAmount,winAmount,count,activityAndSend,rechargeAmount,drawingAmount,test,tigerWinAmount,wages",
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?"});
        int id = this.dbSession.updateKeyHolder(sql, new Object[]{
                m.getAccount(),
                m.getCreateDate(),
                m.getBetAmount(),
                m.getRebateAmount(),
                m.getJuniorRebateAmount(),
                m.getActualSaleAmount(),
                m.getWinAmount(),
                m.getCount(),
                m.getActivityAndSend(),
                m.getRechargeAmount(),
                m.getDrawingAmount(),
                m.getTest(),
                m.getTigerWinAmount(),
                m.getWages()});

        m.setId(Integer.valueOf(id));
        return true;
    }

    public List<UserReport> historyList(Page page, UserReport m, String startTime, String endTime) {
        List<Object> args = new ArrayList();
        StringBuilder buffer = new StringBuilder(" select * from " + this.tableName + " where 1=1");
        buffer.append(" and account = ?");
        args.add(m.getAccount());
        if (!StrUtils.hasEmpty(new Object[]{startTime, endTime})) {
            buffer.append(" and createDate >=? and createDate <= ?");
            args.add(startTime);
            args.add(endTime);
        } else if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            buffer.append(" and createDate <=?");
            args.add(endTime);
        } else if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            buffer.append(" and createDate >=?");
            args.add(startTime);
        }
        buffer.append(" ORDER BY createDate desc");
        String sql = buffer.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, page);
    }

    public List<UserReport> findLossByCreateDate(String createDate) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE createDate =? and count < 0"});
        argsObjects = new Object[]{createDate};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public List<UserReport> findBetByCreateDate(String createDate) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE createDate =? and betAmount > 0"});
        argsObjects = new Object[]{createDate};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public List<UserReport> listTigerWin(String createDate, int limit) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE createDate = ? ORDER BY tigerWinAmount DESC LIMIT ?"});
        Object[] argsObjects = {createDate, Integer.valueOf(limit)};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public List<UserReport> adminList(Page p, String account, String startDate, String endDate) {
        List<Object> args = new ArrayList();
        StringBuffer sb = new StringBuffer(" select t.id,t.account,t.test,t.createDate,");
        sb.append(" IFNULL(t.betAmount,0) as betAmount,");
        sb.append(" IFNULL(t.winAmount,0) as winAmount,");
        sb.append(" IFNULL(t.activityAndSend,0) as activityAndSend,");
        sb.append(" IFNULL(t.rebateAmount,0) as rebateAmount,");
        sb.append(" IFNULL(t.rechargeAmount,0) as rechargeAmount,");
        sb.append(" IFNULL(t.tigerWinAmount,0) as tigerWinAmount,");
        sb.append(" IFNULL(t.drawingAmount,0) as drawingAmount,");
        sb.append(" IFNULL(t.actualSaleAmount,0) as actualSaleAmount,");
        sb.append(" IFNULL(t.count,0) as count,");
        sb.append(" IFNULL(t.wages,0) as wages,");
        sb.append(" IFNULL(t.juniorRebateAmount,0) as juniorRebateAmount");
        sb.append(" from t_user_report t where 1=1 ");
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sb.append(" and account = ?");
            args.add(account);
        }
        if (!StrUtils.hasEmpty(new Object[]{startDate, endDate})) {
            sb.append(" and createDate >=? and createDate <= ?");
            args.add(startDate);
            args.add(endDate);
        } else if (!StrUtils.hasEmpty(new Object[]{endDate})) {
            sb.append(" and createDate <=?");
            args.add(endDate);
        } else if (!StrUtils.hasEmpty(new Object[]{startDate})) {
            sb.append(" and createDate >=?");
            args.add(startDate);
        }
        sb.append(" ORDER BY createDate desc,account desc");
        String sql = sb.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, p);
    }

    public int getHumanNum(Date startDate, Date endDate, String account) {
        Object[] argsObjects = null;
        String sql = " select count(DISTINCT account) from " + this.tableName + " where betAmount >0 and account  IN (SELECT account FROM t_user WHERE parentList LIKE CONCAT(( SELECT parentList FROM  t_user WHERE account =? ),'%') or account =?) and createDate BETWEEN ? and ? ";
        argsObjects = new Object[]{account, account, startDate, endDate};
        return this.dbSession.getInt(sql, argsObjects).intValue();
    }

    public List<UserTeamInfo> getUserInfo(String account, Date begin, Date end, int amounts) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("\tu.account,");
        sb.append("\tu.parentAccount,");
        sb.append("\tu.parentList,");
        sb.append("\tu.userType,");
        sb.append("\t(SELECT COUNT(1) FROM t_user WHERE parentList LIKE CONCAT( ( SELECT parentList FROM t_user WHERE account = u.account),'%')) AS teamCount,");
        sb.append("\t(SELECT SUM(amount) FROM t_user WHERE parentList LIKE CONCAT( ( SELECT parentList FROM t_user WHERE account = u.account),'%')) AS teamAmount,");
        sb.append("\t(SELECT COUNT(1) FROM t_user WHERE account = u.account AND regTime>=? AND regTime<=?) AS registerNum,");
        sb.append("\t(SELECT COUNT(1) FROM t_user WHERE account = u.account AND rechargeFirstTime>=? AND rechargeFirstTime<=?) AS firstRechargeNum,");
        sb.append("\tIFNULL(b.rechargeAmount,0) as rechargeAmount,");
        sb.append("\tIFNULL(b.drawingAmount,0) as drawingAmount,");

        sb.append("\tIFNULL(b.wages,0) as wages,");
        sb.append("\tIFNULL(b.winAmount,0) as winAmount,");
        sb.append("\tIFNULL(b.win,0) as win,");
        sb.append("\tIF (b.userCount >= ?, 1, 0) AS userCount ");
        sb.append("FROM t_user u");
        sb.append(" LEFT JOIN ");
        sb.append("(");
        sb.append("\tSELECT");
        sb.append("\t\taccount,");
        sb.append("\t\tsum(rechargeAmount) AS rechargeAmount,\t");
        sb.append("\t\tsum(drawingAmount) AS drawingAmount,");

        sb.append("\t\tsum(wages) as wages,");
        sb.append("\t\tsum(winAmount) as winAmount,");
        sb.append("\t\tsum(count+juniorRebateAmount) as win,");
        sb.append("\t\tmax(betAmount) AS userCount");
        sb.append("\tFROM ");
        sb.append("\t\tt_user_report ");
        sb.append("\tWHERE ");
        sb.append("\t\tcreateDate >= ?");
        sb.append("\t\tAND createDate <= ?");

        sb.append("\tGROUP BY account");
        sb.append(") as b on u.account = b.account ");
        sb.append("WHERE ");
        sb.append("\tu.parentList LIKE  CONCAT((SELECT parentList FROM t_user WHERE account = ?),'%')");
        Object[] args = {begin, end, begin, end, Integer.valueOf(amounts), begin, end, account};
        return this.dbSession.list(sb.toString(), args, UserTeamInfo.class);
    }

    public UserReport getOne(String account, Date startDate, Date endDate, Integer test) {
        List<UserReport> list = getList(account, startDate, endDate, 0, test);
        if (list.isEmpty()) {
            return null;
        }
        return (UserReport) list.get(0);
    }

    public List<UserReport> getList(String account, Date startDate, Date endDate, int types, Integer test) {
        String c = "";
        if (account != null) {
            if (types == 1) {
                c = " AND changeUser IN (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user WHERE account = ?),'%')) ";
            } else {
                c = " AND changeUser = ?";
            }
        }
        String t = "";
        if (test != null) {
            t = " AND test =? ";
        }
        String sql = "SELECT \t\taccount,\t\ttest,     '" +
                DateUtils.formatDate(startDate) + "' as createDate," +
                "     -sum(CASE a.changeType WHEN 1 THEN a.amount ELSE 0 END ) as betAmount," +
                "     sum(CASE a.changeType WHEN 2 THEN a.amount ELSE 0 END ) as rebateAmount," +
                "     sum(CASE a.changeType WHEN 3 THEN a.amount ELSE 0 END ) as juniorRebateAmount," +
                "     -sum(CASE  WHEN a.changeType in (1,2,3) THEN a.amount ELSE 0 END ) as actualSaleAmount," +
                "     sum(CASE a.changeType WHEN 4 THEN a.amount ELSE 0 END ) as winAmount," +
                "     sum(CASE  WHEN a.changeType = 4 and a.lotteryId ='tiger' THEN a.amount ELSE 0 END ) as tigerWinAmount," +
                "     sum(CASE  WHEN a.changeType in (19,29,41) THEN a.amount ELSE 0 END ) as activityAndSend," +
                "     sum(CASE  WHEN a.changeType in (11,12,21) THEN a.amount ELSE 0 END ) as rechargeAmount," +
                "     -sum(CASE a.changeType WHEN 18 THEN a.amount ELSE 0 END ) as drawingAmount," +
                "     sum(CASE  WHEN a.changeType in (28,40) THEN a.amount ELSE 0 END ) as wages," +
                "     sum(CASE  WHEN a.changeType in (1,2,3,4,28,40,19,29,41) THEN a.amount ELSE 0 END ) as count" +
                "\t  FROM (" +
                "\t\tSELECT " +
                "\t\t\tchangeUser as account," +
                "         test," +
                "\t\t\tlotteryId," +
                "\t\t\tchangeAmount as amount," +
                "\t\t\taccountChangeTypeId as changeType" +
                " \tFROM " +
                "\t\t\tt_amount_change" +
                "\t\tWHERE betId in (select id from t_bet where status in (1,2) AND lastTime BETWEEN ? AND ? )" +
                c +
                t +
                "     AND accountChangeTypeId in (1,2,3,4)" +
                "\t\tUNION ALL " +
                " \tSELECT " +
                "\t\t\tchangeUser as account," +
                "         test," +
                "\t\t\t'' as lotteryId," +
                "\t\t\tchangeAmount as amount," +
                "\t\t\taccountChangeTypeId as changeType" +
                "\t\tFROM t_finance_change" +
                "     WHERE changeTime  BETWEEN ? AND ?" +
                c +
                t +
                "     AND accountChangeTypeId in (11,12,18,19,21,28,29,40,41)" +
                "  ) as a GROUP BY account,test";
        Object[] argsObjects = null;
        if (account == null) {
            if (test == null) {
                argsObjects = new Object[]{startDate, endDate, startDate, endDate};
            } else {
                argsObjects = new Object[]{startDate, endDate, test, startDate, endDate, test};
            }
        } else if (test == null) {
            argsObjects = new Object[]{startDate, endDate, account, startDate, endDate, account};
        } else {
            argsObjects = new Object[]{startDate, endDate, account, test, startDate, endDate, account, test};
        }
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public int getCountByCreatDate(Date createDate) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"count(1)", this.tableName, "WHERE createDate =?"});
        Object[] args = {createDate};
        return this.dbSession.getInt(sql, args).intValue();
    }

    public List<String> getAccountList(Date createDate) {
        StringBuilder sb = new StringBuilder(" select account from " + this.tableName + " where createDate=?");
        String sql = sb.toString();
        Object[] argsObjects = {DateUtils.getDate(createDate)};
        return this.dbSession.listSerializable(sql, argsObjects, String.class);
    }

    public int deleteByCreateDate(Date createDate) {
        String sql = "DELETE FROM " + this.tableName + " WHERE createDate=?";
        return this.dbSession.update(sql, new Object[]{DateUtils.getDate(createDate)});
    }

    public Date getMaxDate() {
        String sql = "select max(createDate) from t_user_report";
        return DateUtils.toDateNull(this.dbSession.getString(sql), "yyyy-MM-dd");
    }

    public UserReport totalBySelf(String account, Date startTime, Date endTime, Integer test) {
        String sql = "select t.account,\t\tSUM(IFNULL(t.betAmount,0)) as betAmount,\t\tSUM(IFNULL(t.winAmount,0)) as winAmount,\t\tSUM(IFNULL(t.activityAndSend,0)) as activityAndSend,\t\tSUM(IFNULL(t.rebateAmount,0)) as rebateAmount,\t\tSUM(IFNULL(t.rechargeAmount,0)) as rechargeAmount,\t\tSUM(IFNULL(t.tigerWinAmount,0)) as tigerWinAmount,\t\tSUM(IFNULL(t.drawingAmount,0)) as drawingAmount,\t\tSUM(IFNULL(t.actualSaleAmount,0)) as actualSaleAmount,\t\tSUM(IFNULL(t.count,0)) as count,\t\tSUM(IFNULL(t.wages,0)) as wages,\t\tSUM(IFNULL(t.juniorRebateAmount,0)) as juniorRebateAmount from t_user_report t where t.account=? and t.createDate BETWEEN ? and ? and t.test =?";


        Object[] argsObjects = null;
        argsObjects = new Object[]{account, DateUtils.getDate(startTime), DateUtils.getDate(endTime), test};
        return (UserReport) this.dbSession.getObject(sql, argsObjects, this.cls);
    }

    //**************************************以下为变更部分*****************************************

    public List<UserReport> gainLostList(String account, String startDate, String endDate) {
        List<Object> args = new ArrayList<Object>();
        StringBuffer sb = new StringBuffer(" select t.id,t.account,t.createDate,");
        sb.append(" IFNULL(t.betAmount,0) as betAmount,");//投注金额
        sb.append(" IFNULL(t.winAmount,0) as winAmount,");//中奖金额
        sb.append(" IFNULL(t.activityAndSend,0) as activityAndSend,");//活动礼金
        sb.append(" IFNULL(t.rechargeAmount,0) as rechargeAmount,");//充值金额
        sb.append(" IFNULL(t.drawingAmount,0) as drawingAmount,");//提现金额
        sb.append(" IFNULL(t.juniorRebateAmount,0) as juniorRebateAmount");//下级返点金额
        sb.append(" from t_user_report t where 1=1 ");
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sb.append(" and account = ?");
            args.add(account);
        }

        if (!StrUtils.hasEmpty(new Object[]{endDate})) {
            sb.append(" and createDate <=?");
            args.add(endDate);
        }

        if (!StrUtils.hasEmpty(new Object[]{startDate})) {
            sb.append(" and createDate >=?");
            args.add(startDate);
        }

        sb.append(" ORDER BY createDate desc,account desc");
        String sql = sb.toString();
        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, this.cls);
    }


}
