package com.hs3.lotts.crawler;

import com.hs3.entity.lotts.LotterySeason;
import com.hs3.utils.DateUtils;
import com.hs3.utils.HttpUtils;
import com.hs3.utils.StrUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Canada3_5CrawlerThread {
    private static final Logger logger = LoggerFactory.getLogger(Canada3_5CrawlerThread.class);
    private boolean running = true;
    private String url;
    private LotterySeason season;
    private Thread thread;
    private Integer timeout;

    public String getUrl() {
        return this.url;
    }

    public LotterySeason await(Date d) {
        do {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
        } while ((this.season == null) && (new Date().getTime() < d.getTime()));
        return this.season;
    }

    public Canada3_5CrawlerThread(final String url, final String regex, final String seasonId, final String lotteryId, final Integer timeOut) {
        this.timeout = timeOut;
        this.url = url;
        this.thread = new Thread(new Runnable() {
            public void run() {
                //jd-gui
        /*while (Canada3_5CrawlerThread.this.running) {
          try
          {
        	  
            Canada3_5CrawlerThread.this.season = Canada3_5CrawlerThread.this.getSeason(url, regex, seasonId, lotteryId, timeOut);
            if (Canada3_5CrawlerThread.this.season != null) {
              Canada3_5CrawlerThread.this.running = false;
            }
            
          } catch (Exception e) {
        	  
            Canada3_5CrawlerThread.logger.error(e.getMessage(), e);
            try{
              Thread.sleep(Canada3_5CrawlerThread.this.timeout.intValue());
            } catch (InterruptedException e) {
              Canada3_5CrawlerThread.logger.error(e.getMessage(), e);
            }
            
          }
        }*/

                while (running) {
                    try {
                        season = getSeason(url, regex, seasonId, lotteryId, timeOut);
                        if (season != null) {
                            running = false;
                            break;
                        }
                    } catch (Exception e) {
                        Canada3_5CrawlerThread.logger.error(e.getMessage(), e);
                    }

                    try {
                        Thread.sleep(timeout.intValue());
                    } catch (InterruptedException e) {
                        Canada3_5CrawlerThread.logger.error(e.getMessage(), e);
                    }

                }


                if (Canada3_5CrawlerThread.logger.isDebugEnabled()) {
                    Canada3_5CrawlerThread.logger.debug(seasonId + " " + Thread.currentThread().getId() + "结束线程--" + url);
                }

            }
        });
    }

    public void run() {
        try {
            this.running = true;
            this.thread.start();
        } catch (Exception e) {
            Canada3_5CrawlerThread.logger.error(e.getMessage(), e);
        }
    }

    public void stop() {
        this.running = false;
    }

    protected LotterySeason getSeason(String url, String regex, String seasonId, String lotteryId, Integer timeOut) {
        try {
            String xml = HttpUtils.getString(url);
            LotterySeason ls = parseXML(xml, regex, seasonId, lotteryId);
            if (ls != null) {
                logger.info("抓号成功:" + url);
            }
            return ls;
        } catch (Exception e) {
            logger.error(url + "," + e.getMessage(), e);
        }
        return null;
    }

    protected LotterySeason parseXML(String xml, String regex, String seasonId, String lotteryId) {
        try {
            if (StrUtils.hasEmpty(new Object[]{xml})) {
                return null;
            }
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(xml);
            LotterySeason season = null;
            while (matcher.find()) {
                String index = matcher.group("index");
                if (seasonId.endsWith(index)) {
                    season = new LotterySeason();
                    season.setLotteryId(lotteryId);
                    season.setSeasonId(seasonId);
                    season.setAddTime(new Date());
                    season.setOpenTime(DateUtils.toDate(matcher.group("time")));
                    season.setN1(Integer.valueOf(Integer.parseInt(matcher.group("n1"))));
                    season.setN2(Integer.valueOf(Integer.parseInt(matcher.group("n2"))));
                    season.setN3(Integer.valueOf(Integer.parseInt(matcher.group("n3"))));
                    try {
                        season.setN4(Integer.valueOf(Integer.parseInt(matcher.group("n4"))));
                        season.setN5(Integer.valueOf(Integer.parseInt(matcher.group("n5"))));
                        season.setN6(Integer.valueOf(Integer.parseInt(matcher.group("n6"))));
                        season.setN7(Integer.valueOf(Integer.parseInt(matcher.group("n7"))));
                        season.setN8(Integer.valueOf(Integer.parseInt(matcher.group("n8"))));
                        season.setN9(Integer.valueOf(Integer.parseInt(matcher.group("n9"))));
                        season.setN10(Integer.valueOf(Integer.parseInt(matcher.group("n10"))));
                    } catch (IllegalArgumentException localIllegalArgumentException) {
                    }
                    return season;
                }
            }
        } catch (Exception e) {
            logger.error(xml + "," + regex + "," + e.getMessage(), e);
        }
        return null;
    }
}
