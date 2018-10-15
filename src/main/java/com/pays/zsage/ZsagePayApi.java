package com.pays.zsage;

import com.hs3.utils.DateUtils;
import com.pays.ApiParam;
import com.pays.MD5;
import com.pays.NoticeResult;
import com.pays.PayApi;
import com.pays.PayApiParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZsagePayApi
        extends PayApi {
    public static final String ID = "zsage";
    private static final String GATEWAY = "http://payment.zsagepay.com/ebank/pay.do";
    private static final String TITLE = "泽圣支付接口";

    protected String getRemoteUrl(PayApiParam param) {
        return "http://payment.zsagepay.com/ebank/pay.do";
    }

    public String getTitle() {
        return "泽圣支付接口";
    }

    public String getId() {
        return "zsage";
    }

    protected void init() {
        addBank("ABC", "ABC");
        addBank("BOB", "BCCB");
        addBank("COMM", "BCM");
        addBank("BJRCB", "BJRCB");
        addBank("BOC", "BOC");
        addBank("BOS", "BOS");
        addBank("CCB", "CCB");
        addBank("CEB", "CEB");
        addBank("CIB", "CIB");
        addBank("CITIC", "CITIC");
        addBank("CMB", "CMB");
        addBank("CMBC", "CMBC");
        addBank("GDB", "GDB");
        addBank("HXB", "HXB");
        addBank("ICBC", "ICBC");
        addBank("SPA", "PAB");
        addBank("PSBC", "PSBC");
        addBank("SPDB", "SPDB");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = new LinkedHashMap();

        Date now = new Date();
        String time = DateUtils.format(now, "yyyyMMddHHmmss");
        String lastTime = DateUtils.format(DateUtils.addMinute(now, 15), "yyyyMMddHHmmss");

        //jd-gui
        //String amount = new BigDecimal(param.getAmount()).multiply(new BigDecimal("100")).intValue();
        String amount = (new StringBuilder(String.valueOf((new BigDecimal(param.getAmount())).multiply(new BigDecimal("100")).intValue()))).toString();

        maps.put("merchantCode", param.getMerchantCode());
        maps.put("outOrderId", param.getOrderId());
        maps.put("totalAmount", amount);
        maps.put("goodsName", "");
        maps.put("goodsExplain", "");
        maps.put("orderCreateTime", time);
        maps.put("lastPayTime", lastTime);
        maps.put("merUrl", param.getPayReturnUrl());
        maps.put("noticeUrl", param.getPayNoticeUrl());
        maps.put("bankCode", param.getBank());
        maps.put("bankCardType", "01");

        maps.put("ext", "");

        return maps;
    }

    protected String createPaySign(Map<String, String> maps, String key) {
        String signsrc = String.format("lastPayTime=%s&merchantCode=%s&orderCreateTime=%s&outOrderId=%s&totalAmount=%s&KEY=%s", new Object[]{maps.get("lastPayTime"), maps.get("merchantCode"),
                maps.get("orderCreateTime"), maps.get("outOrderId"), maps.get("totalAmount"), key});
        return MD5.encode(signsrc, "utf-8").toUpperCase();
    }

    protected String createNoticeSign(Map<String, String> maps, String key) {
        String signsrc = String.format("instructCode=%s&merchantCode=%s&outOrderId=%s&totalAmount=%s&transTime=%s&transType=%s&KEY=%s", new Object[]{maps.get("instructCode"), maps.get("merchantCode"),
                maps.get("outOrderId"), maps.get("totalAmount"), maps.get("transTime"), maps.get("transType"), key});
        return MD5.encode(signsrc, "utf-8").toUpperCase();
    }

    public NoticeResult verify(Map<String, String> maps, ApiParam apiParam) {
        NoticeResult rel = new NoticeResult();

        String signKey = createNoticeSign(maps, apiParam.getKey());

        String sign = (String) maps.get("sign");
        if (!signKey.equals(sign)) {
            return rel;
        }
        String outOrderId = (String) maps.get("outOrderId");
        String instructCode = (String) maps.get("instructCode");
        rel.setStatus(true);
        rel.setOrderId(outOrderId);
        rel.setApiOrderId(instructCode);
        return rel;
    }

    public String getResultString(boolean success) {
        return success ? "{'code':'00'}" : "{'code':'01','msg':'验签失败'}";
    }
}
