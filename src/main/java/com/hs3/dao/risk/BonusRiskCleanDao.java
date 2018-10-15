package com.hs3.dao.risk;

import com.hs3.dao.BaseDao;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.BonusRisk;
import com.hs3.entity.lotts.BonusRiskClean;
import org.springframework.stereotype.Repository;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-08-25 15:53
 **/
@Repository
public class BonusRiskCleanDao extends BaseDao<BonusRiskClean> {

    public void save(BonusRiskClean br) {
        String sql = new SQLUtils(this.tableName).field("id,lotteryId,cleanAmount,beforeBonusPool,operateUser,createTime").getInsert();
        this.dbSession.update(sql, new Object[]{
                br.getId(),
                br.getLotteryId(),
                br.getCleanAmount(),
                br.getBeforeBonusPool(),
                br.getOperateUser(),
                br.getCreateTime()
        });
    }

}
