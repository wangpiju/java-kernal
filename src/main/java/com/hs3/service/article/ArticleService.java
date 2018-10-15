package com.hs3.service.article;

import com.hs3.dao.article.ArticleDao;
import com.hs3.dao.article.ArticleGroupDao;
import com.hs3.db.Page;
import com.hs3.entity.article.Article;
import com.hs3.entity.article.ArticleGroup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("articleService")
public class ArticleService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticleGroupDao articleGroupDao;

    public List<Article> listByCond(Article m, Page page) {
        return this.articleDao.listByCond(m, page);
    }

    public void save(Article m) {
        if (m.getArticleGroupId() == null) {
            return;
        }
        if (m.getView() == null) {
            m.setView(((ArticleGroup) this.articleGroupDao.find(m.getArticleGroupId())).getView());
        }
        this.articleDao.save(m);
    }

    public List<Article> listWithOrder(Page p) {
        return this.articleDao.listWithOrder(p);
    }

    public Article find(Integer id) {
        return (Article) this.articleDao.find(id);
    }

    public int update(Article m) {
        return this.articleDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.articleDao.delete(ids);
    }
}
