package com.pays.ecpss;

import com.pays.PayApiParam;

import java.util.Map;

public class EcpssquickPayPayApi
        extends EcpssPayApi {
    public static final String ID = "ecpssquickPay";
    private static final String TITLE = "汇潮个人快捷支付接口";

    public String getTitle() {
        return "汇潮个人快捷支付接口";
    }

    public String getId() {
        return "ecpssquickPay";
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        maps.put("payType", "quickPay");

        return maps;
    }
}
