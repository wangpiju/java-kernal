package com.pays.din;

import com.hs3.utils.DateUtils;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DinPayApi
        extends PayApi {
    public static final String ID = "din";
    private static final String INPUT_CHARSET = "UTF-8";
    private static final String GATEWAY = "https://pay.dinpay.com/gateway?input_charset=UTF-8";
    private static final String TITLE = "智付支付接口";
    private static final String PAY_TYPE = "b2c";
    private static final String SERVICE_TYPE = "direct_pay";
    private static final String INTERFACE_VERSION = "V3.0";
    private static final String SIGN_TYPE = "MD5";

    public String getTitle() {
        return "智付支付接口";
    }

    public String getId() {
        return "din";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://pay.dinpay.com/gateway?input_charset=UTF-8";
    }

    protected void init() {
        addBank("ABC", "ABC");
        addBank("ICBC", "ICBC");
        addBank("CCB", "CCB");
        addBank("COMM", "BCOM");
        addBank("BOC", "BOC");
        addBank("CMB", "CMB");
        addBank("CMBC", "CMBC");
        addBank("CEB", "CEBB");
        addBank("BOB", "BOB");
        addBank("SHB", "SHB");
        addBank("NBCB", "NBB");
        addBank("HXB", "HXB");
        addBank("CIB", "CIB");
        addBank("PSBC", "PSBC");
        addBank("SPA", "SPABANK");
        addBank("SPDB", "SPDB");
        addBank("BEA", "BEA");
        addBank("CITIC", "ECITIC");

        addBankCredit("RMBJJK", "");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return createPaySign(maps, key, new String[]{"sign_type", "sign"});
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();


        maps.put("merchant_code", param.getMerchantCode());
        maps.put("service_type", "direct_pay");
        maps.put("pay_type", "b2c");
        maps.put("input_charset", "UTF-8");
        maps.put("notify_url", param.getPayNoticeUrl());
        maps.put("return_url", param.getPayReturnUrl());
        maps.put("client_ip", param.getIp());
        maps.put("interface_version", "V3.0");


        maps.put("order_no", param.getOrderId());
        maps.put("order_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        maps.put("order_amount", param.getAmount());
        maps.put("product_name", param.getOrderId());
        maps.put("sign_type", "MD5");

        maps.put("bank_code", param.getBank());

        return maps;
    }

    public String getResultString(boolean success) {
        if (success) {
            return "SUCCESS";
        }
        return "FAILED";
    }
}
