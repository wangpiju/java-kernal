package com.pays.ddbill;

import com.hs3.utils.DateUtils;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DdbillWxPayApi
        extends DdbillPayApi {
    private static final String TITLE = "多得宝-微信扫码接口";
    public static final String ID = "ddbillWx";
    private static final String SERVICE_TYPE = "weixin_scan";
    private static final String GATEWAY = "https://api.ddbill.com/gateway/api/scanpay";
    private static final String INTERFACE_VERSION = "V3.1";

    protected String getRemoteUrl(PayApiParam param) {
        return "https://api.ddbill.com/gateway/api/scanpay";
    }

    protected String getVersion() {
        return "V3.1";
    }

    protected String getServiceType() {
        return "weixin_scan";
    }

    public String getTitle() {
        return "多得宝-微信扫码接口";
    }

    public String getId() {
        return "ddbillWx";
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
