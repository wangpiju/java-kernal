package com.pays.ali;

import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Map;

public class AliPayApi
        extends PayApi {
    public static final String ID = "alipay";
    private static final String TITLE = "个人支付宝支付接口";

    public String getTitle() {
        return "个人支付宝支付接口";
    }

    public String getId() {
        return "alipay";
    }

    protected void init() {
        addBankCredit("ALIPAY", "ZHIFUBAO");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        return null;
    }

    protected String getRemoteUrl(PayApiParam param) {
        return null;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return null;
    }

    public String getResultString(boolean success) {
        return null;
    }
}
