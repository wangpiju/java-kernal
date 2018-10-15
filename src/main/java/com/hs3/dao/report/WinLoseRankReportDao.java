package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.models.report.WinLoseRank;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("winLoseRankReportDao")
public class WinLoseRankReportDao
        extends BaseDao<WinLoseRank> {
    public List<WinLoseRank> list(Page page, String account, BigDecimal count, Integer order, Integer test, Date startDate, Date endDate) {
        List<Object> obList = new ArrayList();
        StringBuffer buffer = new StringBuffer("select *," + UserDao.getMarkSQL("alltable.account") + " from (");
        buffer.append(" SELECT o.account,o.test,o.rebateAmount,o.betAmount,o.actualSaleAmount,o.winAmount,o.count as count,IFNULL(p.activityAndSend,0) as activityAndSend ");
        buffer.append(",o.winAmount/o.actualSaleAmount as winRatio");
        buffer.append(" FROM ");
        buffer.append(" (SELECT ");
        if ((account != null) && (!"".equals(account))) {
            buffer.append(" IFNULL(account, ?) AS account,SUM(IF(accountChangeName = 2, amount, 0)) AS rebateAmount,");
            obList.add(account);
        } else {
            buffer.append(" account,SUM(IF(accountChangeName = 2, amount, 0)) AS rebateAmount,");
        }
        buffer.append(" - SUM(IF(accountChangeName = 1, amount, 0)) AS betAmount,");
        buffer.append(" (- SUM(IF(accountChangeName = 1, amount, 0))) - SUM(IF(accountChangeName = 2, amount, 0)) AS actualSaleAmount,");
        buffer.append(" SUM(IF(accountChangeName = 4, amount, 0)) AS winAmount,");
        buffer.append(" SUM(IF(accountChangeName = 4, amount, 0)) - ((- SUM(IF(accountChangeName = 1, amount, 0))) - SUM(IF(accountChangeName = 2, amount, 0))) AS count,test FROM ");
        buffer.append(" (SELECT t.changeUser AS account,t.accountChangeTypeId AS accountChangeName,SUM(t.changeAmount) AS amount ,test");
        buffer.append(" FROM ");
        buffer.append(" (SELECT changeUser,accountChangeTypeId,changeAmount,test FROM t_amount_change c WHERE ");
        buffer.append(" betId IN (SELECT id FROM t_bet WHERE STATUS IN (1, 2) ");
        buffer.append(" AND id = c.betId ");
        buffer.append(" AND createTime BETWEEN ? AND ? ");
        obList.add(startDate);
        obList.add(endDate);
        buffer.append(" AND account = c.changeUser");
        if ((account != null) && (!"".equals(account))) {
            buffer.append(" AND account=?");
            obList.add(account);
        }
        buffer.append(" )) t");
        buffer.append(" GROUP BY account,accountChangeName) AS A GROUP BY account,test ) o");
        buffer.append(" LEFT JOIN ");
        if ((account != null) && (!"".equals(account))) {
            buffer.append(" (SELECT IFNULL(account, ?) AS account,curdate() AS createDate,SUM(IF(accountChangeName = 19, amount, 0)) AS activityAndSend ");
            obList.add(account);
        } else {
            buffer.append(" (SELECT account,curdate() AS createDate,SUM(IF(accountChangeName = 19, amount, 0)) AS activityAndSend ");
        }
        buffer.append(" FROM ");
        buffer.append(" (SELECT q.changeUser AS account,q.accountChangeTypeId AS accountChangeName,SUM(q.changeAmount) AS amount ");
        buffer.append(" FROM t_finance_change q ");
        buffer.append(" WHERE q.changeTime BETWEEN ? AND ? ");
        obList.add(startDate);
        obList.add(endDate);
        if ((account != null) && (!"".equals(account))) {
            buffer.append(" AND q.changeUser = ? ");
            obList.add(account);
        }
        buffer.append(" GROUP BY account,accountChangeName ) AS A GROUP BY account) p ");
        buffer.append(" ON p.account = o.account");
        buffer.append(") alltable");
        buffer.append(" WHERE test=?");
        obList.add(test);
        if (count != null) {
            buffer.append(" AND ABS(count)>? ");
            obList.add(count);
        }
        if ((!StrUtils.hasEmpty(new Object[]{order})) && (order.intValue() == 0)) {
            buffer.append(" ORDER BY count DESC");
        } else {
            buffer.append(" ORDER BY count ASC");
        }
        String sql = buffer.toString();
        Object[] argsObjects = obList.toArray();
        return this.dbSession.list(sql, argsObjects, this.cls, page);
    }

    public List<WinLoseRank> historyList(Page page, String account, BigDecimal count, Integer order, Date startTime, Date endTime, Integer test) {
        List<Object> obList = new ArrayList();
        StringBuilder buffer = new StringBuilder(" select account,");
        buffer.append("\tSUM(rebateAmount) as rebateAmount,");
        buffer.append("\tSUM(betAmount) as betAmount,");
        buffer.append("\tSUM(actualSaleAmount) as actualSaleAmount,");
        buffer.append("\tSUM(winAmount) as winAmount,SUM(count) as count,");
        buffer.append("\tIFNULL(activityAndSend,0) as activityAndSend, ");
        buffer.append("\tSUM(winAmount)/SUM(actualSaleAmount) as winRatio ");
        buffer.append("from t_user_report where");
        buffer.append(" createDate between ? and ?");
        obList.add(startTime);
        obList.add(endTime);
        buffer.append(" and test=?");
        obList.add(test);
        if (count != null) {
            buffer.append(" and ABS(count)>? ");
            obList.add(count);
        }
        if (StrUtils.hasEmpty(new Object[]{account})) {
            buffer.append(" GROUP BY account,test");
        } else {
            buffer.append(" and account=? ");
            obList.add(account);
        }
        if ((!StrUtils.hasEmpty(new Object[]{order})) && (order.intValue() == 0)) {
            buffer.append(" ORDER BY count DESC");
        } else {
            buffer.append(" ORDER BY count ASC");
        }
        String sql = buffer.toString();
        sql = "SELECT *," + UserDao.getMarkSQL() + " FROM (" + sql + ") as i";
        Object[] args = obList.toArray();
        return this.dbSession.list(sql, args, this.cls, page);
    }
}
