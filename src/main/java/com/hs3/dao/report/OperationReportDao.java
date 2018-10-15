package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.report.OperationReport;
import com.hs3.utils.DateUtils;
import com.hs3.utils.sys.WebDateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("operationReportDao")
public class OperationReportDao
        extends BaseDao<OperationReport> {
    private static final String SQL = "SELECT\t? reportDate,\tu.*, g.*, rn1.*, rn2.*, rn3.*, an2.allUserAmount, an2.allOperationAmount\tFROM\t(\t\tSELECT\t\t\t(SELECT count(1) FROM t_user t1 WHERE t1.regTime >= ? AND t1.regTime <= ? AND t1.test = 0) userRegisteredNum,\t\t\t(SELECT count(DISTINCT t2.account) FROM t_bet t2 WHERE t2.createTime >= ? AND t2.createTime <= ? AND t2.test = 0) userActiveNum,\t\t\t(SELECT IFNULL(sum(t3.changeAmount), 0) FROM t_finance_change t3 WHERE t3.accountChangeTypeId = 19 AND t3.changeTime >= ? AND t3.changeTime <= ? AND t3.test = 0) userActivityAmount\t\tFROM\t\t\tDUAL\t) u,\t(\t\tSELECT\t\t\tIFNULL(sum(t4.amount), 0) gameValidAmount,\t\t\t(SELECT count(1) FROM t_bet t5 WHERE t5.createTime >= ? AND t5.createTime <= ? AND t5.test = 0) gameBetNum,\t\t\tIFNULL(sum((SELECT sum(t6.changeAmount) FROM t_amount_change t6 WHERE t6.betId = t4.id AND t6.accountChangeTypeId = 2 AND t6.test = 0)), 0) gameRebate,\t\t\tIFNULL(sum((SELECT sum(t7.changeAmount) FROM t_amount_change t7 WHERE t7.betId = t4.id AND t7.accountChangeTypeId = 3 AND t7.test = 0)), 0) gameHigherRebate,\t\t\tIFNULL(sum(t4.win - t4.amount), 0) gameProfit,\t\t\tIFNULL(sum(IF(t4.lotteryId = '3d', t4.amount, 0)), 0) game3DAmount\t\tFROM\t\t\tt_bet t4\t\tWHERE\t\t\tt4.STATUS IN (1, 2)\t\tAND t4.lastTime >= ? AND t4.lastTime <= ?\t\tAND t4.test = 0\t) g,\t(\t\tSELECT\t\t\tcount(1) rechaegeFirstNum,\t\t\tIFNULL(SUM(r1.rechargeFirstAmount), 0) rechargeFirstAmount\t\tFROM\t\t\tt_user r1\t\tWHERE\t\t\tr1.rechargeFirstTime >= ? AND r1.rechargeFirstTime <= ?\t\tAND r1.test = 0\t) rn1,\t(\t\tSELECT\t\t\tcount(DISTINCT r3.account) rechargeAccountNum,\t\t\tcount(1) rechargeNum,\t\t\tIFNULL(SUM(IF(r3.rechargeType = 1, r3.amount, 0)), 0) rechargePayAmount,\t\t\tIFNULL(SUM(IF(r3.rechargeType = 0, r3.amount, 0)), 0) rechargeBankAmount\t\tFROM\t\t\tt_recharge r3\t\tWHERE\t\t\tr3.lastTime >= ? AND r3.lastTime <= ?\t\tAND r3. STATUS = 2\t\tAND r3.test = 0\t) rn2,\t(SELECT count(DISTINCT r4.account) depositAccountNum, count(1) depositNum, IFNULL(SUM(r4.amount), 0) depositAmount FROM t_deposit r4 WHERE r4.lastTime >= ? AND r4.lastTime <= ? AND r4. STATUS = 2 AND r4.test = 0) rn3,\t(\tSELECT\t\t\t(anan2.allUserAmount + anan2.allOperationAmount + anan3.allUserAmount) allUserAmount,\t\t\t(anan2.allUserAmount - anan2.allOperationAmount + anan3.allOperationAmount) allOperationAmount\t\tFROM\t\t\t(\t\t\t\tSELECT\t\t\t\t\tIFNULL(SUM(IF(ann2.accountChangeTypeId IN (11, 12, 18), ann2.changeAmount, 0)), 0) allUserAmount, IFNULL(SUM(IF(ann2.accountChangeTypeId IN (11, 12, 18), 0, ann2.changeAmount)), 0) allOperationAmount\t\t\t\tFROM\t\t\t\t\t(\t\t\t\t\t\tSELECT annn1.changeAmount, annn1.accountChangeTypeId FROM t_amount_change annn1 WHERE annn1.changeTime >= ? AND annn1.changeTime <= ? AND annn1.test = 0\t\t\t\t\t\tUNION ALL\t\t\t\t\t\tSELECT annn2.changeAmount, annn2.accountChangeTypeId FROM t_finance_change annn2 WHERE annn2.changeTime >= ? AND annn2.changeTime <= ? AND annn2.test = 0\t\t\t\t\t) ann2\t\t\t) anan2, \t\t\t(SELECT IFNULL(SUM(anan3.allOperationAmount), 0) allOperationAmount, IFNULL(SUM(anan3.allUserAmount), 0) allUserAmount FROM t_operation_report anan3 WHERE \tanan3.reportDate = ?) anan3\t) an2";
    private static final String SELECT_SQL = "SELECT * FROM t_operation_report WHERE 1=1 AND reportDate >= ? AND reportDate <= ? ORDER BY reportDate DESC";

    public List<OperationReport> listByCond(String begin, String end, Page page) {
        return this.dbSession.list("SELECT * FROM t_operation_report WHERE 1=1 AND reportDate >= ? AND reportDate <= ? ORDER BY reportDate DESC", new Object[]{begin, end}, this.cls, page);
    }

    public OperationReport find(String d) {
        Date reportDate = DateUtils.toDateNull(d, "yyyy-MM-dd");
        Date beginTime = WebDateUtils.getCurTime(reportDate);
        Date endTime = WebDateUtils.getCurTime(DateUtils.addDay(reportDate, 1));
        String preReportDate = DateUtils.format(DateUtils.addDay(reportDate, -1), "yyyy-MM-dd");

        List<Object> cond = new ArrayList();
        cond.add(d);
        cond.add(beginTime);
        cond.add(endTime);
        cond.add(beginTime);
        cond.add(endTime);
        cond.add(beginTime);
        cond.add(endTime);
        cond.add(beginTime);
        cond.add(endTime);
        cond.add(beginTime);
        cond.add(endTime);
        cond.add(beginTime);
        cond.add(endTime);
        cond.add(beginTime);
        cond.add(endTime);
        cond.add(beginTime);
        cond.add(endTime);
        cond.add(beginTime);
        cond.add(endTime);
        cond.add(beginTime);
        cond.add(endTime);
        cond.add(preReportDate);

        return (OperationReport) this.dbSession.getObject("SELECT\t? reportDate,\tu.*, g.*, rn1.*, rn2.*, rn3.*, an2.allUserAmount, an2.allOperationAmount\tFROM\t(\t\tSELECT\t\t\t(SELECT count(1) FROM t_user t1 WHERE t1.regTime >= ? AND t1.regTime <= ? AND t1.test = 0) userRegisteredNum,\t\t\t(SELECT count(DISTINCT t2.account) FROM t_bet t2 WHERE t2.createTime >= ? AND t2.createTime <= ? AND t2.test = 0) userActiveNum,\t\t\t(SELECT IFNULL(sum(t3.changeAmount), 0) FROM t_finance_change t3 WHERE t3.accountChangeTypeId = 19 AND t3.changeTime >= ? AND t3.changeTime <= ? AND t3.test = 0) userActivityAmount\t\tFROM\t\t\tDUAL\t) u,\t(\t\tSELECT\t\t\tIFNULL(sum(t4.amount), 0) gameValidAmount,\t\t\t(SELECT count(1) FROM t_bet t5 WHERE t5.createTime >= ? AND t5.createTime <= ? AND t5.test = 0) gameBetNum,\t\t\tIFNULL(sum((SELECT sum(t6.changeAmount) FROM t_amount_change t6 WHERE t6.betId = t4.id AND t6.accountChangeTypeId = 2 AND t6.test = 0)), 0) gameRebate,\t\t\tIFNULL(sum((SELECT sum(t7.changeAmount) FROM t_amount_change t7 WHERE t7.betId = t4.id AND t7.accountChangeTypeId = 3 AND t7.test = 0)), 0) gameHigherRebate,\t\t\tIFNULL(sum(t4.win - t4.amount), 0) gameProfit,\t\t\tIFNULL(sum(IF(t4.lotteryId = '3d', t4.amount, 0)), 0) game3DAmount\t\tFROM\t\t\tt_bet t4\t\tWHERE\t\t\tt4.STATUS IN (1, 2)\t\tAND t4.lastTime >= ? AND t4.lastTime <= ?\t\tAND t4.test = 0\t) g,\t(\t\tSELECT\t\t\tcount(1) rechaegeFirstNum,\t\t\tIFNULL(SUM(r1.rechargeFirstAmount), 0) rechargeFirstAmount\t\tFROM\t\t\tt_user r1\t\tWHERE\t\t\tr1.rechargeFirstTime >= ? AND r1.rechargeFirstTime <= ?\t\tAND r1.test = 0\t) rn1,\t(\t\tSELECT\t\t\tcount(DISTINCT r3.account) rechargeAccountNum,\t\t\tcount(1) rechargeNum,\t\t\tIFNULL(SUM(IF(r3.rechargeType = 1, r3.amount, 0)), 0) rechargePayAmount,\t\t\tIFNULL(SUM(IF(r3.rechargeType = 0, r3.amount, 0)), 0) rechargeBankAmount\t\tFROM\t\t\tt_recharge r3\t\tWHERE\t\t\tr3.lastTime >= ? AND r3.lastTime <= ?\t\tAND r3. STATUS = 2\t\tAND r3.test = 0\t) rn2,\t(SELECT count(DISTINCT r4.account) depositAccountNum, count(1) depositNum, IFNULL(SUM(r4.amount), 0) depositAmount FROM t_deposit r4 WHERE r4.lastTime >= ? AND r4.lastTime <= ? AND r4. STATUS = 2 AND r4.test = 0) rn3,\t(\tSELECT\t\t\t(anan2.allUserAmount + anan2.allOperationAmount + anan3.allUserAmount) allUserAmount,\t\t\t(anan2.allUserAmount - anan2.allOperationAmount + anan3.allOperationAmount) allOperationAmount\t\tFROM\t\t\t(\t\t\t\tSELECT\t\t\t\t\tIFNULL(SUM(IF(ann2.accountChangeTypeId IN (11, 12, 18), ann2.changeAmount, 0)), 0) allUserAmount, IFNULL(SUM(IF(ann2.accountChangeTypeId IN (11, 12, 18), 0, ann2.changeAmount)), 0) allOperationAmount\t\t\t\tFROM\t\t\t\t\t(\t\t\t\t\t\tSELECT annn1.changeAmount, annn1.accountChangeTypeId FROM t_amount_change annn1 WHERE annn1.changeTime >= ? AND annn1.changeTime <= ? AND annn1.test = 0\t\t\t\t\t\tUNION ALL\t\t\t\t\t\tSELECT annn2.changeAmount, annn2.accountChangeTypeId FROM t_finance_change annn2 WHERE annn2.changeTime >= ? AND annn2.changeTime <= ? AND annn2.test = 0\t\t\t\t\t) ann2\t\t\t) anan2, \t\t\t(SELECT IFNULL(SUM(anan3.allOperationAmount), 0) allOperationAmount, IFNULL(SUM(anan3.allUserAmount), 0) allUserAmount FROM t_operation_report anan3 WHERE \tanan3.reportDate = ?) anan3\t) an2", cond.toArray(new Object[cond.size()]), this.cls);
    }

    public int delete(String reportDate) {
        String sql = "DELETE FROM " + this.tableName + " WHERE reportDate=?";
        return this.dbSession.update(sql, new Object[]{reportDate});
    }

    public void save(OperationReport m) {
        saveAuto(m);
    }

    protected String[] getColumns() {
        return new String[]{"reportDate", "userRegisteredNum", "userActiveNum", "userActivityAmount", "gameValidAmount", "gameBetNum", "gameRebate", "gameHigherRebate", "gameProfit", "game3DAmount",
                "rechaegeFirstNum", "rechargeFirstAmount", "rechargeAccountNum", "rechargeNum", "rechargePayAmount", "rechargeBankAmount", "depositAccountNum", "depositNum", "depositAmount",
                "allUserAmount", "allOperationAmount"};
    }

    protected Object[] getValues(OperationReport m) {
        return new Object[]{m.getReportDate(), m.getUserRegisteredNum(), m.getUserActiveNum(), m.getUserActivityAmount(), m.getGameValidAmount(), m.getGameBetNum(), m.getGameRebate(),
                m.getGameHigherRebate(), m.getGameProfit(), m.getGame3DAmount(), m.getRechaegeFirstNum(), m.getRechargeFirstAmount(), m.getRechargeAccountNum(), m.getRechargeNum(),
                m.getRechargePayAmount(), m.getRechargeBankAmount(), m.getDepositAccountNum(), m.getDepositNum(), m.getDepositAmount(), m.getAllUserAmount(), m.getAllOperationAmount()};
    }
}
