package com.pays.yjfl;

import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class YjflWxPayApi
        extends PayApi {
    public static final String ID = "yjflWx";
    private static final String GATEWAY = "http://101.37.135.79/weixin/api.do";
    private static final String TITLE = "缘聚福来微信扫码接口";

    public String getTitle() {
        return "缘聚福来微信扫码接口";
    }

    public String getId() {
        return "yjflWx";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://101.37.135.79/weixin/api.do";
    }

    protected void init() {
        addBank("WEIXIN", "");
        addBankCredit("WEIXIN", "");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new LinkedHashMap();

        maps.put("p1_MerchantNo", param.getMerchantCode());
        maps.put("p2_OrderNo", param.getOrderId());
        maps.put("p3_Amount", param.getAmount());
        maps.put("p4_Cur", "1");
        maps.put("p5_ProductName", param.getOrderId());
        maps.put("p6_NotifyUrl", param.getPayNoticeUrl());
        return maps;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            sb.append((String) entry.getValue());
        }
        sb.append(key);
        return MD5.encode(sb.toString(), "utf-8");
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append((String) maps.get("r1_MerchantNo"));
        sb.append((String) maps.get("r2_OrderNo"));
        sb.append((String) maps.get("r3_Amount"));
        sb.append((String) maps.get("r4_Cur"));
        sb.append((String) maps.get("r5_Status"));
        sb.append((String) maps.get("ra_PayTime"));
        sb.append((String) maps.get("rb_DealTime"));
        sb.append(key);
        return MD5.encode(sb.toString(), "utf-8");
    }

    protected NoticeResult verifyDefault(Map<String, String> maps, String key) {
        return verify(maps, key, "sign", "r2_OrderNo", "r2_OrderNo", "r5_Status");
    }

    protected String getSuccessCode() {
        return "100";
    }

    public String getResultString(boolean success) {
        if (success) {
            return "success";
        }
        return "faile";
    }
}
