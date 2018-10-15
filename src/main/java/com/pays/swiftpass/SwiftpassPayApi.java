package com.pays.swiftpass;

import com.hs3.utils.DateUtils;
import com.hs3.utils.IdBuilder;
import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SwiftpassPayApi
        extends PayApi {
    public static final String ID = "swiftpass";
    private static final String GATEWAY = "https://pay.swiftpass.cn/pay/gateway";
    private static final String TITLE = "威富通扫码接口";

    public String getTitle() {
        return "威富通扫码接口";
    }

    public String getId() {
        return "swiftpass";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://pay.swiftpass.cn/pay/gateway";
    }

    protected void init() {
        addBank("WEIXIN", "pay.weixin.native");
        addBankCredit("ALIPAY", "pay.alipay.native");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        Date now = new Date();
        String time_start = DateUtils.format(now, "yyyyMMddHHmmss");
        String time_expire = DateUtils.format(DateUtils.addMinute(now, 15), "yyyyMMddHHmmss");

        //jd-gui
        //String amount = new BigDecimal(param.getAmount()).multiply(new BigDecimal("100")).intValue();
        String amount = (new StringBuilder(String.valueOf((new BigDecimal(param.getAmount())).multiply(new BigDecimal("100")).intValue()))).toString();

        maps.put("service", param.getBank());
        maps.put("version", "2.0");
        maps.put("charset", "UTF-8");
        maps.put("sign_type", "MD5");
        maps.put("mch_id", param.getMerchantCode());
        maps.put("sign_agentno", "");
        maps.put("out_trade_no", param.getOrderId());
        maps.put("device_info", "");
        maps.put("body", "Recharge" + amount);
        maps.put("attach", "");
        maps.put("total_fee", amount);
        maps.put("mch_create_ip", param.getIp());
        maps.put("notify_url", param.getPayNoticeUrl());
        maps.put("time_start", time_start);
        maps.put("time_expire", time_expire);
        maps.put("op_user_id", "");
        maps.put("goods_tag", "");
        maps.put("product_id", "");
        maps.put("nonce_str", IdBuilder.getId("nonce", 10));

        return maps;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        StringBuilder sb = new StringBuilder();

        sb.append(getDictSortStr(maps, new String[]{"sign"})).append("&key=").append(key);
        return MD5.encode(sb.toString(), "UTF-8").toUpperCase();
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Iterator<Map.Entry<String, String>> it = maps.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ((v != null) && (!"".equals(v)) && (!"appkey".equals(k))) {
                sb.append("<" + k + ">" + (String) maps.get(k) + "</" + k + ">\n");
            }
        }
        sb.append("<sign>" + sign + "</sign>\n");
        sb.append("</xml>");

        html.append(createInput("xml", sb.toString()));
        html.append(createInput("total_fee", (String) maps.get("total_fee")));
        html.append(createInput("out_trade_no", (String) maps.get("out_trade_no")));
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult rel = new NoticeResult();

        String signKey = createNoticeSign(maps, apiParam.getKey());

        String sign = (String) maps.get("sign");
        if (!signKey.equals(sign)) {
            return rel;
        }
        String status = (String) maps.get("status");
        String resultCode = (String) maps.get("result_code");
        String payResult = (String) maps.get("pay_result");
        if (("0".equals(status)) && ("0".equals(resultCode)) && ("0".equals(payResult))) {
            rel.setStatus(true);
            rel.setApiOrderId((String) maps.get("transaction_id"));
            rel.setOrderId((String) maps.get("out_trade_no"));
        }
        return rel;
    }

    public String getResultString(boolean success) {
        return success ? "success" : "failed";
    }
}
