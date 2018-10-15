package com.pays.ali;

import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Map;

public class Ali2BankPayApi
        extends PayApi {
    public static final String ID = "alipay2bank";
    private static final String TITLE = "支付宝转银行卡接口";

    public String getTitle() {
        return "支付宝转银行卡接口";
    }

    public String getId() {
        return "alipay2bank";
    }

    protected void init() {
        addBankCredit("ALIPAY", "alipay2bank");
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
