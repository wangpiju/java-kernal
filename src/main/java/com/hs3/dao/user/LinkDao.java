package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.users.Link;
import com.hs3.utils.DateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("linkDao")
public class LinkDao
        extends BaseDao<Link> {
    public List<Link> list(String user, String url, Date date_from, Date date_to, String bonus, BigDecimal ratio, String regTime, Page page) {
        StringBuilder sb = new StringBuilder();
        List<Object> args = new ArrayList();
        if ((user != null) && (user != "")) {
            sb.append(" and account=?");
            args.add(user);
        }
        if ((url != null) && (url != "")) {
            sb.append(" and url like ?");
            args.add('%' + url + '%');
        }
        if ((date_from != null) && (date_to != null)) {
            sb.append(" and createUrlTime between  ? and ?");
            args.add(DateUtils.format(date_from));
            args.add(DateUtils.format(date_to));
        }
        if ((bonus != null) && (bonus != "")) {
            sb.append(" and bonusGroupId=?");
            args.add(bonus);
        }
        if (ratio != null) {
            sb.append(" and bonusRatio=?");
            args.add(ratio);
        }
        if ((regTime != null) && (regTime != "")) {
            Date d = DateUtils.getToDay(Integer.parseInt(regTime) * -1);
            sb.append("and lastRegisterTime>=?");
            args.add(d);
        }
        String sql = "select a.id, account,parentAccount,createUrlTime,registerCount,rechargeCount,url,title as bonusGroupName ,a.bonusRatio,status,lastRegisterTime  from t_link a left join t_bonus_group b on a.bonusGroupId=b.id where 1=1 " +
                sb;
        return this.dbSession.list(sql, args.toArray(), Link.class, page);
    }

    public int setStatus(Integer id, Integer status) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", new Object[]{this.tableName, "status=?", "id=?"});
        return this.dbSession.update(sql, new Object[]{status, id});
    }
}
