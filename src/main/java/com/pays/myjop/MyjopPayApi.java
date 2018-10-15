package com.pays.myjop;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyjopPayApi
        extends PayApi {
    public static final String ID = "myjop";
    private static final String TITLE = "创瑞宝支付接口";
    private static final String GETWAY = "http://gateway.myjop.cn/b2cPay/initPay";

    public String getTitle() {
        return "创瑞宝支付接口";
    }

    public String getId() {
        return "myjop";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://gateway.myjop.cn/b2cPay/initPay";
    }

    protected void init() {
        addBank("PSBC", "POST");
        addBank("ICBC", "ICBC");
        addBank("CITIC", "ECITIC");
        addBank("CIB", "CIB");
        addBank("CCB", "CCB");
        addBank("BOC", "BOC");
        addBank("ABC", "ABC");
        addBank("CEB", "CEB");
        addBank("GDB", "CGB");
        addBank("HXB", "HXB");

        addBankCredit("PSBC", "POST");
        addBankCredit("ICBC", "ICBC");
        addBankCredit("CITIC", "ECITIC");
        addBankCredit("CIB", "CIB");
        addBankCredit("CCB", "CCB");
        addBankCredit("BOC", "BOC");
        addBankCredit("ABC", "ABC");
        addBankCredit("CEB", "CEB");
        addBankCredit("GDB", "CGB");
        addBankCredit("HXB", "HXB");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("payKey", param.getMerchantCode());
        maps.put("orderPrice", param.getAmount());
        maps.put("outTradeNo", param.getOrderId());
        maps.put("productType", param.getEmail());
        maps.put("orderTime", DateUtils.format(new Date(), "yyyyMMddHHmmss"));
        maps.put("productName", param.getOrderId());
        maps.put("orderIp", param.getIp());
        maps.put("bankCode", param.getBank());
        maps.put("bankAccountType", param.getIsCredit().booleanValue() ? "PRIVATE_DEBIT_ACCOUNT" : "PRIVATE_CREDIT_ACCOUNT");
        maps.put("returnUrl", param.getPayReturnUrl());
        maps.put("notifyUrl", param.getPayNoticeUrl());
        maps.put("remark", param.getOrderId());

        return maps;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();

        sb.append(getDictSortStr(maps, new String[]{"sign"}));
        sb.append("&paySecret=").append(key);
        return MD5.encode(sb.toString(), "utf-8").toUpperCase();
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "sign", "outTradeNo", "trxNo", "tradeStatus");
    }

    public String getResultString(boolean success) {
        return success ? "SUCCESS" : "FAIL";
    }
}
