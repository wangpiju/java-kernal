package com.pays.myjop;

import com.hs3.utils.DateUtils;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyjopScanPayApi
        extends MyjopPayApi {
    public static final String ID = "myjopScan";
    private static final String TITLE = "创瑞宝扫码支付接口";
    private static final String GETWAY = "http://gateway.myjop.cn/cnpPay/initPay";

    public String getTitle() {
        return "创瑞宝扫码支付接口";
    }

    public String getId() {
        return "myjopScan";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://gateway.myjop.cn/cnpPay/initPay";
    }

    protected void init() {
        addBank("WEIXIN", "");
        addBankCredit("ALIPAY", "");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("payKey", param.getMerchantCode());
        maps.put("orderPrice", param.getAmount());
        maps.put("outTradeNo", param.getOrderId());
        maps.put("productType", param.getEmail());
        maps.put("orderTime", DateUtils.format(new Date(), "yyyyMMddHHmmss"));
        maps.put("productName", param.getOrderId());
        maps.put("orderIp", param.getIp());
        maps.put("returnUrl", param.getPayReturnUrl());
        maps.put("notifyUrl", param.getPayNoticeUrl());
        maps.put("subPayKey", param.getPublicKey() == null ? "" : param.getPublicKey());
        maps.put("remark", param.getOrderId());

        return maps;
    }
}
