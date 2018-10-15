package com.pays.yom;

import com.pays.ApiParam;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.HashMap;
import java.util.Map;

public class YomPayApi
        extends PayApi {
    public static final String ID = "yom";
    private static final String INPUT_CHARSET = "UTF-8";
    private static final String GATEWAY = "https://api.yompay.com";
    private static final String TITLE = "优付支付接口";
    private static final String VERSION = "V2.0";

    public String getTitle() {
        return "优付支付接口";
    }

    public String getId() {
        return "yom";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://api.yompay.com";
    }

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
        addBank("CIB", "CIB");

        addBank("ALIPAY", "ALIPAY");
        addBank("WEIXIN", "WEIXIN");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return super.createPaySign(maps, key, new String[0]);
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "sign", "trade_no", "order_no", "trade_status");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("VERSION", "V2.0");
        maps.put("INPUT_CHARSET", "UTF-8");
        maps.put("RETURN_URL", param.getPayReturnUrl());
        maps.put("NOTIFY_URL", param.getPayNoticeUrl());
        maps.put("BANK_CODE", param.getBank());
        maps.put("MER_NO", param.getMerchantCode());
        maps.put("ORDER_NO", param.getOrderId());
        maps.put("ORDER_AMOUNT", param.getAmount());
        maps.put("REFERER", param.getDomain());

        return maps;
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        super.appendValue(html, maps, sign, param);


        html.append(createInput("trade_no", (String) maps.get("ORDER_NO")));
        html.append(createInput("order_amount", (String) maps.get("ORDER_AMOUNT")));
    }

    protected String getSuccessCode() {
        return "success";
    }

    public String getResultString(boolean success) {
        if (success) {
            return "success";
        }
        return "faild";
    }

    protected String getSignName() {
        return "SIGN";
    }

    protected String getKeyName() {
        return "KEY";
    }
}
