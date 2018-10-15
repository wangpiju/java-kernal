package com.pays.eka;

import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.HashMap;
import java.util.Map;

public class EkaPayApi
        extends PayApi {
    public static final String ID = "eka";
    private static final String INPUT_CHARSET = "GB2312";
    private static final String GATEWAY = "http://apika.10001000.com/chargebank.aspx";
    private static final String TITLE = "千网支付接口";

    public String getTitle() {
        return "千网支付接口";
    }

    public String getId() {
        return "eka";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://apika.10001000.com/chargebank.aspx";
    }

    protected void init() {
        addBank("CITIC", "962");
        addBank("BOC", "963");
        addBank("ABC", "964");
        addBank("CCB", "965");
        addBank("ICBC", "967");
        addBank("CZB", "968");

        addBank("CMB", "970");
        addBank("PSBC", "971");
        addBank("CIB", "972");

        addBank("SDB", "974");
        addBank("SHB", "975");
        addBank("SRCB", "976");
        addBank("SPDB", "977");
        addBank("SPA", "978");
        addBank("NJCB", "979");
        addBank("CMBC", "980");
        addBank("COMM", "981");
        addBank("HXB", "982");
        addBank("HZB", "983");

        addBank("GDB", "985");
        addBank("CEB", "986");
        addBank("BEA", "987");
        addBank("CBHB", "988");
        addBank("BOB", "989");
        addBank("BJRCB", "990");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append("parter").append("=").append((String) maps.get("parter")).append("&");
        sb.append("type").append("=").append((String) maps.get("type")).append("&");
        sb.append("value").append("=").append((String) maps.get("value")).append("&");
        sb.append("orderid").append("=").append((String) maps.get("orderid")).append("&");
        sb.append("callbackurl").append("=").append((String) maps.get("callbackurl"));
        sb.append(key);
        return MD5.encode(sb.toString(), "GB2312").toLowerCase();
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append("orderid").append("=").append((String) maps.get("orderid")).append("&");
        sb.append("opstate").append("=").append((String) maps.get("opstate")).append("&");
        sb.append("ovalue").append("=").append((String) maps.get("ovalue"));
        sb.append(key);
        return MD5.encode(sb.toString(), "GB2312").toLowerCase();
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "sign", "orderid", "sysorderid", "opstate");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("parter", param.getMerchantCode());
        maps.put("type", param.getBank());
        maps.put("value", param.getAmount());
        maps.put("orderid", param.getOrderId());
        maps.put("callbackurl", param.getPayNoticeUrl());
        maps.put("hrefbackurl", param.getPayReturnUrl());

        return maps;
    }

    protected String getSignName() {
        return "sign";
    }

    protected String getSuccessCode() {
        return "0";
    }

    public String getResultString(boolean success) {
        if (success) {
            return "opstate=0";
        }
        return "opstate=-1";
    }
}
