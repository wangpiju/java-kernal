package com.hs3.dao.bank;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.bank.BankSys;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("bankSysDao")
public class BankSysDao
        extends BaseDao<BankSys> {
    private static final String INSERT = "INSERT INTO t_bank_sys(nameId,card,niceName,address,levelId,crossStatus,status,remark,createTime,sign) VALUES(?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_STATUS = "UPDATE t_bank_sys SET status=? WHERE id=?";
    private static final String UPDATE_RECHARGE = "UPDATE t_bank_sys SET rechargeAmount=rechargeAmount+?, rechargeNum=rechargeNum+? WHERE id=?";

    public void save(BankSys m) {
        this.dbSession.update("INSERT INTO t_bank_sys(nameId,card,niceName,address,levelId,crossStatus,status,remark,createTime,sign) VALUES(?,?,?,?,?,?,?,?,?,?)",
                new Object[]{m.getNameId(), m.getCard(), m.getNiceName(), m.getAddress(), m.getLevelId(), m.getCrossStatus(), m.getStatus(), m.getRemark(),
                        new Date(), m.getSign()});
    }

    public int update(BankSys m) {
        List<Object> list = new ArrayList();
        String updateSql = "UPDATE t_bank_sys SET nameId=?,card=?,niceName=?,address=?,levelId=?,crossStatus=?,status=?,remark=?";
        list.addAll(Arrays.asList(new Object[]{m.getNameId(), m.getCard(), m.getNiceName(), m.getAddress(), m.getLevelId(), m.getCrossStatus(), m.getStatus(), m.getRemark()}));
        if (!StrUtils.hasEmpty(new Object[]{m.getSign()})) {
            updateSql = updateSql + ",sign=?";
            list.add(m.getSign());
        }
        updateSql = updateSql + " WHERE id=?";
        list.add(m.getId());
        return this.dbSession.update(updateSql, list.toArray());
    }

    public int updateStatus(Integer id, Integer status) {
        return this.dbSession.update("UPDATE t_bank_sys SET status=? WHERE id=?", new Object[]{status, id});
    }

    public int updateRecharge(Integer id, BigDecimal amount, Integer num) {
        return this.dbSession.update("UPDATE t_bank_sys SET rechargeAmount=rechargeAmount+?, rechargeNum=rechargeNum+? WHERE id=?", new Object[]{amount, num, id});
    }

    public List<BankSys> listByIds(List<Integer> ids) {
        if ((ids == null) || (ids.size() == 0)) {
            return new ArrayList();
        }
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE id in(" + getQuestionNumber(ids.size()) + ")"});
        return this.dbSession.list(sql, ids.toArray(), this.cls);
    }

    public List<BankSys> listByIdsNot(List<Integer> ids, Page p) {
        if ((ids == null) || (ids.size() == 0)) {
            return list(p);
        }
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE id not in(" + getQuestionNumber(ids.size()) + ")"});
        return this.dbSession.list(sql, ids.toArray(), this.cls);
    }
}
