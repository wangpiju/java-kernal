package com.hs3.dao.risk;

import com.hs3.dao.BaseDao;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.BonusRisk;
import com.hs3.entity.lotts.BonusRiskConfig;
import com.hs3.models.lotts.BonusRiskEx;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-06-26 10:29
 **/
@Repository("bonusRiskConfigDao")
public class BonusRiskConfigDao extends BaseDao<BonusRiskConfig> {

    public void save(BonusRiskConfig brc) {
        if (brc.getId() != null) {
            BonusRiskConfig config = this.find(brc.getId());
            if (config != null) {
                StringBuilder fileSql = new StringBuilder();
                List<Object> params = new ArrayList<>();

                if (brc.getLotteryId() != null) {
                    fileSql.append("lotteryId,");
                    params.add(brc.getLotteryId());
                }
                if (brc.getDayFlowWater() != null) {
                    fileSql.append("dayFlowWater,");
                    params.add(brc.getDayFlowWater());
                }
                if (brc.getInitBonusPool() != null) {
                    fileSql.append("initBonusPool,");
                    params.add(brc.getInitBonusPool());
                }
                if (brc.getRevenueRate() != null) {
                    fileSql.append("revenueRate,");
                    params.add(brc.getRevenueRate());
                }
                if (brc.getLoseRate() != null) {
                    fileSql.append("loseRate,");
                    params.add(brc.getLoseRate());
                }
                if (brc.getStatus() != null) {
                    fileSql.append("status,");
                    params.add(brc.getStatus());
                }
                if (brc.getUpdateTime() != null) {
                    fileSql.append("updateTime,");
                    params.add(brc.getUpdateTime());
                }
                if (brc.getCollectCount() != null) {
                    fileSql.append("collectCount,");
                    params.add(brc.getCollectCount());
                }
                params.add(brc.getLotteryId());
                String sql = new SQLUtils(this.tableName).field(fileSql.substring(0, fileSql.length() - 1)).where("lotteryId=?").getUpdate();

                this.dbSession.update(sql, params.toArray());
            }
        } else {
            String sql = new SQLUtils(this.tableName).field("lotteryId, dayFlowWater, initBonusPool, revenueRate, loseRate, status, createTime, updateTime, collectCount").getInsert();
            this.dbSession.update(sql, new Object[]{
                    brc.getLotteryId(),
                    brc.getDayFlowWater(),
                    brc.getInitBonusPool(),
                    brc.getRevenueRate(),
                    brc.getLoseRate(),
                    brc.getStatus(),
                    brc.getCreateTime(),
                    brc.getUpdateTime(),
                    brc.getCollectCount()
            });
        }

    }

    public BonusRiskConfig findByLotteryId(String lotteryId) {
        String sql = new SQLUtils(this.tableName).where("lotteryId=?").getSelect();
        return this.dbSession.getObject(sql, new Object[]{lotteryId}, this.cls);
    }
}
