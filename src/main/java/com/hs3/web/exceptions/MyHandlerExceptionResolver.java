package com.hs3.web.exceptions;

import com.hs3.exceptions.BaseCheckException;
import com.hs3.exceptions.UnLogException;
import com.hs3.models.Jsoner;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyHandlerExceptionResolver
        implements HandlerExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(MyHandlerExceptionResolver.class);
    private boolean debug;
    private String page;

    public void setPage(String page) {
        this.page = page;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ResponseBody body = (ResponseBody) handlerMethod.getMethodAnnotation(ResponseBody.class);

        String message = null;
        if (this.debug) {
            message = ex.getMessage();
            ex.printStackTrace();
        } else {
            message = "系统繁忙,请稍后重试";
        }
        if (!(ex instanceof UnLogException)) {
            logger.error(ex.getMessage(), ex);
        }
        if ((ex instanceof BaseCheckException)) {
            message = ex.getMessage();
        }
        if (body == null) {
            ModelAndView mv = new ModelAndView(this.page);
            mv.addObject("message", message);
            request.setAttribute("decorator", "1");
            return mv;
        }
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(Jsoner.error(message).toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        return new ModelAndView();
    }
}
