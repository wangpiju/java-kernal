package com.pays.mobao;

import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;
import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MobaoPayApi
        extends PayApi {
    public static final String ID = "mobao";
    private static final String TITLE = "摩宝支付接口";
    private static final String MOBAOPAY_GETWAY = "http://saascashier.mobaopay.com/cgi-bin/netpayment/pay_gate.cgi";
    private static final String API_NAME_WEB = "WEB_PAY_B2C";
    private static final String API_VERSION = "1.0.0.0";
    private static final String TRADE_DATE_FORMAT = "yyyyMMdd";
    private static final String RES_CODE_SUCCESS = "1";

    public String getTitle() {
        return "摩宝支付接口";
    }

    public String getId() {
        return "mobao";
    }

    protected void init() {
        addBank("ICBC", "ICBC");
        addBank("ABC", "ABC");
        addBank("BOC", "BOC");
        addBank("CCB", "CCB");
        addBank("COMM", "COMM");
        addBank("CMB", "CMB");
        addBank("SPDB", "SPDB");
        addBank("CIB", "CIB");
        addBank("CMBC", "CMBC");
        addBank("GDB", "GDB");
        addBank("CITIC", "CNCB");
        addBank("CEB", "CEB");
        addBank("HXB", "HXB");
        addBank("PSBC", "PSBC");
        addBank("SPA", "PAB");

        addBankCredit("RMBJJK", "RMBJJK");
        addBankCredit("WEIXIN", "WEIXIN");
        addBankCredit("ALIPAY", "ZHIFUBAO");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("apiName", "WEB_PAY_B2C");
        maps.put("apiVersion", "1.0.0.0");
        maps.put("platformID", param.getEmail());
        maps.put("merchNo", param.getMerchantCode());
        maps.put("orderNo", param.getOrderId());
        maps.put("tradeDate", DateUtils.format(new Date(), "yyyyMMdd"));
        maps.put("amt", param.getAmount());
        maps.put("merchUrl", param.getPayNoticeUrl());
        maps.put("merchParam", "Recharge" + param.getAmount());
        maps.put("tradeSummary", "Recharge" + param.getAmount());
        if ("ZHIFUBAO".equals(param.getBank())) {
            maps.put("choosePayType", "4");
        } else if ("WEIXIN".equals(param.getBank())) {
            maps.put("choosePayType", "5");
        } else if ("RMBJJK".equals(param.getBank())) {
            maps.put("choosePayType", "1");
        } else {
            maps.put("bankCode", param.getBank());
        }
        return maps;
    }

    protected String getRemoteUrl(PayApiParam param) {
        return StrUtils.hasEmpty(new Object[]{param.getPublicKey()}) ? "http://saascashier.mobaopay.com/cgi-bin/netpayment/pay_gate.cgi" : param.getPublicKey();
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        String paramsStr = String.format("apiName=%s&apiVersion=%s&platformID=%s&merchNo=%s&orderNo=%s&tradeDate=%s&amt=%s&merchUrl=%s&merchParam=%s&tradeSummary=%s", new Object[]{maps.get("apiName"),
                maps.get("apiVersion"), maps.get("platformID"), maps.get("merchNo"), maps.get("orderNo"), maps.get("tradeDate"), maps.get("amt"), maps.get("merchUrl"), maps.get("merchParam"),
                maps.get("tradeSummary")});
        return MD5.encode(paramsStr + key, "utf-8").toUpperCase();
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        appendMapValue(html, maps);
        html.append(createInput("signMsg", sign));
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        maps.put("signMsg", ((String) maps.get("signMsg")).toUpperCase());
        NoticeResult noticeResult = verify(maps, apiParam.getKey(), "signMsg", "orderNo", "accNo", "orderStatus");
        if ((noticeResult.isStatus()) &&
                ("0".equals(maps.get("notifyType")))) {
            noticeResult.setNotifyType(1);
        }
        return noticeResult;
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        String paramsStr = String.format("apiName=%s&notifyTime=%s&tradeAmt=%s&merchNo=%s&merchParam=%s&orderNo=%s&tradeDate=%s&accNo=%s&accDate=%s&orderStatus=%s", new Object[]{maps.get("apiName"),
                maps.get("notifyTime"), maps.get("tradeAmt"), maps.get("merchNo"), maps.get("merchParam"), maps.get("orderNo"), maps.get("tradeDate"), maps.get("accNo"), maps.get("accDate"),
                maps.get("orderStatus")});
        return MD5.encode(paramsStr + key, "utf-8").toUpperCase();
    }

    protected String getSuccessCode() {
        return "1";
    }

    public String getResultString(boolean success) {
        return success ? "SUCCESS" : "FAILED";
    }
}
