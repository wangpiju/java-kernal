package com.pays.zito;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ZitoPayApi
        extends PayApi {
    public static final String ID = "zito";
    private static final String TITLE = "融智付支付接口";
    private static final String GETWAY = "https://tech.zitopay.com/service/api/controller/zitopay/topayByMc";
    private static final String GETWAY_PRE = "https://pretech.zitopay.com/service/api/controller/zitopay/topayByMc";
    private static final String POST_TIME_FORMAT = "yyyyMMddhhmmssSSS";

    public String getTitle() {
        return "融智付支付接口";
    }

    public String getId() {
        return "zito";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return param.getIsCredit().booleanValue() ? "https://tech.zitopay.com/service/api/controller/zitopay/topayByMc" : "https://pretech.zitopay.com/service/api/controller/zitopay/topayByMc";
    }

    protected void init() {
        addBank("RMBJJK", "RMBJJK");
        addBankCredit("RMBJJK", "RMBJJK");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("id", param.getMerchantCode());
        maps.put("appid", param.getEmail());
        maps.put("posttime", DateUtils.format(new Date(), "yyyyMMddhhmmssSSS"));

        maps.put("gid", param.getPublicKey());
        maps.put("orderidinf", param.getOrderId());
        maps.put("totalPrice", param.getAmount());
        maps.put("ordertitle", "Recharge" + param.getAmount());
        maps.put("goodsname", "Recharge" + param.getAmount());
        maps.put("goodsdetail", "Recharge" + param.getAmount());
        maps.put("bgRetUrl", param.getPayNoticeUrl());
        maps.put("returnUrl", param.getPayReturnUrl());

        return maps;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        String paramsStr = (String) maps.get("id") + (String) maps.get("appid") + (String) maps.get("orderidinf") + (String) maps.get("totalPrice") + key;
        return MD5.encode(paramsStr, "utf-8");
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        maps.put("id", apiParam.getMerchantCode());


        maps.put("totalPrice", new BigDecimal((String) maps.get("totalPrice")).setScale(2, 5).toEngineeringString());
        return verify(maps, apiParam.getKey(), "sign", "orderidinf", "orderId", "success");
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        String paramsStr = "payreturn" + (String) maps.get("id") + (String) maps.get("appid") + (String) maps.get("orderidinf") + (String) maps.get("totalPrice") + key;
        return MD5.encode(paramsStr, "utf-8");
    }

    protected String getSuccessCode() {
        return "true";
    }

    public String getResultString(boolean success) {
        return success ? "SUCCESS" : "FAILED";
    }
}
