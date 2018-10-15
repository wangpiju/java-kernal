package com.pays.kkl;

import com.hs3.utils.StrUtils;
import com.pays.ApiParam;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Map;

public class KklPayApi
        extends PayApi {
    public static final String ID = "kkl";
    private static final String WAYGATE = "https://spayment.kklpay.com/onlinebank/createOrder.do";
    private static final String TITLE = "中联网支付接口";
    private static final String[] PAY_SIGN_FIELDS = {"merchantCode", "merchantOrderTime", "notifyUrl", "outOrderId", "outUserId", "totalAmount"};
    private static final String[] NOTICE_SIGN_FIELDS = {"instructCode", "merchantCode", "outOrderId", "totalAmount", "transTime", "transType"};

    protected void init() {
        addBank("ABC", "ABC");
        addBank("BOC", "BOC");
        addBank("BCCB", "BCCB");
        addBank("BCM", "BCM");
        addBank("BOS", "BOS");
        addBank("CCB", "CCB");
        addBank("CEB", "CEB");
        addBank("CIB", "CIB");
        addBank("CITIC", "CITIC");
        addBank("CMB", "CMB");
        addBank("CMBC", "CMBC");
        addBank("GDB", "GDB");
        addBank("HXB", "HXB");
        addBank("ICBC", "ICBC");
        addBank("PAB", "PAB");
        addBank("PSBC", "PSBC");
        addBank("SPDB", "SPDB");
    }

    public String getTitle() {
        return "中联网支付接口";
    }

    public String getId() {
        return "kkl";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://spayment.kklpay.com/onlinebank/createOrder.do";
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        for (String f : PAY_SIGN_FIELDS) {
            String v = (String) maps.get(f);
            if (StrUtils.hasEmpty(new Object[]{v})) {
                v = "";
            }
            sb.append(f).append("=").append(v).append("&");
        }
        sb.append("KEY=").append(key);
        return StrUtils.MD5(sb.toString());
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        for (String f : NOTICE_SIGN_FIELDS) {
            String v = (String) maps.get(f);
            if (StrUtils.hasEmpty(new Object[]{v})) {
                v = "";
            }
            sb.append(f).append("=").append(v).append("&");
        }
        sb.append("KEY=").append(key);
        return StrUtils.MD5(sb.toString());
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult rel = new NoticeResult();

        String signKey = createNoticeSign(maps, apiParam.getKey());

        String sign = (String) maps.get("sign");
        if (!signKey.equals(sign)) {
            return rel;
        }
        rel.setStatus(true);
        rel.setOrderId((String) maps.get("outOrderId"));
        rel.setApiOrderId((String) maps.get("instructCode"));
        return rel;
    }

    public String getResultString(boolean success) {
        if (success) {
            return "{\"code\":\"00\"}";
        }
        return "{\"code\":\"99\"}";
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        return null;
    }
}
