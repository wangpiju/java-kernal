package com.hs3.lotts.config.impl;

import com.hs3.lotts.config.ICrawlerUrlBuilder;

public class Y_m_dCrawlerUrlBuilder
        implements ICrawlerUrlBuilder {
    public String getTitle() {
        return "yyyy-MM-dd";
    }

    public String getRemark() {
        return "yyyy-MM-dd";
    }

    public String createUrl(String url, String seasonId) {
        if (!seasonId.startsWith("20")) {
            seasonId = "20" + seasonId;
        }
        url = url.replace("{tttt}", seasonId.substring(0, 4) + "-" + seasonId.substring(4, 6) + "-" + seasonId.substring(6, 8));
        return url;
    }
}
