package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.models.lotts.TempAmountChange;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("tempAmountChangeDao")
public class TempAmountChangeDao
        extends BaseDao<TempAmountChange> {
    public void saveTempAmountChange(TempAmountChange m) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", new Object[]{this.tableName,
                "playName,changeUser,changeAmount,balance,changeSource,betId,seasonId,lotteryId,changeTime,remark,handlers,accountChangeTypeId,test,playerId,groupName,lotteryName,unit,status",
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"});
        int id = this.dbSession.updateKeyHolder(sql, new Object[]{
                m.getPlayName(),
                m.getChangeUser(),
                m.getChangeAmount(),
                m.getBalance(),
                m.getChangeSource(),
                m.getBetId(),
                m.getSeasonId(),
                m.getLotteryId(),
                m.getChangeTime(),
                m.getRemark(),
                m.getHandlers(),
                Integer.valueOf(m.getAccountChangeTypeId()),
                Integer.valueOf(m.getTest()),
                m.getPlayerId(),
                m.getGroupName(),
                m.getLotteryName(),
                m.getUnit(),
                m.getStatus()});


        m.setId(id);
    }

    public List<TempAmountChange> listByBetId(String betId) {
        Object[] args = {betId};
        String sql = "/*master*/" + new SQLUtils(this.tableName).where("betId=? and status=0").getSelect();
        return this.dbSession.list(sql, args, this.cls);
    }

    public void updateByBetId(String betId) {
        Object[] args = {Integer.valueOf(1), betId};
        String sql = new SQLUtils(this.tableName).field("status").where("betId=?").getUpdate();
        this.dbSession.update(sql, args);
    }
}
