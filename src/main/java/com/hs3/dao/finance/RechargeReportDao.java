package com.hs3.dao.finance;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.finance.RechargeReport;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("rechargeReportDao")
public class RechargeReportDao
        extends BaseDao<RechargeReport> {
    public void save(RechargeReport m) {
        String sql = new SQLUtils(this.tableName).field("receiveName,receiveCard,amount,poundage,realAmount,num,createDate").getInsert();
        this.dbSession.update(sql, new Object[]{
                m.getReceiveName(),
                m.getReceiveCard(),
                m.getAmount(),
                m.getPoundage(),
                m.getRealAmount(),
                m.getNum(),
                m.getCreateDate()});
    }

    public List<RechargeReport> list(Date begin, Date end, String receiveName, String receiveCard, Page p) {
        String sql = "SELECT * FROM t_recharge_report WHERE 1=1";
        List<Object> args = new ArrayList();
        if (begin != null) {
            sql = sql + " AND createDate>=?";
            args.add(begin);
        }
        if (end != null) {
            sql = sql + " AND createDate<=?";
            args.add(end);
        }
        if (!StrUtils.hasEmpty(new Object[]{receiveName})) {
            sql = sql + " AND receiveName=?";
            args.add(receiveName);
        }
        if (!StrUtils.hasEmpty(new Object[]{receiveCard})) {
            sql = sql + " AND receiveCard=?";
            args.add(receiveCard);
        }
        return this.dbSession.list(sql, args.toArray(), this.cls, p);
    }

    public List<RechargeReport> totalRecharge(Date begin, Date end) {
        String sql = "SELECT    (\t\tCASE rechargeType \t\tWHEN 1 THEN \t\t\tIFNULL((SELECT classKey FROM t_bank_api WHERE id = b.receiveBankId),'')  \t\tWHEN 2 THEN \t\t\t'现金充值' \t\tELSE \t\t\t'银行充值' \t\tEND\t  ) AS receiveName,\t  IFNULL(receiveCard,'') as receiveCard,amount,poundage,realAmount,num FROM \t  (\t\tSELECT \t\t\treceiveBankId,receiveCard,rechargeType,\t\t\tSUM(amount) as amount,\t\t\tSUM(IFNULL(poundage,0)) as poundage,\t\t\tSUM(amount-IFNULL(poundage,0)) as realAmount,\t\t\tCOUNT(1) as num \tFROM \t\t\tt_recharge \tWHERE \t\t\tstatus=2 AND createTime>=? AND createTime<=? \tGROUP BY \t\t\treceiveBankId,receiveCard,rechargeType\t  ) AS b";


        return this.dbSession.list(sql, new Object[]{begin, end}, this.cls);
    }

    public int count(Date date) {
        String sql = "SELECT COUNT(1) FROM t_recharge_report WHERE createDate = ?";
        return this.dbSession.getInt(sql, new Object[]{DateUtils.getDate(date)}).intValue();
    }
}
