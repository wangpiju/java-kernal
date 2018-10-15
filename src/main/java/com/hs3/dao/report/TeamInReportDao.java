package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.report.TeamInReport;
import com.hs3.utils.StrUtils;
import com.hs3.utils.sys.WebDateUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("teamInReportDao")
public class TeamInReportDao
        extends BaseDao<TeamInReport> {
    public boolean save(TeamInReport m) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", new Object[]{this.tableName,
                "account,createDate,betAmount,winAmount,totalAmount,test",
                "?,?,?,?,?,?"});
        int id = this.dbSession.updateKeyHolder(sql, new Object[]{
                m.getAccount(),
                m.getCreateDate(),
                m.getBetAmount(),
                m.getWinAmount(),
                m.getTotalAmount(),
                m.getTest()});


        m.setId(id);
        return true;
    }

    public TeamInReport getNewCommonTeamReport(String account, String createTime) {
        StringBuffer buffer = new StringBuffer(" SELECT ? AS account,t.createDate AS createDate,t.test AS test,");
        buffer.append(" sum(IFNULL(t.betAmount, 0)) AS betAmount,");
        buffer.append(" sum(IFNULL(t.winAmount, 0)) AS winAmount,");
        buffer.append(" (sum(IFNULL(t.winAmount, 0)) -sum(IFNULL(t.betAmount, 0))) AS totalAmount ");
        buffer.append(" FROM  t_user_in_report t ");
        buffer.append(" WHERE t.account in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user WHERE account = ?),'%'))\t");
        buffer.append(" AND date_sub(t.createDate, INTERVAL - 1 DAY) = str_to_date(?, '%Y-%m-%d') ");
        buffer.append(" GROUP BY createDate,test ");
        String sql = buffer.toString();
        Object[] argsObjects = null;
        argsObjects = new Object[]{account, account, createTime};
        return this.dbSession.getObject(sql, argsObjects, this.cls);
    }

    public List<TeamInReport> adminNewHistoryStatistics(Page page, TeamInReport m, String startTime, String endTime) {
        List<Object> args = new ArrayList();
        StringBuffer sb = new StringBuffer();
        sb.append(" select account,createDate,betAmount,winAmount,totalAmount,test ");
        sb.append(" from t_team_in_report where 1=1");
        if ((m.getTest().intValue() == 0) || (1 == m.getTest().intValue())) {
            sb.append(" and test=? ");
            args.add(m.getTest());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
            sb.append(" and account=? ");
            args.add(m.getAccount());
        }
        if (!StrUtils.hasEmpty(new Object[]{startTime, endTime})) {
            sb.append(" and createDate >= ? and createDate <=?");
            args.add(startTime);
            args.add(endTime);
        } else if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            sb.append(" and createDate >= ?");
            args.add(startTime);
        } else if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            sb.append(" and createDate <= ?");
            args.add(endTime);
        }
        sb.append(" order by createDate desc");
        String sql = sb.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, page);
    }

    public List<TeamInReport> newHistoryStatistics(Page page, TeamInReport m, String startTime, String endTime) {
        List<Object> args = new ArrayList();
        StringBuffer sb = new StringBuffer();
        sb.append(" select account,createDate,betAmount, winAmount,totalAmount,test ");
        sb.append(" from t_team_in_report where 1=1");
        if ((m.getTest().intValue() == 0) || (1 == m.getTest().intValue())) {
            sb.append(" and test=? ");
            args.add(m.getTest());
        } else {
            sb.append(" and account=? ");
            args.add(m.getAccount());
        }
        if (!StrUtils.hasEmpty(new Object[]{startTime, endTime})) {
            sb.append(" and createDate >=? and createDate <= ?");
            args.add(startTime);
            args.add(endTime);
        } else if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            sb.append(" and createDate >= ?");
            args.add(startTime);
        } else if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            sb.append(" and createDate <= ?");
            args.add(endTime);
        }
        sb.append(" order by createDate desc");
        String sql = sb.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, page);
    }

    public List<TeamInReport> adminNewHistoryDetails(Page page, TeamInReport m, String startTime, String endTime) {
        List<Object> args = new ArrayList();
        StringBuffer sb = new StringBuffer();
        sb.append(" select account,sum(IFNULL(betAmount,0)) as betAmount,");
        sb.append(" sum(IFNULL(winAmount,0)) as winAmount,");
        sb.append(" sum(IFNULL(totalAmount,0)) as totalAmount,");
        sb.append(" test from t_team_in_report  where account =?");
        args.add(m.getAccount());
        if ((m.getTest().intValue() == 0) || (1 == m.getTest().intValue())) {
            sb.append(" and test=? ");
            args.add(m.getTest());
        }
        if (!StrUtils.hasEmpty(new Object[]{startTime, endTime})) {
            sb.append(" and createDate >= ? and createDate <= ?");
            args.add(startTime);
            args.add(endTime);
        } else if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            sb.append(" and createDate >= ?");
            args.add(startTime);
        } else if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            sb.append(" and createDate <= ?");
            args.add(endTime);
        }
        sb.append(" GROUP BY account ORDER BY  createDate desc");
        String sql = sb.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, page);
    }

    public List<TeamInReport> newHistoryDetails(Page page, TeamInReport m, String startTime, String endTime) {
        List<Object> args = new ArrayList();
        StringBuffer sb = new StringBuffer();
        sb.append(" select account,sum(IFNULL(betAmount,0)) as betAmount,");
        sb.append(" sum(IFNULL(winAmount,0)) as winAmount,");
        sb.append(" sum(IFNULL(totalAmount,0)) as totalAmount,");
        sb.append(" test from t_team_in_report  where account =?");
        args.add(m.getAccount());
        if ((m.getTest().intValue() == 0) || (1 == m.getTest().intValue())) {
            sb.append(" and test=? ");
            args.add(m.getTest());
        }
        if (!StrUtils.hasEmpty(new Object[]{startTime, endTime})) {
            sb.append(" and createDate >=? and createDate <= ?");
            args.add(startTime);
            args.add(endTime);
        } else if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            sb.append(" and createDate >= ?");
            args.add(startTime);
        } else if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            sb.append(" and createDate <= ?");
            args.add(endTime);
        }
        sb.append(" GROUP BY account union");
        sb.append(" (select account,betAmount,winAmount,totalAmount,test ");
        sb.append(" from (select account,sum(IFNULL(betAmount,0)) as betAmount,");
        sb.append(" sum(IFNULL(winAmount,0)) as winAmount,sum(IFNULL(totalAmount,0)) as totalAmount,test ");
        sb.append(" from t_team_in_report  where account in  (select account from t_user t where  t.parentAccount=?)");
        args.add(m.getAccount());
        if ((m.getTest().intValue() == 0) || (1 == m.getTest().intValue())) {
            sb.append(" and test=? ");
            args.add(m.getTest());
        }
        if (!StrUtils.hasEmpty(new Object[]{startTime, endTime})) {
            sb.append(" and createDate >=? and createDate <= ?");
            args.add(startTime);
            args.add(endTime);
        } else if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            sb.append(" and createDate >= ?");
            args.add(startTime);
        } else if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            sb.append(" and createDate <= ?");
            args.add(endTime);
        }
        sb.append(" GROUP BY account) a ORDER BY  totalAmount)");
        String sql = sb.toString();

        return this.dbSession.list(sql, args.toArray(), this.cls, page);
    }

    public List<TeamInReport> getTodayDataList(TeamInReport m) {
        StringBuffer buffer = new StringBuffer(" SELECT changeUser AS account,");
        buffer.append(" - sum(CASE t.accountChangeTypeId WHEN '91' THEN t.changeAmount ELSE\t0 END) AS betAmount,");
        buffer.append(" sum(CASE t.accountChangeTypeId WHEN '92' THEN\tt.changeAmount ELSE 0 END) AS winAmount ");
        buffer.append(" FROM t_amount_change t ");
        buffer.append(" WHERE t.changeUser IN (SELECT\taccount FROM t_user\tWHERE\tparentList LIKE CONCAT((SELECT\tparentList FROM\tt_user\tWHERE\taccount = ? and test =?),'%')) ");
        buffer.append(" AND t.changeTime >= ? AND t.changeTime <= ? ");
        buffer.append(" GROUP BY account ");
        String sql = buffer.toString();
        Object[] argsObjects = null;
        argsObjects = new Object[]{m.getAccount(), m.getTest(), WebDateUtils.getProfitBeginTime(m.getCreateDate()), m.getCreateDate()};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public int getCountByCreatDate(String createDate) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"count(1)", this.tableName, "WHERE date_sub(createDate,interval -1 day)=str_to_date(?, '%Y-%m-%d')"});
        Object[] args = {createDate};
        return this.dbSession.getInt(sql + " for update", args).intValue();
    }

    public boolean isExist(String account, String createTime) {
        StringBuffer buffer = new StringBuffer("select count(1) from " + this.tableName + " t where account=? and date_sub(createDate,interval -1 day)=str_to_date(?, '%Y-%m-%d') ");
        String sql = buffer.toString();
        Object[] argsObjects = {account, createTime};
        return this.dbSession.getInt(sql, argsObjects).intValue() > 0;
    }

    public List<TeamInReport> getTodayAllDataList(TeamInReport m) {
        StringBuffer buffer = new StringBuffer(" SELECT changeUser AS account,");
        buffer.append(" - sum(CASE t.accountChangeTypeId WHEN '91' THEN t.changeAmount ELSE\t0 END) AS betAmount,");
        buffer.append(" sum(CASE t.accountChangeTypeId WHEN '92' THEN\tt.changeAmount ELSE 0 END) AS winAmount ");
        buffer.append(" FROM t_amount_change t ");
        buffer.append(" WHERE t.changeTime >= ? AND t.changeTime <= ? ");
        buffer.append(" GROUP BY account ");
        String sql = buffer.toString();
        Object[] argsObjects = null;
        argsObjects = new Object[]{WebDateUtils.getProfitBeginTime(m.getCreateDate()), m.getCreateDate()};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public List<TeamInReport> list(Page p, TeamInReport m) {
        StringBuffer buffer = new StringBuffer(" SELECT ? AS account,curdate() AS createDate,");
        buffer.append(" - sum(CASE t.accountChangeTypeId WHEN '91' THEN t.changeAmount ELSE\t0 END) AS betAmount,");
        buffer.append(" sum(CASE t.accountChangeTypeId WHEN '92' THEN\tt.changeAmount ELSE 0 END) AS winAmount, ");
        buffer.append(" (sum(CASE t.accountChangeTypeId WHEN '92' THEN\tt.changeAmount ELSE 0 END)+sum(CASE t.accountChangeTypeId WHEN '91' THEN t.changeAmount ELSE 0 END)) as totalAmount ");
        buffer.append(" FROM t_amount_change t ");
        buffer.append(" WHERE t.changeUser IN (SELECT\taccount FROM t_user\tWHERE\tparentList LIKE CONCAT((SELECT\tparentList FROM\tt_user\tWHERE\taccount = ? and test =?),'%')) ");
        buffer.append(" and t.changeTime >= ? AND t.changeTime <= ? ");
        String sql = buffer.toString();
        Object[] argsObjects = null;
        argsObjects = new Object[]{m.getAccount(), m.getAccount(), m.getTest(), WebDateUtils.getBeginTime(m.getCreateDate()), m.getCreateDate()};
        return this.dbSession.list(sql, argsObjects, this.cls, p);
    }

    public List<TeamInReport> getTodayDataListByTeam(TeamInReport m) {
        StringBuffer buffer = new StringBuffer(" SELECT changeUser AS account,curdate() AS createDate,");
        buffer.append(" - sum(CASE t.accountChangeTypeId WHEN '91' THEN t.changeAmount ELSE\t0 END) AS betAmount,");
        buffer.append(" sum(CASE t.accountChangeTypeId WHEN '92' THEN\tt.changeAmount ELSE 0 END) AS winAmount, ");
        buffer.append(" (sum(CASE t.accountChangeTypeId WHEN '92' THEN\tt.changeAmount ELSE 0 END)+sum(CASE t.accountChangeTypeId WHEN '91' THEN t.changeAmount ELSE 0 END)) as totalAmount ");
        buffer.append(" FROM t_amount_change t ");
        buffer.append(" WHERE t.changeUser IN (SELECT\taccount FROM t_user\tWHERE\tparentList LIKE CONCAT((SELECT\tparentList FROM\tt_user\tWHERE\taccount = ? and test =?),'%')) ");
        buffer.append(" and t.changeTime >= ? AND t.changeTime <= ? ");
        buffer.append(" GROUP BY account ");
        String sql = buffer.toString();
        Object[] argsObjects = null;
        argsObjects = new Object[]{m.getAccount(), m.getTest(), WebDateUtils.getBeginTime(m.getCreateDate()), m.getCreateDate()};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }
}
