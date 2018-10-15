package com.hs3.dao.activity;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.activity.ActivityBetconsumer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("activityBetconsumerDao")
public class ActivityBetconsumerDao
        extends BaseDao<ActivityBetconsumer> {
    public void save(ActivityBetconsumer m) {
        String sql = new SQLUtils(this.tableName)
                .field("activityId,itemId,betAmount,giveAmount,icoUrl").getInsert();
        this.dbSession.update(
                sql,
                new Object[]{m.getActivityId(), m.getItemId(), m.getBetAmount(), m.getGiveAmount(), m.getIcoUrl()});
    }

    public int delete(String activityId) {
        String sql = "DELETE FROM " + this.tableName + " WHERE activityId=?";
        return this.dbSession.update(sql, new Object[]{activityId});
    }

    public int deleteByActivityId(Integer activityId) {
        String sql = "DELETE FROM " + this.tableName + " WHERE activityId=?";
        return this.dbSession.update(sql, new Object[]{activityId});
    }

    public int deleteByDay(String days) {
        String sql = "DELETE FROM " + this.tableName + " WHERE days=?";
        return this.dbSession.update(sql, new Object[]{days});
    }

    public int updateByDay(ActivityBetconsumer m, String days) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", this.tableName, "activityId=?,itemId=?,icoUrl=?,betAmount=?,giveAmount=?", "days=?");
        return this.dbSession.update(sql, new Object[]{m.getActivityId(), m.getItemId(), m.getIcoUrl(), m.getBetAmount(), m.getGiveAmount(), days});
    }

    public List<ActivityBetconsumer> findEntityByActivityId(Integer activityId) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE activityId =? ORDER BY giveAmount");
        argsObjects = new Object[]{activityId};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public ActivityBetconsumer getObject(String item) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE itemId =?");
        argsObjects = new Object[]{item};
        return this.dbSession.getObject(sql, argsObjects, this.cls);
    }

    public List<ActivityBetconsumer> getHasGiveList(String account, Integer activityId, Date begin, Date end) {
        Object[] argsObjects = null;
        String sql = "SELECT a.activityId,itemId,icoUrl,betAmount,giveAmount FROM t_activity_user a LEFT JOIN t_activity_betconsumer b ON a.item=b.itemId WHERE account=? AND a.activityId=? AND status=3 AND a.createTime BETWEEN ? AND ?";

        argsObjects = new Object[]{account, activityId, begin, end};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public List<ActivityBetconsumer> getCanGiveList(BigDecimal canAmount) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE betAmount <=?");
        argsObjects = new Object[]{canAmount};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }
}
