package com.pays.zb;

import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.HashMap;
import java.util.Map;

public class ZbPayApi
        extends PayApi {
    public static final String ID = "zb";
    private static final String GATEWAY = "https://gateway.zbpay.cc/GateWay/index.aspx";
    private static final String TITLE = "众宝支付接口";
    private static final String[] SIGN_LIST = {"customer", "banktype", "amount", "orderid", "asynbackurl", "request_time"};
    private static final String[] VERY_LIST = {"orderid", "result", "amount", "zborderid", "completetime"};

    protected void init() {
        addBank("CITIC", "962");
        addBank("BOC", "963");
        addBank("ABC", "964");
        addBank("CCB", "965");
        addBank("ICBC", "967");
        addBank("CMB", "970");
        addBank("PSBC", "971");
        addBank("CIB", "972");
        addBank("SRCB", "976");
        addBank("SPDB", "977");
        addBank("CMBC", "980");
        addBank("COMM", "981");
        addBank("HZB", "983");
        addBank("GDB", "985");
        addBank("CEB", "986");
        addBank("BEA", "987");
        addBank("BOB", "989");
    }

    public String getTitle() {
        return "众宝支付接口";
    }

    public String getId() {
        return "zb";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://gateway.zbpay.cc/GateWay/index.aspx";
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        for (String k : SIGN_LIST) {
            String value = (String) maps.get(k);
            sb.append(k).append("=").append(value).append("&");
        }
        sb.append("key=").append(key);
        return MD5.getDigest(sb.toString());
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        for (String k : VERY_LIST) {
            String value = (String) maps.get(k);
            sb.append(k).append("=").append(value).append("&");
        }
        sb.append("key=").append(key);
        return MD5.getDigest(sb.toString());
    }

    protected String getSignName() {
        return "sign";
    }

    protected String getSuccessCode() {
        return "0";
    }

    protected NoticeResult verifyDefault(Map<String, String> maps, String key) {
        return verify(maps, key, "sign", "orderid", "zborderid", "result");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        //jd-gui
        //String t = System.currentTimeMillis() / 1000L;
        String t = (new StringBuilder(String.valueOf(System.currentTimeMillis() / 1000L))).toString();
        Map<String, String> maps = new HashMap();
        maps.put("customer", param.getMerchantCode());
        maps.put("banktype", param.getBank());
        maps.put("amount", param.getAmount());
        maps.put("orderid", param.getOrderId());
        maps.put("asynbackurl", param.getPayNoticeUrl());
        maps.put("request_time", t);

        return maps;
    }

    public String getResultString(boolean success) {
        if (success) {
            return "opstate=0";
        }
        return "failed";
    }
}
