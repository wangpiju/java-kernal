package com.hs3.lotts.crawler;

import com.hs3.entity.lotts.LotterySeason;
import com.hs3.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
    private static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

    public static void main(String[] args){
        String seasonId = "2018080843";
        String index = "20180808043";
       boolean isMatch = seasonId.endsWith(index) || seasonId.substring(seasonId.length() - 2).equals(index.substring(index.length() - 2))
                || seasonId.substring(seasonId.length() - 3).equals(index.substring(index.length() - 3));
       System.out.println(isMatch);
    }

    public static LotterySeason getSeason(String url, String regex, String seasonId) {
        try {
            String xml = HttpUtils.getString(url);
            return parseXML(xml, regex, seasonId);
        } catch (IOException e) {
            logger.warn(url + "," + e.getMessage());
        } catch (Exception e) {
            logger.error(url + "," + e.getMessage(), e);
        }
        return null;
    }

    private static LotterySeason parseXML(String xml, String regex, String seasonId) {
       // boolean isFind = false;
        try {
            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(xml);
            //isFind = matcher.find();
            matcher.find();
            LotterySeason season;

            try {
                do {
                    String regexSeasonId = "20" + matcher.group("date") + "-";
//                    if (!seasonId.startsWith(regexSeasonId)) {
//                    }
                } while (matcher.find());
            } catch (IllegalArgumentException localIllegalArgumentException) {
                String index = "";
                try {
                    index = matcher.group("index");
                } catch (Exception e) {
                    //do nothing
                }

                if (seasonId.endsWith(index) || seasonId.substring(seasonId.length() - 2).equals(index.substring(index.length() - 2))
                        || seasonId.substring(seasonId.length() - 3).equals(index.substring(index.length() - 3))) {
                    season = new LotterySeason();
                    season.setSeasonId(seasonId);
                    season.setN1(Integer.parseInt(matcher.group("n1")));
                    season.setN2(Integer.parseInt(matcher.group("n2")));
                    season.setN3(Integer.parseInt(matcher.group("n3")));
                    try {
                        season.setN4(Integer.parseInt(matcher.group("n4")));
                        season.setN5(Integer.parseInt(matcher.group("n5")));
                        season.setN6(Integer.parseInt(matcher.group("n6")));
                        season.setN7(Integer.parseInt(matcher.group("n7")));
                        season.setN8(Integer.parseInt(matcher.group("n8")));
                        season.setN9(Integer.parseInt(matcher.group("n9")));
                        season.setN10(Integer.parseInt(matcher.group("n10")));
                    } catch (IllegalArgumentException localIllegalArgumentException1) {
                        logger.error(localIllegalArgumentException.getMessage(), localIllegalArgumentException);
                    }
                    return season;
                }
            }
        } catch (Exception e) {
            //logger.error(seasonId + ", xml: " + xml + ",regex: " + regex + "," + e.getMessage(), e);
            //logger.error(seasonId + "," + regex + "," + e.getMessage(), e);
        }
        return null;
    }


}
