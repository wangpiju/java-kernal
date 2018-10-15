package com.pays.go;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GoPayApi
        extends PayApi {
    public static final String ID = "go";
    private static final String GATEWAY = "https://gateway.gopay.com.cn/Trans/WebClientAction.do";
    private static final String TITLE = "国付宝网关支付接口";
    private static final String RES_CODE_SUCCESS = "0000";
    private static final String VERSION_2_1 = "2.1";
    private static final String CHARSET_UTF8 = "2";
    private static final String LANGUAGE_CHINESE = "1";
    private static final String SIGN_TYPE_MD5 = "1";
    private static final String TRAN_CODE_8888 = "8888";
    private static final String CURRENCY_TYPE_RMB = "156";
    private static final String IS_REPEAT_SUBMIT_Y = "1";
    private static final String USER_TYPE_PERSONAL = "1";

    public String getTitle() {
        return "国付宝网关支付接口";
    }

    public String getId() {
        return "go";
    }

    protected void init() {
        addBank("CCB", "CCB");
        addBank("CMB", "CMB");
        addBank("ICBC", "ICBC");
        addBank("BOC", "BOC");
        addBank("ABC", "ABC");
        addBank("COMM", "BOCOM");
        addBank("CMBC", "CMBC");
        addBank("HXB", "HXBC");
        addBank("CIB", "CIB");
        addBank("SPDB", "SPDB");
        addBank("CITIC", "CITIC");
        addBank("CEB", "CEB");
        addBank("PSBC", "PSBC");
        addBank("BOB", "BOB");
        addBank("SHB", "BOS");
        addBank("SPA", "PAB");
        addBank("NBCB", "NBCB");
        addBank("NJCB", "NJCB");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        Date now = new Date();
        String time = DateUtils.format(now, "yyyyMMddHHmmss");

        maps.put("version", "2.1");
        maps.put("charset", "2");
        maps.put("language", "1");
        maps.put("signType", "1");
        maps.put("tranCode", "8888");

        maps.put("merchantID", param.getMerchantCode());
        maps.put("merOrderNum", param.getOrderId());
        maps.put("tranAmt", param.getAmount());
        maps.put("feeAmt", "");
        maps.put("currencyType", "156");
        maps.put("frontMerUrl", param.getPayReturnUrl());
        maps.put("backgroundMerUrl", param.getPayNoticeUrl());
        maps.put("tranDateTime", time);
        maps.put("virCardNoIn", param.getEmail());
        maps.put("tranIP", param.getIp());
        maps.put("isRepeatSubmit", "1");
        maps.put("gopayServerTime", getGopayServerTime());
        maps.put("bankCode", param.getBank());
        maps.put("userType", "1");

        return maps;
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://gateway.gopay.com.cn/Trans/WebClientAction.do";
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder plain = new StringBuilder();
        plain.append(getMapString(maps, "version"));
        plain.append(getMapString(maps, "tranCode"));
        plain.append(getMapString(maps, "merchantID"));
        plain.append(getMapString(maps, "merOrderNum"));
        plain.append(getMapString(maps, "tranAmt"));
        plain.append(getMapString(maps, "feeAmt"));
        plain.append(getMapString(maps, "tranDateTime"));
        plain.append(getMapString(maps, "frontMerUrl"));
        plain.append(getMapString(maps, "backgroundMerUrl"));
        plain.append(getMapString("", "orderId"));
        plain.append(getMapString("", "gopayOutOrderId"));
        plain.append(getMapString(maps, "tranIP"));
        plain.append(getMapString("", "respCode"));
        plain.append(getMapString(maps, "gopayServerTime"));
        plain.append(getMapString(key, "VerficationCode"));
        return MD5.encode(plain.toString(), "utf-8");
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        StringBuilder plain = new StringBuilder();
        plain.append(getMapString(maps, "version"));
        plain.append(getMapString(maps, "tranCode"));
        plain.append(getMapString(maps, "merchantID"));
        plain.append(getMapString(maps, "merOrderNum"));
        plain.append(getMapString(maps, "tranAmt"));
        plain.append(getMapString(maps, "feeAmt"));
        plain.append(getMapString(maps, "tranDateTime"));
        plain.append(getMapString(maps, "frontMerUrl"));
        plain.append(getMapString(maps, "backgroundMerUrl"));
        plain.append(getMapString(maps, "orderId"));
        plain.append(getMapString(maps, "gopayOutOrderId"));
        plain.append(getMapString(maps, "tranIP"));
        plain.append(getMapString(maps, "respCode"));
        plain.append(getMapString(maps, "gopayServerTime"));
        plain.append(getMapString(key, "VerficationCode"));
        return MD5.encode(plain.toString(), "utf-8");
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verify(maps, apiParam.getKey(), "signValue", "merOrderNum", "orderId", "respCode");
    }

    public String getResultString(boolean success) {
        return success ? "RespCode=0000|JumpURL=" : "RespCode=9999|JumpURL=";
    }

    protected String getSuccessCode() {
        return "0000";
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        super.appendMapValue(html, maps);
        html.append(createInput("signValue", sign));
    }

    private String getMapString(Map<String, String> maps, String key) {
        return getMapString((String) maps.get(key), key);
    }

    private String getMapString(String value, String key) {
        return key + "=[" + (value == null ? "" : value) + "]";
    }

    private String getGopayServerTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
}
