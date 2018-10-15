package com.pays.ecpss;

import com.pays.PayApiParam;

import java.util.Map;

public class EcpssB2BPayApi
        extends EcpssPayApi {
    public static final String ID = "ecpssB2B";
    private static final String TITLE = "汇潮企业网银支付接口";

    public String getTitle() {
        return "汇潮企业网银支付接口";
    }

    public String getId() {
        return "ecpssB2B";
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        maps.put("payType", "B2B");

        return maps;
    }
}
