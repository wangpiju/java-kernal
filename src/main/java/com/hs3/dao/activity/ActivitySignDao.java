package com.hs3.dao.activity;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.activity.ActivitySign;

import java.util.List;

import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Repository("activitySignDao")
public class ActivitySignDao
        extends BaseDao<ActivitySign> {
    public void save(ActivitySign m) {
        String sql = new SQLUtils(this.tableName)
                .field("activityId,days,betAmount,giveAmount,icoUrl").getInsert();
        this.dbSession.update(
                sql,
                new Object[]{m.getActivityId(), m.getDays(), m.getBetAmount(), m.getGiveAmount(), m.getIcoUrl()});
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

    public int updateByDay(ActivitySign m, String days) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", this.tableName, "activityId=?,days=?,icoUrl=?,betAmount=?,giveAmount=?", "days=?");
        return this.dbSession.update(sql, new Object[]{m.getActivityId(), m.getDays(), m.getIcoUrl(), m.getBetAmount(), m.getGiveAmount(), days});
    }

    public List<ActivitySign> findEntityByActivityId(Integer activityId) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE activityId =?");
        argsObjects = new Object[]{activityId};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public ActivitySign getObject(int days) {
        Object[] argsObjects = null;
        String sql = String.format("SELECT %s FROM %s %s", "*", this.tableName, "WHERE days =?");
        argsObjects = new Object[]{days};
        return (ActivitySign) this.dbSession.getObject(sql, argsObjects, this.cls);
    }
}
