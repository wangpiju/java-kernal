package com.hs3.dao.activity;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.activity.ActivityUser;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("activityUserDao")
public class ActivityUserDao
        extends BaseDao<ActivityUser> {
    public int update(ActivityUser m) {
        String sql = new SQLUtils(this.tableName).field("account,activityId,operator,status,createTime,validTime,item").where("id=?").getUpdate();

        return this.dbSession.update(
                sql,
                new Object[]{
                        m.getAccount(),
                        m.getActivityId(),
                        m.getOperator(),
                        m.getStatus(),
                        m.getCreateTime(),
                        m.getValidTime(),
                        m.getItem(),
                        m.getId()});
    }

    public void save(ActivityUser m) {
        String sql = new SQLUtils(this.tableName).field("account,activityId,operator,status,createTime,validTime,item").getInsert();

        this.dbSession.update(
                sql,
                new Object[]{
                        m.getAccount(),
                        m.getActivityId(),
                        m.getOperator(),
                        m.getStatus(),
                        m.getCreateTime(),
                        m.getValidTime(),
                        m.getItem()});
    }

    public List<ActivityUser> list(Integer activityId, Integer status, Page p) {
        SQLUtils utils = new SQLUtils(this.tableName).where("1=1");
        List<Object> args = new ArrayList<>();
        if (!StrUtils.hasEmpty(activityId)) {
            utils.where("AND activityId=?");
            args.add(activityId);
        }
        if (status != null) {
            utils.where("AND status=?");
            args.add(status);
        }
        String sql = utils.getSelect();
        return this.dbSession.list(sql, args.toArray(), this.cls, p);
    }

    public List<ActivityUser> listByUser(Integer activityId, String account, Integer status, Date beginDate, Date endDate) {
        SQLUtils utils = new SQLUtils(this.tableName).where("1=1");
        List<Object> args = new ArrayList<>();
        if (!StrUtils.hasEmpty(activityId)) {
            utils.where("AND activityId=?");
            args.add(activityId);
        }
        if (status != null) {
            utils.where("AND status=?");
            args.add(status);
        }
        utils.where("AND createTime BETWEEN ? AND ?");
        args.add(beginDate);
        args.add(endDate);
        utils.where(" AND account=?");
        args.add(account);
        utils.orderBy(" createTime DESC");
        String sql = utils.getSelect();
        return this.dbSession.list(sql, args.toArray(), this.cls);
    }

    public List<ActivityUser> listByUserEx(Integer activityId, String account, Integer status) {
        SQLUtils utils = new SQLUtils(this.tableName).where("1=1");
        List<Object> args = new ArrayList<>();
        if (!StrUtils.hasEmpty(activityId)) {
            utils.where("AND activityId=?");
            args.add(activityId);
        }
        if (status != null) {
            utils.where("AND status=?");
            args.add(status);
        }
        utils.where(" AND account=?");
        args.add(account);
        utils.orderBy(" createTime DESC");
        String sql = utils.getSelect();
        return this.dbSession.list(sql, args.toArray(), this.cls);
    }

    public ActivityUser find(String activityId, String account) {
        String sql = String.format("SELECT * FROM %s WHERE activityId=? AND account=? ORDER BY id DESC LIMIT 1", this.tableName);
        return this.dbSession.getObject(sql, new Object[]{activityId, account}, this.cls);
    }

    public ActivityUser findByActivityId(Integer activityId, String account) {
        String sql = String.format("SELECT * FROM %s WHERE activityId=? AND account=? ORDER BY id DESC LIMIT 1", this.tableName);
        return this.dbSession.getObject(sql, new Object[]{activityId, account}, this.cls);
    }

    public int getCountAccount(Integer activityId, String account) {
        SQLUtils utils = new SQLUtils(this.tableName).field("count(1)").where("activityId=?");
        List<Object> args = new ArrayList<>();
        args.add(activityId);
        if (!StrUtils.hasEmpty(account)) {
            utils.where("AND account=?");
            args.add(account);
        }
        String sql = utils.getSelect();
        return this.dbSession.getInt(sql, args.toArray());
    }

    public int setStatus(String key, String account, Integer oldStatus, Integer newStatus, String operator) {
        String sql = new SQLUtils(this.tableName).field("status,operator").where("activityId=? AND account=? AND status=? AND (operator='' OR operator=?)").getUpdate();
        return this.dbSession.update(sql, new Object[]{newStatus, operator, key, account, oldStatus, operator});
    }

    public int setStatus(Integer id, Integer oldStatus, Integer newStatus, String operator) {
        String sql = new SQLUtils(this.tableName).field("status,operator").where("id=? AND status=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{newStatus, operator, id, oldStatus});
    }

    public ActivityUser hasAttendOrPrize(Integer activityId, String beginTime, String endTime, String account) {
        String sql = new SQLUtils(this.tableName).where("activityId=? AND account=? AND (createTime between ? AND ?) ORDER BY createTime LIMIT 1 FOR UPDATE").getSelect();
        Object[] args = {activityId, account, beginTime, endTime};
        return this.dbSession.getObject(sql, args, this.cls);
    }

    public ActivityUser hasAttendOrPrizeByItem(Integer activityId, String beginTime, String endTime, String account, String item) {
        String sql = new SQLUtils(this.tableName).where("activityId=? AND account=? AND (createTime between ? AND ?) AND item=? ORDER BY createTime LIMIT 1 FOR UPDATE").getSelect();
        Object[] args = {activityId, account, beginTime, endTime, item};
        return this.dbSession.getObject(sql, args, this.cls);
    }
}
