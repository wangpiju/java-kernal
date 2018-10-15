package com.pays.ecpss;

import com.pays.PayApiParam;

import java.util.Map;

public class EcpssnoCardPayApi
        extends EcpssPayApi {
    public static final String ID = "ecpssnoCard";
    private static final String TITLE = "汇潮个人银联快捷支付接口";

    public String getTitle() {
        return "汇潮个人银联快捷支付接口";
    }

    public String getId() {
        return "ecpssnoCard";
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        maps.put("payType", "noCard");

        return maps;
    }
}
