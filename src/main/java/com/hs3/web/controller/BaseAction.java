package com.hs3.web.controller;

import com.hs3.db.Page;
import com.hs3.utils.StrUtils;
import com.hs3.web.utils.MyDateEditor;
import com.hs3.web.utils.RequestResponseContext;
import com.hs3.web.utils.WebUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

public abstract class BaseAction<T> {
    protected HttpSession getSession() {
        return RequestResponseContext.getSession();
    }

    protected HttpServletRequest getRequest() {
        return RequestResponseContext.getRequest();
    }

    protected HttpServletResponse getResponse() {
        return RequestResponseContext.getResponse();
    }

    @InitBinder
    protected void InitBinder(ServletRequestDataBinder bin) {
        bin.registerCustomEditor(Date.class, new MyDateEditor());
    }

    protected boolean validationCode(String code) {
        String sessionCode = (String) getSession().getAttribute("KAPTCHA_SESSION_KEY");
        getSession().removeAttribute("KAPTCHA_SESSION_KEY");
        if ((StrUtils.hasEmpty(new Object[]{sessionCode, code})) || (!sessionCode.equalsIgnoreCase(code))) {
            return false;
        }
        return true;
    }

    protected String getIP() {
        return WebUtils.getIP(getRequest());
    }

    protected String getViewName(String viewName) {
        String uri = getTheme();
        if (viewName.startsWith("/")) {
            uri = uri + viewName;
        } else {
            uri = uri + "/" + viewName;
        }
        return uri;
    }

    protected ModelAndView getView(String viewName) {
        return new ModelAndView(getViewName(viewName));
    }

    protected ModelAndView getView(boolean check, String trueViewName, String falseViewName) {
        return check ? getView(trueViewName) : getView(falseViewName);
    }

    protected String redirect(String url) {
        return "redirect:" + url;
    }

    protected T getLogin() {
        //jd-gui
        //return getSession().getAttribute(getSessionKey());
        return (T) getSession().getAttribute(getSessionKey());
    }

    protected void setLogin(T u) {
        getSession().setAttribute(getSessionKey(), u);
    }

    protected Page getPageWithParams() {
        return WebUtils.getPageWithParams(getRequest());
    }

    protected Map<String, String> getParams() {
        return WebUtils.getParams(getRequest());
    }

    protected abstract String getTheme();

    protected abstract String getSessionKey();
}
