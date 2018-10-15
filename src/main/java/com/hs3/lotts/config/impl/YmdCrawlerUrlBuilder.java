package com.hs3.lotts.config.impl;

import com.hs3.lotts.config.ICrawlerUrlBuilder;

public class YmdCrawlerUrlBuilder
        implements ICrawlerUrlBuilder {
    public String getTitle() {
        return "yyyyMMdd";
    }

    public String getRemark() {
        return "yyyyMMdd";
    }

    public String createUrl(String url, String seasonId) {
        if (!seasonId.startsWith("20")) {
            seasonId = "20" + seasonId;
        }
        url = url.replace("{tttt}", seasonId.substring(0, 8));
        return url;
    }
}
