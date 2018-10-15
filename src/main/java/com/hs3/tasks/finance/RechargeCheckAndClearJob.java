package com.hs3.tasks.finance;

import com.hs3.commons.RechargeStatus;
import com.hs3.entity.finance.Recharge;
import com.hs3.service.finance.RechargeService;
import com.hs3.web.utils.SpringContext;
import com.pays.PayApi;
import com.pays.PayApiFactory;
import com.pays.jmoney.JmoneyPayApi;
import org.codehaus.jackson.node.ObjectNode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RechargeCheckAndClearJob
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(RechargeCheckAndClearJob.class);
    private static RechargeService rechargeService = (RechargeService) SpringContext.getBean(RechargeService.class);
    private static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

    public void execute(JobExecutionContext arg0) {
        try {
            logger.info("充值记录检查清除任务启动!!");
            Calendar date = Calendar.getInstance();
            long t = date.getTimeInMillis();
            Date newDate = new Date(t - (30 * ONE_MINUTE_IN_MILLIS));
            List<Recharge> rechargeList = rechargeService.listUnProcess(newDate);
            if (null != rechargeList && rechargeList.size() > 0)
                for (Recharge recharge : rechargeList) {
                    String classKey = recharge.getClassKey();
                    PayApi payApi = PayApiFactory.getInstance(classKey);
                    //如果沒找到第三方廠商 更新為已過期
                    if (null == payApi) {
                        rechargeService.updateStatus(recharge.getId(), RechargeStatus.expire.getStatus());
                        continue;
                    }
                    //jmoney
                    if (payApi instanceof JmoneyPayApi) {
                        JmoneyPayApi jmoneyPayApi = (JmoneyPayApi) payApi;
                        String url = jmoneyPayApi.getCHECKURL();
                        if (null == url) {
                            rechargeService.updateStatus(recharge.getId(), RechargeStatus.expire.getStatus());
                            continue;
                        }
                        ObjectNode result = jmoneyPayApi.getCheckResult(recharge, url);

                        if (null != result && null != result.get("result")) {
                            //成功
                            if (result.get("result").asText().equals("1")) {
                                rechargeService.updateStatus(recharge.getId(), RechargeStatus.completed.getStatus());
                            }
                            //第三方發送此張單已失敗, 商戶系統改為已撤銷
                            else if (result.get("result").asText().equals("2")) {
                                rechargeService.updateStatus(recharge.getId(), RechargeStatus.cancel.getStatus());
                            }
                            //已過期
                            else if (result.get("result").asText().equals("0")) {
                                rechargeService.updateStatus(recharge.getId(), RechargeStatus.expire.getStatus());
                            } else {
                                rechargeService.updateStatus(recharge.getId(), RechargeStatus.expire.getStatus());
                            }
                        } else
                            rechargeService.updateStatus(recharge.getId(), RechargeStatus.expire.getStatus());
                    }
                }
        } catch (Exception e) {
            logger.error("充值记录检查清除任务异常:" + e.getMessage(), e);
        }
    }
}
