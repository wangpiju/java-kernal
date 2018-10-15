package com.hs3.dao.contract;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.contract.ContractConfig;
import org.springframework.stereotype.Repository;

@Repository("contractConfigDao")
public class ContractConfigDao
        extends BaseDao<ContractConfig> {
    public int save(ContractConfig m) {
        String sql = new SQLUtils(this.tableName)
                .field("ruleNum,dayNum,bonusCycle").getInsert();
        return this.dbSession.update(
                sql,
                new Object[]{m.getRuleNum(), m.getDayNum(), m.getBonusCycle()});
    }

    public int update(ContractConfig m) {
        String sql = new SQLUtils(this.tableName).field("ruleNum,dayNum,bonusCycle").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{m.getRuleNum(), m.getDayNum(), m.getBonusCycle(), m.getId()});
    }

    public ContractConfig findEntity() {
        String sql = "SELECT * FROM " + this.tableName;
        return (ContractConfig) this.dbSession.getObject(sql, this.cls);
    }
}
