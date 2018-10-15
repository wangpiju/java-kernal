package com.hs3.lotts.config;

import com.hs3.lotts.config.impl.Y_m_dCrawlerUrlBuilder;
import com.hs3.lotts.config.impl.YmdCrawlerUrlBuilder;

import java.util.ArrayList;
import java.util.List;

public class CrawlerUrlBuilderFactory {
    private static final List<ICrawlerUrlBuilder> LIST = new ArrayList();

    static {
        LIST.add(new YmdCrawlerUrlBuilder());
        LIST.add(new Y_m_dCrawlerUrlBuilder());
    }

    public static ICrawlerUrlBuilder getInstance(String title) {
        for (ICrawlerUrlBuilder sb : LIST) {
            if (sb.getTitle().equals(title)) {
                return sb;
            }
        }
        return null;
    }

    public static List<ICrawlerUrlBuilder> getList() {
        return LIST;
    }
}
