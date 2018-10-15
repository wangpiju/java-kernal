package com.pays.le;

import com.hs3.utils.DateUtils;
import com.hs3.utils.HttpUtils;
import com.pays.ApiParam;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;
import com.pays.RSA;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class LeWxPayApi
        extends PayApi {
    public static final String ID = "leWx";
    private static final String TITLE = "乐付RSA微信接口";
    private static final String GETWAY = "https://service.lepayle.com/api/gateway";

    public String getTitle() {
        return "乐付RSA微信接口";
    }

    public String getId() {
        return "leWx";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://service.lepayle.com/api/gateway";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXIN");
        addBankCredit("WEIXIN", "WEIXIN");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        StringBuilder content = new StringBuilder();
        content.append("amount_str=").append(param.getAmount());
        content.append("&out_trade_no=").append(param.getOrderId());
        content.append("&partner=").append(param.getMerchantCode());
        content.append("&remark=").append("");
        content.append("&service=").append("wx_pay");
        content.append("&sub_body=").append(param.getOrderId());
        content.append("&subject=").append(param.getOrderId());
        content.append("&wx_pay_type=").append("wx_sm");
        content.append("&return_url=").append(param.getPayNoticeUrl());

        maps.put("partner", param.getMerchantCode());
        maps.put("input_charset", "utf-8");
        maps.put("sign_type", "SHA1WITHRSA");
        maps.put("request_time", DateUtils.format(new Date(), "yyMMddHHmmss"));
        maps.put("content", RSA.getParamsWithDecodeByPublicKey(content.toString(), param.getPublicKey()));


        maps.put("contentSrc", content.toString());

        return maps;
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        maps.put(getSignName(), sign);
        maps.put("transferUrl", getRemoteUrl(param));
        String result = "";
        try {
            result = HttpUtils.postString(param.getShopUrl().substring(0, param.getShopUrl().lastIndexOf(getId())) + "transfer", maps);

            JsonNode jn = new ObjectMapper().readTree(result);
            JsonNode recode = jn.get("is_succ");
            if ("T".equals(recode.asText())) {
                Iterator<Map.Entry<String, JsonNode>> jsonNodes = new ObjectMapper().readTree(RSA.decryptByPrivateKey(jn.get("response").asText(), param.getKey())).getFields();
                while (jsonNodes.hasNext()) {
                    Map.Entry<String, JsonNode> node = (Map.Entry) jsonNodes.next();
                    html.append(createInput((String) node.getKey(), ((JsonNode) node.getValue()).asText()));
                }
            } else {
                html.append(createInput("error", URLEncoder.encode(result, "utf-8")));
            }
        } catch (Exception e) {
            try {
                html.append(createInput("error", URLEncoder.encode("leWx获取二维码异常" + e.getMessage() + result, "utf-8")));
            } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            }
        }
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult rel = new NoticeResult();
        try {
            String content = RSA.decryptByPrivateKey(URLDecoder.decode((String) maps.get("content"), "utf-8"), apiParam.getKey());
            if (!RSA.verify(content, URLDecoder.decode((String) maps.get("sign"), "utf-8"), apiParam.getPublicKey(), "")) {
                return rel;
            }
            JsonNode jn = new ObjectMapper().readTree(content);
            if (("1".equals(maps.get("status"))) && ("1".equals(jn.get("status").asText()))) {
                rel.setStatus(true);
                rel.setApiOrderId(jn.get("trade_id").asText());
            } else {
                rel.setStatus(false);
            }
            rel.setOrderId((String) maps.get("out_trade_no"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rel;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        try {
            return URLEncoder.encode(RSA.sign((String) maps.remove("contentSrc"), key, ""), "utf-8");
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
        }
        return null;
    }

    public String getResultString(boolean success) {
        return success ? "success" : "fail";
    }
}
