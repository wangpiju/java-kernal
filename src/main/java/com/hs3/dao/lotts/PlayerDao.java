package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.Player;
import com.hs3.models.lotts.PlayerBonus;
import com.hs3.models.lotts.PlayerModel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("playerDao")
public class PlayerDao
        extends BaseDao<Player> {
    public int update(Player m) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "saleStatus=?,remark=?,example=?", "id=? and lotteryId=?"});
        return this.dbSession.update(sql, new Object[]{m.getSaleStatus(), m.getRemark(), m.getExample(), m.getId(), m.getLotteryId()});
    }

    public void save(Player m) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", new Object[]{this.tableName, "id,lotteryId,title,saleStatus,remark,example", "?,?,?,?,?,?"});
        this.dbSession.update(sql,
                new Object[]{m.getId(), m.getLotteryId(), m.getTitle(), m.getSaleStatus(), m.getRemark(), m.getExample()});
    }

    public List<Player> listByLotteryId(String lotteryId) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE lotteryId=?"});
        return this.dbSession.list(sql, new Object[]{lotteryId}, this.cls);
    }

    public List<PlayerBonus> listFullByLotteryIdAndGroupId(String lotteryId, Integer groupId) {
        StringBuilder sb = new StringBuilder("SELECT a.bonusRatio,a.rebateRatio,a.bonus,p.* FROM t_bonus_group_details AS a ");
        sb.append(" LEFT JOIN t_player as p ");
        sb.append(" ON a.id=p.id AND a.lotteryId=p.lotteryId ");
        sb.append(" WHERE a.lotteryId=? AND a.bonusGroupId=?");
        String sql = sb.toString();
        return this.dbSession.list(sql, new Object[]{lotteryId, groupId}, PlayerBonus.class);
    }

    public List<Player> listByLotteryIdAndStatus(String lotteryId, int status) {
        String sql = new SQLUtils(this.tableName).where("lotteryId=? AND saleStatus=?").getSelect();
        return this.dbSession.list(sql, new Object[]{lotteryId, Integer.valueOf(status)}, this.cls);
    }

    public Integer deleteByLotteryId(String lotteryId) {
        String sql = new SQLUtils(this.tableName).where("lotteryId=?").getDelete();
        return Integer.valueOf(this.dbSession.update(sql, new Object[]{lotteryId}));
    }

    public Integer updateAll(PlayerModel model, String lotteryId) {
        int count = 0;
        for (Player p : model.getDetails()) {
            List<Object> args = new ArrayList();
            SQLUtils su = new SQLUtils(this.tableName);
            su.field("remark,example").where("lotteryId=? AND id=?");
            args.add(p.getRemark());
            args.add(p.getExample());
            if (p.getSaleStatus() != null) {
                su.field("saleStatus");
                args.add(p.getSaleStatus());
            }
            if (p.getMobileStatus() != null) {
                su.field("mobileStatus");
                args.add(p.getMobileStatus());
            }
            args.add(lotteryId);
            args.add(p.getId());
            String sql = su.getUpdate();
            count += this.dbSession.update(sql, args.toArray());
        }
        return Integer.valueOf(count);
    }

    public Integer findByIdAndLotteryIdCount(String id, String lotteryId) {
        String sql = new SQLUtils(this.tableName).field("count(1)").where("id=? AND lotteryId=?").getSelect();
        return this.dbSession.getInt(sql, new Object[]{id, lotteryId});
    }

    public Integer getStatus(String playId, String lotteryId) {
        String sql = new SQLUtils(this.tableName).field("saleStatus").where("id=? AND lotteryId=?").getSelect();
        return this.dbSession.getInt(sql, new Object[]{playId, lotteryId});
    }
}
