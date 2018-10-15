package com.hs3.service.lotts;

import com.hs3.dao.finance.RechargeLowerDao;
import com.hs3.dao.lotts.BetBackupDao;
import com.hs3.dao.lotts.BetDao;
import com.hs3.dao.lotts.SettlementDao;
import com.hs3.dao.lotts.TraceDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.AmountChange;
import com.hs3.entity.lotts.Bet;
import com.hs3.entity.lotts.Trace;
import com.hs3.entity.users.User;
import com.hs3.exceptions.UnLogException;
import com.hs3.lotts.LotteryUtils;
import com.hs3.models.lotts.TraceCancelTotal;
import com.hs3.models.lotts.TraceModel;
import com.hs3.models.lotts.TraceOrder;
import com.hs3.utils.IdBuilder;
import com.hs3.web.auth.ThreadLog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("traceService")
public class TraceService {
    private static final Logger logger = LoggerFactory.getLogger(TraceService.class);
    @Autowired
    private TraceDao traceDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BetDao betDao;
    @Autowired
    private SettlementDao settlementDao;
    @Autowired
    private BetService betService;
    @Autowired
    private LotterySaleTimeService lotterySaleTimeService;
    @Autowired
    private LotteryLoseWinService lotteryLoseWinService;
    @Autowired
    private BetBackupDao backupDao;
    @Autowired
    private RechargeLowerDao rechargeLowerDao;

    public void save(Trace trace) {
        this.traceDao.save(trace);
    }

    public List<Trace> list(Page p, TraceModel m) {
        return this.traceDao.list(p, m);
    }

    public void updateTraceAmountAndNum(String traceId, TraceCancelTotal traceCancelTotal, Integer preStatus, Integer sign) {
        if (traceCancelTotal != null) {
            Trace trace = this.traceDao.find(traceId);
            if ((trace != null) &&
                    (preStatus == 0) && (sign == 1)) {
                this.traceDao.updateTraceAmount(traceId, traceCancelTotal.getBigDecimal().multiply(new BigDecimal("-1")), 0);
                this.traceDao.updateTraceNum(traceId, -traceCancelTotal.getNumInteger(), 0);
            }
            this.traceDao.updateTraceAmount(traceId, traceCancelTotal.getBigDecimal(), sign);
            this.traceDao.updateTraceNum(traceId, traceCancelTotal.getNumInteger(), sign);
            if (trace != null) {
                this.traceDao.updateStatusEx(traceId);
            }
        }
    }

    public Map<String, Object> saveBetTraceOrder(List<Bet> betList, List<TraceOrder> traceList, String curSeason, BigDecimal traceAllPrice, Date curOpenTime) {
        String account = betList.get(0).getAccount();
        User user = this.userDao.findByAccountMaster(account);
        ThreadLog.addKey("得到用户锁后的时间:" + System.currentTimeMillis());
        if (user.getAmount().compareTo(traceAllPrice) < 0) {
            throw new UnLogException("余额不足");
        }
        BigDecimal allAmout = new BigDecimal("0");
        BigDecimal curAmout = new BigDecimal("0");
        BigDecimal bonus = new BigDecimal("0");
        boolean isCurSeason = false;
        boolean havaCurSeason = false;
        Map<String, Object> map = new HashMap<>();
        List<Bet> firstBets = new ArrayList<>();
        List<Trace> traces = new ArrayList<>();
        List<AmountChange> amountChanges = new ArrayList<>();


        Trace trace = new Trace();
        for (Bet bet : betList) {
            bonus = bet.getTheoreticalBonus();
            allAmout = new BigDecimal("0");

            amountChanges.clear();
            for (TraceOrder traceOrder : traceList) {
                bet.setSeasonId(traceOrder.getSeasonId());
                if (traceOrder.getSeasonId().equals(curSeason)) {
                    bet.setStatus(0);
                    isCurSeason = true;
                    havaCurSeason = true;
                    this.lotteryLoseWinService.addLoseAndWin(bet.clone());
                } else {
                    bet.setStatus(3);
                    isCurSeason = false;
                }
                bet.setSeasonId(traceOrder.getSeasonId());
                bet.setPrice(traceOrder.getPrice());
                bet.setTheoreticalBonus(bonus.multiply(new BigDecimal(traceOrder.getPrice())));
                bet.setAmount(new BigDecimal(bet.getBetCount()).multiply(bet.getUnit()).multiply(new BigDecimal(bet.getPrice())));
                bet.setId(IdBuilder.CreateId(bet.getLotteryId(), "D"));
                bet.setHashCode(LotteryUtils.hashCode(bet));
                this.betDao.save(bet);


                allAmout = bet.getAmount().add(allAmout);
                if (isCurSeason) {
                    curAmout = bet.getAmount();
                    AmountChange amountChangeDeduct = new AmountChange();
                    amountChangeDeduct.setBetId(bet.getId());
                    amountChangeDeduct.setLotteryId(bet.getLotteryId());
                    amountChangeDeduct.setSeasonId(traceOrder.getSeasonId());
                    amountChangeDeduct.setChangeUser(bet.getAccount());
                    amountChangeDeduct.setChangeSource(bet.getAccount());
                    amountChangeDeduct.setChangeAmount(bet.getAmount().multiply(new BigDecimal("-1")));
                    amountChangeDeduct.setChangeTime(new Date());
                    amountChangeDeduct.setPlayerId(bet.getPlayerId());
                    amountChangeDeduct.setPlayName(bet.getPlayName());
                    amountChangeDeduct.setTest(user.getTest());
                    amountChangeDeduct.setGroupName(bet.getGroupName());
                    amountChangeDeduct.setLotteryName(bet.getLotteryName());
                    amountChangeDeduct.setAccountChangeTypeId(1);
                    amountChangeDeduct.setUnit(bet.getUnit());
                    amountChanges.add(amountChangeDeduct);

                    AmountChange amountChangeBack = new AmountChange();
                    amountChangeBack.setBetId(bet.getTraceId());
                    amountChangeBack.setAccountChangeTypeId(6);
                    amountChangeBack.setChangeAmount(bet.getAmount());
                    amountChangeBack.setBalance(user.getAmount().add(bet.getAmount()));

                    amountChangeBack.setLotteryId(bet.getLotteryId());
                    amountChangeBack.setSeasonId(traceOrder.getSeasonId());
                    amountChangeBack.setChangeUser(bet.getAccount());
                    amountChangeBack.setChangeSource(bet.getAccount());
                    amountChangeBack.setChangeTime(new Date());
                    amountChangeBack.setPlayerId(bet.getPlayerId());
                    amountChangeBack.setPlayName(bet.getPlayName());
                    amountChangeBack.setTest(user.getTest());
                    amountChangeBack.setGroupName(bet.getGroupName());
                    amountChangeBack.setLotteryName(bet.getLotteryName());
                    amountChangeBack.setUnit(bet.getUnit());
                    amountChanges.add(amountChangeBack);

                    Bet b = bet.clone();
                    b.setHashCode(null);
                    if (b.getContent().length() > 20) {
                        b.setContent(b.getContent().substring(0, 20) + "...");
                    }
                    firstBets.add(b);
                }
            }
            AmountChange amountChangeTrace = new AmountChange();
            amountChangeTrace.setBetId(betList.get(0).getTraceId());
            amountChangeTrace.setLotteryId(betList.get(0).getLotteryId());
            amountChangeTrace.setSeasonId(curSeason);
            amountChangeTrace.setChangeUser(account);
            amountChangeTrace.setChangeSource(account);
            amountChangeTrace.setChangeAmount(allAmout.multiply(new BigDecimal("-1")));
            amountChangeTrace.setChangeTime(new Date());
            amountChangeTrace.setPlayerId(bet.getPlayerId());
            amountChangeTrace.setPlayName(bet.getPlayName());
            amountChangeTrace.setTest(user.getTest());
            amountChangeTrace.setGroupName(bet.getGroupName());
            amountChangeTrace.setLotteryName(bet.getLotteryName());
            amountChangeTrace.setUnit(bet.getUnit());
            amountChangeTrace.setAccountChangeTypeId(5);
            amountChanges.add(amountChangeTrace);
            for (int m = amountChanges.size() - 1; m >= 0; m--) {
                user = this.userDao.findByAccountMaster(bet.getAccount());
                amountChanges.get(m).setBalance(user.getAmount().add(amountChanges.get(m).getChangeAmount()));
                this.userDao.updateAmount(bet.getAccount(), amountChanges.get(m).getChangeAmount());
                this.settlementDao.save(amountChanges.get(m));
            }
            int traceNum = traceList.size();
            trace.setId(bet.getTraceId());
            trace.setAccount(bet.getAccount());
            trace.setLotteryId(bet.getLotteryId());
            trace.setTest(bet.getTest());
            trace.setCreateTime(bet.getCreateTime());
            trace.setLotteryName(bet.getLotteryName());
            trace.setTraceNum(traceNum);
            if (curAmout.compareTo(new BigDecimal("0")) > 0) {
                trace.setFinishTraceNum(1);
            } else {
                trace.setFinishTraceNum(0);
            }
            trace.setCancelTraceNum(0);
            trace.setTraceAmount(allAmout);
            trace.setFinishTraceAmount(curAmout);
            trace.setCancelTraceAmount(new BigDecimal("0"));
            if (traceNum == 1) {
                trace.setStatus(1);
            } else {
                trace.setStatus(0);
            }
            trace.setWinAmount(new BigDecimal("0"));
            trace.setIsWinStop(betList.get(0).getTraceWinStop());
            trace.setStartSeason(traceList.get(0).getSeasonId());
            save(trace);
            try {
                traces.add((Trace) trace.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        map.put("betList", firstBets);
        map.put("traceList", traces);
        if (!"1".equals(user.getIsBet())) {
            this.userDao.setBet(user.getAccount());
        }
        ThreadLog.addKey("lock end:" + System.currentTimeMillis());
        if ((havaCurSeason) &&
                (new Date().getTime() >= curOpenTime.getTime())) {
            throw new UnLogException("本期已停售");
        }
        return map;
    }

    public Integer updateTrace(String lotteryId, String seasonId) {
        int allStatus = 0;
        List<Bet> betList = this.betDao.getAllBet(seasonId, 3, lotteryId);
        for (Bet bet : betList) {
            allStatus += updateTrace(bet);
        }
        return allStatus > 0 ? 2 : 0;
    }

    public Integer updateTraceByUser(String lotteryId, String seasonId, String account) {
        int allStatus = 0;
        try {
            List<Bet> betList = this.betDao.getAllBetByUser(seasonId, 3, lotteryId, account);
            for (Bet bet : betList) {
                allStatus += updateTrace(bet);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            try {
                throw e;
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return allStatus;
    }

    public Integer updateTrace(Bet bet) {
        bet.setStatus(0);
        if (this.betDao.updateForTrace(0, 3, bet.getId(), new Date(), bet.getLastTime()) > 0) {
            User user = this.userDao.findByAccountMaster(bet.getAccount());


            TraceCancelTotal traceCancelTotal = new TraceCancelTotal();
            AmountChange amountChange = new AmountChange();
            traceCancelTotal.setBigDecimal(bet.getAmount());
            traceCancelTotal.setNumInteger(1);
            updateTraceAmountAndNum(bet.getTraceId(), traceCancelTotal, bet.getStatus(), 0);

            amountChange.setBetId(bet.getTraceId());
            amountChange.setLotteryId(bet.getLotteryId());
            amountChange.setSeasonId(bet.getSeasonId());
            amountChange.setChangeUser(bet.getAccount());
            amountChange.setChangeSource(bet.getAccount());
            amountChange.setChangeAmount(bet.getAmount());
            amountChange.setChangeTime(new Date());
            amountChange.setBalance(user.getAmount().add(bet.getAmount()));
            amountChange.setPlayerId(bet.getPlayerId());
            amountChange.setPlayName(bet.getPlayName());
            amountChange.setTest(user.getTest());
            amountChange.setGroupName(bet.getGroupName());
            amountChange.setLotteryName(bet.getLotteryName());
            amountChange.setAccountChangeTypeId(6);
            amountChange.setUnit(bet.getUnit());

            this.settlementDao.save(amountChange);


            amountChange.setBetId(bet.getId());
            amountChange.setAccountChangeTypeId(1);
            amountChange.setChangeAmount(bet.getAmount().multiply(new BigDecimal("-1")));

            amountChange.setBalance(user.getAmount());
            this.settlementDao.save(amountChange);


            this.lotteryLoseWinService.addLoseAndWin(bet.clone());
            return 0;
        }
        logger.info(bet.getAccount() + " 追号-无法更新:" + bet.getId());
        return 2;
    }

    public List<Trace> listByAccount(String account, int count) {
        return this.traceDao.listByAccount(account, count);
    }

    public List<Trace> adminList(Page p, TraceModel m) {
        return this.traceDao.adminList(p, m);
    }

    public void updateStatus(String traceId, Integer status, Integer repStatus) {
        this.traceDao.updateStatus(traceId, status, repStatus);
    }

    public Trace find(String id) {
        return this.traceDao.find(id);
    }

    public void updateWinAmount(String traceId, BigDecimal winAmount) {
        this.traceDao.updateWinAmount(traceId, winAmount);
    }

    public int deleteTraceData(List<Trace> traceList, boolean needBackup) {
        int count = 0;
        for (Trace trace : traceList) {
            count += deleteTraceData(trace.getId(), needBackup);
        }
        return count;
    }

    public int deleteTraceData(String id, boolean needBackup) {
        int i = this.traceDao.deleteByClear(id, needBackup);
        if (i == 1) {
            this.betDao.deleteByTraceId(id, needBackup);
        }
        return i;
    }

    public List<Trace> listClearTrace(Date createTime, int start, int size) {
        return this.traceDao.listClearTrace(createTime, start, size);
    }

    public int countClearTrace(Date createTime) {
        return this.traceDao.countClearTrace(createTime);
    }
}
