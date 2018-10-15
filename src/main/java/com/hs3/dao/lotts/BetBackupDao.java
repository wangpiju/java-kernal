package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.lotts.BetBackup;
import com.hs3.models.CommonModel;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("betBackup")
public class BetBackupDao
        extends BaseDao<BetBackup> {
    private static final String SELECT_FIELD = "id,lotteryId,playerId,seasonId,account,lotteryName,playName,price,unit,amount,status,bonusType,betCount,createTime,lastTime,openNum,groupName,win,bonusRate,traceId,isTrace,test,theoreticalBonus,traceWinStop,CASE WHEN LENGTH(content)>1500 THEN CONCAT(LEFT(content,1500),'...') ELSE content END  as content";

    public String findContentNext(String id, Integer i) {
        if ((i == null) || (i.intValue() == 0)) {
            i = Integer.valueOf(1);
        }
        Integer index = Integer.valueOf(i.intValue() * 1500 + 1);
        String sql = new SQLUtils(this.tableName)
                .field("CASE WHEN LENGTH(SUBSTRING(content,?))>1500 THEN CONCAT(SUBSTRING(content,?,1500),'...') ELSE SUBSTRING(content,?,1500) END  as content")
                .where("id=?").getSelect();
        return this.dbSession.getString(sql, new Object[]{index, index, index, id});
    }

    public List<BetBackup> adminList(Page p, CommonModel m) {
        List<Object> args = new ArrayList();
        StringBuffer buffer = new StringBuffer(" select id,lotteryId,playerId,seasonId,account,lotteryName,playName,price,unit,amount,status,bonusType,betCount,createTime,lastTime,openNum,groupName,win,bonusRate,traceId,isTrace,test,theoreticalBonus,traceWinStop,CASE WHEN LENGTH(content)>1500 THEN CONCAT(LEFT(content,1500),'...') ELSE content END  as content from " + this.tableName + " where 1=1 ");
        if (!StrUtils.hasEmpty(new Object[]{m.getAccount()})) {
            if ((m.getInclude() == null) || (m.getInclude().intValue() == 0)) {
                buffer.append(" and account =?");
                args.add(m.getAccount());
            } else if (m.getInclude().intValue() == 1) {
                buffer.append("  and account in (SELECT account FROM t_user WHERE parentList LIKE CONCAT((SELECT parentList FROM t_user u WHERE u.account=?),'%'))");

                args.add(m.getAccount());
            }
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getLotteryId()})) {
            buffer.append(" and lotteryId =?");
            args.add(m.getLotteryId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStartSeasonId(), m.getEndSeasonId()})) {
            buffer.append(" and seasonId  between ? and ?");
            args.add(m.getStartSeasonId());
            args.add(m.getEndSeasonId());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getEndSeasonId()})) {
            buffer.append(" and seasonId <=?");
            args.add(m.getEndSeasonId());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getStartSeasonId()})) {
            buffer.append(" and seasonId >=?");
            args.add(m.getStartSeasonId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getGroupName()})) {
            buffer.append(" and groupName =?");
            args.add(m.getGroupName());
        }
        if ((m.getTest() != null) && (m.getTest().intValue() != 2)) {
            buffer.append(" and test =?");
            args.add(m.getTest());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getStartTime(), m.getEndTime()})) {
            buffer.append(" and createTime  between ? and ?");
            args.add(m.getStartTime());
            args.add(m.getEndTime());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getEndTime()})) {
            buffer.append(" and createTime <=?");
            args.add(m.getEndTime());
        } else if (!StrUtils.hasEmpty(new Object[]{m.getStartTime()})) {
            buffer.append(" and createTime >=?");
            args.add(m.getStartTime());
        }
        buffer.append(" order by createTime desc,seasonId desc ");
        String sql = buffer.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, p);
    }
}
