package com.hs3.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RequestResponseContext {
    private static ThreadLocal<HttpServletRequest> requests = new ThreadLocal();
    private static ThreadLocal<HttpServletResponse> responses = new ThreadLocal();
    private static ThreadLocal<HttpSession> sessions = new ThreadLocal();

    public static void set(HttpServletRequest request, HttpServletResponse response) {
        requests.set(request);
        responses.set(response);
        sessions.set(request.getSession());
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) requests.get();
    }

    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) responses.get();
    }

    public static HttpSession getSession() {
        return (HttpSession) sessions.get();
    }

    public static void remove() {
        requests.remove();
        responses.remove();
        sessions.remove();
    }
}
