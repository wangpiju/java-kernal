package com.pays;

import com.pays.daddy.DaddyWithdrawApi;
import com.pays.hs.HsWithdrawApi;
import com.pays.khbao.KhbaoWithdrawApi;
import com.pays.sfis.SfisWithdrawApi;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public abstract class WithdrawApi {
    public boolean checkApprove(Map<String, String> param, String key) {
        return encConfirm(param, key).equals(param.get("key"));
    }

    public boolean checkResult(Map<String, String> param, String key) {
        return encResult(param, key).equals(param.get("key"));
    }

    public boolean isSuccessAll(Map<String, String> param) {
        return "1".equals(param.get("status"));
    }

    public boolean isSuccessParts(Map<String, String> param) {
        return "2".equals(param.get("status"));
    }

    public BigDecimal getAmount(Map<String, String> param) {
        return new BigDecimal((String) param.get("amount"));
    }

    public boolean checkResult(String result) {
        return false;
    }

    public String getReturnMessage(String orderId, String tradeNo, String status, String message) {
        return "Success";
    }

    private static Map<String, WithdrawApi> instances = new HashMap();

    static {
        instances.put(DaddyWithdrawApi.class.getSimpleName(), new DaddyWithdrawApi());
        instances.put(HsWithdrawApi.class.getSimpleName(), new HsWithdrawApi());
        instances.put(KhbaoWithdrawApi.class.getSimpleName(), new KhbaoWithdrawApi());
        instances.put(SfisWithdrawApi.class.getSimpleName(), new SfisWithdrawApi());
    }

    public static WithdrawApi getInstance(String className) {
        return (WithdrawApi) instances.get(className);
    }

    public static Map<String, WithdrawApi> getInstances() {
        return instances;
    }

    private Map<String, String> banks = new HashMap();

    protected void addBank(String key, String value) {
        this.banks.put(key, value);
    }

    protected String getBank(String key) {
        return (String) this.banks.get(key);
    }

    public boolean containsBank(String key) {
        return this.banks.containsKey(key);
    }

    public WithdrawApi() {
        init();
    }

    public abstract String encApply(Map<String, String> paramMap, String paramString);

    public abstract String encConfirm(Map<String, String> paramMap, String paramString);

    public abstract String encResult(Map<String, String> paramMap, String paramString);

    public abstract Map<String, String> getPostMap(WithdrawApiParam paramWithdrawApiParam, String paramString);

    public abstract String getOrderId(Map<String, String> paramMap);

    public abstract String getTradeNo(Map<String, String> paramMap);

    protected void init() {
    }
}
