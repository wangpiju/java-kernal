package com.pays.zhih;

import com.hs3.utils.DateUtils;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ZhihWxPayApi
        extends ZhihPayApi {
    private static final String TITLE = "智汇付-微信扫码接口";
    public static final String ID = "zhihWx";
    private static final String SERVICE_TYPE = "weixin_scan";
    private static final String GATEWAY = "https://api.zhihpay.com/gateway/api/scanpay";
    private static final String INTERFACE_VERSION = "V3.1";

    protected String getRemoteUrl(PayApiParam param) {
        return "https://api.zhihpay.com/gateway/api/scanpay";
    }

    protected String getVersion() {
        return "V3.1";
    }

    protected String getServiceType() {
        return "weixin_scan";
    }

    public String getTitle() {
        return "智汇付-微信扫码接口";
    }

    public String getId() {
        return "zhihWx";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXIN");
        addBankCredit("WEIXIN", "WEIXIN");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();


        maps.put("merchant_code", param.getMerchantCode());
        maps.put("service_type", getServiceType());
        maps.put("notify_url", param.getPayNoticeUrl());
        maps.put("interface_version", getVersion());
        maps.put("client_ip", param.getIp());
        maps.put("sign_type", getSignType());


        maps.put("order_no", param.getOrderId());
        maps.put("order_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        maps.put("order_amount", param.getAmount());
        maps.put("product_name", param.getOrderId());

        return maps;
    }
}
