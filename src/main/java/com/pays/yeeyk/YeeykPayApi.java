package com.pays.yeeyk;

import com.pays.ApiParam;
import com.pays.HmacSign;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class YeeykPayApi
        extends PayApi {
    public static final String ID = "yeeyk";
    private static final String INPUT_CHARSET = "UTF-8";
    private static final String GATEWAY = "http://fpay.yeeyk.com/fourth-app/prof/unify";
    private static final String TITLE = "易游酷支付接口";

    public String getTitle() {
        return "易游酷支付接口";
    }

    public String getId() {
        return "yeeyk";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://fpay.yeeyk.com/fourth-app/prof/unify";
    }

    protected void init() {
        addBank("ALIPAY", "ALIPAY");
        addBank("WEIXIN", "WX");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder source = new StringBuilder();
        source.append((String) maps.get("merchantNo"));
        source.append((String) maps.get("merchantOrderno"));
        source.append((String) maps.get("requestAmount"));
        source.append((String) maps.get("noticeSysaddress"));
        source.append((String) maps.get("memberNo"));
        source.append((String) maps.get("memberGoods"));
        source.append((String) maps.get("payType"));

        return HmacSign.signToHex(source.toString(), key, "UTF-8");
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        StringBuilder source = new StringBuilder();
        source.append((String) maps.get("reCode"));
        source.append((String) maps.get("merchantNo"));
        source.append((String) maps.get("merchantOrderno"));
        source.append((String) maps.get("result"));
        source.append((String) maps.get("payType"));
        source.append((String) maps.get("memberGoods"));
        source.append((String) maps.get("amount"));

        return HmacSign.signToHex(source.toString(), key, "UTF-8");
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult rel = new NoticeResult();

        String signKey = createNoticeSign(maps, apiParam.getKey());

        String sign = (String) maps.get("hmac");
        if (!signKey.equals(sign)) {
            return rel;
        }
        rel.setStatus(getSuccessCode().equals(maps.get("reCode")));
        rel.setAmount(new BigDecimal((String) maps.get("amount")));
        rel.setOrderId((String) maps.get("merchantOrderno"));
        rel.setApiOrderId((String) maps.get("merchantOrderno"));
        return rel;
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("merchantNo", param.getMerchantCode());
        maps.put("merchantOrderno", param.getOrderId());
        maps.put("requestAmount", param.getAmount());
        maps.put("noticeSysaddress", param.getPayNoticeUrl());
        maps.put("memberNo", param.getTraceId());
        maps.put("memberGoods", "");
        maps.put("payType", param.getBank());

        return maps;
    }

    protected String getSignName() {
        return "hmac";
    }

    protected String getSuccessCode() {
        return "1";
    }

    public String getResultString(boolean success) {
        if (success) {
            return "SUCCESS";
        }
        return "FAILED";
    }
}
