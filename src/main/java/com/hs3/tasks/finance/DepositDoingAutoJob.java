package com.hs3.tasks.finance;

import com.hs3.entity.finance.Deposit;
import com.hs3.entity.finance.FinanceSetting;
import com.hs3.entity.finance.FinanceWithdraw;
import com.hs3.entity.sys.SysConfig;
import com.hs3.service.finance.DepositService;
import com.hs3.service.finance.FinanceSettingService;
import com.hs3.service.finance.FinanceWithdrawService;
import com.hs3.service.sys.SysConfigService;
import com.hs3.utils.HttpUtils;
import com.hs3.utils.StrUtils;
import com.hs3.web.utils.SpringContext;
import com.pays.WithdrawApi;
import com.pays.WithdrawApiParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DepositDoingAutoJob
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(DepositDoingAutoJob.class);

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {
        DepositService depositService = (DepositService) SpringContext.getBean(DepositService.class);
        FinanceSettingService financeSettingService = (FinanceSettingService) SpringContext.getBean(FinanceSettingService.class);
        FinanceWithdrawService financeWithdrawService = (FinanceWithdrawService) SpringContext.getBean(FinanceWithdrawService.class);
        SysConfigService sysConfigService = (SysConfigService) SpringContext.getBean(SysConfigService.class);

        FinanceSetting financeSetting = (FinanceSetting) financeSettingService.list().get(0);
        if (financeSetting.getDepositAuto().intValue() == 1) {
            Deposit deposit = depositService.findToDoing();

            FinanceWithdraw financeWithdraw = financeWithdrawService.findByAmount(deposit.getAmount(), Integer.valueOf(0));
            if (financeWithdraw != null) {
                WithdrawApi withdrawApi = WithdrawApi.getInstance(financeWithdraw.getClassKey());
                if ((!StrUtils.hasEmpty(new Object[]{deposit.getBankCode()})) && (withdrawApi.containsBank(deposit.getBankCode().toUpperCase()))) {
                    WithdrawApiParam param = new WithdrawApiParam();
                    param.setAccount(deposit.getAccount());
                    param.setAmount(deposit.getAmount().setScale(2, 4).toEngineeringString());
                    param.setBank(deposit.getBankCode().toUpperCase());
                    param.setCardName(deposit.getNiceName());
                    param.setCardNum(deposit.getCard());
                    param.setIssueBankName(deposit.getBankName());
                    param.setIssueBankAddress(deposit.getAddress());
                    param.setMemo("");
                    param.setMerchantCode(financeWithdraw.getMerchantCode());
                    param.setOrderId(deposit.getId());
                    param.setEmail(financeWithdraw.getEmail());
                    param.setPublicKey(financeWithdraw.getPublicKey());
                    param.setShopUrl(financeWithdraw.getShopUrl());
                    param.setWithdrawUrl(financeWithdraw.getApiUrl());

                    Map<String, String> postMap = withdrawApi.getPostMap(param, financeWithdraw.getSign());
                    postMap.put("WithdrawUrl", param.getWithdrawUrl());

                    String json = "{}";
                    try {
                        json = HttpUtils.postString(param.getShopUrl() + "/" + sysConfigService.find("PROJECT_NAME").getVal() + "/" + financeWithdraw.getClassKey() + "/withdrawal",
                                postMap);
                        logger.info(param + json + "deposit auto done");
                        if (withdrawApi.checkResult(json)) {
                            deposit.setOperator(financeWithdraw.getAutoOperator());
                            deposit.setRemark("AUTO DOING");
                            depositService.updateByDoing(deposit);
                        }
                    } catch (Exception e) {
                        logger.error(param + json + "deposit auto exception:" + e.getMessage(), e);
                    }
                }
            }
        }
    }
}
