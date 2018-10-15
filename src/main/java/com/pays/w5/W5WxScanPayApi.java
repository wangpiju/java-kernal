package com.pays.w5;

import com.pays.MD5;
import com.pays.PayApiParam;

import java.util.Map;

public class W5WxScanPayApi
        extends W5PayApi {
    public static final String ID = "w5WxScan";
    private static final String TITLE = "W5微信扫码支付接口";

    public String getTitle() {
        return "W5微信扫码支付接口";
    }

    public String getId() {
        return "w5WxScan";
    }

    protected void init() {
        addBank("WEIXIN", "2");
    }

    protected String getService() {
        return "TRADE.SCANPAY";
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        maps.put("typeId", param.getBank());
        return maps;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        String resultStr = String.format("service=%s&version=%s&merId=%s&typeId=%s&tradeNo=%s&tradeDate=%s&amount=%s&notifyUrl=%s&extra=%s&summary=%s&expireTime=%s&clientIp=%s", new Object[]{maps.get("service"),
                maps.get("version"), maps.get("merId"), maps.get("typeId"), maps.get("tradeNo"), maps.get("tradeDate"), maps.get("amount"), maps.get("notifyUrl"), maps.get("extra"),
                maps.get("summary"), maps.get("expireTime"), maps.get("clientIp")});
        return MD5.encode(resultStr + key, "utf-8").toUpperCase();
    }
}
