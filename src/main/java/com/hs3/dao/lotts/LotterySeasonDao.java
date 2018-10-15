package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.lotts.LotterySeason;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("lotterySeasonDao")
public class LotterySeasonDao
        extends BaseDao<LotterySeason> {
    public void save(LotterySeason m) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", new Object[]{this.tableName, "seasonId,lotteryId,n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,openTime,addTime",
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?"});
        this.dbSession.update(
                sql,
                new Object[]{m.getSeasonId(), m.getLotteryId(), m.getN1(), m.getN2(), m.getN3(), m.getN4(), m.getN5(), m.getN6(), m.getN7(),
                        m.getN8(), m.getN9(), m.getN10(), m.getOpenTime(), m.getAddTime()});
    }

    public void update(LotterySeason m) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "n1=?,n2=?,n3=?,n4=?,n5=?,n6=?,n7=?,n8=?,n9=?,n10=?,openTime=?,addTime=?",
                "seasonId=? AND lotteryId=?"});
        this.dbSession.update(
                sql,
                new Object[]{m.getN1(), m.getN2(), m.getN3(), m.getN4(), m.getN5(), m.getN6(), m.getN7(), m.getN8(), m.getN9(), m.getN10(),
                        m.getOpenTime(), m.getAddTime(), m.getSeasonId(), m.getLotteryId()});
    }

    public List<LotterySeason> list(String lotteryId, Page p) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE lotteryId=? ORDER BY seasonId DESC"});
        return this.dbSession.list(sql, new Object[]{lotteryId}, this.cls, p);
    }

    public List<LotterySeason> getLast(String lotteryId, int num) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE lotteryId=? ORDER BY seasonId DESC LIMIT ?"});
        return this.dbSession.list(sql, new Object[]{lotteryId, Integer.valueOf(num)}, this.cls);
    }

    public List<LotterySeason> getALLLast( int num ) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, " GROUP BY lotteryId ORDER BY seasonId DESC LIMIT ?"});
        return this.dbSession.list(sql, new Object[]{Integer.valueOf(num)}, this.cls);
    }

    public LotterySeason getBylotteryIdAndSeason(String lotteryId, String season) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE lotteryId=? AND seasonId=?"});
        return (LotterySeason) this.dbSession.getObject(sql, new Object[]{lotteryId, season}, this.cls);
    }

    public void delete(String lotteryId, String season) {
        String sql = String.format("DELETE FROM %s WHERE lotteryId=? and seasonId=?", new Object[]{this.tableName});
        this.dbSession.update(sql, new Object[]{lotteryId, season});
    }

    public int count(String lotteryId, String seasonIdBegin, String seasonIdEnd) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"COUNT(1)", this.tableName, "WHERE lotteryId=? AND seasonId >= ? and seasonId <= ?"});
        return this.dbSession.getInt(sql, new Object[]{lotteryId, seasonIdBegin, seasonIdEnd}).intValue();
    }

    public int deleteByLotteryId(String id) {
        String sql = "DELETE FROM " + this.tableName + " WHERE lotteryId=?";
        return this.dbSession.update(sql, new Object[]{id});
    }
}
