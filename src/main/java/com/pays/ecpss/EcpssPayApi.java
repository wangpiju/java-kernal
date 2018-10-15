package com.pays.ecpss;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EcpssPayApi
        extends PayApi {
    public static final String ID = "ecpss";
    private static final String GATEWAY = "https://gwapi.yemadai.com/pay/sslpayment";
    private static final String TITLE = "汇潮支付接口";

    public String getTitle() {
        return "汇潮支付接口";
    }

    public String getId() {
        return "ecpss";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://gwapi.yemadai.com/pay/sslpayment";
    }

    protected void init() {
        addBank("ICBC", "ICBC");
        addBank("ABC", "ABC");
        addBank("BOC", "BOCSH");
        addBank("CCB", "CCB");
        addBank("CMB", "CMB");
        addBank("SPDB", "SPDB");
        addBank("GDB", "GDB");
        addBank("COMM", "BOCOM");
        addBank("PSBC", "PSBC");
        addBank("CITIC", "CNCB");
        addBank("CMBC", "CMBC");
        addBank("CEB", "CEB");
        addBank("HXB", "HXB");
        addBank("CIB", "CIB");
        addBank("SHB", "BOS");
        addBank("SRCB", "SRCB");
        addBank("SPA", "PAB");
        addBank("BOB", "BCCB");


        addBankCredit("NOCARD", "NOCARD");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("MerNo", param.getMerchantCode());
        maps.put("BillNo", param.getOrderId());
        maps.put("Amount", param.getAmount());
        maps.put("ReturnURL", param.getPayReturnUrl());
        maps.put("AdviceURL", param.getPayNoticeUrl());
        maps.put("OrderTime", DateUtils.format(new Date(), "yyyyMMddHHmmss"));
        maps.put("defaultBankNumber", param.getBank());

        return maps;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append("MerNo=").append((String) maps.get("MerNo")).append("&");
        sb.append("BillNo=").append((String) maps.get("BillNo")).append("&");
        sb.append("Amount=").append((String) maps.get("Amount")).append("&");
        sb.append("OrderTime=").append((String) maps.get("OrderTime")).append("&");
        sb.append("ReturnURL=").append((String) maps.get("ReturnURL")).append("&");
        sb.append("AdviceURL=").append((String) maps.get("AdviceURL")).append("&");
        sb.append(key);
        return MD5.encode(sb.toString(), "utf-8").toUpperCase();
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append("MerNo=").append((String) maps.get("MerNo")).append("&");
        sb.append("BillNo=").append((String) maps.get("BillNo")).append("&");
        sb.append("OrderNo=").append((String) maps.get("OrderNo")).append("&");
        sb.append("Amount=").append((String) maps.get("Amount")).append("&");
        sb.append("Succeed=").append((String) maps.get("Succeed")).append("&");
        sb.append(key);
        return MD5.encode(sb.toString(), "utf-8").toUpperCase();
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return super.verify(maps, apiParam.getKey(), "SignInfo", "BillNo", "OrderNo", "Succeed");
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        super.appendMapValue(html, maps);
        html.append(createInput("SignInfo", sign));
    }

    public String getResultString(boolean success) {
        return success ? "ok" : "fail";
    }

    protected String getSuccessCode() {
        return "88";
    }
}
