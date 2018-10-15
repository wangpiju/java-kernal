package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.models.report.SaleReport;
import com.hs3.models.report.SaleSeasonReport;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("saleReportDao")
public class SaleReportDao
        extends BaseDao<SaleReport> {
    public List<SaleReport> getReport(Date begin, Date end, String lotteryId, Integer test) {
        StringBuilder sb = new StringBuilder();
        List<Object> args = new ArrayList();
        args.add(begin);
        args.add(end);
        args.add(test);

        sb.append("SELECT ");
        sb.append("\tlotteryId,");
        sb.append("\tDATE_FORMAT(SUBTIME(createTime,'00:00:00'),'%Y-%m-%d') as date,");
        sb.append("\tSUM(IF(status=1 OR status=2,amount,0)) as amount,");
        sb.append("\tSUM(ifnull(win,0)) as win,");
        sb.append("\tcount(1) as betNum,");
        sb.append("\tcount(DISTINCT account) as userNum ");
        sb.append("FROM ");
        sb.append("\tt_bet ");
        sb.append("WHERE ");
        sb.append("\tcreateTime>=? AND createTime<=? AND test=? ");
        if (!StrUtils.hasEmpty(new Object[]{lotteryId})) {
            sb.append(" AND lotteryId=? ");
            args.add(lotteryId);
        }
        sb.append("GROUP BY ");
        sb.append(" lotteryId,date ");
        sb.append("ORDER BY ");
        sb.append(" lotteryId,date DESC");
        String sql = sb.toString();
        List<SaleReport> list = this.dbSession.list(sql, args.toArray(), this.cls);
        for (SaleReport sale : list) {
            sale.setWinLose(sale.getAmount().subtract(sale.getWin()));
        }
        return list;
    }

    public List<SaleSeasonReport> getSeasonReport(Date begin, Date end, String lotteryId, Integer test) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        sb.append("\tlotteryId,seasonId,");
        sb.append("\tSUM(IF(status=1 OR status=2,amount,0)) as amount,");
        sb.append("\tSUM(ifnull(win,0)) as win,");
        sb.append("\tcount(1) as betNum,");
        sb.append("\tcount(DISTINCT account) as userNum\t");
        sb.append("FROM ");
        sb.append("\tt_bet ");
        sb.append("WHERE ");
        sb.append("\tcreateTime>=? AND createTime<=? AND lotteryId=? AND test=? ");
        sb.append("GROUP BY ");
        sb.append(" seasonId ");
        sb.append("ORDER BY ");
        sb.append(" seasonId DESC");
        String sql = sb.toString();
        List<SaleSeasonReport> list = this.dbSession.list(sql, new Object[]{begin, end, lotteryId, test}, SaleSeasonReport.class);
        for (SaleSeasonReport sale : list) {
            sale.setWinLose(sale.getAmount().subtract(sale.getWin()));
        }
        return list;
    }
}
