package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.entity.lotts.BonusGroup;
import org.springframework.stereotype.Repository;

@Repository("bonusGroupDao")
public class BonusGroupDao
        extends BaseDao<BonusGroup> {
    public int update(BonusGroup m) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "title=?,bonusRatio=?,rebateRatio=?,noneMinRatio=?,userMinRatio=?,playerMaxRatio=?", "id=?"});
        return this.dbSession.update(sql, new Object[]{
                m.getTitle(),
                m.getBonusRatio(),
                m.getRebateRatio(),
                m.getNoneMinRatio(),
                m.getUserMinRatio(),
                m.getPlayerMaxRatio(),
                m.getId()});
    }

    public void save(BonusGroup m) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", new Object[]{this.tableName, "title,bonusRatio,rebateRatio,noneMinRatio,userMinRatio,playerMaxRatio", "?,?,?,?,?,?"});
        int id = this.dbSession.updateKeyHolder(sql, new Object[]{
                m.getTitle(),
                m.getBonusRatio(),
                m.getRebateRatio(),
                m.getNoneMinRatio(),
                m.getUserMinRatio(),
                m.getPlayerMaxRatio()});

        m.setId(Integer.valueOf(id));
    }

    public BonusGroup findById(int id) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE id=?"});
        Object[] args = {Integer.valueOf(id)};
        return this.dbSession.getObject(sql, args, this.cls);
    }

    public int findRecordById(int id) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"count(1)", this.tableName, "WHERE id=?"});
        Object[] args = {Integer.valueOf(id)};
        return this.dbSession.getInt(sql, args).intValue();
    }
}
