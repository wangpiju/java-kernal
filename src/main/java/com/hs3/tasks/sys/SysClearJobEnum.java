package com.hs3.tasks.sys;

import com.hs3.entity.sys.SysClear;
import com.hs3.utils.DateUtils;

import java.util.Date;

import org.quartz.Job;

public enum SysClearJobEnum {
    ClearLog4jJob(1, "t_log4j", "createdate", "日志清理"),
    ClearAmountChangeJob(2, "t_amount_change", "changeTime", "游戏帐变清理"),
    ClearFinanceChangeJob(3, "t_finance_change", "changeTime", "充提帐变清理"),
    ClearBetJob(4, "t_bet", "isTrace = 0 AND createTime", "投注记录"),
    ClearBetBackupJob(5, "t_bet_backup", "isTrace = 0 AND createTime", "投注记录备份清理"),
    ClearRechargeJob(6, "t_recharge", "createTime", "充值记录清理"),
    ClearDepositJob(7, "t_deposit", "createTime", "提款记录清理"),
    ClearUserReportJob(8, "t_user_report", "createDate", "个人盈亏记录清理"),
    ClearTeamReportJob(9, "t_team_report", "createDate", "团队盈亏记录清理"),
    ClearRechargeLowerJob(10, "t_recharge_lower", "createTime", "会员转账清理"),
    ClearLotterySaleTimeJob(11, "t_lottery_sale_time", "endTime", "seasonId, lotteryId", "彩种奖期清理"),
    ClearLotterySeasonJob(12, "t_lottery_season", "addTime", "seasonId, lotteryId", "开奖列表清理"),
    ClearExtCodeJob(13, "t_ext_code", "createTime", "注册链接清理", LinkSysClearJob.class),
    ClearUserJob(14, "t_user", "loginTime", "用户清理", UserSysClearJob.class),
    ClearLogAllJob(15, "t_log_all", "createTime", "线程日志清理"),
    ClearActivityUser(16, "t_activity_user", "createTime", "用户参加活动的记录清理"),
    ClearApproval(17, "t_approval", "createTime", "审核记录清理"),
    ClearBetIn(18, "t_bet_in", "createTime", "彩中彩记录清理"),
    ClearBetInTotal(19, "t_bet_in_total", "startTime", "彩中彩汇总记录清理"),
    ClearTempAmountChange(20, "t_temp_amount_change", "changeTime", "临时帐变表清理"),
    ClearBetTiger(21, "t_bet_tiger", "createTime", "老虎机数据清理"),
    ClearLotterySeason(22, "t_lottery_season", "lotteryId!='3d' AND lotteryId!='pl3' AND lotteryId!='pl5' AND addTime", "seasonId, lotteryId", "高频彩开奖结果清理"),
    ClearRechargeLower(23, "t_recharge_lower", "createTime", "下级充值记录清理"),
    ClearTrace(24, "t_trace", "createTime", "追号记录清理", TraceSysClearJob.class),
    ClearLotterySeasonWeight(25, "t_lottery_season_weight", "createTime", "权重记录清理"),
    ClearRiskBonus(26, "t_risk_bonus", "", "风控奖池清理",BonusRiskClearJob.class);

    private Integer value;
    private String table;
    private String column;
    private String order;
    private String desc;
    private Class<? extends Job> clazz;

    private SysClearJobEnum(Integer value, String table, String column, String desc) {
        this(value, table, column, "ID", desc, SysClearJob.class);
    }

    private SysClearJobEnum(Integer value, String table, String column, String order, String desc) {
        this(value, table, column, order, desc, SysClearJob.class);
    }

    private SysClearJobEnum(Integer value, String table, String column, String desc, Class<? extends Job> clazz) {
        this(value, table, column, "ID", desc, clazz);
    }

    private SysClearJobEnum(Integer value, String table, String column, String order, String desc, Class<? extends Job> clazz) {
        this.value = value;
        this.table = table;
        this.column = column;
        this.order = order;
        this.desc = desc;
        this.clazz = clazz;
    }

    public Object getObj(SysClear sysClear) {
        Integer beforeDays = sysClear.getBeforeDays() == null ? sysClear.getBeforeDaysDefault() : sysClear.getBeforeDays();
        return DateUtils.addDay(new Date(), 0 - Math.abs(beforeDays));
    }

    public Integer getValue() {
        return this.value;
    }

    public String getTable() {
        return this.table;
    }

    public String getColumn() {
        return this.column;
    }

    public String getOrder() {
        return this.order;
    }

    public String getDesc() {
        return this.desc;
    }

    public Class<? extends Job> getClazz() {
        return this.clazz;
    }

    public String toString() {
        //jd-gui
        //return this.value;
        return (new StringBuilder()).append(value).toString();
    }
}
