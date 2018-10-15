package com.pays.juxin;

import com.hs3.utils.DateUtils;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class JuxinPayApi
        extends PayApi {
    public static final String ID = "juxin";
    protected static final String PAY_TYPE_WX = "10";
    protected static final String PAY_TYPE_WX_WAP = "20";
    protected static final String PAY_TYPE_ALI_SCAN = "30";
    protected static final String PAY_TYPE_ALI_WAP = "40";
    protected static final String PAY_TYPE_YL = "50";
    protected static final String PAY_TYPE_QQ_SCAN = "60";
    protected static final String PAY_TYPE_QQ_WAP = "70";
    private static final String GATEWAY = "/gateway/payment";
    private static final String TITLE = "聚鑫网关支付接口";
    private static final String VERSION = "1.0";

    public String getTitle() {
        return "聚鑫网关支付接口";
    }

    public String getId() {
        return "juxin";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://" + param.getEmail() + "/gateway/payment";
    }

    protected void init() {
        addBank("ICBC", "ICBC");
        addBank("CCB", "CCB");
        addBank("ABC", "ABC");
        addBank("CMB", "CMB");
        addBank("COMM", "COMM");
        addBank("CMBC", "CMBC");
        addBank("CIB", "CIB");
        addBank("CEB", "CEB");
        addBank("CITIC", "CITIC");
        addBank("GZCB", "GZCB");
        addBank("HXB", "HXB");
        addBank("BOC", "BOC");
        addBank("BJRCB", "BJRCB");
        addBank("BOB", "BCCB");
        addBank("SDB", "SDB");
        addBank("SPA", "SZPAB");
        addBank("SHB", "BOS");
        addBank("PSBC", "PSBC");
        addBank("SPDB", "SPDB");
        addBank("GDB", "GDB");
    }

    protected String getPayType() {
        return "50";
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new LinkedHashMap();

        maps.put("version", "1.0");
        maps.put("agentId", param.getMerchantCode());
        maps.put("agentOrderId", param.getOrderId());
        maps.put("payType", getPayType());
        maps.put("bankCode", param.getBank());
        maps.put("payAmt", param.getAmount());
        maps.put("orderTime", DateUtils.format(new Date(), "yyyyMMddHHmmss"));
        maps.put("payIp", param.getIp());
        maps.put("notifyUrl", param.getPayNoticeUrl());
        maps.put("noticePage", param.getPayReturnUrl());
        maps.put("remark", "");

        return maps;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();

        sb.append((String) maps.get("version")).append("|");
        sb.append((String) maps.get("agentId")).append("|");
        sb.append((String) maps.get("agentOrderId")).append("|");
        sb.append((String) maps.get("payType")).append("|");
        sb.append((String) maps.get("payAmt")).append("|");
        sb.append((String) maps.get("orderTime")).append("|");
        sb.append((String) maps.get("payIp")).append("|");
        sb.append((String) maps.get("notifyUrl")).append("|");
        sb.append(key);

        return MD5.encode(sb.toString(), "utf-8");
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();

        sb.append((String) maps.get("version")).append("|");
        sb.append((String) maps.get("agentId")).append("|");
        sb.append((String) maps.get("agentOrderId")).append("|");
        sb.append((String) maps.get("jnetOrderId")).append("|");
        sb.append((String) maps.get("payAmt")).append("|");
        sb.append((String) maps.get("payResult")).append("|");
        sb.append(key);

        return MD5.encode(sb.toString(), "utf-8");
    }

    protected NoticeResult verifyDefault(Map<String, String> maps, String key) {
        return verify(maps, key, "sign", "agentOrderId", "jnetOrderId", "payResult");
    }

    protected String getSuccessCode() {
        return "SUCCESS";
    }

    public String getResultString(boolean success) {
        return success ? "OK" : "FAIL";
    }
}
