package com.hs3.service.article;

import com.hs3.dao.article.ArticleGroupDao;
import com.hs3.db.Page;
import com.hs3.entity.article.ArticleGroup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("articleGroupService")
public class ArticleGroupService {
    @Autowired
    private ArticleGroupDao articleGroupDao;

    public void save(ArticleGroup ag) {
        this.articleGroupDao.save(ag);
    }

    public List<ArticleGroup> listWithOrder(Page p) {
        return this.articleGroupDao.listWithOrder(p);
    }

    public ArticleGroup find(Integer id) {
        return (ArticleGroup) this.articleGroupDao.find(id);
    }

    public int update(ArticleGroup ag) {
        return this.articleGroupDao.update(ag);
    }

    public int delete(List<Integer> ids) {
        return this.articleGroupDao.delete(ids);
    }
}
