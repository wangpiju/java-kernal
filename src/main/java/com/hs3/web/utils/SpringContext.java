package com.hs3.web.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service("springContext")
public class SpringContext
        implements ApplicationContextAware {
    protected static ApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static <T> T getBean(Class<T> c) {
        String[] names = context.getBeanNamesForType(c);
        //jd-gui
        //return names.length > 0 ? context.getBean(names[0]) : null;
        return names.length > 0 ? (T) context.getBean(names[0]) : null;
    }

    public static String getMessage(String key, Object[] args, Locale locale) {
        return context.getMessage(key, args, locale);
    }

    public static String getMessage(String key, Object[] args, String defaultMessage, Locale locale) {
        return context.getMessage(key, args, defaultMessage, locale);
    }
}
