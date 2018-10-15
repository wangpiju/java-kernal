package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.BonusGroupDetails;
import com.hs3.models.BonusGroupDetailsModel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("bonusGroupDetailsDao")
public class BonusGroupDetailsDao
        extends BaseDao<BonusGroupDetails> {
    public int update(BonusGroupDetails m) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName,
                "bonusRatio=?,rebateRatio=?,bonus=?",
                "id=? and lotteryId=? and bonusGroupId=?"});
        return this.dbSession.update(
                sql,
                new Object[]{m.getBonusRatio(), m.getRebateRatio(),
                        m.getBonus(), m.getId(), m.getLotteryId(),
                        m.getBonusGroupId()});
    }

    public void save(BonusGroupDetails m) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", new Object[]{this.tableName,
                "bonusRatio,rebateRatio,bonus,id,lotteryId,bonusGroupId",
                "?,?,?,?,?,?"});
        this.dbSession.update(
                sql,
                new Object[]{m.getBonusRatio(), m.getRebateRatio(),
                        m.getBonus(), m.getId(), m.getLotteryId(),
                        m.getBonusGroupId()});
    }

    public List<BonusGroupDetails> listByGroupIdAndLotteryId(Integer groupId, String lotteryId) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName,
                "WHERE bonusGroupId=? AND lotteryId=?"});
        return this.dbSession.list(sql, new Object[]{groupId, lotteryId}, this.cls);
    }

    public List<BonusGroupDetails> listByLotteryId(String lotteryId) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName,
                "WHERE lotteryId=?"});
        return this.dbSession.list(sql, new Object[]{lotteryId}, this.cls);
    }

    public BonusGroupDetails find(String playId, String lotteryId, Integer bonusGroupId) {
        String sql = new SQLUtils(this.tableName).where(
                "id=? AND lotteryId=? AND bonusGroupId=?").getSelect();
        return (BonusGroupDetails) this.dbSession.getObject(sql, new Object[]{playId, lotteryId,
                bonusGroupId}, this.cls);
    }

    public Integer deleteByLotteryId(String id) {
        String sql = String.format("DELETE FROM %s WHERE lotteryId=?", new Object[]{
                this.tableName});
        return Integer.valueOf(this.dbSession.update(sql, new Object[]{id}));
    }

    public Integer updateAll(BonusGroupDetailsModel model) {
        String sql =
                String.format("UPDATE %s SET bonusRatio=?,rebateRatio=? WHERE id=? AND lotteryId=? AND bonusGroupId=?", new Object[]{
                        this.tableName});
        int count = 0;
        for (BonusGroupDetails bgd : model.getDetails()) {
            count = count + this.dbSession.update(sql, new Object[]{bgd.getBonusRatio(), bgd.getRebateRatio(), bgd.getId(), model.getLotteryId(), model.getBonusGroupId()});
        }
        return Integer.valueOf(count);
    }

    public List<BonusGroupDetails> listByPlayIdAndGroupId(Integer groupId, String[] playIds) {
        String sql = new SQLUtils(this.tableName).where("bonusGroupId=?")
                .whereInAnd("id in(?)", playIds.length).getSelect();
        List<Object> args = new ArrayList();
        args.add(groupId);
        for (String p : playIds) {
            args.add(p);
        }
        return this.dbSession.list(sql, args.toArray(), this.cls);
    }
}
