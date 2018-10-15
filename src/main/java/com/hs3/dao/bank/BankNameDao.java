package com.hs3.dao.bank;

import com.hs3.dao.BaseDao;
import com.hs3.db.SQLUtils;
import com.hs3.entity.bank.BankName;
import com.hs3.entity.report.MembershipReport;
import com.hs3.utils.StrUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository("bankNameDao")
public class BankNameDao
        extends BaseDao<BankName> {
    private static final String INSERT = "INSERT INTO t_bank_name(code,title,orderId,minAmount,maxAmount,depositStatus,rechargeStatus,link) VALUES(?,?,?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE t_bank_name SET code=?,title=?,orderId=?,minAmount=?,maxAmount=?,depositStatus=?,rechargeStatus=?,link=? WHERE id=?";
    private static final String SELECT = "SELECT id,code,title,orderId,minAmount,maxAmount,depositStatus,rechargeStatus,link FROM t_bank_name WHERE 1 = 1";
    private static final String LISTALL = "SELECT id,title FROM t_bank_name where depositStatus=0";

    public List<BankName> findByAccount(String account) {
        String sql = "SELECT id,code,title,orderId,minAmount,maxAmount,depositStatus,rechargeStatus,link FROM t_bank_name WHERE 1 = 1 and bankNameId in (select bankNameId from t_bank_user where account = ?)";
        return this.dbSession.list(sql, new Object[]{account}, this.cls);
    }

    public List<BankName> findByStatus(Integer depositStatus, Integer rechargeStatus) {
        String sql = "SELECT id,code,title,orderId,minAmount,maxAmount,depositStatus,rechargeStatus,link FROM t_bank_name WHERE 1 = 1 ";
        List<Object> list = new ArrayList();
        if (depositStatus != null) {
            sql = sql + " AND depositStatus=?";
            list.add(depositStatus);
        }
        if (rechargeStatus != null) {
            sql = sql + " AND rechargeStatus=?";
            list.add(rechargeStatus);
        }

        sql = sql + " order by orderId ";

        if (list.isEmpty()) {
            return this.dbSession.list(sql, this.cls);
        }
        return this.dbSession.list(sql, list.toArray(new Object[list.size()]), this.cls);
    }

    public void save(BankName m) {
        this.dbSession.update("INSERT INTO t_bank_name(code,title,orderId,minAmount,maxAmount,depositStatus,rechargeStatus,link,bnType) VALUES(?,?,?,?,?,?,?,?,?)", new Object[]{m.getCode(), m.getTitle(), m.getOrderId(), m.getMinAmount(), m.getMaxAmount(), m.getDepositStatus(), m.getRechargeStatus(), m.getLink(),m.getBnType()});
    }

    public int update(BankName m) {
        return this.dbSession.update("UPDATE t_bank_name SET code=?,title=?,orderId=?,minAmount=?,maxAmount=?,depositStatus=?,rechargeStatus=?,link=?,bnType=? WHERE id=?",
                new Object[]{m.getCode(), m.getTitle(), m.getOrderId(), m.getMinAmount(), m.getMaxAmount(), m.getDepositStatus(), m.getRechargeStatus(), m.getLink(),m.getBnType() , m.getId()});
    }

    public List<Map<String, Object>> listBankAll() {
        return this.dbSession.listMap("SELECT id,title FROM t_bank_name where depositStatus=0");
    }

    public List<BankName> listByCodes(Set<String> codes) {
        List<Object> list = new ArrayList();
        String inSql = " AND code IN (";
        for (String s : codes) {
            inSql = inSql + "?,";
            list.add(s);
        }
        inSql = inSql + "'') ORDER BY code DESC";

        return this.dbSession.list("SELECT id,code,title,orderId,minAmount,maxAmount,depositStatus,rechargeStatus,link FROM t_bank_name WHERE 1 = 1" + inSql, list.toArray(new Object[list.size()]), this.cls);
    }

    //**************************************以下为变更部分*****************************************

    public List<Map<String, Object>> mapListBankAll() {
        return this.dbSession.listMap("SELECT id,code,title FROM t_bank_name where depositStatus=0 and bnType = 1 order by orderId ");
    }

    public List<Map<String, Object>> mapListBankAllByKb(String account) {
        String sql = "SELECT id,code,title FROM t_bank_name where depositStatus=0 and bnType = 1 and id in (select nameId from t_bank_sys where id in (select bankId from t_bank_group_sys where groupId = (select id from t_bank_group where title = (select tuz.bankGroup from t_user as tuz where tuz.account = (select tuc.rootAccount from t_user as tuc where tuc.account = ? )))))";
        Object[] args = {account};
        return this.dbSession.listMap(sql, args, null);
    }

    public BankName getBankNameById (Integer nameId){
        String sql = new SQLUtils(this.tableName).where("id=?").getSelect();
        Object[] args = {nameId};
        return (BankName) this.dbSession.getObject(sql, args, this.cls);
    }

    public BankName getBankNameByTitle (String title){
        String sql = new SQLUtils(this.tableName).where("title=?").getSelect();
        Object[] args = {title};
        return (BankName) this.dbSession.getObject(sql, args, this.cls);
    }


    public List<BankName> bankByBnTypeList(Integer bnType) {

        String sql = "SELECT id,code,title FROM t_bank_name where depositStatus=0 ";
        List<Object> args = new ArrayList<Object>();

        if (!StrUtils.hasEmpty(new Object[]{bnType})) {
            sql = sql + " and bnType = ? ";
            args.add(bnType);
        }

        Object[] arg = args.toArray();

        return this.dbSession.list(sql, arg, BankName.class);
    }


}
