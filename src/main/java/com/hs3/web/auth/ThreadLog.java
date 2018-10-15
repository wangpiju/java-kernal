package com.hs3.web.auth;

import com.hs3.dao.sys.LogAllDao;
import com.hs3.entity.sys.LogAll;
import com.hs3.web.utils.SpringContext;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadLog {
    private static final Logger logger = LoggerFactory.getLogger(ThreadLog.class);
    private static final String SPLIT_CHAR = ",";
    private static ExecutorService es = Executors.newFixedThreadPool(50);
    private static ThreadLocal<LogAll> threadLogAll = new ThreadLocal<>();

    public static void log(String info) {
        LogAll logAll = threadLogAll.get();
        if (logAll != null) {
            if (logAll.getC1() == null) {
                logAll.setC1(info);
                return;
            }
            if (logAll.getC2() == null) {
                logAll.setC2(info);
                return;
            }
            if (logAll.getC3() == null) {
                logAll.setC3(info);
                return;
            }
            if (logAll.getC4() == null) {
                logAll.setC4(info);
                return;
            }
            if (logAll.getC5() == null) {
                logAll.setC5(info);
                return;
            }
            logAll.setCext((logAll.getCext() == null ? "" : new StringBuilder(String.valueOf(logAll.getCext())).append(",").toString()) + info);
        }
    }

    public static void addKey(String key) {
        LogAll logAll = threadLogAll.get();
        if (logAll != null) {
            if (logAll.getK1() == null) {
                logAll.setK1(key);
                return;
            }
            if (logAll.getK2() == null) {
                logAll.setK2(key);
                return;
            }
            if (logAll.getK3() == null) {
                logAll.setK3(key);
                return;
            }
            if (logAll.getK4() == null) {
                logAll.setK4(key);
                return;
            }
            if (logAll.getK5() == null) {
                logAll.setK5(key);
                return;
            }
            logAll.setKext((logAll.getKext() == null ? "" : new StringBuilder(String.valueOf(logAll.getKext())).append(",").toString()) + key);
        }
    }

    public static void begin() {
        begin(null);
    }

    public static void begin(String uri) {
        begin(uri, null);
    }

    public static void begin(String uri, String account) {
        LogAll logAll = new LogAll();
        logAll.setBeginTime(System.currentTimeMillis());
        logAll.setRequestUri(uri);
        logAll.setAccount(account);
        threadLogAll.set(logAll);
    }

    public static void end(String ip, String userAgent, String params, String method, String exception, Long warnTime) {
        LogAll logAll = (LogAll) threadLogAll.get();
        if (logAll == null) {
            return;
        }
        threadLogAll.remove();
        long endTime = System.currentTimeMillis();
        if ((warnTime != null) && (endTime - logAll.getBeginTime() < warnTime)) {
            return;
        }
        logAll.setEndTime(endTime);
        logAll.setRemoteAddr(ip);
        logAll.setUserAgent(userAgent);
        if ((params != null) && (params.length() < 1000)) {
            logAll.setParams(params);
        }
        logAll.setMethod(method);
        logAll.setException(exception);

        es.execute(new SaveLogAllThread(logAll));
    }

    public static void end(Long warnTime) {
        end(null, null, null, null, null, warnTime);
    }

    public static void end() {
        end(null);
    }

    public static void setAccount(String account) {
        LogAll logAll = threadLogAll.get();
        if (logAll != null) {
            logAll.setAccount(account);
        }
    }

    private static class SaveLogAllThread
            extends Thread {
        private LogAll logAll;

        public SaveLogAllThread(LogAll logAll) {
            this.logAll = logAll;
        }

        public void run() {
            try {
                this.logAll.setDuringTime(logAll.getEndTime() - logAll.getBeginTime());

                this.logAll.setMaxMemory(Runtime.getRuntime().maxMemory() / 1024L / 1024L);
                this.logAll.setTotalMemory(Runtime.getRuntime().totalMemory() / 1024L / 1024L);
                this.logAll.setFreeMemory(Runtime.getRuntime().freeMemory() / 1024L / 1024L);

                this.logAll.setCreateTime(new Date());

                SpringContext.getBean(LogAllDao.class).saveAutoReturnId(this.logAll);
            } catch (Exception e) {
                ThreadLog.logger.error(e.getMessage(), e);
            }
        }
    }
}
