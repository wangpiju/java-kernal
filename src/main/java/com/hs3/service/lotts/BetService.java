package com.hs3.service.lotts;

import com.hs3.commons.BetStatus;
import com.hs3.commons.SettleStatus;
import com.hs3.dao.finance.RechargeLowerDao;
import com.hs3.dao.lotts.*;
import com.hs3.dao.sys.LogAllDao;
import com.hs3.dao.sys.ResettleMailDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.*;
import com.hs3.entity.sys.BaseReturnPojo;
import com.hs3.entity.sys.LogAll;
import com.hs3.entity.sys.ResettleMail;
import com.hs3.entity.users.User;
import com.hs3.exceptions.UnLogException;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryFactory;
import com.hs3.lotts.PlayerBase;
import com.hs3.models.CommonModel;
import com.hs3.models.Jsoner;
import com.hs3.models.lotts.AdminBetDetail;
import com.hs3.models.lotts.TraceCancelTotal;
import com.hs3.utils.*;
import com.hs3.web.auth.ThreadLog;
import nl.bitwalker.useragentutils.*;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("betService")
public class BetService {
    private static final Logger logger = LoggerFactory.getLogger(BetService.class);
    private static final BigDecimal TWO = new BigDecimal("2");
    private static final Integer TIGER_CONFIG_ID = Integer.valueOf(1);
    @Autowired
    private BetDao betDao;
    @Autowired
    private SettlementDao settlementDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LotterySaleTimeDao lotterySaleTimeDao;
    @Autowired
    private LotterySeasonDao lotterySeasonDao;
    @Autowired
    private TraceService traceService;
    @Autowired
    private BonusGroupService bonusGroupService;
    @Autowired
    private BonusGroupDetailsService bonusGroupDetailsService;
    @Autowired
    private LotteryLoseWinService lotteryLoseWinService;
    @Autowired
    private BetBackupDao betBackupDao;
    @Autowired
    private TempAmountChangeDao tempAmountChangeDao;
    @Autowired
    private RechargeLowerDao rechargeLowerDao;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private BetTigerDao betTigerDao;
    @Autowired
    private BetTigerConfigDao betTigerConfigDao;
    @Autowired
    private ResettleMailDao resettleMailDao;
    @Autowired
    private LogAllDao logAllDao;

    public List<Bet> getAllBet(String seasonId, String lotteryId, Integer status) {
        return this.betDao.getAllBet(seasonId, status, lotteryId);
    }

    public List<Bet> getBetByUser(String seasonId, String lotteryId, Integer[] status, String account) {
        return this.betDao.getBetByUser(seasonId, lotteryId, status, account);
    }

    public int update(Bet bet, Date preTime) {
        return this.betDao.update(bet, preTime);
    }

    public List<Bet> getAllTraceCancelBet(String TraceId) {
        return this.betDao.getAllTraceCancelBet(TraceId);
    }

    public List<Bet> getAllTraceCanCancel(String TraceId) {
        return this.betDao.getAllTraceCanCancel(TraceId);
    }

    public List<Bet> saveBetTigerOrder(String account, List<Bet> betList, BigDecimal allPrice, Date curOpenTime) {
        Date now = new Date();


        String openNumsByParam = null;
        boolean isTestUser = this.userDao.findByAccount(account).getTest() == 1;
        if (isTestUser) {
            openNumsByParam = genTigerOpenNumsByRandom();
        } else {
            BetTigerConfig betTigerConfig = this.betTigerConfigDao.findLock(TIGER_CONFIG_ID);
            if (betTigerConfig.getStatus() == 1) {
                openNumsByParam = genTigerOpenNumsByRandom();
            } else {
                long begin = System.currentTimeMillis();
                openNumsByParam = genTigerOpenNums(account, betList, allPrice, betTigerConfig, now);
                ThreadLog.addKey("开奖耗时：" + (System.currentTimeMillis() - begin));
            }
        }
        BetTiger betTiger = new BetTiger();
        betTiger.setAccount(account);
        betTiger.setCreateTime(now);
        betTiger.setOpenNum(openNumsByParam);
        this.betTigerDao.save(betTiger);


        long begin = System.currentTimeMillis();
        saveBetOrder(betList, allPrice, curOpenTime, false);
        ThreadLog.addKey("投注耗时：" + (System.currentTimeMillis() - begin));
        begin = System.currentTimeMillis();


        BigDecimal winAmount = BigDecimal.ZERO;
        BigDecimal betAmount = BigDecimal.ZERO;
        LotteryBase lb = LotteryFactory.getInstance("时时彩");
        List<Bet> list = new ArrayList<>();
        for (Bet bet : betList) {
            this.settlementService.updateBetAndAmount(lb, bet, openNumsByParam, "");
            if (!isTestUser) {
                winAmount = bet.getWin().add(winAmount);
                betAmount = bet.getAmount().add(betAmount);
            }
            hideBet(bet);
            list.add(bet);
        }
        if (!isTestUser) {
            this.betTigerConfigDao.updateAmount(TIGER_CONFIG_ID, winAmount, betAmount);
        }
        ThreadLog.addKey("结算耗时：" + (System.currentTimeMillis() - begin));
        return list;
    }

    private String genTigerOpenNums(String account, List<Bet> betList, BigDecimal allPrice, BetTigerConfig betTigerConfig, Date now) {

        /**jd-gui
         List<Integer> list = null;

         long beginTime = System.currentTimeMillis();

         BigDecimal ratio = betTigerConfig.getRatio();
         BigDecimal deviation = betTigerConfig.getDeviation();
         BigDecimal winAmount = betTigerConfig.getWinAmount();
         BigDecimal betAmount = betTigerConfig.getBetAmount().add(allPrice);

         Random ran = NumUtils.getRandomInstance();
         List<Integer> list_i = getNums(ran);
         List<Integer> list_j = getNums(ran);
         List<Integer> list_k = getNums(ran);
         List<Integer> list_m = getNums(ran);
         List<Integer> list_n = getNums(ran);

         BigDecimal lastWinAndBetAmount = null;
         BigDecimal lastWinAmount = null;

         LotteryBase lb = LotteryFactory.getInstance(((Bet)betList.get(0)).getGroupName());

         Integer[] openNums = null;
         Iterator localIterator2;
         label687:
         label707:
         for (Iterator localIterator1 = list_i.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         int i = ((Integer)localIterator1.next());
         openNums = new Integer[5];
         openNums[0] = Integer.valueOf(i);
         BigDecimal betWin = getBetWin(lb, betList, Arrays.asList(openNums));
         if ((betWin.compareTo(BigDecimal.ZERO) > 0) && (winAmount.add(betWin).divide(betAmount, 2, 1).compareTo(ratio) > 0)) {
         break label707;
         }
         localIterator2 = list_j.iterator(); continue;int j = ((Integer)localIterator2.next());
         openNums[1] = Integer.valueOf(j);
         betWin = getBetWin(lb, betList, Arrays.asList(openNums));
         if ((betWin.compareTo(BigDecimal.ZERO) <= 0) || (winAmount.add(betWin).divide(betAmount, 2, 1).compareTo(ratio) <= 0))
         {
         Iterator localIterator4;
         for (Iterator localIterator3 = list_k.iterator(); localIterator3.hasNext(); localIterator4.hasNext())
         {
         int k = ((Integer)localIterator3.next());
         openNums[2] = Integer.valueOf(k);
         betWin = getBetWin(lb, betList, Arrays.asList(openNums));
         if ((betWin.compareTo(BigDecimal.ZERO) > 0) && (winAmount.add(betWin).divide(betAmount, 2, 1).compareTo(ratio) > 0)) {
         break label687;
         }
         localIterator4 = list_m.iterator(); continue;int m = ((Integer)localIterator4.next());
         openNums[3] = Integer.valueOf(m);
         betWin = getBetWin(lb, betList, Arrays.asList(openNums));
         if ((betWin.compareTo(BigDecimal.ZERO) <= 0) || (winAmount.add(betWin).divide(betAmount, 2, 1).compareTo(ratio) <= 0)) {
         for (Iterator localIterator5 = list_n.iterator(); localIterator5.hasNext();)
         {
         int n = ((Integer)localIterator5.next());
         openNums[4] = Integer.valueOf(n);
         betWin = getBetWin(lb, betList, Arrays.asList(openNums));
         BigDecimal winAndBetAmount = winAmount.add(betWin).divide(betAmount, 2, 1);
         if ((betWin.compareTo(BigDecimal.ZERO) <= 0) || (winAndBetAmount.compareTo(ratio) <= 0)) {
         if ((lastWinAndBetAmount == null) || (winAndBetAmount.compareTo(lastWinAndBetAmount) > 0))
         {
         lastWinAndBetAmount = winAndBetAmount;
         lastWinAmount = betWin;
         list = new ArrayList();
         list.add(Integer.valueOf(i));
         list.add(Integer.valueOf(j));
         list.add(Integer.valueOf(k));
         list.add(Integer.valueOf(m));
         list.add(Integer.valueOf(n));
         if (ratio.subtract(winAndBetAmount).compareTo(deviation) <= 0) {
         break;
         }
         }
         }
         }
         }
         }
         }
         }
         String openNum = null;
         if (list == null)
         {
         openNum = genTigerOpenNumsByRandom();
         logger.info(now + " " + account + " bet tiger(" + (System.currentTimeMillis() - beginTime) + ")(" + ratio + ")(" + deviation + "), lose, gen random:" + openNum);
         }
         else
         {
         openNum = list.get(0) + "," + list.get(1) + "," + list.get(2) + "," + list.get(3) + "," + list.get(4);
         logger.info(now + " " + account + " bet tiger(" + (System.currentTimeMillis() - beginTime) + ")(" + ratio + ")(" + deviation + "), win, gen is:" + openNum + " bet:" + allPrice + "(" + betAmount + ")" + " win:" + lastWinAmount + "(" + winAmount + ") radio:" + lastWinAndBetAmount.multiply(new BigDecimal("100")) + "%");
         }
         return openNum;*/

        List<Integer> list = null;
        long beginTime = System.currentTimeMillis();
        BigDecimal ratio = betTigerConfig.getRatio();
        BigDecimal deviation = betTigerConfig.getDeviation();
        BigDecimal winAmount = betTigerConfig.getWinAmount();
        BigDecimal betAmount = betTigerConfig.getBetAmount().add(allPrice);
        Random ran = NumUtils.getRandomInstance();
        List<Integer> list_i = getNums(ran);
        List<Integer> list_j = getNums(ran);
        List<Integer> list_k = getNums(ran);
        List<Integer> list_m = getNums(ran);
        List<Integer> list_n = getNums(ran);
        BigDecimal lastWinAndBetAmount = null;
        BigDecimal lastWinAmount = null;
        LotteryBase lb = LotteryFactory.getInstance(betList.get(0).getGroupName());
        Integer openNums[] = null;
        Iterator iterator = list_i.iterator();

        label0:

        while (iterator.hasNext()) {
            int i = ((Integer) iterator.next());
            openNums = new Integer[5];
            openNums[0] = i;
            BigDecimal betWin = getBetWin(lb, betList, Arrays.asList(openNums));
            if (betWin.compareTo(BigDecimal.ZERO) > 0 && winAmount.add(betWin).divide(betAmount, 2, 1).compareTo(ratio) > 0)
                continue;
            Iterator iterator1 = list_j.iterator();
            while (iterator1.hasNext()) {
                int j = ((Integer) iterator1.next());
                openNums[1] = j;
                betWin = getBetWin(lb, betList, Arrays.asList(openNums));
                if (betWin.compareTo(BigDecimal.ZERO) > 0 && winAmount.add(betWin).divide(betAmount, 2, 1).compareTo(ratio) > 0)
                    continue;
                Iterator iterator2 = list_k.iterator();
                while (iterator2.hasNext()) {
                    int k = ((Integer) iterator2.next());
                    openNums[2] = Integer.valueOf(k);
                    betWin = getBetWin(lb, betList, Arrays.asList(openNums));
                    if (betWin.compareTo(BigDecimal.ZERO) > 0 && winAmount.add(betWin).divide(betAmount, 2, 1).compareTo(ratio) > 0)
                        continue;
                    Iterator iterator3 = list_m.iterator();
                    while (iterator3.hasNext()) {
                        int m = ((Integer) iterator3.next());
                        openNums[3] = Integer.valueOf(m);
                        betWin = getBetWin(lb, betList, Arrays.asList(openNums));
                        if (betWin.compareTo(BigDecimal.ZERO) > 0 && winAmount.add(betWin).divide(betAmount, 2, 1).compareTo(ratio) > 0)
                            continue;
                        Iterator iterator4 = list_n.iterator();
                        while (iterator4.hasNext()) {
                            int n = ((Integer) iterator4.next());
                            openNums[4] = Integer.valueOf(n);
                            betWin = getBetWin(lb, betList, Arrays.asList(openNums));
                            BigDecimal winAndBetAmount = winAmount.add(betWin).divide(betAmount, 2, 1);
                            if (betWin.compareTo(BigDecimal.ZERO) > 0 && winAndBetAmount.compareTo(ratio) > 0 || lastWinAndBetAmount != null && winAndBetAmount.compareTo(lastWinAndBetAmount) <= 0)
                                continue;
                            lastWinAndBetAmount = winAndBetAmount;
                            lastWinAmount = betWin;
                            list = new ArrayList();
                            list.add(Integer.valueOf(i));
                            list.add(Integer.valueOf(j));
                            list.add(Integer.valueOf(k));
                            list.add(Integer.valueOf(m));
                            list.add(Integer.valueOf(n));
                            if (ratio.subtract(winAndBetAmount).compareTo(deviation) <= 0) break label0;
                        }
                    }
                }
            }
        }

        String openNum = null;
        if (list == null) {
            openNum = genTigerOpenNumsByRandom();
            logger.info((new StringBuilder()).append(now).append(" ").append(account).append(" bet tiger(").append(System.currentTimeMillis() - beginTime).append(")(").append(ratio).append(")(").append(deviation).append("), lose, gen random:").append(openNum).toString());
        } else {
            openNum = (new StringBuilder()).append(list.get(0)).append(",").append(list.get(1)).append(",").append(list.get(2)).append(",").append(list.get(3)).append(",").append(list.get(4)).toString();
            logger.info((new StringBuilder()).append(now).append(" ").append(account).append(" bet tiger(").append(System.currentTimeMillis() - beginTime).append(")(").append(ratio).append(")(").append(deviation).append("), win, gen is:").append(openNum).append(" bet:").append(allPrice).append("(").append(betAmount).append(")").append(" win:").append(lastWinAmount).append("(").append(winAmount).append(") radio:").append(lastWinAndBetAmount.multiply(new BigDecimal("100"))).append("%").toString());
        }
        return openNum;

    }

    private String genTigerOpenNumsByRandom() {
        return NumUtils.getRandom(0, 9) + "," + NumUtils.getRandom(0, 9) + "," + NumUtils.getRandom(0, 9) + "," + NumUtils.getRandom(0, 9) + "," + NumUtils.getRandom(0, 9);
    }

    private BigDecimal getBetWin(LotteryBase lb, List<Bet> betList, List<Integer> openNums) {
        BigDecimal win = new BigDecimal("0");
        for (Bet bet : betList) {
            try {
                PlayerBase play = lb.getPlayer(bet.getPlayerId());
                //如果是宏发快三的大小单双或者和值，走豹子不通杀的逻辑
                if (bet.getLotteryId().equals("dfk3") && (bet.getPlayerId().equals("k3_star3_big_odd") || bet.getPlayerId().equals("k3_star3_and"))) {
                    logger.info(String.format("--> lotteryId:%s, playerId:%s, id:%s use getWinPlus", bet.getLotteryId(), bet.getPlayerId(), bet.getId()));
                    win = win.add(play.getWinPlus(bet.getContent(), openNums).multiply(new BigDecimal(bet.getPrice())).multiply(bet.getUnit()).divide(TWO).setScale(4, 1));
                } else {
                    logger.info(String.format("--> lotteryId:%s, playerId:%s, id:%s use getWin", bet.getLotteryId(), bet.getPlayerId(), bet.getId()));
                    win = win.add(play.getWin(bet.getContent(), openNums).multiply(new BigDecimal(bet.getPrice())).multiply(bet.getUnit()).divide(TWO).setScale(4, 1));
                }
            } catch (Exception localException) {
            }
        }
        return win;
    }

    private List<Integer> getNums(Random ran) {
        List<Integer> nums = new ArrayList<>(Arrays.asList(0, 2, 5, 6, 9, 1, 4, 7, 3, 8));
        int times = NumUtils.getRandom(ran, 50, 100);
        for (int j = 0; j < 10 + times; j++) {
            int n = NumUtils.getRandom(ran, 0, 9);
            nums.add(0, nums.remove(n));
        }
        return nums;
    }

    public boolean saveBetOrder(List<Bet> betList, BigDecimal allPrice, Date curOpenTime, boolean hideBet) {
        String account = betList.get(0).getAccount();
        User user = this.userDao.findByAccountMaster(account);
        Integer userType = user.getUserType();
        BigDecimal allRetrievableRebate = BigDecimal.ZERO;
        ThreadLog.addKey("得到用户锁后的时间:" + System.currentTimeMillis());
        if (user.getAmount().compareTo(allPrice) < 0) {
            throw new UnLogException("余额不足");
        }
        AmountChange amountChange = new AmountChange();
        for (Bet bet : betList) {
            user.setAmount(user.getAmount().subtract(bet.getAmount()));
            this.betDao.save(bet);
            this.lotteryLoseWinService.addLoseAndWin(bet.clone());

            amountChange.setBetId(bet.getId());
            amountChange.setLotteryId(bet.getLotteryId());
            amountChange.setSeasonId(bet.getSeasonId());
            amountChange.setChangeUser(bet.getAccount());
            amountChange.setChangeSource(bet.getAccount());
            amountChange.setChangeAmount(bet.getAmount().multiply(new BigDecimal("-1")));
            amountChange.setChangeTime(new Date());
            amountChange.setBalance(user.getAmount());
            amountChange.setPlayName(bet.getPlayName());
            amountChange.setTest(bet.getTest());
            amountChange.setUnit(bet.getUnit());
            amountChange.setAccountChangeTypeId(1);
            amountChange.setPlayerId(bet.getPlayerId());
            amountChange.setGroupName(bet.getGroupName());
            amountChange.setLotteryName(bet.getLotteryName());
            this.settlementDao.save(amountChange);
            if (hideBet) {
                hideBet(bet);
            }
        }
        this.userDao.updateAmount(user.getAccount(), allPrice.multiply(new BigDecimal("-1")));
        if (!"1".equals(user.getIsBet())) {
            this.userDao.setBet(account);
        }
        ThreadLog.addKey("lock end:" + System.currentTimeMillis());
        if (new Date().getTime() >= curOpenTime.getTime()) {
            throw new UnLogException("本期已停售");
        }
        return true;
    }

    private void hideBet(Bet bet) {
        bet.setHashCode(null);
        if (bet.getContent().length() > 50) {
            bet.setContent(bet.getContent().substring(0, 50) + "...");
        }
    }

    public List<Bet> list(Page p, CommonModel m) {
        return this.betDao.list(p, m);
    }

    public List<Bet> findByTraceId(String traceId) {
        return this.betDao.findByTraceId(traceId);
    }

    public Bet find(String id) {
        return this.betDao.findShort(id);
    }

    /**
     * @param betIdList
     * @param operator
     * @return
     * @author Jason Wang
     */
    public BaseReturnPojo saveUserCancelOrderNewFormat(List<String> betIdList, String operator) {
        Date curDate = new Date();
        List<Bet> bets = new ArrayList();
        for (String lst : betIdList) {
            Bet bet = this.betDao.find(lst);
            if (bet == null) {

                return BaseBeanUtils.getFailReturn("取消订单不存在！");
            }
            LotterySaleTime lotterySaleTime = this.lotterySaleTimeDao.find(bet.getSeasonId(), bet.getLotteryId());
            if (!bet.getAccount().equals(operator)) {
                return BaseBeanUtils.getFailReturn("不是本用户的订单不允许撤单");
            }
            if ((bet.getStatus() == 0) || (bet.getStatus() == 3)) {
                if (lotterySaleTime.getEndTime().getTime() <= curDate.getTime()) {
                    return BaseBeanUtils.getFailReturn("已封单，不允许撤单");
                }
            } else {
                return BaseBeanUtils.getFailReturn("只允许当前或追号期！");
            }
            bets.add(bet);
        }
        for (Bet bet : bets) {
            if (bet.getStatus() == 0) {
                insertBetCancel(bet, Integer.valueOf(4), "");
            } else if ((bet.getStatus() == 3) || (bet.getStatus() == 8)) {
                insertTraceCancel(bet, Integer.valueOf(4), Integer.valueOf(7), "");
            }
        }
        return BaseBeanUtils.getSuccessReturn("撤单成功");
    }

    public Object saveUserCancelOrder(List<String> betIdList, String operator) {
        Date curDate = new Date();
        List<Bet> bets = new ArrayList();
        for (String lst : betIdList) {
            Bet bet = this.betDao.find(lst);
            if (bet == null) {
                return Jsoner.error("取消订单不存在！");
            }
            LotterySaleTime lotterySaleTime = this.lotterySaleTimeDao.find(bet.getSeasonId(), bet.getLotteryId());
            if (!bet.getAccount().equals(operator)) {
                return Jsoner.error("不是本用户的订单不允许撤单");
            }
            if ((bet.getStatus() == 0) || (bet.getStatus() == 3)) {
                if (lotterySaleTime.getEndTime().getTime() <= curDate.getTime()) {
                    return Jsoner.error("已封单，不允许撤单");
                }
            } else {
                return Jsoner.error("只允许当前或追号期！");
            }
            bets.add(bet);
        }
        for (Bet bet : bets) {
            if (bet.getStatus() == 0) {
                insertBetCancel(bet, Integer.valueOf(4), "");
            } else if ((bet.getStatus() == 3) || (bet.getStatus() == 8)) {
                insertTraceCancel(bet, Integer.valueOf(4), Integer.valueOf(7), "");
            }
        }
        return Jsoner.success();
    }

    public Jsoner saveSystemCancelAll(String traceId, String operator) {
        return saveSystemCancelAll(this.betDao.getAllTraceCancelBet(traceId), "");
    }

    public Jsoner saveSystemCancelAll(List<Bet> bets, String operator) {
        for (Bet bet : bets) {
            saveSystemCancel(bet, operator);
        }
        return Jsoner.success();
    }

    public Jsoner saveSystemCancelStopAll(List<Bet> bets, String operator) {
        for (Bet bet : bets) {
            logger.info("cancel bet:" + bet.getId());
            insertTraceCancel(bet, 9, 27, operator);
        }
        return Jsoner.success();
    }

    public Jsoner saveSystemCancel(Bet bet, String operator) {
        if ((bet.getStatus() == 3) || (bet.getStatus() == 8)) {
            return insertTraceCancel(bet, 5, 7, operator);
        }
        return insertBetCancel(bet, 5, operator);
    }

    public Jsoner insertBetCancel(Bet bet, Integer status, String operator) {
        int preStatus = bet.getStatus();
        bet.setStatus(status);
        if (this.betDao.update(status, preStatus, bet.getId(), bet.getLastTime()) > 0) {
            int accountChangeTypeId = status == 4 ? 8 : 9;
            List<AmountChange> amountChangeList = this.settlementDao.getAmountChangeByBetId(bet.getId());
            saveCancelOrderAmount(amountChangeList, accountChangeTypeId, operator);
            if (!"".equals(bet.getTraceId())) {
                TraceCancelTotal traceCancelTotal = new TraceCancelTotal();
                traceCancelTotal.setBigDecimal(bet.getAmount());
                traceCancelTotal.setNumInteger(1);
                this.traceService.updateTraceAmountAndNum(bet.getTraceId(), traceCancelTotal, preStatus, 1);
            }
            this.lotteryLoseWinService.subtractLoseAndWin(bet);
        }
        return Jsoner.success();
    }

    public Jsoner insertTraceCancel(Bet bet, Integer betStatus, Integer changeTypeId, String operator) {
        AmountChange amountChange = new AmountChange();
        Integer preStatus = bet.getStatus();
        bet.setStatus(betStatus);
        if (this.betDao.update(betStatus, preStatus, bet.getId(), bet.getLastTime()) > 0) {
            User user = this.userDao.findByAccountMaster(bet.getAccount());
            amountChange.setBetId(bet.getId());
            amountChange.setLotteryId(bet.getLotteryId());
            amountChange.setSeasonId(bet.getSeasonId());
            amountChange.setChangeUser(bet.getAccount());
            amountChange.setChangeSource(bet.getAccount());
            amountChange.setChangeAmount(bet.getAmount());
            amountChange.setChangeTime(new Date());
            amountChange.setBalance(user.getAmount().add(bet.getAmount()));
            amountChange.setPlayName(bet.getPlayName());
            amountChange.setTest(user.getTest());
            amountChange.setAccountChangeTypeId(changeTypeId);
            amountChange.setPlayerId(bet.getPlayerId());
            amountChange.setGroupName(bet.getGroupName());
            amountChange.setLotteryName(bet.getLotteryName());
            amountChange.setUnit(bet.getUnit());
            amountChange.setHandlers(operator);
            this.settlementDao.save(amountChange);
            this.userDao.updateAmount(bet.getAccount(), amountChange.getChangeAmount());
            if (!"".equals(bet.getTraceId())) {
                TraceCancelTotal traceCancelTotal = new TraceCancelTotal();
                traceCancelTotal.setBigDecimal(bet.getAmount());
                traceCancelTotal.setNumInteger(Integer.valueOf(1));
                this.traceService.updateTraceAmountAndNum(bet.getTraceId(), traceCancelTotal, Integer.valueOf(3), Integer.valueOf(1));
            }
        } else {
            logger.error("cancel bet error:" + bet.getId());
        }
        return Jsoner.success();
    }

    public void insertTraceCancel(List<Bet> betList, Integer betStatus, String operator) {
        for (Bet bet : betList) {
            insertTraceCancel(bet, betStatus, Integer.valueOf(7), operator);
        }
    }

    public Boolean saveCancelOrderAmount(List<AmountChange> amountChanges, Integer accountChangeTypeId, String operator) {
        Date curData = new Date();
        for (AmountChange amountChange : amountChanges) {
            amountChange.setChangeAmount(amountChange.getChangeAmount().multiply(new BigDecimal("-1")));
            amountChange.setChangeTime(curData);
            int typeId = amountChange.getAccountChangeTypeId();
            if (typeId == 1) {
                amountChange.setAccountChangeTypeId(7);
            } else if ((typeId == 2) || (typeId == 3)) {
                amountChange.setAccountChangeTypeId(accountChangeTypeId);
            } else if (typeId == 4) {
                amountChange.setAccountChangeTypeId(10);
            }
            amountChange.setHandlers(operator);

            amountChange.setBalance(this.userDao.getAmountMaster(amountChange.getChangeUser()));
            this.settlementDao.save(amountChange);
            this.userDao.updateAmount(amountChange.getChangeUser(), amountChange.getChangeAmount());
        }
        return Boolean.valueOf(true);
    }

    public boolean saveOpenNum(String lotteryId, String seasonId, String openNum) {
        List<Integer> lst = ListUtils.toIntList(openNum);
        int listSize = lst.size();
        LotterySaleTime lotterySaleTime = this.lotterySaleTimeDao.getByLotteryIdAndSeasonId(lotteryId, seasonId);
        if ((listSize < 3) || (lotterySaleTime == null)) {
            return false;
        }
        Date curDate = new Date();
        LotterySeason lotterySeason = new LotterySeason();
        lotterySeason.setLotteryId(lotteryId);
        lotterySeason.setAddTime(curDate);
        lotterySeason.setSeasonId(seasonId);
        lotterySeason.setOpenTime(lotterySaleTime.getOpenTime());
        if (listSize > 0) {
            lotterySeason.setN1(lst.get(0));
        }
        if (listSize > 1) {
            lotterySeason.setN2(lst.get(1));
        }
        if (listSize > 2) {
            lotterySeason.setN3(lst.get(2));
        }
        if (listSize > 3) {
            lotterySeason.setN4(lst.get(3));
        }
        if (listSize > 4) {
            lotterySeason.setN5(lst.get(4));
        }
        if (listSize > 5) {
            lotterySeason.setN6(lst.get(5));
        }
        if (listSize > 6) {
            lotterySeason.setN7(lst.get(6));
        }
        if (listSize > 7) {
            lotterySeason.setN8(lst.get(7));
        }
        if (listSize > 8) {
            lotterySeason.setN9(lst.get(8));
        }
        if (listSize > 9) {
            lotterySeason.setN10(lst.get(9));
        }
        this.lotterySeasonDao.save(lotterySeason);
        return true;
    }

    public Object saveAdvanceCancelOrder(String lotteryId, String seasonId, String operator) {
        List<Bet> betList = this.betDao.getAllCancelBet(seasonId, lotteryId);
        saveSystemCancelAll(betList, operator);
        for (Bet bet : betList) {
            if ((bet.getTraceId() != null) && (!"".equals(bet.getTraceId()))) {
                saveTraceCancelOrder(bet.getTraceId());
            }
        }
        return Jsoner.success();
    }

    public Object saveManCancelOrder(String lotteryId, String startSeasonId, String endSeasonId, String operator) {
        List<LotterySaleTime> lotterySaleTimes = this.lotterySaleTimeDao.listDuringSeason(lotteryId, startSeasonId, endSeasonId);
        if (lotterySaleTimes.size() < 1) {
            return Jsoner.error("填写的期号有误！");
        }
        for (LotterySaleTime lotterySaleTime : lotterySaleTimes) {
            List<Bet> betList = this.betDao.getAllCancelBet(lotterySaleTime.getSeasonId(), lotteryId);
            for (Bet bet : betList) {
                if ((bet.getStatus().equals(BetStatus.no_start.getStatus())) || (bet.getStatus().equals(BetStatus.stop.getStatus()))) {
                    insertTraceCancel(bet, BetStatus.system_withdraw.getStatus(), 7, operator);
                } else {
                    insertBetCancel(bet, 5, operator);
                }
            }
            this.lotterySaleTimeDao.updateOpenStatus(lotterySaleTime.getSeasonId(), lotteryId, 6);
            this.lotterySaleTimeDao.updateSettleStatus(lotterySaleTime.getSeasonId(), lotteryId, null, SettleStatus.system_withdraw.getStatus());
        }
        return Jsoner.success();
    }

    public TraceCancelTotal saveTraceCancelOrder(String traceId) {
        TraceCancelTotal traceCancelTotal = new TraceCancelTotal();
        BigDecimal bigDecimal = new BigDecimal("0");
        Integer allNum = 0;
        List<Bet> betList = this.betDao.getAllTraceCancelBet(traceId);
        if (betList != null) {
            for (Bet bet : betList) {
                allNum = allNum + 1;
                bigDecimal = bigDecimal.add(bet.getAmount());
            }
            saveSystemCancelAll(betList, "");

            traceCancelTotal.setBigDecimal(bigDecimal);
            traceCancelTotal.setNumInteger(allNum);
        }
        return traceCancelTotal;
    }

    public List<Bet> listByAccount(String account, int num) {
        return this.betDao.listByAccount(account, num);
    }

    public List<Bet> summaryList(Page p, String traceId) {
        return this.betDao.findByTraceId(traceId);
    }

    public List<Bet> adminList(Page p, CommonModel m) {
        return this.betDao.adminList(p, m);
    }

    public int updateClose(String lotteryId, String seasonId) {
        List<String> ids = this.betDao.getIds(lotteryId, seasonId, 0);
        int n = 0;
        for (String id : ids) {
            n += this.betDao.updateStatus(id, 0, 6);
        }
        return n;
    }

    public List<Bet> listByLatest(int pageNo, String userName) {
        return this.betDao.listByLatest(pageNo, userName);
    }

    public String findContentNext(String id, Integer i) {
        return this.betDao.findContentNext(id, i);
    }

    public AdminBetDetail showBetDetail(String id, String account) {
        AdminBetDetail adminBetDetail = new AdminBetDetail();
        User user = this.userDao.findByAccount(account);
        adminBetDetail.setRebateRatio(user.getRebateRatio());
        Bet bet = this.betDao.findShort(id);
        adminBetDetail.setSeasonId(bet.getSeasonId());
        adminBetDetail.setCreateTime(bet.getCreateTime());
        adminBetDetail.setAmount(bet.getAmount());
        adminBetDetail.setContent(bet.getContent());
        adminBetDetail.setLotteryName(bet.getLotteryName());
        adminBetDetail.setBetCount(bet.getBetCount());
        adminBetDetail.setPrice(bet.getPrice());
        adminBetDetail.setUnit(bet.getUnit());
        adminBetDetail.setOpenNum(bet.getOpenNum());
        adminBetDetail.setBonusType(bet.getBonusType());
        adminBetDetail.setStatus(bet.getStatus());
        adminBetDetail.setWin(bet.getWin());
        adminBetDetail.setId(bet.getId());
        adminBetDetail.setAccount(bet.getAccount());
        adminBetDetail.setPlayName(bet.getPlayName());
        return adminBetDetail;
    }

    public List<Bet> listByCond(String account, Integer start, Integer limit) {
        return this.betDao.listByCond(account, start, limit);
    }

    public boolean SendBetEmail(String seasonId, String lotteryId) {
        List<ResettleMail> resettleMails = this.resettleMailDao.list(Integer.valueOf(1), Integer.valueOf(0), lotteryId);
        if (resettleMails.size() < 1) {
            return false;
        }
        int len = resettleMails.size();
        int bit = Integer.parseInt(seasonId.substring(seasonId.length() - 1));
        int mod = bit % len;
        List<Integer> statusList = new ArrayList();
        statusList.add(Integer.valueOf(0));
        statusList.add(Integer.valueOf(6));
        List<Bet> bets = this.betDao.getAllBet(seasonId, lotteryId, statusList);
        StringBuffer bf = new StringBuffer();
        if (bets.size() > 0) {
            for (Bet bet : bets) {
                bf.append("--------------------------------");
                bf.append("<br/>");
                bf.append(bet.getAccount());
                bf.append(" ID:");
                bf.append(bet.getId());
                bf.append("<br/>");
                bf.append(" 金额:");
                bf.append(bet.getAmount());
                bf.append(" 倍数:");
                bf.append(bet.getPrice());
                bf.append(" 单位");
                bf.append(bet.getUnit());
                bf.append(" 时间:");
                bf.append(DateUtils.format(bet.getCreateTime()));
                bf.append("<br/>");
                bf.append(bet.getPlayName());
                bf.append(" 内容:");
                bf.append(bet.getContent());
                bf.append("<br/>");
                bf.append("--------------------------------");
                bf.append("<br/>");
            }
            ResettleMail email = resettleMails.get(mod);

            String title = bets.get(0).getLotteryName() + " " + bets.get(0).getSeasonId();
            for (int i = 0; i < 3; i++) {
                if (EmailSender.send(email.getHost(), email.getSendAddress(), email.getAddress(), email.getUser(), email.getPassword(), title, bf.toString() + " 发送时间:" + DateUtils.format(new Date()))) {
                    break;
                }
                logger.error("第" + (i + 1) + "次投注发送邮箱失败:" + title + " " + email.getAddress());
                try {
                    Thread.sleep(8000L);
                } catch (InterruptedException localInterruptedException) {
                }
            }
        }
        return true;
    }

    //**************************************以下为变更部分*****************************************

    public List<Map<String, Object>> adminList_Z(Page p, CommonModel m) {
        List<Bet> betList = this.betDao.adminList(p, m);
        List<Map<String, Object>> betMapList = null;
        if (betList.size() > 0) {
            betMapList = new ArrayList<Map<String, Object>>();
            Map<String, Object> betmap = null;
            for (Bet bet : betList) {
                betmap = BeanZUtils.transBeanMap(bet);

                String userAgent = "";
                String account = bet.getAccount();
                Date createTime = bet.getCreateTime();
                if(null != betmap.get("createTime")){
                    betmap.remove("createTime");
                    betmap.put("createTime", DateUtils.format(createTime));
                }

                if (!StrUtils.hasEmpty(account) && !StrUtils.hasEmpty(createTime)) {
                    LogAll logAll = logAllDao.findByAccountAndCreateTime(account, createTime);
                    if (logAll != null && logAll.getUserAgent() != null) {
                        String userAgentStr = logAll.getUserAgent();
                        UserAgent userAgentOb = UserAgent.parseUserAgentString(userAgentStr);
                        Browser browser = userAgentOb.getBrowser();
                        //String browserName = browser.getName();// 浏览器名称
                        String group = "-";// 浏览器大类
                        if (browser != null && browser.getGroup() != null) {
                            group = browser.getGroup().getName();
                        }
                        Version browserVersion = userAgentOb.getBrowserVersion();// 详细版本
                        String version = "-";// 浏览器主版本
                        if (browserVersion != null) {
                            version = browserVersion.getMajorVersion();
                        }
                        OperatingSystem os = userAgentOb.getOperatingSystem();// 访问设备系统
                        DeviceType deviceType = DeviceType.UNKNOWN; // 访问设备类型
                        if (os != null) {
                            deviceType = os.getDeviceType();
                        }
                        //Manufacturer manufacturer = os.getManufacturer();// 访问设备制造厂商
                        userAgent = group + "-" + version + "/" + os + "/" + deviceType;
                    }
                }
                betmap.put("userAgent", userAgent);
                betMapList.add(betmap);
            }
        }
        return betMapList;
    }

}
