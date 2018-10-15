package com.pays.keke;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KeKePayApi
        extends PayApi {
    public static final String ID = "keke";
    private static final String INPUT_CHARSET = "UTF-8";
    private static final String GATEWAY = "http://gateway.kekepay.com/b2cPay/initPay";
    private static final String TITLE = "可可支付接口";

    public String getTitle() {
        return "可可支付接口";
    }

    public String getId() {
        return "keke";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://gateway.kekepay.com/b2cPay/initPay";
    }

    protected void init() {
        addBank("ICBC", "ICBC");
        addBank("CMB", "CMBCHINA");
        addBank("ABC", "ABC");
        addBank("CCB", "CCB");
        addBank("BOB", "BCCB");
        addBank("COMM", "BOCO");
        addBank("CMBC", "CMBC");
        addBank("SPA", "PINGANBANK");
        addBank("CIB", "CIB");
        addBank("NJCB", "NJCB");
        addBank("CEB", "CEB");
        addBank("BOC", "BOC");
        addBank("GDB", "CGB");
        addBank("SHB", "SHB");
        addBank("SPDB", "SPDB");
        addBank("PSBC", "POST");
        addBank("CBHB", "CBHB");
        addBank("BEA", "HKBEA");
        addBank("NBCB", "NBCB");
        addBank("CITIC", "ECITIC");
        addBank("BJRCB", "BJRCB");
        addBank("HXB", "HXB");
        addBank("CZB", "CZB");
        addBank("HZB", "HZBANK");
        addBank("GZCB", "GUANGZHOUBANK");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();

        sb.append(getDictSortStr(maps, new String[]{"sign"}));
        sb.append("&paySecret=").append(key);
        System.out.println(sb.toString());
        String sign = MD5.encode(sb.toString(), "UTF-8").toUpperCase();
        System.out.println(sign);
        return sign;
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "sign", "outTradeNo", "trxNo", "tradeStatus");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("payKey", param.getMerchantCode());
        maps.put("orderPrice", param.getAmount());
        maps.put("outTradeNo", param.getOrderId());
        maps.put("productType", "50000103");
        maps.put("orderTime", DateUtils.format(new Date(), "yyyyMMddHHmmss"));
        maps.put("productName", param.getOrderId());
        maps.put("orderIp", param.getIp());
        maps.put("bankCode", param.getBank());
        maps.put("bankAccountType", "PRIVATE_DEBIT_ACCOUNT");
        maps.put("returnUrl", param.getPayReturnUrl());
        maps.put("notifyUrl", param.getPayNoticeUrl());

        return maps;
    }

    public String getResultString(boolean success) {
        if (success) {
            return "SUCCESS";
        }
        return "FAILD";
    }

    protected String getSignName() {
        return "sign";
    }
}
