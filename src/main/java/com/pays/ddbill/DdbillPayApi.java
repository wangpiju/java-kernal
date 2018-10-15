package com.pays.ddbill;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;
import com.pays.RSA;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DdbillPayApi
        extends PayApi {
    public static final String ID = "ddbill";
    private static final String TITLE = "多得宝-网银支付接口";
    private static final String GATEWAY = "https://pay.ddbill.com/gateway?input_charset=UTF-8";
    private static final String PAY_TYPE = "b2c";
    private static final String SERVICE_TYPE = "direct_pay";
    private static final String INTERFACE_VERSION = "V3.0";
    private static final String SIGN_TYPE = "RSA-S";
    private static final String NOTIFY_TYPE_PAGE = "page_notify";

    public String getTitle() {
        return "多得宝-网银支付接口";
    }

    public String getId() {
        return "ddbill";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://pay.ddbill.com/gateway?input_charset=UTF-8";
    }

    protected String getVersion() {
        return "V3.0";
    }

    protected String getSignType() {
        return "RSA-S";
    }

    protected String getServiceType() {
        return "direct_pay";
    }

    protected String getPayType() {
        return "b2c";
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
        maps.put("service_type", getServiceType());
        maps.put("notify_url", param.getPayNoticeUrl());
        maps.put("interface_version", getVersion());
        maps.put("input_charset", "utf-8");
        maps.put("sign_type", getSignType());

        maps.put("return_url", param.getPayReturnUrl());
        maps.put("pay_type", getPayType());
        maps.put("client_ip", param.getIp());
        maps.put("bank_code", param.getBank());


        maps.put("order_no", param.getOrderId());
        maps.put("order_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        maps.put("order_amount", param.getAmount());
        maps.put("product_name", param.getOrderId());

        return maps;
    }

    public String getResultString(boolean success) {
        if (success) {
            return "SUCCESS";
        }
        return "FAILED";
    }
}
