package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.report.UserInReport;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("userInReportDao")
public class UserInReportDao
        extends BaseDao<UserInReport> {
    public boolean save(UserInReport m) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", new Object[]{this.tableName,
                "account,createDate,betAmount,winAmount,totalAmount,test",
                "?,?,?,?,?,?"});
        int id = this.dbSession.updateKeyHolder(sql, new Object[]{
                m.getAccount(),
                m.getCreateDate(),
                m.getBetAmount(),
                m.getWinAmount(),
                m.getTotalAmount(),
                m.getTest()});
        m.setId(id);
        return true;
    }

    public List<UserInReport> getListByCreateTime(String createTime, String endTime) {
        StringBuffer buffer = new StringBuffer(" SELECT changeUser as account ,test,date_add(curdate(),INTERVAL - 1 DAY) AS createDate, ");
        buffer.append(" - sum( CASE t.accountChangeTypeId WHEN '91' THEN t.changeAmount ELSE 0 END) AS betAmount,");
        buffer.append(" sum(CASE t.accountChangeTypeId WHEN '92' THEN t.changeAmount ELSE\t0\tEND) AS winAmount,");
        buffer.append(" (sum(CASE t.accountChangeTypeId WHEN '92' THEN t.changeAmount ELSE\t0\tEND)+sum( CASE t.accountChangeTypeId WHEN '91' THEN t.changeAmount ELSE 0 END)) as totalAmount ");
        buffer.append(" FROM t_amount_change t ");
        buffer.append(" where changeTime >= ? and changeTime<=? ");
        buffer.append(" GROUP BY changeUser,test ");
        buffer.append(" HAVING betAmount > 0 ");
        String sql = buffer.toString();
        Object[] argsObjects = null;
        argsObjects = new Object[]{createTime, endTime};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public List<UserInReport> historyList(Page page, UserInReport m, String startTime, String endTime) {
        List<Object> args = new ArrayList();
        StringBuilder buffer = new StringBuilder(" select * from " + this.tableName + " where 1=1");
        buffer.append(" and account = ?");
        args.add(m.getAccount());
        if (!StrUtils.hasEmpty(new Object[]{startTime, endTime})) {
            buffer.append(" and createDate >=? and createDate <= ?");
            args.add(startTime);
            args.add(endTime);
        } else if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            buffer.append(" and createDate <=?");
            args.add(endTime);
        } else if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            buffer.append(" and createDate >=?");
            args.add(startTime);
        }
        buffer.append(" ORDER BY createDate desc");
        String sql = buffer.toString();
        return this.dbSession.list(sql, args.toArray(), this.cls, page);
    }

    public List<UserInReport> getAccountByCreateTime(String createTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select t.account from " + this.tableName + " t where date_sub(t.createDate,interval -1 day)=str_to_date(?, '%Y-%m-%d') ");
        String sql = sb.toString();
        Object[] argsObjects = {createTime};
        return this.dbSession.list(sql, argsObjects, this.cls);
    }

    public int getCountByCreatDate(String createDate) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"count(1)", this.tableName, "WHERE date_sub(createDate,interval -1 day)=str_to_date(?, '%Y-%m-%d')"});
        Object[] args = {createDate};
        return this.dbSession.getInt(sql, args).intValue();
    }
}
