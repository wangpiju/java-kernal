package com.pays.ips;

import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;
import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class IpsPayApi
        extends PayApi {
    public static final String ID = "ips";
    private static final String GATEWAY = "https://newpay.ips.com.cn/psfp-entry/gateway/payment.do";
    private static final String TITLE = "环讯网关支付接口";
    private static final String RES_CODE_SUCCESS = "000000";
    private static final String GATEWAY_TYPE_01 = "01";
    private static final String CURRENCY_TYPE_RMB = "156";
    private static final String LANG = "GB";
    private static final String ORDER_ENCODE_TYPE_MD5 = "5";
    private static final String RET_ENCODE_TYPE_MD5 = "17";
    private static final String RET_TYPE = "1";
    private static final String IS_CREDIT_TYPE_BANK = "1";
    private static final String PRODUCT_TYPE_PERSONAL = "1";

    public String getTitle() {
        return "环讯网关支付接口";
    }

    public String getId() {
        return "ips";
    }

    protected void init() {
        addBank("ICBC", "1100");
        addBank("ABC", "1101");
        addBank("CMB", "1102");
        addBank("CIB", "1103");
        addBank("CITIC", "1104");
        addBank("CCB", "1106");
        addBank("BOC", "1107");
        addBank("COMM", "1108");
        addBank("SPDB", "1109");
        addBank("CMBC", "1110");
        addBank("HXB", "1111");
        addBank("CEB", "1112");
        addBank("BOB", "1113");
        addBank("GDB", "1114");
        addBank("NJCB", "1115");
        addBank("SHB", "1116");
        addBank("HZB", "1117");
        addBank("NBCB", "1118");
        addBank("PSBC", "1119");
        addBank("CZB", "1120");
        addBank("SPA", "1121");
        addBank("BEA", "1122");
        addBank("CBHB", "1123");
        addBank("SRCB", "1124");

        addBankCredit("RMBJJK", "01");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        Date now = new Date();
        String date = DateUtils.format(now, "yyyyMMdd");
        String time = DateUtils.format(now, "yyyyMMddHHmmss");

        maps.put("MerCode", param.getMerchantCode());
        maps.put("Account", param.getEmail());
        maps.put("MsgId", StrUtils.getGuid());
        maps.put("ReqDate", time);

        maps.put("MerBillNo", param.getOrderId());
        maps.put("Amount", param.getAmount());
        maps.put("Date", date);
        maps.put("CurrencyType", "156");
        maps.put("GatewayType", "01");
        maps.put("Lang", "GB");
        maps.put("Merchanturl", param.getPayReturnUrl());
        maps.put("OrderEncodeType", "5");
        maps.put("RetEncodeType", "17");
        maps.put("RetType", "1");
        maps.put("ServerUrl", param.getPayNoticeUrl());
        maps.put("GoodsName", "Recharge" + param.getAmount());
        if (param.getIsCredit().booleanValue()) {
            maps.put("IsCredit", "1");
            maps.put("BankCode", param.getBank());
            maps.put("ProductType", "1");
        }
        maps.put("Cert", param.getKey());

        return maps;
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "https://newpay.ips.com.cn/psfp-entry/gateway/payment.do";
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return null;
    }

    public String getResultString(boolean success) {
        return success ? "Y" : "N";
    }

    protected void appendValue(StringBuilder html, Map<String, String> maps, String sign, PayApiParam param) {
        html.append(createInput("pGateWayReq", StrUtils.htmlEncode(getXml(maps))));


        html.append(createInput("MerBillNo", (String) maps.get("MerBillNo")));
        html.append(createInput("Amount", (String) maps.get("Amount")));
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult rel = new NoticeResult();
        try {
            String paymentResult = (String) maps.get("paymentResult");
            Document doc = DocumentHelper.parseText(paymentResult);

            Element ips = doc.getRootElement();

            Element head = ips.element("GateWayRsp").element("head");
            Element body = ips.element("GateWayRsp").element("body");

            String rspCode = head.elementText("RspCode");
            String signature = head.elementText("Signature");

            String bodyStr = body.asXML();
            String merBillNo = body.elementText("MerBillNo");
            String ipsTradeNo = body.elementText("IpsTradeNo");
            String status = body.elementText("Status");

            String sign = encode(bodyStr, apiParam.getMerchantCode(), apiParam.getKey());
            if (!signature.equals(sign)) {
                return rel;
            }
            rel.setStatus(("000000".equals(rspCode)) && ("Y".equals(status)));
            rel.setOrderId(merBillNo);
            rel.setApiOrderId(ipsTradeNo);
        } catch (DocumentException localDocumentException) {
        }
        return rel;
    }

    private String getXml(Map<String, String> maps) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        sb.append("<Ips>");
        sb.append("<GateWayReq>");
        sb.append(getByMap(maps));
        sb.append("</GateWayReq>");
        sb.append("</Ips>");
        return sb.toString();
    }

    private String getByMap(Map<String, String> maps) {
        StringBuilder sb = new StringBuilder();
        sb.append("<head>");
        sb.append(getElement(maps, "MerCode"));
        sb.append(getElement(maps, "Account"));
        sb.append(getElement(maps, "MsgId"));
        sb.append(getElement(maps, "ReqDate"));

        String bodyStr = getBody(maps);
        sb.append(getElement("Signature", encode(bodyStr, (String) maps.get("MerCode"), (String) maps.get("Cert"))));

        sb.append("</head>");
        sb.append(bodyStr);
        return sb.toString();
    }

    private String getBody(Map<String, String> maps) {
        StringBuilder sb = new StringBuilder();
        sb.append("<body>");
        sb.append(getElement(maps, "MerBillNo"));
        sb.append(getElement(maps, "Amount"));
        sb.append(getElement(maps, "Date"));
        sb.append(getElement(maps, "CurrencyType"));
        sb.append(getElement(maps, "GatewayType"));
        sb.append(getElement(maps, "Lang"));
        sb.append(getElement(maps, "Merchanturl"));
        sb.append(getElement(maps, "FailUrl"));
        sb.append(getElement(maps, "Attach"));
        sb.append(getElement(maps, "OrderEncodeType"));
        sb.append(getElement(maps, "RetEncodeType"));
        sb.append(getElement(maps, "RetType"));
        sb.append(getElement(maps, "ServerUrl"));
        sb.append(getElement(maps, "BillEXP"));
        sb.append(getElement(maps, "GoodsName"));
        sb.append(getElement(maps, "IsCredit"));
        sb.append(getElement(maps, "BankCode"));
        sb.append(getElement(maps, "ProductType"));
        sb.append("</body>");
        return sb.toString();
    }

    private String encode(String bodyStr, String merCode, String cert) {
        return MD5.encode(bodyStr + merCode + cert, "utf-8").toLowerCase();
    }

    private String getElement(Map<String, String> maps, String key) {
        String v = (String) maps.get(key);
        return getElement(key, v == null ? "" : v);
    }

    private String getElement(String key, String value) {
        return "<" + key + "><![CDATA[" + value + "]]></" + key + ">";
    }
}
