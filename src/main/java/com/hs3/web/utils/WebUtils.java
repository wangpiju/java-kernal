package com.hs3.web.utils;

import com.hs3.db.Page;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class WebUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

    public static Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> map = new HashMap();
        Map<String, String[]> params = request.getParameterMap();
        if (params != null) {
            for (String key : params.keySet()) {
                String val = ListUtils.toString((Object[]) params.get(key));
                map.put(key, val);
            }
        }
        return map;
    }

    public static Page getPageWithParams(HttpServletRequest request) {
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        Page p = new Page(page, rows);
        Map<String, String[]> params = request.getParameterMap();
        if (params != null) {
            for (String key : params.keySet()) {
                String val = ListUtils.toString((Object[]) params.get(key));
                if ((!StrUtils.hasEmpty(new Object[]{val})) && (!val.equals("page")) && (!val.equals("rows"))) {
                    p.addParams(key, val);
                }
            }
        }
        return p;
    }

    public static String getContextPath(HttpServletRequest request) {
        return request.getContextPath();
    }

    public static String getUrl(HttpServletRequest request) {
        String context = request.getContextPath();
        String url = request.getRequestURI();

        String rootPath = url.substring(context.length());
        return rootPath;
    }

    public static String getDomainName(HttpServletRequest request) {
        StringBuffer rootPath = request.getRequestURL();
        int i = request.getRequestURI().length();
        rootPath.delete(rootPath.length() - i, rootPath.length());
        return rootPath.toString();
    }

    private static final String[] HEADS = {"X-Forwarded-For",
            "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"};

    public static String getIP(HttpServletRequest request) {
        String ip = null;
        for (String h : HEADS) {
            ip = request.getHeader(h);
            if (!checkIP(ip)) {
                break;
            }
        }
        if (checkIP(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        } else if (ip.contains(",")) {
            ip = (String) ListUtils.toList(ip).get(0);
        }
        ip = ip != null ? ip.trim() : "";
        if (StrUtils.hasEmpty(new Object[]{ip})) {
            StringBuilder sb = new StringBuilder("获取IP异常：");
            for (String h : HEADS) {
                String str = request.getHeader(h);
                sb.append(h).append(":").append(str).append(";");
            }
            sb.append("remoteAddr").append(":").append(request.getRemoteAddr()).append(";");
            logger.info(sb.toString());
        }
        return ip;
    }

    private static boolean checkIP(String ip) {
        if ((StrUtils.hasEmpty(new Object[]{ip})) || ("unknown".equalsIgnoreCase(ip))) {
            return true;
        }
        return false;
    }
}
