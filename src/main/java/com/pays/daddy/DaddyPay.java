package com.pays.daddy;

import com.pays.PayApi;
import com.pays.PayApiParam;
import org.junit.Test;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-07-18 11:21
 **/
public class DaddyPay {


    @Test
    public void createDeposit() {
        //    http://52.69.65.224/Mownecum_2_API_Live/Deposit?format=json
// &company_id=379&bank_id=1&amount=4.00&company_order_num=123456789&company_user=hb123&estimated_payment_bank=1
// &deposit_mode=2&group_id=0&web_url=http://baidu.com/&note_model=2&memo=&note=&terminal=1&key=#HF$20180717%FHBS%@
        PayApiParam param = new PayApiParam();
        param.setMerchantCode("379");
        param.setBank("ALIPAY");
        param.setAmount("200.00");
        param.setOrderId("HF000000073");
        param.setShopUrl("http://www.dppay100d.com/mownecum_api/Deposit");
        param.setIsMobile(true);
        param.setKey("#HF$20180717%FHBS%@");
        param.setDepositMode("2");
        DaddyPayApi payApi = new DaddyPayApi() ;
        String result = payApi.sendPayReqHtml(param);
        System.out.println(result);
//        System.out.println(payApi.getHtml(param));

    }
}
