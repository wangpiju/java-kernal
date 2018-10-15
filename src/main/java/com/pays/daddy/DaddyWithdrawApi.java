package com.pays.daddy;

import com.pays.MD5;
import com.pays.WithdrawApi;
import com.pays.WithdrawApiParam;

import java.util.LinkedHashMap;
import java.util.Map;

public class DaddyWithdrawApi
        extends WithdrawApi {
    protected void init() {
        addBank("ICBC", "1");
        addBank("CMB", "2");
        addBank("CCB", "3");
        addBank("ABC", "4");
        addBank("BOC", "5");
        addBank("COMM", "6");
        addBank("CMBC", "7");
        addBank("CITIC", "8");
        addBank("SPDB", "9");
        addBank("PSBC", "10");
        addBank("CEB", "11");
        addBank("SPA", "12");
        addBank("GDB", "13");
        addBank("HXB", "14");
        addBank("CIB", "15");
    }

    public String getOrderId(Map<String, String> param) {
        return (String) param.get("company_order_num");
    }

    public String getTradeNo(Map<String, String> param) {
        return (String) param.get("mownecum_order_num");
    }

    public String encApply(Map<String, String> param, String key) {
        StringBuilder sb = new StringBuilder(MD5.encode(key, "utf-8"));
        sb.append((String) param.get("company_id"));
        sb.append((String) param.get("bank_id"));
        sb.append((String) param.get("company_order_num"));
        sb.append((String) param.get("amount"));
        sb.append((String) param.get("card_num"));
        sb.append((String) param.get("card_name"));
        sb.append((String) param.get("company_user"));
        sb.append((String) param.get("issue_bank_name"));
        sb.append((String) param.get("issue_bank_address"));
        sb.append((String) param.get("memo"));
        return MD5.encode(sb.toString(), "utf-8");
    }

    public String encConfirm(Map<String, String> param, String key) {
        StringBuilder sb = new StringBuilder(MD5.encode(key, "utf-8"));
        sb.append((String) param.get("company_order_num"));
        sb.append((String) param.get("mownecum_order_num"));
        sb.append((String) param.get("amount"));
        sb.append((String) param.get("card_num"));
        sb.append((String) param.get("card_name"));
        sb.append((String) param.get("company_user"));
        return MD5.encode(sb.toString(), "utf-8");
    }

    public String encResult(Map<String, String> param, String key) {
        StringBuilder sb = new StringBuilder(MD5.encode(key, "utf-8"));
        sb.append((String) param.get("mownecum_order_num"));
        sb.append((String) param.get("company_order_num"));
        sb.append((String) param.get("status"));
        sb.append((String) param.get("amount"));
        sb.append((String) param.get("exact_transaction_charge"));
        return MD5.encode(sb.toString(), "utf-8");
    }

    public Map<String, String> getPostMap(WithdrawApiParam param, String key) {
        Map<String, String> map = new LinkedHashMap();
        map.put("company_id", param.getMerchantCode());
        map.put("bank_id", getBank(param.getBank()));
        map.put("company_order_num", param.getOrderId());
        map.put("amount", param.getAmount());
        map.put("card_num", param.getCardNum());
        map.put("card_name", param.getCardName());
        map.put("company_user", param.getAccount());
        map.put("issue_bank_name", param.getIssueBankName());
        map.put("issue_bank_address", param.getIssueBankAddress());
        map.put("memo", param.getMemo());

        map.put("key", encApply(map, key));
        return map;
    }

    public String getReturnMessage(String orderId, String tradeNo, String status, String message) {
        return "{\"company_order_num\":\"" + orderId + "\",\"mownecum_order_num\":\"" + tradeNo + "\",\"status\":" + status + ",\"error_msg\":\"" + message + "\"}";
    }
}
