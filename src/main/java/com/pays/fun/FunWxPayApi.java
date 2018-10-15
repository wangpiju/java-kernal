package com.pays.fun;

import com.pays.PayApiParam;

import java.util.Map;

public class FunWxPayApi
        extends FunPayApi {
    public static final String ID = "funWx";
    private static final String TITLE = "乐盈微信支付接口";

    public String getTitle() {
        return "乐盈微信支付接口";
    }

    public String getId() {
        return "funWx";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXIN");
        addBankCredit("WEIXIN", "WEIXIN");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        maps.put("payType", "WX");
        maps.put("orgCode", "");

        return maps;
    }
}
