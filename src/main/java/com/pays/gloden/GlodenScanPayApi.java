package com.pays.gloden;

import com.pays.PayApiParam;

import java.util.LinkedHashMap;
import java.util.Map;

public class GlodenScanPayApi
        extends GlodenPayApi {
    public static final String ID = "glodenScan";
    private static final String TITLE = "Gloden扫码支付接口";
    private static final String P1_MD = "4";
    private static final String P6_CATEGORY = "1";
    private static final String P13_EX = "1";
    private static final String STR_NULL = "";

    public String getTitle() {
        return "Gloden扫码支付接口";
    }

    public String getId() {
        return "glodenScan";
    }

    protected void init() {
        addBank("WEIXIN", "170001");
        addBankCredit("ALIPAY", "180001");
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
        maps.put("p11_oid", param.getEmail() == null ? "" : param.getEmail());
        maps.put("p12_url", param.getPayNoticeUrl());
        maps.put("p13_ex", "1");
        return maps;
    }
}
