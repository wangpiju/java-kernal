package com.hs3.utils;

import com.hs3.models.sys.UserAgent;

public class UserAgentUtils {
    public static UserAgent getUserAgent(String userAgent) {
        if (StrUtils.hasEmpty(new Object[]{userAgent})) {
            return new UserAgent("没有信息", "", "", "", "");
        }
        if (userAgent.contains("Windows")) {
            if (userAgent.contains("Windows NT 6.2")) {
                return judgeBrowser(userAgent, "Windows", "8", null);
            }
            if (userAgent.contains("Windows NT 6.1")) {
                return judgeBrowser(userAgent, "Windows", "7", null);
            }
            if (userAgent.contains("Windows NT 6.0")) {
                return judgeBrowser(userAgent, "Windows", "Vista", null);
            }
            if (userAgent.contains("Windows NT 5.2")) {
                return judgeBrowser(userAgent, "Windows", "XP", "x64 Edition");
            }
            if (userAgent.contains("Windows NT 5.1")) {
                return judgeBrowser(userAgent, "Windows", "XP", null);
            }
            if (userAgent.contains("Windows NT 5.01")) {
                return judgeBrowser(userAgent, "Windows", "2000", "SP1");
            }
            if (userAgent.contains("Windows NT 5.0")) {
                return judgeBrowser(userAgent, "Windows", "2000", null);
            }
            if (userAgent.contains("Windows NT 4.0")) {
                return judgeBrowser(userAgent, "Windows", "NT 4.0", null);
            }
            if (userAgent.contains("Windows 98; Win 9x 4.90")) {
                return judgeBrowser(userAgent, "Windows", "ME", null);
            }
            if (userAgent.contains("Windows 98")) {
                return judgeBrowser(userAgent, "Windows", "98", null);
            }
            if (userAgent.contains("Windows 95")) {
                return judgeBrowser(userAgent, "Windows", "95", null);
            }
            if (userAgent.contains("Windows CE")) {
                return judgeBrowser(userAgent, "Windows", "CE", null);
            }
        } else if (userAgent.contains("Mac OS X")) {
            if (userAgent.contains("iPod")) {
                return judgeBrowser(userAgent, "iPod", null, null);
            }
        }
        return new UserAgent("未能解析", userAgent, "", "", "");
    }

    private static UserAgent judgeBrowser(String userAgent, String platformType, String platformSeries, String platformVersion) {
        if (userAgent.contains("Chrome")) {
            String temp = userAgent.substring(userAgent.indexOf("Chrome/") + 7);
            String chromeVersion = null;
            if (temp.indexOf(" ") < 0) {
                chromeVersion = temp;
            } else {
                chromeVersion = temp.substring(0, temp.indexOf(" "));
            }
            return new UserAgent("Chrome", chromeVersion, platformType, platformSeries, platformVersion);
        }
        if (userAgent.contains("Firefox")) {
            String temp = userAgent.substring(userAgent.indexOf("Firefox/") + 8);
            String ffVersion = null;
            if (temp.indexOf(" ") < 0) {
                ffVersion = temp;
            } else {
                ffVersion = temp.substring(0, temp.indexOf(" "));
            }
            return new UserAgent("Firefox", ffVersion, platformType, platformSeries, platformVersion);
        }
        if (userAgent.contains("MSIE")) {
            if (userAgent.contains("MSIE 10.0")) {
                return new UserAgent("Internet Explorer", "10", platformType, platformSeries, platformVersion);
            }
            if (userAgent.contains("MSIE 9.0")) {
                return new UserAgent("Internet Explorer", "9", platformType, platformSeries, platformVersion);
            }
            if (userAgent.contains("MSIE 8.0")) {
                return new UserAgent("Internet Explorer", "8", platformType, platformSeries, platformVersion);
            }
            if (userAgent.contains("MSIE 7.0")) {
                return new UserAgent("Internet Explorer", "7", platformType, platformSeries, platformVersion);
            }
            if (userAgent.contains("MSIE 6.0")) {
                return new UserAgent("Internet Explorer", "6", platformType, platformSeries, platformVersion);
            }
        }
        return new UserAgent(null, null, platformType, platformSeries, platformVersion);
    }
}
