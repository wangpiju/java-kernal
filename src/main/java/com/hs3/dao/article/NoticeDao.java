package com.hs3.dao.article;

import com.hs3.dao.BaseDao;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.article.Notice;
import com.hs3.utils.BeanZUtils;
import com.hs3.utils.StrUtils;
import com.hs3.utils.entity.NoticeInfo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("noticeDao")
public class NoticeDao
        extends BaseDao<Notice> {
    protected static final String SQL_SELECT_PAGE = "SELECT %s FROM %s";

    public List<Notice> listByCond(Notice m, Page page) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE 1 = 1"});
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{m.getTitle()})) {
            sql = sql + " AND title like CONCAT('%', ?, '%')";
            cond.add(m.getTitle());
        }
        if (m.getStatus() != null) {
            sql = sql + " AND status = ?";
            cond.add(m.getStatus());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getAuthor()})) {
            sql = sql + " AND author = ?";
            cond.add(m.getAuthor());
        }
        sql = sql + " ORDER BY orderId desc,createTime desc";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public void save(Notice m) {
        saveAuto(m);
    }

    public int update(Notice m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"title", "content", "createTime", "status", "orderId", "author"};
    }

    protected Object[] getValues(Notice m) {
        return new Object[]{m.getTitle(), m.getContent(), m.getCreateTime(), m.getStatus(), m.getOrderId(), m.getAuthor()};
    }

    public List<Notice> getNoticeList(Integer num) {
        String sql = new SQLUtils(this.tableName).where(" status = 0 ").orderBy(" orderId DESC,createTime DESC LIMIT ?").getSelect();
        return this.dbSession.list(sql, new Object[]{num}, this.cls);
    }

    public int getOpenNum(Integer switching) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"count(1)", this.tableName, "WHERE switching=?"});
        Object[] args = {switching};
        return this.dbSession.getInt(sql, args).intValue();
    }

    public int updateOpenOrClose(String id, Integer switching) {
        String sql = new SQLUtils(this.tableName).field("switching").where("id=?").getUpdate();
        return this.dbSession.update(sql,
                new Object[]{switching, id});
    }

    public Notice getNotice(int i) {
        String sql = "SELECT * FROM " + this.tableName + " WHERE switching=? and status = 0 ORDER BY orderId DESC,createTime DESC";
        return (Notice) this.dbSession.getObject(sql, new Object[]{Integer.valueOf(i)}, this.cls);
    }

    public Notice first() {
        String sql = "SELECT * FROM " + this.tableName + " WHERE status = 0  ORDER BY orderId DESC,createTime DESC LIMIT 1";
        return (Notice) this.dbSession.getObject(sql, this.cls);
    }

    public List<Notice> listByPage(int pageNo, int pageSize) {
        String sql = new SQLUtils(this.tableName).where(" status = 0 ").orderBy(" orderId DESC,createTime DESC LIMIT ?,?").getSelect();
        return this.dbSession.list(sql, new Object[]{Integer.valueOf((pageNo - 1) * pageSize), Integer.valueOf(pageNo * pageSize)}, this.cls);
    }

    public int countByPage() {
        String sql = String.format("SELECT %s FROM %s", new Object[]{"count(1)", this.tableName});
        return this.dbSession.getInt(sql).intValue();
    }

    public List<Notice> listWithOrder(Integer index, Integer size) {
        String sql = "SELECT * FROM " + this.tableName + " ORDER BY orderId DESC,createTime DESC LIMIT ?,?";
        return this.dbSession.list(sql, new Object[]{index, size}, this.cls);
    }


    //**************************************以下为变更部分*****************************************

    public List<Map<String, Object>> listNotice_Z(Integer start, Integer limit) {
        List<Object> args = new ArrayList<Object>();
        String sql = "SELECT id, title, date_format(createTime,'%Y-%c-%d %h:%i:%s') as createTime FROM t_notice WHERE status=0 ORDER BY orderId DESC,createTime DESC ";
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

    public Integer listNoticeCount_Z() {
        String sql = "SELECT count(1) FROM t_notice WHERE status = 0 ";
        Integer count = this.dbSession.getInt(sql);
        return count;
    }

    public Map<String, Object> getNotice_Z(int id) {
        String sql = "SELECT id,title,content,date_format(createTime,'%Y-%c-%d %h:%i:%s') as createTime,author FROM " + this.tableName + " WHERE id = ?";
        Object[] args = {id};
        return this.dbSession.getMap(sql, args);
    }


}
