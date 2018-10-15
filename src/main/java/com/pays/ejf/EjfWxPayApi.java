package com.pays.ejf;

import com.pays.ApiParam;
import com.pays.HASH;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EjfWxPayApi
        extends PayApi {
    public static final String ID = "ejfWx";
    private static final String GATEWAY = "http://pay.smilepay.vip/v10/sha2/";
    private static final String TITLE = "易捷付-微信接口";
    private static final String PAY_TYPE = "Wechat";
    private static final String STR_NULL = "";
    private static final String SIGN_NAME = "signSHA2";
    private static final String ENCODE_TYPE = "SHA2";

    public String getTitle() {
        return "易捷付-微信接口";
    }

    public String getId() {
        return "ejfWx";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://pay.smilepay.vip/v10/sha2/";
    }

    protected String getPayType() {
        return "Wechat";
    }

    protected String getSignName() {
        return "signSHA2";
    }

    protected void init() {
        addBank("WEIXIN", "");
        addBankCredit("WEIXIN", "");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        String pk = (String) maps.remove("HashIV");
        StringBuilder sb = new StringBuilder();

        List<String> keys = new ArrayList(maps.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String k = (String) keys.get(i);
            if ((!k.equals("success")) && (!k.equals("signSHA2"))) {
                String value = (String) maps.get(k);
                sb.append("&").append(k).append("=").append(value);
            }
        }
        String str = "SHA2Key=" + key + sb.toString() + "&HashIV=" + pk;
        try {
            str = URLEncoder.encode(str, "utf-8").toLowerCase();
            return HASH.SHA256(str).toUpperCase();
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }

    public static void main(String[] args) {
        Map<String, String> maps = new HashMap();
        maps.put("encodeType", "SHA2");
        maps.put("merchantId", "90101170301136");
        maps.put("orderAmount", "10.00");
        maps.put("orderNo", "R170328220046819");
        maps.put("payMode", "Alipay");
        maps.put("tradeNo", "MD9AE9A10D1F01");
        maps.put("success", "Y");
        maps.put("signSHA2", "5EF00A6CAA00441D375D7800D4B553A0E8BEF313507619B43974916E2784A82F");

        maps.put("HashIV", "6B2C9B4EB5356D36DEC5FCCA17F60115");


        EjfWxPayApi n = new EjfWxPayApi();
        n.verify(maps, "D7C9C86372D24E4C9BE811542757B38DF588580D6EAE105581DD4A30A4177B75", "signSHA2", "orderNo", "tradeNo", "success");
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        maps.put("HashIV", apiParam.getPublicKey());

        return verify(maps, apiParam.getKey(), getSignName(), "orderNo", "tradeNo", "success");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();


        maps.put("merchantId", param.getMerchantCode());
        maps.put("payMode", getPayType());
        maps.put("orderNo", param.getOrderId());
        maps.put("orderAmount", param.getAmount());
        maps.put("goods", "");
        maps.put("notifyUrl", param.getPayNoticeUrl());
        maps.put("returnUrl", param.getPayReturnUrl());
        maps.put("bank", param.getBank());
        maps.put("encodeType", "SHA2");
        maps.put("HashIV", param.getPublicKey());
        return maps;
    }

    protected String getSuccessCode() {
        return "Y";
    }

    public String getResultString(boolean success) {
        if (success) {
            return "SUCCESS";
        }
        return "WO_RI";
    }
}
