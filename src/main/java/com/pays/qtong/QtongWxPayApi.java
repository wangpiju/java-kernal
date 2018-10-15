package com.pays.qtong;

import com.hs3.utils.DateUtils;
import com.pays.PayApiParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QtongWxPayApi
        extends QtongPayApi {
    public static final String ID = "qtongWx";
    private static final String TITLE = "钱通微信接口";

    public String getTitle() {
        return "钱通微信接口";
    }

    public String getId() {
        return "qtongWx";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXIN");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        //jd-gui
        //String amount = new BigDecimal(param.getAmount()).multiply(new BigDecimal("100")).intValue();
        String amount = (new StringBuilder(String.valueOf((new BigDecimal(param.getAmount())).multiply(new BigDecimal("100")).intValue()))).toString();

        Map<String, String> map = new HashMap();
        map.put("application", "WeiXinScanOrder");
        map.put("version", "1.0.1");
        map.put("timestamp", DateUtils.format(new Date(), "yyyyMMddHHmmss"));
        map.put("merchantId", param.getMerchantCode());
        map.put("merchantOrderId", param.getOrderId());
        map.put("merchantOrderAmt", amount);
        map.put("merchantOrderDesc", param.getOrderId());
        map.put("userName", param.getOrderId());
        map.put("merchantPayNotifyUrl", param.getPayNoticeUrl());
        return map;
    }
}
