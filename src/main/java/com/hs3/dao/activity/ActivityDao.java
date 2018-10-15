package com.hs3.dao.activity;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.activity.Activity;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("activityDao")
public class ActivityDao
        extends BaseDao<Activity> {
    public int update(Activity activity) {
        SQLUtils su = new SQLUtils(this.tableName).field("title,beginTime,endTime,status,beginPrizeTime,endPrizeTime,activityUser")
                .field("prizeType,useRange,amountType,amount,maxAmount,remark,needAttend,needPrize,visibleRange,orderId,beginRegTime,endRegTime,changeRemark").where("id=?");

        List<Object> args = new ArrayList<>();
        args.add(activity.getTitle());
        args.add(activity.getBeginTime());
        args.add(activity.getEndTime());
        args.add(activity.getStatus());
        args.add(activity.getBeginPrizeTime());
        args.add(activity.getEndPrizeTime());
        args.add(activity.getActivityUser());
        args.add(activity.getPrizeType());
        args.add(activity.getUseRange());
        args.add(activity.getAmountType());
        args.add(activity.getAmount());
        args.add(activity.getMaxAmount());
        args.add(activity.getRemark());
        args.add(activity.getNeedAttend());
        args.add(activity.getNeedPrize());
        args.add(activity.getVisibleRange());
        args.add(activity.getOrderId());
        args.add(activity.getBeginRegTime());
        args.add(activity.getEndRegTime());
        args.add(activity.getChangeRemark());
        if (!StrUtils.hasEmpty(activity.getIcon())) {
            su.field("icon");
            args.add(activity.getIcon());
        }
        args.add(activity.getId());

        String sql = su.getUpdate();
        return this.dbSession.update(sql, args.toArray());
    }

    public int save(Activity a) {
        String sql = new SQLUtils(this.tableName)
                .field("activityType,icon,title,beginTime,endTime,status,beginPrizeTime,endPrizeTime,activityUser,prizeType,useRange,amountType,amount,maxAmount,remark," +
                        "needAttend,needPrize,visibleRange,orderId,beginRegTime,endRegTime,changeRemark")
                .getInsert();
        Object[] params = new Object[]{a.getActivityType(), a.getIcon(), a.getTitle(), a.getBeginTime(), a.getEndTime(), a.getStatus(), a.getBeginPrizeTime(),
                a.getEndPrizeTime(), a.getActivityUser(), a.getPrizeType(), a.getUseRange(), a.getAmountType(), a.getAmount(), a.getMaxAmount(), a.getRemark(),
                a.getNeedAttend(), a.getNeedPrize(), a.getVisibleRange(), a.getOrderId(), a.getBeginRegTime(), a.getEndRegTime(), a.getChangeRemark()};
        Integer id = dbSession.updateKeyHolder(sql, params);
        a.setId(id);
        return id;
    }

    public <T> T findToClass(Integer id, Class<T> clzz) {
        String sql = new SQLUtils(this.tableName).where("id=?").getSelect();
        return this.dbSession.getObject(sql, new Object[]{id}, clzz);
    }

    public List<Activity> listOrderBy(Page page) {
        String sql = "SELECT * FROM " + this.tableName + " ORDER BY orderId";
        return this.dbSession.list(sql, this.cls, page);
    }

    public List<Activity> listByActive(Integer visibleRange) {
        Date d = new Date();
        String sql = new SQLUtils(this.tableName).where("status=0 AND beginTime<=? AND endTime>=? AND (visibleRange=? OR visibleRange=2)").orderBy("orderId").getSelect();
        return this.dbSession.list(sql, new Object[]{d, d, visibleRange}, this.cls);
    }

    public int del(Integer id) {
        String sql = "DELETE FROM " + this.tableName + " WHERE id=?";
        return this.dbSession.update(sql, new Object[]{id});
    }
}
