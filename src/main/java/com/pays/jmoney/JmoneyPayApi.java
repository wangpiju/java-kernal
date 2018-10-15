package com.pays.jmoney;

import com.hs3.entity.finance.Recharge;
import com.hs3.utils.DateUtils;
import com.hs3.utils.HttpUtils;
import com.pays.ApiParam;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;
import com.pays.common.IThirdPayCommon;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author jason
 * 20180722
 */
public class JmoneyPayApi extends PayApi implements IThirdPayCommon {
    public static final String ID = "jmoney";
    private static final Logger logger = LoggerFactory.getLogger(JmoneyPayApi.class);
    private static final String INPUT_CHARSET = "UTF-8";
    private static final String GATEWAY = "https://gateway.jmoney.cc/GateWay/Index";
    private static final String TITLE = "Jmoney支付接口";
    private static final String CHECKURL = "https://gateway.jmoney.cc/GateWay/search";
    private static final ObjectMapper mapper = new ObjectMapper();

    private static String getInputCharset() {
        return INPUT_CHARSET;
    }

    public static void main(String[] args) {
//        System.out.println("天杀得");
        System.out.println(sign("customer=88997770&banktype=967&amount=20&orderid=dhR180723113558963&asynbackurl=https://gateway.jmoney.cc/GateWay/Index&request_time=20180723120001&key=1316fa0e3e3d48f1ab7cd8328f07a216", "UTF-8"));
        System.out.println(sign("orderid=dhR180805144030018&result=1&amount=150.00&systemorderid=ABC&completetime=20180805135223&key=1316fa0e3e3d48f1ab7cd8328f07a216", "UTF-8"));
    }

    public ObjectNode getCheckResult(Recharge recharge, String url){
        ObjectNode objectNode = mapper.createObjectNode();
        try{
            String xml = HttpUtils.getString(url);
            JsonNode jsonNode = mapper.readTree(xml);
            if(null != jsonNode.get("obj")){
                String sign = jsonNode.get("obj").get("sign").asText();

                String orderid = jsonNode.get("obj").get("orderid").asText();
                String result = jsonNode.get("obj").get("result").asText();
                String amount = jsonNode.get("obj").get("amount").asText();
                String systemorderid = jsonNode.get("obj").get("systemorderid").asText();
                String completetime = jsonNode.get("obj").get("completetime").asText();


                String beforeSign = "orderid=" + orderid + "&result=" + result + "&amount=" + amount + "&systemorderid=" + systemorderid +
                        "&completetime=" + completetime + "&key=1316fa0e3e3d48f1ab7cd8328f07a216";

                String systemSign =  sign(beforeSign, getInputCharset());
                //驗簽
                if(!sign.equals(systemSign)){
                    objectNode.put("result", "2");
                    objectNode.put("message","驗簽失败");
                }else {
                    if(null != jsonNode.get("obj").get("result") ){
                        if( jsonNode.get("obj").get("result").asText().equals("0")){
                            objectNode.put("result", "0");
                            objectNode.put("message","未支付");
                        }
                        else if( jsonNode.get("obj").get("result").asText().equals("1")){
                            objectNode.put("result", "1");
                            objectNode.put("message","支付成功");
                        }
                        else if( jsonNode.get("obj").get("result").asText().equals("2")){
                            objectNode.put("result", "2");
                            objectNode.put("message","失败");
                        }
                    }
                    else{
                        objectNode.put("result", "2");
                        objectNode.put("message","失败");
                    }
                }
            }
            else{
                objectNode.put("result", "2");
                objectNode.put("message","失敗");
            }
        }
        catch (Exception e){
            logger.error(e.getMessage(), e);
        }


        return objectNode;
    }

    public static String sign(String text, String charset) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes(charset));
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTitle() {
        return TITLE;
    }

    public String getId() {
        return ID;
    }

//    private String getInputCharset() {
//        return INPUT_CHARSET;
//    }

    private String getBankByValue(String value) {

        for (Map.Entry entry : this.banks.entrySet()) {
            if (entry.getValue().toString().equals(value)) {
                return entry.getKey().toString();
            }
        }

        for (Map.Entry entry : this.banksCredit.entrySet()) {
            if (entry.getValue().toString().equals(value)) {
                return entry.getKey().toString();
            }
        }

        return "";
    }

    protected void init() {
        addBankCredit("1004", "支付宝直连-手机端");
        //addBankCredit("1003", "支付宝扫码");
        addBankCredit("1005", "QQ钱包扫码");
        addBankCredit("963", "中国银行");
        addBankCredit("964", "中国农业银行");
        addBankCredit("965", "中国建设银行");
        addBankCredit("967", "中国工商银行");

//        addBank("1003", "支付宝扫码");
//        addBank("1004", "支付宝直连-手机端");
        addBank("1005", "QQ钱包扫码");
        addBank("962", "中信银行");
        addBank("963", "中国银行");
        addBank("964", "中国农业银行");
        addBank("965", "中国建设银行");
        addBank("967", "中国工商银行");
        addBank("970", "招商银行");
        addBank("971", "邮政储蓄");
//        addBank("972", "兴业银行");
//        addBank("976", "上海农村商业银行");
//        addBank("977", "浦东发展银行");
//        addBank("979", "南京银行");
        addBank("980", "民生银行");
        addBank("981", "交通银行");
//        addBank("983", "杭州银行");
//        addBank("985", "广东发展银行");
        addBank("986", "光大银行");
//        addBank("987", "东亚银行");
//        addBank("989", "北京银行");
//        工行  农行  建行  中国银行  中兴  民生  招行  交通  邮政储蓄   光大  广发
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap<>();

        maps.put("customer", param.getMerchantCode());
        maps.put("banktype", param.getBank());
        maps.put("amount", param.getAmount());
        maps.put("orderid", param.getOrderId());
        maps.put("asynbackurl", param.getShopNoticeUrl());
        maps.put("request_time", DateUtils.formatPayTime(new Date()));
        maps.put("synbackurl", param.getShopReturnUrl());
        maps.put("isqrcode", "N");
        maps.put("israndom", "N");
        maps.put("attach", param.getTraceId());
        maps.put("sign", param.getTraceId());

        return maps;
    }

    protected String getRemoteUrl(PayApiParam param) {
        return GATEWAY;
    }

    private String createSign(Map<String, String> param, String key) {
        String customer = param.get("customer");
        String banktype = param.get("banktype");
        String amount = param.get("amount");
        String orderid = param.get("orderid");
        String asynbackurl = param.get("asynbackurl");
        String request_time = param.get("request_time");

        String beforeSign = "customer=" + customer + "&banktype=" + banktype +
                "&amount=" + amount + "&orderid=" + orderid +
                "&asynbackurl=" + asynbackurl + "&request_time=" + request_time +
                "&key=" + key;

        return sign(beforeSign, getInputCharset());
    }

    protected String createNoticeSign(Map<String, String> param, String key) {

        String orderid = param.get("orderid");
        String result = param.get("result");
        String amount = param.get("amount");
        String systemorderid = param.get("systemorderid");
        String completetime = param.get("completetime");


        String beforeSign = "orderid=" + orderid + "&result=" + result + "&amount=" + amount + "&systemorderid=" + systemorderid +
                "&completetime=" + completetime + "&key=1316fa0e3e3d48f1ab7cd8328f07a216";

        return sign(beforeSign, getInputCharset());
    }

    protected String createPaySign(Map<String, String> param, String key) {
        return createSign(param, key);
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult rel = new NoticeResult();

        String signKey = createNoticeSign(maps, apiParam.getKey());

        String sign = maps.get("sign");

        if (!signKey.equals(sign)) {
            rel.setStatus(false);
            rel.setAmount(new BigDecimal(maps.get("amount")));
            rel.setOrderId(maps.get("orderid"));
            rel.setApiOrderId(maps.get("systemorderid"));
            return rel;
        }

        String result = maps.get("result");

        if (result.equals("1"))
            rel.setStatus(true);
        else
            rel.setStatus(false);

        rel.setAmount(new BigDecimal(maps.get("amount")));
        rel.setOrderId(maps.get("orderid"));
        rel.setApiOrderId(maps.get("systemorderid"));

        return rel;
    }

    public String getResultString(boolean success) {
        if(success)
            return "success";
        else
            return "false";
    }

    public String getResultString(boolean success, Map<String, String> params) {
        // return "{\"company_order_num\":\"" +  params.get("company_order_num") + "\",\"mownecum_order_num\":\"" + params.get("mownecum_order_num") + "\",\"status\":" + (success ? 1 : 0) + ",\"error_msg\":\"" + (success ? "" : "处理失败！") + "\"}";
        if(success)
            return "success";
        else
            return "{\"systemorderid\":\"" +  params.get("systemorderid") + "\",\"orderid\":\"" + params.get("orderid") + "\",\"status\":" + (success ? 1 : 0) + ",\"error_msg\":\"" + (success ? "" : "处理失败！") + "\"}";

    }

    protected String getSignName() {
        return "key";
    }

    public String sendPayReqHtml(PayApiParam param) {
        param.setBank(getBankByValue(param.getBank()));
        Map<String, String> params = getParamMaps(param);
        String sign = createPaySign(params, param.getKey()).toLowerCase();
        params.put("sign", sign);
        String sHtmlText = buildRequest(params, param.getShopUrl(), "post");

        return sHtmlText;
    }

    private String buildRequest(Map<String, String> sParaTemp, String actionUrl, String strMethod) {
        List<String> keys = new ArrayList<>(sParaTemp.keySet());

        StringBuilder sbHtml = new StringBuilder();

        sbHtml.append("<form id=\"frm1\" name=\"frm1\" action=\"" + actionUrl + "\" method=\"" + strMethod + "\">");

        for (String name : keys) {
            String value = sParaTemp.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        sbHtml.append("<input type=\"submit\" value=\"submit\" style=\"display:none;\"></form>");
        sbHtml.append("<script>setTimeout(\"document.getElementById('frm1').submit();\",100);</script>");

        return sbHtml.toString();
    }
}
