package com.hs3.dao.user;

import com.hs3.dao.BaseDao;
import com.hs3.db.SQLUtils;
import com.hs3.entity.users.UserNotice;
import com.hs3.utils.BeanZUtils;
import com.hs3.utils.StrUtils;
import com.hs3.utils.entity.NoticeInfo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Repository
public class UserNoticeDao
        extends BaseDao<UserNotice> {
    public void save(UserNotice m) {
        String sql = new SQLUtils(this.tableName).field(
                "account,content,betId,win,createTime").getInsert();
        this.dbSession.update(
                sql,
                new Object[]{m.getAccount(), m.getContent(), m.getBetId(),
                        m.getWin(), new Date()});
    }

    public List<UserNotice> listByAccount(String account, Date begin, int size) {
        String sql = "SELECT * FROM " +
                this.tableName +
                " WHERE account=? AND createTime>=? AND status=0 ORDER BY createTime LIMIT ?";

        Object[] args = {account, begin, Integer.valueOf(size)};
        return this.dbSession.list(sql, args, this.cls);
    }

    public int deleteOld(String account, Date begin) {
        String sql = new SQLUtils(this.tableName).where("account = ? AND createTime<=?").getDelete();
        return this.dbSession.update(sql, new Object[]{account, begin});
    }

    //**************************************以下为变更部分*****************************************

    public List<Map<String, Object>> listUserNotice(String account, Integer start, Integer limit) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT id, content as title, date_format(createTime,'%Y-%c-%d %h:%i:%s') as createTime FROM t_user_notice WHERE account = ? AND status=0 ORDER BY createTime DESC ";
        args.add(account);
        String limitStr = "";
        if (!StrUtils.hasEmpty(new Object[]{start}) && !StrUtils.hasEmpty(new Object[]{limit})) {

            limitStr = " LIMIT ?,? ";

            args.add(start);
            args.add(limit);
        }
        sql = sql + limitStr;
        Object[] argz = args.toArray();

        List<NoticeInfo> NoticeInfoList = this.dbSession.list(sql, argz, NoticeInfo.class);
        List<Map<String, Object>> obList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ob = null;
        for(NoticeInfo noticeInfo: NoticeInfoList){
            ob = BeanZUtils.transBeanMap(noticeInfo);
            obList.add(ob);
        }

        return obList;
    }


    public Integer listUserNoticeCount(String account) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT count(1) FROM t_user_notice WHERE account = ? AND status=0 ";
        args.add(account);
        Object[] argz = args.toArray();
        Integer count = this.dbSession.getInt(sql, argz);
        return count;
    }

}
