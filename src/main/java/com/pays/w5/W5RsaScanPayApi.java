package com.pays.w5;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;
import com.pays.RSA;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class W5RsaScanPayApi
        extends PayApi {
    public static final String ID = "w5RsaScan";
    protected static final String SERVICE_TYPE_ALIPAY = "alipay_scan";
    protected static final String SERVICE_TYPE_WEIXIN = "weixin_scan";
    protected static final String SERVICE_TYPE_QQ = "tenpay_scan";
    private static final String TITLE = "W付RSA扫码支付接口";
    private static final String GATEWAY = "https://api.5wpay.net/gateway/api/scanpay";
    private static final String INTERFACE_VERSION = "V3.1";
    private static final String SIGN_TYPE = "RSA-S";
    private static final String NOTIFY_TYPE_PAGE = "page_notify";

    public String getTitle() {
        return "W付RSA扫码支付接口";
    }

    public String getId() {
        return "w5RsaScan";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://api.5wpay.net/gateway/api/scanpay";
    }

    protected void init() {
        addBank("ALIPAY", "alipay_scan");
        addBank("WEIXIN", "weixin_scan");
        addBank("QQ", "tenpay_scan");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();


        maps.put("merchant_code", param.getMerchantCode());
        maps.put("service_type", param.getBank());
        maps.put("notify_url", param.getPayNoticeUrl());
        maps.put("interface_version", "V3.1");
        maps.put("client_ip", param.getIp());
        maps.put("sign_type", "RSA-S");


        maps.put("order_no", param.getOrderId());
        maps.put("order_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        maps.put("order_amount", param.getAmount());
        maps.put("product_name", param.getOrderId());

        return maps;
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
                (getResultString(true).equals(maps.get("trade_status")))) {
            noticeResult.setStatus(true);
            noticeResult.setOrderId((String) maps.get("order_no"));
            noticeResult.setApiOrderId((String) maps.get("trade_no"));
            if ("page_notify".equals(maps.get("notify_type"))) {
                noticeResult.setNotifyType(1);
            }
        }
        return noticeResult;
    }

    public String getResultString(boolean success) {
        if (success) {
            return "SUCCESS";
        }
        return "FAILED";
    }
}
