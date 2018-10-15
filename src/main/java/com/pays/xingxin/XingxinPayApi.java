package com.pays.xingxin;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;
import com.pays.RSA;
import com.pays.WithdrawApiParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class XingxinPayApi
        extends PayApi {
    public static final String ID = "xingxin";
    private static final String GATEWAY = "http://47.93.233.232:28080/Pay/gateway/payGetewayOrder";
    private static final String PROXY_URL = "http://47.93.233.232:28080/Pay/trans/proxypay";
    private static final String TITLE = "兴信网关支付接口";

    public String getTitle() {
        return "兴信网关支付接口";
    }

    public String getId() {
        return "xingxin";
    }

    protected String getRemoteUrl(PayApiParam param) {
        return "http://47.93.233.232:28080/Pay/gateway/payGetewayOrder";
    }

    protected void init() {
        addBank("CMB", "CMB");
        addBank("ICBC", "ICBC");
        addBank("CMB", "CCB");
        addBank("BOC", "BOC");
        addBank("ABC", "ABC");
        addBank("COMM", "BOCM");
        addBank("SPDB", "SPDB");
        addBank("GDB", "CGB");
        addBank("CITIC", "CITIC");
        addBank("CEB", "CEB");
        addBank("CIB", "CIB");
        addBank("SPA", "PAYH");
        addBank("CMBC", "CMBC");
        addBank("HXB", "HXB");
        addBank("PSBC", "PSBC");
        addBank("BOB", "BCCB");
        addBank("SHB", "SHBANK");
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        return RSA.sign((String) maps.get("TRANDATA"), key, "");
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult rel = new NoticeResult();

        String signKey = createNoticeSign(maps, apiParam.getKey());

        String sign = (String) maps.get("SIGN");
        if (!signKey.equals(sign)) {
            return rel;
        }
        String outOrderId = (String) maps.get("ORDERNO");
        String instructCode = (String) maps.get("CHNLORDERNO");
        String status = (String) maps.get("ORDSTATUS");
        String recode = (String) maps.get("RECODE");
        if (("00000".equals(recode)) && (getSuccessCode().equals(status))) {
            rel.setStatus(true);
        } else {
            rel.setStatus(false);
        }
        rel.setOrderId(outOrderId);
        rel.setApiOrderId(instructCode);
        return rel;
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new HashMap();

        maps.put("MERCNUM", param.getMerchantCode());


        //jd-gui
        //String amount = new BigDecimal(param.getAmount()).multiply(new BigDecimal("100")).intValue();
        String amount = (new StringBuilder(String.valueOf((new BigDecimal(param.getAmount())).multiply(new BigDecimal("100")).intValue()))).toString();

        StringBuilder sb = new StringBuilder();
        sb.append("ORDERNO=").append(param.getOrderId());
        sb.append("&TXNAMT=").append(amount);
        sb.append("&PRO_ID=").append("GC_GATEWAY");
        sb.append("&RETURNURL=").append(param.getPayReturnUrl());
        sb.append("&NOTIFYURL=").append(param.getPayNoticeUrl());
        sb.append("&BANKID=").append(param.getBank());
        sb.append("&CARD_TYPE=").append("01");
        sb.append("&USER_TYPE=").append("1");
        sb.append("&REMARK=").append(param.getOrderId());

        maps.put("TRANDATA", sb.toString());

        return maps;
    }

    protected String getSuccessCode() {
        return "01";
    }

    public String getResultString(boolean success) {
        if (success) {
            return "success";
        }
        return "faild";
    }

    protected String getSignName() {
        return "SIGN";
    }

    public String getProxyUrl() {
        return "http://47.93.233.232:28080/Pay/trans/proxypay";
    }

    public Map<String, String> getProxyParam(WithdrawApiParam param) {
        Map<String, String> params = new HashMap();

        StringBuilder TRANDATA = new StringBuilder();
        TRANDATA.append("CP_NO=").append(param.getGroupName() + "xingxin" + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS"));
        TRANDATA.append("&TXNAMT=").append(new BigDecimal(param.getAmount()).multiply(new BigDecimal("100")).intValue());
        TRANDATA.append("&OPNBNK=").append(param.getBank());
        TRANDATA.append("&OPNBNKNAM=").append(param.getIssueBankName() + param.getIssueBankAddress());
        TRANDATA.append("&ACTNO=").append(param.getCardNum());
        TRANDATA.append("&ACTNAM=").append(param.getCardName());
        TRANDATA.append("&ACTIDCARD=").append(param.getExtends1());
        TRANDATA.append("&ACTMOBILE=").append(param.getExtends1());

        String SIGN = RSA.sign(TRANDATA.toString(), param.getKey(), "");

        params.put("MERCNUM", param.getMerchantCode());
        params.put("TRANDATA", TRANDATA.toString());
        params.put("SIGN", SIGN);

        return params;
    }
}
