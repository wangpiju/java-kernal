package com.pays.cnw;

import com.hs3.lotts.losewin.LoseWinInfo;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class CnwPayApi
        extends PayApi {
    public static final String ID = "cnw";
    private static final String INPUT_CHARSET = "UTF-8";
    private static final String GATEWAY = "http://api.cnwpay.com/payapi/api/pcpay/merWeChatPay";
    private static final String TITLE = "银商通支付接口";

    public String getTitle() {
        return "银商通支付接口";
    }

    public String getId() {
        return "cnw";
    }

    protected void init() {
        addBank("WEIXIN", "76001");
        addBank("ALIPAY", "76003");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new TreeMap(new Comparator() {

            public int compare(Object o1, Object o2) {
                String l1 = (String) o1;
                String l2 = (String) o2;
                return l1.compareTo(l2);
            }

        });

        //jd-gui
        //String amount = new BigDecimal(param.getAmount()).multiply(new BigDecimal("100")).intValue();
        String amount = (new StringBuilder(String.valueOf((new BigDecimal(param.getAmount())).multiply(new BigDecimal("100")).intValue()))).toString();

        maps.put("order", param.getOrderId());
        //jd-gui
        //maps.put("transtime", System.currentTimeMillis());
        maps.put("transtime", (new StringBuilder(String.valueOf(System.currentTimeMillis()))).toString());
        maps.put("amount", amount);
        maps.put("productcategory", "1");
        maps.put("productname", "R" + amount);
        maps.put("productdesc", "R" + amount);
        maps.put("productprice", amount);
        maps.put("productcount", "1");
        maps.put("userip", param.getIp());
        maps.put("areturl", param.getPayNoticeUrl());
        maps.put("sreturl", param.getPayReturnUrl());
        maps.put("pnc", param.getBank());

        return maps;
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://api.cnwpay.com/payapi/api/pcpay/merWeChatPay";
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        maps.put("sign", sign);
        try {
            String data = URLEncoder.encode(new ObjectMapper().writeValueAsString(maps), "UTF-8");
            html.append(createInput("data", data));
            html.append(createInput("encryptkey", "1"));
            html.append(createInput("merchantaccount", param.getMerchantCode()));


            html.append(createInput("cnwOrder", param.getOrderId()));
            html.append(createInput("cnwAmount", param.getAmount()));
        } catch (Exception localException) {
        }
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        String signTemp = "";
        for (String k : maps.keySet()) {
            signTemp = signTemp + (String) maps.get(k);
        }
        signTemp = signTemp + key;
        return MD5.encode(signTemp, "UTF-8");
    }

    protected NoticeResult verify(Map<String, String> maps, String key, String keySign, String keyOrderNo, String keyTradeNo, String keyTradeStatus) {
        NoticeResult rel = new NoticeResult();

        String signKey = "";
        try {
            String data = URLDecoder.decode((String) maps.get("data"), "UTF-8");
            JsonNode jn = new ObjectMapper().readTree(data);
            Iterator<Map.Entry<String, JsonNode>> iter = jn.getFields();
            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> jsonNodeMap = (Map.Entry) iter.next();
                if (!getSignName().equals(jsonNodeMap.getKey())) {
                    signKey = signKey + (jsonNodeMap.getValue() == null ? "" : ((JsonNode) jsonNodeMap.getValue()).asText());
                }
            }
            signKey = MD5.encode(signKey + key, "UTF-8");

            String sign = jn.get(getSignName()).asText();
            if (!signKey.equals(sign)) {
                return rel;
            }
            String outOrderId = jn.get("order").asText();
            String instructCode = jn.get("payorderid").asText();
            String status = jn.get("status").asText();
            if ("1".equals(status)) {
                rel.setStatus(true);
            } else {
                rel.setStatus(false);
            }
            rel.setOrderId(outOrderId);
            rel.setApiOrderId(instructCode);
            return rel;
        } catch (Exception localException) {
        }
        return rel;
    }

    public String getResultString(boolean success) {
        return success ? "success" : "";
    }
}
