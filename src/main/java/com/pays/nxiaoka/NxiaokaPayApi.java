package com.pays.nxiaoka;

import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.HashMap;
import java.util.Map;

public class NxiaokaPayApi
        extends PayApi {
    public static final String ID = "nxiaoka";
    private static final String INPUT_CHARSET = "GB2312";
    private static final String GATEWAY = "http://pay.wangfutongpay.com/PayBank.aspx";
    private static final String TITLE = "网富通支付接口";

    public String getTitle() {
        return "网富通支付接口";
    }

    public String getId() {
        return "nxiaoka";
    }

    private static final String[] SIGN_NAME = {"partner", "banktype", "paymoney", "ordernumber", "callbackurl"};
    private static final String[] VERY_NAME = {"partner", "ordernumber", "orderstatus", "paymoney"};

    protected String getRemoteUrl(PayApiParam param) {
        return "http://pay.wangfutongpay.com/PayBank.aspx";
    }

    protected void init() {
        addBank("ABC", "ABC");
        addBank("ICBC", "ICBC");
        addBank("CCB", "CCB");
        addBank("COMM", "BOCO");
        addBank("BOC", "BOC");
        addBank("CMB", "CMB");
        addBank("CMBC", "CMBC");
        addBank("CEB", "CEB");
        addBank("BOB", "BCCB");
        addBank("SHB", "SHB");
        addBank("NBCB", "NBCB");
        addBank("HXB", "HXB");
        addBank("CIB", "CIB");
        addBank("PSBC", "PSBS");
        addBank("SPA", "PINGANBANK");
        addBank("SPDB", "SPDB");
        addBank("BEA", "HKBEA");
        addBank("CITIC", "CTTIC");
        addBank("NJCB", "NJCB");
        addBank("CBHB", "CBHB");
        addBank("GDB", "GDB");
        addBank("BJRCB", "BJRCB");
        addBank("SRCB", "SRCB");
        addBank("SDB", "SDB");
    }

    protected String getSign(Map<String, String> maps, String key, String[] names) {
        StringBuilder sb = new StringBuilder();
        for (String n : names) {
            sb.append("&").append(n).append("=").append((String) maps.get(n));
        }
        sb.append(key);
        String s = sb.substring(1);
        return MD5.encode(s, "GB2312").toLowerCase();
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return getSign(maps, key, SIGN_NAME);
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        return getSign(maps, key, VERY_NAME);
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "sign", "ordernumber", "sysnumber", "orderstatus");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("version", "3.0");
        maps.put("method", "Rx.online.pay");
        maps.put("partner", param.getMerchantCode());
        maps.put("banktype", param.getBank());
        maps.put("paymoney", param.getAmount());
        maps.put("ordernumber", param.getOrderId());
        maps.put("callbackurl", param.getPayNoticeUrl());
        maps.put("hrefbackurl", param.getPayReturnUrl());
        maps.put("attach", "");
        maps.put("isshow", "1");

        return maps;
    }

    protected String getSuccessCode() {
        return "1";
    }

    public String getResultString(boolean success) {
        if (success) {
            return "ok";
        }
        return "no";
    }
}
