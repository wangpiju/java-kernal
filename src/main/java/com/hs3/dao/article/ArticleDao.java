package com.hs3.dao.article;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.article.Article;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("articleDao")
public class ArticleDao
        extends BaseDao<Article> {
    public List<Article> listByCond(Article m, Page page) {
        String sql = "SELECT * FROM t_article WHERE 1 = 1";
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
        if (m.getArticleGroupId() != null) {
            sql = sql + " AND articleGroupId = ?";
            cond.add(m.getArticleGroupId());
        }
        sql = sql + " ORDER BY articleGroupId, orderId desc";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public void save(Article m) {
        saveAuto(m);
    }

    public int update(Article m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"articleGroupId", "title", "content", "createTime", "status", "orderId", "author", "view"};
    }

    protected Object[] getValues(Article m) {
        return new Object[]{m.getArticleGroupId(), m.getTitle(), m.getContent(), m.getCreateTime(), m.getStatus(), m.getOrderId(), m.getAuthor(), m.getView()};
    }

    public List<Article> findListByArticleGroupId(Integer articleGroupId) {
        String sql = new SQLUtils(this.tableName).where("articleGroupId=? ").orderBy(" createTime desc ").getSelect();
        return this.dbSession.list(sql, new Object[]{articleGroupId}, this.cls);
    }
}
