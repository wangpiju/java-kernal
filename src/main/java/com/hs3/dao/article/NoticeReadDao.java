package com.hs3.dao.article;

import com.hs3.dao.BaseDao;
import com.hs3.entity.article.Article;
import com.hs3.entity.article.Notice;
import com.hs3.entity.article.NoticeRead;
import jdk.nashorn.internal.ir.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-08-07 14:14
 **/
@Repository("noticeReadDao")
public class NoticeReadDao extends BaseDao<NoticeRead> {

    public List<Integer> queryReadIds(String account, Integer type, Integer[] noticeIds) {
        StringBuilder sql = new StringBuilder("SELECT noticeId FROM " + this.tableName + " WHERE status = 0 ");
        List<Object> args=new ArrayList<>();
        if (StringUtils.isNotBlank(account)) {
            sql.append(" AND account = ?");
            args.add(account);
        }
        if (type != null) {
            sql.append(" and type = ?");
            args.add(type);
        }
        if (noticeIds != null && noticeIds.length > 0) {
            String sqlIds = "";
            for (Integer noticeId : noticeIds) {
                sqlIds += "?,";
                args.add(noticeId);
            }
            sql.append(" and noticeId in (").append(sqlIds.substring(0,sqlIds.length()-1)).append(")");

        }
        List<NoticeRead> noticeReads = this.dbSession.list(sql.toString(), args.toArray(), NoticeRead.class);
        List<Integer> ids = new ArrayList<>();
        if (noticeReads != null && noticeReads.size() > 0) {
            noticeReads.forEach(nr->{
                ids.add(nr.getNoticeId());
            });
        }
        return ids;
    }


    public NoticeRead getByNoticeId(Integer noticeId, String account) {
        String sql = String.format("select * from %s where noticeId = ? and account = ?", this.tableName);
        Object [] params = new Object[]{noticeId, account};
        return this.dbSession.getObject(sql, params, NoticeRead.class);

    }

    protected String[] getColumns() {
        return new String[]{"type", "noticeId", "account", "status", "createTime"};
    }

    protected Object[] getValues(NoticeRead nr) {
        return new Object[]{nr.getType(), nr.getNoticeId(), nr.getAccount(), nr.getStatus(), nr.getCreateTime()};
    }
}
