package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.Trace;
import com.hs3.models.lotts.TraceModel;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("TraceDao")
public class TraceDao
        extends BaseDao<Trace> {
    private static final String UPDATE_WINAMOUNT = "UPDATE t_trace SET winAmount=winAmount+? WHERE id=?";

    public void save(Trace trace) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", new Object[]{this.tableName,
                "id,account,lotteryId,createTime,lotteryName,startSeason,traceNum,finishTraceNum,cancelTraceNum,isWinStop,status,traceAmount,finishTraceAmount,cancelTraceAmount,winAmount,test",
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"});
        this.dbSession.update(
                sql,
                new Object[]{trace.getId(), trace.getAccount(), trace.getLotteryId(), trace.getCreateTime(), trace.getLotteryName(),
                        trace.getStartSeason(), trace.getTraceNum(), trace.getFinishTraceNum(), trace.getCancelTraceNum(), trace.getIsWinStop(),
                        trace.getStatus(), trace.getTraceAmount(), trace.getFinishTraceAmount(), trace.getCancelTraceAmount(),
                        trace.getWinAmount(), trace.getTest()});
    }

    public void updateTraceAmount(String traceId, BigDecimal bigDecimal, Integer sign) {
        String sql = null;
        if (sign.intValue() == 0) {
            sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "finishTraceAmount=finishTraceAmount+?", "id=?"});
        } else {
            sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "cancelTraceAmount=cancelTraceAmount+?", "id=?"});
        }
        this.dbSession.update(
                sql,
                new Object[]{bigDecimal, traceId});
    }

    public void updateTraceNum(String traceId, Integer num, Integer sign) {
        String sql = null;
        if (sign.intValue() == 0) {
            sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "finishTraceNum=finishTraceNum+?", "id=?"});
        } else {
            sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "cancelTraceNum=cancelTraceNum+?", "id=?"});
        }
        this.dbSession.update(
                sql,
                new Object[]{num, traceId});
    }

    public List<Trace> list(Page p, TraceModel m) {
        List<Object> args = new ArrayList();
        StringBuffer buffer = new StringBuffer("SELECT * FROM t_trace t where 1=1 ");
        if (!m.isLowerLevel()) {
            buffer.append(" and t.account=?");
            args.add(m.getAccount());
        } else {
            buffer.append(" and t.account in (SELECT account FROM t_user\tWHERE parentList LIKE CONCAT((\tSELECT\tparentList\tFROM t_user WHERE\taccount =?\t),'%')) ");
            args.add(m.getAccount());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStartTime(), m.getEndTime()})) {
            buffer.append(" and t.createTime  between ? and ?");
            args.add(m.getStartTime());
            args.add(m.getEndTime());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getEndTime()})) {
            buffer.append(" and t.createTime <=?");
            args.add(m.getEndTime());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getStartTime()})) {
            buffer.append(" and t.createTime >=?");
            args.add(m.getStartTime());
        }
        if (m.getStatus() != null) {
            buffer.append(" and t.status = ?");
            args.add(m.getStatus());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getLotteryId()})) {
            buffer.append(" and t.lotteryId=?");
            args.add(m.getLotteryId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStartSeason()})) {
            buffer.append(" and t.startSeason=?");
            args.add(m.getStartSeason());
        }
        buffer.append(" order by t.createTime desc,t.startSeason desc");
        String finalSql = buffer.toString();
        return this.dbSession.list(finalSql, args.toArray(), this.cls, p);
    }

    public List<Trace> listByAccount(String account, int count) {
        String sql = new SQLUtils(this.tableName).where("account=?").orderBy("startSeason DESC,createTime DESC LIMIT ?").getSelect();
        return this.dbSession.list(sql, new Object[]{account, Integer.valueOf(count)}, this.cls);
    }

    public List<Trace> adminList(Page p, TraceModel m) {
        List<Object> args = new ArrayList();
        StringBuffer buffer = new StringBuffer("select i.*," + UserDao.getMarkSQL() + " from " + this.tableName + " as i where 1=1 ");
        if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
            if ((m.getInclude() == null) || (m.getInclude().intValue() == 0)) {
                buffer.append(" and account =?");
                args.add(m.getAccount());
            } else if (m.getInclude().intValue() == 1) {
                buffer.append("  and account in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%'))");

                args.add(m.getAccount());
            }
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getLotteryId()})) {
            buffer.append(" and lotteryId =?");
            args.add(m.getLotteryId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStartSeason()})) {
            buffer.append(" and startSeason =?");
            args.add(m.getStartSeason());
        }
        if ((m.getTest() != null) && (m.getTest().intValue() != 2)) {
            buffer.append(" and test =?");
            args.add(m.getTest());
        }
        if ((m.getStatus() != null) && (m.getStatus().intValue() != 2)) {
            buffer.append(" and status =?");
            args.add(m.getStatus());
        }
        if ((m.getIsWinStop() != null) && (m.getIsWinStop().intValue() != 2)) {
            buffer.append(" and isWinStop =?");
            args.add(m.getIsWinStop());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStartTime(), m.getEndTime()})) {
            buffer.append(" and createTime  between ? and ?");
            args.add(m.getStartTime());
            args.add(m.getEndTime());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getEndTime()})) {
            buffer.append(" and createTime <=?");
            args.add(m.getEndTime());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getStartTime()})) {
            buffer.append(" and createTime >=?");
            args.add(m.getStartTime());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getLowerWinAmount(), m.getHighWinAmount()})) {
            buffer.append(" and winAmount between ? and ?");
            args.add(m.getLowerWinAmount());
            args.add(m.getHighWinAmount());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getHighWinAmount()})) {
            buffer.append(" and winAmount <=?");
            args.add(m.getHighWinAmount());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getLowerWinAmount()})) {
            buffer.append(" and winAmount >=?");
            args.add(m.getLowerWinAmount());
        }
        buffer.append(" order by createTime desc,startSeason desc");
        String sql = buffer.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, p);
    }

    public void updateStatus(String traceId, Integer status, Integer repStatus) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "status=?", "id=? and status=?"});
        this.dbSession.update(
                sql,
                new Object[]{status, traceId, repStatus});
    }

    public void updateStatusEx(String traceId) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "status=1", "id=? and finishTraceNum+cancelTraceNum=traceNum"});
        this.dbSession.update(
                sql,
                new Object[]{traceId});
    }

    public void updateWinAmount(String traceId, BigDecimal winAmount) {
        Object[] args = {winAmount, traceId};
        this.dbSession.update("UPDATE t_trace SET winAmount=winAmount+? WHERE id=?", args);
    }

    public int deleteByClear(String id, boolean needBackup) {
        Object[] cond = {id};
        if (needBackup) {
            this.dbSession.update("INSERT INTO backup_t_trace SELECT * FROM t_trace WHERE id = ?", cond);
        }
        return this.dbSession.update("DELETE FROM t_trace WHERE id = ?", cond);
    }

    public List<Trace> listClearTrace(Date createTime, int start, int size) {
        return this.dbSession.list("SELECT * FROM t_trace t WHERE t.createTime < ? AND t.status = 1 ORDER BY id LIMIT ?,?", new Object[]{createTime, Integer.valueOf(start), Integer.valueOf(size)}, this.cls);
    }

    public int countClearTrace(Date createTime) {
        return this.dbSession.getInt("SELECT count(1) FROM t_trace t WHERE t.createTime < ? AND t.status = 1", new Object[]{createTime}).intValue();
    }
}
