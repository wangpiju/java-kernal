package com.hs3.service.quartz;

import com.hs3.commons.OpenStatus;
import com.hs3.commons.PlanStatus;
import com.hs3.commons.SettleStatus;
import com.hs3.dao.lotts.BetDao;
import com.hs3.entity.lotts.Bet;
import com.hs3.entity.lotts.Lottery;
import com.hs3.entity.lotts.LotterySeason;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryFactory;
import com.hs3.service.lotts.*;
import com.hs3.utils.ListUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service("taskLogic")
public class TaskLogic {
    private static final Logger logger = LoggerFactory.getLogger(TaskLogic.class);
    @Autowired
    private BetService betService;
    @Autowired
    private BetDao betDao;
    @Autowired
    private TraceService traceService;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private LotterySaleTimeService lotterySaleTimeService;
    @Autowired
    private LotteryService lotteryService;

    /**
     * 开奖核心
     * @param ls
     */
    public void openTaskByUser(LotterySeason ls) {
        String lotteryId = ls.getLotteryId();
        String seasonId = ls.getSeasonId();

        String info = lotteryId + " " + seasonId;

        Lottery lottery = this.lotteryService.find(lotteryId);
        LotteryBase lb = LotteryFactory.getInstance(lottery.getGroupName());
        String openNum = ListUtils.toString(lb.getSeasonOpen(ls).getNums());

        this.lotterySaleTimeService.updateOpenStatus(seasonId, lotteryId, OpenStatus.completed.getStatus());
        this.settlementService.updateBetChange(lb, lotteryId, seasonId, openNum, new Integer[]{0, 6});
        long sss = System.currentTimeMillis();
        logger.info(info + " 开始结算");

        this.lotterySaleTimeService.updateSettleStatus(seasonId, lotteryId, SettleStatus.non_execution.getStatus(), SettleStatus.executing.getStatus());
        this.lotterySaleTimeService.updatePlanStatus(seasonId, lotteryId, PlanStatus.non_execution.getStatus(), PlanStatus.executing.getStatus());
        List<String> accounts = this.betDao.getDistinctUser(lotteryId, seasonId, new Integer[]{0, 6});
        boolean hasError = false;
        if (accounts != null) {
            String nextSeasonId = this.lotterySaleTimeService.getNextByLotteryId(lotteryId, seasonId, ls.getOpenTime()).getSeasonId();
            logger.info(info + " 结算用户数:" + accounts.size());
            for (String account : accounts) {
                try {
                    //定时开奖后需要进行相关的金额变更
                    settlementService.saveSettlementByUser(lb, lotteryId, seasonId, openNum, account, nextSeasonId);
                } catch (Exception e) {
                    hasError = true;
                    logger.error((new StringBuilder(String.valueOf(account))).append("-").append(info).append("结算异常-").append(e.getMessage()).toString(), e);
                    try {
                        List<Bet> TraceBetList = betDao.getAllBetByUser(seasonId, 3, lotteryId, account);
                        for (Bet tracebBet : TraceBetList) {
                            List<Bet> betLst = betDao.getAllTraceCancelBet(tracebBet.getTraceId());
                            for (Bet bet: betLst) {
                                betDao.update(8, 3, bet.getId(), bet.getLastTime());
                            }
                        }
                    } catch (Exception e2) {
                        logger.error((new StringBuilder("追号改成8异常:")).append(e2.getMessage()).toString(), e2);
                    }
                }
            }
            int betNum = SettlementThread.getIncNums();
            logger.info(info + " 结束结算:耗时:" + (System.currentTimeMillis() - sss) + ",结算注单：" + betNum);
            if (!hasError) {
                this.traceService.updateTrace(lotteryId, nextSeasonId);
                this.lotterySaleTimeService.updateSettleStatus(seasonId, lotteryId, null, SettleStatus.completed.getStatus());
                this.lotterySaleTimeService.updatePlanStatus(seasonId, lotteryId, null, PlanStatus.completed.getStatus());
            } else {
                this.lotterySaleTimeService.updateSettleStatus(seasonId, lotteryId, null, SettleStatus.executed.getStatus());
                this.lotterySaleTimeService.updatePlanStatus(seasonId, lotteryId, null, PlanStatus.executed.getStatus());
                throw new BaseCheckException(info + " 结算异常");
            }
        } else {
            logger.warn(String.format("--> %s 没有投注用户", info));
        }

    }

    public void saveAdvanceOpenTask(LotterySeason lotterySeason) {
        logger.info(lotterySeason.getLotteryId() + " " + lotterySeason.getSeasonId() + " 开始提前开奖-撤单");
        this.betService.saveAdvanceCancelOrder(lotterySeason.getLotteryId(), lotterySeason.getSeasonId(), "");
    }

    public void saveNoOpenTask(String lotteryId, String seasonId) {
        logger.info((new StringBuilder(String.valueOf(lotteryId))).append(" ").append(seasonId).append("开始未开奖-状态改成8").toString());
        List<Bet> traceBetList = betDao.getAllIsTraceBet(lotteryId, seasonId, 1);
        for (Bet tracebBet : traceBetList) {
            List betLst = betDao.getAllTraceCancelBet(tracebBet.getTraceId());
            Bet bet;
            for (Iterator iterator1 = betLst.iterator(); iterator1.hasNext(); betDao.update(8, 3, bet.getId(), bet.getLastTime())) {
                bet = (Bet) iterator1.next();
                bet.setStatus(8);
            }
        }
    }

    public void saveRepeatOpenTask(LotterySeason lotterySeason) {
        logger.info(lotterySeason.getLotteryId() + " " + lotterySeason.getSeasonId() + " 开始重复开奖-人工处理");

        this.lotterySaleTimeService.updateOpenStatus(lotterySeason.getSeasonId(), lotterySeason.getLotteryId(), OpenStatus.lottery_repeat.getStatus());
    }
}
