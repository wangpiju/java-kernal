package com.pays.keke;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.NoticeResult;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KeKeQuickPayApi
        extends KeKePayApi {
    public static final String ID = "kekeQuick";
    private static final String GATEWAY = "http://gateway.kekepay.com/quickGateWayPay/initPay";
    private static final String TITLE = "可可快捷支付接口";

    public String getTitle() {
        return "可可快捷支付接口";
    }

    public String getId() {
        return "kekeQuick";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://gateway.kekepay.com/quickGateWayPay/initPay";
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "sign", "outTradeNo", "trxNo", "tradeStatus");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("payKey", param.getMerchantCode());
        maps.put("orderPrice", param.getAmount());
        maps.put("outTradeNo", param.getOrderId());
        maps.put("productType", "40000303");
        maps.put("orderTime", DateUtils.format(new Date(), "yyyyMMddHHmmss"));
        maps.put("bankAccountNo", "123456789");
        maps.put("productName", param.getOrderId());
        maps.put("orderIp", param.getIp());
        maps.put("returnUrl", param.getPayReturnUrl());
        maps.put("notifyUrl", param.getPayNoticeUrl());

        return maps;
    }
}
