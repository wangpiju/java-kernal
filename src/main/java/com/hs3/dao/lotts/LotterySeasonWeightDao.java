package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.LotterySeasonWeight;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("lotterySeasonWeightDao")
public class LotterySeasonWeightDao
        extends BaseDao<LotterySeasonWeight> {
    public void save(LotterySeasonWeight m) {
        String sql = new SQLUtils(this.tableName).field("seasonId,lotteryId,nums,weightType,weight,createTime").getInsert();
        this.dbSession.update(sql, new Object[]{
                m.getSeasonId(),
                m.getLotteryId(),
                m.getNums(),
                m.getWeightType(),
                m.getWeight(),
                m.getCreateTime()});
    }

    public List<LotterySeasonWeight> list(String lotteryId, Page p) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE lotteryId=? ORDER BY seasonId DESC"});
        return this.dbSession.list(sql, new Object[]{lotteryId}, this.cls, p);
    }

    public List<LotterySeasonWeight> list(String lotteryId, String seasonId) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE lotteryId=? AND seasonId=?"});
        return this.dbSession.list(sql, new Object[]{lotteryId, seasonId}, this.cls);
    }

    public LotterySeasonWeight getObject(String lotteryId, String seasonId, String weightType) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE lotteryId=? AND seasonId=? AND weightType=?"});
        return (LotterySeasonWeight) this.dbSession.getObject(sql, new Object[]{lotteryId, seasonId, weightType}, this.cls);
    }

    public int deleteByLotteryId(String id) {
        String sql = "DELETE FROM " + this.tableName + " WHERE lotteryId=?";
        return this.dbSession.update(sql, new Object[]{id});
    }
}
