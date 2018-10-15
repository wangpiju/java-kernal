package com.pays.wx;

import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Map;

public class WxPayApi
        extends PayApi {
    public static final String ID = "wxpay";
    private static final String TITLE = "个人微信支付接口";

    public String getTitle() {
        return "个人微信支付接口";
    }

    public String getId() {
        return "wxpay";
    }

    protected void init() {
        addBankCredit("WEIXIN", "WEIXIN");
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
