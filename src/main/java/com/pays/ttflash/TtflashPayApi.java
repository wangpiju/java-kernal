package com.pays.ttflash;

import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.HashMap;
import java.util.Map;

public class TtflashPayApi
        extends PayApi {
    public static final String ID = "ttflash";
    private static final String INPUT_CHARSET = "GB2312";
    private static final String GATEWAY = "https://gw.169.cc/interface/AutoBank/index.aspx";
    private static final String TITLE = "闪付支付接口";

    public String getTitle() {
        return "闪付支付接口";
    }

    public String getId() {
        return "ttflash";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://gw.169.cc/interface/AutoBank/index.aspx";
    }

    protected void init() {
        addBank("ABC", "964");
        addBank("ICBC", "967");
        addBank("CCB", "965");
        addBank("COMM", "981");
        addBank("BOC", "963");
        addBank("CMB", "970");
        addBank("CMBC", "980");
        addBank("CEB", "986");
        addBank("BOB", "989");
        addBank("CIB", "972");
        addBank("PSBC", "971");
        addBank("SPA", "978");
        addBank("SPDB", "977");
        addBank("BEA", "987");
        addBank("CITIC", "962");
        addBank("NJCB", "979");
        addBank("GDB", "985");
        addBank("SRCB", "976");
        addBank("HZB", "983");

        addBank("WEIXIN", "1004");
        addBank("ALIPAY", "992");

        addBankCredit("WEIXIN", "1007");
        addBankCredit("ALIPAY", "1006");
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
