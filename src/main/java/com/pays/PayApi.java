package com.pays;

import com.hs3.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class PayApi {
    public static final String PAY_TYPE_WEIXIN = "WEIXIN";
    public static final String PAY_TYPE_ZHIFUBAO = "ZHIFUBAO";
    public static final String PAY_TYPE_YINLIAN = "RMBJJK";
    protected static final String INPUT_CHARSET = "utf-8";
    public Map<String, String> banks = new HashMap<>();
    public Map<String, String> banksCredit = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(PayApi.class);
    public String CHECKURL;


    protected void addBank(String key, String value) {
        this.banks.put(key, value);
    }
    protected void addBankCredit(String key, String value) {
        this.banksCredit.put(key, value);
    }

    public String getCHECKURL() {
        return CHECKURL;
    }

    public void setCHECKURL(String CHECKURL) {
        this.CHECKURL = CHECKURL;
    }

    public Map<String, String> getAllBanks(boolean isCredit) {
        if(!isCredit)
            return banksCredit;
        else
            return banks;
    }

    public String getBank(String key, Boolean isCredit) {
        if (isCredit != null && !isCredit) {
            return this.banks.get(key);
        }else
        return this.banksCredit.get(key);
    }

    public Set<String> getBankCodes(boolean isCredit) {
        if (!isCredit) {
            return this.banksCredit.keySet();
        }
        return this.banks.keySet();
    }

    public abstract String getTitle();

    public abstract String getId();

    public PayApi() {
        init();
    }

    protected abstract void init();

    public String getHtml(PayApiParam param) {
        StringBuilder html = new StringBuilder();

        param.setBank(getBank(param.getBank(), param.getIsCredit()));

        Map<String, String> maps = getParamMaps(param);

        String sign = createPaySign(maps, param.getKey());

        html.append("<form id=\"form1\" method=\"post\" action=\"").append(param.getShopUrl()).append("\">");

        appendValue(html, maps, sign, param);


        html.append(createInput("remoteUrl", getRemoteUrl(param)));
        html.append(createInput("shopNoticeUrl", param.getShopNoticeUrl()));
        html.append(createInput("shopReturnUrl", param.getShopReturnUrl()));
        html.append(createInput("publicPayOrderNo", param.getOrderId()));
        html.append(createInput("publicPayAmount", param.getAmount()));

        html.append("</form>");
        html.append("<script type=\"text/javascript\">");
        html.append("function sub() {");
        html.append("\tdocument.getElementById('form1').submit();");
        html.append("}");
        html.append("setTimeout('sub()', 0);");
        html.append("</script>");
        return html.toString();
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        appendMapValue(html, maps);
        html.append(createInput(getSignName(), sign));
    }

    protected void appendMapValue(StringBuilder html, Map<String, String> maps) {
        for (String name : maps.keySet()) {
            String value = (String) maps.get(name);
            html.append(createInput(name, value));
        }
    }

    protected String createInput(String name, String value) {
        return "<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>";
    }

    protected abstract Map<String, String> getParamMaps(PayApiParam paramPayApiParam);

    protected abstract String getRemoteUrl(PayApiParam paramPayApiParam);

    protected String createPaySign(Map<String, String> maps, String key, String[] withouts) {
        StringBuilder sb = new StringBuilder();

        sb.append(getDictSortStr(maps, withouts));
        sb.append("&").append(getKeyName()).append("=").append(key);
        return MD5.encode(sb.toString(), "utf-8");
    }

    protected String getDictSortStr(Map<String, String> maps, String[] withouts) {
        StringBuilder sb = new StringBuilder();

        List<String> keys = new ArrayList<>(maps.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String k = (String) keys.get(i);
            if (withouts != null) {
                for (String str : withouts) {
                    if (k.equals(str)) {
                        break;
                    }
                }
            }
            String value = maps.get(k);
            if (!StrUtils.hasEmpty(new Object[]{value})) {
                sb.append(k).append("=").append(value).append("&");
            }
        }
        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }

    protected NoticeResult verifyDefault(Map<String, String> maps, String key) {
        return verify(maps, key, "sign", "order_no", "trade_no", "trade_status");
    }

    protected NoticeResult verify(Map<String, String> maps, String key, String keySign, String keyOrderNo, String keyTradeNo, String keyTradeStatus) {
        NoticeResult rel = new NoticeResult();

        String signKey = createNoticeSign(maps, key);

        String sign = (String) maps.get(keySign);
        if (!signKey.equals(sign)) {
            return rel;
        }
        String outOrderId = (String) maps.get(keyOrderNo);
        String instructCode = (String) maps.get(keyTradeNo);
        String status = (String) maps.get(keyTradeStatus);
        if (getSuccessCode().equals(status)) {
            rel.setStatus(true);
        } else {
            rel.setStatus(false);
        }
        rel.setOrderId(outOrderId);
        rel.setApiOrderId(instructCode);
        return rel;
    }

    protected abstract String createPaySign(Map<String, String> paramMap, String paramString);

    protected String createNoticeSign(Map<String, String> maps, String key) {
        return createPaySign(maps, key);
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        return verifyDefault(maps, apiParam.getKey());
    }

    public abstract String getResultString(boolean paramBoolean);

    public String getResultString(boolean success, Map<String, String> params) {
        return getResultString(success);
    }

    protected String getSuccessCode() {
        return getResultString(true);
    }

    protected String getSignName() {
        return "sign";
    }

    protected String getKeyName() {
        return "key";
    }

    public String getProxyUrl() {
        return null;
    }

    public String getProxyNotifyUrl(WithdrawApiParam param) {
        return param.getShopUrl() + "/" + param.getGroupName() + "/proxyNotify";
    }

    public Map<String, String> getProxyParam(WithdrawApiParam param) {
        return null;
    }
}
