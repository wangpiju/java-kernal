package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.Page;
import com.hs3.entity.users.UserAgentsNature;
import com.hs3.utils.StrUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("userAgentsNatureDao")
public class UserAgentsNatureDao extends BaseDao<UserAgentsNature> {


    public void save(UserAgentsNature m) {
        this.dbSession.update("INSERT INTO t_user_agents_nature(account,isA,isDaily,isDividend,isDailyLottery,isDividendLottery,modifyTime,operator) VALUES(?,?,?,?,?,?,?,?)",
                new Object[]{m.getAccount(), m.getIsA(), m.getIsDaily(), m.getIsDividend(), m.getIsDailyLottery(), m.getIsDividendLottery(), new Date(), m.getOperator()});
    }


    public int update(UserAgentsNature m) {
        return this.dbSession.update("UPDATE t_user_agents_nature SET isA=?,isDaily=?,isDividend=?,isDailyLottery=?,isDividendLottery=?,modifyTime=?,operator=? WHERE account=?",
                new Object[]{m.getIsA(),m.getIsDaily(),m.getIsDividend(),m.getIsDailyLottery(),m.getIsDividendLottery(),new Date(),m.getOperator(), m.getAccount()});
    }


    public UserAgentsNature findByID(Integer id){
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT ";
        sql = sql + " u.account,u.parentAccount,u.loginTime, ";
        sql = sql + " uan.id,uan.account as accountUan,uan.isA,uan.isDaily,uan.isDailyLottery,uan.isDividend,uan.isDividendLottery,uan.modifyTime,uan.operator ";
        sql = sql + " from t_user as u ";
        sql = sql + " LEFT JOIN t_user_agents_nature as uan on uan.account = u.account ";
        sql = sql + " where 1=1 ";

        sql = sql + " AND uan.id = ? ";
        args.add(id);

        sql = sql + " u.userType = 1 and u.test = 0 ";

        Object[] arg = args.toArray();
        return (UserAgentsNature) this.dbSession.getObject(sql, arg, this.cls);
    }


    public UserAgentsNature findByAccount(String account){
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT ";
        sql = sql + " u.account,u.parentAccount,u.loginTime, ";
        sql = sql + " uan.id,uan.account as accountUan,uan.isA,uan.isDaily,uan.isDailyLottery,uan.isDividend,uan.isDividendLottery,uan.modifyTime,uan.operator ";
        sql = sql + " from t_user as u ";
        sql = sql + " LEFT JOIN t_user_agents_nature as uan on uan.account = u.account ";
        sql = sql + " where 1=1 ";

        sql = sql + " AND u.account = ? ";
        args.add(account);

        sql = sql + " and u.userType = 1 and u.test = 0 ";

        Object[] arg = args.toArray();
        return (UserAgentsNature) this.dbSession.getObject(sql, arg, this.cls);
    }


    /**
     * 查询
     *
     * @param account           会员账户
     * @param parentAccount     上级账户
     * @param isA               是否A类代理 0：否、1：是
     * @param isDaily           是否日工资 0：否、1：是
     * @param isDividend        是否周期分红 0：否、1：是
     * @param isDailyLottery    是否日工资彩种加奖 0：否、1：是
     * @param isDividendLottery 是否周期分红彩种加奖 0：否、1：是
     * @param p                 分页对象
     * @return
     */
    public List<UserAgentsNature> userAgentsNatureList(String account, String parentAccount, Integer isA, Integer isDaily, Integer isDividend, Integer isDailyLottery, Integer isDividendLottery, Page p) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT ";
        sql = sql + " u.account,u.parentAccount,u.loginTime, ";
        sql = sql + " uan.id,uan.account as accountUan,uan.isA,uan.isDaily,uan.isDailyLottery,uan.isDividend,uan.isDividendLottery,uan.modifyTime,uan.operator ";
        sql = sql + " from t_user as u ";
        sql = sql + " LEFT JOIN t_user_agents_nature as uan on uan.account = u.account ";
        sql = sql + " where u.userType = 1 and u.test = 0 ";

        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND u.account like ? ";
            args.add("%" + account + "%");
        }

        if (!StrUtils.hasEmpty(new Object[]{parentAccount})) {
            sql = sql + " AND u.parentAccount like ? ";
            args.add("%" + parentAccount + "%");
        }

        if (!StrUtils.hasEmpty(new Object[]{isA})) {
            sql = sql + " AND uan.isA = ? ";
            args.add(isA);
        }

        if (!StrUtils.hasEmpty(new Object[]{isDaily})) {
            sql = sql + " AND uan.isDaily = ? ";
            args.add(isDaily);
        }

        if (!StrUtils.hasEmpty(new Object[]{isDividend})) {
            sql = sql + " AND uan.isDividend = ? ";
            args.add(isDividend);
        }

        if (!StrUtils.hasEmpty(new Object[]{isDailyLottery})) {
            sql = sql + " AND uan.isDailyLottery = ? ";
            args.add(isDailyLottery);
        }

        if (!StrUtils.hasEmpty(new Object[]{isDividendLottery})) {
            sql = sql + " AND uan.isDividendLottery = ? ";
            args.add(isDividendLottery);
        }

        sql = sql + " ORDER BY uan.modifyTime desc,u.loginTime desc ";

        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, UserAgentsNature.class, p);
    }


    /**
     * 根据状态获取用户
     * @param isA               是否A类代理 0：否、1：是
     * @param isDaily           是否日工资 0：否、1：是
     * @param isDividend        是否周期分红 0：否、1：是
     * @param isDailyLottery    是否日工资彩种加奖 0：否、1：是
     * @param isDividendLottery 是否周期分红彩种加奖 0：否、1：是
     * @return
     */
    public String[] userAgentsNatureAccounts(Integer isA, Integer isDaily, Integer isDividend, Integer isDailyLottery, Integer isDividendLottery) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT ";
        sql = sql + " account ";
        sql = sql + " from t_user_agents_nature where 1=1 ";

        if (!StrUtils.hasEmpty(new Object[]{isA})) {
            sql = sql + " AND isA = ? ";
            args.add(isA);
        }

        if (!StrUtils.hasEmpty(new Object[]{isDaily})) {
            sql = sql + " AND isDaily = ? ";
            args.add(isDaily);
        }

        if (!StrUtils.hasEmpty(new Object[]{isDividend})) {
            sql = sql + " AND isDividend = ? ";
            args.add(isDividend);
        }

        if (!StrUtils.hasEmpty(new Object[]{isDailyLottery})) {
            sql = sql + " AND isDailyLottery = ? ";
            args.add(isDailyLottery);
        }

        if (!StrUtils.hasEmpty(new Object[]{isDividendLottery})) {
            sql = sql + " AND isDividendLottery = ? ";
            args.add(isDividendLottery);
        }

        Object[] arg = args.toArray();
        List<UserAgentsNature> userAgentsNatureList = this.dbSession.list(sql, arg, UserAgentsNature.class, null);
        Integer listSize = userAgentsNatureList.size();

        String[] accountArr = new String[listSize];
        for(int i = 0; i<accountArr.length; i++){
            accountArr[i] = userAgentsNatureList.get(i).getAccount();
        }

        return accountArr;
    }


}
