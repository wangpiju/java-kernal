package com.pays.open;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.DES;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class OpenPayApi
        extends PayApi {
    private static final String GATEWAY = "http://pc.api.open2pay.com:23000/ToService.aspx";
    public static final String ID = "open";
    private static final String TITLE = "Open2pay支付接口";

    protected String getRemoteUrl(PayApiParam param) {
        return "http://pc.api.open2pay.com:23000/ToService.aspx";
    }

    public String getTitle() {
        return "Open2pay支付接口";
    }

    public String getId() {
        return "open";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXIN");
        addBankCredit("ALIPAY", "ALIPAY");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        sb.append("<message>");
        sb.append("<cmd>").append("6006").append("</cmd>");
        sb.append("<merchantid>").append(param.getMerchantCode()).append("</merchantid>");
        sb.append("<language>").append("zh-cn").append("</language>");
        sb.append("<userinfo>");
        sb.append("<order>").append(param.getOrderId()).append("</order>");
        sb.append("<username>").append("Recharge" + param.getAmount()).append("</username>");
        sb.append("<money>").append(param.getAmount()).append("</money>");
        sb.append("<unit>").append("1").append("</unit>");
        sb.append("<time>").append(DateUtils.format(new Date())).append("</time>");
        sb.append("<remark>").append("Recharge" + param.getAmount()).append("</remark>");
        sb.append("<backurl>").append(param.getPayNoticeUrl()).append("</backurl>");
        sb.append("<backurlbrowser>").append(param.getPayReturnUrl()).append("</backurlbrowser>");
        sb.append("</userinfo>");
        sb.append("</message>");

        String md5Encrypt = MD5.encode(sb.toString() + param.getKey(), "utf-8");


        //jd-gui
        //String randomValue = new Random().nextInt(10000);
        String randomValue = (new StringBuilder(String.valueOf((new Random()).nextInt(10000)))).toString();
        String dataToEncrypt = sb.toString() + md5Encrypt + MD5.encode(randomValue, "utf-8");
        String desData = DES.encryptByDecode(dataToEncrypt, param.getEmail(), param.getPublicKey());

        Map<String, String> map = new HashMap();
        map.put("pid", param.getMerchantCode());
        map.put("des", desData);
        return map;
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult noticeResult = new NoticeResult();
        try {
            String desData = DES.decryptByDecode((String) maps.get("res"), apiParam.getEmail(), apiParam.getPublicKey());

            desData = desData.substring(0, desData.length() - 32);

            String xmlData = desData.substring(0, desData.length() - 32);

            String md5Data = desData.substring(desData.length() - 32);

            String md5DataVerify = MD5.encode(xmlData + apiParam.getKey(), "utf-8");

            Document doc = DocumentHelper.parseText(xmlData);
            Element message = doc.getRootElement();
            String order = message.elementText("order");
            String username = message.elementText("username");
            String result = message.elementText("result");
            String call = message.elementText("call");
            String merchantid = message.elementText("merchantid");

            maps.put("order", order);
            maps.put("merchantid", merchantid);
            maps.put("username", username);
            if (!md5DataVerify.equals(md5Data)) {
                return noticeResult;
            }
            if (("server".equals(call)) && ("1".equals(result)) && (apiParam.getMerchantCode().equals(merchantid))) {
                noticeResult.setStatus(true);
                noticeResult.setApiOrderId(order);
                noticeResult.setOrderId(order);
            }
        } catch (Exception localException) {
        }
        return noticeResult;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return null;
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        super.appendMapValue(html, maps);
    }

    public String getResultString(boolean success) {
        return null;
    }

    public String getResultString(boolean success, Map<String, String> params) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        sb.append("<message>");
        sb.append("<cmd>").append("60071").append("</cmd>");
        sb.append("<merchantid>").append((String) params.get("merchantid")).append("</merchantid>");
        sb.append("<order>").append((String) params.get("order")).append("</order>");
        sb.append("<username>").append((String) params.get("username")).append("</username>");
        sb.append("<result>").append(success ? 100 : 103).append("</result>");
        sb.append("</message>");
        return sb.toString();
    }
}
