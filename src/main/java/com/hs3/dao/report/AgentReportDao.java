package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.db.Page;
import com.hs3.entity.report.AgentReport;
import com.hs3.utils.StrUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("agentReportDao")
public class AgentReportDao extends BaseDao<AgentReport> {

    public List<AgentReport> listByCond(String account, String begin, String end, Page page) {
        String sql = "SELECT account,sum(rechargeAmount) as rechargeAmount,sum(withdrawAmount) as withdrawAmount,sum(activityAmount) as activityAmount,sum(dailyAmount) as dailyAmount,sum(dividendAmount) as dividendAmount,sum(winningAmount) as winningAmount,sum(rebateAmount) as rebateAmount,sum(rebateAmountL) as rebateAmountL,sum(betAmount) as betAmount,sum(profit) as profit,sum(betAmountNoOwn) as betAmountNoOwn,sum(profitNoOwn) as profitNoOwn  FROM t_agent_report WHERE 1=1 AND reportDate >= ? AND reportDate <= ? ";
        List<Object> args = new ArrayList<Object>();
        args.add(begin);
        args.add(end);
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND account = ? ";
            args.add(account);
        }
        sql = sql + " group by account ";

        return this.dbSession.list(sql, args.toArray(), this.cls, page);
    }

    public List<AgentReport> listByAccountsAndDate(String accountArr[], String begin, String end, Page page) {
        String sql = "SELECT account,parentAccount,sum(betAmountNoOwn) as betAmountNoOwn,sum(profitNoOwn) as profitNoOwn  FROM t_agent_report WHERE 1=1 AND reportDate >= ? AND reportDate <= ? ";
        List<Object> args = new ArrayList<Object>();
        args.add(begin);
        args.add(end);

        if(!StrUtils.hasEmpty(new Object[]{accountArr})){
            sql = sql + " AND account in (";
            for(int i = 0; i<accountArr.length; i++){
                if(i == (accountArr.length - 1)) {
                    sql = sql + "'" + accountArr[i] + "'";
                }else {
                    sql = sql + "'" + accountArr[i] + "'" + ",";
                }
            }
            sql = sql + ") ";
        }

        sql = sql + " group by account ";

        return this.dbSession.list(sql, args.toArray(), this.cls, page);
    }

    public List<AgentReport> findListByDate(String begin, String end, Page page) {
        String sql = "SELECT * FROM t_agent_report WHERE 1=1 AND reportDate >= ? AND reportDate <= ? ORDER BY reportDate DESC";
        List<Object> args = new ArrayList<Object>();
        args.add(begin);
        args.add(end);
        return this.dbSession.list(sql, args.toArray(), this.cls, page);
    }

    public List<AgentReport> findListByAccountsAndDate(String accountArr[], String begin, String end, Page page) {
        String sql = "SELECT * FROM t_agent_report WHERE 1=1 ";
        List<Object> args = new ArrayList<Object>();

        if (!StrUtils.hasEmpty(new Object[]{begin})) {
            sql = sql + " AND reportDate >= ? ";
            args.add(begin);
        }

        if (!StrUtils.hasEmpty(new Object[]{end})) {
            sql = sql + " AND reportDate <= ? ";
            args.add(end);
        }

        if(!StrUtils.hasEmpty(new Object[]{accountArr})){
            sql = sql + " AND account in (";
            for(int i = 0; i<accountArr.length; i++){
                if(i == (accountArr.length - 1)) {
                    sql = sql + "'" + accountArr[i] + "'";
                }else {
                    sql = sql + "'" + accountArr[i] + "'" + ",";
                }
            }
            sql = sql + ") ";
        }

        return this.dbSession.list(sql, args.toArray(), this.cls, page);
    }


    public int delete(String reportDate) {
        String sql = "DELETE FROM " + this.tableName + " WHERE reportDate=?";
        return this.dbSession.update(sql, new Object[]{reportDate});
    }


    /**
     * 新代理报表
     *
     * @param account           会员账户
     * @param startTime         开始时间
     * @param endTime           结束时间
     * @param startChangeTime   帐变开始时间
     * @param endChangeTime     帐变结束时间
     * @param userType          用户类型 0：会员、1：代理
     * @return
     */
    public List<AgentReport> agentReportList(String account, Date startTime, Date endTime, Date startChangeTime, Date endChangeTime, Integer userType) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT ";
        sql = sql + " ue.account, ";
        sql = sql + " ue.parentAccount, ";
        sql = sql + " ue.amount, ";
        sql = sql + " ue.test, ";
        sql = sql + " ue.userType, ";
        sql = sql + " ue.lowerCount, ";
        sql = sql + " ue.regCount, ";
        sql = sql + " ue.firstChargeCount, ";
        sql = sql + " ue.teamAmount, ";
        sql = sql + " ue.teamCount, ";
        sql = sql + " ue.rechargeAmount, ";
        sql = sql + " ue.withdrawAmount, ";
        sql = sql + " ue.activityAmount, ";
        sql = sql + " ue.dailyAmount, ";
        sql = sql + " ue.dividendAmount, ";
        sql = sql + " ue.winningAmount, ";
        sql = sql + " ue.rebateAmount, ";
        sql = sql + " ue.betAmount ";
        sql = sql + " from ";
        sql = sql + " ( ";
        sql = sql + " select ";
        sql = sql + " u.account, ";
        sql = sql + " u.parentAccount, ";
        sql = sql + " u.amount, ";
        sql = sql + " u.test, ";
        sql = sql + " u.userType, ";
        sql = sql + " u.lowerCount, ";
        sql = sql + " u.regCount, ";
        sql = sql + " u.firstChargeCount, ";
        sql = sql + " ifnull(u.teamAmount,0.00) as teamAmount, ";
        sql = sql + " u.teamCount, ";
        sql = sql + " ifnull(fc.rechargeAmount,0.00) as rechargeAmount, ";
        sql = sql + " ifnull(fc.withdrawAmount,0.00) as withdrawAmount, ";
        sql = sql + " ifnull(fc.activityAmount,0.00) as activityAmount, ";
        sql = sql + " ifnull(fc.dailyAmount,0.00) as dailyAmount, ";
        sql = sql + " ifnull(fc.dividendAmount,0.00) as dividendAmount, ";
        sql = sql + " (ifnull(ac.winningAmount,0.00) - ifnull(ac.cancelAmount,0.00)) as winningAmount, ";
        sql = sql + " (ifnull(ac.rebateAmount,0.00) - ifnull(ac.withdrawalSysAmount,0.00)) as rebateAmount, ";
        sql = sql + " (ifnull(ac.betAmount,0.00) - ifnull(ac.wofwAmount,0.00)) as betAmount ";
        sql = sql + " from ";
        sql = sql + " ( ";
        sql = sql + " select a.account,a.parentAccount,a.amount,a.test,a.userType, ";
        sql = sql + " (SELECT count(1) FROM t_user as uz WHERE uz.parentAccount = a.account) as lowerCount, ";
        sql = sql + " (SELECT count(1) FROM t_user as uz WHERE 1=1 ";
        //sql = sql + " and uz.regTime >= '2018-08-20 00:00:00' and uz.regTime <= '2018-08-20  23:59:59' ";
        if (startTime != null) {
            sql = sql + " AND uz.regTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND uz.regTime <= ? ";
            args.add(endTime);
        }

        sql = sql + " and uz.account in (select tus.subSetAccount from t_user_subset as tus where tus.account = a.account)) as regCount, ";
        sql = sql + " (select count(1) from t_user as uz where 1=1 ";
        //sql = sql + " and uz.rechargeFirstTime >= '2018-08-20 00:00:00' and uz.rechargeFirstTime <= '2018-08-20 23:59:59' ";
        if (startTime != null) {
            sql = sql + " AND uz.rechargeFirstTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND uz.rechargeFirstTime <= ? ";
            args.add(endTime);
        }

        sql = sql + " and uz.account in (select tus.subSetAccount from t_user_subset as tus where tus.account = a.account)) as firstChargeCount, ";
        sql = sql + " (select sum(uz.amount) from t_user as uz where uz.account in (select tus.subSetAccount from t_user_subset as tus where tus.account = a.account)) as teamAmount, ";
        sql = sql + " (select count(1) from t_user_subset as tus where tus.account = a.account) as teamCount ";
        sql = sql + " from t_user as a where 1 = 1 ";

            sql = sql + " and a.account in ( ";

                sql = sql + " SELECT DISTINCT account from t_user_subset where subSetAccount in ( ";

                    sql = sql + " select DISTINCT changeUser from ";
                    sql = sql + " ( ";
                    sql = sql + " SELECT changeUser from t_amount_change ";
                    sql = sql + " where 1=1 ";
                    //sql = sql + " and changeTime >= '2018-08-20 00:00:00' and changeTime <= '2018-08-20 23:59:59' ";
                    if (startTime != null) {
                        sql = sql + " AND changeTime >= ? ";
                        args.add(startTime);
                    }
                    if (endTime != null) {
                        sql = sql + " AND changeTime <= ? ";
                        args.add(endTime);
                    }

                    //sql = sql + " and changeUser = '' ";
                    if (!StrUtils.hasEmpty(new Object[]{account})) {
                        sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = ?) ";
                        args.add(account);
                    }

                    sql = sql + " UNION ALL ";
                    sql = sql + " SELECT changeUser from t_finance_change ";
                    sql = sql + " where 1=1 ";
                    //sql = sql + " and changeTime >= '2018-08-20 00:00:00' and changeTime <= '2018-08-20 23:59:59' ";
                    if (startTime != null) {
                        sql = sql + " AND changeTime >= ? ";
                        args.add(startTime);
                    }
                    if (endTime != null) {
                        sql = sql + " AND changeTime <= ? ";
                        args.add(endTime);
                    }

                    //sql = sql + " and changeUser = '' ";
                    if (!StrUtils.hasEmpty(new Object[]{account})) {
                        sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = ?) ";
                        args.add(account);
                    }

                    sql = sql + " ) as ub ";

                sql = sql + " ) ";

            sql = sql + " ) ";

        sql = sql + " and test = 0 ";
        //sql = sql + " and userType = 1 ";
        /*if (!StrUtils.hasEmpty(new Object[]{userType})) {
            sql = sql + " AND userType = ? ";
            args.add(userType);
        }*/

        sql = sql + " ) as u ";
        sql = sql + " left join ";
        sql = sql + " ( ";
        sql = sql + " select ";
        sql = sql + " changeUser, ";
        sql = sql + " sum(IF(accountChangeTypeId in (12,11,13,21), changeAmount, 0)) as rechargeAmount, ";
        sql = sql + " -(sum(IF(accountChangeTypeId = 18, changeAmount, 0))) as withdrawAmount, ";
        sql = sql + " sum(IF(accountChangeTypeId = 19, changeAmount, 0)) as activityAmount, ";
        sql = sql + " sum(IF(accountChangeTypeId = 28, changeAmount, 0)) as dailyAmount, ";
        sql = sql + " sum(IF(accountChangeTypeId = 30, changeAmount, 0)) as dividendAmount ";
        sql = sql + " from t_finance_change ";
        sql = sql + " where 1=1 ";

        //sql = sql + " and changeTime >= '2018-08-20 00:00:00' and changeTime <= '2018-08-20 23:59:59' ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startChangeTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endChangeTime);
        }

        //sql = sql + " and changeUser = '' ";
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = ?) ";
            args.add(account);
        }

        sql = sql + " and test = 0 ";
        sql = sql + " group by changeUser ";
        sql = sql + " ) as fc on u.account = fc.changeUser ";
        sql = sql + " left join ";
        sql = sql + " ( ";
        sql = sql + " select ";
        sql = sql + " changeUser, ";
        sql = sql + " sum(IF(accountChangeTypeId = 3, changeAmount, 0)) as rebateAmount, ";
        sql = sql + " sum(IF(accountChangeTypeId = 4, changeAmount, 0)) as winningAmount, ";
        sql = sql + " sum(IF(accountChangeTypeId = 7, changeAmount, 0)) as wofwAmount, ";
        sql = sql + " -(sum(IF(accountChangeTypeId = 1, changeAmount, 0))) as betAmount, ";
        sql = sql + " -(sum(IF(accountChangeTypeId = 9, changeAmount, 0))) as withdrawalSysAmount, ";
        sql = sql + " -(sum(IF(accountChangeTypeId = 10, changeAmount, 0))) as cancelAmount ";
        sql = sql + " from t_amount_change ";
        sql = sql + " where 1=1 ";

        //sql = sql + " and changeTime >= '2018-08-20 00:00:00' and changeTime <= '2018-08-20 23:59:59' ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startChangeTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endChangeTime);
        }

        //sql = sql + " and changeUser = '' ";
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = ?) ";
            args.add(account);
        }

        sql = sql + " and test = 0 ";
        sql = sql + " group by changeUser ";
        sql = sql + " ) as ac on u.account = ac.changeUser ";
        sql = sql + " ) as ue ";

        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, AgentReport.class, null);
    }



    /**
     * 根据代理报表历史记录里的用户获取其相关的用户统计信息
     *
     * @param account       会员账户
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param begin         开始日期（代理报表历史用户检索用）
     * @param end           结束日期（代理报表历史用户检索用）
     * @param userType      用户类型 0：会员、1：代理
     * @return
     */
    public List<AgentReport> agentInfoByHistoryAccount(String account, Date startTime, Date endTime, String begin, String end, Integer userType) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT ";
        sql = sql + " a.account,a.parentAccount,a.amount,a.test,a.userType, ";
        sql = sql + " (SELECT count(1) FROM t_user as uz WHERE uz.parentAccount = a.account) as lowerCount, ";
        sql = sql + " (SELECT count(1) FROM t_user as uz WHERE 1=1 ";
        //sql = sql + " and uz.regTime >= '2018-08-20 00:00:00' and uz.regTime <= '2018-08-20  23:59:59' ";
        if (startTime != null) {
            sql = sql + " AND uz.regTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND uz.regTime <= ? ";
            args.add(endTime);
        }

        sql = sql + " and uz.account in (select tus.subSetAccount from t_user_subset as tus where tus.account = a.account)) as regCount, ";
        sql = sql + " (select count(1) from t_user as uz where 1=1 ";
        //sql = sql + " and uz.rechargeFirstTime >= '2018-08-20 00:00:00' and uz.rechargeFirstTime <= '2018-08-20 23:59:59' ";
        if (startTime != null) {
            sql = sql + " AND uz.rechargeFirstTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND uz.rechargeFirstTime <= ? ";
            args.add(endTime);
        }

        sql = sql + " and uz.account in (select tus.subSetAccount from t_user_subset as tus where tus.account = a.account)) as firstChargeCount, ";
        sql = sql + " (select sum(uz.amount) from t_user as uz where uz.account in (select tus.subSetAccount from t_user_subset as tus where tus.account = a.account)) as teamAmount, ";
        sql = sql + " (select count(1) from t_user_subset as tus where tus.account = a.account) as teamCount ";
        sql = sql + " from t_user as a where 1 = 1 ";

        sql = sql + " and (a.account in ( ";
        sql = sql + " select DISTINCT account from t_agent_report ";
        sql = sql + " where 1=1 ";
        if (!StrUtils.hasEmpty(new Object[]{begin})) {
            sql = sql + " AND reportDate >= ? ";
            args.add(begin);
        }
        if (!StrUtils.hasEmpty(new Object[]{end})) {
            sql = sql + " AND reportDate <= ? ";
            args.add(end);
        }

        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " or account = ? ";
            args.add(account);
        }

        sql = sql + " ) ";

        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " or a.account = ? ";
            args.add(account);
        }

        sql = sql + " ) ";
        sql = sql + " and test = 0 ";
        //sql = sql + " and userType = 1 ";
        if (!StrUtils.hasEmpty(new Object[]{userType})) {
            sql = sql + " AND userType = ? ";
            args.add(userType);
        }

        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, AgentReport.class, null);
    }

    /**
     * 获取用户充值金额与投注金额数据
     *
     * @param account       会员账户
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @return
     */
    public List<AgentReport> agentRechargeAndBetInfo(String account, Date startTime, Date endTime) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT ";
        sql = sql + " u.account, ";
        sql = sql + " ifnull(fc.rechargeAmount,0.00) as rechargeAmount, ";
        sql = sql + " (ifnull(ac.betAmount,0.00) - ifnull(ac.wofwAmount,0.00)) as betAmount ";
        sql = sql + " from ";
        sql = sql + " ( ";
        sql = sql + " select ubz.changeUser as account ";
        sql = sql + " from  ( ";
        sql = sql + " select DISTINCT changeUser from ";
        sql = sql + " ( ";
        sql = sql + " SELECT changeUser from t_amount_change ";
        sql = sql + " where 1=1 ";

        //sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = '') ";
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = ?) ";
            args.add(account);
        }

        //sql = sql + " and changeTime >= '2018-08-20 00:00:00' and changeTime <= '2018-08-20 23:59:59' ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }


        sql = sql + " and test = 0 ";
        sql = sql + " UNION ALL ";
        sql = sql + " SELECT changeUser from t_finance_change ";
        sql = sql + " where 1=1 ";

        //sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = '') ";
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = ?) ";
            args.add(account);
        }

        //sql = sql + " and changeTime >= '2018-08-20 00:00:00' and changeTime <= '2018-08-20 23:59:59' ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }

        sql = sql + " and test = 0 ";

        //sql = sql + " UNION ALL ";
        //sql = sql + " select account as changeUser from t_user where account = '' ";
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " UNION ALL ";
            sql = sql + " select account as changeUser from t_user where account = ? ";
            args.add(account);
        }

        sql = sql + " ) as ub ";
        sql = sql + " ) as ubz ";
        sql = sql + " ) as u ";
        sql = sql + " left join ";
        sql = sql + " ( ";
        sql = sql + " select ";
        sql = sql + " changeUser, ";
        sql = sql + " sum(IF(accountChangeTypeId in (12,11,13,21), changeAmount, 0)) as rechargeAmount ";
        sql = sql + " from t_finance_change ";
        sql = sql + " where 1=1 ";

        //sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = '') ";
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = ?) ";
            args.add(account);
        }

        //sql = sql + " and changeTime >= '2018-08-20 00:00:00' and changeTime <= '2018-08-20 23:59:59' ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }

        sql = sql + " and test = 0 ";
        sql = sql + " group by changeUser ";
        sql = sql + " ) as fc on u.account = fc.changeUser ";
        sql = sql + " left join ";
        sql = sql + " ( ";
        sql = sql + " select ";
        sql = sql + " changeUser, ";
        sql = sql + " sum(IF(accountChangeTypeId = 7, changeAmount, 0)) as wofwAmount, ";
        sql = sql + " -(sum(IF(accountChangeTypeId = 1, changeAmount, 0))) as betAmount ";
        sql = sql + " from t_amount_change ";
        sql = sql + " where 1=1 ";

        //sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = '') ";
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = ?) ";
            args.add(account);
        }

        //sql = sql + " and changeTime >= '2018-08-20 00:00:00' and changeTime <= '2018-08-20 23:59:59' ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }

        sql = sql + " and test = 0 ";
        sql = sql + " group by changeUser ";
        sql = sql + " ) as ac on u.account = ac.changeUser ";

        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, AgentReport.class, null);
    }


    public void save(AgentReport m) {
        Object[] a = {
                0,
                m.getReportDate(),
                m.getAccount(),
                m.getLowerCount(),
                m.getRegCount(),
                m.getFirstChargeCount(),
                m.getTeamAmount(),
                m.getTeamCount(),
                m.getRechargeAmount(),
                m.getTeamRechargeCount(),
                m.getWithdrawAmount(),
                m.getActivityAmount(),
                m.getDailyAmount(),
                m.getDividendAmount(),
                m.getWinningAmount(),
                m.getRebateAmount(),
                m.getRebateAmountL(),
                m.getBetAmount(),
                m.getBetPerCount(),
                m.getProfit(),
                m.getBetAmountNoOwn(),
                m.getBetPerCountNoOwn(),
                m.getProfitNoOwn()
        };

        this.dbSession.update("insert into t_agent_report values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", a);
    }



}
