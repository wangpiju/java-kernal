package com.pays.e95;

import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.LinkedHashMap;
import java.util.Map;

public class E95PayApi
        extends PayApi {
    public static final String ID = "e95";
    private static final String GATEWAY = "https://www.95epay.cn/sslpayment";
    private static final String TITLE = "双乾-网银接口";
    private static final String PAY_TYPE = "CSPAY";
    private static final String[] md5ReqMap = {"Amount", "BillNo", "MerNo", "ReturnURL"};
    private static final String[] md5ResMap = {"Amount", "BillNo", "MerNo", "Succeed"};

    public String getTitle() {
        return "双乾-网银接口";
    }

    public String getId() {
        return "e95";
    }

    protected String getPayType() {
        return "CSPAY";
    }

    protected void init() {
        addBank("ABC", "ABC");
        addBank("BOC", "BOCSH");
        addBank("COMM", "BOCOM");
        addBank("CCB", "CCB");
        addBank("ICBC", "ICBC");
        addBank("PSBC", "PSBC");
        addBank("CMB", "CMB");
        addBank("SPDB", "SPDB");
        addBank("CEB", "CEB");
        addBank("CITIC", "CNCB");
        addBank("SPA", "PAB");
        addBank("CMBC", "CMBC");
        addBank("HXB", "HXB");
        addBank("GDB", "GDB");
        addBank("BOB", "BCCB");
        addBank("SHB", "BOS");
        addBank("BJRCB", "BRCB");
        addBank("CIB", "CIB");
        addBank("SRCB", "SRCB");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        String url = param.getPayReturnUrl();
        int n = url.indexOf("/", 8);
        String u1 = url.substring(0, n);
        String u2 = url.substring(n);
        url = u1 + u2.replaceAll("^/?/\\S+?/", "/");
        param.setPayReturnUrl(url);


        Map<String, String> maps = new LinkedHashMap();

        maps.put("Amount", param.getAmount());
        maps.put("BillNo", param.getOrderId());
        maps.put("MerNo", param.getMerchantCode());
        maps.put("ReturnURL", param.getPayReturnUrl());
        maps.put("PayType", getPayType());
        maps.put("NotifyURL", param.getPayNoticeUrl());
        maps.put("PaymentType", param.getBank());


        return maps;
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://www.95epay.cn/sslpayment";
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        for (String k : md5ReqMap) {
            sb.append(k).append("=").append((String) maps.get(k)).append("&");
        }
        sb.append(MD5.encode(key, "utf-8").toUpperCase());
        return MD5.encode(sb.toString(), "utf-8").toUpperCase();
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        for (String k : md5ResMap) {
            sb.append(k).append("=").append((String) maps.get(k)).append("&");
        }
        sb.append(MD5.encode(key, "utf-8").toUpperCase());
        return MD5.encode(sb.toString(), "utf-8").toUpperCase();
    }

    protected NoticeResult verifyDefault(Map<String, String> maps, String key) {
        NoticeResult rel = verify(maps, key, "MD5info", "BillNo", "BillNo",
                "Succeed");


        return rel;
    }

    public String getResultString(boolean success) {
        if (success) {
            return "success";
        }
        return "faile";
    }

    protected String getSignName() {
        return "MD5info";
    }

    protected String getSuccessCode() {
        return "88";
    }
}
