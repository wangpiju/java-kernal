package com.hs3.dao.article;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.article.ArticleGroup;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("articleGroupDao")
public class ArticleGroupDao
        extends BaseDao<ArticleGroup> {
    public void save(ArticleGroup m) {
        saveAuto(m);
    }

    public int update(ArticleGroup m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"title", "type", "orderId", "status", "view"};
    }

    protected Object[] getValues(ArticleGroup m) {
        return new Object[]{m.getTitle(), m.getType(), m.getOrderId(), m.getStatus(), m.getView()};
    }

    public List<ArticleGroup> findListByType(Integer type) {
        String sql = new SQLUtils(this.tableName).where("type=? ").orderBy(" orderId ").getSelect();
        return this.dbSession.list(sql, new Object[]{type}, this.cls);
    }

    public List<ArticleGroup> findListByType() {
        String sql = new SQLUtils(this.tableName).where("type in(0,1)").orderBy(" orderId ").getSelect();
        return this.dbSession.list(sql, this.cls);
    }
}
