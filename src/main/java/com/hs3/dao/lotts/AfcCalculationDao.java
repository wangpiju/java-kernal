package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.entity.lotts.AfcCalculation;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository("afcCalculationDao")
public class AfcCalculationDao extends BaseDao<AfcCalculation> {

    public void save(AfcCalculation afc) {

        Object[] a = {
                0,
                afc.getChangeUser(),
                afc.getTest(),
                afc.getAfId(),
                afc.getChangeType(),
                new Date(),
                new Date(),
                afc.getStatus(),
                afc.getAmount(),
                afc.getCashableBalance(),
                afc.getChangeAmount(),
                afc.getRemainAmount()
        };

        this.dbSession.update("insert into t_afc_calculation values(?,?,?,?,?,?,?,?,?,?,?,?)", a);
    }

    public AfcCalculation findTopByAccount(String account, String afId) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE changeUser=? and changeType = 1 and afId = ? ORDER BY createTime DESC limit 1"});
        Object[] args = {account, afId};
        return (AfcCalculation) this.dbSession.getObject(sql, args, this.cls);
    }

    public int updateInfo(Integer id, Integer status) {
        return this.dbSession.update("UPDATE t_afc_calculation SET status=?,changeTime=? WHERE id=?", new Object[]{status, new Date(), id});
    }


}
