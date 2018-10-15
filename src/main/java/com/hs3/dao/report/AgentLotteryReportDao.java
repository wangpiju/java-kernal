package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.entity.report.AgentLotteryReport;
import com.hs3.utils.StrUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("agentLotteryReportDao")
public class AgentLotteryReportDao extends BaseDao<AgentLotteryReport> {

    /**
     * 代理彩种报表
     *
     * @param account           会员账户
     * @param lotteryIdArr      彩种IDs
     * @param startTime         开始时间
     * @param endTime           结束时间
     * @return
     */
    public List<AgentLotteryReport> agentLotteryReportList(String account, String[] lotteryIdArr, Date startTime, Date endTime) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT ";
        sql = sql + " ac.changeUser as account, ";
        sql = sql + " ac.lotteryId, ";
        sql = sql + " ac.lotteryName, ";
        sql = sql + " (ifnull(ac.winningAmount,0.00) - ifnull(ac.cancelAmount,0.00)) as winningAmount, ";
        sql = sql + " (ifnull(ac.rebateAmount,0.00) - ifnull(ac.withdrawalSysAmount,0.00)) as rebateAmount, ";
        sql = sql + " (ifnull(ac.betAmount,0.00) - ifnull(ac.wofwAmount,0.00)) as betAmount ";
        sql = sql + " FROM( ";
        sql = sql + " select ";
        sql = sql + " changeUser,lotteryId,lotteryName, ";
        sql = sql + " sum(IF(accountChangeTypeId = 3, changeAmount, 0)) as rebateAmount, ";
        sql = sql + " sum(IF(accountChangeTypeId = 4, changeAmount, 0)) as winningAmount, ";
        sql = sql + " sum(IF(accountChangeTypeId = 7, changeAmount, 0)) as wofwAmount, ";
        sql = sql + " -(sum(IF(accountChangeTypeId = 1, changeAmount, 0))) as betAmount, ";
        sql = sql + " -(sum(IF(accountChangeTypeId = 9, changeAmount, 0))) as withdrawalSysAmount, ";
        sql = sql + " -(sum(IF(accountChangeTypeId = 10, changeAmount, 0))) as cancelAmount ";
        sql = sql + " from t_amount_change ";
        sql = sql + " where 1=1 ";

        //sql = sql + " and changeTime >= '2018-08-20 00:00:00' and changeTime <= '2018-08-20 23:59:59' ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }

        //sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = '') ";
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " and changeUser in (select tus.subSetAccount from t_user_subset as tus where tus.account = ?) ";
            args.add(account);
        }

        //sql = sql + " and lotteryId = 'dfk3' ";
        if(!StrUtils.hasEmpty(new Object[]{lotteryIdArr})){
            sql = sql + " AND lotteryId in (";
            for(int i = 0; i<lotteryIdArr.length; i++){
                if(i == (lotteryIdArr.length - 1)) {
                    sql = sql + "'" + lotteryIdArr[i] + "'";
                }else {
                    sql = sql + "'" + lotteryIdArr[i] + "'" + ",";
                }
            }
            sql = sql + ") ";
        }

        sql = sql + " and test = 0 ";
        sql = sql + " group by changeUser,lotteryId ";
        sql = sql + " ) as ac  ";
        sql = sql + " ORDER BY betAmount desc  ";

        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, AgentLotteryReport.class, null);
    }




}
