package com.pays.worth;

import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.HashMap;
import java.util.Map;

public class WorthPayApi
        extends PayApi {
    public static final String ID = "worth";
    private static final String INPUT_CHARSET = "GBK";
    private static final String GATEWAY = "https://ebank.payworth.net/portal?charset=GBK";
    private static final String TITLE = "华势在线支付接口";
    private static final String ENC_TYPE = "MD5";

    public String getTitle() {
        return "华势在线支付接口";
    }

    public String getId() {
        return "worth";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://ebank.payworth.net/portal?charset=GBK";
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

        sb.append(getDictSortStr(maps, new String[]{"sign", "sign_type"}));
        sb.append(key);
        return MD5.encode(sb.toString(), "GBK");
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "sign", "order_no", "trade_no", "trade_status");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("service", "online_pay");
        maps.put("merchant_ID", param.getMerchantCode());
        maps.put("notify_url", param.getPayNoticeUrl());
        maps.put("return_url", param.getPayReturnUrl());
        maps.put("sign_type", "MD5");
        maps.put("charset", "GBK");
        maps.put("title", "Recharge" + param.getAmount());
        maps.put("body", "Recharge" + param.getAmount());
        maps.put("order_no", param.getOrderId());
        maps.put("total_fee", param.getAmount());
        maps.put("payment_type", "1");
        maps.put("paymethod", "directPay");
        maps.put("defaultbank", param.getBank());
        maps.put("seller_email", param.getEmail());

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
