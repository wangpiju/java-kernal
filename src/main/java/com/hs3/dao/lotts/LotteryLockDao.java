package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.LotteryLock;
import org.springframework.stereotype.Repository;

@Repository("lotteryLockDao")
public class LotteryLockDao
        extends BaseDao<LotteryLock> {
    public void save(LotteryLock m) {
        String sql = new SQLUtils(this.tableName).field("lotteryId,status,ratio,deviation,closeTime").getInsert();
        this.dbSession.update(sql, new Object[]{
                m.getLotteryId(),
                m.getStatus(),
                m.getRatio(),
                m.getDeviation(),
                m.getCloseTime()});
    }

    public int update(LotteryLock m) {
        String sql = new SQLUtils(this.tableName).field("lotteryId,status,ratio,deviation,closeTime").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{
                m.getLotteryId(),
                m.getStatus(),
                m.getRatio(),
                m.getDeviation(),
                m.getCloseTime(),
                m.getId()});
    }

    public LotteryLock findByLotteryId(String lotteryId) {
        String sql = new SQLUtils(this.tableName).where("lotteryId=?").getSelect();
        return this.dbSession.getObject(sql, new Object[]{lotteryId}, this.cls);
    }

    public int deleteByLotteryId(String id) {
        String sql = "DELETE FROM " + this.tableName + " WHERE lotteryId=?";
        return this.dbSession.update(sql, new Object[]{id});
    }
}
