package com.hs3.dao.risk;

import com.hs3.dao.BaseDao;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.BonusRisk;
import com.hs3.models.lotts.BonusRiskEx;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

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
@Repository("bonusRiskDao")
public class BonusRiskDao extends BaseDao<BonusRisk> {

    public void save(BonusRisk br) {
        String sql = new SQLUtils(this.tableName).field("id,lotteryId,seasonId,initNum,endNum,createNumCount,validBetAmount,rebate,initBonus,initGains,endBonus,endGains,bonusPoolLeft,createTime,isChangeNum,secondBonusPoolLeft,collectCount").getInsert();
        this.dbSession.update(sql, new Object[]{
                br.getId(),
                br.getLotteryId(),
                br.getSeasonId(),
                br.getInitNum(),
                br.getEndNum(),
                br.getCreateNumCount(),
                br.getValidBetAmount(),
                br.getRebate(),
                br.getInitBonus(),
                br.getInitGains(),
                br.getEndBonus(),
                br.getEndGains(),
                br.getBonusPoolLeft(),
                br.getCreateTime(),
                br.getIsChangeNum(),
                br.getSecondBonusPoolLeft(),
                br.getCollectCount()
        });
    }

    public List<BonusRisk> list(BonusRiskEx r, Page page) {
        String sql = new SQLUtils(this.tableName).getSelect();
        List<Object> list = new ArrayList<>();
        StringBuilder whereSql = new StringBuilder();
        if (r != null) {
            if (StringUtils.isNotBlank(r.getLotteryId())) {
                whereSql.append(" AND lotteryId = ?");
                list.add(r.getLotteryId());
            }
            if (r.getBeginTime() != null) {
                whereSql.append(" AND createTime > ?");
                list.add(r.getBeginTime());
            }
            if (r.getEndTime() != null) {
                whereSql.append(" AND createTime < ?");
                list.add(r.getEndTime());
            }
            if (StringUtils.isNotBlank(r.getSeasonId())) {
                whereSql.append(" AND seasonId like ?");
                list.add(r.getSeasonId()+"%");
            }
            if (r.getIsChangeNum() != null) {
                whereSql.append(" AND isChangeNum = ? ");
                list.add(r.getIsChangeNum());
            }
        }
        if (list.size() > 0) {
            Pattern p = Pattern.compile("AND");
            Matcher m = p.matcher(whereSql);
            sql += m.replaceFirst("WHERE");
        }
        sql += " ORDER BY createTime desc ";
        return this.dbSession.list(sql, list.toArray(), this.cls, page);
    }

}
