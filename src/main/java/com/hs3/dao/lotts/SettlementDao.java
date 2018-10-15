package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.lotts.AmountChange;
import com.hs3.models.CommonModel;
import com.hs3.models.report.AllChange;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository("settlementDao")
public class SettlementDao
        extends BaseDao<AmountChange> {
    private static final String SQL_DELETE_DAYS = "DELETE FROM t_amount_change WHERE changeTime <= ?";

    public void save(AmountChange m) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", new Object[]{this.tableName,
                "playName,changeUser,changeAmount,balance,changeSource,betId,seasonId,lotteryId,changeTime,remark,handlers,accountChangeTypeId,test,playerId,groupName,lotteryName,unit",
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"});
        int id = this.dbSession.updateKeyHolder(sql, new Object[]{
                m.getPlayName(),
                m.getChangeUser(),
                m.getChangeAmount(),
                m.getBalance(),
                m.getChangeSource(),
                m.getBetId(),
                m.getSeasonId(),
                m.getLotteryId(),
                m.getChangeTime(),
                m.getRemark(),
                m.getHandlers(),
                Integer.valueOf(m.getAccountChangeTypeId()),
                Integer.valueOf(m.getTest()),
                m.getPlayerId(),
                m.getGroupName(),
                m.getLotteryName(),
                m.getUnit()});


        m.setId(id);
    }

    public List<AmountChange> list(Page p, CommonModel m) {
        List<Object> args = new ArrayList();


        StringBuffer buffer = new StringBuffer(" select r.*,a.name from (select * from t_amount_change t where 1=1 ");
        if ((m.getAccountChangeTypeId() != null) && (m.getAccountChangeTypeId()  != 0)) {
            buffer.append(" and t.accountChangeTypeId=?");
            args.add(m.getAccountChangeTypeId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getBetId()})) {
            buffer.append(" and t.betId=?");
            args.add(m.getBetId());
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
        if (!m.isLowerLevel()) {
            buffer.append(" and t.changeUser=?");
            args.add(m.getAccount());
        } else {
            buffer.append(" and t.changeUser in (SELECT account FROM t_user\tWHERE parentList LIKE CONCAT((\tSELECT\tparentList\tFROM t_user WHERE\taccount =?\t),'%')) ");
            args.add(m.getAccount());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStartTime(), m.getEndTime()})) {
            buffer.append(" and t.changeTime  between ? and ?");
            args.add(m.getStartTime());
            args.add(m.getEndTime());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getEndTime()})) {
            buffer.append(" and t.changeTime <=?");
            args.add(m.getEndTime());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getStartTime()})) {
            buffer.append(" and t.changeTime >=?");
            args.add(m.getStartTime());
        }
        buffer.append(" )r, t_account_change_type a where 1=1 and r.accountChangeTypeId =a.id order by r.changeTime desc ");
        String sql = buffer.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, p);
    }

    public List<AllChange> listAll(String account, int status, Date begin, Date end, Page p) {
        List<Object> args1 = new ArrayList();
        List<Object> args2 = new ArrayList();
        String where = " WHERE 1=1";
        if (status == 1) {
            where = where + " AND changeUser in (SELECT account FROM t_user\tWHERE parentList LIKE CONCAT((\tSELECT\tparentList\tFROM t_user WHERE\taccount =?\t),'%')) ";
        } else {
            where = where + " AND changeUser = ?";
        }
        args1.add(account);
        args2.add(account);
        if (begin != null) {
            where = where + " AND changeTime >= ?";
            args1.add(begin);
            args2.add(begin);
        }
        if (end != null) {
            where = where + " AND changeTime <= ?";
            args1.add(end);
            args2.add(end);
        }
        args1.addAll(args2);

        String sql = "SELECT  account, lotteryName, seasonId, playName, createTime, unit, amount, balance, (SELECT name FROM t_account_change_type WHERE id=a.changeType) as changeType \tFROM ( SELECT  changeUser as account, lotteryName, seasonId, playName, changeTime as createTime, unit, changeAmount as amount, balance, accountChangeTypeId as changeType \tFROM  t_amount_change" +


                where +
                " UNION ALL " +
                " SELECT " +
                " changeUser as account," +
                " '' as lotteryName," +
                " '' as seasonId," +
                " '' as playName," +
                " changeTime as createTime," +
                " '' as unit," +
                " changeAmount as amount," +
                " balance," +
                " accountChangeTypeId as changeType" +
                " FROM t_finance_change" +
                where +
                ") as a ORDER BY createTime DESC";

        String pageSql1 = "SELECT ((SELECT COUNT(1) FROM t_amount_change " +
                where + ") + " +
                "(SELECT COUNT(1) FROM t_finance_change " + where + "))";

        Object[] args = args1.toArray();
        return this.dbSession.listAndPage(sql, args, pageSql1, args, AllChange.class, p);
    }

    public List<AmountChange> getAmountChangeByBetId(String betId) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE betId=? order by id desc"});
        return this.dbSession.list(sql, new Object[]{betId}, this.cls);
    }

    public List<AmountChange> adminList(Page p, CommonModel m) {
        List<Object> args = new ArrayList();
        StringBuffer buffer = new StringBuffer(" select t.*,a.name from  t_amount_change t, t_account_change_type a where 1=1 and t.accountChangeTypeId =a.id ");
        if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
            if ((m.getInclude() == null) || (m.getInclude()  == 0)) {
                buffer.append(" and t.changeUser =?");
                args.add(m.getAccount());
            } else if (m.getInclude()  == 1) {
                buffer.append("  and t.changeUser in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%'))");

                args.add(m.getAccount());
            }
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getLotteryId()})) {
            buffer.append(" and t.lotteryId =?");
            args.add(m.getLotteryId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getBetId()})) {
            buffer.append(" and t.betId =?");
            args.add(m.getBetId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getSeasonId()})) {
            buffer.append(" and t.seasonId =?");
            args.add(m.getSeasonId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getGroupName()})) {
            buffer.append(" and t.groupName =?");
            args.add(m.getGroupName());
        }
        if ((m.getTest() != null) && (m.getTest()  != 2)) {
            buffer.append(" and t.test =?");
            args.add(m.getTest());
        }
        if ((m.getAccountChangeTypeId() != null) && (m.getAccountChangeTypeId()  != 0)) {
            buffer.append(" and t.accountChangeTypeId =?");
            args.add(m.getAccountChangeTypeId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStartTime(), m.getEndTime()})) {
            buffer.append(" and t.changeTime  between ? and ?");
            args.add(m.getStartTime());
            args.add(m.getEndTime());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getEndTime()})) {
            buffer.append(" and t.changeTime <=?");
            args.add(m.getEndTime());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getStartTime()})) {
            buffer.append(" and t.changeTime >=?");
            args.add(m.getStartTime());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getLowerAmount(), m.getHighAmount()})) {
            buffer.append(" and t.changeAmount between ? and ?");
            args.add(m.getLowerAmount());
            args.add(m.getHighAmount());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getHighAmount()})) {
            buffer.append(" and t.changeAmount <=?");
            args.add(m.getHighAmount());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getLowerAmount()})) {
            buffer.append(" and t.changeAmount >=?");
            args.add(m.getLowerAmount());
        }
        if ((m.getAccountChangeTypes() != null) && (m.getAccountChangeTypes().length > 0)) {
            buffer.append(" and t.accountChangeTypeId in(" + getQuestionNumber(m.getAccountChangeTypes().length) + ")");
            for (int i = 0; i < m.getAccountChangeTypes().length; i++) {
                args.add(m.getAccountChangeTypes()[i]);
            }
        }
        if ((m.getSortClass() == null) || (m.getSortClass()  == 0)) {
            buffer.append(" order by t.changeTime desc");
        } else if (m.getSortClass()  == 1) {
            buffer.append(" order by t.changeTime ");
        } else if (m.getSortClass()  == 2) {
            buffer.append(" order by t.changeAmount desc");
        } else if (m.getSortClass()  == 3) {
            buffer.append(" order by t.changeAmount ");
        }
        String sql = buffer.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, p);
    }

    public void delete(Integer beforeDays) {
        this.dbSession.update("DELETE FROM t_amount_change WHERE changeTime <= ?", new Object[]{DateUtils.addDay(new Date(), 0 - Math.abs(beforeDays ))});
    }

    public List<AmountChange> getTeamList(String account, Date b, Date end) {
        String sql = "SELECT changeUser,sum(changeAmount) as changeAmount,accountChangeTypeId\tFROM t_amount_change WHERE  betId in( SELECT id FROM t_bet WHERE  \tstatus in(1,2)  \tAND lastTime>=? AND lastTime<=?  \tAND account in(  SELECT account FROM t_user WHERE   parentList LIKE CONCAT((SELECT parentList FROM t_user WHERE account = ?),'%') \t) ) \tGROUP BY changeUser,accountChangeTypeId UNION ALL \tSELECT  changeUser,sum(changeAmount) as changeAmount,accountChangeTypeId \tFROM t_finance_change WHERE  accountChangeTypeId in(11,12,18,19,28)  AND changeTime>=? AND changeTime<=? AND changeUser in( SELECT account FROM t_user WHERE  \tparentList LIKE CONCAT((SELECT parentList FROM t_user WHERE account = ?),'%') ) \tGROUP BY changeUser,accountChangeTypeId";


        return this.dbSession.list(sql, new Object[]{b, end, account, b, end, account}, this.cls);
    }

    public List<AllChange> newAdminList(Page p, CommonModel m) {
        List<Object> args1 = new ArrayList<>();
        List<Object> args2 = new ArrayList<>();


        String where1 = " WHERE 1=1 ";
        String where2 = " WHERE 1=1 ";
        if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
            String w = null;
            if (m.getInclude() != null && m.getInclude() == 1) {
                w = " AND changeUser in (SELECT account FROM t_user\tWHERE parentList LIKE CONCAT((\tSELECT\tparentList\tFROM t_user WHERE\taccount =?\t),'%')) ";
            } else {
                w = " AND changeUser = ?";
            }
            where1 = where1 + w;
            where2 = where2 + w;
            args1.add(m.getAccount());
            args2.add(m.getAccount());
        }
        if (m.getStartTime() != null) {
            String w = " AND changeTime>=?";
            where1 = where1 + w;
            where2 = where2 + w;
            args1.add(m.getStartTime());
            args2.add(m.getStartTime());
        }
        if (m.getEndTime() != null) {
            String w = " AND changeTime<=?";
            where1 = where1 + w;
            where2 = where2 + w;
            args1.add(m.getEndTime());
            args2.add(m.getEndTime());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getSeasonId()})) {
            where1 = where1 + " AND seasonId=? ";
            args1.add(m.getSeasonId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getBetId()})) {
            where1 = where1 + " AND betId =?";
            where2 = where2 + " AND financeId =?";
            args1.add(m.getBetId());
            args2.add(m.getBetId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getLotteryId()})) {
            where1 = where1 + " AND lotteryId=? ";
            args1.add(m.getLotteryId());
        }
        if ((m.getAccountChangeTypes() != null) && (m.getAccountChangeTypes().length > 0)) {
            String w = " AND accountChangeTypeId in(" + getQuestionNumber(m.getAccountChangeTypes().length) + ")";
            where1 = where1 + w;
            where2 = where2 + w;
            for (int i = 0; i < m.getAccountChangeTypes().length; i++) {
                args1.add(m.getAccountChangeTypes()[i]);
                args2.add(m.getAccountChangeTypes()[i]);
            }
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getGroupName()})) {
            where1 = where1 + " AND groupName =?";
            args1.add(m.getGroupName());
        }
        if ((m.getTest() != null) && (m.getTest()  != 2)) {
            where1 = where1 + " AND test =?";
            args1.add(m.getTest());
        }
        if (m.getLowerAmount() != null) {
            String w = " AND changeAmount>=?";
            where1 = where1 + w;
            where2 = where2 + w;
            args1.add(m.getLowerAmount());
            args2.add(m.getLowerAmount());
        }
        if (m.getHighAmount() != null) {
            String w = " AND changeAmount<=?";
            where1 = where1 + w;
            where2 = where2 + w;
            args1.add(m.getHighAmount());
            args2.add(m.getHighAmount());
        }
        String sql = "SELECT " +
                UserDao.getMarkSQL("a.account") + "," +
                " account," +
                " lotteryName," +
                " seasonId," +
                " playName," +
                " createTime," +
                " unit," +
                " amount," +
                " balance," +
                "     test," +
                "     groupName," +
                "     betId," +
                " (SELECT name FROM t_account_change_type WHERE id=a.changeType) as changeType " +
                " FROM (" +
                " SELECT " +
                " changeUser as account," +
                " lotteryName," +
                " lotteryId," +
                " seasonId," +
                " playName," +
                " changeTime as createTime," +
                " unit," +
                " changeAmount as amount," +
                " balance,test,groupName,betId," +
                " accountChangeTypeId as changeType" +
                " FROM " +
                " t_amount_change" +
                where1 +
                " UNION ALL " +
                " SELECT " +
                " changeUser as account," +
                " '' as lotteryName," +
                " '' as lotteryId," +
                " '' as seasonId," +
                " '' as playName," +
                " changeTime as createTime," +
                " '' as unit," +
                " changeAmount as amount," +
                " balance,'' as test,'' as groupName,IFNULL(financeId,'') as betId," +
                " accountChangeTypeId as changeType" +
                " FROM t_finance_change" +
                where2 +
                ") as a";
        if ((m.getSortClass() == null) || (m.getSortClass() == 0)) {
            sql = sql + " ORDER BY createTime DESC";
        } else if (m.getSortClass() == 1) {
            sql = sql + " ORDER BY createTime ";
        } else if (m.getSortClass() == 2) {
            sql = sql + " ORDER BY amount DESC";
        } else if (m.getSortClass() == 3) {
            sql = sql + " ORDER BY amount ";
        }
        List<Object> args = new ArrayList();
        args.addAll(args1);
        args.addAll(args2);

        return this.dbSession.list(sql, args.toArray(), AllChange.class, p);
    }

    //**************************************以下为变更部分*****************************************

    //代理返点：下级下注的返点
    public List<Map<String, Object>> getTeamInfo(String account, Date startTime, Date endTime, Integer accountChangeTypeId) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT sum(changeAmount) as sumChangeAmount ";
        sql = sql + " FROM t_amount_change WHERE 1=1 ";
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


    public List<AllChange> listAll_Z(String account, Integer include, Integer accountChangeType, Date begin, Date end, Integer start, Integer limit) {
        List<Object> args1 = new ArrayList<Object>();
        List<Object> args2 = new ArrayList<Object>();
        String where = " WHERE 1=1";

        if (!StrUtils.hasEmpty(new Object[]{include})) {
            if ((include == null) || (include  == 0)) {
                where = where + " and changeUser =?";
                args1.add(account);
                args2.add(account);
            } else if (include  == 1) {
                where = where + " and changeUser in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%'))";
                args1.add(account);
                args2.add(account);
            } else if (include  == 2) {
                where = where + " and changeUser in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%')) and changeUser !=?";
                args1.add(account);
                args1.add(account);
                args2.add(account);
                args2.add(account);
            }
        }


        if (!StrUtils.hasEmpty(new Object[]{accountChangeType}) && accountChangeType != 100 && accountChangeType != 0) {
            if (accountChangeType == 1) {
                where = where + " and accountChangeTypeId in (18,52)";
            } else if (accountChangeType == 2) {
                where = where + " and accountChangeTypeId in (11,12,13,15,20,21,24,26)";
            }
        }

        if (begin != null) {
            where = where + " AND changeTime >= ?";
            args1.add(begin);
            args2.add(begin);
        }
        if (end != null) {
            where = where + " AND changeTime <= ?";
            args1.add(end);
            args2.add(end);
        }
        args1.addAll(args2);

        String sql = "SELECT  account, lotteryName, seasonId, playName, createTime, unit, amount, balance, (SELECT name FROM t_account_change_type WHERE id=a.changeType) as changeType \tFROM ( SELECT  changeUser as account, lotteryName, seasonId, playName, changeTime as createTime, unit, changeAmount as amount, balance, accountChangeTypeId as changeType \tFROM  t_amount_change" +

                where +
                " UNION ALL " +
                " SELECT " +
                " changeUser as account," +
                " '' as lotteryName," +
                " '' as seasonId," +
                " '' as playName," +
                " changeTime as createTime," +
                " '' as unit," +
                " changeAmount as amount," +
                " balance," +
                " accountChangeTypeId as changeType" +
                " FROM t_finance_change" +
                where +
                ") as a ORDER BY createTime DESC";


        String limitStr = "";

        if (!StrUtils.hasEmpty(new Object[]{start}) && !StrUtils.hasEmpty(new Object[]{limit})) {

            limitStr = " LIMIT ?,? ";

            args1.add(start);
            args1.add(limit);
        }

        sql = sql + limitStr;

        Object[] args = args1.toArray();
        //return this.dbSession.listAndPage(sql, args, pageSql1, args, AllChange.class, p);
        return this.dbSession.list(sql, args, AllChange.class);
    }


    public int listAll_ZNum(String account, Integer include, Integer accountChangeType, Date begin, Date end) {
        List<Object> args1 = new ArrayList<Object>();
        List<Object> args2 = new ArrayList<Object>();
        String where = " WHERE 1=1";

        if (!StrUtils.hasEmpty(new Object[]{include})) {
            if ((include == null) || (include  == 0)) {
                where = where + " and changeUser =?";
                args1.add(account);
                args2.add(account);
            } else if (include  == 1) {
                where = where + " and changeUser in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%'))";
                args1.add(account);
                args2.add(account);
            } else if (include  == 2) {
                where = where + " and changeUser in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%')) and changeUser !=?";
                args1.add(account);
                args1.add(account);
                args2.add(account);
                args2.add(account);
            }
        }


        if (!StrUtils.hasEmpty(new Object[]{accountChangeType}) && accountChangeType != 100 && accountChangeType != 0) {
            if (accountChangeType == 1) {
                where = where + " and accountChangeTypeId in (18,52)";
            } else if (accountChangeType == 2) {
                where = where + " and accountChangeTypeId in (11,12,13,15,20,21,24,26)";
            }
        }

        if (begin != null) {
            where = where + " AND changeTime >= ?";
            args1.add(begin);
            args2.add(begin);
        }
        if (end != null) {
            where = where + " AND changeTime <= ?";
            args1.add(end);
            args2.add(end);
        }
        args1.addAll(args2);

        String sql = " SELECT count(*) from ( SELECT id \tFROM  t_amount_change" +

                where +
                " UNION ALL " +
                " SELECT id " +
                " FROM t_finance_change" +
                where +
                ") as a ";

        Object[] args = args1.toArray();
        return this.dbSession.getInt(sql, args);
    }


    //根据用户与时间获取投注帐变的变更金额列表
    public List<AmountChange> findListAmountChange(String account, Date startTime) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT changeAmount from t_amount_change where changeUser = ? and accountChangeTypeId = 1";
        args.add(account);
        if (startTime != null) {
            sql = sql + " AND changeTime >= ?";
            args.add(startTime);
        }
        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, AmountChange.class);
    }

    //获取投注帐变涉及综合统计的的变更金额列表
    //返点金额类：下级投注返点/3
    public List<AmountChange> cpsReportAmountChange(Date startTime, Date endTime) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT changeAmount,changeUser,accountChangeTypeId from t_amount_change where 1 = 1 and test = 0 and accountChangeTypeId in (3)";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ?";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ?";
            args.add(endTime);
        }
        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, AmountChange.class);
    }



}
