package com.hs3.service.lotts;

import com.hs3.web.utils.SpringContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

public final class BetSendEmail {
    private static ExecutorService executorService = Executors.newFixedThreadPool(30);
    private static final Logger logger = LoggerFactory.getLogger(BetSendEmail.class);

    public static void send(String lotteryId, String seasonId) {
        executorService.execute(new SendThread(lotteryId, seasonId));
    }

    private static class SendThread
            extends Thread {
        private BetService betService = (BetService) SpringContext.getBean("betService");
        private String lotteryId;
        private String seasonId;

        public SendThread(String lotteryId, String seasonId) {
            this.lotteryId = lotteryId;
            this.seasonId = seasonId;
        }

        public void run() {
            try {
                this.betService.SendBetEmail(this.seasonId, this.lotteryId);
            } catch (Exception e) {
                BetSendEmail.logger.error("投注发邮件异常" + e.getMessage(), e);
            }
        }
    }
}
