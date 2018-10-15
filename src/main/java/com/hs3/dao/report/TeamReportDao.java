package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.contract.ContractRule;
import com.hs3.entity.report.TeamReport;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("teamReportDao")
public class TeamReportDao
        extends BaseDao<TeamReport> {
    public boolean save(TeamReport m) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", new Object[]{this.tableName,
                "account,createDate,betAmount,rebateAmount,actualSaleAmount,winAmount,count,activityAndSend,juniorRebateAmount,rechargeAmount,drawingAmount,test,wages",
                "?,?,?,?,?,?,?,?,?,?,?,?,?"});
        int id = this.dbSession.updateKeyHolder(sql, new Object[]{
                m.getAccount(),
                m.getCreateDate(),
                m.getBetAmount(),
                m.getRebateAmount(),
                m.getActualSaleAmount(),
                m.getWinAmount(),
                m.getCount(),
                m.getActivityAndSend(),
                m.getJuniorRebateAmount(),
                m.getRechargeAmount(),
                m.getDrawingAmount(),
                m.getTest(),
                m.getWages()});
        m.setId(Integer.valueOf(id));
        return true;
    }

    public int deleteByCreateDate(Date createDate) {
        String sql = "DELETE FROM " + this.tableName + " WHERE createDate=?";
        return this.dbSession.update(sql, new Object[]{createDate});
    }

    public List<TeamReport> findListByContract(Date startDate, Date endDate, List<ContractRule> account) {
        List<Object> cond = new ArrayList();
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"account,sum(IFNULL(betAmount, 0)) as betAmount,sum(IFNULL(count, 0)) as count", this.tableName,
                "WHERE createDate between ? AND ? AND account in (" + getQuestionNumber(account.size()) + ") GROUP BY account "});

        cond.add(startDate);
        cond.add(endDate);
        for (ContractRule a : account) {
            cond.add(a.getAccount());
        }
        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls);
    }

    public TeamReport findByAccount(String account, Date date) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE createDate =? AND account=?"});
        return (TeamReport) this.dbSession.getObject(sql, new Object[]{date, account}, this.cls);
    }

    public int getCountByCreatDate(Date createDate) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"count(1)", this.tableName, "WHERE createDate=? "});
        Object[] args = {DateUtils.getDate(createDate)};
        return this.dbSession.getInt(sql + " for update", args).intValue();
    }

    public boolean isExist(String account, Date createDate) {
        StringBuffer buffer = new StringBuffer("select count(1) from " + this.tableName + " where account=? and createDate = ? ");
        String sql = buffer.toString();
        Object[] argsObjects = {account, DateUtils.getDate(createDate)};
        return this.dbSession.getInt(sql + " for update", argsObjects).intValue() > 0;
    }

    public TeamReport getTeamReportByAccount(String account, Date createDate) {
        String SQL = " SELECT ? AS account,\t\tt.createDate AS createDate,\t\tt.test AS test, \t\tsum(IFNULL(t.betAmount, 0)) AS betAmount,\t\tsum(IFNULL(t.juniorRebateAmount+t.rebateAmount, 0)) AS rebateAmount,\t\tsum(IFNULL(t.actualSaleAmount,0)) AS actualSaleAmount,\t\tsum(IFNULL(t.winAmount, 0)) AS winAmount,\t\tsum(IFNULL(t.activityAndSend, 0)) AS activityAndSend,\t\tsum(IFNULL(t.wages, 0)) AS wages,\t\tsum(IFNULL(t.count, 0)) AS count,\t\tsum(IFNULL(t.rechargeAmount, 0)) AS rechargeAmount,\t\tsum(IFNULL(t.drawingAmount, 0)) AS drawingAmount FROM  \t\tt_user_report t WHERE \t\tt.account in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((\tSELECT parentList FROM t_user WHERE account = ?),'%'))\t\tAND t.createDate = ? GROUP BY t.createDate,t.test ";


        Object[] argsObjects = null;
        argsObjects = new Object[]{account, account, DateUtils.getDate(createDate)};
        return this.dbSession.getObject(SQL, argsObjects, this.cls);
    }

    public List<TeamReport> newHistoryStatistics(Page page, String account, Date begin, Date end) {
        List<Object> args = new ArrayList();
        StringBuffer sb = new StringBuffer(" select * from t_team_report where 1=1");
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sb.append(" and account=? ");
            args.add(account);
        }
        if (!StrUtils.hasEmpty(new Object[]{begin, end})) {
            sb.append(" and createDate >=? and createDate <= ?");
            args.add(begin);
            args.add(end);
        } else if (!StrUtils.hasEmpty(new Object[]{begin})) {
            sb.append(" and createDate >= ?");
            args.add(begin);
        } else if (!StrUtils.hasEmpty(new Object[]{end})) {
            sb.append(" and createDate <= ?");
            args.add(end);
        }
        sb.append(" order by createDate desc");
        String sql = sb.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, page);
    }

    public Date getMaxDate() {
        String sql = "select max(createDate) from t_team_report";
        return DateUtils.toDateNull(this.dbSession.getString(sql), "yyyy-MM-dd");
    }

    public TeamReport totalBySelf(String account, Date begin, Date end, Integer test) {
        String sql = "select account,\t\tsum(IFNULL(betAmount,0)) as betAmount,\t\tsum(IFNULL(rebateAmount,0)) as rebateAmount,\t\tsum(IFNULL(actualSaleAmount,0)) as actualSaleAmount,\t\tsum(IFNULL(winAmount,0)) as winAmount,\t\tsum(IFNULL(count,0)) as count,\t\tsum(IFNULL(wages, 0)) AS wages,\t\tsum(IFNULL(activityAndSend,0)) AS activityAndSend,\t\tsum(IFNULL(rechargeAmount,0)) AS rechargeAmount,\t\tsum(IFNULL(drawingAmount,0)) AS drawingAmount,\t\ttest \tFROM t_team_report \tWHERE account = ? AND createDate BETWEEN ? and ? AND test = ?";


        Object[] argsObjects = null;
        argsObjects = new Object[]{account, DateUtils.getDate(begin), DateUtils.getDate(end), test};
        return this.dbSession.getObject(sql, argsObjects, this.cls);
    }

    public List<TeamReport> totalByChild(String account, Date begin, Date end, Integer test) {
        String c = "";
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            c = " AND account in (select account from t_user where account = ? OR parentAccount = ?)";
        } else {
            c = " AND account in (select account from t_user where test = ? and account=parentAccount)";
        }
        String sql = "select account,\t\tsum(IFNULL(betAmount,0)) as betAmount,\t\tsum(IFNULL(rebateAmount,0)) as rebateAmount,\t\tsum(IFNULL(actualSaleAmount,0)) as actualSaleAmount,\t\tsum(IFNULL(winAmount,0)) as winAmount,\t\tsum(IFNULL(count,0)) as count,\t\tsum(IFNULL(wages, 0)) AS wages,\t\tsum(IFNULL(activityAndSend,0)) AS activityAndSend,\t\tsum(IFNULL(rechargeAmount,0)) AS rechargeAmount,\t\tsum(IFNULL(drawingAmount,0)) AS drawingAmount,\t\ttest \tFROM t_team_report \tWHERE createDate BETWEEN ? and ? " +


                c +
                " GROUP BY account,test";
        Object[] argsObjects = null;
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            argsObjects = new Object[]{DateUtils.getDate(begin), DateUtils.getDate(end), account, account};
        } else {
            argsObjects = new Object[]{DateUtils.getDate(begin), DateUtils.getDate(end), test};
        }
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public List<TeamReport> getHisTeamDetails(Page p, String account, Date begin, Date end, Integer test) {
        List<Object> args = new ArrayList();
        StringBuffer sb = new StringBuffer(" select * from t_team_report where 1=1");
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sb.append(" and account=? ");
            args.add(account);
        }
        if (test != null) {
            sb.append(" and test=? ");
            args.add(test);
        }
        if (!StrUtils.hasEmpty(new Object[]{begin, end})) {
            sb.append(" and createDate >=? and createDate <= ?");
            args.add(begin);
            args.add(end);
        } else if (!StrUtils.hasEmpty(new Object[]{begin})) {
            sb.append(" and createDate >= ?");
            args.add(begin);
        } else if (!StrUtils.hasEmpty(new Object[]{end})) {
            sb.append(" and createDate <= ?");
            args.add(end);
        }
        sb.append(" order by createDate desc");
        String sql = sb.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, p);
    }
}
