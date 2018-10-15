package com.pays.cq;

import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CqWxPayApi
        extends PayApi {
    public static final String ID = "cqWx";
    private static final String GATEWAY = "http://i.tuangouzhi.com/pay.api";
    private static final String TITLE = "超道-微信接口";
    private static final String P1_MD = "4";
    private static final String P6_CATEGORY = "1";
    private static final String P13_EX = "1";
    private static final String STR_NULL = "";

    public String getTitle() {
        return "超道-微信接口";
    }

    public String getId() {
        return "cqWx";
    }

    protected void init() {
        addBank("WEIXIN", "170001");
        addBankCredit("WEIXIN", "170001");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new LinkedHashMap();

        maps.put("p1_md", "4");
        maps.put("p2_xn", param.getOrderId());
        maps.put("p3_bn", param.getMerchantCode());
        maps.put("p4_pd", param.getBank());
        maps.put("p5_amount", param.getAmount());
        maps.put("p6_category", "1");
        maps.put("p7_name", param.getOrderId());
        maps.put("p8_desc", param.getOrderId());
        maps.put("p9_ac", "");
        maps.put("p10_ip", param.getIp());
        maps.put("p11_oid", param.getEmail());
        maps.put("p12_url", param.getPayNoticeUrl());
        maps.put("p13_ex", "1");

        return maps;
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://i.tuangouzhi.com/pay.api";
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();

        List<String> keys = new ArrayList(maps.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String k = (String) keys.get(i);
            String value = (String) maps.get(k);
            sb.append("&").append(k).append("=").append(value);
        }
        String str = sb.substring(1).toString() + key;
        return MD5.encode(str, "utf-8").toUpperCase();
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        String str = getDictSortStr(maps, new String[]{"sign"}) + key;
        return MD5.encode(str, "utf-8").toUpperCase();
    }

    protected NoticeResult verifyDefault(Map<String, String> maps, String key) {
        NoticeResult rel = verify(maps, key, "sign", "p3_xn", "p2_sn", "p7_st");
        if (rel.isStatus()) {
            if (!"1".equals(maps.get("p8_reply"))) {
                rel.setNotifyType(1);
            }
        }
        return rel;
    }

    public String getResultString(boolean success) {
        if (success) {
            return "success";
        }
        return "faile";
    }
}
