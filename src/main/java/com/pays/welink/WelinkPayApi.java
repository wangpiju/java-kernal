package com.pays.welink;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class WelinkPayApi
        extends PayApi {
    public static final String ID = "welink";
    private static final String GATEWAY = "https://gateway.welinkpay.com/payment/gateway";
    private static final String TITLE = "welink银联支付接口";

    public String getTitle() {
        return "welink银联支付接口";
    }

    public String getId() {
        return "welink";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://gateway.welinkpay.com/payment/gateway";
    }

    protected void init() {
        addBank("CCB", "BANK_CCB");
        addBank("ICBC", "BANK_ICBC");
        addBank("ABC", "BANK_ABC");
        addBank("CMBC", "BANK_CMBC");
        addBank("CIB", "BANK_CIB");
        addBank("GDB", "BANK_GDB");
        addBank("CEB", "BANK_CEB");
        addBank("BOB", "BANK_BOBJ");
        addBank("SHB", "BANK_BOS");
        addBank("SPA", "BANK_PAB");
        addBank("NBCB", "BANK_NBCB");
        addBank("CZB", "BANK_CZB");
        addBank("HZB", "BANK_HZCB");
        addBank("CMB", "BANK_CMB");
        addBank("BOC", "BANK_BOC");
        addBank("COMM", "BANK_BOCOM");
        addBank("HXB", "BANK_HXBC");
        addBank("SPDB", "BANK_SPDB");
        addBank("CITIC", "BANK_CITIC");
        addBank("PSBC", "BANK_PSBC");
        addBank("SRCB", "BANK_SRCB");
        addBank("BJRCB", "BANK_BJRCB");
        addBank("CBHB", "BANK_CBHB");
        addBank("NJCB", "BANK_BON");
        addBank("BEA", "BANK_BEA");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new LinkedHashMap();

        Date now = new Date();
        String time = DateUtils.format(now, "yyyy-MM-dd HH:mm:ss");


        maps.put("partner_id", param.getMerchantCode());
        maps.put("service_name", "PTY_ONLINE_PAY");
        maps.put("input_charset", "UTF-8");
        maps.put("version", "V3.5.0");
        maps.put("sign_type", "MD5");


        maps.put("out_trade_no", param.getOrderId());
        maps.put("order_amount", param.getAmount());
        maps.put("order_time", time);
        maps.put("return_url", param.getPayReturnUrl());
        maps.put("notify_url", param.getPayNoticeUrl());
        maps.put("pay_type", "BANK_PAY");
        maps.put("bank_code", param.getBank());

        return maps;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return createPaySign(maps, key, new String[]{"sign_type", "sign"}).toUpperCase();
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "sign", "out_trade_no", "order_sn", "trade_status");
    }

    protected String getSuccessCode() {
        return "TRADE_SUCCESS";
    }

    public String getResultString(boolean success) {
        return success ? "SUCCESS" : "FAILD";
    }
}
