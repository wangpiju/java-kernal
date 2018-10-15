package com.hs3.service.newReport;

import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.finance.RechargeDao;
import com.hs3.dao.lotts.BetDao;
import com.hs3.dao.lotts.SettlementDao;
import com.hs3.dao.report.AgentLotteryReportDao;
import com.hs3.dao.report.AgentReportDao;
import com.hs3.dao.report.CpsReportDao;
import com.hs3.dao.report.OnlineDao;
import com.hs3.dao.sys.LogAllDao;
import com.hs3.dao.user.UserDao;
import com.hs3.dao.user.UserSubsetDao;
import com.hs3.db.Page;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.finance.Recharge;
import com.hs3.entity.lotts.AmountChange;
import com.hs3.entity.lotts.Bet;
import com.hs3.entity.report.*;
import com.hs3.entity.sys.LogAll;
import com.hs3.entity.users.User;
import com.hs3.entity.users.UserSubset;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.models.report.Online;
import com.hs3.utils.BeanZUtils;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;
import com.hs3.utils.entity.UnderUser;
import nl.bitwalker.useragentutils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

@Service("cpsReportService")
public class CpsReportService {

    @Autowired
    private FinanceChangeDao financeChangeDao;

    @Autowired
    private SettlementDao settlementDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BetDao betDao;

    @Autowired
    private RechargeDao rechargeDao;

    @Autowired
    private OnlineDao onlineDao;

    @Autowired
    private LogAllDao logAllDao;

    @Autowired
    private CpsReportDao cpsReportDao;

    @Autowired
    private AgentReportDao agentReportDao;

    @Autowired
    private UserSubsetDao userSubsetDao;

    @Autowired
    private AgentLotteryReportDao agentLotteryReportDao;


    //综合报表（旧）
    public HashMap<String, Object> cpsReport(Date beginDate, Date endDate, Date beginDateThisM, Date endDateThisM, Date beginDateLastM, Date endDateLastM){

        HashMap<String, Object> cpsReportMap = new HashMap<String, Object>();

        BigDecimal profitAndLoss = BigDecimal.ZERO;//损益
        BigDecimal grossRate = BigDecimal.ZERO;//毛率
        BigDecimal profit = BigDecimal.ZERO;//盈利
        BigDecimal earningsRatio = BigDecimal.ZERO;//盈率

        String betAmount = "";//投注金额

        String winningAmount = "";//中奖金额
        //---------------------------------------------------------------

        String rechargeAmount = "";//充值金额 ******** 【同时计算充值笔数】
        String bankRechargeAmount = "";//银行充值
        String onlineRechargeAmount = "";//在线充值
        String rechargeFee = "";//充值手续费
        String cashRecharge = "";//现金充值

        String withdrawalAmount = "";//提款成功【同时计算提现笔数】

        String rebateAmount = "";//返点金额类：下级投注返点

        String activityAmount = "";//活动派发
        //---------------------------------------------------------------

        String rechargeAmountNum = "";//充值笔数

        String withdrawalAmountNum = "";//提现笔数

        String administratorDeduction = "";//管理员扣减

        String refuseAmount = "";//拒绝金额 ********
        String refuseWithdrawalAmount = "";//拒绝提款金额类：提款失败，管理员拒绝其提款
        String refuseRechargeAmount = "";//拒绝充值金额类：拒绝充值

        String registrationData = "";//注册人数

        String firstChargeData = "";//首充人数

        String onlineUserData = "";//在线人数
        //---------------------------------------------------------------

        String bettingAmount = "";//投注单量

        String withdrawalBetAmount = "";//撤单金额 ******** 4为“个人撤单”，5为“系统撤单”
        String withdrawalBetUser = "";//个人撤单
        String withdrawalBetSys = "";//系统撤单

        String userAmount = "";//会员余额

        //---------------------------------------------------------------


        String profitThisMonth = "";//本月盈利

        String earningsRatioThisMonth = "";//本月盈率

        String profitAndLossThisMonth = "";//本月损益

        String grossRateThisMonth = "";//本月毛率


        String profitLastMonth = "";//上月盈利

        String earningsRatioLastMonth = "";//上月盈率

        String profitAndLossLastMonth = "";//上月损益

        String grossRateLastMonth = "";//上月毛率

        //---------------------------------------------------------------

        BigDecimal systemDividendsB = BigDecimal.ZERO;//系统分红
        BigDecimal claimsRechargeB = BigDecimal.ZERO;//理赔充值
        BigDecimal privateReturnB = BigDecimal.ZERO;//私返
        BigDecimal consumerCommissionB = BigDecimal.ZERO;//消费佣金


        //综合报表需要的充值帐变
        //充值金额类：银行充值、在线充值、 充值手续费、现金充值/12、11、13、21【充值笔数】
        //提现金额类：提款成功/18【提现笔数】
        //活动礼金类：活动派发/19
        //拒绝提款金额类：提款失败/17
        //盈利赢率的计算：系统分红、理赔充值、私返、消费佣金/22、20、41、25
        //管理员扣减类：管理员扣减/37
        List<FinanceChange> cpsReportFinanceChangeL = financeChangeDao.cpsReportFinanceChange(beginDate, endDate);
        //综合报表需要的彩票帐变////返点金额类：下级投注返点/3
        List<AmountChange> cpsReportAmountChangeL = settlementDao.cpsReportAmountChange(beginDate, endDate);
        //综合报表需要的充值信息列表//拒绝充值金额类：拒绝充值/1
        List<Recharge> cpsReportRechargeL = rechargeDao.cpsReportRecharge(beginDate, endDate);
        //综合报表需要的投注信息列表，数据中状态3为“未开始”，4为“个人撤单”，5为“系统撤单”
        List<Bet> cpsReportBetL = betDao.cpsReportBet(beginDate, endDate);
        //首充人数
        List<Map<String, Object>> rechargeFirstUserL = userDao.rechargeFirstUser(null, beginDate, endDate);
        //注册人数
        List<Map<String, Object>> regUserL = userDao.regUser(null, beginDate, endDate);
        //会员余额
        Map<String, Object> amountAllUser = userDao.amountUser(null);
        //当前在线人员信息
        List<Online> onlineL = onlineDao.list(null, null, null, null, null);


        //处理充值金额类数据
        //充值金额类：银行充值、在线充值、 充值手续费、现金充值/12、11、13、21【充值笔数】
        //提现金额类：提款成功/18【提款笔数】
        //活动礼金类：活动派发/19
        //拒绝提款金额类：提款失败/17
        //盈利赢率的计算：系统分红、理赔充值、私返、消费佣金/22、20、41、25

        BigDecimal rechargeAmountB = BigDecimal.ZERO;//充值金额
        int rechargeAmountUserNumInt = 0;//充值人数
        int rechargeAmountNumInt = 0;//【充值笔数】

        BigDecimal bankRechargeAmountB = BigDecimal.ZERO;//银行充值金额
        int bankRechargeAmountNum = 0;//银行充值人数
        Set bankRechargeAmountUserSet = new HashSet();
        BigDecimal onlineRechargeAmountB = BigDecimal.ZERO;//在线充值金额
        int onlineRechargeAmountNum = 0;//在线充值人数
        Set onlineRechargeAmountUserSet = new HashSet();
        BigDecimal rechargeFeeB = BigDecimal.ZERO;//充值手续费金额
        int rechargeFeeNum = 0;//充值手续费人数
        Set rechargeFeeUserSet = new HashSet();
        BigDecimal cashRechargeB = BigDecimal.ZERO;//现金充值金额
        int cashRechargeNum = 0;//现金充值人数
        Set cashRechargeUserSet = new HashSet();

        BigDecimal withdrawalAmountB = BigDecimal.ZERO;//提款金额
        int withdrawalAmountUserNumInt = 0;//提款人数
        Set withdrawalAmountUserSet = new HashSet();
        int withdrawalAmountNumInt = 0;//【提款笔数】

        BigDecimal activityAmountB = BigDecimal.ZERO;//活动派发金额
        int activityAmountUserNum = 0;//活动派发人数
        Set activityAmountUserSet = new HashSet();

        BigDecimal refuseWithdrawalAmountB = BigDecimal.ZERO;//拒绝金额1：提款失败金额
        int refuseWithdrawalAmountNum = 0;//拒绝金额1：提款失败人数
        Set refuseWithdrawalAmountUserSet = new HashSet();

        BigDecimal administratorDeductionB = BigDecimal.ZERO;//管理员扣减
        int administratorDeductionNum = 0;//管理员扣减人数
        Set administratorDeductionUserSet = new HashSet();

        Integer accountChangeTypeId = 0;
        for(FinanceChange financeChange: cpsReportFinanceChangeL){
            accountChangeTypeId = financeChange.getAccountChangeTypeId();
            if(accountChangeTypeId == 12 || accountChangeTypeId == 11 || accountChangeTypeId == 13 || accountChangeTypeId == 21){
                if(accountChangeTypeId == 12){//银行充值
                    bankRechargeAmountB = bankRechargeAmountB.add(financeChange.getChangeAmount());
                    bankRechargeAmountUserSet.add(financeChange.getChangeUser());
                }else if(accountChangeTypeId == 11){//在线充值
                    onlineRechargeAmountB = onlineRechargeAmountB.add(financeChange.getChangeAmount());
                    onlineRechargeAmountUserSet.add(financeChange.getChangeUser());
                }else if(accountChangeTypeId == 13){//充值手续费
                    rechargeFeeB = rechargeFeeB.add(financeChange.getChangeAmount());
                    rechargeFeeUserSet.add(financeChange.getChangeUser());
                }else if(accountChangeTypeId == 21){//现金充值
                    cashRechargeB = cashRechargeB.add(financeChange.getChangeAmount());
                    cashRechargeUserSet.add(financeChange.getChangeUser());
                }
                rechargeAmountNumInt ++;
            }else if(accountChangeTypeId == 18){//提款成功
                withdrawalAmountB = withdrawalAmountB.add(financeChange.getChangeAmount());
                withdrawalAmountUserSet.add(financeChange.getChangeUser());
                withdrawalAmountNumInt ++;
            }else if(accountChangeTypeId == 19){//活动派发
                activityAmountB = activityAmountB.add(financeChange.getChangeAmount());
                activityAmountUserSet.add(financeChange.getChangeUser());
            }else if(accountChangeTypeId == 17){//提款失败
                refuseWithdrawalAmountB = refuseWithdrawalAmountB.add(financeChange.getChangeAmount());
                refuseWithdrawalAmountUserSet.add(financeChange.getChangeUser());
            }else if(accountChangeTypeId == 22){//系统分红
                systemDividendsB = systemDividendsB.add(financeChange.getChangeAmount());
            }else if(accountChangeTypeId == 20){//理赔充值
                claimsRechargeB = claimsRechargeB.add(financeChange.getChangeAmount());
            }else if(accountChangeTypeId == 41){//私返
                privateReturnB = privateReturnB.add(financeChange.getChangeAmount());
            }else if(accountChangeTypeId == 25){//消费佣金
                consumerCommissionB = consumerCommissionB.add(financeChange.getChangeAmount());
            }else if(accountChangeTypeId == 37){//管理员扣减
                administratorDeductionB = administratorDeductionB.add(financeChange.getChangeAmount());
                administratorDeductionUserSet.add(financeChange.getChangeUser());
            }
        }

        bankRechargeAmountNum = bankRechargeAmountUserSet.size();
        onlineRechargeAmountNum = onlineRechargeAmountUserSet.size();
        rechargeFeeNum = rechargeFeeUserSet.size();
        cashRechargeNum = cashRechargeUserSet.size();

        //充值总金额
        rechargeAmountB = rechargeAmountB.add(bankRechargeAmountB).add(onlineRechargeAmountB).add(rechargeFeeB).add(cashRechargeB);
        //充值总人数
        rechargeAmountUserNumInt = bankRechargeAmountNum + onlineRechargeAmountNum + rechargeFeeNum + cashRechargeNum;

        rechargeAmount = rechargeAmountB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + rechargeAmountUserNumInt + "人";//充值金额 ******** 【同时计算充值笔数】

        bankRechargeAmount = bankRechargeAmountB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + bankRechargeAmountNum + "人";//银行充值
        onlineRechargeAmount = onlineRechargeAmountB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + onlineRechargeAmountNum + "人";//在线充值
        rechargeFee = rechargeFeeB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + rechargeFeeNum + "人";//充值手续费
        cashRecharge = cashRechargeB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + cashRechargeNum + "人";//现金充值

        rechargeAmountNum = rechargeAmountNumInt + "/" + rechargeAmountUserNumInt + "人";//充值笔数

        withdrawalAmountUserNumInt = withdrawalAmountUserSet.size();
        withdrawalAmount = withdrawalAmountB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + withdrawalAmountUserNumInt + "人";//提款成功【同时计算提现笔数】
        withdrawalAmountNum = withdrawalAmountNumInt + "/" + withdrawalAmountUserNumInt + "人";//充值笔数

        activityAmountUserNum = activityAmountUserSet.size();
        activityAmount = activityAmountB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + activityAmountUserNum + "人";//活动派发

        refuseWithdrawalAmountNum = refuseWithdrawalAmountUserSet.size();
        refuseWithdrawalAmount = refuseWithdrawalAmountB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + refuseWithdrawalAmountNum + "人";//拒绝提款金额类：提款失败，管理员拒绝其提款


        //投注帐变涉及综合统计的的变更金额数据处理
        //返点金额类：下级投注返点/3
        BigDecimal rebateAmountB = BigDecimal.ZERO;//返点金额
        int rebateAmountNum = 0;//返点金额涉及数
        Set rebateAmountUserSet = new HashSet();
        Integer accountChangeTypeId_Z = 0;
        for(AmountChange amountChange: cpsReportAmountChangeL){
            accountChangeTypeId_Z = amountChange.getAccountChangeTypeId();
            if(accountChangeTypeId_Z == 3){//下级投注返点
                rebateAmountB = rebateAmountB.add(amountChange.getChangeAmount());
                rebateAmountUserSet.add(amountChange.getChangeUser());
            }
        }

        rebateAmountNum = rebateAmountUserSet.size();
        rebateAmount = rebateAmountB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + rebateAmountNum + "人";//返点金额类：下级投注返点

        //管理员扣减
        administratorDeductionNum = administratorDeductionUserSet.size();
        administratorDeduction = administratorDeductionB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + administratorDeductionNum + "人";//管理员扣减


        //综合报表需要的充值数据处理
        //拒绝充值金额类2：拒绝充值申请/1
        BigDecimal refuseRechargeAmountB = BigDecimal.ZERO;//返点金额
        int refuseRechargeAmountNum = 0;//拒绝充值申请涉及数
        Set refuseRechargeAmountUserSet = new HashSet();
        Integer status = 0;
        for(Recharge recharge: cpsReportRechargeL){
            status = recharge.getStatus();
            if(status == 1){//拒绝充值申请
                refuseRechargeAmountB = refuseRechargeAmountB.add(recharge.getAmount());
                refuseRechargeAmountUserSet.add(recharge.getAccount());
            }
        }

        refuseRechargeAmountNum = refuseRechargeAmountUserSet.size();
        refuseRechargeAmount = refuseRechargeAmountB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + refuseRechargeAmountNum + "人";//拒绝充值金额类：拒绝充值

        refuseAmount = refuseWithdrawalAmountB.add(refuseRechargeAmountB).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + (refuseWithdrawalAmountNum + refuseRechargeAmountNum) + "人";//拒绝金额 ********

        onlineUserData =  onlineL.size() + "人"; //在线人数


        registrationData = regUserL.size() + "人"; //注册人数

        firstChargeData = rechargeFirstUserL.size() + "人"; //首充人数
        if (amountAllUser.get("allAmount") != null) {
            userAmount = new DecimalFormat("#.##").format(Double.parseDouble(String.valueOf(amountAllUser.get("allAmount"))));//会员余额
        }


        //投注金额/中奖金额/投注单量/撤单金额，数据中状态3为“未开始”，4为“个人撤单”，5为“系统撤单”，1为“已中奖”

        BigDecimal betAmountB = BigDecimal.ZERO;//投注金额
        int betAmountNum = 0;//投注人数
        Set betAmountUserSet = new HashSet();

        int bettingAmountNumInt = cpsReportBetL.size();//投注数量

        BigDecimal winningAmountB = BigDecimal.ZERO;//中奖金额
        int winningAmountNum = 0;//中奖人数
        Set winningAmountUserSet = new HashSet();

        BigDecimal withdrawalBetUserB = BigDecimal.ZERO;//个人撤单金额
        int withdrawalBetUserNum = 0;//个人撤单人数
        Set withdrawalBetUserSet = new HashSet();

        BigDecimal withdrawalBetSysB = BigDecimal.ZERO;//系统撤单金额
        int withdrawalBetSysNum = 0;//系统撤单人数
        Set withdrawalBetSysSet = new HashSet();

        Integer bet_status = 0;
        for(Bet betObj: cpsReportBetL){
            bet_status = betObj.getStatus();
            if(bet_status == 1 || bet_status == 2 || bet_status == 0 || bet_status == 6){
                if(bet_status == 1) {
                    winningAmountB = winningAmountB.add(betObj.getWin());
                    winningAmountUserSet.add(betObj.getAccount());
                }
                betAmountB = betAmountB.add(betObj.getAmount());
                betAmountUserSet.add(betObj.getAccount());
            }else if(bet_status == 4 || bet_status == 5){
                if(bet_status == 4){
                    withdrawalBetUserB = withdrawalBetUserB.add(betObj.getAmount());
                    withdrawalBetUserSet.add(betObj.getAccount());
                }else if(bet_status == 5){
                    withdrawalBetSysB = withdrawalBetSysB.add(betObj.getAmount());
                    withdrawalBetSysSet.add(betObj.getAccount());
                }
            }
        }

        betAmountNum = betAmountUserSet.size();
        betAmount = betAmountB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + betAmountNum + "人";//投注金额

        winningAmountNum = winningAmountUserSet.size();
        winningAmount = winningAmountB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + winningAmountNum + "人";//中奖金额

        bettingAmount = bettingAmountNumInt + "/" + betAmountNum + "人";//投注单量


        withdrawalBetUserNum = withdrawalBetUserSet.size();
        withdrawalBetSysNum = withdrawalBetSysSet.size();
        withdrawalBetAmount = (withdrawalBetUserB.add(withdrawalBetSysB)).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + (withdrawalBetUserNum + withdrawalBetSysNum) + "人";//撤单金额 ******** 4为“个人撤单”，5为“系统撤单”
        withdrawalBetUser = withdrawalBetUserB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + withdrawalBetUserNum + "人";//个人撤单
        withdrawalBetSys = withdrawalBetSysB.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "/" + withdrawalBetSysNum + "人";//系统撤单


        //损益
        profitAndLoss = betAmountB.subtract(rebateAmountB).subtract(winningAmountB);

        //毛率
        if(betAmountB.compareTo(BigDecimal.ZERO) != 0 ) {
            grossRate = profitAndLoss.divide(betAmountB, 2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal("100"));
        }

        //盈利
        profit = profitAndLoss.subtract(activityAmountB).subtract(systemDividendsB).subtract(claimsRechargeB).subtract(privateReturnB).subtract(consumerCommissionB).add(administratorDeductionB);

        //盈率
        if(betAmountB.compareTo(BigDecimal.ZERO) != 0 ) {
            earningsRatio = profit.divide(betAmountB, 2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal("100"));
        }

        //HashMap<String, Object> profitThisM = cpsProfitReport(beginDateThisM, endDateThisM);
        //profitThisMonth = String.valueOf(profitThisM.get("profit"));//本月盈利
        //earningsRatioThisMonth = String.valueOf(profitThisM.get("earningsRatio"));//本月盈率
        //profitAndLossThisMonth = String.valueOf(profitThisM.get("profitAndLoss"));//本月损益
        //grossRateThisMonth = String.valueOf(profitThisM.get("grossRate"));//本月毛率

        //HashMap<String, Object> profitLastM = cpsProfitReport(beginDateLastM, endDateLastM);
        //profitLastMonth = String.valueOf(profitLastM.get("profit"));//上月盈利
        //earningsRatioLastMonth = String.valueOf(profitLastM.get("earningsRatio"));//上月盈率
        //profitAndLossLastMonth = String.valueOf(profitLastM.get("profitAndLoss"));//上月损益
        //grossRateLastMonth = String.valueOf(profitLastM.get("grossRate"));//上月毛率



        cpsReportMap.put("profit",profit.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        cpsReportMap.put("earningsRatio",earningsRatio.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        cpsReportMap.put("profitAndLoss",profitAndLoss.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        cpsReportMap.put("grossRate",grossRate.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        cpsReportMap.put("betAmount",betAmount);
        cpsReportMap.put("winningAmount",winningAmount);
        cpsReportMap.put("rechargeAmount",rechargeAmount);
        cpsReportMap.put("bankRechargeAmount",bankRechargeAmount);
        cpsReportMap.put("onlineRechargeAmount",onlineRechargeAmount);
        cpsReportMap.put("rechargeFee",rechargeFee);
        cpsReportMap.put("cashRecharge",cashRecharge);
        cpsReportMap.put("withdrawalAmount",withdrawalAmount);
        cpsReportMap.put("rebateAmount",rebateAmount);
        cpsReportMap.put("activityAmount",activityAmount);
        cpsReportMap.put("rechargeAmountNum",rechargeAmountNum);
        cpsReportMap.put("withdrawalAmountNum",withdrawalAmountNum);
        cpsReportMap.put("administratorDeduction",administratorDeduction);
        cpsReportMap.put("refuseAmount",refuseAmount);
        cpsReportMap.put("refuseWithdrawalAmount",refuseWithdrawalAmount);
        cpsReportMap.put("refuseRechargeAmount",refuseRechargeAmount);
        cpsReportMap.put("registrationData",registrationData);
        cpsReportMap.put("firstChargeData",firstChargeData);
        cpsReportMap.put("onlineUserData",onlineUserData);
        cpsReportMap.put("bettingAmount",bettingAmount);
        cpsReportMap.put("withdrawalBetAmount",withdrawalBetAmount);
        cpsReportMap.put("withdrawalBetUser",withdrawalBetUser);
        cpsReportMap.put("withdrawalBetSys",withdrawalBetSys);
        cpsReportMap.put("userAmount",userAmount);
        cpsReportMap.put("profitThisMonth",profitThisMonth);
        cpsReportMap.put("earningsRatioThisMonth",earningsRatioThisMonth);
        cpsReportMap.put("profitAndLossThisMonth",profitAndLossThisMonth);
        cpsReportMap.put("grossRateThisMonth",grossRateThisMonth);
        cpsReportMap.put("profitLastMonth",profitLastMonth);
        cpsReportMap.put("earningsRatioLastMonth",earningsRatioLastMonth);
        cpsReportMap.put("profitAndLossLastMonth",profitAndLossLastMonth);
        cpsReportMap.put("grossRateLastMonth",grossRateLastMonth);
        return cpsReportMap;
    }


    //
    public HashMap<String, Object> cpsProfitReport(Date beginDate, Date endDate){

        HashMap<String, Object> cpsReportMap = new HashMap<String, Object>();

        BigDecimal profitAndLoss = BigDecimal.ZERO;//损益
        BigDecimal grossRate = BigDecimal.ZERO;//毛率
        BigDecimal profit = BigDecimal.ZERO;//盈利
        BigDecimal earningsRatio = BigDecimal.ZERO;//盈率

        BigDecimal systemDividendsB = BigDecimal.ZERO;//系统分红
        BigDecimal claimsRechargeB = BigDecimal.ZERO;//理赔充值
        BigDecimal privateReturnB = BigDecimal.ZERO;//私返
        BigDecimal consumerCommissionB = BigDecimal.ZERO;//消费佣金


        //综合报表需要的充值帐变
        //充值金额类：银行充值、在线充值、 充值手续费、现金充值/12、11、13、21【充值笔数】
        //提现金额类：提款成功/18【提现笔数】
        //活动礼金类：活动派发/19
        //拒绝提款金额类：提款失败/17
        //盈利赢率的计算：系统分红、理赔充值、私返、消费佣金/22、20、41、25
        //管理员扣减类：管理员扣减/37
        List<FinanceChange> cpsReportFinanceChangeL = financeChangeDao.cpsReportFinanceChange(beginDate, endDate);
        //综合报表需要的彩票帐变////返点金额类：下级投注返点/3
        List<AmountChange> cpsReportAmountChangeL = settlementDao.cpsReportAmountChange(beginDate, endDate);
        //综合报表需要的投注信息列表，数据中状态3为“未开始”，4为“个人撤单”，5为“系统撤单”
        List<Bet> cpsReportBetL = betDao.cpsReportBet(beginDate, endDate);


        //处理充值金额类数据
        BigDecimal activityAmountB = BigDecimal.ZERO;//活动派发金额

        BigDecimal administratorDeductionB = BigDecimal.ZERO;//管理员扣减

        Integer accountChangeTypeId = 0;
        for(FinanceChange financeChange: cpsReportFinanceChangeL){
            accountChangeTypeId = financeChange.getAccountChangeTypeId();
            if(accountChangeTypeId == 19){//活动派发
                activityAmountB = activityAmountB.add(financeChange.getChangeAmount());
            }else if(accountChangeTypeId == 22){//系统分红
                systemDividendsB = systemDividendsB.add(financeChange.getChangeAmount());
            }else if(accountChangeTypeId == 20){//理赔充值
                claimsRechargeB = claimsRechargeB.add(financeChange.getChangeAmount());
            }else if(accountChangeTypeId == 41){//私返
                privateReturnB = privateReturnB.add(financeChange.getChangeAmount());
            }else if(accountChangeTypeId == 25){//消费佣金
                consumerCommissionB = consumerCommissionB.add(financeChange.getChangeAmount());
            }else if(accountChangeTypeId == 37){//管理员扣减
                administratorDeductionB = administratorDeductionB.add(financeChange.getChangeAmount());
            }
        }

        //投注帐变涉及综合统计的的变更金额数据处理
        //返点金额类：下级投注返点/3
        BigDecimal rebateAmountB = BigDecimal.ZERO;//返点金额
        Integer accountChangeTypeId_Z = 0;
        for(AmountChange amountChange: cpsReportAmountChangeL){
            accountChangeTypeId_Z = amountChange.getAccountChangeTypeId();
            if(accountChangeTypeId_Z == 3){//下级投注返点
                rebateAmountB = rebateAmountB.add(amountChange.getChangeAmount());
            }
        }

        //投注金额/中奖金额/投注单量/撤单金额，数据中状态3为“未开始”，4为“个人撤单”，5为“系统撤单”，1为“已中奖”

        BigDecimal betAmountB = BigDecimal.ZERO;//投注金额

        BigDecimal winningAmountB = BigDecimal.ZERO;//中奖金额

        Integer bet_status = 0;
        for(Bet betObj: cpsReportBetL){
            bet_status = betObj.getStatus();
            if(bet_status == 1 || bet_status == 2 || bet_status == 0 || bet_status == 6){
                if(bet_status == 1) {
                    winningAmountB = winningAmountB.add(betObj.getWin());
                }
                betAmountB = betAmountB.add(betObj.getAmount());
            }
        }

        //损益
        profitAndLoss = betAmountB.subtract(rebateAmountB).subtract(winningAmountB);

        //毛率
        if(betAmountB.compareTo(BigDecimal.ZERO) != 0 ){
            grossRate = profitAndLoss.divide(betAmountB, 2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal("100"));
        }

        //盈利
        profit = profitAndLoss.subtract(activityAmountB).subtract(systemDividendsB).subtract(claimsRechargeB).subtract(privateReturnB).subtract(consumerCommissionB).add(administratorDeductionB);

        //盈率
        if(betAmountB.compareTo(BigDecimal.ZERO) != 0 ){
            earningsRatio = profit.divide(betAmountB, 2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal("100"));
        }

        cpsReportMap.put("profitAndLoss",profitAndLoss.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        cpsReportMap.put("grossRate",grossRate.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        cpsReportMap.put("profit",profit.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        cpsReportMap.put("earningsRatio",earningsRatio.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        return cpsReportMap;
    }


    //综合报表（新）
    public Map<String, Object> cpsReport_new(Date beginDate, Date endDate){
        Map<String, Object> cpsReport = cpsReportDao.cpsReport(beginDate, endDate);
        return cpsReport;
    }

    public void addWhenNotExists(String reportDate) throws Exception {
        if (this.cpsReportDao.listByCond(reportDate, reportDate, null).isEmpty()) {
            add(reportDate);
        }
    }

    public void add(String reportDate) throws Exception {
        this.cpsReportDao.delete(reportDate);
        CpsReport m = this.cpsReportDao.find(reportDate);
        this.cpsReportDao.save(m);
    }

    public CpsReport fromList(Date reportDateBegin, Date reportDateEnd, Page page)
            throws BaseCheckException {
        if ((reportDateBegin == null) || (reportDateEnd == null)) {
            throw new BaseCheckException("统计时间不能为空！");
        }
        if (reportDateEnd.before(reportDateBegin)) {
            throw new BaseCheckException("统计时间起不能大于统计时间止！");
        }
        String begin = DateUtils.format(reportDateBegin, "yyyy-MM-dd");
        String end = DateUtils.format(reportDateEnd, "yyyy-MM-dd");

        List<CpsReport> cpsReportList = this.cpsReportDao.listByCond(begin, end, null);

        BigDecimal rechargeBankAmount = BigDecimal.ZERO;//银行充值金额
        Integer rechargeBankNum = 0;//银行充值人数---
        Integer rechargeBankOrNum = 0;//充值笔数
        BigDecimal rechargeOnlineAmount = BigDecimal.ZERO;//在线充值金额
        Integer rechargeOnlineNum = 0;//在线充值人数---
        Integer rechargeOnlineOrNum = 0;//充值笔数
        BigDecimal rechargeHandAmount = BigDecimal.ZERO;//充值手续费金额
        Integer rechargeHandNum = 0;//充值手续费人数---
        Integer rechargeHandOrNum = 0;//充值笔数
        BigDecimal rechargeCashAmount = BigDecimal.ZERO;//现金充值金额
        Integer rechargeCashNum = 0;//现金充值人数---
        Integer rechargeCashOrNum = 0;//充值笔数
        Integer rechargeAllNum = 0;//

        BigDecimal withdrawAmount = BigDecimal.ZERO;//提现金额
        Integer withdrawNum = 0;//提现人数---
        Integer withdrawOrNum = 0;//提现笔数
        BigDecimal rebateAmount = BigDecimal.ZERO;//返点金额
        Integer rebateNum = 0;//返点人数---
        BigDecimal distributeActivityAmount = BigDecimal.ZERO;//活动派发金额
        Integer distributeActivityNum = 0;//活动派发人数---
        BigDecimal distributeClaimAmount = BigDecimal.ZERO;//理赔充值金额
        Integer distributeClaimNum = 0;//理赔充值人数---
        BigDecimal distributeDailyAmount = BigDecimal.ZERO;//日工资金额
        Integer distributeDailyNum = 0;//日工资人数---
        BigDecimal distributeDividendAmount = BigDecimal.ZERO;//周期分红金额
        Integer distributeDividendNum = 0;//周期分红人数---
        BigDecimal distributeAdminAmount = BigDecimal.ZERO;//管理员派发金额
        Integer distributeAdminNum = 0;//管理员派发人数---
        Integer distributeAllNum = 0;//

        BigDecimal deductionAdministraAmount = BigDecimal.ZERO;//行政提出金额
        Integer deductionAdministraNum = 0;//行政提出人数---
        BigDecimal deductionAdminAmount = BigDecimal.ZERO;//管理员扣减金额
        Integer deductionAdminNum = 0;//管理员扣减人数---
        Integer deductionAllNum = 0;//

        Integer regNum = 0;//注册人数
        Integer firstChargeNum = 0;//首充人数
        Integer betNum = 0;//投注单量
        BigDecimal betAmount = BigDecimal.ZERO;//投注金额
        Integer betPerNum = 0;//投注人数---
        BigDecimal winAmount = BigDecimal.ZERO;//中奖金额
        Integer winPerNum = 0;//中奖人数---
        BigDecimal withdrawalPerAmount = BigDecimal.ZERO;//个人撤单金额
        Integer withdrawalPerNum = 0;//个人撤单人数---
        BigDecimal withdrawalSysAmount = BigDecimal.ZERO;//系统撤单金额
        Integer withdrawalSysNum = 0;//系统撤单人数---
        Integer withdrawalAllNum = 0;//

        for(CpsReport cpsReportObj: cpsReportList){
            rechargeBankAmount = rechargeBankAmount.add(cpsReportObj.getRechargeBankAmount());//银行充值金额
            rechargeBankOrNum = rechargeBankOrNum + cpsReportObj.getRechargeBankOrNum();//充值笔数
            rechargeOnlineAmount = rechargeOnlineAmount.add(cpsReportObj.getRechargeOnlineAmount());//在线充值金额
            rechargeOnlineOrNum = rechargeOnlineOrNum + cpsReportObj.getRechargeOnlineOrNum();//充值笔数
            rechargeHandAmount = rechargeHandAmount.add(cpsReportObj.getRechargeHandAmount());//充值手续费金额
            rechargeHandOrNum = rechargeHandOrNum + cpsReportObj.getRechargeHandOrNum();//充值笔数
            rechargeCashAmount = rechargeCashAmount.add(cpsReportObj.getRechargeCashAmount());//现金充值金额
            rechargeCashOrNum = rechargeCashOrNum + cpsReportObj.getRechargeCashOrNum();//充值笔数
            withdrawAmount = withdrawAmount.add(cpsReportObj.getWithdrawAmount());//提现金额
            withdrawOrNum = withdrawOrNum + cpsReportObj.getWithdrawOrNum();//提现笔数
            rebateAmount = rebateAmount.add(cpsReportObj.getRebateAmount());//返点金额
            distributeActivityAmount = distributeActivityAmount.add(cpsReportObj.getDistributeActivityAmount());//活动派发金额
            distributeClaimAmount = distributeClaimAmount.add(cpsReportObj.getDistributeClaimAmount());//理赔充值金额
            distributeDailyAmount = distributeDailyAmount.add(cpsReportObj.getDistributeDailyAmount());//日工资金额
            distributeDividendAmount = distributeDividendAmount.add(cpsReportObj.getDistributeDividendAmount());//周期分红金额
            distributeAdminAmount = distributeAdminAmount.add(cpsReportObj.getDistributeAdminAmount());//管理员派发金额
            deductionAdministraAmount = deductionAdministraAmount.add(cpsReportObj.getDeductionAdministraAmount());//行政提出金额
            deductionAdminAmount = deductionAdminAmount.add(cpsReportObj.getDeductionAdminAmount());//管理员扣减金额
            regNum = regNum + cpsReportObj.getRegNum();//注册人数
            firstChargeNum = firstChargeNum + cpsReportObj.getFirstChargeNum();//首充人数
            betNum = betNum + cpsReportObj.getBetNum();//投注单量
            betAmount = betAmount.add(cpsReportObj.getBetAmount());//投注金额
            winAmount = winAmount.add(cpsReportObj.getWinAmount());//中奖金额
            withdrawalPerAmount = withdrawalPerAmount.add(cpsReportObj.getWithdrawalPerAmount());//个人撤单金额
            withdrawalSysAmount = withdrawalSysAmount.add(cpsReportObj.getWithdrawalSysAmount());//系统撤单金额
        }

        Map<String, Object> cpsReportPerNum = this.cpsReportDao.cpsReportPerNum(reportDateBegin, reportDateEnd);
        rechargeBankNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("rechargeBankNum")));//银行充值人数---
        rechargeOnlineNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("rechargeOnlineNum")));//在线充值人数---
        rechargeHandNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("rechargeHandNum")));//充值手续费人数---
        rechargeCashNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("rechargeCashNum")));//现金充值人数---
        rechargeAllNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("rechargeAllNum")));//

        withdrawNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("withdrawNum")));//提现人数---
        rebateNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("rebateNum")));//返点人数---
        distributeActivityNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("distributeActivityNum")));//活动派发人数---
        distributeClaimNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("distributeClaimNum")));//理赔充值人数---
        distributeDailyNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("distributeDailyNum")));//日工资人数---
        distributeDividendNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("distributeDividendNum")));//周期分红人数---
        distributeAdminNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("distributeAdminNum")));//管理员派发人数---
        distributeAllNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("distributeAllNum")));//

        deductionAdministraNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("deductionAdministraNum")));//行政提出人数---
        deductionAdminNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("deductionAdminNum")));//管理员扣减人数---
        deductionAllNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("deductionAllNum")));//

        betPerNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("betPerNum")));//投注人数---
        winPerNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("winPerNum")));//中奖人数---
        withdrawalPerNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("withdrawalPerNum")));//个人撤单人数---
        withdrawalSysNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("withdrawalSysNum")));//系统撤单人数---
        withdrawalAllNum = Integer.valueOf(String.valueOf(cpsReportPerNum.get("withdrawalAllNum")));//

        CpsReport cpsReport = new CpsReport();
        cpsReport.setRechargeBankAmount(rechargeBankAmount);
        cpsReport.setRechargeBankNum(rechargeBankNum);
        cpsReport.setRechargeBankOrNum(rechargeBankOrNum);
        cpsReport.setRechargeOnlineAmount(rechargeOnlineAmount);
        cpsReport.setRechargeOnlineNum(rechargeOnlineNum);
        cpsReport.setRechargeOnlineOrNum(rechargeOnlineOrNum);
        cpsReport.setRechargeHandAmount(rechargeHandAmount);
        cpsReport.setRechargeHandNum(rechargeHandNum);
        cpsReport.setRechargeHandOrNum(rechargeHandOrNum);
        cpsReport.setRechargeCashAmount(rechargeCashAmount);
        cpsReport.setRechargeCashNum(rechargeCashNum);
        cpsReport.setRechargeCashOrNum(rechargeCashOrNum);
        cpsReport.setRechargeAllNum(rechargeAllNum);

        cpsReport.setWithdrawAmount(withdrawAmount);
        cpsReport.setWithdrawNum(withdrawNum);
        cpsReport.setWithdrawOrNum(withdrawOrNum);
        cpsReport.setRebateAmount(rebateAmount);
        cpsReport.setRebateNum(rebateNum);
        cpsReport.setDistributeActivityAmount(distributeActivityAmount);
        cpsReport.setDistributeActivityNum(distributeActivityNum);
        cpsReport.setDistributeClaimAmount(distributeClaimAmount);
        cpsReport.setDistributeClaimNum(distributeClaimNum);
        cpsReport.setDistributeDailyAmount(distributeDailyAmount);
        cpsReport.setDistributeDailyNum(distributeDailyNum);
        cpsReport.setDistributeDividendAmount(distributeDividendAmount);
        cpsReport.setDistributeDividendNum(distributeDividendNum);
        cpsReport.setDistributeAdminAmount(distributeAdminAmount);
        cpsReport.setDistributeAdminNum(distributeAdminNum);
        cpsReport.setDistributeAllNum(distributeAllNum);

        cpsReport.setDeductionAdministraAmount(deductionAdministraAmount);
        cpsReport.setDeductionAdministraNum(deductionAdministraNum);
        cpsReport.setDeductionAdminAmount(deductionAdminAmount);
        cpsReport.setDeductionAdminNum(deductionAdminNum);
        cpsReport.setDeductionAllNum(deductionAllNum);

        cpsReport.setRegNum(regNum);
        cpsReport.setFirstChargeNum(firstChargeNum);
        cpsReport.setBetNum(betNum);
        cpsReport.setBetAmount(betAmount);
        cpsReport.setBetPerNum(betPerNum);
        cpsReport.setWinAmount(winAmount);
        cpsReport.setWinPerNum(winPerNum);
        cpsReport.setWithdrawalPerAmount(withdrawalPerAmount);
        cpsReport.setWithdrawalPerNum(withdrawalPerNum);
        cpsReport.setWithdrawalSysAmount(withdrawalSysAmount);
        cpsReport.setWithdrawalSysNum(withdrawalSysNum);
        cpsReport.setWithdrawalAllNum(withdrawalAllNum);

        return cpsReport;
    }


    //首充报表
    public List<Map<String, Object>> firstChargeReport(String account, String parentAccount, Integer sorting, Date beginDate, Date endDate, Page p){
        List<User> rechargeFirstUser = userDao.rechargeFirstUser(account, parentAccount, sorting, beginDate, endDate, p);
        List<Map<String, Object>> rechargeFirstUserMapList = null;
        if (rechargeFirstUser.size() > 0) {
            rechargeFirstUserMapList = new ArrayList<Map<String, Object>>();
            Map<String, Object> rechargeFirstUserMap = null;
            for (User user : rechargeFirstUser) {
                rechargeFirstUserMap = BeanZUtils.transBeanMap(user);
                String account_z = user.getAccount();

                Integer rechargeType = 100;
                String userAgent = "";

                Recharge recharge = rechargeDao.firstRechargeByAccount(account_z);
                Date createTime = null;
                if(!StrUtils.hasEmpty(new Object[]{recharge}) && !StrUtils.hasEmpty(new Object[]{recharge.getAccount()})) {
                    rechargeType = recharge.getRechargeType();
                    createTime = recharge.getCreateTime();
                }

                if (!StrUtils.hasEmpty(new Object[]{account_z}) && !StrUtils.hasEmpty(new Object[]{createTime})) {
                    LogAll logAll = logAllDao.findByAccountAndCreateTime(account_z, createTime);
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

                rechargeFirstUserMap.put("rechargeFirstTimeStr", DateUtils.format(user.getRechargeFirstTime()));
                rechargeFirstUserMap.put("regTimeStr", DateUtils.format(user.getRegTime()));
                rechargeFirstUserMap.put("rechargeType", rechargeType);
                rechargeFirstUserMap.put("userAgent", userAgent);
                rechargeFirstUserMapList.add(rechargeFirstUserMap);
            }
        }
        return rechargeFirstUserMapList;
    }


    //会员报表
    public List<Map<String, Object>> membershipReport(String account, String parentAccount, Integer sorting, Date beginDate, Date endDate, Page p){
        List<Map<String, Object>> membershipReport = userDao.membershipReport(account, parentAccount, sorting, beginDate, endDate, p);
        return membershipReport;
    }

    //新会员报表
    public List<MembershipReport> membershipReport_Z(String[] accountArr, String parentAccount, Integer sorting, Integer isBlurry, Date beginDate, Date endDate, Page p){
        List<MembershipReport> membershipReport = userDao.membershipReport_Z(accountArr, parentAccount, sorting, isBlurry, beginDate, endDate, p);
        return membershipReport;
    }



    public void addAgentsReportWhenNotExists(String reportDate) throws Exception {
        if (this.agentReportDao.findListByDate(reportDate, reportDate, null).isEmpty()) {
            addAgentsReport(reportDate);
        }
    }

    public void addAgentsReport(String reportDate) throws Exception {
        this.agentReportDao.delete(reportDate);
        List<AgentReport> mList = agentsReportByDate(reportDate);
        for(AgentReport agentReport: mList){
            this.agentReportDao.save(agentReport);
        }
    }

    //按日期获取代理报表
    public List<AgentReport> agentsReportByDate(String dateStr) throws ParseException {

        if(StrUtils.hasEmpty(new Object[]{dateStr})){
            throw new BaseCheckException("统计日期不能为空！");
        }

        List<AgentReport> agentTeamReportListX = new ArrayList<AgentReport>();

        Date beginDate = DateUtils.toDate(dateStr + " 00:00:00");
        Date endDate = DateUtils.toDate(dateStr + " 23:59:59");

        List<AgentReport> agentReportList = agentReportDao.agentReportList(null, beginDate, endDate, beginDate, endDate, null);

        List<AgentReport> agentReportListZ = new ArrayList<AgentReport>();
        for (AgentReport agentReport : agentReportList) {
            agentReportListZ.add(agentReport);
        }

        AgentReport agentReportObj = null;
        for (AgentReport agentReport : agentReportList) {

            Integer userTypeObj = agentReport.getUserType();//用户类型 1：代理、0：会员

            if (userTypeObj == 1) {
                agentReportObj = new AgentReport();
                agentReportObj.setReportDate(dateStr);

                String accountObj = agentReport.getAccount();//账户
                String parentAccountObj = agentReport.getParentAccount();//上级
                BigDecimal amountObj = agentReport.getAmount();//余额
                Integer testObj = agentReport.getTest();//是否正式用户 0为正式账户
                Integer lowerCountObj = agentReport.getLowerCount();//下级人数
                Integer regCountObj = agentReport.getRegCount();//注册人数
                Integer firstChargeCountObj = agentReport.getFirstChargeCount();//首充人数
                BigDecimal teamAmountObj = agentReport.getTeamAmount();//团队余额
                Integer teamCountObj = agentReport.getTeamCount();//团队人数

                BigDecimal rechargeAmountObj = BigDecimal.ZERO;//充值金额
                Integer teamRechargeCountObj = 0;//团队充值人数 ***
                BigDecimal withdrawAmountObj = BigDecimal.ZERO;//提现金额
                BigDecimal activityAmountObj = BigDecimal.ZERO;//活动金额
                BigDecimal dailyAmountObj = BigDecimal.ZERO;//日工资
                BigDecimal dividendAmountObj = BigDecimal.ZERO;//周期分红
                BigDecimal winningAmountObj = BigDecimal.ZERO;//中奖金额
                BigDecimal rebateAmountObj = agentReport.getRebateAmount();//代理返点金额
                BigDecimal rebateAmountLObj = BigDecimal.ZERO;//团队返点金额
                BigDecimal betAmountObj = BigDecimal.ZERO;//投注金额
                Integer betPerCountObj = 0;//投注人数 ***
                BigDecimal profitObj = BigDecimal.ZERO;//盈利

                BigDecimal winningAmountNoOwnObj = BigDecimal.ZERO;//中奖金额（不含本人）
                BigDecimal rebateAmountLNoOwnObj = BigDecimal.ZERO;//团队返点（不含本人）
                BigDecimal betAmountNoOwnObj = BigDecimal.ZERO;//投注金额（不含本人）
                BigDecimal activityAmountNoOwnObj = BigDecimal.ZERO;//活动（不含本人）
                BigDecimal dailyAmountNoOwnObj = BigDecimal.ZERO;//日工资（不含本人）
                BigDecimal dividendAmountNoOwnObj = BigDecimal.ZERO;//周期分红（不含本人）

                Integer betPerCountNoOwnObj = 0;//投注人数（不含本人）
                BigDecimal profitNoOwnObj = BigDecimal.ZERO;//盈利（不含本人）

                List<UserSubset> userSubsetList = userSubsetDao.listUserSubsetByAccount(accountObj);
                for (AgentReport agentReportZ : agentReportListZ) {
                    for (UserSubset userSubset : userSubsetList) {
                        if (agentReportZ.getAccount().equals(userSubset.getSubSetAccount())) {
                            rechargeAmountObj = rechargeAmountObj.add(agentReportZ.getRechargeAmount());//充值金额
                            if (agentReportZ.getRechargeAmount().compareTo(BigDecimal.ZERO) > 0) {
                                teamRechargeCountObj = teamRechargeCountObj + 1;
                            }
                            withdrawAmountObj = withdrawAmountObj.add(agentReportZ.getWithdrawAmount());//提现金额
                            activityAmountObj = activityAmountObj.add(agentReportZ.getActivityAmount());//活动金额
                            dailyAmountObj = dailyAmountObj.add(agentReportZ.getDailyAmount());//日工资
                            dividendAmountObj = dividendAmountObj.add(agentReportZ.getDividendAmount());//周期分红
                            winningAmountObj = winningAmountObj.add(agentReportZ.getWinningAmount());//中奖金额
                            rebateAmountLObj = rebateAmountLObj.add(agentReportZ.getRebateAmount());//团队返点金额
                            betAmountObj = betAmountObj.add(agentReportZ.getBetAmount());//投注金额
                            if (agentReportZ.getBetAmount().compareTo(BigDecimal.ZERO) > 0) {
                                betPerCountObj = betPerCountObj + 1;//投注人数
                            }
                            if(!agentReportZ.getAccount().equals(accountObj)){
                                betAmountNoOwnObj = betAmountNoOwnObj.add(agentReportZ.getBetAmount());//投注金额（不含本人）
                                if (agentReportZ.getBetAmount().compareTo(BigDecimal.ZERO) > 0) {
                                    betPerCountNoOwnObj = betPerCountNoOwnObj + 1;//投注人数（不含本人）
                                }
                                winningAmountNoOwnObj = winningAmountNoOwnObj.add(agentReportZ.getWinningAmount());
                                rebateAmountLNoOwnObj = rebateAmountLNoOwnObj.add(agentReportZ.getRebateAmount());
                                activityAmountNoOwnObj = activityAmountNoOwnObj.add(agentReportZ.getActivityAmount());
                                dailyAmountNoOwnObj = dailyAmountNoOwnObj.add(agentReportZ.getDailyAmount());
                                dividendAmountNoOwnObj = dividendAmountNoOwnObj.add(agentReportZ.getDividendAmount());
                            }
                            break;
                        }
                    }
                }

                agentReportObj.setAccount(accountObj);//用户
                agentReportObj.setParentAccount(parentAccountObj);//上级
                agentReportObj.setAmount(amountObj);//余额
                agentReportObj.setTest(testObj);//是否正式
                agentReportObj.setUserType(userTypeObj);//用户类型
                agentReportObj.setLowerCount(lowerCountObj);//下级人数
                agentReportObj.setRegCount(regCountObj);//注册人数
                agentReportObj.setFirstChargeCount(firstChargeCountObj);//首充人数
                agentReportObj.setTeamAmount(teamAmountObj);//团队余额
                agentReportObj.setTeamCount(teamCountObj);//团队人数
                agentReportObj.setRechargeAmount(rechargeAmountObj);//充值金额
                agentReportObj.setTeamRechargeCount(teamRechargeCountObj);//充值人数
                agentReportObj.setWithdrawAmount(withdrawAmountObj);//提现金额
                agentReportObj.setActivityAmount(activityAmountObj);//活动金额
                agentReportObj.setDailyAmount(dailyAmountObj);//日工资
                agentReportObj.setDividendAmount(dividendAmountObj);//周期分红
                agentReportObj.setWinningAmount(winningAmountObj);//中奖金额
                agentReportObj.setRebateAmount(rebateAmountObj);//代理返点
                agentReportObj.setRebateAmountL(rebateAmountLObj);//团队返点
                agentReportObj.setBetAmount(betAmountObj);//投注金额
                agentReportObj.setBetPerCount(betPerCountObj);//投注人数
                profitObj = (winningAmountObj.add(rebateAmountLObj).subtract(betAmountObj)).add(activityAmountObj).add(dailyAmountObj).add(dividendAmountObj);
                agentReportObj.setProfit(profitObj);//盈利
                profitNoOwnObj = (winningAmountNoOwnObj.add(rebateAmountLNoOwnObj).subtract(betAmountNoOwnObj)).add(activityAmountNoOwnObj).add(dailyAmountNoOwnObj).add(dividendAmountNoOwnObj);
                agentReportObj.setProfitNoOwn(profitNoOwnObj);//盈利（不含本人）
                agentReportObj.setBetAmountNoOwn(betAmountNoOwnObj);
                agentReportObj.setBetPerCountNoOwn(betPerCountNoOwnObj);

                agentTeamReportListX.add(agentReportObj);
            }
        }

        return agentTeamReportListX;
    }

    //代理报表
    public List<AgentReport> agentsReport(String account, Integer sorting, String beginDateStr, String endDateStr, Integer userType, Page p) throws ParseException {

        if(StrUtils.hasEmpty(new Object[]{beginDateStr}) || StrUtils.hasEmpty(new Object[]{endDateStr})){
            throw new BaseCheckException("统计开始日期或结束日期不能为空！");
        }

        if(beginDateStr.compareTo(endDateStr)>0){
            throw new BaseCheckException("统计开始日期不能大于结束日期！");
        }

        Date nowDate = new Date();
        String nowDateStr = DateUtils.formatDate(nowDate);

        if(endDateStr.compareTo(nowDateStr)>0){
            throw new BaseCheckException("统计结束日期不能大于今日的日期！");
        }

        //计算出需要统计的数据的区域，分为3类，1：只统计今天的数据、2：只统计历史数据、3：包括今天与历史的数据
        int beginFlag = beginDateStr.compareTo(nowDateStr);
        int endFlag = endDateStr.compareTo(nowDateStr);


        List<AgentReport> agentTeamReportListLast = new ArrayList<AgentReport>();
        List<AgentReport> agentTeamReportListX = new ArrayList<AgentReport>();

        Date beginDate = DateUtils.toDate(beginDateStr + " 00:00:00");
        Date endDate = DateUtils.toDate(endDateStr + " 23:59:59");

        if(endFlag == 0) {
            //计算今天的数据
            Date beginChangeDate = null;
            Date endChangeDate = DateUtils.toDate(endDateStr + " 23:59:59");

            if(beginFlag < 0) {
                beginChangeDate = DateUtils.toDate(endDateStr + " 00:00:00");
            }else{
                beginChangeDate = DateUtils.toDate(beginDateStr + " 00:00:00");
            }

            List<AgentReport> agentReportList = agentReportDao.agentReportList(account, beginDate, endDate, beginChangeDate, endChangeDate, null);

            List<AgentReport> agentReportListZ = new ArrayList<AgentReport>();
            for (AgentReport agentReport : agentReportList) {
                agentReportListZ.add(agentReport);
            }

            AgentReport agentReportObj = null;
            for (AgentReport agentReport : agentReportList) {

                Integer userTypeObj = agentReport.getUserType();//用户类型 1：代理、0：会员

                if (userTypeObj == 1) {
                    agentReportObj = new AgentReport();

                    String accountObj = agentReport.getAccount();//账户
                    String parentAccountObj = agentReport.getParentAccount();//上级
                    BigDecimal amountObj = agentReport.getAmount();//余额
                    Integer testObj = agentReport.getTest();//是否正式用户 0为正式账户
                    Integer lowerCountObj = agentReport.getLowerCount();//下级人数
                    Integer regCountObj = agentReport.getRegCount();//注册人数
                    Integer firstChargeCountObj = agentReport.getFirstChargeCount();//首充人数
                    BigDecimal teamAmountObj = agentReport.getTeamAmount();//团队余额
                    Integer teamCountObj = agentReport.getTeamCount();//团队人数

                    BigDecimal rechargeAmountObj = BigDecimal.ZERO;//充值金额
                    Integer teamRechargeCountObj = 0;//团队充值人数 ***
                    BigDecimal withdrawAmountObj = BigDecimal.ZERO;//提现金额
                    BigDecimal activityAmountObj = BigDecimal.ZERO;//活动金额
                    BigDecimal dailyAmountObj = BigDecimal.ZERO;//日工资
                    BigDecimal dividendAmountObj = BigDecimal.ZERO;//周期分红
                    BigDecimal winningAmountObj = BigDecimal.ZERO;//中奖金额
                    BigDecimal rebateAmountObj = agentReport.getRebateAmount();//代理返点金额
                    BigDecimal rebateAmountLObj = BigDecimal.ZERO;//团队返点金额
                    BigDecimal betAmountObj = BigDecimal.ZERO;//投注金额
                    Integer betPerCountObj = 0;//投注人数 ***
                    BigDecimal profitObj = BigDecimal.ZERO;//盈利

                    BigDecimal winningAmountNoOwnObj = BigDecimal.ZERO;//中奖金额（不含本人）
                    BigDecimal rebateAmountLNoOwnObj = BigDecimal.ZERO;//团队返点（不含本人）
                    BigDecimal betAmountNoOwnObj = BigDecimal.ZERO;//投注金额（不含本人）
                    BigDecimal activityAmountNoOwnObj = BigDecimal.ZERO;//活动（不含本人）
                    BigDecimal dailyAmountNoOwnObj = BigDecimal.ZERO;//日工资（不含本人）
                    BigDecimal dividendAmountNoOwnObj = BigDecimal.ZERO;//周期分红（不含本人）

                    Integer betPerCountNoOwnObj = 0;//投注人数（不含本人）
                    BigDecimal profitNoOwnObj = BigDecimal.ZERO;//盈利（不含本人）

                    List<UserSubset> userSubsetList = userSubsetDao.listUserSubsetByAccount(accountObj);
                    for (AgentReport agentReportZ : agentReportListZ) {
                        for (UserSubset userSubset : userSubsetList) {
                            if (agentReportZ.getAccount().equals(userSubset.getSubSetAccount())) {
                                rechargeAmountObj = rechargeAmountObj.add(agentReportZ.getRechargeAmount());//充值金额
                                if (agentReportZ.getRechargeAmount().compareTo(BigDecimal.ZERO) > 0) {
                                    teamRechargeCountObj = teamRechargeCountObj + 1;
                                }
                                withdrawAmountObj = withdrawAmountObj.add(agentReportZ.getWithdrawAmount());//提现金额
                                activityAmountObj = activityAmountObj.add(agentReportZ.getActivityAmount());//活动金额
                                dailyAmountObj = dailyAmountObj.add(agentReportZ.getDailyAmount());//日工资
                                dividendAmountObj = dividendAmountObj.add(agentReportZ.getDividendAmount());//周期分红
                                winningAmountObj = winningAmountObj.add(agentReportZ.getWinningAmount());//中奖金额
                                rebateAmountLObj = rebateAmountLObj.add(agentReportZ.getRebateAmount());//团队返点金额
                                betAmountObj = betAmountObj.add(agentReportZ.getBetAmount());//投注金额
                                if (agentReportZ.getBetAmount().compareTo(BigDecimal.ZERO) > 0) {
                                    betPerCountObj = betPerCountObj + 1;//投注人数
                                }
                                if(!agentReportZ.getAccount().equals(accountObj)){
                                    betAmountNoOwnObj = betAmountNoOwnObj.add(agentReportZ.getBetAmount());//投注金额（不含本人）
                                    if (agentReportZ.getBetAmount().compareTo(BigDecimal.ZERO) > 0) {
                                        betPerCountNoOwnObj = betPerCountNoOwnObj + 1;//投注人数（不含本人）
                                    }
                                    winningAmountNoOwnObj = winningAmountNoOwnObj.add(agentReportZ.getWinningAmount());
                                    rebateAmountLNoOwnObj = rebateAmountLNoOwnObj.add(agentReportZ.getRebateAmount());
                                    activityAmountNoOwnObj = activityAmountNoOwnObj.add(agentReportZ.getActivityAmount());
                                    dailyAmountNoOwnObj = dailyAmountNoOwnObj.add(agentReportZ.getDailyAmount());
                                    dividendAmountNoOwnObj = dividendAmountNoOwnObj.add(agentReportZ.getDividendAmount());
                                }
                                break;
                            }
                        }
                    }

                    agentReportObj.setAccount(accountObj);//用户
                    agentReportObj.setParentAccount(parentAccountObj);//上级
                    agentReportObj.setAmount(amountObj);//余额
                    agentReportObj.setTest(testObj);//是否正式
                    agentReportObj.setUserType(userTypeObj);//用户类型
                    agentReportObj.setLowerCount(lowerCountObj);//下级人数
                    agentReportObj.setRegCount(regCountObj);//注册人数
                    agentReportObj.setFirstChargeCount(firstChargeCountObj);//首充人数
                    agentReportObj.setTeamAmount(teamAmountObj);//团队余额
                    agentReportObj.setTeamCount(teamCountObj);//团队人数
                    agentReportObj.setRechargeAmount(rechargeAmountObj);//充值金额
                    agentReportObj.setTeamRechargeCount(teamRechargeCountObj);//充值人数
                    agentReportObj.setWithdrawAmount(withdrawAmountObj);//提现金额
                    agentReportObj.setActivityAmount(activityAmountObj);//活动金额
                    agentReportObj.setDailyAmount(dailyAmountObj);//日工资
                    agentReportObj.setDividendAmount(dividendAmountObj);//周期分红
                    agentReportObj.setWinningAmount(winningAmountObj);//中奖金额
                    agentReportObj.setRebateAmount(rebateAmountObj);//代理返点
                    agentReportObj.setRebateAmountL(rebateAmountLObj);//团队返点
                    agentReportObj.setBetAmount(betAmountObj);//投注金额
                    agentReportObj.setBetPerCount(betPerCountObj);//投注人数
                    profitObj = (winningAmountObj.add(rebateAmountLObj).subtract(betAmountObj)).add(activityAmountObj).add(dailyAmountObj).add(dividendAmountObj);
                    agentReportObj.setProfit(profitObj);//盈利
                    profitNoOwnObj = (winningAmountNoOwnObj.add(rebateAmountLNoOwnObj).subtract(betAmountNoOwnObj)).add(activityAmountNoOwnObj).add(dailyAmountNoOwnObj).add(dividendAmountNoOwnObj);
                    agentReportObj.setProfitNoOwn(profitNoOwnObj);//盈利（不含本人）
                    agentReportObj.setBetAmountNoOwn(betAmountNoOwnObj);
                    agentReportObj.setBetPerCountNoOwn(betPerCountNoOwnObj);

                    agentTeamReportListX.add(agentReportObj);
                }
            }
        }

        if(endFlag == 0 && beginFlag == 0) {
            for(AgentReport agentReport: agentTeamReportListX){
                agentTeamReportListLast.add(agentReport);
            }
        }

        if(endFlag == 0 && beginFlag < 0){
            String endDateStr_Z = DateUtils.format(DateUtils.addDay(new Date(), -1), "yyyy-MM-dd");
            List<AgentReport> agentReportHistoryList = agentReportDao.listByCond(account, beginDateStr, endDateStr_Z, null);

            List<AgentReport> agentRechargeAndBetInfo = agentReportDao.agentRechargeAndBetInfo(account, beginDate, endDate);

            AgentReport agentReportObj = null;
            for(AgentReport agentReport: agentTeamReportListX){
                agentReportObj = new AgentReport();
                String accountObj = agentReport.getAccount();//账户
                agentReportObj.setAccount(accountObj);
                agentReportObj.setLowerCount(agentReport.getLowerCount());
                agentReportObj.setTeamAmount(agentReport.getTeamAmount());
                agentReportObj.setTeamCount(agentReport.getTeamCount());
                agentReportObj.setRegCount(agentReport.getRegCount());
                agentReportObj.setFirstChargeCount(agentReport.getFirstChargeCount());

                BigDecimal rechargeAmountObj = BigDecimal.ZERO;//充值金额
                BigDecimal betAmountObj = BigDecimal.ZERO;//投注金额
                Integer teamRechargeCountObj = 0;//团队充值人数 ***
                Integer betPerCountObj = 0;//投注人数 ***
                BigDecimal betAmountNoOwnObj = BigDecimal.ZERO;//投注金额（不含本人）
                Integer betPerCountNoOwnObj = 0;//投注人数（不含本人）

                agentReportObj.setRechargeAmount(agentReport.getRechargeAmount());//充值金额
                agentReportObj.setWithdrawAmount(agentReport.getWithdrawAmount());//提现金额
                agentReportObj.setActivityAmount(agentReport.getActivityAmount());//活动金额
                agentReportObj.setDailyAmount(agentReport.getDailyAmount());//日工资
                agentReportObj.setDividendAmount(agentReport.getDividendAmount());//周期分红
                agentReportObj.setWinningAmount(agentReport.getWinningAmount());//中奖金额
                agentReportObj.setRebateAmount(agentReport.getRebateAmount());//代理返点
                agentReportObj.setRebateAmountL(agentReport.getRebateAmountL());//团队返点
                agentReportObj.setBetAmount(agentReport.getBetAmount());//投注金额
                agentReportObj.setProfit(agentReport.getProfit());//盈利
                agentReportObj.setProfitNoOwn(agentReport.getProfitNoOwn());//盈利（不含本人）

                for(AgentReport agentReportZ: agentReportHistoryList){
                    if(accountObj.equals(agentReportZ.getAccount())){
                        agentReportObj.setRechargeAmount(agentReportObj.getRechargeAmount().add(agentReportZ.getRechargeAmount()));//充值金额
                        agentReportObj.setWithdrawAmount(agentReportObj.getWithdrawAmount().add(agentReportZ.getWithdrawAmount()));//提现金额
                        agentReportObj.setActivityAmount(agentReportObj.getActivityAmount().add(agentReportZ.getActivityAmount()));//活动金额
                        agentReportObj.setDailyAmount(agentReportObj.getDailyAmount().add(agentReportZ.getDailyAmount()));//日工资
                        agentReportObj.setDividendAmount(agentReportObj.getDividendAmount().add(agentReportZ.getDividendAmount()));//周期分红
                        agentReportObj.setWinningAmount(agentReportObj.getWinningAmount().add(agentReportZ.getWinningAmount()));//中奖金额
                        agentReportObj.setRebateAmount(agentReportObj.getRebateAmount().add(agentReportZ.getRebateAmount()));//代理返点
                        agentReportObj.setRebateAmountL(agentReportObj.getRebateAmountL().add(agentReportZ.getRebateAmountL()));//团队返点
                        agentReportObj.setBetAmount(agentReportObj.getBetAmount().add(agentReportZ.getBetAmount()));//投注金额
                        agentReportObj.setProfit(agentReportObj.getProfit().add(agentReportZ.getProfit()));//盈利
                        agentReportObj.setProfitNoOwn(agentReportObj.getProfitNoOwn().add(agentReportZ.getProfitNoOwn()));//盈利（不含本人）
                        break;
                    }
                }

                //设置团队充值人数、投注人数、投注人数（不含本人）
                List<UserSubset> userSubsetList = userSubsetDao.listUserSubsetByAccount(accountObj);
                for(AgentReport agentReportZ: agentRechargeAndBetInfo){
                    for (UserSubset userSubset : userSubsetList) {
                        if (agentReportZ.getAccount().equals(userSubset.getSubSetAccount())) {

                            if (agentReportZ.getRechargeAmount().compareTo(BigDecimal.ZERO) > 0) {
                                teamRechargeCountObj = teamRechargeCountObj + 1;//充值人数
                            }

                            if (agentReportZ.getBetAmount().compareTo(BigDecimal.ZERO) > 0) {
                                betPerCountObj = betPerCountObj + 1;//投注人数
                            }

                            if(!agentReportZ.getAccount().equals(accountObj)){
                                betAmountNoOwnObj = betAmountNoOwnObj.add(agentReportZ.getBetAmount());//投注金额（不含本人）
                                if (agentReportZ.getBetAmount().compareTo(BigDecimal.ZERO) > 0) {
                                    betPerCountNoOwnObj = betPerCountNoOwnObj + 1;//投注人数（不含本人）
                                }
                            }
                            break;
                        }
                    }
                }

                agentReportObj.setTeamRechargeCount(teamRechargeCountObj);//充值人数
                agentReportObj.setBetPerCount(betPerCountObj);//投注人数
                agentReportObj.setBetAmountNoOwn(betAmountNoOwnObj);
                agentReportObj.setBetPerCountNoOwn(betPerCountNoOwnObj);

                agentTeamReportListLast.add(agentReportObj);
            }
        }

        if(endFlag < 0 && beginFlag < 0){
            List<AgentReport> agentReportHistoryList = agentReportDao.listByCond(account, beginDateStr, endDateStr, null);
            List<AgentReport> agentInfo = agentReportDao.agentInfoByHistoryAccount(account, beginDate, endDate, beginDateStr, endDateStr, userType);
            List<AgentReport> agentRechargeAndBetInfo = agentReportDao.agentRechargeAndBetInfo(account, beginDate, endDate);

            AgentReport agentReportObj = null;
            for (AgentReport agentReport : agentInfo) {

                Integer userTypeObj = agentReport.getUserType();//用户类型 1：代理、0：会员

                if (userTypeObj == 1) {
                    agentReportObj = new AgentReport();

                    String accountObj = agentReport.getAccount();//账户
                    String parentAccountObj = agentReport.getParentAccount();//上级
                    BigDecimal amountObj = agentReport.getAmount();//余额
                    Integer testObj = agentReport.getTest();//是否正式用户 0为正式账户
                    Integer lowerCountObj = agentReport.getLowerCount();//下级人数
                    Integer regCountObj = agentReport.getRegCount();//注册人数
                    Integer firstChargeCountObj = agentReport.getFirstChargeCount();//首充人数
                    BigDecimal teamAmountObj = agentReport.getTeamAmount();//团队余额
                    Integer teamCountObj = agentReport.getTeamCount();//团队人数

                    BigDecimal rechargeAmountObj = BigDecimal.ZERO;//充值金额
                    Integer teamRechargeCountObj = 0;//团队充值人数 ***
                    BigDecimal withdrawAmountObj = BigDecimal.ZERO;//提现金额
                    BigDecimal activityAmountObj = BigDecimal.ZERO;//活动金额
                    BigDecimal dailyAmountObj = BigDecimal.ZERO;//日工资
                    BigDecimal dividendAmountObj = BigDecimal.ZERO;//周期分红
                    BigDecimal winningAmountObj = BigDecimal.ZERO;//中奖金额
                    BigDecimal rebateAmountObj = BigDecimal.ZERO;//代理返点金额
                    BigDecimal rebateAmountLObj = BigDecimal.ZERO;//团队返点金额
                    BigDecimal betAmountObj = BigDecimal.ZERO;//投注金额
                    Integer betPerCountObj = 0;//投注人数 ***
                    BigDecimal profitObj = BigDecimal.ZERO;//盈利
                    BigDecimal betAmountNoOwnObj = BigDecimal.ZERO;//投注金额（不含本人）
                    Integer betPerCountNoOwnObj = 0;//投注人数（不含本人）
                    BigDecimal profitNoOwnObj = BigDecimal.ZERO;//盈利（不含本人）

                    List<UserSubset> userSubsetList = userSubsetDao.listUserSubsetByAccount(accountObj);
                    for (AgentReport agentReportZ : agentReportHistoryList) {
                        for (UserSubset userSubset : userSubsetList) {
                            if (agentReportZ.getAccount().equals(userSubset.getSubSetAccount())) {
                                rechargeAmountObj = rechargeAmountObj.add(agentReportZ.getRechargeAmount());//充值金额
                                withdrawAmountObj = withdrawAmountObj.add(agentReportZ.getWithdrawAmount());//提现金额
                                activityAmountObj = activityAmountObj.add(agentReportZ.getActivityAmount());//活动金额
                                dailyAmountObj = dailyAmountObj.add(agentReportZ.getDailyAmount());//日工资
                                dividendAmountObj = dividendAmountObj.add(agentReportZ.getDividendAmount());//周期分红
                                winningAmountObj = winningAmountObj.add(agentReportZ.getWinningAmount());//中奖金额
                                if(agentReportZ.getAccount().equals(accountObj)){
                                    rebateAmountObj = agentReportZ.getRebateAmount();
                                }
                                rebateAmountLObj = rebateAmountLObj.add(agentReportZ.getRebateAmount());//团队返点金额
                                betAmountObj = betAmountObj.add(agentReportZ.getBetAmount());//投注金额
                                profitObj = profitObj.add(agentReportZ.getProfit());
                                profitNoOwnObj = profitNoOwnObj.add(agentReportZ.getProfitNoOwn());
                                break;
                            }
                        }
                    }

                    for(AgentReport agentReportZ: agentRechargeAndBetInfo){
                        for (UserSubset userSubset : userSubsetList) {
                            if (agentReportZ.getAccount().equals(userSubset.getSubSetAccount())) {
                                if (agentReportZ.getRechargeAmount().compareTo(BigDecimal.ZERO) > 0) {
                                    teamRechargeCountObj = teamRechargeCountObj + 1;
                                }
                                if (agentReportZ.getBetAmount().compareTo(BigDecimal.ZERO) > 0) {
                                    betPerCountObj = betPerCountObj + 1;
                                }
                                if(!agentReportZ.getAccount().equals(accountObj)){
                                    betAmountNoOwnObj = betAmountNoOwnObj.add(agentReportZ.getBetAmount());//投注金额（不含本人）
                                    if (agentReportZ.getBetAmount().compareTo(BigDecimal.ZERO) > 0) {
                                        betPerCountNoOwnObj = betPerCountNoOwnObj + 1;//投注人数（不含本人）
                                    }
                                }
                                break;
                            }
                        }
                    }

                    agentReportObj.setAccount(accountObj);//用户
                    agentReportObj.setParentAccount(parentAccountObj);//上级
                    agentReportObj.setAmount(amountObj);//余额
                    agentReportObj.setTest(testObj);//是否正式
                    agentReportObj.setUserType(userTypeObj);//用户类型
                    agentReportObj.setLowerCount(lowerCountObj);//下级人数
                    agentReportObj.setRegCount(regCountObj);//注册人数
                    agentReportObj.setFirstChargeCount(firstChargeCountObj);//首充人数
                    agentReportObj.setTeamAmount(teamAmountObj);//团队余额
                    agentReportObj.setTeamCount(teamCountObj);//团队人数
                    agentReportObj.setRechargeAmount(rechargeAmountObj);//充值金额
                    agentReportObj.setTeamRechargeCount(teamRechargeCountObj);//充值人数
                    agentReportObj.setWithdrawAmount(withdrawAmountObj);//提现金额
                    agentReportObj.setActivityAmount(activityAmountObj);//活动金额
                    agentReportObj.setDailyAmount(dailyAmountObj);//日工资
                    agentReportObj.setDividendAmount(dividendAmountObj);//周期分红
                    agentReportObj.setWinningAmount(winningAmountObj);//中奖金额
                    agentReportObj.setRebateAmount(rebateAmountObj);//代理返点
                    agentReportObj.setRebateAmountL(rebateAmountLObj);//团队返点
                    agentReportObj.setBetAmount(betAmountObj);//投注金额
                    agentReportObj.setBetPerCount(betPerCountObj);//投注人数
                    agentReportObj.setProfit(profitObj);//盈利
                    agentReportObj.setProfitNoOwn(profitNoOwnObj);//盈利（不含本人）
                    agentReportObj.setBetAmountNoOwn(betAmountNoOwnObj);//投注人数（不含本人
                    agentReportObj.setBetPerCountNoOwn(betPerCountNoOwnObj);//投注人数（不含本人）

                    agentTeamReportListLast.add(agentReportObj);
                }
            }

        }


        List<AgentReport> agentTeamReportList = new ArrayList<AgentReport>();
        if(!StrUtils.hasEmpty(new Object[]{account})){
            for(AgentReport agentReport: agentTeamReportListLast){
                if(agentReport.getAccount().equals(account)){
                    agentTeamReportList.add(agentReport);
                    break;
                }
            }
        }else{
            for(AgentReport agentReport: agentTeamReportListLast){
                agentTeamReportList.add(agentReport);
            }
        }


        //排序
        if(StrUtils.hasEmpty(new Object[]{sorting})){
            sorting = 0;
        }

        if(sorting == 0){//首充人数递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getFirstChargeCount() < o2.getFirstChargeCount()){
                        return 1;
                    }
                    if(o1.getFirstChargeCount() == o2.getFirstChargeCount()){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 1){//充值人数递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getTeamRechargeCount() < o2.getTeamRechargeCount()){
                        return 1;
                    }
                    if(o1.getTeamRechargeCount() == o2.getTeamRechargeCount()){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 2){//投注人数递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getBetPerCount() < o2.getBetPerCount()){
                        return 1;
                    }
                    if(o1.getBetPerCount() == o2.getBetPerCount()){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 3){//注册人数递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getRegCount() < o2.getRegCount()){
                        return 1;
                    }
                    if(o1.getRegCount() == o2.getRegCount()){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 4){//下级人数递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getLowerCount() < o2.getLowerCount()){
                        return 1;
                    }
                    if(o1.getLowerCount() == o2.getLowerCount()){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 5){//活动礼金递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getActivityAmount().compareTo(o2.getActivityAmount()) < 0){
                        return 1;
                    }
                    if(o1.getActivityAmount().compareTo(o2.getActivityAmount()) == 0){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 6){//投注金额递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getBetAmount().compareTo(o2.getBetAmount()) < 0){
                        return 1;
                    }
                    if(o1.getBetAmount().compareTo(o2.getBetAmount()) == 0){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 7){//中奖金额递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getWinningAmount().compareTo(o2.getWinningAmount()) < 0){
                        return 1;
                    }
                    if(o1.getWinningAmount().compareTo(o2.getWinningAmount()) == 0){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 8){//充值金额递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getRechargeAmount().compareTo(o2.getRechargeAmount()) < 0){
                        return 1;
                    }
                    if(o1.getRechargeAmount().compareTo(o2.getRechargeAmount()) == 0){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 9){//提现金额递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getWithdrawAmount().compareTo(o2.getWithdrawAmount()) < 0){
                        return 1;
                    }
                    if(o1.getWithdrawAmount().compareTo(o2.getWithdrawAmount()) == 0){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 10){//团队盈利递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getProfit().compareTo(o2.getProfit()) < 0){
                        return 1;
                    }
                    if(o1.getProfit().compareTo(o2.getProfit()) == 0){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 11){//团队返点递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getRebateAmountL().compareTo(o2.getRebateAmountL()) < 0){
                        return 1;
                    }
                    if(o1.getRebateAmountL().compareTo(o2.getRebateAmountL()) == 0){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 12){//代理返点递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getRebateAmount().compareTo(o2.getRebateAmount()) < 0){
                        return 1;
                    }
                    if(o1.getRebateAmount().compareTo(o2.getRebateAmount()) == 0){
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(sorting == 13){//团队余额递减
            Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
                public int compare(AgentReport o1, AgentReport o2) {
                    //按照字段进行降序排列
                    if(o1.getTeamAmount().compareTo(o2.getTeamAmount()) < 0){
                        return 1;
                    }
                    if(o1.getTeamAmount().compareTo(o2.getTeamAmount()) == 0){
                        return 0;
                    }
                    return -1;
                }
            });
        }


        List<AgentReport> agentTeamReportListZ = new ArrayList<AgentReport>();

        if(p != null) {
            Integer start = p.getStartIndex();
            Integer limit = p.getPageSize();

            int listSize = agentTeamReportList.size();
            p.setRowCount(listSize);

            Integer listSizeZ = listSize - start;
            if (listSizeZ < limit) limit = listSizeZ;

            for (int i = start; i < start + limit; i++) {
                agentTeamReportListZ.add(agentTeamReportList.get(i));
            }
        }else{
            for(AgentReport agentReport: agentTeamReportList){
                agentTeamReportListZ.add(agentReport);
            }
        }

        return agentTeamReportListZ;
    }


    //下级报表
    public Map<String, Object> underAgentsReport(String account, Date beginDate, Date endDate, Integer start, Integer limit){

        if(StrUtils.hasEmpty(new Object[]{account})){
            throw new BaseCheckException("[用户名称]不能为空！");
        }

        List<AgentReport> agentReportList = agentReportDao.agentReportList(account, beginDate, endDate, beginDate, endDate, null);

        List<AgentReport> agentTeamReportList = new ArrayList<AgentReport>();

        List<AgentReport> agentReportListZ = new ArrayList<AgentReport>();
        for(AgentReport agentReport: agentReportList){
            agentReportListZ.add(agentReport);
        }

        AgentReport agentReportObj = null;
        List<UnderUser> underUserList = userDao.underUserListForLevelReportZ(account);
        for(AgentReport agentReport: agentReportList){

            String accountObj = agentReport.getAccount();//账户

            for(UnderUser underUser: underUserList){
                if(accountObj.equals(underUser.getAccount())){

                    agentReportObj = new AgentReport();

                    Integer testObj = agentReport.getTest();//是否正式用户 0为正式账户
                    Integer userTypeObj = agentReport.getUserType();//用户类型 1：代理、0：会员
                    Integer lowerCountObj = agentReport.getLowerCount();//下级人数
                    Integer teamCountObj = agentReport.getTeamCount();//团队人数

                    BigDecimal activityAmountObj = BigDecimal.ZERO;//活动金额
                    BigDecimal dailyAmountObj = BigDecimal.ZERO;//日工资
                    BigDecimal dividendAmountObj = BigDecimal.ZERO;//周期分红
                    BigDecimal winningAmountObj = BigDecimal.ZERO;//中奖金额
                    BigDecimal rebateAmountLObj = BigDecimal.ZERO;//团队返点金额
                    BigDecimal betAmountObj = BigDecimal.ZERO;//投注金额
                    BigDecimal profitObj = BigDecimal.ZERO;//盈利

                    List<UserSubset> userSubsetList = userSubsetDao.listUserSubsetByAccount(accountObj);
                    for(AgentReport agentReportZ: agentReportListZ){
                        for(UserSubset userSubset: userSubsetList){
                            if(agentReportZ.getAccount().equals(userSubset.getSubSetAccount())){
                                activityAmountObj = activityAmountObj.add(agentReportZ.getActivityAmount());//活动金额
                                dailyAmountObj = dailyAmountObj.add(agentReportZ.getDailyAmount());//日工资
                                dividendAmountObj = dividendAmountObj.add(agentReportZ.getDividendAmount());//周期分红
                                winningAmountObj = winningAmountObj.add(agentReportZ.getWinningAmount());//中奖金额
                                rebateAmountLObj = rebateAmountLObj.add(agentReportZ.getRebateAmount());//团队返点金额
                                betAmountObj = betAmountObj.add(agentReportZ.getBetAmount());//投注金额
                                break;
                            }
                        }
                    }

                    agentReportObj.setAccount(accountObj);//用户
                    agentReportObj.setTest(testObj);//是否正式
                    agentReportObj.setUserType(userTypeObj);//用户类型
                    agentReportObj.setLowerCount(lowerCountObj);//下级人数
                    agentReportObj.setTeamCount(teamCountObj);//团队人数
                    agentReportObj.setActivityAmount(activityAmountObj);//活动金额
                    agentReportObj.setDailyAmount(dailyAmountObj);//日工资
                    agentReportObj.setDividendAmount(dividendAmountObj);//周期分红
                    agentReportObj.setWinningAmount(winningAmountObj);//中奖金额
                    agentReportObj.setRebateAmountL(rebateAmountLObj);//团队返点
                    agentReportObj.setBetAmount(betAmountObj);//投注金额
                    profitObj = (winningAmountObj.add(rebateAmountLObj).subtract(betAmountObj)).add(activityAmountObj).add(dailyAmountObj).add(dividendAmountObj);
                    agentReportObj.setProfit(profitObj);//盈利

                    agentTeamReportList.add(agentReportObj);

                    break;
                }
            }
        }

        //团队盈利递减
        Collections.sort(agentTeamReportList, new Comparator<AgentReport>(){
            public int compare(AgentReport o1, AgentReport o2) {
                //按照字段进行降序排列
                if(o1.getProfit().compareTo(o2.getProfit()) < 0){
                    return 1;
                }
                if(o1.getProfit().compareTo(o2.getProfit()) == 0){
                    return 0;
                }
                return -1;
            }
        });



        Integer listCount = agentTeamReportList.size();

        List<AgentReport> agentTeamReportListZ = new ArrayList<AgentReport>();

        Integer listSizeZ = listCount - start;
        if(listSizeZ < limit) limit = listSizeZ;

        for(int i = start; i < start + limit; i++){
            agentTeamReportListZ.add(agentTeamReportList.get(i));
        }

        Map<String, Object> underAgentsReportObj = new HashMap<String, Object>();
        underAgentsReportObj.put("listCount", listCount);
        underAgentsReportObj.put("agentReportList", agentTeamReportListZ);
        return underAgentsReportObj;
    }



    //用户可提现余额报表
    public List<Map<String, Object>> cashWithdrawalBalanceReport(String account, Page p){
        List<Map<String, Object>> cwbrReport = userDao.cashWithdrawalBalanceReport(account, p);
        return cwbrReport;
    }

    //彩种报表
    public List<BetReport> betReport(String[] lotteryIdArr, Integer sorting, Date startTime, Date endTime, Page p){
        List<BetReport> betReport = betDao.betReport(lotteryIdArr, sorting, startTime, endTime, p);
        return betReport;
    }


    /**
     * 代理彩种报表
     *
     * @param account           会员账户
     * @param lotteryIdArr      彩种IDs
     * @param startTime         开始时间
     * @param endTime           结束时间
     * @return
     */
    public List<AgentLotteryReport> agentLotteryReportList(String account, String[] lotteryIdArr, Date startTime, Date endTime, Page p) {

        List<AgentLotteryReport> agentLotteryReportList = agentLotteryReportDao.agentLotteryReportList(account, lotteryIdArr, startTime, endTime);
        List<AgentLotteryReport> resultList = new ArrayList<AgentLotteryReport>();

        if(!agentLotteryReportList.isEmpty()) {

            List<AgentLotteryReport> resultListX = new ArrayList<AgentLotteryReport>();

            Set<String> set = new HashSet();
            for (AgentLotteryReport agentLotteryReport : agentLotteryReportList) {
                set.add(agentLotteryReport.getAccount());
            }
            String[] accountArr = set.toArray(new String[set.size()]);

            List<UserSubset> listUserSubset = userSubsetDao.listUserSubsetBySubSetAccountArr(accountArr);

            Set<String> subAccAllSet = new HashSet();
            for(UserSubset userSubset: listUserSubset){
                subAccAllSet.add(userSubset.getAccount());
            }
            String[] subAccAllArr = subAccAllSet.toArray(new String[subAccAllSet.size()]);


            Set<String> subAccSet = new HashSet();
            Boolean isNullSub = true;
            for(String subAccAll: subAccAllArr){
                isNullSub = true;
                for(String acc: accountArr){
                    if(subAccAll.equals(acc)){
                        isNullSub = false;
                        break;
                    }
                }
                if(isNullSub){
                    subAccSet.add(subAccAll);
                }
            }
            String[] subAccArr = subAccSet.toArray(new String[subAccSet.size()]);

            List<AgentLotteryReport> agentLotteryReportListZ = new ArrayList<AgentLotteryReport>();
            for(AgentLotteryReport agentLotteryReport: agentLotteryReportList){
                agentLotteryReportListZ.add(agentLotteryReport);
            }

            AgentLotteryReport alrNullSub = null;
            List<UserSubset> listUserSubsetByAcc = null;
            for(String subAcc: subAccArr){

                listUserSubsetByAcc = new ArrayList<UserSubset>();

                for(UserSubset userSubset: listUserSubset){
                    if(userSubset.getAccount().equals(subAcc)){
                        listUserSubsetByAcc.add(userSubset);
                    }
                }

                Set<Map<String, String>> subAccLotterySet = new HashSet<Map<String, String>>();
                Map<String, String> subAccLotteryMap = null;

                for(AgentLotteryReport agentLotteryReport: agentLotteryReportListZ) {
                    for(UserSubset userSubset: listUserSubsetByAcc){
                        if(userSubset.getSubSetAccount().equals(agentLotteryReport.getAccount())){
                            subAccLotteryMap = new HashMap<String, String>();
                            subAccLotteryMap.put("lotteryId", agentLotteryReport.getLotteryId());
                            subAccLotteryMap.put("lotteryName", agentLotteryReport.getLotteryName());
                            subAccLotterySet.add(subAccLotteryMap);
                            break;
                        }
                    }
                }

                for(Map<String, String> salMap: subAccLotterySet){
                    alrNullSub = new AgentLotteryReport();
                    alrNullSub.setAccount(subAcc);
                    alrNullSub.setLotteryId(salMap.get("lotteryId"));
                    alrNullSub.setLotteryName(salMap.get("lotteryName"));
                    alrNullSub.setBetAmount(BigDecimal.ZERO);
                    alrNullSub.setWinningAmount(BigDecimal.ZERO);
                    alrNullSub.setRebateAmount(BigDecimal.ZERO);
                    agentLotteryReportList.add(alrNullSub);
                }

            }



            List<AgentLotteryReport> agentLotteryReportListX = new ArrayList<AgentLotteryReport>();
            for(AgentLotteryReport agentLotteryReport: agentLotteryReportList){
                agentLotteryReportListX.add(agentLotteryReport);
            }

            List<UserSubset> listUserSubsetWithAccount = null;
            AgentLotteryReport agentLotteryReportObj = null;
            for(AgentLotteryReport agentLotteryReport: agentLotteryReportList){

                agentLotteryReportObj = new AgentLotteryReport();

                String alrAccount = agentLotteryReport.getAccount();
                listUserSubsetWithAccount = new ArrayList<UserSubset>();
                for(UserSubset userSubset: listUserSubset){
                    if(userSubset.getAccount().equals(alrAccount)){
                        listUserSubsetWithAccount.add(userSubset);
                    }
                }

                String lotteryId = agentLotteryReport.getLotteryId(); //彩种ID
                String lotteryName = agentLotteryReport.getLotteryName(); //彩种名称
                Integer betPerCount = 0;   //投注人数
                Integer betPerCountNoOwn = 0;   //投注人数（不含自己）
                BigDecimal betAmount = BigDecimal.ZERO;//投注金额
                BigDecimal betAmountNoOwn = BigDecimal.ZERO;//投注金额（不含本人）
                BigDecimal winningAmount = BigDecimal.ZERO;//中奖金额
                BigDecimal winningAmountNoOwn = BigDecimal.ZERO;//中奖金额（不含本人）
                BigDecimal rebateAmount = BigDecimal.ZERO;//返点金额
                BigDecimal rebateAmountNoOwn = BigDecimal.ZERO;//返点金额（不含本人）
                BigDecimal profit = BigDecimal.ZERO;//负盈利
                BigDecimal profitNoOwn = BigDecimal.ZERO;//负盈利（不含本人）

                boolean isSubset = false;
                for(AgentLotteryReport agentLotteryReportX: agentLotteryReportListX) {

                    isSubset = false;
                    String alrAccountX = agentLotteryReportX.getAccount();
                    String lotteryIdX = agentLotteryReportX.getLotteryId();
                    if(lotteryIdX.equals(lotteryId)){
                        for(UserSubset userSubset: listUserSubsetWithAccount){
                            if(alrAccountX.equals(userSubset.getSubSetAccount())){
                                isSubset = true;
                            }
                        }
                    }

                    if(isSubset){
                        betPerCount = betPerCount + 1;   //投注人数
                        betAmount = betAmount.add(agentLotteryReportX.getBetAmount());//投注金额
                        winningAmount = winningAmount.add(agentLotteryReportX.getWinningAmount());//中奖金额
                        rebateAmount = rebateAmount.add(agentLotteryReportX.getRebateAmount());//返点金额
                        if(!alrAccountX.equals(alrAccount)){
                            betPerCountNoOwn = betPerCountNoOwn + 1;   //投注人数（不含自己）
                            betAmountNoOwn = betAmountNoOwn.add(agentLotteryReportX.getBetAmount());//投注金额（不含本人）
                            winningAmountNoOwn = winningAmountNoOwn.add(agentLotteryReportX.getWinningAmount());//中奖金额（不含本人）
                            rebateAmountNoOwn = rebateAmountNoOwn.add(agentLotteryReportX.getRebateAmount());//返点金额（不含本人）
                        }
                    }

                }

                agentLotteryReportObj.setAccount(alrAccount);
                agentLotteryReportObj.setLotteryId(lotteryId);
                agentLotteryReportObj.setLotteryName(lotteryName);
                agentLotteryReportObj.setBetPerCount(betPerCount);//投注人数
                agentLotteryReportObj.setBetPerCountNoOwn(betPerCountNoOwn);//投注人数（不含本人）
                agentLotteryReportObj.setBetAmount(betAmount);//投注金额
                agentLotteryReportObj.setBetAmountNoOwn(betAmountNoOwn);//投注金额（不含本人）
                agentLotteryReportObj.setWinningAmount(winningAmount);//中奖金额
                agentLotteryReportObj.setWinningAmountNoOwn(winningAmountNoOwn);//中奖金额（不含本人）
                agentLotteryReportObj.setRebateAmount(rebateAmount);//返点金额
                agentLotteryReportObj.setRebateAmountNoOwn(rebateAmountNoOwn);//返点金额（不含本人）
                profit = winningAmount.add(rebateAmount).subtract(betAmount);//负盈利
                agentLotteryReportObj.setProfit(profit);
                profitNoOwn = winningAmountNoOwn.add(rebateAmountNoOwn).subtract(betAmountNoOwn);//负盈利（不含本人）
                agentLotteryReportObj.setProfitNoOwn(profitNoOwn);
                resultListX.add(agentLotteryReportObj);
            }


            List<AgentLotteryReport> resultListZ = new ArrayList<AgentLotteryReport>();
            if(!StrUtils.hasEmpty(new Object[]{account})){
                for(AgentLotteryReport agentReport: resultListX){
                    if(agentReport.getAccount().equals(account)){
                        resultListZ.add(agentReport);
                        //break;
                    }
                }
            }else{
                for(AgentLotteryReport agentReport: resultListX){
                    resultListZ.add(agentReport);
                }
            }


            //按用户降序
            Collections.sort(resultListZ, new Comparator<AgentLotteryReport>(){

                public int compare(AgentLotteryReport o1, AgentLotteryReport o2) {
                    //按照字段进行降序排列
                    if(o1.getAccount().compareTo(o2.getAccount()) < 0){
                        return 1;
                    }
                    if(o1.getAccount().compareTo(o2.getAccount()) == 0){
                        return 0;
                    }
                    return -1;
                }
            });


            if(p != null) {
                Integer start = p.getStartIndex();
                Integer limit = p.getPageSize();

                int listSize = resultListZ.size();
                p.setRowCount(listSize);

                Integer listSizeZ = listSize - start;
                if (listSizeZ < limit) limit = listSizeZ;

                for (int i = start; i < start + limit; i++) {
                    resultList.add(resultListZ.get(i));
                }
            }else{
                for(AgentLotteryReport agentLotteryReport: resultListZ){
                    resultList.add(agentLotteryReport);
                }
            }



        }

        return resultList;
    }



}
