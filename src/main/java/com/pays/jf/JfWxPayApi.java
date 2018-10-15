package com.pays.jf;

import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JfWxPayApi
        extends PayApi {
    public static final String ID = "jfWx";
    private static final String SERVICE = "Default.Pay";
    private static final String GATEWAY = "http://api.jifupay.cc/";
    private static final String TITLE = "极付-微信接口";
    private static final BigDecimal UNIT = new BigDecimal("100");

    public String getTitle() {
        return "极付-微信接口";
    }

    public String getId() {
        return "jfWx";
    }

    protected void init() {
        addBank("WEIXIN", "wx");
        addBankCredit("WEIXIN", "wx");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();
        String amount = new BigDecimal(param.getAmount()).multiply(UNIT).setScale(0).toString();


        maps.put("orderno", param.getOrderId());
        maps.put("notify_url", param.getPayNoticeUrl());
        maps.put("amount", amount);
        maps.put("info", param.getOrderId());
        maps.put("paytype", param.getBank());
        maps.put("userid", param.getMerchantCode());
        maps.put("service", "Default.Pay");
        return maps;
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://api.jifupay.cc/";
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();

        List<String> keys = new ArrayList(maps.keySet());
        Collections.sort(keys);
        for (String k : keys) {
            if (!k.equals("sign")) {
                sb.append((String) maps.get(k));
            }
        }
        String str = key;
        return MD5.encode(str, "utf-8").toLowerCase();
    }

    protected NoticeResult verifyDefault(Map<String, String> maps, String key) {
        return verify(maps, key, "sign", "trade_number", "pay_number", "respCode");
    }

    public String getResultString(boolean success) {
        if (success) {
            return "success";
        }
        return "WO_RI";
    }

    protected String getSuccessCode() {
        return "000000";
    }
}
