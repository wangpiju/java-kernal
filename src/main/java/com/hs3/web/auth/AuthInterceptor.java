package com.hs3.web.auth;

import com.hs3.models.Jsoner;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthInterceptor
        extends HandlerInterceptorAdapter {
    protected boolean WriteJson(HttpServletResponse response, Jsoner jsoner)
            throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(jsoner.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        return false;
    }
}
