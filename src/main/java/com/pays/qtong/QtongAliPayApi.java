package com.pays.qtong;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.Base64;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApiParam;
import com.pays.RSA;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class QtongAliPayApi
        extends QtongPayApi {
    public static final String ID = "qtongAli";
    private static final String TITLE = "钱通支付宝接口";

    public String getTitle() {
        return "钱通支付宝接口";
    }

    public String getId() {
        return "qtongAli";
    }

    protected void init() {
        addBank("ALIPAY", "ALIPAY");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        //jd-gui
        //String amount = new BigDecimal(param.getAmount()).multiply(new BigDecimal("100")).intValue();
        String amount = (new StringBuilder(String.valueOf((new BigDecimal(param.getAmount())).multiply(new BigDecimal("100")).intValue()))).toString();

        Map<String, String> map = new HashMap();
        map.put("application", "ZFBScanOrder");
        map.put("version", "1.0.1");
        map.put("timestamp", DateUtils.format(new Date(), "yyyyMMddHHmmss"));
        map.put("merchantId", param.getMerchantCode());
        map.put("merchantOrderId", param.getOrderId());
        map.put("merchantOrderAmt", amount);
        map.put("merchantOrderDesc", param.getOrderId());
        map.put("userName", param.getOrderId());
        map.put("merchantPayNotifyUrl", param.getPayNoticeUrl());
        return map;
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
                String status = message.attributeValue("respCode");
                if ("000".equals(status)) {
                    String orderId = message.attributeValue("merchantOrderId");
                    noticeResult.setStatus(true);
                    noticeResult.setApiOrderId(orderId);
                    noticeResult.setOrderId(orderId);
                }
            }
        } catch (Exception localException) {
        }
        return noticeResult;
    }
}
