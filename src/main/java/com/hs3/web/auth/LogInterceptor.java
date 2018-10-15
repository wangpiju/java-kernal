package com.hs3.web.auth;

import com.hs3.entity.users.Manager;
import com.hs3.entity.users.User;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;
import com.hs3.web.utils.WebUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LogInterceptor
        extends AuthInterceptor {
    private static final String KEY_HOME = "USER_SESSION";
    private static final String KEY_ADMIN = "ADMIN_SESSION";
    private Logger log = LoggerFactory.getLogger(LogInterceptor.class);
    private long logTime;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            ThreadLog.begin(request.getRequestURI(), getAccount(request));
            ThreadLog.addKey(request.getSession().getId());
        } catch (Exception e) {
            this.log.error("thread log begin exception!", e);
        }
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception exception)
            throws Exception {
        try {
            ThreadLog.end(WebUtils.getIP(request), request.getHeader("user-agent"), getMap(request.getParameterMap()), request.getMethod(), getStackTraceAsString(exception), Long.valueOf(this.logTime));
        } catch (Exception e) {
            this.log.error("thread log end exception!", e);
        }
    }

    private static String getAccount(HttpServletRequest request) {
        Object obj = request.getSession().getAttribute("USER_SESSION");
        if (obj != null) {
            return ((User) obj).getAccount();
        }
        obj = request.getSession().getAttribute("ADMIN_SESSION");
        if (obj != null) {
            return ((Manager) obj).getAccount();
        }
        String acc = request.getParameter("account");
        if ((!StrUtils.hasEmpty(new Object[]{acc})) && (acc.length() < 50)) {
            return acc;
        }
        return null;
    }

    private static String getMap(Map paramMap) {
        /** jd-gui
         * if (paramMap == null) {
         return null;
         }
         StringBuilder params = new StringBuilder();
         for (Map.Entry<String, String[]> param : paramMap.entrySet())
         {
         params.append("".equals(params.toString()) ? "" : "&").append((String)param.getKey()).append("=");
         params.append((((String)param.getKey()).contains("pass")) || (((String)param.getKey()).contains("key")) || (((String)param.getKey()).contains("publicKey")) ? "****" : ListUtils.toString((Object[])param.getValue()));
         }
         return params.toString();*/

        if (paramMap == null)
            return null;
        StringBuilder params = new StringBuilder();
        Entry param;
        for (Iterator iterator = paramMap.entrySet().iterator(); iterator.hasNext(); params.append(!((String) param.getKey()).contains("pass") && !((String) param.getKey()).contains("key") && !((String) param.getKey()).contains("publicKey") ? ListUtils.toString((Object[]) param.getValue()) : "****")) {
            param = (java.util.Map.Entry) iterator.next();
            params.append("".equals(params.toString()) ? "" : "&").append((String) param.getKey()).append("=");
        }

        return params.toString();

    }

    private static String getStackTraceAsString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public void setLogTime(long logTime) {
        this.logTime = logTime;
    }
}
