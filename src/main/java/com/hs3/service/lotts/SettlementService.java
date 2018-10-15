package com.hs3.service.lotts;

import com.hs3.commons.OpenStatus;
import com.hs3.commons.PlanStatus;
import com.hs3.commons.SettleStatus;
import com.hs3.commons.Whether;
import com.hs3.dao.lotts.*;
import com.hs3.dao.sys.ResettleMailDao;
import com.hs3.dao.user.UserDao;
import com.hs3.dao.user.UserNoticeDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.*;
import com.hs3.entity.sys.ResettleMail;
import com.hs3.entity.users.User;
import com.hs3.entity.users.UserNotice;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.lotts.*;
import com.hs3.models.CommonModel;
import com.hs3.models.Jsoner;
import com.hs3.models.report.AllChange;
import com.hs3.service.quartz.TaskLogic;
import com.hs3.utils.DateUtils;
import com.hs3.utils.EmailSender;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service("settlementService")
public class SettlementService {
    public static final String BET_CHANGE_PLAYERID = "ssc_star3_last_single";
    public static final Map<String, String> BET_CHANGE_LOTTERY = new LinkedHashMap<>();

    static {
        BET_CHANGE_LOTTERY.put("cqssc", "重庆时时彩");
        BET_CHANGE_LOTTERY.put("tjssc", "天津时时彩");
        BET_CHANGE_LOTTERY.put("xjssc", "新疆时时彩");
    }

    public static final List<String> UN_USER_NOTICE_LOTTERY = Arrays.asList("tiger");
    private static final Logger logger = LoggerFactory.getLogger(SettlementService.class);
    public static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal TWO = new BigDecimal("2");
    private static final List<Integer> needProcessStatusList = Arrays.asList(0, 3, 6, 8);
    @Autowired
    private SettlementDao settlementDao;
    @Autowired
    private BetDao betDao;
    @Autowired
    private TraceDao traceDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BetService betService;
    @Autowired
    private LotterySaleTimeService lotterySaleTimeService;
    @Autowired
    private TraceService traceService;
    @Autowired
    private LotterySeasonDao lotterySeasonDao;
    @Autowired
    private LotteryDao lotteryDao;
    @Autowired
    private UserNoticeDao userNoticeDao;
    @Autowired
    private TempAmountChangeDao tempAmountChangeDao;
    @Autowired
    private LotterySaleTimeDao lotterySaleTimeDao;
    @Autowired
    private ResettleMailDao resettleMailDao;
    @Autowired
    private LotterySeasonService lotterySeasonService;
    @Autowired
    private TaskLogic taskLogic;
    public void updateBetChange(LotteryBase lb, String lotteryId, String seasonId, String openNum, Integer[] statusArray) {
        if (!BET_CHANGE_LOTTERY.containsKey(lotteryId)) {
            return;
        }
        String info = "[" + lotteryId + "][" + seasonId + "][" + openNum + "]";
        try {
            if (!StrUtils.hasEmpty(openNum)) {
                for (Bet bet : this.betDao.findBetChange(lotteryId, seasonId, "ssc_star3_last_single", statusArray)) {
                    if (BigDecimal.ZERO.compareTo(getBetWin(lb, bet, openNum)) >= 0) {
                        String before = bet.getContent();
                        bet.setContent(openNum.replace(",", "").substring(2) + before.substring(3));
                        int i = this.betDao.updateContent(bet.getId(), bet.getLastTime(), bet.getContent(), LotteryUtils.hashCode(bet));
                        if (i != 1) {
                            logger.error("改单失败" + info + "[" + bet.getId() + "][" + i + "]");
                        } else {
                            logger.info("改单成功" + info + "[" + bet.getId() + "][" + bet.getAccount() + "][" + before + "][" + bet.getContent() + "]");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("改单异常" + info, e);
        } finally {
            logger.info("改单完毕" + info);
        }
    }

    public void saveEmergency(String lotteryId, String seasonIdBegin, boolean force) {
        LotterySeason lotterySeasonLast = null;
        List<LotterySeason> lotterySeasonList = this.lotterySeasonDao.getLast(lotteryId, 1);
        if (lotterySeasonList.isEmpty()) {
            return;
        }
        lotterySeasonLast = lotterySeasonList.get(0);
        if (lotterySeasonLast.getSeasonId().compareTo(seasonIdBegin) < 0) {
            return;
        }
        List<LotterySaleTime> lotterySaleTimeList = this.lotterySaleTimeService.listDuringSeason(lotteryId, seasonIdBegin, lotterySeasonLast.getSeasonId());
        if (!force) {
            int count = this.lotterySeasonDao.count(lotteryId, seasonIdBegin, lotterySeasonLast.getSeasonId());
            if (count != lotterySaleTimeList.size()) {
                throw new BaseCheckException("检测到" + seasonIdBegin + "至" + lotterySeasonLast.getSeasonId() + "之间有未录入号码的奖期，如确认请强制处理！");
            }
            if (this.betDao.countBeforeSeason(lotteryId, seasonIdBegin, needProcessStatusList) > 0) {
                throw new BaseCheckException("检测到" + seasonIdBegin + "之前有需要应急处理状态的注单，如确定从当前期开始执行，请强制处理！");
            }
        }
        Lottery lottery = this.lotteryDao.find(lotteryId);
        LotteryBase lb = LotteryFactory.getInstance(lottery.getGroupName());
        String openNum;
        for (LotterySaleTime lotterySaleTime : lotterySaleTimeList) {
            this.lotterySaleTimeService.updateSettleStatus(lotterySaleTime.getSeasonId(), lotteryId, SettleStatus.non_execution.getStatus(), SettleStatus.executed.getStatus());
            this.lotterySaleTimeService.updatePlanStatus(lotterySaleTime.getSeasonId(), lotteryId, PlanStatus.non_execution.getStatus(), PlanStatus.executed.getStatus());

            openNum = null;
            LotterySeason lotterySeason = this.lotterySeasonDao.getBylotteryIdAndSeason(lotteryId, lotterySaleTime.getSeasonId());
            if (lotterySeason != null) {
                openNum = ListUtils.toString(lb.getSeasonOpen(lotterySeason).getNums());


                updateBetChange(lb, lotterySeason.getLotteryId(), lotterySeason.getSeasonId(), openNum, new Integer[]{0, 6});
            }
            for (Bet bet : this.betDao.getAllBet(lotterySaleTime.getSeasonId(), lotteryId, needProcessStatusList)) {
                bet = this.betDao.find(bet.getId());
                if (needProcessStatusList.contains(bet.getStatus())) {
                    if (openNum == null) {
                        Jsoner jsoner = this.betService.saveSystemCancel(bet, "");
                        updateRollback(jsoner.getStatus() != 200, bet, "处理系统撤单失败，回滚所有操作.");
                    } else {
                        if (bet.getStatus() == 8) {
                            bet.setStatus(3);
                            updateRollback(this.betDao.update(3, 8, bet.getId(), bet.getLastTime()) != 1, bet, "处理更新暂停状态失败，回滚所有操作.");
                            bet = this.betDao.find(bet.getId());
                        }
                        if (bet.getStatus() == 3) {
                            updateRollback(this.traceService.updateTrace(bet) == 2, bet, "处理追号失败，回滚所有操作.");
                            bet = this.betDao.find(bet.getId());
                        }
                        if ((bet.getStatus() == 0) || (bet.getStatus() == 6)) {
                            int updateStatus = updateBetAndAmount(lb, bet, openNum, "");
                            if ((updateStatus == 1) &&
                                    (bet.getIsTrace() == Whether.yes.getStatus()) && (bet.getTraceWinStop() == Whether.yes.getStatus())) {
                                this.betService.saveSystemCancelStopAll(this.betDao.getAllDuringBet(bet.getSeasonId(), null, lotteryId, bet.getTraceId(), null), "");
                            }
                            updateRollback((updateStatus != 0) && (updateStatus != 1), bet, "处理结算失败[" + updateStatus + "]，回滚所有操作.");
                        }
                    }
                }
            }
            if (openNum == null) {
                this.lotterySaleTimeService.updateOpenStatus(lotterySaleTime.getSeasonId(), lotteryId, OpenStatus.system_withdraw.getStatus());
                this.lotterySaleTimeService.updateSettleStatus(lotterySaleTime.getSeasonId(), lotteryId, SettleStatus.executed.getStatus(), SettleStatus.system_withdraw.getStatus());
            } else {
                this.lotterySaleTimeService.updateSettleStatus(lotterySaleTime.getSeasonId(), lotteryId, null, SettleStatus.completed.getStatus());
            }
            this.lotterySaleTimeService.updatePlanStatus(lotterySaleTime.getSeasonId(), lotteryId, null, PlanStatus.completed.getStatus());
        }
        for (Bet bet : this.betDao.getAllDuringBet(lotterySeasonLast.getSeasonId(), null, lotteryId, null, 8)) {
            bet.setStatus(3);
            updateRollback(this.betDao.update(3, 8, bet.getId(), bet.getLastTime()) != 1, bet, "处理更新状态8为3失败，回滚所有操作.");
        }
        LotterySaleTime lotterySaleTimeNext = this.lotterySaleTimeService.getNextByLotteryId(lotteryId, lotterySeasonLast.getSeasonId(), lotterySeasonLast.getOpenTime());
        for (Bet bet : this.betDao.getAllBet(lotterySaleTimeNext.getSeasonId(), 3, lotteryId)) {
            updateRollback(this.traceService.updateTrace(bet) == 2, bet, "处理追号（下一期）失败，回滚所有操作.");
        }
    }

    private void updateRollback(boolean cond, Bet bet, String log) {
        if (cond) {
            if (bet == null) {
                logger.error(log);
            } else {
                logger.error(log + "[" + bet.getGroupName() + "][" + bet.getSeasonId() + "][" + bet.getId() + "][" + bet.getStatus() + "][" + bet.getTraceWinStop() + "][" + bet.getTraceId() + "]");
            }
            throw new BaseCheckException(log);
        }
    }

    //处理状态为0/6（未开奖）的投注记录
    public int updateBetAndAmount(LotteryBase lb, Bet bet, String openNumsByParam, String handler) {
        int successflag = 0;

        //获取所赢取的金额
        BigDecimal win = getBetWin(lb, bet, openNumsByParam);

        User user = this.userDao.findByAccountMaster(bet.getAccount());
        if (BigDecimal.ZERO.compareTo(win) >= 0)//如果赢取的金额比0小，说明未中奖
        {
            bet.setStatus(2);//设置未中奖状态
        } else {
            bet.setStatus(1);//设置已中奖状态

            //已中奖时在会员金流中记录中奖的信息
            AmountChange model = new AmountChange();
            model.setChangeAmount(win);
            model.setBalance(user.getAmount().add(win));
            model.setChangeUser(bet.getAccount());
            model.setChangeSource(bet.getAccount());
            model.setChangeTime(new Date());
            model.setHandlers(handler);
            model.setBetId(bet.getId());
            model.setLotteryId(bet.getLotteryId());
            model.setSeasonId(bet.getSeasonId());
            model.setAccountChangeTypeId(4);//设置金流变更类型为4：奖金派送
            model.setRemark("中奖金额");
            model.setPlayName(bet.getPlayName());
            model.setTest(user.getTest());
            model.setPlayerId(bet.getPlayerId());
            model.setGroupName(bet.getGroupName());
            model.setLotteryName(bet.getLotteryName());
            model.setUnit(bet.getUnit());
            this.settlementDao.save(model);
            if (bet.getIsTrace() == 1) {
                this.traceDao.updateWinAmount(bet.getTraceId(), win);
            }
            this.userDao.updateAmount(user.getAccount(), win);
            successflag = 1;
            if (!UN_USER_NOTICE_LOTTERY.contains(bet.getLotteryId())) {
                //新增会员中奖的提示信息
                UserNotice userNotice = new UserNotice();
                userNotice.setAccount(user.getAccount());
                userNotice.setContent("恭喜您在" + bet.getLotteryName() + "第" + bet.getSeasonId() + "期，中奖" + win + "元！");
                userNotice.setBetId(bet.getId());
                userNotice.setWin(win);
                this.userNoticeDao.save(userNotice);
            }
        }
        Date oldTime = bet.getLastTime();
        bet.setLastTime(new Date());
        bet.setWin(win);
        bet.setOpenNum(openNumsByParam);
        if (this.betDao.update(bet, oldTime) < 1)//根据投注ID和投注记录的原最后修改时间更新投注信息
        {
            logger.info("结算条件不符合无法更新" + bet.getId() + ":" + bet.getSeasonId() + ":" + bet.getLotteryId());
            throw new BaseCheckException("结算条件不符合无法更新" + bet.getId() + ":" + bet.getSeasonId() + ":" + bet.getLotteryId());
        }

        //取当前用户所在奖金组的回扣比率
        BigDecimal group = ThreadLocalCache.getGroup(user.getBonusGroupId()).getRebateRatio();
        //取当前游戏当前玩法的回扣比率
        BigDecimal details = ThreadLocalCache.getGroupDetails(bet.getPlayerId(), bet.getLotteryId(), user.getBonusGroupId()).getRebateRatio();

        //{游戏回扣比率与奖金组回扣比率差}：用【取当前游戏当前玩法的回扣比率】减去【取当前用户所在奖金组的回扣比率】
        BigDecimal subRatio = details.subtract(group);

        //{用户计算关键回扣比率}：【当前用户的回扣比率】+{游戏回扣比率与奖金组回扣比率差}
        BigDecimal userRatio = user.getRebateRatio().add(subRatio);
        //{用户计算关键回扣比率}与0作比较，如果结果为小于返回-1，如果结果为大于返回1，如果结果为等于返回0，为了计算，所以结果小于0时让其值等于0
        if (userRatio.compareTo(BigDecimal.ZERO) < 0) {
            userRatio = BigDecimal.ZERO;
        }

        //BonusType为0是高奖，为1是高返，且{用户计算关键回扣比率}与0作比较大于0
        if ((bet.getBonusType() == 1) && (userRatio.compareTo(BigDecimal.ZERO) > 0)) {
            BigDecimal ratio = bet.getAmount().multiply(userRatio).divide(HUNDRED);
            AmountChange ratioChange = new AmountChange();
            ratioChange.setBetId(bet.getId());
            ratioChange.setLotteryId(bet.getLotteryId());
            ratioChange.setSeasonId(bet.getSeasonId());
            ratioChange.setChangeUser(bet.getAccount());
            ratioChange.setChangeSource(bet.getAccount());
            ratioChange.setChangeTime(new Date());
            ratioChange.setHandlers(handler);
            ratioChange.setPlayName(bet.getPlayName());
            ratioChange.setTest(bet.getTest());
            ratioChange.setPlayerId(bet.getPlayerId());
            ratioChange.setGroupName(bet.getGroupName());
            ratioChange.setLotteryName(bet.getLotteryName());
            ratioChange.setUnit(bet.getUnit());
            ratioChange.setAccountChangeTypeId(2);//设置金流变更类型：投注返点
            ratioChange.setChangeAmount(ratio);
            ratioChange.setBalance(user.getAmount().add(ratioChange.getChangeAmount()));
            ratioChange.setRemark("");
            this.settlementDao.save(ratioChange);

            this.userDao.updateAmount(user.getAccount(), ratio);
        }
        if (ThreadLocalCache.getHasRatio(user))//如果用户充值次数与最低充值次数都小于等于0时返回false，否则返回true
        {
            //取出用户上级账户树
            List<String> accounts = ListUtils.toList(user.getParentList());

            //当前用户的回扣比率
            BigDecimal currentRebateRatio = user.getRebateRatio();

            //该次循环到的上级用户的直属下级的回扣比率
            BigDecimal cRatio = currentRebateRatio;

            //至少二级代理才会执行该循环，坐标每次减一表示不断给上级返点回扣
            for (int i = accounts.size() - 2; i >= 0; i--) {

                String pAccount = accounts.get(i);//该次循环到的上级用户账户

                User p = this.userDao.findByAccountMaster(pAccount);

                //该次循环到的上级用户的回扣比率
                BigDecimal paccRebateRatio = p.getRebateRatio();
                //该次循环到的上级用户的回扣比率 减去 直属下级的回扣比率
                BigDecimal calculationRebateRatio = paccRebateRatio.subtract(cRatio);

                //{返点关键回扣比率}：该次循环到的上级用户的回扣比率+{游戏回扣比率与奖金组回扣比率差}
                //BigDecimal pRatio = p.getRebateRatio().add(subRatio);

                //if (pRatio.compareTo(BigDecimal.ZERO) > 0) {//如果{返点关键回扣比率}比0大

                //{上级是否该获得回扣金额的关键回扣比率}：{返点关键回扣比率}-{用户计算关键回扣比率}
                //BigDecimal nowRatio = pRatio.subtract(userRatio);

                //if (nowRatio.compareTo(BigDecimal.ZERO) > 0) {//如果{上级是否该获得回扣金额的关键回扣比率}大于0则给其应得的回扣金额
                if (calculationRebateRatio.compareTo(BigDecimal.ZERO) > 0) {//如果该次循环到的上级用户的回扣比率 减去 当前用户的回扣比率

                    //该次循环到的上级用户所应获得的回扣金额
                    //BigDecimal ratio = bet.getAmount().multiply(nowRatio).divide(HUNDRED);
                    BigDecimal ratio = bet.getAmount().multiply(calculationRebateRatio).divide(HUNDRED);

                    AmountChange ratioChange = new AmountChange();
                    ratioChange.setBetId(bet.getId());
                    ratioChange.setLotteryId(bet.getLotteryId());
                    ratioChange.setSeasonId(bet.getSeasonId());
                    ratioChange.setChangeUser(pAccount);
                    ratioChange.setChangeSource(bet.getAccount());
                    ratioChange.setChangeTime(new Date());
                    ratioChange.setHandlers(handler);
                    ratioChange.setPlayName(bet.getPlayName());
                    ratioChange.setTest(bet.getTest());
                    ratioChange.setPlayerId(bet.getPlayerId());
                    ratioChange.setGroupName(bet.getGroupName());
                    ratioChange.setLotteryName(bet.getLotteryName());
                    ratioChange.setUnit(bet.getUnit());
                    ratioChange.setAccountChangeTypeId(3);//设置金流变更类型：下级投注返点
                    ratioChange.setChangeAmount(ratio);//设置该次循环到的上级用户所应获得的回扣金额
                    ratioChange.setBalance(p.getAmount().add(ratioChange.getChangeAmount()));//设置该次循环到的上级用户的余额
                    ratioChange.setRemark("");
                    this.settlementDao.save(ratioChange);

                    this.userDao.updateAmount(pAccount, ratio);
                    //this.userDao.updateAmount_Z(pAccount, ratio);

                }

                //} else {
                //pRatio = BigDecimal.ZERO;
                //}

                //userRatio = pRatio;
                cRatio = paccRebateRatio;

            }
        }
        return successflag;
    }

    public BigDecimal getBetWin(LotteryBase lb, Bet bet, String openNum) {
        PlayerBase play = lb.getPlayer(bet.getPlayerId());

        BigDecimal win ;
        //如果是宏发快三的大小单双或者和值，走豹子不通杀的逻辑
        if (bet.getLotteryId().equals("dfk3") && (bet.getPlayerId().equals("k3_star3_big_odd") || bet.getPlayerId().equals("k3_star3_and"))) {
            win = play.getWinPlus(bet.getContent(), ListUtils.toIntList(openNum));
        } else {
            win = play.getWin(bet.getContent(), ListUtils.toIntList(openNum));
        }
        if (win.compareTo(BigDecimal.ZERO) > 0) {
            //BigDecimal price = bet.getUnit().multiply(new BigDecimal(bet.getPrice())).divide(TWO, 4, 1);
            BigDecimal price = bet.getUnit().multiply(new BigDecimal(bet.getPrice()));
            BigDecimal ratio = bet.getBonusRate().divide(HUNDRED, 4, 1);

            //win = win.multiply(price).multiply(ratio).setScale(4, 1);
            //派彩改為小數點下兩位 除了快三大小單雙是三位
            if (null != lb.getGroupId() && lb.getGroupId().trim().equals("k3")
                    && play.getId().trim().equals("k3_star3_big_odd")) {
                win = win.multiply(ratio).setScale(3, RoundingMode.DOWN).multiply(price);
            } else
                win = win.multiply(ratio).setScale(2, RoundingMode.DOWN).multiply(price);
        }
        return win;
    }

    public void saveSettlementByUser(LotteryBase lb, String lotteryId, String seasonId, String openNum, String account, String nextSeasonId) {
        List<Bet> betList = this.betService.getBetByUser(seasonId, lotteryId, new Integer[]{0, 6}, account);

        Integer num = betList.size();
        logger.info(lotteryId + " " + seasonId + " " + account + "结算数:" + num);
        SettlementThread.incNums(num);
        for (Bet bet : betList) {
            //根据需要开奖的每个投注记录变更相关的金额
            int settleStatus = updateBetAndAmount(lb, bet, openNum, "");

            //如果是追号进行下面的流程
            if ((bet.getIsTrace() == 1) &&
                    (settleStatus == 1) && (bet.getTraceWinStop() == 1)) {
                List<Bet> betLst = this.betDao.getAllTraceCancelBet(bet.getTraceId());
                this.betService.saveSystemCancelStopAll(betLst, "");
            }
        }
        this.traceService.updateTraceByUser(lotteryId, nextSeasonId, account);
    }

    public Object saveReSettlement(String lotteryId, String seasonId, String openNum, String operator, Integer enforce) {
        if (enforce == null) {
            enforce = 0;
        }
        LotterySaleTime lotterySaleTime = this.lotterySaleTimeDao.findMaster(seasonId, lotteryId);
        if (lotterySaleTime != null) {
            if ((lotterySaleTime.getSettleStatus() != 0) && (enforce != 1)) {
                return Jsoner.error("系统已经执行，要人工执行重新派奖请在强制执行前打勾");
            }
        } else {
            return Jsoner.error("您输入的奖期有误");
        }
        Lottery lottery = this.lotteryDao.find(lotteryId);
        LotteryBase lb = LotteryFactory.getInstance(lottery.getGroupName());
        String lotteryTitle = lottery.getTitle();
        List<ResettleMail> resettleMails = this.resettleMailDao.listByStatusAndType(0, 0);
        if (DateUtils.getSecondBetween(lotterySaleTime.getOpenTime(), new Date()) > lottery.getReSettleTime() * 60) {
            for (ResettleMail resettleMail : resettleMails) {
                EmailSender.send(resettleMail.getHost(), resettleMail.getSendAddress(), resettleMail.getAddress(), resettleMail.getUser(), resettleMail.getPassword(), "重新派奖(当前操作期已经超时)",
                        "操作员:" + operator + " 彩种名:" + lotteryTitle + " 奖期:" + seasonId + " 重新派奖内容:" + openNum + " 操作时间:" + DateUtils.format(new Date()));
            }
            return Jsoner.error("当前操作期已经超时，并且发出邮件给相关管理人员");
        }
        this.lotterySeasonDao.delete(lotteryId, seasonId);
        if (!this.betService.saveOpenNum(lotteryId, seasonId, openNum)) {
            return Jsoner.error("输入的内容有误！");
        }
        this.lotterySaleTimeService.updateOpenStatus(seasonId, lotteryId, OpenStatus.lottery_manual.getStatus());


        updateBetChange(lb, lotteryId, seasonId, openNum, new Integer[]{0, 1, 2, 6});

        List<Integer> statusList = Arrays.asList(0, 1, 2, 6);

        List<Bet> betList = betDao.getAllBet(seasonId, lotteryId, statusList);
        for (Bet bet : betList) {
            if (bet.getStatus() == 1) {
                List<AmountChange> amountChangeList = this.settlementDao.getAmountChangeByBetId(bet.getId());
                BigDecimal winAmount = new BigDecimal("0");
                for (AmountChange amountChange : amountChangeList) {
                    if ((amountChange.getAccountChangeTypeId() == 4) || (amountChange.getAccountChangeTypeId() == 10)) {
                        winAmount = winAmount.add(amountChange.getChangeAmount());
                    }
                }
                if (winAmount.compareTo(new BigDecimal("0")) > 0) {
                    AmountChange model = new AmountChange();
                    model.setChangeAmount(winAmount.multiply(new BigDecimal("-1")));
                    model.setChangeUser(bet.getAccount());
                    model.setChangeSource(bet.getAccount());
                    model.setChangeTime(new Date());
                    model.setHandlers(operator);
                    model.setBetId(bet.getId());
                    model.setLotteryId(bet.getLotteryId());
                    model.setSeasonId(bet.getSeasonId());
                    model.setAccountChangeTypeId(10);
                    model.setRemark("");
                    model.setPlayName(bet.getPlayName());
                    model.setTest(bet.getTest());
                    model.setPlayerId(bet.getPlayerId());
                    model.setGroupName(bet.getGroupName());
                    model.setLotteryName(bet.getLotteryName());
                    model.setUnit(bet.getUnit());
                    model.setBalance(this.userDao.getAmountMaster(bet.getAccount()));
                    this.settlementDao.save(model);
                    this.userDao.updateAmount(bet.getAccount(), model.getChangeAmount());

                    this.traceDao.updateWinAmount(bet.getTraceId(), model.getChangeAmount());
                }
            }
            updateBetAndAmount(lb, bet, openNum, operator);
        }
        this.lotterySaleTimeService.updateSettleStatus(seasonId, lotteryId, SettleStatus.non_execution.getStatus(), SettleStatus.completed.getStatus());
        for (ResettleMail resettleMail : resettleMails) {
            EmailSender.send(resettleMail.getHost(), resettleMail.getSendAddress(), resettleMail.getAddress(), resettleMail.getUser(), resettleMail.getPassword(), "重新派奖",
                    "操作员:" + operator + " 彩种名:" + lotteryTitle + " 奖期:" + seasonId + " 重新派奖内容:" + openNum + " 操作时间:" + DateUtils.format(new Date()));
        }
        return Jsoner.success();
    }

    public void save(AmountChange m) {
        this.settlementDao.save(m);
    }

    public List<AmountChange> list(Page p, CommonModel m) {
        return this.settlementDao.list(p, m);
    }

    public List<AllChange> listAll(String account, int status, Date begin, Date end, Page p) {
        return this.settlementDao.listAll(account, status, begin, end, p);
    }

    public AmountChange find(Integer id) {
        return this.settlementDao.find(id);
    }

    public List<AmountChange> adminList(Page p, CommonModel m) {
        return this.settlementDao.adminList(p, m);
    }

    public String jinuoZssc(String openNum) {
        List<Integer> lst = ListUtils.toIntList(openNum);
        List<Integer> lstInt = new ArrayList<>();
        Collections.sort(lst);
        try {
            lstInt.add((lst.get(0) + lst.get(1) + lst.get(2) + lst.get(3)) % 10);
            lstInt.add((lst.get(4) + lst.get(5) + lst.get(6) + lst.get(7)) % 10);
            lstInt.add((lst.get(8) + lst.get(9) + lst.get(10) + lst.get(11)) % 10);
            lstInt.add((lst.get(12) + lst.get(13) + lst.get(14) + lst.get(15)) % 10);
            lstInt.add((lst.get(16) + lst.get(17) + lst.get(18) + lst.get(19)) % 10);
        } catch (IllegalArgumentException ex) {
            return null;
        }
        return ListUtils.toString(lstInt, ",");
    }

    public List<AllChange> newAdminList(Page p, CommonModel m) {
        return this.settlementDao.newAdminList(p, m);
    }


}
