package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.LotteryCrawlerConfig;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("lotteryCrawlerConfigDao")
public class LotteryCrawlerConfigDao
        extends BaseDao<LotteryCrawlerConfig> {
    public void save(LotteryCrawlerConfig m) {
        String sql = new SQLUtils(this.tableName).field("lotteryId,url,urlBuilder,regex,weight,craType,status").getInsert();
        this.dbSession.update(sql, new Object[]{
                m.getLotteryId(),
                m.getUrl(),
                m.getUrlBuilder(),
                m.getRegex(),
                m.getWeight(),
                m.getCraType(),
                m.getStatus()});
    }

    public int update(LotteryCrawlerConfig m) {
        String sql = new SQLUtils(this.tableName).field("lotteryId,url,urlBuilder,regex,weight,craType,status").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{
                m.getLotteryId(),
                m.getUrl(),
                m.getUrlBuilder(),
                m.getRegex(),
                m.getWeight(),
                m.getCraType(),
                m.getStatus(),
                m.getId()});
    }

    public List<LotteryCrawlerConfig> listByLotteryId(String lotteryId, Page p) {
        String sql = new SQLUtils(this.tableName).where("lotteryId=?").getSelect();
        return this.dbSession.list(sql, new Object[]{lotteryId}, this.cls, p);
    }

    public List<LotteryCrawlerConfig> listByLotteryId(String lotteryId, Integer status) {
        String sql = new SQLUtils(this.tableName).where("lotteryId=? AND status=?").getSelect();
        return this.dbSession.list(sql, new Object[]{lotteryId, status}, this.cls);
    }

    public List<LotteryCrawlerConfig> listByLotteryIdAndStatus(String lotteryId, Integer status, Integer type) {
        String sql = new SQLUtils(this.tableName).where("lotteryId=? AND status=? AND craType=?").orderBy("id DESC").getSelect();
        return this.dbSession.list(sql, new Object[]{lotteryId, status, type}, this.cls);
    }

    public int deleteByLotteryId(String id) {
        String sql = "DELETE FROM " + this.tableName + " WHERE lotteryId=?";
        return this.dbSession.update(sql, new Object[]{id});
    }
}
