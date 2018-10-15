package com.hs3.dao.article;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.article.MessageContent;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("messageContentDao")
public class MessageContentDao
        extends BaseDao<MessageContent> {
    public List<MessageContent> listByCond(MessageContent m, Date startTime, Date endTime, Page page) {
        String sql = "SELECT * FROM t_message_content WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (m.getId() != null) {
            sql = sql + " AND id = ?";
            cond.add(m.getId());
        }
        if (m.getMessageId() != null) {
            sql = sql + " AND messageId = ?";
            cond.add(m.getMessageId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getSender()})) {
            sql = sql + " AND sender = ?";
            cond.add(m.getSender());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getRever()})) {
            sql = sql + " AND rever = ?";
            cond.add(m.getRever());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getContent()})) {
            sql = sql + " AND content = ?";
            cond.add(m.getContent());
        }
        if (startTime != null) {
            sql = sql + " AND createTime >= ?";
            cond.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND createTime <= ?";
            cond.add(endTime);
        }
        sql = sql + " ORDER BY id";
        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public void save(MessageContent m) {
        saveAuto(m);
    }

    public int update(MessageContent m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"messageId", "sender", "rever", "content", "createTime"};
    }

    protected Object[] getValues(MessageContent m) {
        return new Object[]{m.getMessageId(), m.getSender(), m.getRever(), m.getContent(), m.getCreateTime()};
    }
}
