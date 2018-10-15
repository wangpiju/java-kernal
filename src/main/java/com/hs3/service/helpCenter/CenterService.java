package com.hs3.service.helpCenter;

import com.hs3.dao.article.ArticleDao;
import com.hs3.dao.article.ArticleGroupDao;
import com.hs3.entity.article.Article;
import com.hs3.entity.article.ArticleGroup;
import com.hs3.models.helpCenter.ArticleModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("centerService")
public class CenterService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticleGroupDao articleGroupDao;

    public List<ArticleModel> findList() {
        List<ArticleModel> articleModelList = new ArrayList();
        List<ArticleGroup> articleGroupList = this.articleGroupDao.findListByType();
        for (ArticleGroup articleGroup : articleGroupList) {
            List<Article> articleList = this.articleDao.findListByArticleGroupId(articleGroup.getId());
            ArticleModel articleModel = new ArticleModel();
            articleModel.setArticleGroupTitle(articleGroup.getTitle());
            List<Article> articles = new ArrayList();
            for (Article article : articleList) {
                articles.add(article);
            }
            articleModel.setArticleGroupTitle(articleGroup.getTitle());
            articleModel.setType(articleGroup.getType());
            articleModel.setAtrArticles(articles);
            articleModelList.add(articleModel);
        }
        return articleModelList;
    }

    public Article getContentById(Integer id) {
        Article article = (Article) this.articleDao.find(id);
        return article;
    }

    public Map<String, Object> getPlatNoticeById(Integer id) {
        Map<String, Object> map = new HashMap();
        Article article = (Article) this.articleDao.find(id);
        ArticleGroup articleGroup = null;
        if (article != null) {
            articleGroup = (ArticleGroup) this.articleGroupDao.find(article.getArticleGroupId());
        }
        map.put("groupName", articleGroup.getTitle());
        map.put("article", article);
        return map;
    }
}
