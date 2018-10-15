package com.pays.ac;

import com.hs3.utils.DateUtils;
import com.pays.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class AcPayApi{
//public class AcPayApi extends PayApi {
//    public static final String ID = "ac";
//    private static final String INPUT_CHARSET = "UTF-8";
//    private static final String GATEWAY = "https://gateway.acpay365.com/GateWay/Index";
//    private static final String TITLE = "AC支付接口";
//
//    public String getTitle() {
//        return "AC支付接口";
//    }
//
//    public String getId() {
//        return ID;
//    }
//
//    protected void init() {
//        addBank("1000", "微信扫码" );
//        addBank("1002", "微信直连-手机端");
//        addBank("1003", "支付宝扫码");
//        addBank("1004", "支付宝直连-手机端");
//        addBank("1005", "QQ钱包扫码");
//        addBank("1006", "QQ钱包直连-手机端");
//        addBank("1007", "京东钱包扫码");
//        addBank("1008", "京东钱包直连-手机端");
//        addBank("1009", "银联扫码");
//        addBank("962", "中信银行");
//        addBank("963", "中国银行");
//        addBank("964", "中国农业银行");
//        addBank("965", "中国建设银行");
//        addBank("967", "中国工商银行");
//        addBank("970", "招商银行");
//        addBank("971", "邮政储蓄");
//        addBank("972", "兴业银行");
//        addBank("976", "上海农村商业银行");
//        addBank("977", "浦东发展银行");
//        addBank("979", "南京银行");
//        addBank("980", "民生银行");
//        addBank("981", "交通银行");
//        addBank("983", "杭州银行");
//        addBank("985", "广东发展银行");
//        addBank("986", "光大银行");
//        addBank("987", "东亚银行");
//        addBank("989", "北京银行");
//    }
//
//    protected Map<String, String> getParamMaps(PayApiParam param) {
//        Map<String, String> maps = new HashMap();
//
//        maps.put("customer", param.getMerchantCode());
//        maps.put("banktype", param.getBank());
//        maps.put("amount", param.getAmount());
//        maps.put("orderid", param.getOrderId());
//        maps.put("asynbackurl", param.getShopUrl());
//        maps.put("request_time", DateUtils.formatPayTime(new Date()));
//        maps.put("synbackurl", param.getShopUrl());
//        maps.put("isqrcode", "N");
//        maps.put("israndom", "Y");
//        maps.put("attach", param.getTraceId());
//        maps.put("sign", param.getTraceId());
//
//        return maps;
//    }
//
//    protected String getRemoteUrl(PayApiParam param) {
//        return GATEWAY;
//    }
//
//    private  String createSign(Map<String, String> param, String key) {
//        StringBuilder sb = new StringBuilder(MD5.encode(key, "UTF-8"));
//        sb.append((String) param.get("customer"));
//        sb.append((String) param.get("banktype"));
//        sb.append((String) param.get("amount"));
//        sb.append((String) param.get("orderid"));
//        sb.append((String) param.get("asynbackurl"));
//        sb.append((String) param.get("request_time"));
//        sb.append((String) param.get("key"));
//
//        return sign(sb.toString(), "UTF-8");
//    }
//
//    protected String createNoticeSign(Map<String, String> param, String key) {
//        return createSign(param, key);
//    }
//
//    protected String createPaySign(Map<String, String> param, String key) {
//        return createSign(param, key);
//    }
//
//    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
//        NoticeResult rel = new NoticeResult();
//
//        String signKey = createNoticeSign(maps, apiParam.getKey());
//
//        String sign = (String) maps.get("key");
//        if (!signKey.equals(sign)) {
//            return rel;
//        }
//        rel.setStatus(true);
//        rel.setAmount(new BigDecimal((String) maps.get("amount")));
//        rel.setOrderId((String) maps.get("company_order_num"));
//        rel.setApiOrderId((String) maps.get("mownecum_order_num"));
//        return rel;
//    }
//
//    public String getResultString(boolean success) {
//        return "";
//    }
//
//    public String getResultString(boolean success, Map<String, String> params) {
//        return "{\"company_order_num\":\"" + (String) params.get("company_order_num") + "\",\"mownecum_order_num\":\"" + (String) params.get("mownecum_order_num") + "\",\"status\":" + (success ? 1 : 0) + ",\"error_msg\":\"" + (success ? "" : "处理失败！") + "\"}";
//    }
//
//    protected String getSignName() {
//        return "key";
//    }
//
//    public static String sign(String text,String charset) {
//        try
//        {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update(text.getBytes(charset));
//            byte b[] = md.digest();
//            int i;
//            StringBuffer buf = new StringBuffer("");
//            for (int offset = 0; offset < b.length; offset++) {
//                i = b[offset];
//                if (i < 0)
//                    i += 256;
//                if (i < 16)
//                    buf.append("0");
//                buf.append(Integer.toHexString(i));
//            }
//            return buf.toString();
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
