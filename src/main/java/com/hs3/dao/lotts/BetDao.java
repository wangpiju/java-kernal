package com.hs3.dao.lotts;

import com.hs3.commons.RSTestCons;
import com.hs3.dao.BaseDao;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.Bet;
import com.hs3.entity.report.BetReport;
import com.hs3.entity.report.TeamReport;
import com.hs3.models.CommonModel;
import com.hs3.utils.StrUtils;
import com.hs3.utils.sys.WebDateUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("betDao")
public class BetDao
        extends BaseDao<Bet> {
    private static final String SELECT_FIELD = "id,lotteryId,playerId,seasonId,account,lotteryName,playName,price,unit,amount,status,bonusType,betCount,createTime,lastTime,openNum,groupName,win,bonusRate,traceId,isTrace,test,theoreticalBonus,traceWinStop,CASE WHEN LENGTH(content)>1500 THEN CONCAT(LEFT(content,1500),'...') ELSE content END  as content";
    private static final String SELECT_BET_CHANGE = "select t1.* from t_bet t1 where t1.account in (select t2.account from t_bet_change t2 where t2.lotteryId = ? and t2.playerId = ? and t2.status = 0) and t1.lotteryId = ? and t1.seasonId = ? and t1.playerId = ? and t1.status in";

    public void save(Bet bet) {
        String sql = new SQLUtils(this.tableName).field("id,lotteryId,playerId,seasonId,account,lotteryName,playName,content,price,unit,amount,status,bonusType")
                .field("betCount,hashCode,createTime,groupName,bonusRate,win,lastTime,openNum,traceId,isTrace,test,theoreticalBonus,traceWinStop").getInsert();

        this.dbSession.update(sql,
                new Object[]{bet.getId(), bet.getLotteryId(), bet.getPlayerId(), bet.getSeasonId(), bet.getAccount(), bet.getLotteryName(), bet.getPlayName(), bet.getContent(), bet.getPrice(),
                        bet.getUnit(), bet.getAmount(), bet.getStatus(), bet.getBonusType(), bet.getBetCount(), bet.getHashCode(), bet.getCreateTime(), bet.getGroupName(), bet.getBonusRate(),
                        bet.getWin(), bet.getLastTime(), bet.getOpenNum(), bet.getTraceId(), bet.getIsTrace(), bet.getTest(), bet.getTheoreticalBonus(), bet.getTraceWinStop()});
    }

    public List<Bet> getAllBet(String seasonId, Integer status, String lotteryId) {
        List<Object> argsObjects = new ArrayList<>();
        SQLUtils su = new SQLUtils(this.tableName).where("seasonId=? AND lotteryId=?");
        argsObjects.add(seasonId);
        argsObjects.add(lotteryId);
        if (status != null) {
            su.whereAnd("status=?");
            argsObjects.add(status);
        }
        String sql = su.getSelect();
        return this.dbSession.list(sql, argsObjects.toArray(), this.cls);
    }

    public List<Bet> findBetChange(String lotteryId, String seasonId, String playerId, Integer[] statusArray) {
        List<Object> cond = new ArrayList<>();
        cond.add(lotteryId);
        cond.add(playerId);
        cond.add(lotteryId);
        cond.add(seasonId);
        cond.add(playerId);

        String inSql = "(";
        for (Integer status : statusArray) {
            inSql = inSql + "?,";
            cond.add(status);
        }
        inSql = inSql + "'')";

        return this.dbSession.list("select t1.* from t_bet t1 where t1.account in (select t2.account from t_bet_change t2 where t2.lotteryId = ? and t2.playerId = ? and t2.status = 0) and t1.lotteryId = ? and t1.seasonId = ? and t1.playerId = ? and t1.status in" + inSql, cond.toArray(new Object[cond.size()]), this.cls);
    }

    public List<Bet> getAllBetByUser(String seasonId, Integer status, String lotteryId, String account) {
        String sql = "";
        Object[] argsObjects = null;
        if (status == null) {
            sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE seasonId=? AND lotteryId=? AND account=?"});
            argsObjects = new Object[]{seasonId, lotteryId, account};
        } else {
            sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE seasonId=? and status=? AND lotteryId=? AND account=?"});
            argsObjects = new Object[]{seasonId, status, lotteryId, account};
        }
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public List<Bet> getAllBet(String seasonId, String lotteryId, List<Integer> statusList) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE seasonId=? AND lotteryId=? AND status "});
        List<Object> cond = new ArrayList<>();
        cond.add(seasonId);
        cond.add(lotteryId);
        if (statusList.size() == 1) {
            sql = sql + "=?";
            cond.add(statusList.get(0));
        } else {
            sql = sql + "in(?";
            cond.add(statusList.get(0));
            for (int i = 1; i < statusList.size(); i++) {
                sql = sql + ",?";
                cond.add(statusList.get(i));
            }
            sql = sql + ")";
        }
        return this.dbSession.list(sql, cond.toArray(), this.cls);
    }

    public List<Bet> getAllDuringBet(String seasonIdBegin, String seasonIdEnd, String lotteryId, String traceId, Integer status) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE 1=1"});
        List<Object> cond = new ArrayList<>();
        if (!StrUtils.hasEmpty(new Object[]{seasonIdBegin})) {
            sql = sql + " AND seasonId > ?";
            cond.add(seasonIdBegin);
        }
        if (!StrUtils.hasEmpty(new Object[]{seasonIdEnd})) {
            sql = sql + " AND seasonId <= ?";
            cond.add(seasonIdEnd);
        }
        if (!StrUtils.hasEmpty(new Object[]{lotteryId})) {
            sql = sql + " AND lotteryId = ?";
            cond.add(lotteryId);
        }
        if (!StrUtils.hasEmpty(new Object[]{traceId})) {
            sql = sql + " AND traceId = ?";
            cond.add(traceId);
        }
        if (status != null) {
            sql = sql + " AND status = ?";
            cond.add(status);
        }
        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls);
    }

    public List<Bet> getBetByUser(String seasonId, String lotteryId, Integer[] status, String account) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE lotteryId=? AND seasonId=? AND account=? AND status in (" + getQuestionNumber(status.length) + ")"});
        List<Object> args = new ArrayList<>();
        args.add(lotteryId);
        args.add(seasonId);
        args.add(account);
        Integer[] arrayOfInteger;
        int j = (arrayOfInteger = status).length;
        for (int i = 0; i < j; i++) {
            int s = arrayOfInteger[i];
            args.add(s);
        }
        return this.dbSession.list(sql, args.toArray(), this.cls);
    }

    public List<Bet> getBetByStatus(String seasonId, List<Integer> statusList, String lotteryId, Integer isTest, Page page) {
        List<Object> args = new ArrayList<>();
        args.add(seasonId);
        args.add(lotteryId);
        String sql = null;
        if (statusList.size() == 0) {
            sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE seasonId=? AND lotteryId=? "});
        } else {
            sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE seasonId=? AND lotteryId=? AND status in (" + getQuestionNumber(statusList.size()) + ")"});
            args.addAll(statusList);
        }
        if (isTest != null) {
            if (!RSTestCons.useTest) {
                sql += " AND test = ?";
                args.add(isTest);
            }
        }
        return this.dbSession.list(sql, args.toArray(), this.cls, page);
    }

    public List<Bet> getAllCancelBet(String seasonId, String lotteryId) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE seasonId=?  AND lotteryId=? AND status!=4 AND status!=5 AND status!=9"});
        return this.dbSession.list(sql, new Object[]{seasonId, lotteryId}, this.cls);
    }

    public List<Bet> getAllTraceCancelBet(String TraceId) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE traceId=? AND status=3"});
        return this.dbSession.list(sql, new Object[]{TraceId}, this.cls);
    }

    public List<Bet> getAllTraceCanCancel(String TraceId) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE traceId=? AND (status=3 OR status=0)"});
        return this.dbSession.list(sql, new Object[]{TraceId}, this.cls);
    }

    public List<Bet> getAllIsTraceBet(String lotteryId, String seasonId, Integer isTrace) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE isTrace=? AND seasonId=?  AND lotteryId =?"});
        return this.dbSession.list(sql, new Object[]{isTrace, seasonId, lotteryId}, this.cls);
    }

    public Bet getAllIsTraceBetEx(String lotteryId, String seasonId, String traceId, Integer status) {
        String sql = new SQLUtils(this.tableName).where("traceId=? AND seasonId=?  AND lotteryId =? AND status=?").getSelect();
        return (Bet) this.dbSession.getObject(sql, new Object[]{traceId, seasonId, lotteryId, status}, this.cls);
    }

    public List<String> getDistinctUser(String lotteryId, String seasonId, Integer[] status) {
        String sql = "/*master*/" + new SQLUtils(this.tableName).field("DISTINCT account").where("lotteryId=? AND seasonId=?").whereInAnd("status IN(?)", status.length).getSelect();
        List<Object> args = new ArrayList<>(status.length + 2);
        args.add(lotteryId);
        args.add(seasonId);
        for (Integer s : status) {
            args.add(s);
        }
        return this.dbSession.listSerializable(sql, args.toArray(), String.class);
    }

    public List<String> getIds(String lotteryId, String seasonId, int status) {
        String sql = "SELECT id FROM t_bet WHERE lotteryId=? AND seasonId=? AND status=?";
        return this.dbSession.listSerializable(sql, new Object[]{lotteryId, seasonId, status}, String.class);
    }

    public int updateStatus(String id, int oldStatus, int status) {
        String sql = "UPDATE t_bet SET status=? WHERE id=? AND status=?";
        return this.dbSession.update(sql, new Object[]{status, id, oldStatus});
    }

    public int update(Bet bet, Date preTime) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "lastTime=?,status=?,openNum=?,win=?", "id=? AND lastTime=?"});
        return this.dbSession.update(sql, new Object[]{bet.getLastTime(), bet.getStatus(), bet.getOpenNum(), bet.getWin(), bet.getId(), preTime});
    }

    public int updateContent(String id, Date preTime, String content, String hashCode) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "content=?,hashCode=?", "id=? AND lastTime=?"});
        return this.dbSession.update(sql, new Object[]{content, hashCode, id, preTime});
    }

    public List<Bet> list(Page p, CommonModel m) {
        List<Object> args = new ArrayList<>();


        String sql3 = "SELECT id,lotteryId,playerId,seasonId,account,lotteryName,playName,price,unit,amount,status,bonusType,betCount,createTime,lastTime,openNum,groupName,win,bonusRate,traceId,isTrace,test,theoreticalBonus,traceWinStop,CASE WHEN LENGTH(content)>1500 THEN CONCAT(LEFT(content,1500),'...') ELSE content END  as content FROM t_bet t WHERE t. STATUS != '3'";


        StringBuffer buffer = new StringBuffer(sql3);
        if (!StrUtils.hasEmpty(new Object[]{m.getBetId()})) {
            buffer.append(" and t.id=?");
            args.add(m.getBetId());
        }
        if (m.getStatus() != null) {
            int status = m.getStatus();
            if (status == 0) {
                buffer.append(" and t.status in(0,6)");
            } else if (status == 10) {
                buffer.append(" and t.status in(4,5,9)");
            } else {
                buffer.append(" and t.status = " + status);
            }
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getSeasonId()})) {
            buffer.append(" and t.seasonId=?");
            args.add(m.getSeasonId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getLotteryId()})) {
            buffer.append(" and t.lotteryId=?");
            args.add(m.getLotteryId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getPlayerId()})) {
            buffer.append(" and t.playerId=?");
            args.add(m.getPlayerId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getGroupName()})) {
            buffer.append(" and t.groupName=?");
            args.add(m.getGroupName());
        }
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
        if (!StrUtils.hasEmpty(new Object[]{m.getLowerAmount(), m.getHighAmount()})) {
            buffer.append(" and win between ? and ?");
            args.add(m.getLowerAmount());
            args.add(m.getHighAmount());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getHighAmount()})) {
            buffer.append(" and win <=?");
            args.add(m.getHighAmount());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getLowerAmount()})) {
            buffer.append(" and win >=?");
            args.add(m.getLowerAmount());
        }
        buffer.append(" order by t.createTime desc,t.seasonId desc");
        String sql = buffer.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, p);
    }

    public List<Bet> findByTraceId(String traceId) {
        String sql = new SQLUtils(this.tableName).field("id,lotteryId,playerId,seasonId,account,lotteryName,playName,price,unit,amount,status,bonusType,betCount,createTime,lastTime,openNum,groupName,win,bonusRate,traceId,isTrace,test,theoreticalBonus,traceWinStop,CASE WHEN LENGTH(content)>1500 THEN CONCAT(LEFT(content,1500),'...') ELSE content END  as content").where("traceId=? and isTrace =1").orderBy("seasonId").getSelect();
        return this.dbSession.list(sql, new Object[]{traceId}, this.cls);
    }

    public int update(Integer status, Integer preStatus, String id, Date lastTime) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "status=?,lastTime=?", "id=? and lastTime=? and status=?"});
        return this.dbSession.update(sql, new Object[]{status, new Date(), id, lastTime, preStatus});
    }

    public int updateForTrace(Integer status, Integer preStatus, String id, Date createTime, Date lastTime) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "status=?,createTime=?,lastTime=?", "id=? and lastTime=? and status=?"});
        return this.dbSession.update(sql, new Object[]{status, new Date(), new Date(), id, lastTime, preStatus});
    }

    public List<Bet> listByAccount(String account, int num) {
        String sql = new SQLUtils(this.tableName).field("id,lotteryId,playerId,seasonId,account,lotteryName,playName,price,unit,amount,status,bonusType,betCount,createTime,lastTime,openNum,groupName,win,bonusRate,traceId,isTrace,test,theoreticalBonus,traceWinStop,CASE WHEN LENGTH(content)>1500 THEN CONCAT(LEFT(content,1500),'...') ELSE content END  as content").where("account=? AND status!=3").orderBy("createTime DESC,seasonId DESC LIMIT ?").getSelect();
        return this.dbSession.list(sql, new Object[]{account, Integer.valueOf(num)}, this.cls);
    }

    public List<Bet> adminList(Page p, CommonModel m) {
        List<Object> args = new ArrayList<>();
        String where = " WHERE status!=3 ";
        if (!StrUtils.hasEmpty(new Object[]{m.getId()})) {
            where = where + " and id =?";
            args.add(m.getId());
        } else {
            if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
                if ((m.getInclude() == null) || (m.getInclude() == 0)) {
                    where = where + " and account =?";
                    args.add(m.getAccount());
                } else if (m.getInclude() == 1) {
                    where = where + " and account in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%'))";
                    args.add(m.getAccount());
                }
            }
            if (!StrUtils.hasEmpty(new Object[]{m.getLotteryId()})) {
                where = where + " and lotteryId =?";
                args.add(m.getLotteryId());
            }
            if (!StrUtils.hasEmpty(new Object[]{m.getPlayerId()})) {
                where = where + " and playerId =?";
                args.add(m.getPlayerId());
            }
            if (!StrUtils.hasEmpty(new Object[]{m.getStartSeasonId()})) {
                where = where + " and seasonId >=?";
                args.add(m.getStartSeasonId());
            }
            if (!StrUtils.hasEmpty(new Object[]{m.getEndSeasonId()})) {
                where = where + " and seasonId <=?";
                args.add(m.getEndSeasonId());
            }
            if (!StrUtils.hasEmpty(new Object[]{m.getGroupName()})) {
                where = where + " and groupName =?";
                args.add(m.getGroupName());
            }
            if ((m.getTest() != null) && (m.getTest() != 2)) {
                where = where + " and test =?";
                args.add(m.getTest());
            }
            if ((m.getStatuss() != null) && (m.getStatuss().length > 0)) {
                where = where + " and status in(" + getQuestionNumber(m.getStatuss().length) + ")";
                for (int i = 0; i < m.getStatuss().length; i++) {
                    args.add(m.getStatuss()[i]);
                }
            }
            if (!StrUtils.hasEmpty(new Object[]{m.getStartTime()})) {
                where = where + " and createTime >=?";
                args.add(m.getStartTime());
            }
            if (!StrUtils.hasEmpty(new Object[]{m.getEndTime()})) {
                where = where + " and createTime <=?";
                args.add(m.getEndTime());
            }
            if (!StrUtils.hasEmpty(new Object[]{m.getLowerAmount()})) {
                where = where + " and win >=?";
                args.add(m.getLowerAmount());
            }
            if (!StrUtils.hasEmpty(new Object[]{m.getHighAmount()})) {
                where = where + " and win <=?";
                args.add(m.getHighAmount());
            }
            if (!StrUtils.hasEmpty(new Object[]{m.getBetLowerAmount()})) {
                where = where + " and amount >=?";
                args.add(m.getBetLowerAmount());
            }
            if (!StrUtils.hasEmpty(new Object[]{m.getBetHighAmount()})) {
                where = where + " and amount <=?";
                args.add(m.getBetHighAmount());
            }
        }
        Object[] arg = args.toArray();
        String sql = "SELECT id,lotteryId,playerId,seasonId,account,lotteryName,playName,price,unit,amount,status,bonusType,betCount,createTime,lastTime,openNum,groupName,win,bonusRate,traceId,isTrace,test,theoreticalBonus,traceWinStop,CASE WHEN LENGTH(content)>1500 THEN CONCAT(LEFT(content,1500),'...') ELSE content END  as content FROM " + this.tableName + where + " order by createTime desc,seasonId desc ";
        String pageSql = "SELECT COUNT(1) FROM " + this.tableName + where;
        String allSql = "SELECT IFNULL(SUM(AMOUNT), 0) as amount,IFNULL(SUM(WIN), 0) as win FROM " + this.tableName + where;
        Map<String, Object> a = this.dbSession.getMap(allSql, arg);
        p.setObj(a);
        return this.dbSession.listAndPage(sql, arg, pageSql, arg, this.cls, p);
    }

    public List<Bet> listByLatest(int pageNo, String userName) {
        String sql = new SQLUtils(this.tableName).field("id,lotteryId,playerId,seasonId,account,lotteryName,playName,price,unit,amount,status,bonusType,betCount,createTime,lastTime,openNum,groupName,win,bonusRate,traceId,isTrace,test,theoreticalBonus,traceWinStop,CASE WHEN LENGTH(content)>1500 THEN CONCAT(LEFT(content,1500),'...') ELSE content END  as content").where("account=?").orderBy("createTime DESC,seasonId DESC LIMIT ?").getSelect();
        return this.dbSession.list(sql, new Object[]{userName, pageNo}, this.cls);
    }

    public Bet findShort(String id) {
        String sql = new SQLUtils(this.tableName).field("id,lotteryId,playerId,seasonId,account,lotteryName,playName,price,unit,amount,status,bonusType,betCount,createTime,lastTime,openNum,groupName,win,bonusRate,traceId,isTrace,test,theoreticalBonus,traceWinStop,CASE WHEN LENGTH(content)>1500 THEN CONCAT(LEFT(content,1500),'...') ELSE content END  as content").where("id=?").getSelect();
        return (Bet) this.dbSession.getObject(sql, new Object[]{id}, this.cls);
    }

    public String findContentNext(String id, Integer i) {
        if ((i == null) || (i == 0)) {
            i = 1;
        }
        Integer index = i * 1500 + 1;
        String sql = new SQLUtils(this.tableName).field("CASE WHEN LENGTH(SUBSTRING(content,?))>1500 THEN CONCAT(SUBSTRING(content,?,1500),'...') ELSE SUBSTRING(content,?,1500) END  as content")
                .where("id=?").getSelect();
        return this.dbSession.getString(sql, new Object[]{index, index, index, id});
    }

    public List<Bet> getBetData(TeamReport m) {
        StringBuffer buffer = new StringBuffer(" select t.account, sum(IFNULL(t.amount,0)) as amount,sum(IFNULL(t.win,0)) as win ");
        buffer.append(" from t_bet t where ");
        buffer.append(" t.account in (SELECT\taccount\tFROM\tt_user\tWHERE parentList LIKE CONCAT(( ");
        buffer.append(" SELECT  parentList FROM  t_user ");
        buffer.append(" WHERE account =? AND test =?),'%')) ");
        buffer.append(" and t.status in (1,2) ");
        buffer.append(" and t.lastTime BETWEEN ? and ? ");
        buffer.append(" GROUP BY t.account ");
        String sql = buffer.toString();
        Object[] argsObjects = null;
        argsObjects = new Object[]{m.getAccount(), m.getTest(), WebDateUtils.getProfitBeginTime(m.getCreateDate()), m.getCreateDate()};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public List<Bet> getAllBetData(TeamReport m) {
        StringBuffer buffer = new StringBuffer(" select t.account, sum(IFNULL(t.amount,0)) as amount,sum(IFNULL(t.win,0)) as win ");
        buffer.append(" from t_bet t where t.status in (1,2) ");
        buffer.append(" and t.lastTime BETWEEN ? and ? ");
        buffer.append(" GROUP BY t.account ");
        String sql = buffer.toString();
        Object[] argsObjects = null;
        argsObjects = new Object[]{WebDateUtils.getProfitBeginTime(m.getCreateDate()), m.getCreateDate()};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public BigDecimal getBetAmountByDay(String account, Date beginTime, Date endTime) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"SUM(amount)", this.tableName, "WHERE account=?  AND createTime BETWEEN ? AND ?  AND status in(1,2) "});
        BigDecimal bigDecimal = new BigDecimal("0");
        String tempstr = this.dbSession.getString(sql, new Object[]{account, beginTime, endTime});
        if (tempstr != null) {
            bigDecimal = new BigDecimal(tempstr);
        }
        return bigDecimal;
    }

    public List<Bet> listByCond(String account, Integer start, Integer limit) {
        String SQL_LIST = new SQLUtils(this.tableName).field("id,lotteryId,playerId,seasonId,account,lotteryName,playName,price,unit,amount,status,bonusType,betCount,createTime,lastTime,openNum,groupName,win,bonusRate,traceId,isTrace,test,theoreticalBonus,traceWinStop,CASE WHEN LENGTH(content)>1500 THEN CONCAT(LEFT(content,1500),'...') ELSE content END  as content").where("account=?").orderBy("createTime DESC,seasonId DESC LIMIT ?,?").getSelect();
        return this.dbSession.list(SQL_LIST, new Object[]{account, start, limit}, this.cls);
    }

    public int countBeforeSeason(String lotteryId, String seasonIdBegin, List<Integer> statusList) {
        String sql = "select count(1) from t_bet t WHERE t.lotteryId = ? AND t.seasonId in (select t3.seasonId from (SELECT t2.seasonId FROM t_lottery_sale_time t2 WHERE t2.lotteryId = ? AND t2.seasonId < ? ORDER BY t2.seasonId desc LIMIT 50) t3) AND t.status ";
        List<Object> cond = new ArrayList<>();
        cond.add(lotteryId);
        cond.add(lotteryId);
        cond.add(seasonIdBegin);
        if (statusList.size() == 1) {
            sql = sql + "=?";
            cond.add(statusList.get(0));
        } else {
            sql = sql + "in(?";
            cond.add(statusList.get(0));
            for (int i = 1; i < statusList.size(); i++) {
                sql = sql + ",?";
                cond.add(statusList.get(i));
            }
            sql = sql + ")";
        }
        return this.dbSession.getInt(sql, cond.toArray(new Object[cond.size()]));
    }

    public int deleteByTraceId(String traceId, boolean needBackup) {
        Object[] cond = {traceId};
        if (needBackup) {
            this.dbSession.update("INSERT INTO backup_t_bet SELECT * FROM t_bet WHERE traceId = ?", cond);
        }
        return this.dbSession.update("DELETE FROM t_bet WHERE traceId = ?", cond);
    }

    //**************************************以下为变更部分*****************************************


    public int betOrderListCount(String account, Integer include, Integer status, String startTime, String endTime) {
        List<Object> args = new ArrayList<Object>();
        String where = " WHERE status!=3 ";

        if (!StrUtils.hasEmpty(new Object[]{include})) {
            if ((include == null) || (include == 0)) {
                where = where + " and account =?";
                args.add(account);
            } else if (include == 1) {
                where = where + " and account in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%'))";
                args.add(account);
            } else if (include == 2) {
                where = where + " and account in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%')) and account !=?";
                args.add(account);
                args.add(account);
            }
        }
        if (!StrUtils.hasEmpty(new Object[]{status}) && status != 100 && status != 0 && status != 6) {
            where = where + " and status =?";
            args.add(status);
        } else if (status == 0 || status == 6) {
            where = where + " and status in (0,6)";
        }
        if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            where = where + " and createTime >=?";
            args.add(startTime);
        }
        if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            where = where + " and createTime <=?";
            args.add(endTime);
        }

        Object[] arg = args.toArray();
        String sql = "SELECT count(*) FROM " + this.tableName + where + " order by createTime desc,seasonId desc ";

        return this.dbSession.getInt(sql, arg);
    }

    public List<Bet> betOrderList(String account, Integer include, Integer status, String startTime, String endTime, Integer start, Integer limit) {
        List<Object> args = new ArrayList<Object>();
        String where = " WHERE status!=3 ";

        if (!StrUtils.hasEmpty(new Object[]{include})) {
            if ((include == null) || (include == 0)) {
                where = where + " and account =?";
                args.add(account);
            } else if (include == 1) {
                where = where + " and account in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%'))";
                args.add(account);
            } else if (include == 2) {
                where = where + " and account in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%')) and account !=?";
                args.add(account);
                args.add(account);
            }
        }
        if (!StrUtils.hasEmpty(new Object[]{status}) && status != 100 && status != 0 && status != 6) {
            where = where + " and status =?";
            args.add(status);
        } else if (status == 0 || status == 6) {
            where = where + " and status in (0,6)";
        }
        if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            where = where + " and createTime >=?";
            args.add(startTime);
        }
        if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            where = where + " and createTime <=?";
            args.add(endTime);
        }


        String limitStr = "";

        if (!StrUtils.hasEmpty(new Object[]{start}) && !StrUtils.hasEmpty(new Object[]{limit})) {

            limitStr = " LIMIT ?,? ";

            args.add(start);
            args.add(limit);
        }

        Object[] arg = args.toArray();
        String sql = "SELECT id,lotteryId,lotteryName,amount,createTime,status,win,openNum,playName,CASE WHEN LENGTH(content)>1500 THEN CONCAT(LEFT(content,1500),'...') ELSE content END  as content,seasonId,account,price,betCount,playerId FROM " + this.tableName + where + " order by createTime desc,seasonId desc " + limitStr;

        return this.dbSession.list(sql, arg, this.cls);
    }


    //投注人数：唯一投注
    public List<Map<String, Object>> getTeamInfoByAccountAndTime(String account, Date startTime, Date endTime) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT DISTINCT account ";
        sql = sql + " FROM t_bet WHERE 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            args.add(endTime);
        }

        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND account in (SELECT u.account FROM t_user as u WHERE u.parentList LIKE CONCAT((SELECT us.parentList from t_user as us where us.account=?),'%')) ";
            args.add(account);
        }

        Object[] arg = args.toArray();
        return this.dbSession.listMap(sql, arg, null);
    }

    //数据中状态3为“未开始”，4为“个人撤单”，5为“系统撤单”
    public List<Bet> cpsReportBet(Date startTime, Date endTime) {
        List<Object> args = new ArrayList<Object>();
        String where = " WHERE status!=3 and test = 0 ";
        if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            where = where + " and createTime >=?";
            args.add(startTime);
        }
        if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            where = where + " and createTime <=?";
            args.add(endTime);
        }
        Object[] arg = args.toArray();

        String sql = "SELECT amount,status,win,account FROM " + this.tableName + where + " order by createTime desc,seasonId desc ";

        return this.dbSession.list(sql, arg, this.cls);
    }


    public void save_Z(Bet bet) {
        String sql = new SQLUtils(this.tableName).field("id,lotteryId,playerId,seasonId,account,lotteryName,playName,content,price,unit,amount,status,bonusType")
                .field("betCount,hashCode,createTime,groupName,bonusRate,win,lastTime,openNum,traceId,isTrace,test,theoreticalBonus,traceWinStop,ocRebateAmount").getInsert();

        this.dbSession.update(sql,
                new Object[]{bet.getId(), bet.getLotteryId(), bet.getPlayerId(), bet.getSeasonId(), bet.getAccount(), bet.getLotteryName(), bet.getPlayName(), bet.getContent(), bet.getPrice(),
                        bet.getUnit(), bet.getAmount(), bet.getStatus(), bet.getBonusType(), bet.getBetCount(), bet.getHashCode(), bet.getCreateTime(), bet.getGroupName(), bet.getBonusRate(),
                        bet.getWin(), bet.getLastTime(), bet.getOpenNum(), bet.getTraceId(), bet.getIsTrace(), bet.getTest(), bet.getTheoreticalBonus(), bet.getTraceWinStop(), bet.getOcRebateAmount()});
    }

    /**
     * 彩种报表
     *
     * @param lotteryIdArr  彩种ID数组
     * @param sorting       排序标记，0：投注递减、1：盈利递减、2：盈利递增、3：盈率递减、4：盈率递增、5：中奖递减、6：投注人数递减
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param p             分页对象
     * @return
     */
    public List<BetReport> betReport(String[] lotteryIdArr, Integer sorting, Date startTime, Date endTime, Page p){
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT ";
        sql = sql + " b.lotteryId, ";
        sql = sql + " b.lotteryName, ";
        sql = sql + " b.betPerNum, ";
        sql = sql + " b.betAmount, ";
        sql = sql + " b.winningAmount, ";
        sql = sql + " ac.rebateAmount, ";
        sql = sql + " (b.betAmount - b.winningAmount - ac.rebateAmount) as profit, ";
        sql = sql + " (((b.betAmount - b.winningAmount - ac.rebateAmount)/b.betAmount)*100) as earningsRatio ";
        sql = sql + " from ";
        sql = sql + " ( ";
        sql = sql + " select  ";
        sql = sql + " lotteryId, ";
        sql = sql + "  lotteryName, ";
        sql = sql + "  count(DISTINCT account, IF(status in (1,2), TRUE, NULL)) as betPerNum, ";
        sql = sql + "  sum(IF(status in (1,2), amount, 0)) as betAmount, ";
        sql = sql + "  sum(IF(status = 1, win, 0)) as winningAmount ";
        sql = sql + "  from t_bet as b ";
        sql = sql + "  where 1=1  ";

        //sql = sql + "  and lastTime >= '2018-06-02 00:00:00' and lastTime <= '2018-08-01 23:59:59'  ";
        if (startTime != null) {
            sql = sql + " AND lastTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND lastTime <= ? ";
            args.add(endTime);
        }

        //sql = sql + " and lotteryId = '' ";
        if(!StrUtils.hasEmpty(new Object[]{lotteryIdArr})){
            sql = sql + " AND lotteryId in (";
            for(int i = 0; i<lotteryIdArr.length; i++){
                if(i == (lotteryIdArr.length - 1)) {
                    sql = sql + "'" + lotteryIdArr[i] + "'";
                }else {
                    sql = sql + "'" + lotteryIdArr[i] + "'" + ",";
                }
            }
            sql = sql + ") ";
        }
        sql = sql + "  AND test = 0 ";

        sql = sql + "  group by lotteryId ";
        sql = sql + " ) as b  ";
        sql = sql + "  left join  ";
        sql = sql + " ( ";
        sql = sql + " select  ";
        sql = sql + "  lotteryId, ";
        sql = sql + "  lotteryName, ";
        sql = sql + "  sum(IF(accountChangeTypeId = 3, changeAmount, 0)) as rebateAmount ";
        sql = sql + "  from  ";
        sql = sql + "  t_amount_change ";
        sql = sql + "  where 1=1  ";

        //sql = sql + "  and changeTime >= '2018-06-02 00:00:00' and changeTime <= '2018-08-01 23:59:59'  ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }

        //sql = sql + " and lotteryId = '' ";
        if(!StrUtils.hasEmpty(new Object[]{lotteryIdArr})){
            sql = sql + " AND lotteryId in (";
            for(int i = 0; i<lotteryIdArr.length; i++){
                if(i == (lotteryIdArr.length - 1)) {
                    sql = sql + "'" + lotteryIdArr[i] + "'";
                }else {
                    sql = sql + "'" + lotteryIdArr[i] + "'" + ",";
                }
            }
            sql = sql + ") ";
        }
        sql = sql + "  AND test = 0 ";

        sql = sql + "  group by lotteryId  ";
        sql = sql + " ) as ac on b.lotteryId = ac.lotteryId  ";

        if (!StrUtils.hasEmpty(new Object[]{sorting})) {
            if (sorting == 1) {//1：盈利递减
                sql = sql + " order by profit desc ";
            } else if (sorting == 2) {//2：盈利递增
                sql = sql + " order by profit ";
            } else if (sorting == 3) {//3：盈率递减
                sql = sql + " order by earningsRatio desc ";
            } else if (sorting == 4) {//4：盈率递增
                sql = sql + " order by earningsRatio ";
            } else if (sorting == 5) {//5：中奖递减
                sql = sql + " order by b.winningAmount desc ";
            } else if (sorting == 6) {//6：投注人数递减
                sql = sql + " order by b.betPerNum desc ";
            } else {//0：投注递减
                sql = sql + " order by b.betAmount desc ";
            }
        }

        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, BetReport.class, p);
    }


}
