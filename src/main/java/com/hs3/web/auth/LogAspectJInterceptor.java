package com.hs3.web.auth;

import com.hs3.models.Jsoner;
import com.hs3.models.PageData;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

public class LogAspectJInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LogAspectJInterceptor.class);

    @Around("execution (public * com.hs3.*.controller..*.*(..))")
    public Object around(ProceedingJoinPoint point)
            throws Throwable {
        Object object = point.proceed();
        ThreadLog.addKey(getKey(object));
        return object;
    }

    private String getKey(Object object) {
        try {
            if (object != null) {
                if ((object instanceof ModelAndView)) {
                    ModelAndView mv = (ModelAndView) object;
                    return "RESULT_VIEW:" + mv.getViewName();
                }
                if ((object instanceof Jsoner)) {
                    Jsoner result = (Jsoner) object;
                    if (result.getStatus() == 200) {
                        return "RESULT_STATUS:" + result.getStatus();
                    }
                    return "RESULT_CONTENT:" + result.getContent();
                }
                if ((object instanceof ArrayList)) {
                    ArrayList result = (ArrayList) object;
                    return "RESULT_ARSIZE:" + result.size();
                }
                if ((object instanceof PageData)) {
                    PageData result = (PageData) object;
                    return "RESULT_PDSIZE:" + result.getTotal();
                }
                return "RESULT_OBJECT:" + object.toString();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
