package com.pays.din;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;
import com.pays.RSA;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DinRsaPayApi
        extends PayApi {
    public static final String ID = "dinRsa";
    private static final String INPUT_CHARSET = "UTF-8";
    private static final String GATEWAY = "https://pay.dinpay.com/gateway?input_charset=UTF-8";
    private static final String GATEWAY_WEIXIN = "https://api.dinpay.com/gateway/api/weixin";
    private static final String TITLE = "智付RSA-S支付接口";
    private static final String PAY_TYPE = "b2c";
    private static final String SERVICE_TYPE = "direct_pay";
    private static final String SERVICE_TYPE_WEIXIN = "wxpay";
    private static final String INTERFACE_VERSION = "V3.0";
    private static final String SIGN_TYPE = "RSA-S";
    private static final String NOTIFY_TYPE_PAGE = "page_notify";

    public String getTitle() {
        return "智付RSA-S支付接口";
    }

    public String getId() {
        return "dinRsa";
    }

    protected String getRemoteUrl(PayApiParam param) {
        if ("WEIXIN".equalsIgnoreCase(param.getBank())) {
            return "https://api.dinpay.com/gateway/api/weixin";
        }
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

        addBankCredit("WEIXIN", "WEIXIN");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return RSA.sign(super.getDictSortStr(maps, new String[]{"sign_type", "sign"}), key, "", "MD5withRSA");
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult noticeResult = new NoticeResult();

        String plainText = super.getDictSortStr(maps, new String[]{"sign_type", "sign"});
        String signature = (String) maps.get("sign");
        boolean result = RSA.verify(plainText, signature, apiParam.getPublicKey(), "", "MD5withRSA");
        if ((result) &&
                (getResultString(result).equals(maps.get("trade_status")))) {
            noticeResult.setStatus(true);
            noticeResult.setOrderId((String) maps.get("order_no"));
            noticeResult.setApiOrderId((String) maps.get("trade_no"));
            if ("page_notify".equals(maps.get("notify_type"))) {
                noticeResult.setNotifyType(1);
            }
        }
        return noticeResult;
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();


        maps.put("merchant_code", param.getMerchantCode());
        if ("WEIXIN".equalsIgnoreCase(param.getBank())) {
            maps.put("service_type", "wxpay");
        } else {
            maps.put("service_type", "direct_pay");
            maps.put("pay_type", "b2c");
            maps.put("input_charset", "UTF-8");
            maps.put("return_url", param.getPayReturnUrl());
            maps.put("client_ip", param.getIp());
            maps.put("bank_code", param.getBank());
        }
        maps.put("notify_url", param.getPayNoticeUrl());
        maps.put("interface_version", "V3.0");


        maps.put("order_no", param.getOrderId());
        maps.put("order_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        maps.put("order_amount", param.getAmount());
        maps.put("product_name", param.getOrderId());
        maps.put("sign_type", "RSA-S");

        return maps;
    }

    public String getResultString(boolean success) {
        if (success) {
            return "SUCCESS";
        }
        return "FAILED";
    }
}
