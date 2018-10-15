package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.db.Page;
import com.hs3.entity.report.CpsReport;
import com.hs3.utils.BeanZUtils;
import com.hs3.utils.DateUtils;
import com.hs3.utils.MathUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("cpsReportDao")
public class CpsReportDao extends BaseDao<CpsReport> {

    public List<CpsReport> listByCond(String begin, String end, Page page) {
        return this.dbSession.list("SELECT * FROM t_cps_report WHERE 1=1 AND reportDate >= ? AND reportDate <= ? ORDER BY reportDate DESC", new Object[]{begin, end}, this.cls, page);
    }

    public int delete(String reportDate) {
        String sql = "DELETE FROM " + this.tableName + " WHERE reportDate=?";
        return this.dbSession.update(sql, new Object[]{reportDate});
    }

    //综合报表接口
    public Map<String, Object> cpsReport(Date startTime, Date endTime) {
        List<Object> args = new ArrayList<Object>();
        String sql = "select * from ";

        sql = sql + "(select ifnull(sum(changeAmount),0) as rechargeBankAmount,count(DISTINCT changeUser) as rechargeBankNum,count(*) as rechargeBankOrNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + "and accountChangeTypeId = 12) as t1,";

        sql = sql + "(select ifnull(sum(changeAmount),0) as rechargeOnlineAmount,count(DISTINCT changeUser) as rechargeOnlineNum,count(*) as rechargeOnlineOrNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 11) as t2,";

        sql = sql + "(select ifnull(sum(changeAmount),0) as rechargeHandAmount,count(DISTINCT changeUser) as rechargeHandNum,count(*) as rechargeHandOrNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 13) as t3,";

        sql = sql + "(select ifnull(sum(changeAmount),0) as rechargeCashAmount,count(DISTINCT changeUser) as rechargeCashNum,count(*) as rechargeCashOrNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 21) as t4,";

        sql = sql + "(select count(DISTINCT changeUser) as rechargeAllNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId in (12,11,13,21)) as t1t4,";


        sql = sql + "(select ifnull(sum(changeAmount),0) as withdrawAmount,count(DISTINCT changeUser) as withdrawNum,count(*) as withdrawOrNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 18) as t5,";

        sql = sql + "(select ifnull(sum(changeAmount),0) as rebateAmount,count(DISTINCT changeUser) as rebateNum from t_amount_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 3) as t6,";

        sql = sql + "(select ifnull(sum(changeAmount),0) as distributeActivityAmount,count(DISTINCT changeUser) as distributeActivityNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 19) as t7,";

        sql = sql + "(select ifnull(sum(changeAmount),0) as distributeClaimAmount,count(DISTINCT changeUser) as distributeClaimNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 20) as t8,";

        sql = sql + "(select ifnull(sum(changeAmount),0) as distributeDailyAmount,count(DISTINCT changeUser) as distributeDailyNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 28) as t9,";

        sql = sql + "(select ifnull(sum(changeAmount),0) as distributeDividendAmount,count(DISTINCT changeUser) as distributeDividendNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 30) as t10,";

        sql = sql + "(select ifnull(sum(changeAmount),0) as distributeAdminAmount,count(DISTINCT changeUser) as distributeAdminNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 41) as t11,";

        sql = sql + "(select count(DISTINCT changeUser) as distributeAllNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId in (19,20,28,30,41)) as t7t11,";


        sql = sql + "(select ifnull(sum(changeAmount),0) as deductionAdministraAmount,count(DISTINCT changeUser) as deductionAdministraNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 23) as t12,";

        sql = sql + "(select ifnull(sum(changeAmount),0) as deductionAdminAmount,count(DISTINCT changeUser) as deductionAdminNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 37) as t13,";

        sql = sql + "(select count(DISTINCT changeUser) as deductionAllNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId in (23,37)) as t12t13,";


        sql = sql + "(select count(DISTINCT account) as regNum from t_user where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND regTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND regTime <= ? ";
            args.add(endTime);
        }
        sql = sql + "  ) as t14,";

        sql = sql + "(select count(DISTINCT account) as firstChargeNum from t_user where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND rechargeFirstTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND rechargeFirstTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " ) as t15,";

        sql = sql + "(select count(*) as betNum,ifnull(sum(amount),0) as betAmount,count(DISTINCT account) as betPerNum from t_bet where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND lastTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND lastTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and status in (1,2)) as t16,";

        sql = sql + "(select ifnull(sum(win),0) as winAmount,count(DISTINCT account) as winPerNum from t_bet where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND lastTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND lastTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and status = 1) as t17,";

        sql = sql + "(select ifnull(sum(amount),0) as withdrawalPerAmount,count(DISTINCT account) as withdrawalPerNum from t_bet where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND lastTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND lastTime <= ? ";
            args.add(endTime);
        }
        sql = sql + "  and status = 4) as t18,";

        sql = sql + "(select ifnull(sum(amount),0) as withdrawalSysAmount,count(DISTINCT account) as withdrawalSysNum from t_bet where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND lastTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND lastTime <= ? ";
            args.add(endTime);
        }
        sql = sql + "  and status = 5) as t19,";

        sql = sql + "(select count(DISTINCT account) as withdrawalAllNum from t_bet where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND lastTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND lastTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and status in (4,5)) as t18t19";

        Object[] arg = args.toArray();

        return this.dbSession.getMap(sql, arg);
    }


    //综合报表接口【取人数】
    public Map<String, Object> cpsReportPerNum(Date startTime, Date endTime) {
        List<Object> args = new ArrayList<Object>();
        String sql = "select * from ";

        sql = sql + "(select count(DISTINCT changeUser) as rechargeBankNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + "and accountChangeTypeId = 12) as t1,";

        sql = sql + "(select count(DISTINCT changeUser) as rechargeOnlineNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 11) as t2,";

        sql = sql + "(select count(DISTINCT changeUser) as rechargeHandNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 13) as t3,";

        sql = sql + "(select count(DISTINCT changeUser) as rechargeCashNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 21) as t4,";

        sql = sql + "(select count(DISTINCT changeUser) as rechargeAllNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId in (12,11,13,21)) as t1t4,";


        sql = sql + "(select count(DISTINCT changeUser) as withdrawNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 18) as t5,";

        sql = sql + "(select count(DISTINCT changeUser) as rebateNum from t_amount_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 3) as t6,";

        sql = sql + "(select count(DISTINCT changeUser) as distributeActivityNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 19) as t7,";

        sql = sql + "(select count(DISTINCT changeUser) as distributeClaimNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 20) as t8,";

        sql = sql + "(select count(DISTINCT changeUser) as distributeDailyNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 28) as t9,";

        sql = sql + "(select count(DISTINCT changeUser) as distributeDividendNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 30) as t10,";

        sql = sql + "(select count(DISTINCT changeUser) as distributeAdminNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 41) as t11,";

        sql = sql + "(select count(DISTINCT changeUser) as distributeAllNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId in (19,20,28,30,41)) as t7t11,";

        sql = sql + "(select count(DISTINCT changeUser) as deductionAdministraNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 23) as t12,";

        sql = sql + "(select count(DISTINCT changeUser) as deductionAdminNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId = 37) as t13,";

        sql = sql + "(select count(DISTINCT changeUser) as deductionAllNum from t_finance_change where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND changeTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND changeTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and accountChangeTypeId in (23,37)) as t12t13,";

        sql = sql + "(select count(DISTINCT account) as betPerNum from t_bet where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND lastTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND lastTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and status in (1,2)) as t16,";

        sql = sql + "(select count(DISTINCT account) as winPerNum from t_bet where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND lastTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND lastTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and status = 1) as t17,";

        sql = sql + "(select count(DISTINCT account) as withdrawalPerNum from t_bet where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND lastTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND lastTime <= ? ";
            args.add(endTime);
        }
        sql = sql + "  and status = 4) as t18,";

        sql = sql + "(select count(DISTINCT account) as withdrawalSysNum from t_bet where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND lastTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND lastTime <= ? ";
            args.add(endTime);
        }
        sql = sql + "  and status = 5) as t19,";

        sql = sql + "(select count(DISTINCT account) as withdrawalAllNum from t_bet where 1=1 and test = 0 ";
        if (startTime != null) {
            sql = sql + " AND lastTime >= ? ";
            args.add(startTime);
        }
        if (endTime != null) {
            sql = sql + " AND lastTime <= ? ";
            args.add(endTime);
        }
        sql = sql + " and status in (4,5)) as t18t19";

        Object[] arg = args.toArray();

        return this.dbSession.getMap(sql, arg);
    }


    public CpsReport find(String d) throws Exception {

        Date begin = null;
        Date end = null;
        String startTime = "";
        String endTime = "";

        startTime = d + " 00:00:00";
        endTime = d + " 23:59:59";
        begin = DateUtils.toDate(startTime);
        end = DateUtils.toDate(endTime);

        Map<String, Object> cpsReportMap =  cpsReport(begin, end);

        BigDecimal rechargeBankAmount = MathUtils.getBigDecimal(cpsReportMap.get("rechargeBankAmount"));//银行充值金额
        Integer rechargeBankNum = Integer.valueOf(String.valueOf(cpsReportMap.get("rechargeBankNum")));//银行充值人数---
        Integer rechargeBankOrNum = Integer.valueOf(String.valueOf(cpsReportMap.get("rechargeBankOrNum")));//充值笔数
        BigDecimal rechargeOnlineAmount = MathUtils.getBigDecimal(cpsReportMap.get("rechargeOnlineAmount"));//在线充值金额
        Integer rechargeOnlineNum = Integer.valueOf(String.valueOf(cpsReportMap.get("rechargeOnlineNum")));//在线充值人数---
        Integer rechargeOnlineOrNum = Integer.valueOf(String.valueOf(cpsReportMap.get("rechargeOnlineOrNum")));//充值笔数
        BigDecimal rechargeHandAmount = MathUtils.getBigDecimal(cpsReportMap.get("rechargeHandAmount"));//充值手续费金额
        Integer rechargeHandNum = Integer.valueOf(String.valueOf(cpsReportMap.get("rechargeHandNum")));//充值手续费人数---
        Integer rechargeHandOrNum = Integer.valueOf(String.valueOf(cpsReportMap.get("rechargeHandOrNum")));//充值笔数
        BigDecimal rechargeCashAmount = MathUtils.getBigDecimal(cpsReportMap.get("rechargeCashAmount"));//现金充值金额
        Integer rechargeCashNum = Integer.valueOf(String.valueOf(cpsReportMap.get("rechargeCashNum")));//现金充值人数---
        Integer rechargeCashOrNum = Integer.valueOf(String.valueOf(cpsReportMap.get("rechargeCashOrNum")));//充值笔数
        Integer rechargeAllNum = Integer.valueOf(String.valueOf(cpsReportMap.get("rechargeAllNum")));//

        BigDecimal withdrawAmount = MathUtils.getBigDecimal(cpsReportMap.get("withdrawAmount"));//提现金额
        Integer withdrawNum = Integer.valueOf(String.valueOf(cpsReportMap.get("withdrawNum")));//提现人数---
        Integer withdrawOrNum = Integer.valueOf(String.valueOf(cpsReportMap.get("withdrawOrNum")));//提现笔数
        BigDecimal rebateAmount = MathUtils.getBigDecimal(cpsReportMap.get("rebateAmount"));//返点金额
        Integer rebateNum = Integer.valueOf(String.valueOf(cpsReportMap.get("rebateNum")));//返点人数---
        BigDecimal distributeActivityAmount = MathUtils.getBigDecimal(cpsReportMap.get("distributeActivityAmount"));//活动派发金额
        Integer distributeActivityNum = Integer.valueOf(String.valueOf(cpsReportMap.get("distributeActivityNum")));//活动派发人数---
        BigDecimal distributeClaimAmount = MathUtils.getBigDecimal(cpsReportMap.get("distributeClaimAmount"));//理赔充值金额
        Integer distributeClaimNum = Integer.valueOf(String.valueOf(cpsReportMap.get("distributeClaimNum")));//理赔充值人数---
        BigDecimal distributeDailyAmount = MathUtils.getBigDecimal(cpsReportMap.get("distributeDailyAmount"));//日工资金额
        Integer distributeDailyNum = Integer.valueOf(String.valueOf(cpsReportMap.get("distributeDailyNum")));//日工资人数---
        BigDecimal distributeDividendAmount = MathUtils.getBigDecimal(cpsReportMap.get("distributeDividendAmount"));//周期分红金额
        Integer distributeDividendNum = Integer.valueOf(String.valueOf(cpsReportMap.get("distributeDividendNum")));//周期分红人数---
        BigDecimal distributeAdminAmount = MathUtils.getBigDecimal(cpsReportMap.get("distributeAdminAmount"));//管理员派发金额
        Integer distributeAdminNum = Integer.valueOf(String.valueOf(cpsReportMap.get("distributeAdminNum")));//管理员派发人数---
        Integer distributeAllNum = Integer.valueOf(String.valueOf(cpsReportMap.get("distributeAllNum")));//

        BigDecimal deductionAdministraAmount = MathUtils.getBigDecimal(cpsReportMap.get("deductionAdministraAmount"));//行政提出金额
        Integer deductionAdministraNum = Integer.valueOf(String.valueOf(cpsReportMap.get("deductionAdministraNum")));//行政提出人数---
        BigDecimal deductionAdminAmount = MathUtils.getBigDecimal(cpsReportMap.get("deductionAdminAmount"));//管理员扣减金额
        Integer deductionAdminNum = Integer.valueOf(String.valueOf(cpsReportMap.get("deductionAdminNum")));//管理员扣减人数---
        Integer deductionAllNum = Integer.valueOf(String.valueOf(cpsReportMap.get("deductionAllNum")));//

        Integer regNum = Integer.valueOf(String.valueOf(cpsReportMap.get("regNum")));//注册人数
        Integer firstChargeNum = Integer.valueOf(String.valueOf(cpsReportMap.get("firstChargeNum")));//首充人数
        Integer betNum = Integer.valueOf(String.valueOf(cpsReportMap.get("betNum")));//投注单量
        BigDecimal betAmount = MathUtils.getBigDecimal(cpsReportMap.get("betAmount"));//投注金额
        Integer betPerNum = Integer.valueOf(String.valueOf(cpsReportMap.get("betPerNum")));//投注人数---
        BigDecimal winAmount = MathUtils.getBigDecimal(cpsReportMap.get("winAmount"));//中奖金额
        Integer winPerNum = Integer.valueOf(String.valueOf(cpsReportMap.get("winPerNum")));//中奖人数---
        BigDecimal withdrawalPerAmount = MathUtils.getBigDecimal(cpsReportMap.get("withdrawalPerAmount"));//个人撤单金额
        Integer withdrawalPerNum = Integer.valueOf(String.valueOf(cpsReportMap.get("withdrawalPerNum")));//个人撤单人数---
        BigDecimal withdrawalSysAmount = MathUtils.getBigDecimal(cpsReportMap.get("withdrawalSysAmount"));//系统撤单金额
        Integer withdrawalSysNum = Integer.valueOf(String.valueOf(cpsReportMap.get("withdrawalSysNum")));//系统撤单人数---
        Integer withdrawalAllNum = Integer.valueOf(String.valueOf(cpsReportMap.get("withdrawalAllNum")));//

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
        cpsReport.setReportDate(d);
        cpsReport.setWithdrawalAllNum(withdrawalAllNum);

        return cpsReport;
    }

    public void save(CpsReport m) {
        Object[] a = {
                0,
                m.getReportDate(),
                m.getRechargeBankAmount(),
                m.getRechargeBankNum(),
                m.getRechargeBankOrNum(),
                m.getRechargeOnlineAmount(),
                m.getRechargeOnlineNum(),
                m.getRechargeOnlineOrNum(),
                m.getRechargeHandAmount(),
                m.getRechargeHandNum(),
                m.getRechargeHandOrNum(),
                m.getRechargeCashAmount(),
                m.getRechargeCashNum(),
                m.getRechargeCashOrNum(),
                m.getRechargeAllNum(),
                m.getWithdrawAmount(),
                m.getWithdrawNum(),
                m.getWithdrawOrNum(),
                m.getRebateAmount(),
                m.getRebateNum(),
                m.getDistributeActivityAmount(),
                m.getDistributeActivityNum(),
                m.getDistributeClaimAmount(),
                m.getDistributeClaimNum(),
                m.getDistributeDailyAmount(),
                m.getDistributeDailyNum(),
                m.getDistributeDividendAmount(),
                m.getDistributeDividendNum(),
                m.getDistributeAdminAmount(),
                m.getDistributeAdminNum(),
                m.getDistributeAllNum(),
                m.getDeductionAdministraAmount(),
                m.getDeductionAdministraNum(),
                m.getDeductionAdminAmount(),
                m.getDeductionAdminNum(),
                m.getDeductionAllNum(),
                m.getRegNum(),
                m.getFirstChargeNum(),
                m.getBetNum(),
                m.getBetAmount(),
                m.getBetPerNum(),
                m.getWinAmount(),
                m.getWinPerNum(),
                m.getWithdrawalPerAmount(),
                m.getWithdrawalPerNum(),
                m.getWithdrawalSysAmount(),
                m.getWithdrawalSysNum(),
                m.getWithdrawalAllNum()
        };

        this.dbSession.update("insert into t_cps_report values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", a);
    }


}
