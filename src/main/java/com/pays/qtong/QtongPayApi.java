package com.pays.qtong;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.Base64;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;
import com.pays.RSA;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class QtongPayApi
        extends PayApi {
    private static final String GATEWAY = "https://www.qtongpay.com/pay/pay.htm";
    public static final String ID = "qtong";
    private static final String TITLE = "钱通网银接口";

    protected String getRemoteUrl(PayApiParam param) {
        return "https://www.qtongpay.com/pay/pay.htm";
    }

    public String getTitle() {
        return "钱通网银接口";
    }

    public String getId() {
        return "qtong";
    }

    protected void init() {
        addBank("ICBC", "ICBC");
        addBank("BOC", "BOC");
        addBank("ABC", "ABC");
        addBank("CCB", "CCB");
        addBank("BEA", "BEAI");
        addBank("BJRCB", "BRCB");
        addBank("BOB", "BCCB");
        addBank("CBHB", "BOHC");
        addBank("CEB", "CEB");
        addBank("CIB", "CIB");
        addBank("CITIC", "CNCB");
        addBank("CMB", "CMB");
        addBank("CMBC", "CMBC");
        addBank("COMM", "BOCOM");
        addBank("CZB", "ZSBC");
        addBank("GDB", "GDB");
        addBank("GZCB", "GZCB");
        addBank("HSB", "HSBANK");
        addBank("HXB", "HXB");
        addBank("NBCB", "NBBC");
        addBank("NJCB", "NJBC");
        addBank("PSBC", "PSBC");
        addBank("SHB", "BOS");
        addBank("SPA", "PAB");
        addBank("SPDB", "SPDB");
        addBank("SRCB", "SHRCB");

        addBankCredit("RMBJJK", "");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        //jd-gui
        //String amount = new BigDecimal(param.getAmount()).multiply(new BigDecimal("100")).intValue();
        String amount = (new StringBuilder(String.valueOf((new BigDecimal(param.getAmount())).multiply(new BigDecimal("100")).intValue()))).toString();

        Map<String, String> map = new HashMap();
        map.put("application", "SubmitOrder");
        map.put("version", "1.0.1");
        map.put("merchantId", param.getMerchantCode());
        map.put("merchantOrderId", param.getOrderId());
        map.put("merchantOrderAmt", amount);
        map.put("merchantPayNotifyUrl", param.getPayNoticeUrl());
        map.put("merchantFrontEndUrl", param.getPayReturnUrl());
        map.put("accountType", "0");
        map.put("bankId", param.getBank());
        map.put("orderTime", DateUtils.format(new Date(), "yyyyMMddHHmmss"));
        map.put("rptType", "1");
        map.put("payMode", "0");
        return map;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>");
        sb.append("<message");
        for (Map.Entry<String, String> m : maps.entrySet()) {
            sb.append(" ").append((String) m.getKey()).append("='").append((String) m.getValue()).append("'");
        }
        sb.append("/>");

        String xml = sb.toString();
        try {
            String xmlBASE64 = Base64.encode(xml) + "|";
            byte[] xmlMD5 = MD5.getDigest(xml.getBytes("utf-8"));
            return xmlBASE64 + RSA.sign(xmlMD5, key);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        html.append(createInput("msg", sign));

        html.append(createInput("orderNo", (String) maps.get("merchantOrderId")));
        html.append(createInput("amount", (String) maps.get("merchantOrderAmt")));
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult noticeResult = new NoticeResult();
        try {
            String s = (String) maps.get("msg");
            String[] list = s.split("\\|");
            String xml = Base64.decode(list[0]);
            byte[] md5 = MD5.getDigest(xml.getBytes("utf-8"));
            byte[] signature = Base64.decode(list[1].getBytes("utf-8"));
            if (RSA.verify(md5, signature, apiParam.getPublicKey())) {
                Element message = DocumentHelper.parseText(xml).getRootElement();


                Element item = message.element("deductList").element("item");

                String status = item.attribute("payStatus").getValue();
                if ("01".equals(status)) {
                    String orderId = message.attribute("merchantOrderId").getValue();
                    String apiOrderId = item.attribute("payOrderId").getValue();

                    noticeResult.setStatus(true);
                    noticeResult.setApiOrderId(apiOrderId);
                    noticeResult.setOrderId(orderId);
                }
            }
        } catch (Exception localException) {
        }
        return noticeResult;
    }

    public String getResultString(boolean success) {
        return "1";
    }
}
