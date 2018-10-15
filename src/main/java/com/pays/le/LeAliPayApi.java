package com.pays.le;

import com.hs3.utils.DateUtils;
import com.pays.PayApiParam;
import com.pays.RSA;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LeAliPayApi
        extends LeWxPayApi {
    public static final String ID = "leAli";
    private static final String TITLE = "乐付RSA支付宝接口";

    public String getTitle() {
        return "乐付RSA支付宝接口";
    }

    public String getId() {
        return "leAli";
    }

    protected void init() {
        addBank("ALIPAY", "ALIPAY");
        addBankCredit("ALIPAY", "ALIPAY");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        StringBuilder content = new StringBuilder();
        content.append("amount_str=").append(param.getAmount());
        content.append("&out_trade_no=").append(param.getOrderId());
        content.append("&partner=").append(param.getMerchantCode());
        content.append("&remark=").append("");
        content.append("&service=").append("ali_pay");
        content.append("&sub_body=").append(param.getOrderId());
        content.append("&subject=").append(param.getOrderId());
        content.append("&ali_pay_type=").append("ali_sm");
        content.append("&return_url=").append(param.getPayNoticeUrl());

        maps.put("partner", param.getMerchantCode());
        maps.put("input_charset", "utf-8");
        maps.put("sign_type", "SHA1WITHRSA");
        maps.put("request_time", DateUtils.format(new Date(), "yyMMddHHmmss"));
        maps.put("content", RSA.getParamsWithDecodeByPublicKey(content.toString(), param.getPublicKey()));


        maps.put("contentSrc", content.toString());

        return maps;
    }
}
