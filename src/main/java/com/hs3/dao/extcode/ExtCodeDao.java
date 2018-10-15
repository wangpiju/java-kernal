package com.hs3.dao.extcode;

import com.hs3.dao.BaseDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.sys.SysClear;
import com.hs3.entity.users.ExtCode;
import com.hs3.tasks.sys.SysClearJobEnum;
import com.hs3.tasks.sys.SysClearJobFactory;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("extCodeDao")
public class ExtCodeDao
        extends BaseDao<ExtCode> {
    private static String INSERT = "insert into t_ext_code values(?,?,?,?,?,?,?   ,?   ,?,?,?,?,?,?)";
    private static String INSERT_LINK = "insert into t_ext_code_l(code,account,createTime,address) values(?,?,?,?)";
    private static String UPDATE_STATUS = "UPDATE t_ext_code SET status=? WHERE id=?";
    private static final String FROM = "FROM t_ext_code WHERE createTime <= ? AND ((validTime != 0 AND DATE_ADD(createTime, INTERVAL validTime day) < NOW()) OR status = 1)";

    public String save(ExtCode code) {
        this.dbSession.update(INSERT,
                new Object[]{0, code.getAccount(), code.getParnetaccount(),
                        code.getUsertype(), code.getCode(),
                        code.getValidtime(), code.getBonusgroupid(), code.getRebateratio(),
                        code.getExtaddress(), code.getQq(), code.getCreatetime(), code.getStatus(), code.getCanregists(), code.getLastregist()});
        return code.getCode();
    }

    public int savelink(String code, String account, Date createTime, String address) {
        return this.dbSession.update(INSERT_LINK, new Object[]{code, account, createTime, address});
    }

    public int updateIpCanRegistByCode(String code, String IpCanRegist) {
        return this.dbSession.update(" update t_ext_code set canRegists=? where code=?", new Object[]{IpCanRegist, code});
    }

    public int updateLastRegistByCode(String code, Date date) {
        return this.dbSession.update(" update t_ext_code set lastRegist=? where code=?", new Object[]{date, code});
    }

    public ExtCode findByExtCode(String code) {
        String sql = "select * from t_ext_code where code=?";
        Object[] args = {code};
        return (ExtCode) this.dbSession.getObject(sql, args, this.cls);
    }

    public int findRegistersByExtCode(String code, String ip) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT                        ");
        sb.append("  COUNT(1)                    ");
        sb.append(" FROM                          ");
        sb.append("  t_user a                    ");
        sb.append(" WHERE a.regip=?             ");
        sb.append("  AND EXISTS                  ");
        sb.append("  (SELECT                     ");
        sb.append("    1                         ");
        sb.append("  FROM                        ");
        sb.append("    t_ext_code_l b             ");
        sb.append("  WHERE b.account = a.account ");
        sb.append("    AND b.code =?  )  ");


        Object[] args = {ip, code};
        return this.dbSession.getInt(sb.toString(), args).intValue();
    }

    public List<Map<String, Object>> listWithRegistNum(Page p) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT                                  ");
        sb.append("   c.*,                         \t\t\t");
        sb.append("   (SELECT                               ");
        sb.append("     COUNT(1)                            ");
        sb.append("   FROM                                  ");
        sb.append("     t_ext_code_l a                       ");
        sb.append("   WHERE a.code = c.code) registNum,     ");
        sb.append("   (SELECT                               ");
        sb.append("     COUNT(1)                            ");
        sb.append("   FROM                                  ");
        sb.append("     t_ext_code_l a                       ");
        sb.append("   WHERE a.code = c.code                 ");
        sb.append("     AND a.rechargeNum > 0) rechargeNum  ");
        sb.append(" FROM                                    ");
        sb.append("   t_ext_code c                           ");
        return this.dbSession.listMap(sb.toString(), p);
    }

    public List<Map<String, Object>> listWithRegistNum(String account, String code, Date beginCreateTime, Date endCreateTime, Integer bonusGroupId, BigDecimal minRebateRatio, BigDecimal maxRebateRatiot, Date lastTime, Page p) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT                                  ");
        sb.append("   c.*,                         \t\t\t");
        sb.append(UserDao.getMarkSQL("c.account") + ",\t\t");
        sb.append("   (SELECT                               ");
        sb.append("     COUNT(1)                            ");
        sb.append("   FROM                                  ");
        sb.append("     t_ext_code_l a                       ");
        sb.append("   WHERE a.code = c.code) registNum,     ");
        sb.append("   (SELECT                               ");
        sb.append("     COUNT(1)                            ");
        sb.append("   FROM                                  ");
        sb.append("     t_ext_code_l a                       ");
        sb.append("   WHERE a.code = c.code                 ");
        sb.append("     AND a.rechargeNum > 0) rechargeNum  ");
        sb.append(" FROM                                    ");
        sb.append("   t_ext_code c                           ");
        sb.append(" WHERE 1=1\t\t\t\t\t\t\t\t");
        List<Object> args = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sb.append(" AND account=?");
            args.add(account);
        }
        if (!StrUtils.hasEmpty(new Object[]{code})) {
            sb.append(" AND code=?");
            args.add(code);
        }
        if (bonusGroupId != null) {
            sb.append(" AND bonusGroupId=?");
            args.add(bonusGroupId);
        }
        if ((beginCreateTime != null) && (endCreateTime != null)) {
            sb.append(" AND createTime>=? AND createTime<=?");
            args.add(beginCreateTime);
            args.add(endCreateTime);
        }
        if ((minRebateRatio != null) && (maxRebateRatiot != null)) {
            sb.append(" AND rebateRatio>=? AND rebateRatio<=?");
            args.add(minRebateRatio);
            args.add(maxRebateRatiot);
        }
        if (lastTime != null) {
            sb.append(" AND lastRegist>=?");
            args.add(lastTime);
        }
        return this.dbSession.listMap(sb.toString(), args.toArray(), p);
    }

    public Integer setStatus(Integer id, Integer status) {
        return Integer.valueOf(this.dbSession.update(UPDATE_STATUS, new Object[]{status, id}));
    }

    public Integer setStatusByAccount(String account, Integer id, Integer status) {
        String sql = new SQLUtils(this.tableName).field("status").where("account=? AND id=?").getUpdate();
        return Integer.valueOf(this.dbSession.update(sql, new Object[]{status, account, id}));
    }

    public List<Map<String, Object>> listUserExtCode(String account, Page p) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT                              ");
        sb.append("   c.id,                    ");
        sb.append("   c.rebateRatio,                    ");
        sb.append("   c.code,                           ");
        sb.append("   c.validTime,                          ");
        sb.append("   DATE_FORMAT(c.createTime,'%Y-%m-%d %H:%i:%s') createTime,  ");
        sb.append("   c.userType,c.extAddress,          ");
        sb.append("   (SELECT                           ");
        sb.append("     COUNT(a.account)                ");
        sb.append("   FROM                              ");
        sb.append("     t_ext_code_l a                  ");
        sb.append("   WHERE a.code = c.code) registNum  ");
        sb.append(" FROM                                ");
        sb.append("   t_ext_code c                      ");
        sb.append(" WHERE c.account = ? and c.status = '0'                 ");
        sb.append(" ORDER BY c.createTime DESC          ");

        Object[] args = {account};
        return this.dbSession.listMap(sb.toString(), args, p);
    }

    public int deleteByClear(SysClear m) {
        SysClearJobEnum sysClearJobEnum = SysClearJobFactory.getSysClearJobEnum(m.getJob());
        Object[] cond = {sysClearJobEnum.getObj(m)};
        if (m.getClearMode() == 2) {
            this.dbSession.update("INSERT INTO backup_" + sysClearJobEnum.getTable() + " SELECT * " + "FROM t_ext_code WHERE createTime <= ? AND ((validTime != 0 AND DATE_ADD(createTime, INTERVAL validTime day) < NOW()) OR status = 1)", cond);
        }
        return this.dbSession.update("DELETE FROM t_ext_code WHERE createTime <= ? AND ((validTime != 0 AND DATE_ADD(createTime, INTERVAL validTime day) < NOW()) OR status = 1)", cond);
    }

    public List<Map<String, Object>> listForExtCode(Page p, String code, Date beginCreateTime, Date endCreateTime) {
        StringBuffer sb = new StringBuffer();
        String table1 = "";
        String table2 = "";
        String table3 = "";
        if (!StrUtils.hasEmpty(new Object[]{beginCreateTime, endCreateTime})) {
            table1 = " (select a.account,(SELECT count(1) FROM\tt_bank_user WHERE\taccount = a.account) AS num,a.loginStatus from t_user a  where a.account IN (SELECT account FROM t_ext_code_l WHERE CODE = ? and createTime BETWEEN ? and ?)) t ";

            table2 = " (SELECT account,createTime,address FROM t_ext_code_l  WHERE CODE = ? and createTime BETWEEN ? and ?) r ";
            table3 = " (select account,SUM(IFNULL(betAmount, 0)) AS betAmount,SUM(IFNULL(rechargeAmount, 0)) AS rechargeAmount from t_user_report  where account IN (SELECT account FROM t_ext_code_l WHERE CODE = ? and createTime BETWEEN ? and ?)  AND createDate BETWEEN ? AND ? GROUP BY account) p ";
        } else {
            table1 = " (select a.account,(SELECT count(1) FROM\tt_bank_user WHERE\taccount = a.account) AS num,a.loginStatus from t_user a  where a.account IN (SELECT account FROM t_ext_code_l WHERE CODE = ? )) t ";

            table2 = " (SELECT account,createTime,address FROM t_ext_code_l  WHERE CODE = ? ) r ";
            table3 = " (select account,SUM(IFNULL(betAmount, 0)) AS betAmount,SUM(IFNULL(rechargeAmount, 0)) AS rechargeAmount from t_user_report  where account IN (SELECT account FROM t_ext_code_l WHERE CODE = ? )  GROUP BY account) p ";
        }
        sb.append(" SELECT ");
        sb.append(" y.account,y.createTime,y.address,y.num,p.betAmount,p.rechargeAmount,y.loginStatus ");
        sb.append(" FROM ");
        sb.append(" (SELECT ");
        sb.append(" r.account,t.num,t.loginStatus,r.createTime,r.address ");
        sb.append(" FROM " + table1 + "," + table2);
        sb.append(" where t.account = r.account ");
        sb.append(" ) y ");
        sb.append(" LEFT JOIN " + table3);
        sb.append(" ON y.account = p.account ");
        Object[] args = null;
        if (!StrUtils.hasEmpty(new Object[]{beginCreateTime, endCreateTime})) {
            args = new Object[]{code, beginCreateTime, endCreateTime, code, beginCreateTime, endCreateTime, code, beginCreateTime, endCreateTime, beginCreateTime, endCreateTime};
        } else {
            args = new Object[]{code, code, code};
        }
        String sql = sb.toString();
        Map<String, Object> a = this.dbSession.getMap("SELECT IFNULL(SUM(betAmount), 0) as betAmount,IFNULL(SUM(rechargeAmount), 0) as rechargeAmount FROM (" + sql + ") as k ", args);
        p.setObj(a);
        return this.dbSession.listMap(sql, args, p);
    }
}
