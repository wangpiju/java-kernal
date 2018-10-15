package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetDao;
import com.hs3.dao.lotts.BetInAmountDao;
import com.hs3.dao.lotts.BetInConfigDao;
import com.hs3.dao.lotts.BetInDao;
import com.hs3.dao.lotts.BetInPriceDao;
import com.hs3.dao.lotts.BetInRuleFirstDao;
import com.hs3.dao.lotts.BetInRuleKillDao;
import com.hs3.dao.lotts.BetInTimeDao;
import com.hs3.dao.lotts.BetInTotalDao;
import com.hs3.dao.lotts.LotteryDao;
import com.hs3.dao.lotts.SettlementDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.AmountChange;
import com.hs3.entity.lotts.Bet;
import com.hs3.entity.lotts.BetIn;
import com.hs3.entity.lotts.BetInAmount;
import com.hs3.entity.lotts.BetInConfig;
import com.hs3.entity.lotts.BetInPrice;
import com.hs3.entity.lotts.BetInProbability;
import com.hs3.entity.lotts.BetInRuleFirst;
import com.hs3.entity.lotts.BetInRuleKill;
import com.hs3.entity.lotts.BetInTime;
import com.hs3.entity.lotts.BetInTotal;
import com.hs3.entity.lotts.Lottery;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.utils.DateUtils;
import com.hs3.utils.NumUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betInService")
public class BetInService {
    private static final Logger log = LoggerFactory.getLogger(BetInService.class);
    private static final String BET_IN_NAME = "彩中彩";
    private static final String BET_IN_NAME_PLAY = "投注";
    private static final Integer BET_IN_CHANGEID_BUY = Integer.valueOf(91);
    private static final Integer BET_IN_CHANGEID_WIN = Integer.valueOf(92);
    private static final BigDecimal BET_IN_UNIT_YUAN = new BigDecimal("2");
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    @Autowired
    private BetInDao betInDao;
    @Autowired
    private BetInTimeDao betInTimeDao;
    @Autowired
    private BetInAmountDao betInAmountDao;
    @Autowired
    private BetInPriceDao betInPriceDao;
    @Autowired
    private BetInConfigDao betInConfigDao;
    @Autowired
    private BetInRuleFirstDao betInRuleFirstDao;
    @Autowired
    private BetInRuleKillDao betInRuleKillDao;
    @Autowired
    private BetInTotalDao betInTotalDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BetDao betDao;
    @Autowired
    private SettlementDao settlementDao;
    @Autowired
    private LotteryDao lotteryDao;

    public BetIn save(BetIn m)
            throws BaseCheckException {
        //總金額不得小於0
        if (m.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BaseCheckException("it's not allowed amount < 0");
        }
        //只保留小數點下三位
        m.setAmount(m.getAmount().setScale(3, 1));
        //取得設定
        BetInConfig betInConfig = getBetInConfig();
        //0是開啟
        if (betInConfig.getStatus().intValue() != 0) {
            throw new BaseCheckException("彩中彩功能已关闭！");
        }
        //下注金額小於最小額度
        if (betInConfig.getAmountMin().compareTo(m.getAmount()) > 0) {
            throw new BaseCheckException("彩中彩游戏最小额度为：" + betInConfig.getAmountMin());
        }
        //下注金額超過最大額度
        if (betInConfig.getAmountMax().compareTo(m.getAmount()) < 0) {
            throw new BaseCheckException("彩中彩游戏最大额度为：" + betInConfig.getAmountMax());
        }
        User user = this.userDao.findByAccountMaster(m.getAccount());
        if (user == null) {
            throw new BaseCheckException("用户不存在！");
        }
        if (user.getAmount().compareTo(m.getAmount()) < 0) {
            throw new BaseCheckException("余额不足！");
        }
        if (user.getBetInStatus().intValue() != 0) {
            throw new BaseCheckException("您没有开启彩中彩功能！");
        }
        //取得訂單
        Bet bet = (Bet) this.betDao.find(m.getBetId());
        if ((bet == null) || (bet.getStatus().intValue() != 1)) {
            throw new BaseCheckException("订单错误！");
        }
        Lottery lottery = (Lottery) this.lotteryDao.find(bet.getLotteryId());
        if (lottery.getBetInStatus().intValue() != 0) {
            throw new BaseCheckException("该彩种[" + bet.getLotteryName() + "]没有开启彩中彩功能！");
        }
        //取得派彩
        BigDecimal quota = bet.getWin();
        BetInTotal betInTotal = find(m.getAccount(), m.getBetId());
        if (betInTotal != null) {
            if (betInTotal.getGameCount().intValue() >= betInConfig.getGameCountMax().intValue()) {
                throw new BaseCheckException("您本轮的挑战次数已经耗尽<br>投注中奖后将再次激活彩中彩");
            }
            quota = quota.add(betInTotal.getWinAmount()).subtract(betInTotal.getGameAmount());
        }
        if (quota.compareTo(m.getAmount()) < 0) {
            throw new BaseCheckException("您本轮的额度不足<br>投注中奖后将再次激活彩中彩");
        }
        BigDecimal changeAmount = BigDecimal.ZERO.subtract(m.getAmount().abs());
        updateUserAmount(user.getAccount(), changeAmount);
        BigDecimal balance = this.userDao.getAmount(user.getAccount());
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BaseCheckException("操作失败！");
        }
        saveAmoutChange(user, bet, changeAmount, balance, BET_IN_CHANGEID_BUY);


        BigDecimal priceMin = betInConfig.getPriceDefaultMin();
        BigDecimal priceMax = betInConfig.getPriceDefaultMax();

        BetInRuleKill betInRuleKill = null;
        BetInPrice betInPrice = null;
        boolean isWarn = false;
        //看是否是首玩得範圍, 是的話則套用
        if (this.betInDao.countByAccount(user.getAccount()) <= betInConfig.getRuleFirstCount().intValue()) {
            BetInRuleFirst betInRuleFirst = findRuleFirst(m.getAmount());
            if (betInRuleFirst != null) {
                priceMin = betInRuleFirst.getPriceMin();
                priceMax = betInRuleFirst.getPriceMax();
            }
        } else {
            //若不是首玩則拿取追殺規則
            betInRuleKill = findRuleKillAndFish(user.getAccount());
            if (betInRuleKill != null) {
                priceMin = betInRuleKill.getKillPriceMin();
                priceMax = betInRuleKill.getKillPriceMax();
            } else {
                betInPrice = getBetInPrice(m.getAmount());
                if ((betInPrice != null) &&
                        (betInPrice.getSurplusNum().intValue() > 0)) {
                    this.betInPriceDao.updateSurplusNum(betInPrice.getId(), -1);
                    betInPrice = (BetInPrice) this.betInPriceDao.find(betInPrice.getId());
                    isWarn = betInPrice.getSurplusNum().intValue() <= betInPrice.getWarnNum().intValue();

                    priceMin = betInPrice.getStart();
                    priceMax = betInPrice.getEnd();
                }
            }
        }
        BigDecimal multiples = random(priceMin, priceMax);
        BigDecimal win = m.getAmount().multiply(multiples).setScale(4, 1);
        BigDecimal amountSubWin = m.getAmount().subtract(win);
        if (win.compareTo(BigDecimal.ZERO) > 0) {
            updateUserAmount(user.getAccount(), win);
        }
        balance = this.userDao.getAmount(user.getAccount());
        saveAmoutChange(user, bet, win, balance, BET_IN_CHANGEID_WIN);


        m.setMultiples(multiples);
        m.setWin(win);
        m.setCreateTime(new Date());
        m.setTest(user.getTest());
        this.betInDao.save(m);


        this.betInConfigDao.updateTotalAmount(betInConfig.getId(), m.getAmount());
        if (betInTotal != null) {
            this.betInTotalDao.updateGame(Integer.valueOf(1), m.getAmount(), win, betInTotal.getId());
        } else {
            betInTotal = new BetInTotal();
            betInTotal.setAccount(user.getAccount());
            betInTotal.setTest(user.getTest());
            betInTotal.setBetId(bet.getId());
            betInTotal.setLotteryName(bet.getLotteryName());
            betInTotal.setPlayName(bet.getPlayName());
            betInTotal.setStartAmount(bet.getWin());
            betInTotal.setStartTime(new Date());
            betInTotal.setWinAmount(win);
            betInTotal.setGameAmount(m.getAmount());
            betInTotal.setGameCount(Integer.valueOf(1));
            this.betInTotalDao.save(betInTotal);
        }
        if (betInRuleKill != null) {
            BigDecimal hadKillAmount = betInRuleKill.getHadKillAmount().add(amountSubWin);
            BigDecimal hadKillCount = betInRuleKill.getHadKillCount().add(BigDecimal.ONE);

            boolean cond1 = hadKillAmount.abs().compareTo(betInRuleKill.getKillAmount()) >= 0;
            boolean cond2 = hadKillCount.intValue() >= betInRuleKill.getKillCount().intValue();

            Integer status = betInRuleKill.getStatus();
            if (betInRuleKill.getBothStatus().intValue() == 0) {
                status = Integer.valueOf((cond1) && (cond2) ? 2 : status.intValue());
            } else if (betInRuleKill.getBothStatus().intValue() == 1) {
                status = Integer.valueOf((cond1) || (cond2) ? 2 : status.intValue());
            }
            this.betInRuleKillDao.updateHadKill(amountSubWin, BigDecimal.ONE, status, betInRuleKill.getId());
        }
        if (isWarn) {
            betInConfig = (BetInConfig) this.betInConfigDao.find(betInConfig.getId());
            if (betInConfig.getTotalAmount().compareTo(betInPrice.getTotalAmount()) >= 0) {
                this.betInPriceDao.updateSurplusNum(betInPrice.getId(), betInPrice.getAddNum().intValue());
                this.betInConfigDao.updateTotalAmount(betInConfig.getId(), BigDecimal.ZERO.subtract(betInPrice.getTotalAmount()));
            }
        }
        return m;
    }

    private BetInRuleFirst findRuleFirst(BigDecimal amount) {
        BetInRuleFirst betInRuleFirst = new BetInRuleFirst();
        betInRuleFirst.setAmount(amount);

        List<BetInRuleFirst> list = this.betInRuleFirstDao.listByCond(betInRuleFirst, null);
        return list.isEmpty() ? null : (BetInRuleFirst) list.get(0);
    }

    private BetInRuleKill findRuleKillAndFish(String account) {
        BetInRuleKill betInRuleKill = new BetInRuleKill();
        betInRuleKill.setAccount(account);
        betInRuleKill.setStatus(Integer.valueOf(1));

        List<BetInRuleKill> list = this.betInRuleKillDao.listByCond(betInRuleKill, null);
        return list.isEmpty() ? null : (BetInRuleKill) list.get(0);
    }

    private BetInTotal find(String account, String betId) {
        BetInTotal m = new BetInTotal();
        m.setAccount(account);
        m.setBetId(betId);
        List<BetInTotal> list = this.betInTotalDao.findByCond(false, m, null, null, false, null);
        if (list.isEmpty()) {
            return null;
        }
        return (BetInTotal) list.get(0);
    }

    private void updateUserAmount(String account, BigDecimal amount) {
        int i = this.userDao.updateAmount(account, amount).intValue();
        if (i != 1) {
            log.error("user error![" + account + "][" + amount + "][" + i + "]");
            throw new BaseCheckException("更新余额失败！");
        }
    }

    private void saveAmoutChange(User user, Bet bet, BigDecimal changeAmount, BigDecimal balance, Integer amountTypeId) {
        AmountChange amountChange = new AmountChange();


        amountChange.setSeasonId(bet.getSeasonId());
        amountChange.setChangeUser(user.getAccount());
        amountChange.setChangeSource(user.getAccount());
        amountChange.setChangeAmount(changeAmount);
        amountChange.setChangeTime(new Date());
        amountChange.setBalance(balance);
        amountChange.setPlayName("投注");
        amountChange.setTest(user.getTest().intValue());
        amountChange.setAccountChangeTypeId(amountTypeId.intValue());

        amountChange.setGroupName("彩中彩");
        amountChange.setLotteryName("彩中彩");
        amountChange.setUnit(BET_IN_UNIT_YUAN);

        this.settlementDao.save(amountChange);
    }

    private BetInPrice getBetInPrice(BigDecimal amount) {
        String time = DateUtils.format(new Date(), "HH:mm:ss");
        List<BetInTime> betInTimeList = this.betInTimeDao.listByTime(time);
        if (betInTimeList.isEmpty()) {
            log.warn("该时间段[" + time + "]内没有定义规则，请检验是否闭合!");
            return null;
        }
        BetInTime betInTime = (BetInTime) BetInProbability.getProbability(betInTimeList);
        if (betInTime == null) {
            log.warn("该时间段[" + time + "]内没有算出概率，请检验概率之和是否为100!");
            return null;
        }
        BigDecimal ruleAmount = getRuleAmount(amount, betInTime.getRuleId());
        if (ruleAmount == null) {
            log.warn("该时间段[" + time + "][" + amount + "][" + betInTime.getRuleId() + "]没有确定出金额区间，请检查有最大金额区间!");
            return null;
        }
        List<BetInAmount> betInAmountList = this.betInAmountDao.listByRuleId(betInTime.getRuleId(), ruleAmount);
        if (betInAmountList.isEmpty()) {
            log.warn("该时间段[" + time + "][" + ruleAmount + "][" + betInTime.getRuleId() + "]没有定义概率!");
            return null;
        }
        BetInAmount betInAmount = (BetInAmount) BetInProbability.getProbability(betInAmountList);
        if (betInAmount == null) {
            log.warn("该时间段[" + time + "][" + ruleAmount + "][" + betInTime.getRuleId() + "]内没有算出概率，请检验概率之和是否为100!");
            return null;
        }
        return (BetInPrice) this.betInPriceDao.find(betInAmount.getPriceId());
    }

    public BetInConfig getBetInConfig() {
        return (BetInConfig) this.betInConfigDao.list(null).get(0);
    }

    public BigDecimal getRuleAmount(BigDecimal amount, Integer ruleId) {
        for (BetInAmount val : this.betInAmountDao.listAmount(ruleId)) {
            if (amount.compareTo(val.getAmount()) <= 0) {
                return val.getAmount();
            }
        }
        return null;
    }

    private BigDecimal random(BigDecimal min, BigDecimal max) {
        int minInt = min.multiply(HUNDRED).intValue();
        int maxInt = max.multiply(HUNDRED).intValue();
        return new BigDecimal(NumUtils.getRandom(minInt, maxInt)).divide(HUNDRED);
    }

    public List<BetIn> listByCond(BetIn m, Date startTime, Date endTime, Page page) {
        return this.betInDao.listByCond(m, startTime, endTime, page);
    }

    public List<BetIn> list(Page p) {
        return this.betInDao.list(p);
    }

    public BetIn find(Integer id) {
        return (BetIn) this.betInDao.find(id);
    }

    public int update(BetIn m) {
        return this.betInDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.betInDao.delete(ids);
    }
}
