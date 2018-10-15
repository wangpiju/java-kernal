package com.hs3.dao.contract;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.contract.ContractMessage;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("contractMessageDao")
public class ContractMessageDao
        extends BaseDao<ContractMessage> {
    public void save(ContractMessage m) {
        String sql = new SQLUtils(this.tableName)
                .field("sender,receiver,content,contractStatus,messageStatus,createTime").getInsert();
        this.dbSession.update(
                sql,
                new Object[]{m.getSender(), m.getReceiver(), m.getContent(), m.getContractStatus(), m.getMessageStatus(), m.getCreateTime()});
    }

    public int updateEntity(ContractMessage m) {
        String sql = new SQLUtils(this.tableName).field("contractStatus,messageStatus").where("sender=? and receiver =? ").getUpdate();
        return this.dbSession.update(sql, new Object[]{
                m.getContractStatus(),
                m.getMessageStatus(),
                m.getSender(),
                m.getReceiver()});
    }

    public int countByAccount(String account) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"count(*)", this.tableName, "WHERE receiver=? and messageStatus =0"});
        argsObjects = new Object[]{account};
        return this.dbSession.getInt(sql, argsObjects).intValue();
    }

    public List<ContractMessage> listByAccount(String account) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE receiver=? and messageStatus =0 "});
        argsObjects = new Object[]{account};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public int updateMessageStatus(Integer id) {
        String sql = new SQLUtils(this.tableName).field("messageStatus").where("id =?").getUpdate();
        return this.dbSession.update(sql, new Object[]{Integer.valueOf(1), id});
    }

    public int updateStatusByAccount(String account) {
        String sql = new SQLUtils(this.tableName).field("messageStatus").where("receiver =?").getUpdate();
        return this.dbSession.update(sql, new Object[]{Integer.valueOf(1), account});
    }
}
