package com.hs3.dao.activity;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.activity.ActivityRecharge;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("activityRechargeDao")
public class ActivityRechargeDao
        extends BaseDao<ActivityRecharge> {
    public void save(ActivityRecharge m) {
        String sql = new SQLUtils(this.tableName).field("amount,bonus").getInsert();
        this.dbSession.update(sql, new Object[]{m.getAmount(), m.getBonus()});
    }

    public int update(ActivityRecharge m) {
        String sql = new SQLUtils(this.tableName).field("bonus").where("amount=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{m.getBonus(), m.getAmount()});
    }

    public boolean hasRecord(BigDecimal amount) {
        String selectSQL = new SQLUtils(this.tableName).field("count(1)").where("amount=?").getSelect();
        int n = this.dbSession.getInt(selectSQL, new Object[]{amount});
        return n != 0;
    }

    public BigDecimal getBonus(BigDecimal amount) {
        String sql = new SQLUtils(this.tableName).field("bonus").where("amount<=?").orderBy("amount DESC LIMIT 1").getSelect();
        String n = this.dbSession.getString(sql, new Object[]{amount});
        BigDecimal rel = null;
        if (n != null) {
            rel = new BigDecimal(n);
        }
        return rel;
    }

    public int deleteNotInAmount(List<Object> amounts) {
        String deleteSQL = "DELETE FROM " + this.tableName + " WHERE amount NOT IN(" + getQuestionNumber(amounts.size()) + ")";
        return this.dbSession.update(deleteSQL, amounts.toArray());
    }
}
