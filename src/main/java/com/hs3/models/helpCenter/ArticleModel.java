package com.hs3.models.helpCenter;

import com.hs3.entity.article.Article;

import java.util.List;

public class ArticleModel {
    private String articleGroupTitle;
    private Integer type;
    private List<Article> atrArticles;

    public String getArticleGroupTitle() {
        return this.articleGroupTitle;
    }

    public void setArticleGroupTitle(String articleGroupTitle) {
        this.articleGroupTitle = articleGroupTitle;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Article> getAtrArticles() {
        return this.atrArticles;
    }

    public void setAtrArticles(List<Article> atrArticles) {
        this.atrArticles = atrArticles;
    }
}
