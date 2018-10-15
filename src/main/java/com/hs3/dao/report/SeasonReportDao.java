package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.entity.lotts.LotterySeason;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("seasonReportDao")
public class SeasonReportDao
        extends BaseDao<LotterySeason> {
    public List<Map<String, Object>> listHost(String lotteryId, int num, int len) {
        StringBuilder sb = new StringBuilder();
        List<Object> args = new ArrayList();
        sb.append("SELECT B1.n as num,B1.o as o1 ");
        if (num > 1) {
            sb.append(",B2.o as o2 ");
        }
        if (num > 2) {
            sb.append(",B3.o as o3 ");
        }
        if (num > 3) {
            sb.append(",B4.o as o4 ");
        }
        if (num > 4) {
            sb.append(",B5.o as o5 ");
        }
        sb.append("FROM ");
        if (num > 5) {
            num = 5;
        }
        for (int i = 1; i <= num; i++) {
            sb.append("(SELECT ");
            sb.append("\t\tn, COUNT(n) AS o\t");
            sb.append("FROM");
            sb.append("\t\t(SELECT");
            sb.append("\t\t\tn" + i + " as n");
            sb.append("\t\tFROM");
            sb.append("\t\t\tt_lottery_season");
            sb.append("\t\tWHERE");
            sb.append("\t\t\tlotteryId = ?");
            sb.append("\t\tORDER BY seasonId DESC LIMIT ?) AS A\t");
            sb.append("GROUP BY n) AS B" + i);
            if (i != 1) {
                sb.append(" ON B1.n=B" + i + ".n ");
            }
            if (i != num) {
                sb.append(" LEFT JOIN ");
            }
            args.add(lotteryId);
            args.add(Integer.valueOf(len));
        }
        return this.dbSession.listMap(sb.toString(), args.toArray(), null);
    }

    public List<Map<String, Object>> listHostTiger(String account, int num, int len) {
        StringBuilder sb = new StringBuilder();
        List<Object> args = new ArrayList();
        sb.append("SELECT B1.n as num,B1.o as o1 ");
        if (num > 1) {
            sb.append(",B2.o as o2 ");
        }
        if (num > 2) {
            sb.append(",B3.o as o3 ");
        }
        if (num > 3) {
            sb.append(",B4.o as o4 ");
        }
        if (num > 4) {
            sb.append(",B5.o as o5 ");
        }
        sb.append("FROM ");
        if (num > 5) {
            num = 5;
        }
        for (int i = 1; i <= num; i++) {
            sb.append("(SELECT ");
            sb.append("\t\tn, COUNT(n) AS o\t");
            sb.append("FROM");
            sb.append("\t\t(SELECT");
            sb.append("\t\t\tSUBSTRING(openNum, " + (i * 2 - 1) + ", 1) as n");
            sb.append("\t\tFROM");
            sb.append("\t\t\tt_bet_tiger");
            sb.append("\t\tWHERE");
            sb.append("\t\t\taccount = ?");
            sb.append("\t\tORDER BY createTime DESC LIMIT ?) AS A\t");
            sb.append("GROUP BY n) AS B" + i);
            if (i != 1) {
                sb.append(" ON B1.n=B" + i + ".n ");
            }
            if (i != num) {
                sb.append(" LEFT JOIN ");
            }
            args.add(account);
            args.add(Integer.valueOf(len));
        }
        return this.dbSession.listMap(sb.toString(), args.toArray(), null);
    }

    public List<Map<String, Object>> listLost(String lotteryId, String seasonId, int num) {
        StringBuilder sb = new StringBuilder();
        List<Object> args = new ArrayList();
        sb.append("SELECT B1.n as num,B1.o as o1 ");
        if (num > 1) {
            sb.append(",B2.o as o2 ");
        }
        if (num > 2) {
            sb.append(",B3.o as o3 ");
        }
        if (num > 3) {
            sb.append(",B4.o as o4 ");
        }
        if (num > 4) {
            sb.append(",B5.o as o5 ");
        }
        sb.append("FROM ");
        if (num > 5) {
            num = 5;
        }
        for (int i = 1; i <= num; i++) {
            args.add(lotteryId);

            sb.append("(SELECT ");
            sb.append("\t\tn, min(rowNo) AS o\t");
            sb.append("FROM");
            sb.append("\t\t(SELECT");
            sb.append("\t\t\tn" + i + " as n, (@rowNum" + i + " :=@rowNum" + i + " + 1) AS rowNo");
            sb.append("\t\tFROM");
            sb.append("\t\t\tt_lottery_season,(SELECT(@rowNum" + i + " :=- 1)) b");
            sb.append("\t\tWHERE");
            sb.append("\t\t\tlotteryId = ?");
            if (!StrUtils.hasEmpty(new Object[]{seasonId})) {
                sb.append(" AND seasonId<=?");
                args.add(seasonId);
            }
            sb.append("\t\tORDER BY seasonId DESC  LIMIT 500) AS A\t");
            sb.append("GROUP BY n) AS B" + i);
            if (i != 1) {
                sb.append(" ON B1.n=B" + i + ".n ");
            }
            if (i != num) {
                sb.append(" LEFT JOIN ");
            }
        }
        return this.dbSession.listMap(sb.toString(), args.toArray(), null);
    }

    public List<Map<String, Object>> listLostTiger(String account, int num) {
        StringBuilder sb = new StringBuilder();
        List<Object> args = new ArrayList();
        sb.append("SELECT B1.n as num,B1.o as o1 ");
        if (num > 1) {
            sb.append(",B2.o as o2 ");
        }
        if (num > 2) {
            sb.append(",B3.o as o3 ");
        }
        if (num > 3) {
            sb.append(",B4.o as o4 ");
        }
        if (num > 4) {
            sb.append(",B5.o as o5 ");
        }
        sb.append("FROM ");
        if (num > 5) {
            num = 5;
        }
        for (int i = 1; i <= num; i++) {
            args.add(account);

            sb.append("(SELECT ");
            sb.append("\t\tn, min(rowNo) AS o\t");
            sb.append("FROM");
            sb.append("\t\t(SELECT");
            sb.append("\t\t\tSUBSTRING(openNum, " + (i * 2 - 1) + ", 1) as n, (@rowNum" + i + " :=@rowNum" + i + " + 1) AS rowNo");
            sb.append("\t\tFROM");
            sb.append("\t\t\tt_bet_tiger,(SELECT(@rowNum" + i + " :=- 1)) b");
            sb.append("\t\tWHERE");
            sb.append("\t\t\taccount = ?");
            sb.append("\t\tORDER BY createTime DESC  LIMIT 500) AS A\t");
            sb.append("GROUP BY n) AS B" + i);
            if (i != 1) {
                sb.append(" ON B1.n=B" + i + ".n ");
            }
            if (i != num) {
                sb.append(" LEFT JOIN ");
            }
        }
        return this.dbSession.listMap(sb.toString(), args.toArray(), null);
    }
}
