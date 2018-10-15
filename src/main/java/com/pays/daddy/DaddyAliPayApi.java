package com.pays.daddy;

import com.alibaba.fastjson.JSONObject;
import com.hs3.utils.HttpClientUtil;
import com.hs3.utils.ResponseData;
import com.pays.PayApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DaddyAliPayApi
        extends DaddyPayApi {

    public static final String ID = "daddyAli";
    private static final String TITLE = "DADDY移动电子钱包支付接口";

    public String getTitle() {
        return "DADDY移动电子钱包支付接口";
    }

    public String getId() {
        return "daddyAli";
    }

    protected void init() {
        addBank("ALIPAY", "30");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("company_id", param.getMerchantCode());
        maps.put("bank_id", param.getBank());
        maps.put("amount", param.getAmount());
        maps.put("company_order_num", param.getOrderId());
        maps.put("company_user", "R" + param.getAmount());
        maps.put("estimated_payment_bank", param.getBank());
        maps.put("deposit_mode", "3");
        maps.put("group_id", "0");
        maps.put("web_url", param.getShopUrl());
        maps.put("memo", "");
        maps.put("note", "");
        maps.put("note_model", "1");
        maps.put("terminal", param.getIsMobile().booleanValue() ? "2" : "1");

        return maps;
    }


}
