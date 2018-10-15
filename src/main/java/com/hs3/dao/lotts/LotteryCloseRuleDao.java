package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.LotteryCloseRule;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("lotteryCloseRuleDao")
public class LotteryCloseRuleDao
        extends BaseDao<LotteryCloseRule> {
    public int update(LotteryCloseRule m) {
        String sql = new SQLUtils(this.tableName).field("lotteryId,startTime,endTime,status").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{m.getLotteryId(), m.getStartTime(), m.getEndTime(), m.getStatus(), m.getId()});
    }

    public void save(LotteryCloseRule m) {
        String sql = new SQLUtils(this.tableName).field("lotteryId,startTime,endTime,status").getInsert();
        this.dbSession.updateKeyHolder(sql, new Object[]{m.getLotteryId(), m.getStartTime(), m.getEndTime(), m.getStatus()});
    }

    public List<LotteryCloseRule> listByLotteryId(String lotteryId, Page page) {
        String sql = new SQLUtils(this.tableName).where("lotteryId=?").getSelect();
        return this.dbSession.list(sql, new Object[]{lotteryId}, this.cls, page);
    }

    public List<LotteryCloseRule> listByStatus(String lotteryId, int status) {
        String sql = new SQLUtils(this.tableName).where("lotteryId=? AND status=?").getSelect();
        return this.dbSession.list(sql, new Object[]{lotteryId, Integer.valueOf(status)}, this.cls);
    }

    public int deleteByLotteryId(String id) {
        String sql = "DELETE FROM " + this.tableName + " WHERE lotteryId=?";
        return this.dbSession.update(sql, new Object[]{id});
    }
}
