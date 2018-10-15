package com.pays.khbao;

import com.pays.MD5;
import com.pays.WithdrawApi;
import com.pays.WithdrawApiParam;

import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class KhbaoWithdrawApi
        extends WithdrawApi {
    protected void init() {
        addBank("ICBC", "");
        addBank("BOC", "");
        addBank("ABC", "");
        addBank("CCB", "");
        addBank("ADBC", "");
        addBank("RCU", "");
        addBank("BEA", "");

        addBank("BOB", "");
        addBank("CBHB", "");
        addBank("CEB", "");
        addBank("CIB", "");
        addBank("CITIC", "");
        addBank("CMB", "");

        addBank("COMM", "");
        addBank("CZB", "");
        addBank("GDB", "");
        addBank("GZCB", "");
        addBank("HSB", "");
        addBank("HXB", "");
        addBank("HZB", "");
        addBank("NBCB", "");
        addBank("NJCB", "");
        addBank("PSBC", "");
        addBank("SDB", "");
        addBank("SHB", "");
        addBank("SPA", "");
        addBank("SPDB", "");
        addBank("SRCB", "");
    }

    public boolean checkResult(Map<String, String> param, String key) {
        return encResult(param, key).equals(param.get("keyStr"));
    }

    public boolean checkResult(String result) {
        try {
            return "200".equals(new ObjectMapper().readTree(result).get("code").asText());
        } catch (Exception localException) {
        }
        return false;
    }

    public String encApply(Map<String, String> param, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append((String) param.get("orderID")).append("$");
        sb.append((String) param.get("userBankNum")).append("$");
        sb.append((String) param.get("payMoney")).append("$");
        sb.append(key);
        String sign = MD5.encode(sb.toString(), "utf-8");

        return sign.substring(0, 10) + sign.substring(22);
    }

    public String encConfirm(Map<String, String> param, String key) {
        return null;
    }

    public String encResult(Map<String, String> param, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append((String) param.get("orderID")).append("$");
        sb.append((String) param.get("payNumID")).append("$");
        sb.append((String) param.get("status")).append("$");
        sb.append(key);
        String sign = MD5.encode(sb.toString(), "utf-8");

        return sign.substring(0, 10) + sign.substring(22);
    }

    public Map<String, String> getPostMap(WithdrawApiParam param, String key) {
        Map<String, String> map = new LinkedHashMap();
        map.put("funName", "postTransferData");
        map.put("orderID", param.getOrderId());
        map.put("payBankType", "1");
        map.put("payBankName", "民生银行");
        map.put("userBankName", param.getIssueBankName());
        map.put("userBankNum", param.getCardNum());
        map.put("userRealName", param.getCardName());
        map.put("payMoney", param.getAmount());
        map.put("keyStr", encApply(map, key));

        return map;
    }

    public boolean isSuccessAll(Map<String, String> param) {
        return "3".equals(param.get("status"));
    }

    public boolean isSuccessParts(Map<String, String> param) {
        return false;
    }

    public String getOrderId(Map<String, String> param) {
        return (String) param.get("orderID");
    }

    public String getTradeNo(Map<String, String> param) {
        return (String) param.get("payNumID");
    }

    public String getReturnMessage(String orderId, String tradeNo, String status, String message) {
        return "1".equals(status) ? "Success" : message;
    }
}
