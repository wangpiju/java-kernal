package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.models.report.Online;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("onlineDao")
public class OnlineDao extends BaseDao<Online> {

    public List<Online> list(Page page, String account, Integer userType, String ip, String info) {
        List<Object> obList = new ArrayList();
        String sql = "SELECT \t\ta.account,\t\ta.userType,\t\tb.ip,\t\tb.ipInfo,\t\ta.loginTime \tFROM t_user a \tLEFT JOIN t_user_login_ip b ON a.account=b.account AND a.loginTime = b.loginTime \tLEFT JOIN t_user_info c ON a.account=c.account ";
        String pageSql = "SELECT COUNT(1) FROM t_user a \tLEFT JOIN t_user_login_ip b ON a.account=b.account AND a.loginTime = b.loginTime \tLEFT JOIN t_user_info c ON a.account=c.account ";
        String where = " WHERE c.onlineTime>=? ";
        obList.add(DateUtils.addMinute(new Date(), -5));
        if ((account != null) && (!"".equals(account))) {
            where = where + " AND a.account=? ";
            obList.add(account);
        }
        if (userType != null) {
            where = where + " AND a.userType=? ";
            obList.add(userType);
        }
        if (!StrUtils.hasEmpty(new Object[]{ip})) {
            where = where + " AND b.ip = ?";
            obList.add(ip);
        }
        if (!StrUtils.hasEmpty(new Object[]{info})) {
            where = where + " AND b.ipInfo LIKE CONCAT('%',?,'%')";
            obList.add(info);
        }
        sql = sql + where + " ORDER BY a.loginTime DESC";
        pageSql = pageSql + where;
        Object[] args = obList.toArray();
        return this.dbSession.listAndPage(sql, args, pageSql, args, this.cls, page);
    }


    public Integer getOnlineUserNum() {
        List<Object> obList = new ArrayList();
        String sql = "SELECT COUNT(*) as onlineUserNum FROM t_user a LEFT JOIN t_user_login_ip b ON a.account=b.account AND a.loginTime = b.loginTime LEFT JOIN t_user_info c ON a.account=c.account ";
        String where = " WHERE a.test = 0 and c.onlineTime>=? ";
        Date onlineTime = DateUtils.addMinute(new Date(), -5);
        obList.add(onlineTime);
        sql = sql + where;
        Object[] args = obList.toArray();

        String onlineTimeStr = DateUtils.format(onlineTime);
        //System.out.println("===============查询在线人数当前时间=====================>>>>"+DateUtils.format(new Date()));
        //System.out.println("===============查询在线人数参数时间=====onlineTimeStr================>>>>"+onlineTimeStr);

        Map<String, Object> onlineUserNumMap = this.dbSession.getMap(sql, args);

        return Integer.valueOf(String.valueOf(onlineUserNumMap.get("onlineUserNum")));
    }


}
