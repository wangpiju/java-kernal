package com.pays.daddy;

import com.pays.PayApiParam;

import java.util.HashMap;
import java.util.Map;

public class DaddyWxPayApi
        extends DaddyPayApi {
    public static final String ID = "daddyWx";
    private static final String TITLE = "DADDY第三方支付接口";

    public String getTitle() {
        return "DADDY第三方支付接口";
    }

    public String getId() {
        return "daddyWx";
    }

    protected void init() {
        addBank("WEIXIN", "40");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("company_id", param.getMerchantCode());
        maps.put("bank_id", param.getBank());
        maps.put("amount", param.getAmount());
        maps.put("company_order_num", param.getOrderId());
        maps.put("company_user", "R" + param.getAmount());
        maps.put("estimated_payment_bank", param.getBank());
        maps.put("deposit_mode", "2");
        maps.put("group_id", "0");
        maps.put("web_url", param.getShopUrl());
        maps.put("memo", "");
        maps.put("note", param.getTraceId());
        maps.put("note_model", "1");
        maps.put("terminal", param.getIsMobile().booleanValue() ? "2" : "1");

        return maps;
    }
}
