package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.LotterySaleRule;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("lotterySaleRuleDao")
public class LotterySaleRuleDao
        extends BaseDao<LotterySaleRule> {
    public int update(LotterySaleRule m) {
        String sql = new SQLUtils(this.tableName).field("lotteryId,status,openCycle,beforeClose,openAfter,weeks,saleTime,firstTime,lastTime,orderId,beforeDay")
                .where("id=?").getUpdate();
        return this.dbSession.update(
                sql,
                new Object[]{m.getLotteryId(), m.getStatus(), m.getOpenCycle(), m.getBeforeClose(), m.getOpenAfter(),
                        m.getWeeks(), m.getSaleTime(), m.getFirstTime(), m.getLastTime(), m.getOrderId(), m.getBeforeDay(), m.getId()});
    }

    public void save(LotterySaleRule m) {
        String sql = new SQLUtils(this.tableName).field("lotteryId,status,openCycle,beforeClose,openAfter,weeks,saleTime,firstTime,lastTime,orderId,beforeDay")
                .getInsert();
        this.dbSession.updateKeyHolder(
                sql,
                new Object[]{m.getLotteryId(), m.getStatus(), m.getOpenCycle(), m.getBeforeClose(), m.getOpenAfter(),
                        m.getWeeks(), m.getSaleTime(), m.getFirstTime(), m.getLastTime(), m.getOrderId(), m.getBeforeDay()});
    }

    public List<LotterySaleRule> list(String lotteryId, Page p) {
        String sql = new SQLUtils(this.tableName).where("lotteryId=?").orderBy("orderId,firstTime").getSelect();
        return this.dbSession.list(sql, new Object[]{lotteryId}, this.cls, p);
    }

    public List<LotterySaleRule> listByStatus(String lotteryId, Integer status) {
        String sql = new SQLUtils(this.tableName).where("lotteryId=? AND status=?").orderBy("orderId,firstTime").getSelect();
        return this.dbSession.list(sql, new Object[]{lotteryId, status}, this.cls);
    }

    public int deleteByLotteryId(String id) {
        String sql = "DELETE FROM " + this.tableName + " WHERE lotteryId=?";
        return this.dbSession.update(sql, new Object[]{id});
    }
}
