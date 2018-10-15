package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.LotteryGroup;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("lotteryGroupDao")
public class LotteryGroupDao
        extends BaseDao<LotteryGroup> {
    public int update(LotteryGroup m) {
        String sql = new SQLUtils(this.tableName).field("name,icon,status,orderId").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{
                m.getName(),
                m.getIcon(),
                m.getStatus(),
                m.getOrderId(),
                m.getId()});
    }

    public int save(LotteryGroup m) {
        String sql = new SQLUtils(this.tableName).field("id,name,icon,status,orderId").getInsert();
        return this.dbSession.update(sql, new Object[]{
                m.getId(),
                m.getName(),
                m.getIcon(),
                m.getStatus(),
                m.getOrderId()});
    }

    public List<LotteryGroup> listByOrder(Page p, Integer status) {
        SQLUtils s = new SQLUtils(this.tableName).field("id,name,icon,status,orderId");
        Object[] args = null;
        if (status != null) {
            s.where("status=?");
            args = new Object[]{status};
        }
        s.orderBy("orderId DESC");
        String sql = s.getSelect();
        return this.dbSession.listAndPage(sql, args, s.getCount(), args, this.cls, p);
    }
}
