package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetDao;
import com.hs3.entity.lotts.Bet;
import com.hs3.lotts.LotteryBase;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

public class SettlementThread
        extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(SettlementThread.class);
    private static final ThreadLocal<Integer> BET_NUMS = new ThreadLocal<>();
    private SettlementService settlementService;
    private BetDao betDao;
    private List<String> accounts;
    private LotteryBase lb;
    private String lotteryId;
    private String seasonId;
    private String openNum;
    private String nextSeasonId;
    private boolean hasError = false;
    private int betNum = 0;
    private CountDownLatch countDownLatch;

    public static void incNums(int n) {
        Integer num = (Integer) BET_NUMS.get();
        if (num == null) {
            num = n;
        } else {
            num = num + n;
        }
        BET_NUMS.set(num);
    }

    public static int getIncNums() {
        Integer n = (Integer) BET_NUMS.get();
        if (n == null) {
            n = 0;
        }
        BET_NUMS.remove();
        return n;
    }

    public int getBetNum() {
        return this.betNum;
    }

    public boolean isError() {
        return this.hasError;
    }

    public SettlementThread(int index, CountDownLatch cdl, SettlementService settlementService, BetDao betDao, List<String> accounts, LotteryBase lb, String lotteryId, String seasonId, String openNum, String nextSeasonId) {
        this.settlementService = settlementService;
        this.betDao = betDao;
        this.accounts = accounts;
        this.lb = lb;
        this.lotteryId = lotteryId;
        this.seasonId = seasonId;
        this.openNum = openNum;
        this.nextSeasonId = nextSeasonId;
        this.countDownLatch = cdl;
        setName(lotteryId + "_" + seasonId + "_" + index);
    }

    public void run() {
        /**jd-gui
         String info = this.lotteryId + " " + this.seasonId;
         for (String account : this.accounts) {
         try
         {
         this.settlementService.saveSettlementByUser(this.lb, this.lotteryId, this.seasonId, this.openNum, account, this.nextSeasonId);
         }
         catch (Exception e)
         {
         this.hasError = true;
         logger.error(account + "-" + info + "结算异常-" + e.getMessage(), e);
         try
         {
         List<Bet> TraceBetList = this.betDao.getAllBetByUser(this.seasonId, Integer.valueOf(3), this.lotteryId, account);
         Iterator localIterator3;
         for (Iterator localIterator2 = TraceBetList.iterator(); localIterator2.hasNext(); localIterator3.hasNext())
         {
         Bet tracebBet = (Bet)localIterator2.next();
         List<Bet> betLst = this.betDao.getAllTraceCancelBet(tracebBet.getTraceId());
         localIterator3 = betLst.iterator(); continue;Bet bet = (Bet)localIterator3.next();
         bet.setStatus(Integer.valueOf(8));
         this.betDao.update(Integer.valueOf(8), Integer.valueOf(3), bet.getId(), bet.getLastTime());
         }
         }
         catch (Exception e2)
         {
         logger.error("追号改成8异常:" + e.getMessage(), e);
         }
         }
         }
         this.betNum = getIncNums();
         this.countDownLatch.countDown();*/

        String info = (new StringBuilder(String.valueOf(lotteryId))).append(" ").append(seasonId).toString();
        for (String account: accounts) {
            try {
                settlementService.saveSettlementByUser(lb, lotteryId, seasonId, openNum, account, nextSeasonId);
            } catch (Exception e) {
                hasError = true;
                logger.error((new StringBuilder(String.valueOf(account))).append("-").append(info).append("结算异常-").append(e.getMessage()).toString(), e);
                try {
                    List<Bet> TraceBetList = betDao.getAllBetByUser(seasonId, 3, lotteryId, account);
                    for (Bet tracebBet:TraceBetList) {
                        List<Bet> betLst = betDao.getAllTraceCancelBet(tracebBet.getTraceId());
                        for (Bet bet: betLst) {
                            betDao.update(8, 3, bet.getId(), bet.getLastTime());
                        }

                    }

                } catch (Exception e2) {
                    logger.error((new StringBuilder("追号改成8异常:")).append(e.getMessage()).toString(), e);
                }
            }
        }

        betNum = getIncNums();
        countDownLatch.countDown();

    }
}
