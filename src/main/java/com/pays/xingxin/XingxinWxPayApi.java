package com.pays.xingxin;

import com.pays.ApiParam;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;
import com.pays.RSA;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class XingxinWxPayApi
        extends PayApi {
    public static final String ID = "xingxinWx";
    private static final String GATEWAY = "http://47.93.233.232:28080/Pay/trans/payOrder";
    private static final String TITLE = "兴信微信扫码支付接口";

    public String getTitle() {
        return "兴信微信扫码支付接口";
    }

    public String getId() {
        return "xingxinWx";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://47.93.233.232:28080/Pay/trans/payOrder";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXIN");
        addBankCredit("WEIXIN", "WEIXIN");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return RSA.sign((String) maps.get("TRANDATA"), key, "");
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult rel = new NoticeResult();

        StringBuilder sb = new StringBuilder();
        sb.append("ORDERNO=").append((String) maps.get("ORDERNO"));
        sb.append("&TXNAMT=").append((String) maps.get("TXNAMT"));
        sb.append("&ORDSTATUS=").append((String) maps.get("ORDSTATUS"));
        if (!RSA.verify(sb.toString(), (String) maps.get("SIGN"), apiParam.getPublicKey(), "")) {
            return rel;
        }
        String outOrderId = (String) maps.get("ORDERNO");
        String instructCode = (String) maps.get("PAYORDNO");
        String status = (String) maps.get("ORDSTATUS");
        String recode = (String) maps.get("RECODE");
        if (("000000".equals(recode)) && (getSuccessCode().equals(status))) {
            rel.setStatus(true);
        } else {
            rel.setStatus(false);
        }
        rel.setOrderId(outOrderId);
        rel.setApiOrderId(instructCode);
        return rel;
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("MERCNUM", param.getMerchantCode());

        //jd-gui
        //String amount = new BigDecimal(param.getAmount()).multiply(new BigDecimal("100")).intValue();
        String amount = (new StringBuilder(String.valueOf((new BigDecimal(param.getAmount())).multiply(new BigDecimal("100")).intValue()))).toString();

        StringBuilder sb = new StringBuilder();
        sb.append("ORDERNO=").append(param.getOrderId());
        sb.append("&TXNAMT=").append(amount);
        sb.append("&CHANNELCODE=").append(param.getBank());
        sb.append("&REMARK=").append(param.getOrderId());
        sb.append("&RETURNURL=").append(param.getPayNoticeUrl());

        maps.put("TRANDATA", sb.toString());

        return maps;
    }

    protected String getSuccessCode() {
        return "01";
    }

    public String getResultString(boolean success) {
        if (success) {
            return "SUCCESS";
        }
        return "FAIL";
    }

    protected String getSignName() {
        return "SIGN";
    }
}
