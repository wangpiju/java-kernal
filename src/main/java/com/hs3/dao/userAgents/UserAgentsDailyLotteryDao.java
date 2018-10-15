package com.hs3.dao.userAgents;

import com.hs3.dao.BaseDao;
import com.hs3.db.Page;
import com.hs3.entity.userAgents.UserAgentsDaily;
import com.hs3.entity.userAgents.UserAgentsDailyLottery;
import com.hs3.utils.StrUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("userAgentsDailyLotteryDao")
public class UserAgentsDailyLotteryDao extends BaseDao<UserAgentsDailyLottery> {


    public void save(UserAgentsDailyLottery m) {
        this.dbSession.update("INSERT INTO t_user_agents_daily_lottery(programName,lotteryId,lotteryName,salesVolume,proportion,activeNumber,cycle,status,hint) VALUES(?,?,?,?,?,?,?,?,?)",
                new Object[]{m.getProgramName(), m.getLotteryId(), m.getLotteryName(), m.getSalesVolume(), m.getProportion(), m.getActiveNumber(), m.getCycle(), m.getStatus(), m.getHint()});
    }

    public int update(UserAgentsDailyLottery m) {
        return this.dbSession.update("UPDATE t_user_agents_daily_lottery SET salesVolume=?,proportion=?,activeNumber=?,cycle=?,status=?,hint=? WHERE id=?",
                new Object[]{m.getSalesVolume(),m.getProportion(),m.getActiveNumber(),m.getCycle(),m.getStatus(),m.getHint(), m.getId()});
    }


    /**
     * 查询
     *
     * @param programName           方案名称
     * @param status                状态 0：关闭、1：开启
     * @param sorting               排序对象 1：销量倒序、2：活跃人数倒序
     * @param p                     分页对象
     * @return
     */
    public List<UserAgentsDailyLottery> userAgentsDailyLotteryList(String programName, Integer status, Integer sorting, Page p) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT ";
        sql = sql + " * ";
        sql = sql + " from t_user_agents_daily_lottery ";
        sql = sql + " where 1 = 1 ";

        if (!StrUtils.hasEmpty(new Object[]{programName})) {
            sql = sql + " AND programName like ? ";
            args.add("%" + programName + "%");
        }

        if (!StrUtils.hasEmpty(new Object[]{status})) {
            sql = sql + " AND status = ? ";
            args.add(status);
        }

        if (!StrUtils.hasEmpty(new Object[]{sorting})) {
            if(sorting == 1) {
                sql = sql + " ORDER BY salesVolume desc ";
            }else if(sorting == 2){
                sql = sql + " ORDER BY activeNumber desc ";
            }else{
                sql = sql + " ORDER BY id desc ";
            }
        }else{
            sql = sql + " ORDER BY id desc ";
        }

        Object[] arg = args.toArray();
        return this.dbSession.list(sql, arg, UserAgentsDailyLottery.class, p);
    }



}
