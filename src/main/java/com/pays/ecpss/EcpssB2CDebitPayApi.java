package com.pays.ecpss;

import com.pays.PayApiParam;

import java.util.Map;

public class EcpssB2CDebitPayApi
        extends EcpssPayApi {
    public static final String ID = "ecpssB2CDebit";
    private static final String TITLE = "汇潮个人B2C储蓄卡支付接口";

    public String getTitle() {
        return "汇潮个人B2C储蓄卡支付接口";
    }

    public String getId() {
        return "ecpssB2CDebit";
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        maps.put("payType", "B2CDebit");

        return maps;
    }
}
