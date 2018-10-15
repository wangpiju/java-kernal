package com.hs3.dao.userAgents;

import com.hs3.dao.BaseDao;
import com.hs3.db.Page;
import com.hs3.entity.userAgents.UserAgentsDailyMg;
import com.hs3.utils.StrUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository("userAgentsDailyMgDao")
public class UserAgentsDailyMgDao extends BaseDao<UserAgentsDailyMg> {


    public void save(UserAgentsDailyMg m) {
        this.dbSession.update("INSERT INTO t_user_agents_daily_mg(reportDate,account,parentAccount,distributionAmount,actualSalesVolume,actualActiveNumber,programId,programName,salesVolume,proportion,activeNumber,cycle,status,createTime) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[]{m.getReportDate(), m.getAccount(), m.getParentAccount(), m.getDistributionAmount(), m.getActualSalesVolume(), m.getActualActiveNumber(), m.getProgramId(), m.getProgramName(), m.getSalesVolume(), m.getProportion(), m.getActiveNumber(), m.getCycle(), m.getStatus(), new Date()});
    }

    public int update(UserAgentsDailyMg m) {
        return this.dbSession.update("UPDATE t_user_agents_daily_mg SET status=?,modifyTime=?,operator=?,remarks=? WHERE id=?",
                new Object[]{m.getStatus(),new Date(),m.getOperator(),m.getRemarks(), m.getId()});
    }


    /**
     * 查询
     *
     * @param account               用户名称
     * @param parentAccount         上级
     * @param programName           方案名称
     * @param begin                 计算日期-开始
     * @param end                   计算日期-结束
     * @param startTime             修改时间-开始
     * @param endTime               修改时间-结束
     * @param status                状态 0：待处理、1：派发、2：不予派发
     * @param p                     分页对象
     * @return
     */
    public List<UserAgentsDailyMg> userAgentsDailyMgList(String account, String parentAccount, String programName, String begin, String end, Date startTime, Date endTime, Integer status, Page p) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT ";
        sql = sql + " * ";
        sql = sql + " from t_user_agents_daily_mg ";
        sql = sql + " where 1 = 1 ";

        if (!StrUtils.hasEmpty(new Object[]{account})) {
            sql = sql + " AND account like ? ";
            args.add("%" + account + "%");
        }

        if (!StrUtils.hasEmpty(new Object[]{parentAccount})) {
            sql = sql + " AND parentAccount like ? ";
            args.add("%" + parentAccount + "%");
        }

        if (!StrUtils.hasEmpty(new Object[]{programName})) {
            sql = sql + " AND programName like ? ";
            args.add("%" + programName + "%");
        }

        if (!StrUtils.hasEmpty(new Object[]{begin})) {
            sql = sql + " AND reportDate >= ? ";
            args.add(begin);
        }

        if (!StrUtils.hasEmpty(new Object[]{end})) {
            sql = sql + " AND reportDate <= ? ";
            args.add(end);
        }

        if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            sql = sql + " AND modifyTime >= ? ";
            args.add(startTime);
        }

        if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            sql = sql + " AND modifyTime <= ? ";
            args.add(endTime);
        }

        if (!StrUtils.hasEmpty(new Object[]{status})) {
            sql = sql + " AND status = ? ";
            args.add(status);
        }

        sql = sql + " ORDER BY id desc ";

        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, UserAgentsDailyMg.class, p);
    }


    public int delete(String reportDate) {
        String sql = "DELETE FROM " + this.tableName + " WHERE reportDate=? and (status = null or status = 0)";
        return this.dbSession.update(sql, new Object[]{reportDate});
    }




}
