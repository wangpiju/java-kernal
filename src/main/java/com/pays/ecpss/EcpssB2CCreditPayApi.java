package com.pays.ecpss;

import com.pays.PayApiParam;

import java.util.Map;

public class EcpssB2CCreditPayApi
        extends EcpssPayApi {
    public static final String ID = "ecpssB2CCredit";
    private static final String TITLE = "汇潮个人B2C信用卡支付接口";

    public String getTitle() {
        return "汇潮个人B2C信用卡支付接口";
    }

    public String getId() {
        return "ecpssB2CCredit";
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        maps.put("payType", "B2CCredit");

        return maps;
    }
}
