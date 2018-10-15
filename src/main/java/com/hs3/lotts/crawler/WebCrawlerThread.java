package com.hs3.lotts.crawler;

import com.hs3.entity.lotts.LotterySeason;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;

public class WebCrawlerThread {
    private static final Logger logger = LoggerFactory.getLogger(WebCrawlerThread.class);
    private boolean running = true;
    private String url;
    private String regex;
    private String seasonId;
    private Integer weight;
    private LotterySeason season;
    private Thread thread;

    public Integer getWeight() {
        return this.weight;
    }

    public String getUrl() {
        return this.url;
    }

    public LotterySeason getSeason() {
        return this.season;
    }

    public WebCrawlerThread(String urls, String regexs, Integer weight, String seasonIds) {
        this.weight = weight;
        this.url = urls;
        this.regex = regexs;
        this.seasonId = seasonIds;
        this.thread = new Thread(new Runnable() {
            public void run() {
                /**jd-gui
                 while (WebCrawlerThread.this.running) {
                 try
                 {
                 WebCrawlerThread.this.season = WebCrawler.getSeason(WebCrawlerThread.this.url, WebCrawlerThread.this.regex, WebCrawlerThread.this.seasonId);
                 if (WebCrawlerThread.this.season != null)
                 {
                 WebCrawlerThread.logger.info(WebCrawlerThread.this.seasonId + " " + Thread.currentThread().getId() + "抓号成功--" + WebCrawlerThread.this.url);
                 WebCrawlerThread.this.running = false;
                 }
                 }
                 catch (Exception e)
                 {
                 WebCrawlerThread.logger.error(e.getMessage(), e);
                 try
                 {
                 Thread.sleep(5000L);
                 }
                 catch (InterruptedException e)
                 {
                 WebCrawlerThread.logger.error(e.getMessage(), e);
                 }
                 }
                 }
                 */

                while (running) {
                    try {
                        season = WebCrawler.getSeason(url, regex, seasonId);
                        if (season != null) {
                            WebCrawlerThread.logger.info((new StringBuilder(String.valueOf(seasonId))).append(" ").append(Thread.currentThread().getId()).append("抓号成功--").append(url).toString());
                            running = false;
                            break;
                        }
                    } catch (Exception e) {
                        WebCrawlerThread.logger.error(e.getMessage(), e);
                    }
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException e) {
                        WebCrawlerThread.logger.error(e.getMessage(), e);
                    }
                }

                if (WebCrawlerThread.logger.isDebugEnabled()) {
                    WebCrawlerThread.logger.debug(WebCrawlerThread.this.seasonId + " " + Thread.currentThread().getId() + "结束线程--" + WebCrawlerThread.this.url);
                }
            }
        });
    }

    public void run() {
        try {
            this.running = true;
            this.thread.start();
        } catch (Exception e) {
            WebCrawlerThread.logger.error(e.getMessage(), e);
        }
    }

    public void stop() {
        this.running = false;
    }
}
