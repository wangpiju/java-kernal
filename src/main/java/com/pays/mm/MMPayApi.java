package com.pays.mm;

import com.pays.ApiParam;
import com.pays.HASH;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.HashMap;
import java.util.Map;

public class MMPayApi
        extends PayApi {
    public static final String ID = "mm";
    private static final String INPUT_CHARSET = "UTF-8";
    private static final String GATEWAY = "https://ebank.zftong.net";
    private static final String TITLE = "MM支付接口";
    private static final String ENC_TYPE = "SHA";
    private static final String APP = "app";

    public String getTitle() {
        return "MM支付接口";
    }

    public String getId() {
        return "mm";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://ebank.zftong.net/payment/v1/order/" + param.getMerchantCode() + "-" + param.getOrderId();
    }

    protected String isApp() {
        return "app";
    }

    protected void init() {
        addBank("CMB", "CMB");
        addBank("ICBC", "ICBC");
        addBank("CCB", "CCB");
        addBank("BOC", "BOC");
        addBank("ABC", "ABC");
        addBank("COMM", "BOCM");
        addBank("SPDB", "SPDB");
        addBank("GDB", "CGB");
        addBank("CITIC", "CITIC");
        addBank("CEB", "CEB");
        addBank("CIB", "CIB");
        addBank("SPA", "PAYH");
        addBank("CMBC", "CMBC");
        addBank("HXB", "HXB");
        addBank("PSBC", "PSBC");
        addBank("BOB", "BCCB");
        addBank("SHB", "SHBANK");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();

        sb.append(getDictSortStr(maps, new String[]{"sign", "signType"}));
        sb.append(key);

        String sign = HASH.SHA1(sb.toString()).toUpperCase();
        return sign;
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "sign", "order_no", "trade_no", "trade_status");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("service", "online_pay");
        maps.put("merchantId", param.getMerchantCode());
        maps.put("notifyUrl", param.getPayNoticeUrl());
        maps.put("returnUrl", param.getPayReturnUrl());
        maps.put("signType", "SHA");
        maps.put("charset", "UTF-8");
        maps.put("title", "Recharge" + param.getAmount());
        maps.put("body", "Recharge" + param.getAmount());
        maps.put("orderNo", param.getOrderId());
        maps.put("totalFee", param.getAmount());
        maps.put("paymentType", "1");
        maps.put("paymethod", "directPay");
        maps.put("defaultbank", param.getBank());
        maps.put("isApp", isApp());
        maps.put("sellerEmail", param.getEmail());

        return maps;
    }

    protected String getSuccessCode() {
        return "TRADE_FINISHED";
    }

    public String getResultString(boolean success) {
        if (success) {
            return "success";
        }
        return "faild";
    }

    protected String getSignName() {
        return "sign";
    }
}
