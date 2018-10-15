package com.pays.fun;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FunPayApi
        extends PayApi {
    public static final String ID = "fun";
    private static final String GATEWAY = "https://www.funpay.com/website/pay.htm";
    private static final String TITLE = "乐盈支付接口";
    private static final String VERSION = "1.0";
    private static final String TYPE = "1000";

    public String getTitle() {
        return "乐盈支付接口";
    }

    public String getId() {
        return "fun";
    }

    protected void init() {
        addBank("ICBC", "icbc");
        addBank("ABC", "abc");
        addBank("CCB", "ccb");
        addBank("BOC", "boc");
        addBank("COMM", "comm");
        addBank("CMB", "cmb");
        addBank("CMBC", "cmbc");
        addBank("CIB", "cib");
        addBank("SPDB", "spdb");
        addBank("HXB", "hxb");
        addBank("CITIC", "ecitic");
        addBank("CEB", "ceb");
        addBank("GDB", "gdb");
        addBank("PSBC", "post");
        addBank("SDB", "sdb");
        addBank("BEA", "bea");
        addBank("NBCB", "nb");
        addBank("BOB", "bccb");
        addBank("SPA", "pingan");

        addBankCredit("RMBJJK", "");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new LinkedHashMap();

        Date now = new Date();
        String time = DateUtils.format(now, "yyyyMMddHHmmss");

        //jd-gui
        //String amount = new BigDecimal(param.getAmount()).multiply(new BigDecimal("100")).intValue();
        String amount = (new StringBuilder(String.valueOf((new BigDecimal(param.getAmount())).multiply(new BigDecimal("100")).intValue()))).toString();

        maps.put("version", "1.0");
        maps.put("serialID", "hs" + time);
        maps.put("submitTime", time);
        maps.put("failureTime", "");
        maps.put("customerIP", "");
        maps.put("orderDetails", param.getOrderId() + "," + amount + ",--," + "Recharge" + amount + ",1");
        maps.put("totalAmount", amount);
        maps.put("type", "1000");
        maps.put("buyerMarked", "");
        maps.put("payType", param.getIsCredit().booleanValue() ? "BANK_B2C" : "ALL");
        maps.put("orgCode", param.getIsCredit().booleanValue() ? param.getBank() : "");
        maps.put("currencyCode", "1");
        maps.put("directFlag", param.getIsCredit().booleanValue() ? "1" : "0");
        maps.put("borrowingMarked", "");
        maps.put("couponFlag", "0");
        maps.put("platformID", "");
        maps.put("returnUrl", param.getPayReturnUrl());
        maps.put("noticeUrl", param.getPayNoticeUrl());
        maps.put("partnerID", param.getMerchantCode());
        maps.put("remark", "remark");
        maps.put("charset", "1");
        maps.put("signType", "2");

        return maps;
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        appendMapValue(html, maps);
        html.append(createInput("signMsg", sign));
        html.append(createInput("orderID", param.getOrderId()));
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://www.funpay.com/website/pay.htm";
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        String str = "";
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            str = str + (String) entry.getKey() + "=" + (String) entry.getValue() + "&";
        }
        str = str + "pkey=" + key;
        return MD5.encode(str, "utf-8");
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        String pattern = "orderID={0}&resultCode={1}&stateCode={2}&orderAmount={3}&payAmount={4}&acquiringTime={5}&completeTime={6}&orderNo={7}&partnerID={8}&remark={9}&charset={10}&signType={11}";
        String str = MessageFormat.format(pattern, new Object[]{maps.get("orderID"), maps.get("resultCode"), maps.get("stateCode"), maps.get("orderAmount"), maps.get("payAmount"),
                maps.get("acquiringTime"), maps.get("completeTime"), maps.get("orderNo"), maps.get("partnerID"), maps.get("remark"), maps.get("charset"), maps.get("signType")});

        str = str + "&pkey=" + key;

        return MD5.encode(str, "utf-8");
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "signMsg", "orderID", "orderNo", "stateCode");
    }

    protected String getSuccessCode() {
        return "2";
    }

    public String getResultString(boolean success) {
        return success ? "200" : "404";
    }
}
