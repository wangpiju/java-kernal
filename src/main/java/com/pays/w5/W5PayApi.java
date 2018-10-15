package com.pays.w5;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class W5PayApi
        extends PayApi {
    public static final String ID = "w5";
    private static final String TITLE = "W5支付接口";
    private static final String GETWAY = "http://gate.w5pay.com/cooperate/gateway.cgi";
    private static final String VERSION = "1.0.0.0";
    private static final String TRADE_DATE_FORMAT = "yyyyMMdd";
    private static final String RES_CODE_SUCCESS = "1";
    protected static final String APINAME_PAY = "TRADE.B2C";
    protected static final String APINAME_SCANPAY = "TRADE.SCANPAY";
    protected static final String APINAME_NOTIFY = "TRADE.NOTIFY";

    public String getTitle() {
        return "W5支付接口";
    }

    public String getId() {
        return "w5";
    }

    protected void init() {
        addBank("ABC", "ABC");
        addBank("BOC", "BOC");
        addBank("CBHB", "CBHB");
        addBank("CCB", "CCB");
        addBank("CEB", "CEB");
        addBank("CIB", "CIB");
        addBank("CMB", "CMB");
        addBank("CMBC", "CMBC");
        addBank("CITIC", "CNCB");
        addBank("COMM", "COMM");
        addBank("GDB", "GDB");
        addBank("HXB", "HXB");
        addBank("ICBC", "ICBC");
        addBank("SPA", "PAB");
        addBank("PSBC", "PSBC");
        addBank("SPDB", "SPDB");
    }

    protected String getService() {
        return "TRADE.B2C";
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("service", getService());
        maps.put("version", "1.0.0.0");
        maps.put("merId", param.getMerchantCode());
        maps.put("tradeNo", param.getOrderId());
        maps.put("tradeDate", DateUtils.format(new Date(), "yyyyMMdd"));
        maps.put("amount", param.getAmount());
        maps.put("notifyUrl", param.getPayNoticeUrl());
        maps.put("extra", "");
        maps.put("summary", "Recharge" + param.getAmount());
        maps.put("expireTime", "900");
        maps.put("clientIp", param.getIp());
        maps.put("bankId", param.getBank());

        return maps;
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://gate.w5pay.com/cooperate/gateway.cgi";
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        String paramsStr = String.format("service=%s&version=%s&merId=%s&tradeNo=%s&tradeDate=%s&amount=%s&notifyUrl=%s&extra=%s&summary=%s&expireTime=%s&clientIp=%s&bankId=%s", new Object[]{maps.get("service"),
                maps.get("version"), maps.get("merId"), maps.get("tradeNo"), maps.get("tradeDate"), maps.get("amount"), maps.get("notifyUrl"), maps.get("extra"), maps.get("summary"),
                maps.get("expireTime") == null ? "" : (String) maps.get("expireTime"), maps.get("clientIp") == null ? "" : (String) maps.get("clientIp"), maps.get("bankId") == null ? "" : (String) maps.get("bankId")});
        return MD5.encode(paramsStr + key, "utf-8").toUpperCase();
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        maps.put("sign", ((String) maps.get("sign")).toUpperCase());
        NoticeResult noticeResult = verify(maps, apiParam.getKey(), "sign", "tradeNo", "opeNo", "status");
        if ((noticeResult.isStatus()) &&
                ("0".equals(maps.get("notifyType")))) {
            noticeResult.setNotifyType(1);
        }
        return noticeResult;
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        String merId = (String) maps.get("merId");
        String tradeNo = (String) maps.get("tradeNo");
        String tradeDate = (String) maps.get("tradeDate");
        String opeNo = (String) maps.get("opeNo");
        String opeDate = (String) maps.get("opeDate");
        String amount = (String) maps.get("amount");
        String status = (String) maps.get("status");
        String extra = (String) maps.get("extra");
        String payTime = (String) maps.get("payTime");

        String srcMsg = String.format("service=%s&merId=%s&tradeNo=%s&tradeDate=%s&opeNo=%s&opeDate=%s&amount=%s&status=%s&extra=%s&payTime=%s", new Object[]{"TRADE.NOTIFY", merId, tradeNo, tradeDate, opeNo,
                opeDate, amount, status, extra, payTime});

        return MD5.encode(srcMsg + key, "utf-8").toUpperCase();
    }

    protected String getSuccessCode() {
        return "1";
    }

    public String getResultString(boolean success) {
        return success ? "SUCCESS" : "FAILED";
    }
}
