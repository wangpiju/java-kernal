package com.hs3.dao.article;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.article.Message;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("messageDao")
public class MessageDao
        extends BaseDao<Message> {
    private static final String SQL = "SELECT * FROM t_message WHERE (sender = ? and sendStatus in(0, 2, 4, 5)) or (rever = ? and revStatus in(0, 1, 2) and sendStatus != 1)  ORDER BY sendTime desc";
    private static final String SQL_LIMIT = "SELECT * FROM t_message WHERE (sender = ? and sendStatus in(0, 2, 4, 5)) or (rever = ? and revStatus in(0, 1, 2) and sendStatus != 1)  ORDER BY sendTime desc LIMIT ?,?";
    private static final String SQL_COUNT = "SELECT count(1) FROM t_message WHERE (sender = ? and sendStatus = 4) or (rever = ? and revStatus = 0 and sendStatus != 1)";

    public List<Message> listToShowByUser(String account, Page page) {
        return this.dbSession.list("SELECT * FROM t_message WHERE (sender = ? and sendStatus in(0, 2, 4, 5)) or (rever = ? and revStatus in(0, 1, 2) and sendStatus != 1)  ORDER BY sendTime desc", new Object[]{account, account}, this.cls, page);
    }

    public int countUnRead(String account) {
        return this.dbSession.getInt("SELECT count(1) FROM t_message WHERE (sender = ? and sendStatus = 4) or (rever = ? and revStatus = 0 and sendStatus != 1)", new Object[]{account, account}).intValue();
    }

    public int countByCond(Message m) {
        StringBuilder sb = new StringBuilder("SELECT count(1) FROM t_message WHERE 1 = 1");

        Object[] cond = getCond(sb, m);

        return this.dbSession.getInt(sb.toString(), cond).intValue();
    }

    public List<Message> listByCond(Message m, Page page) {
        StringBuilder sb = new StringBuilder("SELECT * FROM t_message WHERE 1 = 1");

        Object[] cond = getCond(sb, m);

        sb.append(" ORDER BY sendTime desc");

        return this.dbSession.list(sb.toString(), cond, this.cls, page);
    }

    private Object[] getCond(StringBuilder sb, Message m) {
        String sql = "";
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{m.getTitle()})) {
            sql = sql + " AND title = ?";
            cond.add(m.getTitle());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getSender()})) {
            sql = sql + " AND sender = ?";
            cond.add(m.getSender());
        }
        if (m.getSendStatus() != null) {
            sql = sql + " AND sendStatus = ?";
            cond.add(m.getSendStatus());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getRever()})) {
            sql = sql + " AND rever = ?";
            cond.add(m.getRever());
        }
        if (m.getRevStatus() != null) {
            sql = sql + " AND revStatus = ?";
            cond.add(m.getRevStatus());
        }
        sb.append(sql);
        return cond.toArray(new Object[cond.size()]);
    }

    public int updateByCond(Message m) {
        List<Object> cond = new ArrayList();
        String sql = "update t_message set id=id";
        if (!StrUtils.hasEmpty(new Object[]{m.getTitle()})) {
            sql = sql + ",title = ?";
            cond.add(m.getTitle());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getSender()})) {
            sql = sql + ",sender = ?";
            cond.add(m.getSender());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getSendContent()})) {
            sql = sql + ",sendContent = ?";
            cond.add(m.getSendContent());
        }
        if (m.getSendTime() != null) {
            sql = sql + ",sendTime = ?";
            cond.add(m.getSendTime());
        }
        if (m.getSendStatus() != null) {
            sql = sql + ",sendStatus = ?";
            cond.add(m.getSendStatus());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getRever()})) {
            sql = sql + ",rever = ?";
            cond.add(m.getRever());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getRevContent()})) {
            sql = sql + ",revContent = ?";
            cond.add(m.getRevContent());
        }
        if (m.getRevTime() != null) {
            sql = sql + ",revTime = ?";
            cond.add(m.getRevTime());
        }
        if (m.getRevStatus() != null) {
            sql = sql + ",revStatus = ?";
            cond.add(m.getRevStatus());
        }
        sql = sql + " WHERE id = ?";
        cond.add(m.getId());

        return this.dbSession.update(sql, cond.toArray(new Object[cond.size()]));
    }

    public int save(Message m) {
        return saveAutoReturnId(m).intValue();
    }

    public int update(Message m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"title", "sender", "sendContent", "sendTime", "sendStatus", "rever", "revContent", "revTime", "revStatus", "sendType"};
    }

    protected Object[] getValues(Message m) {
        return new Object[]{m.getTitle(), m.getSender(), m.getSendContent(), m.getSendTime(), m.getSendStatus(), m.getRever(), m.getRevContent(), m.getRevTime(), m.getRevStatus(), m.getSendType()};
    }

    public List<Message> getMessageList(String account, Integer index, Integer limit) {
        return this.dbSession.list("SELECT * FROM t_message WHERE (sender = ? and sendStatus in(0, 2, 4, 5)) or (rever = ? and revStatus in(0, 1, 2) and sendStatus != 1)  ORDER BY sendTime desc LIMIT ?,?", new Object[]{account, account, index, limit}, this.cls);
    }
}
