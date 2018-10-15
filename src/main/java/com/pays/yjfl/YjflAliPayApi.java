package com.pays.yjfl;

import com.pays.PayApiParam;

import java.util.LinkedHashMap;
import java.util.Map;

public class YjflAliPayApi
        extends YjflWxPayApi {
    public static final String ID = "yjflAli";
    private static final String GATEWAY = "http://101.37.135.79/pay/alipay.do";
    private static final String TITLE = "缘聚福来支付宝扫码接口";

    public String getTitle() {
        return "缘聚福来支付宝扫码接口";
    }

    public String getId() {
        return "yjflAli";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://101.37.135.79/pay/alipay.do";
    }

    protected void init() {
        addBank("ALIPAY", "");
        addBankCredit("ALIPAY", "");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new LinkedHashMap();

        maps.put("p1_MerchantNo", param.getMerchantCode());
        maps.put("p2_OrderNo", param.getOrderId());
        maps.put("p3_Amount", param.getAmount());
        maps.put("p4_Cur", "1");
        maps.put("p5_ProductName", param.getOrderId());
        maps.put("p6_NotifyUrl", param.getPayNoticeUrl());
        maps.put("tranType", "1");
        return maps;
    }
}
