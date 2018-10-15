package com.pays.daddy;

import com.alibaba.fastjson.JSONObject;
import com.hs3.entity.finance.Recharge;
import com.hs3.utils.HttpClientUtil;
import com.hs3.utils.ResponseData;
import com.pays.*;
import com.pays.common.IThirdPayCommon;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DaddyPayApi extends PayApi implements IThirdPayCommon {
    private static final Logger logger = LoggerFactory.getLogger(DaddyPayApi.class);
    public static final String ID = "daddy";
    private static final String INPUT_CHARSET = "UTF-8";
    private static final String GATEWAY = "http://www.dppay100d.com/mownecum_api/Deposit?format=json";
    private static final String TITLE = "DADDY支付接口";
    private static final ObjectMapper mapper = new ObjectMapper();

    public String getTitle() {
        return "DADDY支付接口";
    }

    public String getId() {
        return "daddy";
    }

    protected void init() {
        addBank("1", "中国工商银行");
        addBank("2", "招商银行");
        addBank("3", "中国建设银行");
        addBank("4", "中国农业银行");
        addBank("5", "中国银行");
        addBank("6", "交通银行");
        addBank("7", "中国民生银行");
        addBank("8", "中信银行");
        addBank("9", "上海浦东发展银行");
        addBank("10", "邮政储汇");
        addBank("11", "中国光大银行");
        addBank("12", "平安银行");
        addBank("13", "广发银行");
        addBank("14", "华夏银行");
        addBank("15", "福建兴业银行");
        //addBank("30", "支付宝(二维码)");
        //addBank("31", "财付通");
        //addBank("40", "微信支付（二维码)");
        //addBank("51", "银联无卡支付");
        addBank("52", "QQPAY掃碼");

        addBankCredit("52", "QQPAY掃碼");
        addBankCredit("40", "微信支付（二维码)");
        addBankCredit("30", "支付宝(二维码)");
        addBankCredit("1", "中国工商银行");
        addBankCredit("3", "中国建设银行");
        addBankCredit("4", "中国农业银行");
        addBankCredit("5", "中国银行");
        addBankCredit("2", "招商银行");
        addBankCredit("10", "邮政储汇");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap<>();
        maps.put("format", "json");
        maps.put("company_id", param.getMerchantCode());
        maps.put("bank_id", param.getBank());
        maps.put("amount", param.getAmount());
        maps.put("company_order_num", param.getOrderId());
        maps.put("company_user", "R" + param.getAmount());
        maps.put("estimated_payment_bank", param.getBank());
        maps.put("deposit_mode", param.getDepositMode());
        maps.put("group_id", "0");
        maps.put("web_url", "http://www.dppay100d.com/mownecum_api/Deposit");
        maps.put("memo", "");
        maps.put("note", param.getTraceId());
        maps.put("note_model", "2");
        maps.put("terminal", param.getIsMobile() ? "2" : "1");
        System.out.println(maps);
        return maps;
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://www.dppay100d.com/mownecum_api/Deposit?format=json";
    }

    public ObjectNode getCheckResult(Recharge recharge, String url){
        ObjectNode objectNode = mapper.createObjectNode();

        return objectNode;
    }
//    http://52.69.65.224/Mownecum_2_API_Live/Deposit?format=json
// &company_id=379&bank_id=1&amount=4.00&company_order_num=123456789&company_user=hb123&estimated_payment_bank=1
// &deposit_mode=2&group_id=0&web_url=http://baidu.com/&note_model=2&memo=&note=&terminal=1&key=#HF$20180717%FHBS%@

    protected String createNoticeSign(Map<String, String> param, String key) {
        StringBuilder sb = new StringBuilder(MD5.encode(key, "UTF-8"));
        sb.append(param.get("pay_time"));
        sb.append(param.get("bank_id"));
        sb.append(param.get("amount"));
        sb.append(param.get("company_order_num"));
        sb.append(param.get("mownecum_order_num"));
        sb.append(param.get("pay_card_num"));
        sb.append(param.get("pay_card_name"));
        sb.append(param.get("channel"));
        sb.append(param.get("area"));
        sb.append(param.get("fee"));
        sb.append(param.get("transaction_charge"));
        sb.append(param.get("deposit_mode"));
        return MD5.encode(sb.toString(), "UTF-8");
    }

    protected String createPaySign(Map<String, String> param, String key) {
        StringBuilder sb = new StringBuilder(MD5.encode(key, "UTF-8"));
        sb.append(param.get("company_id"));
        sb.append(param.get("bank_id"));
        sb.append(param.get("amount"));
        sb.append(param.get("company_order_num"));
        sb.append(param.get("company_user"));
        sb.append(param.get("estimated_payment_bank"));
        sb.append(param.get("deposit_mode"));
        sb.append(param.get("group_id"));
        sb.append(param.get("web_url"));
        sb.append(param.get("memo"));
        sb.append(param.get("note"));
        sb.append(param.get("note_model"));
        return MD5.encode(sb.toString(), "UTF-8");
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult rel = new NoticeResult();

        String signKey = createNoticeSign(maps, apiParam.getKey());

        String sign = maps.get("key");
        if (!signKey.equals(sign)) {
            return rel;
        }
        rel.setStatus(true);
        rel.setAmount(new BigDecimal(maps.get("amount")));
        rel.setOrderId(maps.get("company_order_num"));
        rel.setApiOrderId(maps.get("mownecum_order_num"));
        return rel;
    }

    public String getResultString(boolean success) {
        return "";
    }

    public String getResultString(boolean success, Map<String, String> params) {
        return
                "{\"company_order_num\":\"" + params.get("company_order_num") + "\",\"mownecum_order_num\":\"" + params.get("mownecum_order_num") + "\",\"status\":" + (success ? 1 : 0) + ",\"error_msg\":\"" + (success ? "" : "处理失败！") + "\"}";
    }

    protected String getSignName() {
        return "key";
    }

    public String sendPayReqHtml(PayApiParam param) {
       // param.setBank(getBank(param.getBank(), param.getIsCredit()));
        Map params = getParamMaps(param);
        String sign = createPaySign(params, param.getKey());
        params.put("key", sign);
        ResponseData reqResult = HttpClientUtil.buildGet(param.getShopUrl(), params, null);
        logger.info(String.format("--> request third pay complete, url : %s, result:%s ", param.getShopUrl(), JSONObject.toJSONString(reqResult)));
        System.out.println(String.format("--> request third pay complete, url : %s, result:%s ", param.getShopUrl(), JSONObject.toJSONString(reqResult)));
        if (StringUtils.isNotBlank(reqResult.getResult())) {
            JSONObject resObj = JSONObject.parseObject(reqResult.getResult());
            Integer status = resObj.getInteger("status");
            if (status != null && status == 1) {
                String breakUrl = resObj.getString("break_url");
                if (StringUtils.isNotBlank(breakUrl)) {
                    StringBuilder html = new StringBuilder();
                    html.append("<form id=\"form1\" method=\"post\" action=\"").append(breakUrl).append("\">");
                    html.append("</form>");
                    html.append("<script type=\"text/javascript\">");
                    html.append("function sub() {");
                    html.append("\tdocument.getElementById('form1').submit();");
                    html.append("}");
                    html.append("setTimeout('sub()', 0);");
                    html.append("</script>");
                    return html.toString();
                }
            }
        }
        logger.warn("--> request third pay failed, " + JSONObject.toJSONString(reqResult));
        System.out.println("--> request third pay failed, " + JSONObject.toJSONString(reqResult));
        return null;
    }
}
