package com.hs3.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpUtils {
    private static final String CHARSET_DEFAULT = "UTF-8";

    public static String getString(String url)
            throws HttpException, IOException {
        return getString(url, "UTF-8");
    }

    public static String getString(String url, String charset)
            throws HttpException, IOException {

        HttpMethod method = null;
        HttpClient client = null;
        try {
            client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
            client.getHttpConnectionManager().getParams().setSoTimeout(60000);
            method = new GetMethod(url);
            client.executeMethod(method);
            InputStream in = method.getResponseBodyAsStream();
            ByteArrayOutputStream bAOut = new ByteArrayOutputStream();
            int c;
            while ((c = in.read()) != -1) {
                //int c;
                bAOut.write(c);
            }
            return bAOut.toString(charset);
        } finally {
            if (method != null) {
                method.releaseConnection();
                method = null;
            }
            if (client != null) {
                client.getHttpConnectionManager().closeIdleConnections(0L);
            }
        }


    }

    public static String postString(String url, Map<String, String> params)
            throws HttpException, IOException {
        return postString(url, params, "UTF-8");
    }

    public static String postString(String url, Map<String, String> params, String charset)
            throws HttpException, IOException {
        PostMethod method = null;
        HttpClient client = null;
        try {
            client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
            client.getHttpConnectionManager().getParams().setSoTimeout(60000);


            method = new PostMethod(url);
            if (charset != null) {
                method.getParams().setParameter("http.protocol.content-charset", charset);
            }
            if (params != null) {
                NameValuePair[] ps = new NameValuePair[params.size()];
                int i = 0;
                for (Map.Entry entry : params.entrySet()) {
                    NameValuePair p = new NameValuePair();
                    p.setName((String) entry.getKey());
                    p.setValue((String) entry.getValue());
                    ps[i] = p;
                    i++;
                }
                method.setRequestBody(ps);
            }
            client.executeMethod(method);


            return method.getResponseBodyAsString();
        } finally {
            if (method != null) {
                method.releaseConnection();
                method = null;
            }
            if (client != null) {
                client.getHttpConnectionManager().closeIdleConnections(0L);
            }
        }
    }
}
