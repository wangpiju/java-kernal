package com.pays.gloden;

import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GlodenPayApi
        extends PayApi {
    public static final String ID = "gloden";
    private static final String GATEWAY = "http://i.boLuoxuan.com/pay.api";
    private static final String TITLE = "Gloden支付接口";
    private static final String P1_MD = "1";
    private static final String P6_CATEGORY = "1";
    private static final String P11_EX = "1";

    public String getTitle() {
        return "Gloden支付接口";
    }

    public String getId() {
        return "gloden";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://i.boLuoxuan.com/pay.api";
    }

    protected void init() {
        addBank("BOC", "10001");
        addBank("ABC", "10002");
        addBank("ICBC", "10003");
        addBank("CCB", "10004");

        addBank("CMB", "10006");
        addBank("CMBC", "10007");
        addBank("CIB", "10008");
        addBank("SPDB", "10009");
        addBank("GDB", "10010");
        addBank("CITIC", "10011");
        addBank("CEB", "10012");
        addBank("PSBC", "10013");
        addBank("SPA", "10014");
        addBank("BOB", "10015");
        addBank("NJCB", "10016");


        addBank("BEA", "10019");
        addBank("HXB", "10020");

        addBankCredit("RMBJJK", "10021");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new LinkedHashMap();

        maps.put("p1_md", "1");
        maps.put("p2_xn", param.getOrderId());
        maps.put("p3_bn", param.getMerchantCode());
        maps.put("p4_pd", param.getBank());
        maps.put("p5_amount", param.getAmount());
        maps.put("p6_category", "1");
        maps.put("p7_name", param.getOrderId());
        maps.put("p8_desc", param.getOrderId());
        maps.put("p9_ip", param.getIp());
        maps.put("p10_url", param.getPayNoticeUrl());
        maps.put("p11_ex", "1");
        return maps;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();

        List<String> keys = new ArrayList(maps.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String k = (String) keys.get(i);
            String value = (String) maps.get(k);
            sb.append("&").append(k).append("=").append(value);
        }
        String str = sb.substring(1).toString() + key;
        return MD5.encode(str, "utf-8").toUpperCase();
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        String str = getDictSortStr(maps, new String[]{"sign"}) + key;
        return MD5.encode(str, "utf-8").toUpperCase();
    }

    protected NoticeResult verifyDefault(Map<String, String> maps, String key) {
        NoticeResult rel = verify(maps, key, "sign", "p3_xn", "p2_sn", "p7_st");
        if (rel.isStatus()) {
            if (!"1".equals(maps.get("p8_reply"))) {
                rel.setNotifyType(1);
            }
        }
        return rel;
    }

    public String getResultString(boolean success) {
        if (success) {
            return "success";
        }
        return "faile";
    }
}
