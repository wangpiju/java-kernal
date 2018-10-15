package com.pays.yee;

import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.HashMap;
import java.util.Map;

public class YeePayApi
        extends PayApi {
    public static final String ID = "yee";
    private static final String INPUT_CHARSET = "GB2312";
    private static final String GATEWAY = "http://wytj.9vpay.com/PayBank.aspx";
    private static final String TITLE = "银宝支付接口";

    public String getTitle() {
        return "银宝支付接口";
    }

    public String getId() {
        return "yee";
    }

    protected String getRemoteUrl(PayApiParam param) {
        StringBuilder content = new StringBuilder("?");
        Map<String, String> maps = getParamMaps(param);
        for (String name : maps.keySet()) {
            content.append(name).append("=").append((String) maps.get(name)).append("&");
        }
        content.append("sign").append("=").append(createPaySign(maps, param.getKey()));
        return "http://wytj.9vpay.com/PayBank.aspx" + content.toString();
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

        addBank("ALIPAY", "ALIPAY");
        addBank("TENPAY", "TENPAY");
        addBank("WEIXIN", "WEIXIN");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append("partner").append("=").append((String) maps.get("partner")).append("&");
        sb.append("banktype").append("=").append((String) maps.get("banktype")).append("&");
        sb.append("paymoney").append("=").append((String) maps.get("paymoney")).append("&");
        sb.append("ordernumber").append("=").append((String) maps.get("ordernumber")).append("&");
        sb.append("callbackurl").append("=").append((String) maps.get("callbackurl"));
        sb.append(key);
        return MD5.encode(sb.toString(), "GB2312").toLowerCase();
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append("partner").append("=").append((String) maps.get("partner")).append("&");
        sb.append("ordernumber").append("=").append((String) maps.get("ordernumber")).append("&");
        sb.append("orderstatus").append("=").append((String) maps.get("orderstatus")).append("&");
        sb.append("paymoney").append("=").append((String) maps.get("paymoney"));
        sb.append(key);
        return MD5.encode(sb.toString(), "GB2312").toLowerCase();
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        html.append(createInput("paymoney", (String) maps.get("paymoney")));
        html.append(createInput("ordernumber", (String) maps.get("ordernumber")));
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "sign", "ordernumber", "sysnumber", "orderstatus");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("partner", param.getMerchantCode());
        maps.put("banktype", param.getBank());
        maps.put("paymoney", param.getAmount());
        maps.put("ordernumber", param.getOrderId());
        maps.put("callbackurl", param.getPayNoticeUrl());
        maps.put("hrefbackurl", param.getPayReturnUrl());
        maps.put("attach", "");

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
