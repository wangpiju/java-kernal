package com.pays.jubao;

import com.pays.AES;
import com.pays.ApiParam;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;
import com.pays.RSA;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JubaoPayApi
        extends PayApi {
    public static final String ID = "jubao";
    private static final String GATEWAY = "http://www.jubaopay.com/apipay.htm";
    private static final String GATEWAY_WAP = "http://www.jubaopay.com/apiwapsyt.htm";
    private static final String TITLE = "聚宝云计费平台接口";
    private static final String PAY_METHOD_WANGYIN = "WANGYIN";

    public String getTitle() {
        return "聚宝云计费平台接口";
    }

    public String getId() {
        return "jubao";
    }

    protected void init() {
        addBankCredit("RMBJJK", "01");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("payid", param.getOrderId());
        maps.put("partnerid", param.getMerchantCode());
        maps.put("amount", param.getAmount());
        maps.put("payerName", param.getEmail());
        maps.put("goodsName", "Recharge" + param.getAmount());
        maps.put("payMethod", "WANGYIN");
        maps.put("remark", "");
        maps.put("returnURL", param.getPayReturnUrl());
        maps.put("callBackURL", param.getPayNoticeUrl());

        return maps;
    }

    protected String getRemoteUrl(PayApiParam param) {
        if (param.getIsMobile().booleanValue()) {
            return "http://www.jubaopay.com/apiwapsyt.htm";
        }
        return "http://www.jubaopay.com/apipay.htm";
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        try {
            String digest = "";
            String plainString = "";
            Iterator<Map.Entry<String, String>> iter = maps.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) iter.next();
                String field = (String) entry.getKey();
                String value = (String) entry.getValue();

                digest = digest + value;
                plainString = plainString + URLEncoder.encode(field, "UTF-8") + "&" + URLEncoder.encode(value, "UTF-8");
                if (iter.hasNext()) {
                    plainString = plainString + "&";
                }
            }
            String key = AES.generateRandomString();
            String iv = AES.generateRandomString();

            String message = RSA.encrypt(key, param.getPublicKey()) + RSA.encrypt(iv, param.getPublicKey()) + AES.encrypt(plainString, key, iv);
            String signature = RSA.sign(digest, param.getKey(), param.getEmail());

            html.append(createInput("message", message));
            html.append(createInput("signature", signature));
            html.append(createInput("payMethod", "WANGYIN"));


            html.append(createInput("payid", (String) maps.get("payid")));
            html.append(createInput("amount", (String) maps.get("amount")));
        } catch (Exception e) {
            throw new RuntimeException("jubao appendValue exception!", e);
        }
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult noticeResult = new NoticeResult();

        String message = (String) maps.get("message");
        String signature = (String) maps.get("signature");
        try {
            String key = RSA.decrypt(message.substring(0, 172), apiParam.getKey());
            String iv = RSA.decrypt(message.substring(172, 344), apiParam.getKey());
            String plainString = AES.decrypt(message.substring(344), key, iv).trim();

            String digest = "";
            HashMap<String, String> encrypts = new HashMap();

            String[] items = plainString.split("&");
            for (int i = 0; i < items.length / 2; i++) {
                String field = URLDecoder.decode(items[(2 * i)], "UTF-8");
                String value = URLDecoder.decode(items[(2 * i + 1)], "UTF-8");
                encrypts.put(field, value);
                digest = digest + value;
            }
            boolean result = RSA.verify(digest, signature, apiParam.getPublicKey(), apiParam.getEmail());
            if ((result) &&
                    ("2".equals(encrypts.get("state")))) {
                noticeResult.setStatus(true);
                noticeResult.setOrderId((String) encrypts.get("payid"));
                noticeResult.setApiOrderId((String) encrypts.get("orderNo"));
            }
        } catch (Exception localException) {
        }
        return noticeResult;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return null;
    }

    public String getResultString(boolean success) {
        return success ? "success" : "faild";
    }
}
