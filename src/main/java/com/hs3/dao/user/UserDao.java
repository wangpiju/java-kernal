package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.report.MembershipReport;
import com.hs3.entity.sys.SysClear;
import com.hs3.entity.users.User;
import com.hs3.models.user.RootUser;
import com.hs3.tasks.sys.SysClearJobEnum;
import com.hs3.tasks.sys.SysClearJobFactory;
import com.hs3.utils.DateUtils;
import com.hs3.utils.RedisUtils;
import com.hs3.utils.StrUtils;
import com.hs3.utils.entity.UnderUser;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("userDao")
public class UserDao
        extends BaseDao<User> {
    private static final String USER_MARK = "(SELECT userMark FROM t_user WHERE account=i.account) as userMark ";
    private static final String INSERT_DOMAIN = "INSERT INTO t_user_domain(id,account) VALUES(?,?)";
    private static final String UPDATE_PASSWORD = "UPDATE t_user SET password=?,passwordStatus=1 WHERE account=?";
    private static final String UPDATE_AMOUNT = "UPDATE t_user SET amount=amount+? WHERE account=?";
    private static final String UPDATE_RECHARGE = "UPDATE t_user SET amount=amount+?,regchargeAmount=regchargeAmount+?,regchargeCount=regchargeCount+?,rechargeFirstTime=(CASE WHEN rechargeFirstTime IS NULL THEN ? ELSE rechargeFirstTime END),rechargeFirstAmount=(CASE WHEN rechargeFirstAmount IS NULL THEN ? ELSE rechargeFirstAmount END) WHERE account=?";
    private static final String UPDATE_DEPOSIT = "UPDATE t_user SET amount=amount+?,depositAmount=depositAmount+?,depositCount=depositCount+?,withdrawalTimes=withdrawalTimes+? WHERE account=?";
    private static final String UPDATE_RECHARGE_LOWER = "UPDATE t_user SET rechargeLowerSouAmount=rechargeLowerSouAmount+?,rechargeLowerTarAmount=rechargeLowerTarAmount+? WHERE account=?";
    private static final String UPDATE_RECHARGE_LOWER_TOTAL = "UPDATE t_user SET rechargeLowerSouAmountTotal=rechargeLowerSouAmountTotal+?,rechargeLowerSouCountTotal=rechargeLowerSouCountTotal+?,rechargeLowerTarAmountTotal=rechargeLowerTarAmountTotal+?,rechargeLowerTarCountTotal=rechargeLowerTarCountTotal+? WHERE account=?";
    private static final String SELECT_AMOUNT = "SELECT amount FROM t_user WHERE account=?";
    private static final String UPDATE_SAFE_PASSWORD = "UPDATE t_user SET safePassword=? WHERE account=?";
    private static final String UPDATE_BANK_GROUP = "UPDATE t_user SET bankGroup=? WHERE account=?";
    private static final String UPDATE_IPINFO = "UPDATE t_user SET regIp=?,regIpInfo=? WHERE account=?";
    private static final String UPDATE_SESSIONID_NULL = "update t_user set sessionId = NULL where sessionId = ?";
    private static final String TEAMAMOUNT = "SELECT SUM(a.amount) FROM t_user a WHERE a.parentList LIKE CONCAT ( (SELECT  parentList  FROM t_user WHERE account = ?),'%')";
    private static final String MESSAGE = "select message from t_user WHERE account=?";
    private static final String SELECT_SAFE = "select count(1) from t_user WHERE account=? and safePassword=?";
    private static final String SELECT_PASS = "select count(1) from t_user WHERE account=? and password=?";
    private static final String ADJUSTQUOTA = "update t_user set rebateRatio = ? where account = ? and parentAccount = ?";
    private static final String UPDATEPARENTREBATERATIONUM = "UPDATE t_user_quota SET num = num + 1 WHERE rebateRatio = ? AND account = ?";
    private static final String UPDATE_BANKSTATUS = "update t_user set bankStatus = '1' where account = ? and bankStatus = '0'";
    private static final String SELECT_USERFIELD = "account,rebateRatio,userType,parentAccount,amount,niceName,(select if(((UNIX_TIMESTAMP(NOW())-IFNULL(UNIX_TIMESTAMP(onlineTime),0))/60)>5,1,2) as isOnLine from t_user_info where account =t.account) as isOnLine ";
    private static final String INSERT_ONLINE = " INSERT INTO t_user_info (account) VALUES(?)";
    private static final String UPDATE_ONLINE = " UPDATE t_user_info SET onlineTime=? WHERE account=?";
    private static final String UPDATE_DAILY_WAGES_STATUS_OPEN = "UPDATE t_user SET dailyWagesStatus = 0 WHERE account = ? AND parentAccount = ?";
    private static final String UPDATE_DAILY_WAGES_STATUS_CLOSE = "UPDATE t_user SET dailyWagesStatus = 1 WHERE parentList LIKE ?";
    private static final String PRIVATEREBATE = "update t_user set privateRebate = ? where account = ?";
    private static final String WHERE = "WHERE loginTime <= ? AND amount >= ? AND amount <= ?";

    public static String getMarkSQL() {
        return "(SELECT userMark FROM t_user WHERE account=i.account) as userMark ";
    }

    public static String getMarkSQL(String alias) {
        return "(SELECT userMark FROM t_user WHERE account=i.account) as userMark ".replaceAll("i.account", alias);
    }

    public void save(User user) {
        String sql = new SQLUtils(this.tableName).field("account,parentList,rootAccount,parentAccount").field("password,safePassword,bonusGroupId,rebateRatio,userType,test,bankGroup")
                .field("regIp,regIpInfo,regTime,loginTime,phone").getInsert();

        Date d = new Date();
        this.dbSession.update(sql,
                new Object[]{user.getAccount(), user.getParentList(), user.getRootAccount(), user.getParentAccount(), StrUtils.MD5(user.getPassword()), StrUtils.MD5(user.getSafePassword()),
                        user.getBonusGroupId(), user.getRebateRatio(), user.getUserType(), user.getTest(), user.getBankGroup(), user.getRegIp(), user.getRegIpInfo(), d, d, user.getPhone()});
    }

    public int updateRechargeInfo(User m) {
        String sql = new SQLUtils(this.tableName).field("accountRecharge,rechargeLowerTarAmount").where("account=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{
                m.getAccountRecharge(),
                m.getRechargeLowerTarAmount(),
                m.getAccount()});
    }

    public int saveUserIp(String account, String ip, Date d, String ipAddress, String userAgent, String parentAccount, String mac, String hard, String cpu) {
        String sql = "insert into t_user_login_ip values(?,?,?,?,?,?,?,?,?,?)";
        Object[] args = {0, ip, d, account, ipAddress, userAgent, parentAccount, mac, hard, cpu};
        return this.dbSession.update(sql, args);
    }

    public int setBet(String account) {
        String sql = "UPDATE t_user SET isBet='1' WHERE isBet='0' AND account=?";
        Object[] args = {account};
        return this.dbSession.update(sql, args);
    }

    public User findByAccountAndPassword(String account, String password) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE account=? AND password=?"});
        Object[] args = {account, StrUtils.MD5(password)};
        return (User) this.dbSession.getObject(sql, args, this.cls);
    }

    public User findByAccountAndSafeWord(String account, String password) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE account=? AND safePassword=?"});
        Object[] args = {account, StrUtils.MD5(password)};
        return (User) this.dbSession.getObject(sql, args, this.cls);
    }

    public int findRecordBySafeWord(String account, String safeWord) {
        Object[] args = {account, StrUtils.MD5(safeWord)};
        return this.dbSession.getInt("select count(1) from t_user WHERE account=? and safePassword=?", args) ;
    }

    public int findRecordByPassWord(String account, String passWord) {
        Object[] args = {account, StrUtils.MD5(passWord)};
        return this.dbSession.getInt("select count(1) from t_user WHERE account=? and password=?", args) ;
    }

    public Integer updateAmount(String account, BigDecimal amount) {
        Object[] args = {amount, account};
        return this.dbSession.update("UPDATE t_user SET amount=amount+? WHERE account=?", args);
    }

    public Integer updateRecharge(String account, BigDecimal amount, BigDecimal regchargeAmount, Integer regchargeCount) {
        return this.dbSession.update("UPDATE t_user SET amount=amount+?,regchargeAmount=regchargeAmount+?,regchargeCount=regchargeCount+?,rechargeFirstTime=(CASE WHEN rechargeFirstTime IS NULL THEN ? ELSE rechargeFirstTime END),rechargeFirstAmount=(CASE WHEN rechargeFirstAmount IS NULL THEN ? ELSE rechargeFirstAmount END) WHERE account=?", new Object[]{amount, regchargeAmount, regchargeCount, new Date(), regchargeAmount, account});
    }

    public Integer updateDeposit(String account, BigDecimal amount, BigDecimal depositAmount, Integer depositCount) {
        return this.dbSession.update("UPDATE t_user SET amount=amount+?,depositAmount=depositAmount+?,depositCount=depositCount+?,withdrawalTimes=withdrawalTimes+? WHERE account=?", new Object[]{amount, depositAmount, depositCount, depositCount, account});
    }

    public Integer updateRechargeLower(String account, BigDecimal sourceAmount, BigDecimal targetAmount) {
        return this.dbSession.update("UPDATE t_user SET rechargeLowerSouAmount=rechargeLowerSouAmount+?,rechargeLowerTarAmount=rechargeLowerTarAmount+? WHERE account=?", new Object[]{sourceAmount, targetAmount, account});
    }

    public Integer updateRechargeLowerTotal(String account, BigDecimal sourceAmount, int sourceCount, BigDecimal targetAmount, int targetCount) {
        return this.dbSession.update("UPDATE t_user SET rechargeLowerSouAmountTotal=rechargeLowerSouAmountTotal+?,rechargeLowerSouCountTotal=rechargeLowerSouCountTotal+?,rechargeLowerTarAmountTotal=rechargeLowerTarAmountTotal+?,rechargeLowerTarCountTotal=rechargeLowerTarCountTotal+? WHERE account=?", new Object[]{sourceAmount, Integer.valueOf(sourceCount), targetAmount, Integer.valueOf(targetCount), account});
    }

    public int updateUserClear() {
        return this.dbSession.update("update t_user set depositAmount = 0, depositCount = 0, depositRebateAmount = 0, depositRebateCount = 0, rechargeLowerSouAmount = 0, rechargeLowerTarAmount = 0 where (depositAmount >= 1 or depositCount >= 1 or rechargeLowerSouAmount >= 1 or rechargeLowerTarAmount >= 1)");
    }

    public BigDecimal getAmount(String account) {
        Object[] args = {account};
        return new BigDecimal(this.dbSession.getString("SELECT amount FROM t_user WHERE account=?", args));
    }

    public BigDecimal getAmountMaster(String account) {
        String sql = "/*master*/SELECT amount FROM t_user WHERE account=? for update";

        Object[] args = {account};
        return new BigDecimal(this.dbSession.getString(sql, args));
    }

    public String findTeamAmount(String account) {
        Object[] args = {account};
        return this.dbSession.getString("SELECT SUM(a.amount) FROM t_user a WHERE a.parentList LIKE CONCAT ( (SELECT  parentList  FROM t_user WHERE account = ?),'%')", args);
    }

    public String findMessage(String account) {
        Object[] args = {account};
        return this.dbSession.getString("select message from t_user WHERE account=?", args);
    }

    public User findByAccount(String account) {
        String sql = new SQLUtils(this.tableName).where("account=?").getSelect();
        Object[] args = {account};
        return (User) this.dbSession.getObject(sql, args, this.cls);
    }

    public User findByAccountMaster(String account) {
        String sql = "/*master*/" + new SQLUtils(this.tableName).where("account=? for update").getSelect();

        Object[] args = {account};
        return (User) this.dbSession.getObject(sql, args, this.cls);
    }

    public User findByAccountAndParent(String parent, String account) {
        String sql = "select * from t_user where account = ? and loginStatus=0 and parentList LIKE CONCAT((select parentList from t_user where account = ?),'%')";
        Object[] args = {account, parent};
        return (User) this.dbSession.getObject(sql, args, this.cls);
    }

    public Map<String, Object> findUserInformation(String account) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT                                                         ");
        sb.append("   u.account,                                                   ");
        sb.append("   CASE                                                         ");
        sb.append("     WHEN u.rootAccount = u.account                             ");
        sb.append("     THEN '总代'                                                ");
        sb.append("     WHEN u.usertype = 0                                        ");
        sb.append("     THEN '会员'                                                ");
        sb.append("     WHEN u.usertype = 1                                        ");
        sb.append("     THEN '代理'                                                ");
        sb.append("   END uType,                                                    ");
        sb.append("   u.amount,                                                    ");
        sb.append("   (SELECT                                                      ");
        sb.append("     SUM(amount)                                                ");
        sb.append("   FROM                                                         ");
        sb.append("     t_user                                                     ");
        sb.append("   WHERE parentList LIKE CONCAT(u.parentList, '%')) teamAmount  ");
        sb.append(" FROM                                                           ");
        sb.append("   t_user u                                                     ");
        sb.append(" WHERE u.account = ?                                            ");
        Object[] args = {account};
        return this.dbSession.getMap(sb.toString(), args);
    }

    public Map<String, Object> findSafeInformation(String account) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT                                                 ");
        sb.append("   CASE                                                 ");
        sb.append("     WHEN u.passwordStatus = 0                          ");
        sb.append("     THEN '未重置'                                      ");
        sb.append("     ELSE '已重置'                                      ");
        sb.append("   END passStatus,                                      ");
        sb.append("   CASE                                                 ");
        sb.append("     WHEN u.safePassword IS NULL                        ");
        sb.append("     THEN '未设置'                                      ");
        sb.append("     ELSE '已设置'                                      ");
        sb.append("   END safeStatus,                                      ");
        sb.append("   CASE                                                 ");
        sb.append("     WHEN s.id IS NULL                                  ");
        sb.append("     THEN '未设置'                                      ");
        sb.append("     ELSE '已设置'                                      ");
        sb.append("   END questionStatus,                                  ");
        sb.append("   CASE                                                 ");
        sb.append("     WHEN u.qq IS NULL                                  ");
        sb.append("     THEN '未设置'                                      ");
        sb.append("     ELSE u.qq                                          ");
        sb.append("   END qq,                                              ");
        sb.append("   CASE                                                 ");
        sb.append("     WHEN u.email IS NULL                               ");
        sb.append("     THEN '未设置'                                      ");
        sb.append("     ELSE u.email                                       ");
        sb.append("   END email,                                           ");
        sb.append("   CASE                                                 ");
        sb.append("     WHEN u.phone IS NULL                               ");
        sb.append("     THEN '未设置'                                      ");
        sb.append("     ELSE u.phone                                       ");
        sb.append("   END phone,                                           ");
        sb.append("   CONCAT(regTime, ' ', regip, ' ', regipinfo) regInfo  ");
        sb.append(" FROM                                                   ");
        sb.append("   t_user u                                             ");
        sb.append("   LEFT JOIN t_user_safe s                              ");
        sb.append("     ON u.account = s.account                           ");
        sb.append(" WHERE u.account = ?                                    ");
        Object[] args = {account};
        return this.dbSession.getMap(sb.toString(), args);
    }

    public List<Map<String, Object>> getLastFiveLogin(String account) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT                     ");
        sb.append("   CONCAT(                  ");
        sb.append("     a.logintime,           ");
        sb.append("     '  ',                  ");
        sb.append("     a.ip,                  ");
        sb.append("     '  ',                  ");
        sb.append("     a.ipinfo               ");
        sb.append("   ) loginInformation       ");
        sb.append(" FROM                       ");
        sb.append("   t_user_login_ip a        ");
        sb.append(" WHERE a.account = ?        ");
        sb.append(" ORDER BY a.loginTime DESC  ");
        sb.append(" LIMIT 0, 5                 ");
        Object[] args = {account};
        return this.dbSession.listMap(sb.toString(), args, null);
    }

    public int findRecordByAccount(String account) {
        String sql = "select count(1) from t_user where account=?";
        Object[] args = {account};
        return this.dbSession.getInt(sql, args) ;
    }

    public int findRecord(String account, String parent) {
        String sql = "select count(1) from t_user where account=? and parentAccount=? and userType=1 and loginStatus=0";
        Object[] args = {account, parent};
        return this.dbSession.getInt(sql, args) ;
    }

    public List<User> listByParent(String account) {
        String sql = new SQLUtils(this.tableName).where("parentList LIKE CONCAT((SELECT parentList FROM t_user WHERE account = ?),'%')").getSelect();
        return this.dbSession.list(sql, new Object[]{account}, this.cls);
    }

    public List<User> listByParent(String account, Page p) {
        SQLUtils u = new SQLUtils(this.tableName);
        if (StrUtils.hasEmpty(new Object[]{account})) {
            String sql = u.where("account=parentAccount").getSelect();
            return this.dbSession.list(sql, this.cls, p);
        }
        String sql = u.where("parentAccount=?").orderBy("parentList,account").getSelect();
        return this.dbSession.list(sql, new Object[]{account}, this.cls, p);
    }

    public List<RootUser> listWithTeam(String account, Page p) {
        Object[] args = null;

        String sql = "SELECT *,(select count(*) FROM t_user WHERE parentAccount=u.account AND account!=u.account) as childCount,(select count(*) FROM t_user WHERE parentList LIKE CONCAT(u.parentList,'%')) as teamCount,(select sum(amount) FROM t_user WHERE parentList LIKE CONCAT(u.parentList,'%')) as teamAmount  FROM t_user as u WHERE ";


        String pageSql = "SELECT COUNT(1) FROM t_user WHERE ";
        if (StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + "account=parentAccount ORDER BY account";
            pageSql = pageSql + "account=parentAccount";
        } else {
            sql = sql + "parentAccount=? OR account=? ORDER BY parentList,account";
            pageSql = pageSql + "parentAccount=? OR account=?";

            args = new Object[]{account, account};
        }
        return this.dbSession.listAndPage(sql, args, pageSql, args, RootUser.class, p);
    }

    public List<RootUser> listAccountWithTeam(String account, Integer bonusGroup, BigDecimal beginRebateRatio, BigDecimal endRebateRatio, BigDecimal beginAmount, BigDecimal endAmount, Integer regTime, Integer userType, Integer order, Integer test, Integer status, Integer userMark, Page p) {
        String pageSQL = "select count(1) from t_user where 1=1";
        StringBuilder pa = new StringBuilder().append("SELECT *,").append("(select count(*) from t_user where parentAccount=u.account AND parentAccount!=account) as childCount,")
                .append("(select count(*) from t_user where parentList LIKE CONCAT(u.parentList,'%')) as teamCount,")
                .append("(select sum(amount) from t_user where parentList LIKE CONCAT(u.parentList,'%')) as teamAmount ").append(" FROM t_user as u WHERE 1=1");
        List<Object> args = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sb.append(" AND account=?");
            args.add(account);
        }
        if (bonusGroup != null) {
            sb.append(" AND bonusGroupId=?");
            args.add(bonusGroup);
        }
        if ((beginRebateRatio != null) && (endRebateRatio != null)) {
            sb.append(" AND rebateRatio>=? AND rebateRatio<=?");
            args.add(beginRebateRatio);
            args.add(endRebateRatio);
        }
        if ((beginAmount != null) && (endAmount != null)) {
            sb.append(" AND amount>=? AND amount<=?");
            args.add(beginAmount);
            args.add(endAmount);
        }
        if (regTime != null) {
            Date d = DateUtils.getToDay(regTime  * -1);
            sb.append(" AND regTime>=?");
            args.add(d);
        }
        if (userType != null) {
            sb.append(" AND userType=?");
            args.add(userType);
        }
        if ((test != null) && ((test  == 0) || (test  == 1))) {
            sb.append(" AND test=?");
            args.add(test);
        }
        if (userMark != null) {
            sb.append(" AND userMark=?");
            args.add(userMark);
        }
        if ((status != null) && ((status  == 0) || (status  == 1))) {
            sb.append(" AND loginStatus=?");
            args.add(status);
        }
        pageSQL = pageSQL + sb.toString();
        pa.append(sb);
        pa.append(" ORDER BY ");
        if ((order == null) || (order  == 0)) {
            pa.append("regTime DESC");
        } else if (order  == 1) {
            pa.append("rebateRatio DESC");
        } else {
            pa.append("amount DESC");
        }
        pa.append(" LIMIT " + p.getStartIndex() + "," + p.getPageSize());

        int count = this.dbSession.getInt(pageSQL, args.toArray()) ;
        p.setRowCount(count);

        return this.dbSession.list(pa.toString(), args.toArray(), RootUser.class);
    }

    public List<Map<String, Object>> listAccountWithTeamByUser(String account, BigDecimal beginAmount, BigDecimal endAmount, Integer userType, Page p, String parentAccount, String rootAccount) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT                                                            ");
        sb.append("   u.account,                                                      ");
        sb.append("   DATE_FORMAT(u.regTime, '%Y-%m-%d %H:%i:%s') regTime,            ");
        sb.append("   DATE_FORMAT(                                                    ");
        sb.append("     u.loginTime,'%Y-%m-%d %H:%i:%s'                                           ");
        sb.append("   ) lastLogin,                                                    ");
        sb.append("   u.rebateRatio,                                                  ");

        sb.append(" (select if(((UNIX_TIMESTAMP(NOW())-IFNULL(UNIX_TIMESTAMP(onlineTime),0))/60)>5,1,2) as isOnLine from t_user_info where account =u.account) as isOnLine, ");
        sb.append("   (SELECT                                                         ");
        sb.append("     COUNT(*)                                                      ");
        sb.append("   FROM                                                            ");
        sb.append("     t_user                                                        ");
        sb.append("   WHERE parentList LIKE CONCAT(u.parentList, '%')) AS teamCount,  ");
        sb.append("   u.amount,                                                       ");
        sb.append("   (SELECT                                                         ");
        sb.append("     IFNULL(SUM(amount),0)                                                   ");
        sb.append("   FROM                                                            ");
        sb.append("     t_user                                                        ");
        sb.append("   WHERE parentList LIKE CONCAT (u.parentList,'%')) AS teamAmount, ");
        sb.append("   u.userType,                                                   ");
        sb.append("   u.parentAccount,u.niceName                                    ");
        sb.append(" FROM                                                              ");
        sb.append("   t_user u                                                       ");


        sb.append(" WHERE 1 = 1                                                       ");
        sb.append("    and u.account <> ?                      ");

        List<Object> args = new ArrayList<>();

        args.add(parentAccount);
        sb.append(" and u.loginStatus=0 ");
        if ((!StrUtils.hasEmpty(new Object[]{account})) && (!account.equals(parentAccount))) {
            sb.append(" AND u.account=? ");
            sb.append(" AND (u.parentAccount = ? or u.rootAccount = ?) ");
            args.add(account);
            args.add(parentAccount);
            args.add(rootAccount);
        } else {
            sb.append(" AND u.parentAccount = ? ");
            args.add(parentAccount);
        }
        if (beginAmount != null) {
            sb.append(" AND u.amount>=? ");
            args.add(beginAmount);
        }
        if (endAmount != null) {
            sb.append(" AND u.amount<=? ");
            args.add(endAmount);
        }
        if ((userType != null) && (userType  != 2)) {
            sb.append(" AND u.userType=?");
            args.add(userType);
        }
        sb.append(" ORDER BY u.regTime DESC ");
        return this.dbSession.listMap(sb.toString(), args.toArray(), p);
    }

    public int deleteByAccount(List<String> accounts) {
        if ((accounts == null) || (accounts.size() == 0)) {
            return 0;
        }
        String sql = null;
        if (accounts.size() == 1) {
            sql = String.format("DELETE FROM %s WHERE account=?", new Object[]{this.tableName});
        } else {
            sql = String.format("DELETE FROM %s WHERE account in(%s)", new Object[]{this.tableName, getQuestionNumber(accounts.size())});
        }
        return this.dbSession.update(sql, accounts.toArray());
    }

    public int setStatus(String account, String manager, Integer statusType, String remark, Integer loginStatus, Integer betStatus, Integer betInStatus, Integer depositStatus) {
        String parm = null;
        StringBuilder sb = new StringBuilder().append("UPDATE t_user SET freezeRemark=?,freezeDate=?,freezeAccount=?,loginStatus=?,betStatus=?,betInStatus=?,depositStatus=? WHERE ");
        if ((loginStatus == null) || (loginStatus  == 0)) {
            loginStatus = 0;
        } else {
            loginStatus = 1;
        }
        if ((betStatus == null) || (betStatus  == 0)) {
            betStatus = 0;
        } else {
            betStatus = 1;
        }
        if ((betInStatus == null) || (betInStatus  == 0)) {
            betInStatus = 0;
        } else {
            betInStatus = 1;
        }
        if (depositStatus == null) {
            depositStatus = 0;
        }
        if (statusType  == 0) {
            sb.append("account=?");
            parm = account;
        } else {
            parm = this.dbSession.getString("SELECT parentList FROM t_user WHERE account=?", new Object[]{account});
            if (statusType  == 1) {
                parm = parm + "_%";
            } else {
                parm = parm + "%";
            }
            sb.append("parentList LIKE ?");
        }
        return this.dbSession.update(sb.toString(), new Object[]{remark, new Date(), manager, loginStatus, betStatus, betInStatus, depositStatus, parm});
    }

    public int setBankGroup(String account, String bankGroup) {
        return this.dbSession.update("UPDATE t_user SET bankGroup=? WHERE account=?", new Object[]{bankGroup, account});
    }

    public int updateDailyRule(String account, Integer dailyRuleId) {
        return this.dbSession.update("UPDATE t_user SET dailyRuleId = ? WHERE account = ?", new Object[]{dailyRuleId, account});
    }

    public int deleteDailyRuleByParentList(String parentList) {
        return this.dbSession.update("UPDATE t_user SET dailyRuleId = null, dailyWagesStatus = 1 WHERE parentList LIKE ?", new Object[]{parentList + "%"});
    }

    public int deleteDailyRuleByRootAccount(String rootAccount) {
        return this.dbSession.update("UPDATE t_user SET dailyRuleId = null, dailyWagesStatus = 1 WHERE rootAccount = ?", new Object[]{rootAccount});
    }

    public int deleteDailyRuleByRuleId(Integer ruleId) {
        return this.dbSession.update("UPDATE t_user SET dailyRuleId = null, dailyWagesStatus = 1 WHERE dailyRuleId = ?", new Object[]{ruleId});
    }

    public void addDomain(String account, List<Integer> domain) {
        for (Integer n : domain) {
            this.dbSession.update("INSERT INTO t_user_domain(id,account) VALUES(?,?)", new Object[]{n, account});
        }
    }

    public int updatePassword(String account, String password) {
        return this.dbSession.update("UPDATE t_user SET password=?,passwordStatus=1 WHERE account=?", new Object[]{StrUtils.MD5(password), account});
    }

    public int updateNewPassword(String account, String password) {
        return this.dbSession.update("UPDATE t_user SET password=?,passwordStatus=1 WHERE account=?", new Object[]{password, account});
    }

    public int updatePasswordByUser(String account, String oldPass, String newPass) {
        return this.dbSession.update("update t_user set password = ?,passwordStatus = 1 where account = ? and password = ?", new Object[]{StrUtils.MD5(newPass), account, StrUtils.MD5(oldPass)});
    }

    public int updateInformation(String account, String name, String qq, String email, String phone, String message) {
        return this.dbSession.update("update t_user set niceName = ?,email=?,qq=?,phone=?,message=? where account = ?", new Object[]{name, email, qq, phone, message, account});
    }

    public int updateUserRebateRatio(String account, String parent, BigDecimal rebateRatio) {
        return this.dbSession.update("update t_user set rebateRatio = ? where account = ? and parentAccount = ?", new Object[]{rebateRatio, account, parent});
    }

    public int updateParentRebateRatioNum(String parent, double rebateRatio) {
        return this.dbSession.update("UPDATE t_user_quota SET num = num + 1 WHERE rebateRatio = ? AND account = ?", new Object[]{rebateRatio, parent});
    }

    public void updateIpInfo(User u) {
        this.dbSession.update("UPDATE t_user SET regIp=?,regIpInfo=? WHERE account=?", new Object[]{u.getRegIp(), u.getRegIpInfo(), u.getAccount()});
    }

    public int updateBankStatus(String account) {
        return this.dbSession.update("update t_user set bankStatus = '1' where account = ? and bankStatus = '0'", new Object[]{account});
    }

    public int updateBankStatus(String account, Integer status) {
        String sql = new SQLUtils(this.tableName).field("bankStatus").where("account=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{status, account});
    }

    public int updateSafePassword(String account, String password) {
        return this.dbSession.update("UPDATE t_user SET safePassword=? WHERE account=?", new Object[]{StrUtils.MD5(password), account});
    }

    public int updateNewSafePassword(String account, String password) {
        return this.dbSession.update("UPDATE t_user SET safePassword=? WHERE account=?", new Object[]{password, account});
    }

    public int updateUserSessionId(String sessionId) {
        return this.dbSession.update("update t_user set sessionId = NULL where sessionId = ?", new Object[]{sessionId});
    }

    public int updateSafePasswordByUser(String account, String oldPass, String newPass) {
        return this.dbSession.update("update t_user set safePassword = ? where account = ? and safePassword = ?", new Object[]{StrUtils.MD5(newPass), account, StrUtils.MD5(oldPass)});
    }

    public boolean setSessionId(String account, String sessionId) {
        return RedisUtils.hset("ACCOUNT_SESSION_ID", account, sessionId);
    }

    public String getSessionId(String account) {
        return RedisUtils.hget("ACCOUNT_SESSION_ID", account);
    }

    public List<User> getAccountList(Integer test) {
        StringBuffer buffer = new StringBuffer(" select * from " + this.tableName + " where test = ? and account=parentAccount ");
        String sql = buffer.toString();
        Object[] args = null;
        args = new Object[]{test};
        return this.dbSession.list(sql, args, this.cls);
    }

    public BigDecimal findMaxRebateRatioByParent(String account) {
        String sql = new SQLUtils(this.tableName).field("MAX(rebateRatio)").where("parentAccount=? AND account!=?").getSelect();
        String rel = this.dbSession.getString(sql, new Object[]{account, account});
        if (rel == null) {
            return null;
        }
        return new BigDecimal(rel);
    }

    public int setMark(String account, Integer userMark) {
        String sql = new SQLUtils(this.tableName).where("account=?").field("userMark").getUpdate();
        return this.dbSession.update(sql, new Object[]{userMark, account});
    }

    public List<User> listUserAndSelfByParent(String account) {
        String sql = new SQLUtils(this.tableName).where("account in  (select account from t_user t where  t.parentAccount=? or t.account=?) ORDER BY userType DESC ").getSelect();
        return this.dbSession.list(sql, new Object[]{account, account}, this.cls);
    }

    public List<User> getAllUserList(Integer test) {
        String sql = "select account,parentList from " + this.tableName + " where test=? ";
        return this.dbSession.list(sql, new Object[]{test}, this.cls);
    }

    public int updateOnlineTime(String account, Date date) {
        return this.dbSession.update(" UPDATE t_user_info SET onlineTime=? WHERE account=?", new Object[]{date, account});
    }

    public void saveOnLine(String account) {
        this.dbSession.update(" INSERT INTO t_user_info (account) VALUES(?)", new Object[]{account});
    }

    public int updateUserType(String account, Integer userType) {
        String sql = new SQLUtils(this.tableName).where("account=?").field("userType").getUpdate();
        return this.dbSession.update(sql, new Object[]{userType, account});
    }

    public List<User> listByCond(String account, Integer start, Integer limit) {
        String SQL_LIST = new SQLUtils(this.tableName + " t ").field("account,rebateRatio,userType,parentAccount,amount,niceName,(select if(((UNIX_TIMESTAMP(NOW())-IFNULL(UNIX_TIMESTAMP(onlineTime),0))/60)>5,1,2) as isOnLine from t_user_info where account =t.account) as isOnLine ").where("account in  (select account from t_user t where  t.parentAccount=? and t.account!=?) LIMIT ?,?").getSelect();
        return this.dbSession.list(SQL_LIST, new Object[]{account, account, start, limit}, this.cls);
    }

    public List<User> listBySelf(String account, String parentAccount, Integer start, Integer limit) {
        String SQL_LIST = new SQLUtils(this.tableName + " t ").field("account,rebateRatio,userType,parentAccount,amount,niceName,(select if(((UNIX_TIMESTAMP(NOW())-IFNULL(UNIX_TIMESTAMP(onlineTime),0))/60)>5,1,2) as isOnLine from t_user_info where account =t.account) as isOnLine ").where("account =? and parentList LIKE CONCAT((select parentList from t_user where account = ?),'%') LIMIT ?,?").getSelect();
        return this.dbSession.list(SQL_LIST, new Object[]{account, parentAccount, start, limit}, this.cls);
    }

    public int deleteByClear(SysClear m) {
        SysClearJobEnum sysClearJobEnum = SysClearJobFactory.getSysClearJobEnum(m.getJob());
        Object[] cond = {sysClearJobEnum.getObj(m), m.getDeleteMinAmount(), m.getDeleteMaxAmount()};
        if (m.getClearMode() == 2) {
            this.dbSession.update("INSERT INTO backup_" + sysClearJobEnum.getTable() + " SELECT * FROM t_user " + "WHERE loginTime <= ? AND amount >= ? AND amount <= ?", cond);
        }
        return this.dbSession.update("DELETE FROM t_user WHERE loginTime <= ? AND amount >= ? AND amount <= ?", cond);
    }

    public int deleteByFreeze(SysClear m) {
        SysClearJobEnum sysClearJobEnum = SysClearJobFactory.getSysClearJobEnum(m.getJob());
        Object[] cond = {sysClearJobEnum.getObj(m), m.getDeleteMinAmount(), m.getDeleteMaxAmount()};
        return this.dbSession.update("UPDATE t_user SET loginStatus = 1 WHERE loginTime <= ? AND amount >= ? AND amount <= ?", cond);
    }

    public int countChild(String parentAccount) {
        return this.dbSession.getInt("SELECT count(1) FROM t_user WHERE parentAccount = ? AND account != ?", new Object[]{parentAccount, parentAccount}) ;
    }

    public List<User> listClearUser(Date beforeLoginTime, BigDecimal minAmount, BigDecimal maxAmount, int start, int size) {
        return this.dbSession.list("SELECT * FROM t_user t WHERE t.loginTime < ? AND t.amount >= ? AND t.amount <= ? AND !(loginStatus = 1 && freezeAccount IS NOT NULL) ORDER BY account LIMIT ?,?",
                new Object[]{beforeLoginTime, minAmount, maxAmount, start, size}, this.cls);
    }

    public int countClearUser(Date beforeLoginTime, BigDecimal minAmount, BigDecimal maxAmount) {
        return this.dbSession.getInt("SELECT count(1) FROM t_user t WHERE t.loginTime < ? AND t.amount >= ? AND t.amount <= ? AND !(loginStatus = 1 && freezeAccount IS NOT NULL)",
                new Object[]{beforeLoginTime, minAmount, maxAmount}) ;
    }

    private void deleteBeforeFind(String sql, Object[] args) {
        String findSql = "SELECT COUNT(1) " + sql.substring(7);
        if (this.dbSession.getInt(findSql, args)  > 0) {
            this.dbSession.update(sql, args);
        }
    }

    public int deleteUserData(String account) {
        Object[] o = {account};
        Object[] o2 = {account, account};
        int i = this.dbSession.update("DELETE FROM t_user WHERE account = ?", o);
        if (i > 0) {
            deleteBeforeFind("DELETE FROM t_approval WHERE userName = ?", o);
            deleteBeforeFind("DELETE FROM t_ext_code WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_ext_code_l WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_message WHERE sender = ? OR rever = ?", o2);
            deleteBeforeFind("DELETE FROM t_message_content WHERE sender = ? OR rever = ?", o2);
            deleteBeforeFind("DELETE FROM t_bet WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_bet_backup WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_trace WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_finance_change WHERE changeUser = ?", o);
            deleteBeforeFind("DELETE FROM t_amount_change WHERE changeUser = ?", o);
            deleteBeforeFind("DELETE FROM t_recharge WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_recharge_lower WHERE sourceAccount = ?", o);
            deleteBeforeFind("DELETE FROM t_deposit WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_activity_user WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_bank_user WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_bet_change WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_bet_in WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_bet_in_rule_kill WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_bet_in_total WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_team_in_report WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_team_report WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_temp_amount_change WHERE changeUser = ?", o);
            deleteBeforeFind("DELETE FROM t_user_in_report WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_user_login_ip WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_user_notice WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_user_quota WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_user_report WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_user_safe WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_user_info WHERE account = ?", o);

            deleteBeforeFind("DELETE FROM t_contract_badrecord WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_contract_bonus WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_contract_message WHERE sender = ? OR receiver = ?", o2);
            deleteBeforeFind("DELETE FROM t_contract_rule WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_daily_acc WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_daily_data WHERE account = ?", o);
            deleteBeforeFind("DELETE FROM t_private_ratio WHERE account = ?", o);
        }
        return i;
    }

    public int updateFreeze(String account) {
        return this.dbSession.update("UPDATE t_user SET loginStatus = 1, freezeRemark='Sys Freeze', freezeDate=?, freezeAccount = null WHERE account = ?", new Object[]{new Date(), account});
    }

    public List<User> getAllUserList() {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" t.account,t.rebateRatio,t.loginTime,t.regTime,t.amount,");
        sb.append(" t.parentList,t.parentAccount,t.niceName,t.userType,");
        sb.append(" if(((UNIX_TIMESTAMP(NOW())-IFNULL(UNIX_TIMESTAMP(f.onlineTime),0))/60)>5,1,2) as isOnLine ");
        sb.append(" from t_user t,t_user_info f ");
        sb.append(" WHERE t.account = f.account ");
        String sql = sb.toString();
        return this.dbSession.list(sql, this.cls);
    }

    public List<User> getUserListByAccount(String account) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" a.account,a.rebateRatio,a.loginTime,a.regTime,a.amount,a.rootAccount,");
        sb.append(" a.parentList,a.parentAccount,a.niceName,a.userType,a.contractStatus,a.homeRemark,a.dailyWagesStatus,a.dailyRuleId,");
        sb.append(" if(((UNIX_TIMESTAMP(NOW())-IFNULL(UNIX_TIMESTAMP(b.onlineTime),0))/60)>5,1,2) as isOnLine ");
        sb.append(" from t_user a ");
        sb.append(" LEFT JOIN t_user_info b ON a.account = b.account ");
        sb.append(" WHERE a.parentList ");
        sb.append(" LIKE CONCAT(( ");
        sb.append(" SELECT parentList FROM t_user ");
        sb.append(" WHERE account = ? ),'%') ");
        sb.append(" ORDER BY a.regTime DESC ");
        String sql = sb.toString();
        return this.dbSession.list(sql, new Object[]{account}, this.cls);
    }

    public List<User> userListByParent(String account) {
        String sql = new SQLUtils(this.tableName).where("parentAccount=? and account != parentAccount ORDER BY userType DESC,rebateRatio desc,regTime").getSelect();
        return this.dbSession.list(sql, new Object[]{account}, this.cls);
    }

    public int updateHremark(String account, String homeRemark) {
        String sql = "update t_user set homeRemark = ? where account = ?";
        Object[] args = {homeRemark, account};
        return this.dbSession.update(sql, args);
    }

    public int updateAremark(String account, String adminRemark) {
        String sql = "update t_user set adminRemark = ? where account = ?";
        Object[] args = {adminRemark, account};
        return this.dbSession.update(sql, args);
    }

    public int updateContractStatus(String account, Integer contractStatus) {
        String sql = new SQLUtils(this.tableName).where("account=?").field("contractStatus").getUpdate();
        return this.dbSession.update(sql, new Object[]{contractStatus, account});
    }

    public int updateDailyWagesStatusOpen(String account, String parentAccount) {
        return this.dbSession.update("UPDATE t_user SET dailyWagesStatus = 0 WHERE account = ? AND parentAccount = ?", new Object[]{account, parentAccount});
    }

    public int updateDailyWagesStatusClose(String parentList) {
        return this.dbSession.update("UPDATE t_user SET dailyWagesStatus = 1 WHERE parentList LIKE ?", new Object[]{parentList + "%"});
    }

    public List<User> listDailyWagesRoot() {
        return this.dbSession.list("SELECT * FROM t_user WHERE account = parentAccount AND dailyWagesStatus = 0", this.cls);
    }

    public List<User> listDailyWagesLower(String account) {
        return this.dbSession.list("SELECT * FROM t_user WHERE parentAccount = ? AND parentAccount != account AND dailyWagesStatus = 0", new Object[]{account}, this.cls);
    }

    public int updatePrivateRebate(String account, Integer privateRebate) {
        return this.dbSession.update("update t_user set privateRebate = ? where account = ?", new Object[]{privateRebate, account});
    }

    public List<User> listByPrivateRatioNot(int privateRebate) {
        String sql = new SQLUtils(this.tableName).where("privateRebate !=?").getSelect();
        return this.dbSession.list(sql, new Object[]{privateRebate}, this.cls);
    }

    public int updateLoginStatusAndErrorCount(String account, Date loginTime, int errorCount, int status, String remark) {
        if (loginTime != null) {
            return this.dbSession.update("UPDATE t_user SET loginTime=?,loginErrorCount=?,loginStatus=?,freezeRemark=? WHERE account=?", new Object[]{loginTime, errorCount, status, remark, account});
        }
        return this.dbSession.update("UPDATE t_user SET loginErrorCount=?,loginStatus=?,freezeRemark=? WHERE account=?", new Object[]{errorCount, status, remark, account});
    }

    //**************************************以下为变更部分*****************************************

    public int updateUserData(User m) {
        String sql = new SQLUtils(this.tableName).field("image,niceName,sex,birthday").where("account=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{
                m.getImage(),
                m.getNiceName(),
                m.getSex(),
                m.getBirthday(),
                m.getAccount()});
    }

    public int updateUserPhone(User m) {
        String sql = new SQLUtils(this.tableName).field("phone").where("account=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{
                m.getPhone(),
                m.getAccount()});
    }

    public int updateUserEmail(User m) {
        String sql = new SQLUtils(this.tableName).field("email").where("account=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{
                m.getEmail(),
                m.getAccount()});
    }

    public int updatePasswordZ(String account, String password) {
        return this.dbSession.update("UPDATE t_user SET password=?,passwordStatus=1 WHERE account=?", new Object[]{password, account});
    }

    public int updateSafePasswordZ(String account, String safePassword) {
        return this.dbSession.update("UPDATE t_user SET safePassword=? WHERE account=?", new Object[]{safePassword, account});
    }


    public List<Map<String, Object>> underUserList_z(String account) {
        String sql = "SELECT u.account, u.userType, (select count(*) from t_user where parentList LIKE CONCAT(u.parentList,'%')) as teamCount, (select count(*) FROM t_user WHERE parentAccount=u.account AND account!=u.account) as childCount, date_format(u.loginTime,'%Y-%c-%d %h:%i:%s') as loginTime, u.rebateRatio FROM t_user as u WHERE ";
        sql = sql + " u.parentAccount=? and u.account!=? ORDER BY u.account";
        Object[] args = {account, account};
        return this.dbSession.listMap(sql, args, null);
    }

    public int underUserListCount(String account) {
        String sql = "SELECT count(*) FROM t_user as u WHERE ";
        sql = sql + " u.parentAccount=? and u.account!=? ORDER BY u.loginTime DESC,u.regchargeAmount DESC";
        Object[] args = {account, account};
        return this.dbSession.getInt(sql, args);
    }

    public List<UnderUser> underUserListForLevelReport(String account, Integer start, Integer limit) {
        String sql = "SELECT u.account, u.userType, (select count(*) from t_user where parentList LIKE CONCAT(u.parentList,'%')) as teamCount, (select count(*) FROM t_user WHERE parentAccount=u.account AND account!=u.account) as childCount, date_format(u.loginTime,'%Y-%c-%d %h:%i:%s') as loginTime, u.rebateRatio FROM t_user as u WHERE ";
        sql = sql + " u.parentAccount=? and u.account!=? and u.test=0 ORDER BY u.loginTime DESC,u.regchargeAmount DESC ";
        List<Object> args = new ArrayList<Object>();
        args.add(account);
        args.add(account);

        String limitStr = "";
        if (!StrUtils.hasEmpty(new Object[]{start}) && !StrUtils.hasEmpty(new Object[]{limit})) {
            limitStr = " LIMIT ?,? ";
            args.add(start);
            args.add(limit);
        }
        sql = sql + limitStr;
        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, UnderUser.class);
    }


    public List<UnderUser> underUserListForLevelReportZ(String account) {
        String sql = "SELECT u.account, u.userType FROM t_user as u WHERE ";
        sql = sql + " u.parentAccount=? and u.account!=? and u.test=0 ";
        List<Object> args = new ArrayList<Object>();
        args.add(account);
        args.add(account);

        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, UnderUser.class);
    }


    public List<Map<String, Object>> underUserList(String account) {
        String sql = "SELECT u.account, u.userType, (select count(*) from t_user where parentList LIKE CONCAT(u.parentList,'%')) as teamCount, (select count(*) FROM t_user WHERE parentAccount=u.account AND account!=u.account) as childCount, date_format(u.loginTime,'%Y-%c-%d %h:%i:%s') as loginTime, u.rebateRatio FROM t_user as u WHERE ";
        sql = sql + " u.parentList LIKE CONCAT((SELECT us.parentList from t_user as us where us.account=?),'%') and u.account!=?  ORDER BY  u.parentList,u.account";

        Object[] args = {account, account};
        return this.dbSession.listMap(sql, args, null);
    }

    public List<User> userTeamList(String account) {
        String sql = "SELECT u.account, u.userType, u.rebateRatio FROM t_user as u WHERE ";
        sql = sql + " u.parentList LIKE CONCAT((SELECT us.parentList from t_user as us where us.account=?),'%') ORDER BY  u.parentList,u.account";

        return this.dbSession.list(sql, new Object[]{account}, this.cls);
    }


    public List<Map<String, Object>> getTeamInfoByAccount(String account) {
        String sql = "SELECT (select count(*) from t_user where parentList LIKE CONCAT(u.parentList,'%')) as teamCount, (select sum(amount) FROM t_user WHERE parentList LIKE CONCAT(u.parentList,'%')) as teamAmount  FROM t_user as u WHERE ";
        sql = sql + "u.account=? ";
        Object[] args = {account};
        return this.dbSession.listMap(sql, args, null);
    }


    public List<Map<String, Object>> getTeamInfoByAccountAndTime(String account, Date startTime, Date endTime) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT (select count(*) from t_user where parentList LIKE CONCAT(u.parentList,'%')) as teamCount, ";
        sql = sql + " (select sum(amount) FROM t_user WHERE parentList LIKE CONCAT(u.parentList,'%')) as teamAmount, ";
        sql = sql + " (select count(*) FROM t_user WHERE parentAccount=u.account AND account!=u.account) as childCount, ";
        sql = sql + " (select count(*) from t_user where parentList LIKE CONCAT(u.parentList,'%') ";
        if (startTime != null) {
            sql = sql + " AND regTime >= ?";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND regTime <= ?";
            args.add(endTime);
        }
        sql = sql + "  ) as regTeamCount, ";

        sql = sql + " (select count(*) FROM t_user WHERE parentAccount=u.account AND account!=u.account ";
        if (startTime != null) {
            sql = sql + " AND regTime >= ?";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND regTime <= ?";
            args.add(endTime);
        }
        sql = sql + "  ) as regChildCount ";

        sql = sql + " FROM t_user as u WHERE 1=1 ";
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " and u.account=? ";
            args.add(account);
        }
        //Object[] args = { account };
        Object[] arg = args.toArray();
        return this.dbSession.listMap(sql, arg, null);
    }


    //首充人数
    public List<Map<String, Object>> rechargeFirstUser(String account, Date startTime, Date endTime) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT account ";
        sql = sql + " FROM t_user WHERE 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND rechargeFirstTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND rechargeFirstTime <= ? ";
            args.add(endTime);
        }
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND account in (SELECT u.account FROM t_user as u WHERE u.parentList LIKE CONCAT((SELECT us.parentList from t_user as us where us.account=?),'%')) ";
            args.add(account);
        }

        sql = sql + " order by regTime desc";

        Object[] arg = args.toArray();
        return this.dbSession.listMap(sql, arg, null);
    }

    //注册人数
    public List<Map<String, Object>> regUser(String account, Date startTime, Date endTime) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT account ";
        sql = sql + " FROM t_user WHERE 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND regTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND regTime <= ? ";
            args.add(endTime);
        }
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND account in (SELECT u.account FROM t_user as u WHERE u.parentList LIKE CONCAT((SELECT us.parentList from t_user as us where us.account=?),'%')) ";
            args.add(account);
        }

        sql = sql + " order by regTime desc";

        Object[] arg = args.toArray();
        return this.dbSession.listMap(sql, arg, null);
    }

    //会员余额
    public Map<String, Object> amountUser(String account) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT  sum(amount) as allAmount ";
        sql = sql + " FROM t_user WHERE 1=1 and test = 0 ";
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND account in (SELECT u.account FROM t_user as u WHERE u.parentList LIKE CONCAT((SELECT us.parentList from t_user as us where us.account=?),'%')) ";
            args.add(account);
        }
        Object[] arg = args.toArray();
        return this.dbSession.getMap(sql, arg);
    }


    //首充报表用到
    public List<User> rechargeFirstUser(String account, String parentAccount, Integer sorting, Date startTime, Date endTime, Page p) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT account,parentAccount,rootAccount,userType,amount,rechargeFirstTime,rechargeFirstAmount,regTime ";
        sql = sql + " FROM t_user WHERE 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND rechargeFirstTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND rechargeFirstTime <= ? ";
            args.add(endTime);
        }
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND account = ? ";
            args.add(account);
        }
        if (!StrUtils.hasEmpty(new Object[]{parentAccount})) {
            sql = sql + " AND parentAccount = ? ";
            args.add(parentAccount);
        }

        if (!StrUtils.hasEmpty(new Object[]{sorting}) && sorting == 2 ) {
            sql = sql + " order by rechargeFirstAmount desc ";
        }else {
            sql = sql + " order by regTime desc ";
        }

        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, this.cls, p);
    }

    /**
     * 会员报表用到
     *
     * @param account
     * @param parentAccount
     * @param sorting       排序标记，0：投注递减、1：盈利递减、2：盈利递增、3：盈率递减、4：盈率递增、5：入款递减、6：出款递减、7：返点递减、8：活动递减
     * @param startTime
     * @param endTime
     * @param p
     * @return
     */
    public List<Map<String, Object>> membershipReport(String account, String parentAccount, Integer sorting, Date startTime, Date endTime, Page p) {
        List<Object> args = new ArrayList<Object>();
        String sql = "select u.account,u.parentAccount,u.rootAccount,u.amount,DATE_FORMAT(u.regTime,'%Y-%m-%d %H:%i:%s') as regTime, ";

        sql = sql + "  (ifnull(((select sum(changeAmount) from t_finance_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and changeUser = u.account and accountChangeTypeId in (12,11,13,21))),0)) as rechargeAmount, ";

        sql = sql + " (select count(*) from t_finance_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and changeUser = u.account and accountChangeTypeId in (12,11,13,21)) as rechargeAmountNum, ";

        sql = sql + "  (ifnull(((select sum(changeAmount) from t_finance_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and changeUser = u.account and accountChangeTypeId in (18,52))),0)) as withdrawAmount, ";

        sql = sql + " (select count(*) from t_finance_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and changeUser = u.account and accountChangeTypeId in (18,52)) as withdrawAmountNum, ";

        sql = sql + "  (ifnull(((select sum(changeAmount) from t_finance_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and changeUser = u.account and accountChangeTypeId = 19)),0)) as activityAmount, ";

        sql = sql + "  (ifnull(((select sum(amount) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account = u.account and status in (1,2,0,6))),0)) as betAmount, ";

        sql = sql + "  (ifnull(((select sum(win) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account = u.account and status = 1)),0)) as winningAmount, ";

        /*sql = sql + " (select sum(changeAmount) from t_amount_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and betId in (select id from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and status in (1,2) AND account = u.account) and accountChangeTypeId = 3) as rebateAmount, ";*/


        sql = sql + " (ifnull(((select sum(changeAmount) from t_amount_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and betId in (select id from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and status in (1,2) AND account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%'))) and changeUser = u.account and accountChangeTypeId = 3)),0)) as agentRebateAmount ";


        /*sql = sql + " -(ifnull((select sum(amount) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account = u.account and status in (1,2,0,6)),0) - ifnull((select sum(win) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account = u.account and status = 1),0) - ifnull((select sum(changeAmount) from t_amount_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and betId in (select id from t_bet where status in (1,2) AND account = u.account) and accountChangeTypeId = 3),0)) as profitAndLoss, ";*/

        /*sql = sql + " -((ifnull((select sum(amount) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account = u.account and status in (1,2,0,6)),0) - ifnull((select sum(win) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account = u.account and status = 1),0) - ifnull((select sum(changeAmount) from t_amount_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and betId in (select id from t_bet where status in (1,2) AND account = u.account) and accountChangeTypeId = 3),0)) / ifnull((select sum(amount) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account = u.account and status in (1,2,0,6)),0) ) as grossRate, ";*/

        /*sql = sql + " -((ifnull((select sum(amount) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and status in (1,2,0,6)),0) - ifnull((select sum(win) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and status = 1),0) - ifnull((select sum(changeAmount) from t_amount_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and betId in (select id from t_bet where status in (1,2) AND account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')))  and changeUser in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%'))  and accountChangeTypeId = 3),0) - ifnull((select sum(changeAmount) from t_finance_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and changeUser in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and accountChangeTypeId = 19),0))) as profit,  ";*/

        /*sql = sql + " -((ifnull((select sum(amount) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account = u.account and status in (1,2,0,6)),0) - ifnull((select sum(win) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account = u.account and status = 1),0) - ifnull((select sum(changeAmount) from t_amount_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and betId in (select id from t_bet where status in (1,2) AND account = u.account) and accountChangeTypeId = 3),0) - ifnull((select sum(changeAmount) from t_finance_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and changeUser = u.account and accountChangeTypeId = 19),0)) / ifnull((select sum(amount) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account = u.account and status in (1,2,0,6)),0) ) as earningsRatio ";*/

        sql = sql + " from t_user as u where 1 = 1 and u.test = 0 ";

        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND u.account = ? ";
            args.add(account);
        }
        if (!StrUtils.hasEmpty(new Object[]{parentAccount})) {
            sql = sql + " AND u.parentAccount = ? ";
            args.add(parentAccount);
        }

        /*if (!StrUtils.hasEmpty(new Object[]{sorting})) {
            if (sorting == 1) {//1：盈利递减
                sql = sql + " order by profit desc ";
            } else if (sorting == 2) {//2：盈利递增
                sql = sql + " order by profit ";
            } else if (sorting == 3) {//3：盈率递减
                sql = sql + " order by earningsRatio desc ";
            } else if (sorting == 4) {//4：盈率递增
                sql = sql + " order by earningsRatio ";
            } else if (sorting == 5) {//5：入款递减
                sql = sql + " order by rechargeAmount desc ";
            } else if (sorting == 6) {//6：出款递减
                sql = sql + " order by withdrawAmount desc ";
            } else if (sorting == 7) {//7：返点递减
                sql = sql + " order by rebateAmount desc ";
            } else if (sorting == 8) {//8：活动递减
                sql = sql + " order by activityAmount desc ";
            } else {//0：投注递减
                sql = sql + " order by betAmount desc ";
            }
        }*/

        sql = sql + " order by betAmount desc ";

        Object[] arg = args.toArray();
        return this.dbSession.listMap(sql, arg, p);
    }


    /**
     * 新会员报表
     *
     * @param accountArr    会员账户数组
     * @param parentAccount 上级账户
     * @param sorting       排序标记，0：投注递减、1：盈利递减、2：盈利递增、3：盈率递减、4：盈率递增、5：充值递减、6：提现递减、7：返点递减、8：活动递减
     * @param isBlurry      是否模糊查询，0：精确查询、1：模糊查询
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param p             分页对象
     * @return
     */
    public List<MembershipReport> membershipReport_Z(String[] accountArr, String parentAccount, Integer sorting, Integer isBlurry, Date startTime, Date endTime, Page p) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT ";
        sql = sql + " ue.account, ";
        sql = sql + " ue.parentAccount, ";
        sql = sql + " ue.rootAccount, ";
        sql = sql + " ue.amount, ";
        sql = sql + " ue.regTime, ";
        sql = sql + " ue.rechargeAmount, ";
        sql = sql + " ue.rechargeAmountNum, ";
        sql = sql + " ue.withdrawAmount, ";
        sql = sql + " ue.withdrawAmountNum, ";
        sql = sql + " ue.activityAmount, ";
        sql = sql + " ue.dailyAmount, ";
        sql = sql + " ue.dividendAmount, ";
        sql = sql + " ue.betAmount, ";
        sql = sql + " ue.winningAmount, ";
        sql = sql + " ue.rebateAmount, ";
        sql = sql + " (ue.winningAmount + ue.rebateAmount - ue.betAmount) as profitAndLoss, ";
        sql = sql + " ifnull((((ue.winningAmount + ue.rebateAmount - ue.betAmount)/ue.betAmount)*100),0) as grossRate, ";
        sql = sql + " ((ue.winningAmount + ue.rebateAmount - ue.betAmount) + ue.activityAmount + ue.dailyAmount + ue.dividendAmount) as profit, ";
        sql = sql + " ifnull(((((ue.winningAmount + ue.rebateAmount - ue.betAmount) + ue.activityAmount + ue.dailyAmount + ue.dividendAmount)/ue.betAmount)*100),0) as earningsRatio ";
        sql = sql + "  from ";
        sql = sql + " ( ";
        sql = sql + " select  ";
        sql = sql + "  u.account, ";
        sql = sql + "  u.parentAccount, ";
        sql = sql + "  u.rootAccount, ";
        sql = sql + "  u.amount, ";
        sql = sql + "  u.regTime, ";
        sql = sql + "  ifnull(fc.rechargeAmount,0.00) as rechargeAmount, ";
        sql = sql + "  ifnull(fc.rechargeAmountNum,0) as rechargeAmountNum, ";
        sql = sql + "  ifnull(fc.withdrawAmount,0.00) as withdrawAmount, ";
        sql = sql + "  ifnull(fc.withdrawAmountNum,0) as withdrawAmountNum, ";
        sql = sql + "  ifnull(fc.activityAmount,0.00) as activityAmount, ";
        sql = sql + "  ifnull(fc.dailyAmount,0.00) as dailyAmount, ";
        sql = sql + "  ifnull(fc.dividendAmount,0.00) as dividendAmount, ";
        sql = sql + "  (ifnull(ac.rebateAmount,0.00) - ifnull(ac.withdrawalSysAmount,0.00)) as rebateAmount, ";
        sql = sql + "  (ifnull(ac.winningAmount,0.00) - ifnull(ac.cancelAmount,0.00)) as winningAmount, ";
        sql = sql + "  (ifnull(ac.betAmount,0.00) - ifnull(ac.wofwAmount,0.00)) as betAmount ";
        sql = sql + "  from  ";
        sql = sql + " ( ";
        sql = sql + " select account,parentAccount,rootAccount,amount,DATE_FORMAT(regTime,'%Y-%m-%d %H:%i:%s') as regTime from t_user where 1 = 1 ";

        //sql = sql + " and account = '' and parentAccount = ''  ";

        if(!StrUtils.hasEmpty(new Object[]{accountArr})){
            if((!StrUtils.hasEmpty(new Object[]{isBlurry})) && isBlurry == 1){
                sql = sql + " AND account REGEXP '";
                for(int i = 0; i<accountArr.length; i++){
                    if(i == (accountArr.length - 1)) {
                        sql = sql + accountArr[i];
                    }else {
                        sql = sql + accountArr[i] + "|";
                    }
                }
                sql = sql + "' ";
            }else{
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
        }

        if (!StrUtils.hasEmpty(new Object[]{parentAccount})) {
            sql = sql + " AND parentAccount = ? ";
            args.add(parentAccount);
        }

        if (StrUtils.hasEmpty(new Object[]{accountArr}) && StrUtils.hasEmpty(new Object[]{parentAccount})) {
            sql = sql + " and account in ( ";
            sql = sql + " select DISTINCT changeUser from  ";
            sql = sql + " ( ";
            sql = sql + " SELECT changeUser from t_amount_change  ";
            sql = sql + " where 1=1 ";
            //sql = sql + " and changeTime >= '2018-08-01 00:00:00' and changeTime <= '2018-08-20 23:59:59' ";
            if (startTime != null) {
                sql = sql + " AND changeTime >= ? ";
                args.add(startTime);
            }
            if (endTime != null) {
                sql = sql + " AND changeTime <= ? ";
                args.add(endTime);
            }
            sql = sql + " UNION ALL  ";
            sql = sql + " SELECT changeUser from t_finance_change  ";
            sql = sql + " where 1=1  ";
            //sql = sql + " and changeTime >= '2018-08-01 00:00:00' and changeTime <= '2018-08-20 23:59:59' ";
            if (startTime != null) {
                sql = sql + " AND changeTime >= ? ";
                args.add(startTime);
            }
            if (endTime != null) {
                sql = sql + " AND changeTime <= ? ";
                args.add(endTime);
            }
            sql = sql + " ) as a  ";
            sql = sql + " ) ";
        }

        sql = sql + "  and test = 0 ) as u ";
        sql = sql + "  left join ";
        sql = sql + " ( ";
        sql = sql + " select  ";
        sql = sql + "  changeUser, ";
        sql = sql + "  sum(IF(accountChangeTypeId in (12,11,13,21), changeAmount, 0)) as rechargeAmount, ";
        sql = sql + "  count(IF(accountChangeTypeId in (12,11,13,21), TRUE, NULL)) as rechargeAmountNum, ";
        sql = sql + "  -(sum(IF(accountChangeTypeId = 18, changeAmount, 0))) as withdrawAmount, ";
        sql = sql + "  count(IF(accountChangeTypeId = 18, TRUE, NULL)) as withdrawAmountNum, ";
        sql = sql + "  sum(IF(accountChangeTypeId = 19, changeAmount, 0)) as activityAmount, ";
        sql = sql + "  sum(IF(accountChangeTypeId = 28, changeAmount, 0)) as dailyAmount, ";
        sql = sql + "  sum(IF(accountChangeTypeId = 30, changeAmount, 0)) as dividendAmount ";
        sql = sql + "  from t_finance_change  ";
        sql = sql + "  where 1=1 ";

        //sql = sql + " and changeTime >= '2018-07-15 00:00:00' and changeTime <= '2018-07-15 23:59:59'   ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }

        //sql = sql + "  and changeUser = '' ";
        if(!StrUtils.hasEmpty(new Object[]{accountArr})){
            if((!StrUtils.hasEmpty(new Object[]{isBlurry})) && isBlurry == 1){
                sql = sql + " AND changeUser REGEXP '";
                for(int i = 0; i<accountArr.length; i++){
                    if(i == (accountArr.length - 1)) {
                        sql = sql + accountArr[i];
                    }else {
                        sql = sql + accountArr[i] + "|";
                    }
                }
                sql = sql + "' ";
            }else{
                sql = sql + " AND changeUser in (";
                for(int i = 0; i<accountArr.length; i++){
                    if(i == (accountArr.length - 1)) {
                        sql = sql + "'" + accountArr[i] + "'";
                    }else {
                        sql = sql + "'" + accountArr[i] + "'" + ",";
                    }
                }
                sql = sql + ") ";
            }
        }


        //sql = sql + "  and changeUser in (select account FROM t_user WHERE parentAccount='' AND account!='') ";
        if (!StrUtils.hasEmpty(new Object[]{parentAccount})) {
            sql = sql + "  and changeUser in (select account FROM t_user WHERE parentAccount= ? AND account!= ? ) ";
            args.add(parentAccount);
            args.add(parentAccount);
        }

        sql = sql + "  and test = 0 ";
        sql = sql + "  group by changeUser ";
        sql = sql + " ) as fc on u.account = fc.changeUser ";
        sql = sql + "  left join ";
        sql = sql + " ( ";
        sql = sql + " select  ";
        sql = sql + "  changeUser, ";
        sql = sql + "  sum(IF(accountChangeTypeId = 3, changeAmount, 0)) as rebateAmount, ";
        sql = sql + "  sum(IF(accountChangeTypeId = 4, changeAmount, 0)) as winningAmount, ";
        sql = sql + "  sum(IF(accountChangeTypeId = 7, changeAmount, 0)) as wofwAmount, ";
        sql = sql + "  -(sum(IF(accountChangeTypeId = 1, changeAmount, 0))) as betAmount, ";
        sql = sql + "  -(sum(IF(accountChangeTypeId = 9, changeAmount, 0))) as withdrawalSysAmount, ";
        sql = sql + "  -(sum(IF(accountChangeTypeId = 10, changeAmount, 0))) as cancelAmount ";
        sql = sql + "  from t_amount_change  ";
        sql = sql + "  where 1=1  ";

        //sql = sql + " and changeTime >= '2018-07-15 00:00:00' and changeTime <= '2018-07-15 23:59:59'  ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }

        //sql = sql + "  and changeUser = '' ";
        if(!StrUtils.hasEmpty(new Object[]{accountArr})){
            if((!StrUtils.hasEmpty(new Object[]{isBlurry})) && isBlurry == 1){
                sql = sql + " AND changeUser REGEXP '";
                for(int i = 0; i<accountArr.length; i++){
                    if(i == (accountArr.length - 1)) {
                        sql = sql + accountArr[i];
                    }else {
                        sql = sql + accountArr[i] + "|";
                    }
                }
                sql = sql + "' ";
            }else{
                sql = sql + " AND changeUser in (";
                for(int i = 0; i<accountArr.length; i++){
                    if(i == (accountArr.length - 1)) {
                        sql = sql + "'" + accountArr[i] + "'";
                    }else {
                        sql = sql + "'" + accountArr[i] + "'" + ",";
                    }
                }
                sql = sql + ") ";
            }
        }

        //sql = sql + "  and changeUser in (select account FROM t_user WHERE parentAccount='' AND account!='') ";
        if (!StrUtils.hasEmpty(new Object[]{parentAccount})) {
            sql = sql + "  and changeUser in (select account FROM t_user WHERE parentAccount= ? AND account!= ? ) ";
            args.add(parentAccount);
            args.add(parentAccount);
        }

        sql = sql + "  and test = 0 ";
        sql = sql + "  group by changeUser ";
        sql = sql + " ) as ac on u.account = ac.changeUser ";
        sql = sql + " ) as ue  ";

        if (!StrUtils.hasEmpty(new Object[]{sorting})) {
            if (sorting == 1) {//1：盈利递减
                sql = sql + " order by profit desc ";
            } else if (sorting == 2) {//2：盈利递增
                sql = sql + " order by profit ";
            } else if (sorting == 3) {//3：盈率递减
                sql = sql + " order by earningsRatio desc ";
            } else if (sorting == 4) {//4：盈率递增
                sql = sql + " order by earningsRatio ";
            } else if (sorting == 5) {//5：充值递减
                sql = sql + " order by ue.rechargeAmount desc ";
            } else if (sorting == 6) {//6：提现递减
                sql = sql + " order by ue.withdrawAmount desc ";
            } else if (sorting == 7) {//7：返点递减
                sql = sql + " order by ue.rebateAmount desc ";
            } else if (sorting == 8) {//8：活动递减
                sql = sql + " order by ue.activityAmount desc ";
            } else {//0：投注递减
                sql = sql + " order by ue.betAmount desc ";
            }
        }

        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, MembershipReport.class, p);
    }




    public Integer updateAmount_Z(String account, BigDecimal amount) {
        Object[] args = {amount, amount, amount, account};
        return this.dbSession.update("UPDATE t_user SET amount=amount+?,totalRebate=ifnull(totalRebate,0.00)+?,retrievableRebate=ifnull(retrievableRebate,0.00)+? WHERE account=?", args);
    }

    public Integer updateAmount_R(String account, BigDecimal amount) {
        Object[] args = {amount, amount, account};
        return this.dbSession.update("UPDATE t_user SET amount=amount+?,retrievableRebate=ifnull(retrievableRebate,0.00)+? WHERE account=?", args);
    }

    public Integer updateAmount_RZ(String account, BigDecimal amount, BigDecimal retrievableRebate) {
        Object[] args = {amount, retrievableRebate, account};
        return this.dbSession.update("UPDATE t_user SET amount=amount+?,retrievableRebate=ifnull(retrievableRebate,0.00)+? WHERE account=?", args);
    }

    /**
     * 代理报表
     *
     * @param account
     * @param startTime
     * @param endTime
     * @param p
     * @return
     */
    public List<Map<String, Object>> agentsReport(String account, Date startTime, Date endTime, Integer userType, Page p) {
        List<Object> args = new ArrayList<Object>();
        String sql = "select u.account,u.parentAccount,u.rootAccount,DATE_FORMAT(u.regTime,'%Y-%m-%d %H:%i:%s') as regTime,u.userType, ";

        sql = sql + " (select sum(amount) from t_user where account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%'))) as teamAmount, ";

        sql = sql + " (select count(*) from t_user where account in (SELECT uz.account FROM t_user as uz WHERE uz.parentAccount = u.account)) as lowerCount, ";

        sql = sql + " (select count(*) as regC from t_user where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND regTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND regTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%'))) as regCount, ";

        sql = sql + " (select count(DISTINCT account) as betPerC from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and status in (1,2,0,6)) as betPerCount,  ";

        sql = sql + " (select count(*) as regC from t_user where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND rechargeFirstTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND rechargeFirstTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%'))) as firstChargeCount, ";

        sql = sql + " (select count(DISTINCT changeUser) as changeUserNum from t_finance_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId in (12,11,13,21) and changeUser in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%'))) as chargeCount, ";


        sql = sql + " (ifnull(((select sum(amount) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and status in (1,2,0,6))),0)) as betAmount,  ";

        sql = sql + " (ifnull(((select sum(win) from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and status = 1)),0)) as winningAmount,  ";

        sql = sql + " (ifnull(((select sum(changeAmount) from t_finance_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and changeUser in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and accountChangeTypeId = 19)),0)) as activityAmount,  ";

        sql = sql + " (ifnull(((select sum(changeAmount) from t_amount_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and betId in (select id from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and status in (1,2) AND account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%'))) and changeUser in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and accountChangeTypeId = 3)),0)) as teamRebateAmount,  ";

        sql = sql + " (ifnull(((select sum(changeAmount) from t_finance_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and changeUser in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and accountChangeTypeId in (12,11,13,21))),0)) as rechargeAmount,  ";

        sql = sql + " (ifnull(((select sum(changeAmount) from t_finance_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and changeUser in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and accountChangeTypeId in (18,52))),0)) as withdrawAmount, ";


        /*if (!StrUtils.hasEmpty(new Object[]{account}) && !StrUtils.hasEmpty(new Object[]{userType}) && userType == 0) {
            sql = sql + " -(ifnull((select sum(amount) from t_bet where 1=1 ";
            if (startTime != null) {
                sql = sql + " AND createTime >= ? ";
                args.add(startTime);
            }
            if (endTime != null) {
                sql = sql + " AND createTime <= ? ";
                args.add(endTime);
            }
            sql = sql + " and account = u.account and status in (1,2,0,6)),0) - ifnull((select sum(win) from t_bet where 1=1 ";
            if (startTime != null) {
                sql = sql + " AND createTime >= ? ";
                args.add(startTime);
            }
            if (endTime != null) {
                sql = sql + " AND createTime <= ? ";
                args.add(endTime);
            }
            sql = sql + " and account = u.account and status = 1),0) - ifnull((select sum(changeAmount) from t_amount_change where 1=1 ";
            if (startTime != null) {
                sql = sql + " AND changeTime >= ? ";
                args.add(startTime);
            }
            if (endTime != null) {
                sql = sql + " AND changeTime <= ? ";
                args.add(endTime);
            }
            sql = sql + " and betId in (select id from t_bet where status in (1,2) AND account = u.account) and accountChangeTypeId = 3),0) - ifnull((select sum(changeAmount) from t_finance_change where 1=1 ";
            if (startTime != null) {
                sql = sql + " AND changeTime >= ? ";
                args.add(startTime);
            }
            if (endTime != null) {
                sql = sql + " AND changeTime <= ? ";
                args.add(endTime);
            }
            sql = sql + " and changeUser = u.account and accountChangeTypeId = 19),0)) as profit, ";
        }else {*/
            sql = sql + " -((ifnull((select sum(amount) from t_bet where 1=1 ";
            if (startTime != null) {
                sql = sql + " AND createTime >= ? ";
                args.add(startTime);
            }
            if (endTime != null) {
                sql = sql + " AND createTime <= ? ";
                args.add(endTime);
            }
            sql = sql + " and account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and status in (1,2,0,6)),0) - ifnull((select sum(win) from t_bet where 1=1 ";
            if (startTime != null) {
                sql = sql + " AND createTime >= ? ";
                args.add(startTime);
            }
            if (endTime != null) {
                sql = sql + " AND createTime <= ? ";
                args.add(endTime);
            }
            sql = sql + " and account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and status = 1),0) - ifnull((select sum(changeAmount) from t_amount_change where 1=1 ";
            if (startTime != null) {
                sql = sql + " AND changeTime >= ? ";
                args.add(startTime);
            }
            if (endTime != null) {
                sql = sql + " AND changeTime <= ? ";
                args.add(endTime);
            }
            sql = sql + " and betId in (select id from t_bet where status in (1,2) AND account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')))  and changeUser in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%'))  and accountChangeTypeId = 3),0) - ifnull((select sum(changeAmount) from t_finance_change where 1=1 ";
            if (startTime != null) {
                sql = sql + " AND changeTime >= ? ";
                args.add(startTime);
            }
            if (endTime != null) {
                sql = sql + " AND changeTime <= ? ";
                args.add(endTime);
            }
            sql = sql + " and changeUser in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%')) and accountChangeTypeId = 19),0))) as profit,  ";
        //}



        sql = sql + " (ifnull(((select sum(changeAmount) from t_amount_change where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and betId in (select id from t_bet where 1=1 ";
        if (startTime != null) {
            sql = sql + " AND createTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and status in (1,2) AND account in (SELECT uz.account FROM t_user as uz WHERE uz.parentList LIKE CONCAT((SELECT ux.parentList from t_user as ux where ux.account=u.account),'%'))) and changeUser = u.account and accountChangeTypeId = 3)),0)) as agentRebateAmount ";

        sql = sql + " from t_user as u where 1 = 1 and u.test = 0 ";

        if (!StrUtils.hasEmpty(new Object[]{userType}) && userType == 1) {
            sql = sql + " and u.userType = 1 ";
        }

        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND u.account = ? ";
            args.add(account);
        }

        sql = sql + " ORDER BY regTime desc ";

        Object[] arg = args.toArray();
        return this.dbSession.listMap(sql, arg, p);
    }


    public Integer updateDeposit_Z(String account, BigDecimal amount, BigDecimal depositAmount, Integer depositCount) {
        return this.dbSession.update("UPDATE t_user SET amount=amount+?,depositRebateAmount=ifnull(depositRebateAmount,0.00)+?,depositRebateCount=ifnull(depositRebateCount,0)+?,withdrawalTimes=withdrawalTimes+? WHERE account=?", new Object[]{amount, depositAmount, depositCount, depositCount, account});
    }


    /**
     * 用户可提现余额报表
     *
     * @param account
     * @param p
     * @return
     */
    public List<Map<String, Object>> cashWithdrawalBalanceReport(String account, Page p) {
        List<Object> args = new ArrayList<Object>();
        String sql = "select u.account,u.parentAccount,u.rootAccount,DATE_FORMAT(u.regTime,'%Y-%m-%d %H:%i:%s') as regTime,u.amount, ";

        sql = sql + " (ifnull((select afc.remainAmount from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),0.00)) as lastRemainAmount,";

        sql = sql + " DATE_FORMAT((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),'%Y-%m-%d %H:%i:%s') as lastCreateTime,";

        //sql = sql + " -(ifnull((select sum(changeAmount) from t_amount_change where 1 = 1 and changeTime >= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1)) and changeUser = u.account and accountChangeTypeId = 1),0.00)) as betDeduction,";

        //sql = sql + " (ifnull((select sum(changeAmount) from t_amount_change where 1 = 1 and changeTime >= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1)) and changeUser = u.account and accountChangeTypeId = 4),0.00)) as bonusDelivery,";

        //sql = sql + " (ifnull((select sum(amount) from t_bet where account = u.account and status in(1,2) and createTime >= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1))),0.00)) as nwRunAmount,";

        sql = sql + " DATE_FORMAT((select lastTime from t_recharge where 1=1 and account = u.account and status = 2 ORDER BY lastTime desc limit 1),'%Y-%m-%d %H:%i:%s') as lastRechargeTime,";

        sql = sql + " (ifnull((select sum(amount) from t_bet where account = u.account and status = 2 and lastTime >= GREATEST(ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1)), ifnull((select lastTime from t_recharge where 1=1 and account = u.account and status = 2 ORDER BY lastTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1)))),0.00)) as noWinRunAmount,";

        sql = sql + " (ifnull((select sum(GREATEST(amount,win)) from t_bet where account = u.account and status = 1 and lastTime >= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1))),0.00)) as winRunAmount,";

        sql = sql + " (ifnull((select sum(changeAmount) from t_amount_change where 1 = 1 and changeTime >= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1)) and changeUser = u.account and accountChangeTypeId = 3),0.00)) as subordinateRebate,";

        sql = sql + " (ifnull((select sum(changeAmount) from t_amount_change where 1 = 1 and changeTime >= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1)) and changeUser = u.account and accountChangeTypeId = 9),0.00)) as sysWithdrawalPoint,";

        //sql = sql + " (ifnull((select sum(changeAmount) from t_amount_change where 1 = 1 and changeTime >= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1)) and changeUser = u.account and accountChangeTypeId = 10),0.00)) as cancelAward,";

        sql = sql + " -(ifnull((select sum(GREATEST(amount,win)) from t_bet where account = u.account and status = 5 and lastTime <= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1)) and lastTime >= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1))),0.00)) as cancelRunAward,";

        sql = sql + " (ifnull((select sum(changeAmount) from t_finance_change where 1 = 1 and changeTime >= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1)) and changeUser = u.account and accountChangeTypeId = 28),0.00)) as dailyWage,";

        sql = sql + " (ifnull((select sum(changeAmount) from t_finance_change where 1 = 1 and changeTime >= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1)) and changeUser = u.account and accountChangeTypeId = 30),0.00)) as cycleDividend,";

        sql = sql + " (ifnull((select sum(changeAmount) from t_finance_change where 1 = 1 and changeTime >= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1)) and changeUser = u.account and accountChangeTypeId = 41),0.00)) as adminDispatch,";

        sql = sql + " (ifnull((select sum(changeAmount) from t_finance_change where 1 = 1 and changeTime >= ifnull((select afc.createTime from t_afc_calculation as afc where 1 = 1 and afc.changeUser = u.account and afc.test = 0 and afc.changeType = 1 and afc.status = 2 order by afc.createTime desc limit 1),(select onlineMarkTime from t_onlinemarktime where type = 1)) and changeUser = u.account and accountChangeTypeId = 19),0.00)) as activityDispatch";

        sql = sql + "  from t_user as u where 1 = 1 and u.test = 0 ";

        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND u.account = ? ";
            args.add(account);
        }

        sql = sql + " ORDER BY u.amount desc ";

        Object[] arg = args.toArray();
        return this.dbSession.listMap(sql, arg, p);
    }




}
