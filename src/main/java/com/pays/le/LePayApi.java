package com.pays.le;

import com.hs3.utils.DateUtils;
import com.pays.PayApi;
import com.pays.PayApiParam;
import com.pays.RSA;
import com.pays.WithdrawApiParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LePayApi
        extends PayApi {
    public static final String ID = "le";
    private static final String TITLE = "乐付网关交易接口";
    private static final String GETWAY = "https://service.lepayle.com/api/gateway";
    private static final String PROXY_URL = "https://service.lepayle.com/api/quickdraw";

    public String getTitle() {
        return "乐付网关交易接口";
    }

    public String getId() {
        return "le";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://service.lepayle.com/api/gateway";
    }

    protected void init() {
        addBank("ICBC", "ICBC");
        addBank("ABC", "ABC");
        addBank("CCB", "CCB");
        addBank("COMM", "BOCM");
        addBank("BOC", "BOC");
        addBank("CMB", "CMB");
        addBank("PSBC", "PSBC");
        addBank("HXB", "HXB");
        addBank("CIB", "CIB");
        addBank("GDB", "GDB");
        addBank("CITIC", "CNCB");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("service", "");
        maps.put("partner", "");
        maps.put("input_charset", "");
        maps.put("sign_type", "");
        maps.put("sign", "");
        maps.put("request_time", "");
        maps.put("return_url", "");
        maps.put("out_trade_no", "");
        maps.put("amount_str", "");
        maps.put("tran_time", "");
        maps.put("tran_ip", "");
        maps.put("buyer_name", "");
        maps.put("buyer_contact", "");
        maps.put("good_name", "");
        maps.put("goods_detail", "");
        maps.put("bank_code", "");
        maps.put("receiver_address", "");
        maps.put("redirect_url", "");

        return maps;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return null;
    }

    public String getResultString(boolean success) {
        return null;
    }

    public String getProxyUrl() {
        return "https://service.lepayle.com/api/quickdraw";
    }

    public Map<String, String> getProxyParam(WithdrawApiParam param) {
        Map<String, String> params = new HashMap();

        StringBuilder content = new StringBuilder();
        content.append("amount_str=").append(param.getAmount());
        content.append("&bank_account_name=").append(param.getCardName());
        content.append("&bank_account_no=").append(param.getCardNum());
        content.append("&bank_city=").append(param.getMemo());
        content.append("&bank_mobile_no=").append(param.getExtends1());
        content.append("&bank_province=").append(param.getExtends3());
        content.append("&bank_site_name=").append(param.getIssueBankAddress());
        content.append("&bank_sn=").append(param.getBank());
        content.append("&bus_type=").append("11");
        content.append("&out_trade_no=").append(param.getGroupName() + "le" + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS"));
        content.append("&partner=").append(param.getMerchantCode());
        content.append("&remark=").append("");
        content.append("&return_url=").append(getProxyNotifyUrl(param));
        content.append("&service=").append("pay");
        content.append("&user_agreement=").append("1");

        String sign = null;
        try {
            sign = URLEncoder.encode(RSA.sign(content.toString(), param.getKey(), ""), "utf-8");
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
        }
        params.put("partner", param.getMerchantCode());
        params.put("input_charset", "utf-8");
        params.put("sign_type", "SHA1WITHRSA");
        params.put("sign", sign);
        params.put("request_time", DateUtils.format(new Date(), "yyMMddHHmmss"));
        params.put("content", RSA.getParamsWithDecodeByPublicKey(content.toString(), param.getPublicKey()));

        return params;
    }
}
