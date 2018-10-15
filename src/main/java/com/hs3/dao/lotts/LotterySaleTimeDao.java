package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.LotterySaleTime;
import com.hs3.models.lotts.SeasonCount;
import com.hs3.models.lotts.TraceSeasonId;
import com.hs3.utils.DateUtils;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("lotterySaleTimeDao")
public class LotterySaleTimeDao
        extends BaseDao<LotterySaleTime> {
    public void save(LotterySaleTime sale) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", this.tableName, "seasonId,lotteryId,beginTime,endTime,openStatus,settleStatus,planStatus,curDate,openTime,openAfterTime", "?,?,?,?,?,?,?,?,?,?");
        this.dbSession.update(sql, new Object[]{sale.getSeasonId(), sale.getLotteryId(), sale.getBeginTime(), sale.getEndTime(), sale.getOpenStatus(), sale.getSettleStatus(), sale.getPlanStatus(),
                sale.getCurDate(), sale.getOpenTime(), sale.getOpenAfterTime()});
    }

    public boolean updateOpenStatus(String seasonId, String lotteryId, Integer openStatus) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", this.tableName, "openStatus=?", "seasonId=? AND lotteryId=?");
        return 1 == this.dbSession.update(sql, new Object[]{openStatus, seasonId, lotteryId});
    }

    public boolean updateSettleStatus(String seasonId, String lotteryId, Integer PreSettleStatus, Integer settleStatus) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", this.tableName, "settleStatus=?", "seasonId=? AND lotteryId=?");
        Object[] args = null;
        if (PreSettleStatus != null) {
            sql = sql + " AND settleStatus=?";
            args = new Object[]{settleStatus, seasonId, lotteryId, PreSettleStatus};
        } else {
            args = new Object[]{settleStatus, seasonId, lotteryId};
        }
        return 1 == this.dbSession.update(sql, args);
    }

    public boolean updatePlanStatus(String seasonId, String lotteryId, Integer prePlanStatus, Integer planStatus) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", this.tableName, "planStatus=?", "seasonId=? AND lotteryId=?");

        Object[] args = null;
        if (prePlanStatus != null) {
            sql = sql + " AND planStatus=?";
            args = new Object[]{planStatus, seasonId, lotteryId, prePlanStatus};
        } else {
            args = new Object[]{planStatus, seasonId, lotteryId};
        }
        return 1 == this.dbSession.update(sql, args);
    }

    public List<LotterySaleTime> list(String lotteryId, int day) {
        Date d = DateUtils.getToDay(day);
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE lotteryId=? AND curDate=?");
        return this.dbSession.list(sql, new Object[]{lotteryId, d}, this.cls);
    }

    public LotterySaleTime getCurrentByLotteryId(String lotteryId) {
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE lotteryId=? AND endTime>? ORDER BY endTime ASC LIMIT 1");
        return this.dbSession.getObject(sql, new Object[]{lotteryId, new Date()}, this.cls);
    }

    public LotterySaleTime getByLotteryIdAndSeasonId(String lotteryId, String seasonId) {
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE lotteryId=? AND seasonId=?");
        return this.dbSession.getObject(sql, new Object[]{lotteryId, seasonId}, this.cls);
    }

    public LotterySaleTime find(String seasonId, String lotteryId) {
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE seasonId=? AND lotteryId=?");
        return this.dbSession.getObject(sql, new Object[]{seasonId, lotteryId}, this.cls);
    }

    public LotterySaleTime findMaster(String seasonId, String lotteryId) {
        String sql = "/*master*/" + String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE seasonId=? AND lotteryId=?");
        return this.dbSession.getObject(sql, new Object[]{seasonId, lotteryId}, this.cls);
    }

    public void delete(Date begin, Date end, String lotteryid) {
        Object[] args = {begin, end, lotteryid};
        String sql = "delete from t_lottery_sale_time where curDate between ? and ? and lotteryId=?";
        this.dbSession.update(sql, args);
    }

    public void deleteByStartDate(Date begin, String lotteryid) {
        Object[] args = {begin, lotteryid};
        String sql = "delete from t_lottery_sale_time where curDate >=? and lotteryId=?";
        this.dbSession.update(sql, args);
    }

    public void deleteByStartDateTime(Date begin, String lotteryid) {
        Object[] args = {begin, lotteryid};
        String sql = "delete from t_lottery_sale_time where openTime >? and lotteryId=?";
        this.dbSession.update(sql, args);
    }

    public void deleteByDate(Date begin, String lotteryid) {
        Object[] args = {begin, lotteryid};
        String sql = "delete from t_lottery_sale_time where curDate <? and lotteryId=?";
        this.dbSession.update(sql, args);
    }

    public String maxDaySeason(String lotteryId) {
        String sql = "select MAX(curDate) from t_lottery_sale_time where lotteryId=?";
        return this.dbSession.getString(sql, new Object[]{lotteryId});
    }

    public String minDaySeason(String lotteryId, Date date) {
        String sql = "select MIN(seasonId) from t_lottery_sale_time where lotteryId=? AND openTime>?";
        return this.dbSession.getString(sql, new Object[]{lotteryId, date});
    }

    public int findMaxDayCount(String lotteryId, Date date) {
        String sql = "select count(DISTINCT curDate) from t_lottery_sale_time where lotteryId=? and curDate>=?";
        Integer count = this.dbSession.getInt(sql, new Object[]{lotteryId, date});
        if (count == null) {
            count = 0;
        }
        return count;
    }

    public List<LotterySaleTime> list(String lotteryId, Date startTime, Date endTime, Page p) {
        String sql = "/*master*/select * from t_lottery_sale_time where lotteryId=? and curDate>=? and curDate<=?";
        return this.dbSession.list(sql, new Object[]{lotteryId, startTime, endTime}, this.cls, p);
    }

    public SeasonCount getSeasonCountByLotteryId(String lotteryId, Date d) {
        String sql = "SELECT count(1) as allCount,sum(case when endTime<=? then 1 else 0 end) as openCount FROM t_lottery_sale_time WHERE curDate=? AND lotteryId=?";
        return this.dbSession.getObject(sql, new Object[]{new Date(), d, lotteryId}, SeasonCount.class);
    }

    public LotterySaleTime getPreviousSeasonByLotteryId(String lotteryId, Date d) {
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE endTime>? AND  beginTime<=? AND lotteryId=? LIMIT 1");
        return this.dbSession.getObject(sql, new Object[]{d, d, lotteryId}, this.cls);
    }

    public LotterySaleTime getPreviousByLotteryId(String lotteryId, Date d) {
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "where lotteryId=? and endTime<=? ORDER BY endTime DESC LIMIT 1");
        return this.dbSession.getObject(sql, new Object[]{lotteryId, d}, this.cls);
    }

    public LotterySaleTime getNextByLotteryId(String lotteryId, String seasonId, Date openTime) {
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "where lotteryId=? and seasonId>? AND openTime>? ORDER BY openTime ASC LIMIT 1");
        return this.dbSession.getObject(sql, new Object[]{lotteryId, seasonId, openTime}, this.cls);
    }

    public LotterySaleTime getCurrentSettleLotteryId(String lotteryId, Date d) {
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE openTime>? AND lotteryId=? order by endTime ASC LIMIT 1");
        return this.dbSession.getObject(sql, new Object[]{d, lotteryId}, this.cls);
    }

    public List<TraceSeasonId> listTraceSeasonId(String lotteryId, Integer count) {
        String sql = new SQLUtils(this.tableName).field("seasonId,openTime").where("lotteryId=? AND endTime>?").orderBy("endTime ASC LIMIT ?").getSelect();
        return this.dbSession.list(sql, new Object[]{lotteryId, new Date(), count}, TraceSeasonId.class);
    }

    public List<String> listSeasonId(String lotteryId, Integer count) {
        String sql = new SQLUtils(this.tableName).field("seasonId").where("lotteryId=? AND endTime<=?").orderBy("endTime DESC LIMIT ?").getSelect();
        return this.dbSession.listSerializable(sql, new Object[]{lotteryId, new Date(), count}, String.class);
    }

    public List<LotterySaleTime> listDuringSeason(String lotteryId, String seasonBegin, String seasonEnd) {
        String sql = "select seasonId,lotteryId,beginTime,endTime,openStatus,curDate,openTime,openAfterTime from t_lottery_sale_time where lotteryId=? and seasonId>=? and seasonId<=? ORDER BY seasonId";
        return this.dbSession.list(sql, new Object[]{lotteryId, seasonBegin, seasonEnd}, this.cls);
    }

    public int deleteByLotteryId(String id) {
        String sql = "DELETE FROM " + this.tableName + " WHERE lotteryId=?";
        return this.dbSession.update(sql, new Object[]{id});
    }

    public List<LotterySaleTime> listByOpenTime(String lotteryId, String seasonId) {
        String sql = "select seasonId,lotteryId,beginTime,endTime,openStatus,curDate,openTime,openAfterTime from t_lottery_sale_time where lotteryId=? and seasonId>=? ORDER BY seasonId";
        return this.dbSession.list(sql, new Object[]{lotteryId, seasonId}, this.cls);
    }

    public boolean updateTime(Date beginTime, Date endTime, Date openTime, Date afterTime, String lotteryId, String seasonId) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", this.tableName, "beginTime=?,endTime=?,openTime=?,openAfterTime=?,openStatus=0,settleStatus=0,planStatus=0", "seasonId=? AND lotteryId=?");
        return 1 == this.dbSession.update(sql, new Object[]{beginTime, endTime, openTime, afterTime, seasonId, lotteryId});
    }

    public LotterySaleTime getBySeasonId(String lotteryId) {
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE seasonId=?");
        return this.dbSession.getObject(sql, new Object[]{lotteryId}, this.cls);
    }

    public List<LotterySaleTime> listException(Date begin, Date end) {
        String sql = "SELECT DISTINCT(lotteryId) as lotteryId FROM t_lottery_sale_time WHERE openAfterTime>=? and openAfterTime<=? and ( openStatus in (0,1,3,5) OR settleStatus in (0,1) OR planStatus in(0,1))";
        return this.dbSession.list(sql, new Object[]{begin, end}, this.cls);
    }
}
