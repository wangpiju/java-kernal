package com.pays.rfu;

import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.HashMap;
import java.util.Map;

public class RfuPayApi
        extends PayApi {
    public static final String ID = "rfu";
    private static final String INPUT_CHARSET = "UTF-8";
    private static final String GATEWAY = "https://payment.rfupay.com/prod/commgr/control/inPayService";
    private static final String TITLE = "锐付支付接口";
    private static final String ENC_TYPE = "Md5";
    private static final String CARD_TYPE = "01";

    public String getTitle() {
        return "锐付支付接口";
    }

    public String getId() {
        return "rfu";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://payment.rfupay.com/prod/commgr/control/inPayService";
    }

    protected void init() {
        addBank("PSBC", "01000000");
        addBank("ICBC", "01020000");
        addBank("ABC", "01030000");
        addBank("BOC", "01040000");
        addBank("CCB", "01050000");
        addBank("COMM", "03010000");
        addBank("CITIC", "03020000");
        addBank("CEB", "03030000");
        addBank("HXB", "03040000");
        addBank("CMBC", "03050000");
        addBank("GDB", "03060000");
        addBank("SPA", "03070000");
        addBank("CMB", "03080000");
        addBank("CIB", "03090000");
        addBank("SPDB", "03100000");
        addBank("BEA", "03200000");
        addBank("BOB", "04031000");
        addBank("NJCB", "04243010");
        addBank("SRCB", "65012900");

        addBankCredit("WEIXIN", "WECHAT");
        addBankCredit("WAP", "MWEB");
        addBankCredit("MOBILE", "MOBILE");
        addBankCredit("ALIPAY", "ALIPAY");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();

        sb.append("orderNo").append((String) maps.get("orderNo"));
        sb.append("appType").append((String) maps.get("appType"));
        sb.append("orderAmount").append((String) maps.get("orderAmount"));
        sb.append("encodeType").append((String) maps.get("encodeType"));
        sb.append(key);

        return MD5.encode(sb.toString(), "UTF-8").toLowerCase();
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();

        sb.append("orderNo").append((String) maps.get("orderNo"));
        sb.append("appType").append((String) maps.get("appType"));
        sb.append("orderAmount").append((String) maps.get("orderAmount"));
        sb.append("succ").append((String) maps.get("succ"));
        sb.append("encodeType").append((String) maps.get("encodeType"));
        sb.append(key);

        return MD5.encode(sb.toString(), "UTF-8").toLowerCase();
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "signMD5", "orderNo", "tradeNo", "succ");
    }

    protected NoticeResult verify(Map<String, String> maps, String key, String keySign, String keyOrderNo, String keyTradeNo, String keyTradeStatus) {
        NoticeResult nr = super.verify(maps, key, keySign, keyOrderNo, keyTradeNo, keyTradeStatus);
        if (nr.isStatus()) {
            nr.setOrderId(nr.getOrderId().replace((CharSequence) maps.get("goods"), ""));
        }
        return nr;
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("partyId", param.getMerchantCode());
        maps.put("accountId", param.getEmail());


        maps.put("appType", param.getIsCredit().booleanValue() ? "" : param.getBank());
        maps.put("orderNo", param.getPublicKey() + param.getOrderId());
        maps.put("orderAmount", param.getAmount());
        maps.put("goods", param.getPublicKey());
        maps.put("returnUrl", param.getPayReturnUrl());
        maps.put("cardType", "01");


        maps.put("bank", param.getIsCredit().booleanValue() ? param.getBank() : "");
        maps.put("encodeType", "Md5");
        maps.put("refCode", "00000000");

        return maps;
    }

    protected String getSuccessCode() {
        return "Y";
    }

    public String getResultString(boolean success) {
        if (success) {
            return "checkok";
        }
        return "checkfaild";
    }

    protected String getSignName() {
        return "signMD5";
    }
}
