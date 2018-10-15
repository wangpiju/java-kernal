package com.hs3.dao.bank;

import com.hs3.dao.BaseDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.bank.BankUser;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("bankUserDao")
public class BankUserDao
        extends BaseDao<BankUser> {
    private static final String INSERT = "INSERT INTO t_bank_user(account,bankNameId,card,address,niceName,createTime,parentAccount) VALUES(?,?,?,?,?,?,?)";
    private static final String UPDATE_STATUS_ADMIN = "UPDATE t_bank_user SET status=? WHERE id=?";
    private static final String SELECT_ACCOUNT = "SELECT b.id,b.account,b.bankNameId,RIGHT(b.card,4) card,b.address,b.niceName,b.status,b.createTime,b.parentAccount FROM t_bank_user b WHERE b.account = ? and b.status = ?";
    private static final String SELECT_STATUS_ACCOUNT = "SELECT * FROM t_bank_user WHERE account=? AND status=?";
    private static final String SELECT_CITY = "SELECT c.cityname FROM t_city c WHERE c.PROVID = ?";
    private static final String SELECT_PROVINCE = "SELECT provid,provname FROM t_province WHERE state = '1'";
    private static final String findCardByAccountAndName = "select count(1) from t_bank_user where account = ? and niceName = ?";

    public void save(BankUser m) {
        this.dbSession.updateKeyHolder("INSERT INTO t_bank_user(account,bankNameId,card,address,niceName,createTime,parentAccount) VALUES(?,?,?,?,?,?,?)", new Object[]{m.getAccount(), m.getBankNameId(), m.getCard(), m.getAddress(), m.getNiceName(), new Date(),
                m.getParentAccount()});
    }

    public int update(BankUser m) {
        String sql = new SQLUtils(this.tableName).field("account,bankNameId,card,address,niceName,status,parentAccount,createTime").where("id=?").getUpdate();
        return this.dbSession.update(
                sql,
                new Object[]{
                        m.getAccount(),
                        m.getBankNameId(),
                        m.getCard(),
                        m.getAddress(),
                        m.getNiceName(),
                        m.getStatus(),
                        m.getParentAccount(),
                        m.getCreateTime(),
                        m.getId()});
    }

    public int deleteByUser(Integer id, String account) {
        return this.dbSession.update("delete t_bank_user where id = ? and account = ?", new Object[]{id, account});
    }

    public int updateStatus(Integer id, Integer status) {
        return this.dbSession.update("UPDATE t_bank_user SET status=? WHERE id=?", new Object[]{status, id});
    }

    public List<BankUser> listBy_Account(String account, Integer status) {
        return this.dbSession.list("SELECT b.id,b.account,b.bankNameId,RIGHT(b.card,4) card,b.address,b.niceName,b.status,b.createTime,b.parentAccount FROM t_bank_user b WHERE b.account = ? and b.status = ?", new Object[]{account, status}, this.cls);
    }

    public List<Map<String, Object>> listCityByProId(int id) {
        Object[] args = {Integer.valueOf(id)};
        return this.dbSession.listMap("SELECT c.cityname FROM t_city c WHERE c.PROVID = ?", args, null);
    }

    public List<Map<String, Object>> listProvince() {
        return this.dbSession.listMap("SELECT provid,provname FROM t_province WHERE state = '1'");
    }

    public int findRecordByCard(Integer id, String card, String niceName, String account, String safeWord) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT                         ");
        sb.append("   COUNT(1)                     ");
        sb.append(" FROM                           ");
        sb.append("   t_bank_user b                ");
        sb.append(" WHERE b.id = ?                 ");
        sb.append("   AND b.card = ?               ");
        sb.append("   AND b.niceName = ?           ");
        sb.append("   AND b.account = ?           ");
        sb.append("   AND EXISTS                   ");
        sb.append("   (SELECT                      ");
        sb.append("     1                          ");
        sb.append("   FROM                         ");
        sb.append("     t_user u                   ");
        sb.append("   WHERE u.account = b.account  ");
        sb.append("     AND u.safePassword = ?)    ");
        return this.dbSession.getInt(sb.toString(), new Object[]{id, card, niceName, account, StrUtils.MD5(safeWord)}).intValue();
    }

    public int findCard(String card, String account) {
        String sql = "select count(1) from t_bank_user where card = ? and account = ?";
        return this.dbSession.getInt(sql, new Object[]{card, account}).intValue();
    }

    public BankUser findByCard(String card) {
        String sql = "select * from t_bank_user where card = ? limit 1";
        return (BankUser) this.dbSession.getObject(sql, new Object[]{card}, this.cls);
    }

    public int findByStatus(String card, String account) {
        String sql = "select count(1) from t_bank_user where card = ? and account = ? and status =0";
        return this.dbSession.getInt(sql, new Object[]{card, account}).intValue();
    }

    public int findCardByAccountAndName(String account, String name) {
        return this.dbSession.getInt("select count(1) from t_bank_user where account = ? and niceName = ?", new Object[]{account, name}).intValue();
    }

    public List<BankUser> listByAccount(String account, Integer status) {
        return this.dbSession.list("SELECT * FROM t_bank_user WHERE account=? AND status=?", new Object[]{account, status}, this.cls);
    }

    public int findBankCount(String account) {
        String sql = new SQLUtils(this.tableName).field("count(1)").where("account=?").getSelect();
        Integer count = this.dbSession.getInt(sql, new Object[]{account});
        if (count == null) {
            count = Integer.valueOf(0);
        }
        return count.intValue();
    }

    public List<BankUser> listByAccount(int userMark, String account, String niceName, String bankCard, Date begin, Date end, Page p) {
        StringBuilder sb = new StringBuilder("SELECT i.*," + UserDao.getMarkSQL() + " FROM " + this.tableName + " as i WHERE 1=1 ");

        List<Object> args = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sb.append(" AND account=?");
            args.add(account);
        }
        if (!StrUtils.hasEmpty(new Object[]{niceName})) {
            sb.append(" AND niceName=?");
            args.add(niceName);
        }
        if (!StrUtils.hasEmpty(new Object[]{bankCard})) {
            sb.append(" AND card=?");
            args.add(bankCard);
        }
        if ((begin != null) && (end != null)) {
            sb.append(" AND createTime>=? AND createTime<=?");
            args.add(begin);
            args.add(end);
        }
        return this.dbSession.list(sb.toString(), args.toArray(), this.cls, p);
    }

    public List<BankUser> findByAccount(String account) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(this.tableName).append(" WHERE status=0");
        List<Object> args = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sb.append(" AND account=?");
            args.add(account);
        }
        return this.dbSession.list(sb.toString(), args.toArray(), this.cls);
    }

    //**************************************以下为变更部分*****************************************

    public BankUser findById(String id) {
        String sql = "select * from t_bank_user where id = ? ";
        return (BankUser) this.dbSession.getObject(sql, new Object[]{id}, this.cls);
    }

}
