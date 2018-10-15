package com.pays.xingxin2;

import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.HashMap;
import java.util.Map;

public class Xingxin2WxPayApi
        extends PayApi {
    public static final String ID = "xingxin2Wx";
    private static final String GATEWAY = "http://api.wangdailm.top/";
    private static final String TITLE = "兴信（新）微信扫码支付接口";

    public String getTitle() {
        return "兴信（新）微信扫码支付接口";
    }

    public String getId() {
        return "xingxin2Wx";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://api.wangdailm.top/" + (param.getIsCredit().booleanValue() ? "passivePay.jhtml" : "wapPay.jhtml");
    }

    protected void init() {
        addBank("WEIXIN", "2");
        addBankCredit("WEIXIN", "2");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();

        sb.append(getDictSortStr(maps, new String[]{"signature"}));
        sb.append("&").append(key);
        return MD5.encode(sb.toString(), "utf-8").toUpperCase();
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "signature", "traceno", "traceno", "status");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("merchno", param.getMerchantCode());
        maps.put("amount", param.getAmount());
        maps.put("traceno", param.getOrderId());
        maps.put("payType", param.getBank());
        maps.put("goodsName", param.getOrderId());
        maps.put("notifyUrl", param.getPayNoticeUrl());
        maps.put("remark", param.getOrderId());

        return maps;
    }

    protected String getSuccessCode() {
        return "1";
    }

    public String getResultString(boolean success) {
        if (success) {
            return "success";
        }
        return "fail";
    }

    protected String getSignName() {
        return "signature";
    }
}
