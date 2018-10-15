package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.Lottery;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("lotteryDao")
public class LotteryDao
        extends BaseDao<Lottery> {
    public int update(Lottery m) {
        String sql = new SQLUtils(this.tableName).field("title,status,orderId,groupName,groupId,seasonRule,weight,maxPlan,isHot,isSelf,isNew,showGroup,mobileStatus,betInStatus,ReSettleTime,remark,lotteryGroupId").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{
                m.getTitle(),
                m.getStatus(),
                m.getOrderId(),
                m.getGroupName(),
                m.getGroupId(),
                m.getSeasonRule(),
                m.getWeight(),
                m.getMaxPlan(),
                m.getIsHot(),
                m.getIsSelf(),
                m.getIsNew(),
                m.getShowGroup(),
                m.getMobileStatus(),
                m.getBetInStatus(),
                m.getReSettleTime(),
                m.getRemark(),
                m.getLotteryGroupId(),
                m.getId()});
    }

    public void save(Lottery m) {
        String sql = new SQLUtils(this.tableName).field("id,title,status,orderId,groupName,groupId,seasonRule,weight,maxPlan,isHot,isSelf,isNew,showGroup,mobileStatus,betInStatus,ReSettleTime,remark,lotteryGroupId").getInsert();
        this.dbSession.update(sql, new Object[]{
                m.getId(),
                m.getTitle(),
                m.getStatus(),
                m.getOrderId(),
                m.getGroupName(),
                m.getGroupId(),
                m.getSeasonRule(),
                m.getWeight(),
                m.getMaxPlan(),
                m.getIsHot(),
                m.getIsSelf(),
                m.getIsNew(),
                m.getShowGroup(),
                m.getMobileStatus(),
                m.getBetInStatus(),
                m.getReSettleTime(),
                m.getRemark(),
                m.getLotteryGroupId()});
    }

    public List<Lottery> listByOrder(Page p) {
        String sql = "SELECT * FROM " + this.tableName + " ORDER BY groupId,orderId";
        return this.dbSession.list(sql, this.cls, p);
    }

    public List<Lottery> getListByGroupName(String groupName) {
        if (groupName != null) {
            String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE groupName=?"});
            return this.dbSession.list(sql, new Object[]{groupName}, this.cls);
        }
        String sql = "select * from " + this.tableName;
        return this.dbSession.list(sql, this.cls);
    }

    public boolean updateStatus(String lotteryId, Integer Status) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "status=?", "id=?"});
        return 1 == this.dbSession.update(sql, new Object[]{Status, lotteryId});
    }

    public List<Lottery> listByStatus(int status) {
        String sql = new SQLUtils(this.tableName).where("status=?").orderBy("showGroup,groupId,orderId").getSelect();
        return this.dbSession.list(sql, new Object[]{Integer.valueOf(status)}, this.cls);
    }

    public List<Lottery> listAndOrder(Page p) {
        String sql = new SQLUtils(this.tableName).orderBy("weight, lotteryGroupId,showGroup,orderId").getSelect();
        return this.dbSession.list(sql, this.cls, p);
    }

    public List<Lottery> listByStatusOrderId(int status) {
        String sql = new SQLUtils(this.tableName).where("status=?").orderBy("orderId DESC").getSelect();
        return this.dbSession.list(sql, new Object[]{Integer.valueOf(status)}, this.cls);
    }

    public List<Lottery> listAndOrderField(String fields) {
        String sql = new SQLUtils(this.tableName).field(fields).orderBy("lotteryGroupId,showGroup,orderId").getSelect();
        return this.dbSession.list(sql, this.cls);
    }


    public List<Lottery> listByHotNewSelf() {
        String sql = "SELECT id,title FROM " + this.tableName + " ORDER BY isHot desc,isNew desc,isSelf desc";
        return this.dbSession.list(sql, this.cls);
    }


}
