package com.hs3.utils.sys;

import java.util.Calendar;
import java.util.Date;

public class WebDateUtils {
    public static final String BEGIN_TIME = "00:00:00";
    public static final String END_TIME = "23:59:59";
    public static final int HOUR = 0;
    public static final int TASK_HOUR = 4;

    public static Date getBeginTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(11);
        if (hour < 0) {
            cal.add(5, -1);
        }
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        return cal.getTime();
    }

    public static Date getEndTime(Date date) {
        Date d = getBeginTime(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(5, 1);
        cal.add(13, -1);
        return cal.getTime();
    }

    public static Date getCurTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        return cal.getTime();
    }

    public static Date getDayBeginTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        return cal.getTime();
    }

    public static Date getDayEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        return cal.getTime();
    }

    public static Date getMonthBeginTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(5, 1);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        return cal.getTime();
    }

    public static Date getMonthEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(5,
                cal.getActualMaximum(5));
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        return cal.getTime();
    }

    public static Date getProfitStartTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, -15);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        return cal.getTime();
    }

    public static Date getProfitBeginTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(11);
        if (hour < 4) {
            cal.add(5, -1);
        }
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        return cal.getTime();
    }

    public static Date getYesterdayTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(11);
        if ((hour < 4) && (hour >= 0)) {
            cal.add(5, -1);
        }
        return cal.getTime();
    }

    public static Date getYesterdayEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, -1);
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        return cal.getTime();
    }

    public static Date getActivityTaskStartTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, 1);
        int activityHour = 5;
        cal.set(11, activityHour);
        cal.set(12, 0);
        cal.set(13, 0);
        return cal.getTime();
    }

    public static Date getActivityTaskEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, 1);
        int activityHour = 5;
        cal.set(11, activityHour);
        cal.set(12, 0);
        cal.set(13, 5);
        return cal.getTime();
    }

    public static Date getActivityStartTime(Date taskDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(taskDate);
        int activityHour = 5;
        cal.set(11, activityHour);
        cal.set(12, 0);
        cal.set(13, 0);
        return cal.getTime();
    }
}
