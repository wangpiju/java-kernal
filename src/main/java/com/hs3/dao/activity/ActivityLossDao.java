package com.hs3.dao.activity;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.activity.ActivityLoss;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("activityLossDao")
public class ActivityLossDao
        extends BaseDao<ActivityLoss> {
    public void save(ActivityLoss m) {
        String sql = new SQLUtils(this.tableName)
                .field("activityId,maxAmount,giveFatherAmount,giveGrandpaAmount").getInsert();
        this.dbSession.update(
                sql,
                new Object[]{m.getActivityId(), m.getMaxAmount(), m.getGiveFatherAmount(), m.getGiveGrandpaAmount()});
    }

    public int delete(String activityId) {
        String sql = "DELETE FROM " + this.tableName + " WHERE activityId=?";
        return this.dbSession.update(sql, new Object[]{activityId});
    }
    public int deleteByActivityId(Integer activityId) {
        String sql = "DELETE FROM " + this.tableName + " WHERE activityId=?";
        return this.dbSession.update(sql, new Object[]{activityId});
    }

    public List<ActivityLoss> findEntityByActivityId(Integer activityId) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE activityId =?");
        argsObjects = new Object[]{activityId};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public ActivityLoss findEntityByMaxAmount(BigDecimal amount) {
        String sql = new SQLUtils(this.tableName).where("maxAmount<=? ").orderBy(" maxAmount DESC LIMIT 1").getSelect();
        return (ActivityLoss) this.dbSession.getObject(sql, new Object[]{amount}, this.cls);
    }
}
