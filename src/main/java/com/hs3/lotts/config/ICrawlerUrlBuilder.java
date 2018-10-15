package com.hs3.lotts.config;

public abstract interface ICrawlerUrlBuilder {
    public abstract String getTitle();

    public abstract String getRemark();

    public abstract String createUrl(String paramString1, String paramString2);
}
