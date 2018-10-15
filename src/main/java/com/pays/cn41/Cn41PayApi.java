package com.pays.cn41;

import com.hs3.utils.DateUtils;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cn41PayApi
        extends PayApi {
    public static final String ID = "cn41";
    private static final String GATEWAY = "https://pay.41.cn/gateway";
    private static final String TITLE = "通汇卡支付接口";
    private static final String PAY_TYPE = "1";

    protected void init() {
        addBank("ABC", "ABC");
        addBank("BOC", "BOC");
        addBank("COMM", "BOCOM");
        addBank("CCB", "CCB");
        addBank("ICBC", "ICBC");
        addBank("PSBC", "PSBC");
        addBank("CMB", "CMBC");
        addBank("SPDB", "SPDB");
        addBank("CEB", "CEBBANK");
        addBank("CITIC", "ECITIC");
        addBank("SPA", "PINGAN");
        addBank("CMBC", "CMBCS");
        addBank("HXB", "HXB");
        addBank("GDB", "CGB");
        addBank("BOB", "BCCB");
        addBank("SHB", "BOS");
        addBank("BJRCB", "BRCB");
        addBank("CIB", "CIB");
        addBank("SRCB", "SRCB");
        addBankCredit("WEIXIN", "WEIXIN");
    }

    public String getTitle() {
        return "通汇卡支付接口";
    }

    public String getId() {
        return "cn41";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://pay.41.cn/gateway";
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return createPaySign(maps, key, new String[]{"sign"});
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("input_charset", "utf-8");
        maps.put("notify_url", param.getPayNoticeUrl());
        maps.put("return_url", param.getPayReturnUrl());
        maps.put("pay_type", "1");
        maps.put("bank_code", param.getBank());
        maps.put("merchant_code", param.getMerchantCode());
        maps.put("order_no", param.getOrderId());
        maps.put("order_amount", param.getAmount());
        maps.put("order_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        maps.put("req_referer", param.getDomain());
        maps.put("customer_ip", param.getIp());

        return maps;
    }

    public String getResultString(boolean success) {
        if (success) {
            return "success";
        }
        return "failed";
    }
}
