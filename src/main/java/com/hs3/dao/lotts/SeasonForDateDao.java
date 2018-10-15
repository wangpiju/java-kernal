package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.SeasonForDate;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("seasonForDate")
public class SeasonForDateDao
        extends BaseDao<SeasonForDate> {
    public int update(SeasonForDate s) {
        String sql = new SQLUtils(this.tableName).field("lotteryId,seasonRule,seasonDate,firstSeason,lastTime,autoCreateDay").where("id=?").getUpdate();
        return this.dbSession.update(sql,
                new Object[]{s.getLotteryId(), s.getSeasonRule(), s.getSeasonDate(), s.getFirstSeason(), s.getLastTime(), s.getAutoCreateDay(), s.getId()});
    }

    public void save(SeasonForDate s) {
        String sql = new SQLUtils(this.tableName).field("lotteryId,seasonRule,seasonDate,firstSeason,lastTime,autoCreateDay").getInsert();
        this.dbSession.update(sql,
                new Object[]{s.getLotteryId(), s.getSeasonRule(), s.getSeasonDate(), s.getFirstSeason(), s.getLastTime(), s.getAutoCreateDay()});
    }

    public SeasonForDate getByLotteryId(String lotteryId) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE lotteryId=? LIMIT 1"});
        return this.dbSession.getObject(sql, new Object[]{lotteryId}, this.cls);
    }

    public List<SeasonForDate> list(String lotteryId, Page p) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE lotteryId=? ORDER BY lotteryId"});
        return this.dbSession.list(sql, new Object[]{lotteryId}, this.cls, p);
    }

    public List<SeasonForDate> listAll(Page p) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, " ORDER BY lotteryId"});
        return this.dbSession.list(sql, this.cls, p);
    }

    public int deleteByLotteryId(String id) {
        String sql = "DELETE FROM " + this.tableName + " WHERE lotteryId=?";
        return this.dbSession.update(sql, new Object[]{id});
    }
}
