package com.hs3.dao.bank;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.db.SQLUtils;
import com.hs3.entity.bank.BankApi;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("bankApiDao")
public class BankApiDao
        extends BaseDao<BankApi> {
    private static final String UPDATE_RECHARGE = "UPDATE t_bank_api SET rechargeAmount=rechargeAmount+?, rechargeNum=rechargeNum+? WHERE id=?";
    private static final String SELECT_RECHARGE = "SELECT t1.* FROM t_bank_api t1, t_bank_level t2 WHERE t2.minAmount <= ? AND t2.minCount  <= ? and t1.levelId = t2.id and t1.status = 0 ORDER BY t1.orderId desc";
    private static final String SELECT_listByAccount = "SELECT t.* FROM t_bank_api t WHERE ((t.levelId = ? OR t.levelId like ? OR t.levelId like ? OR t.levelId like ?) OR ((t.id IN (SELECT t2.bankApiId FROM t_bank_acc t2 WHERE (t2.type = 0 AND t2.account = ?) OR (t2.type = 1 AND EXISTS (SELECT 1 FROM t_user t3 WHERE t3.account = ? AND t3.parentList like CONCAT((select t4.parentList from t_user t4 where t4.account = t2.account),'%'))))))) and t.status = 0 ORDER BY t.orderId desc";

    public List<BankApi> listByCond(BankApi m, String[] classKeyArray, Page page) {
        String sql = "SELECT * FROM t_bank_api WHERE 1 = 1";
        List<Object> cond = new ArrayList();
        if (m.getId() != null) {
            sql = sql + " AND id = ?";
            cond.add(m.getId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getClassKey()})) {
            sql = sql + " AND classKey = ?";
            cond.add(m.getClassKey());
        }
        if ((classKeyArray != null) && (classKeyArray.length > 0)) {
            sql = sql + " AND classKey IN (";
            for (String classKey : classKeyArray) {
                if (classKey != null) {
                    sql = sql + "?,";
                    cond.add(classKey);
                }
            }
            sql = sql + "-1)";
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getTitle()})) {
            sql = sql + " AND title = ?";
            cond.add(m.getTitle());
        }
        if (m.getOrderId() != null) {
            sql = sql + " AND orderId = ?";
            cond.add(m.getOrderId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getMerchantCode()})) {
            sql = sql + " AND merchantCode = ?";
            cond.add(m.getMerchantCode());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getEmail()})) {
            sql = sql + " AND email = ?";
            cond.add(m.getEmail());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getSign()})) {
            sql = sql + " AND sign = ?";
            cond.add(m.getSign());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getPublicKey()})) {
            sql = sql + " AND publicKey = ?";
            cond.add(m.getPublicKey());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getLevelId()})) {
            sql = sql + " AND levelId = ?";
            cond.add(m.getLevelId());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getSpecialAccount()})) {
            sql = sql + " AND specialAccount = ?";
            cond.add(m.getSpecialAccount());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getProxyLine()})) {
            sql = sql + " AND proxyLine = ?";
            cond.add(m.getProxyLine());
        }
        if (m.getStatus() != null) {
            sql = sql + " AND status = ?";
            cond.add(m.getStatus());
        }
        if (m.getRechargeAmount() != null) {
            sql = sql + " AND rechargeAmount = ?";
            cond.add(m.getRechargeAmount());
        }
        if (m.getRechargeNum() != null) {
            sql = sql + " AND rechargeNum = ?";
            cond.add(m.getRechargeNum());
        }
        if (m.getMinAmount() != null) {
            sql = sql + " AND minAmount = ?";
            cond.add(m.getMinAmount());
        }
        if (m.getMaxAmount() != null) {
            sql = sql + " AND maxAmount = ?";
            cond.add(m.getMaxAmount());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getTwoCode()})) {
            sql = sql + " AND twoCode = ?";
            cond.add(m.getTwoCode());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getIcoCode()})) {
            sql = sql + " AND icoCode = ?";
            cond.add(m.getIcoCode());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getShopUrl()})) {
            sql = sql + " AND shopUrl = ?";
            cond.add(m.getShopUrl());
        }
        if (m.getIsCredit() != null) {
            sql = sql + " AND isCredit = ?";
            cond.add(m.getIsCredit());
        }
        if (m.getIsSupportMobile() != null) {
            sql = sql + " AND isSupportMobile = ?";
            cond.add(m.getIsSupportMobile());
        }
        sql = sql + " ORDER BY orderId DESC, id DESC";
        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public Integer save(BankApi m) {
        String sql = new SQLUtils(this.tableName)
                .field("title,orderId,merchantCode,email,classKey,sign,publicKey,levelId,specialAccount,proxyLine,minAmount,maxAmount,twoCode,icoCode,shopUrl,isCredit,isSupportMobile,articleId,remark,createTime,poundage")
                .getInsert();
        return
                Integer.valueOf(this.dbSession.updateKeyHolder(sql,
                        new Object[]{m.getTitle(), m.getOrderId(), m.getMerchantCode(), m.getEmail(), m.getClassKey(), m.getSign(), m.getPublicKey(), m.getLevelId(), m.getSpecialAccount(),
                                m.getProxyLine(), m.getMinAmount(), m.getMaxAmount(), m.getTwoCode(), m.getIcoCode(), m.getShopUrl(), m.getIsCredit(), m.getIsSupportMobile(), m.getArticleId(),
                                m.getRemark(), new Date(), m.getPoundage()}));
    }

    public int update(BankApi m) {
        List<Object> args = new ArrayList();
        SQLUtils su = new SQLUtils(this.tableName)
                .field("title,orderId,merchantCode,email,classKey,levelId,specialAccount,proxyLine,status,minAmount,maxAmount,shopUrl,isCredit,isSupportMobile,articleId,remark,icoCode,poundage").where("id=?");
        args.add(m.getTitle());
        args.add(m.getOrderId());
        args.add(m.getMerchantCode());
        args.add(m.getEmail());
        args.add(m.getClassKey());
        args.add(m.getLevelId());
        args.add(m.getSpecialAccount());
        args.add(m.getProxyLine());
        args.add(m.getStatus());
        args.add(m.getMinAmount());
        args.add(m.getMaxAmount());
        args.add(m.getShopUrl());
        args.add(m.getIsCredit());
        args.add(m.getIsSupportMobile());
        args.add(m.getArticleId());
        args.add(m.getRemark());
        args.add(m.getIcoCode());
        args.add(m.getPoundage());
        if (!StrUtils.hasEmpty(new Object[]{m.getSign()})) {
            su.field("sign");
            args.add(m.getSign());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getPublicKey()})) {
            su.field("publicKey");
            args.add(m.getPublicKey());
        }
        if (!StrUtils.hasEmpty(new Object[]{m.getTwoCode()})) {
            su.field("twoCode");
            args.add(m.getTwoCode());
        }
        args.add(m.getId());
        String sql = su.getUpdate();
        return this.dbSession.update(sql, args.toArray());
    }

    public int updateStatus(Integer id, Integer status) {
        String sql = new SQLUtils(this.tableName).field("status").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{status, id});
    }

    public List<BankApi> findByClassKey(String classKey) {
        String sql = new SQLUtils(this.tableName).field("*").where("classKey=?").orderBy("levelId DESC").getSelect();
        return this.dbSession.list(sql, new Object[]{classKey}, this.cls);
    }

    public int updateRecharge(Integer id, BigDecimal amount, Integer num) {
        return this.dbSession.update("UPDATE t_bank_api SET rechargeAmount=rechargeAmount+?, rechargeNum=rechargeNum+? WHERE id=?", new Object[]{amount, num, id});
    }

    public List<BankApi> listByAmount(BigDecimal rechargeAmount, Integer rechargeNum) {
        return this.dbSession.list("SELECT t1.* FROM t_bank_api t1, t_bank_level t2 WHERE t2.minAmount <= ? AND t2.minCount  <= ? and t1.levelId = t2.id and t1.status = 0 ORDER BY t1.orderId desc", new Object[]{rechargeAmount, rechargeNum}, this.cls);
    }

    public List<BankApi> listByAccount(String account, int levelId) {
        return this.dbSession.list("SELECT t.* FROM t_bank_api t WHERE ((t.levelId = ? OR t.levelId like ? OR t.levelId like ? OR t.levelId like ?) OR ((t.id IN (SELECT t2.bankApiId FROM t_bank_acc t2 WHERE (t2.type = 0 AND t2.account = ?) OR (t2.type = 1 AND EXISTS (SELECT 1 FROM t_user t3 WHERE t3.account = ? AND t3.parentList like CONCAT((select t4.parentList from t_user t4 where t4.account = t2.account),'%'))))))) and t.status = 0 ORDER BY t.orderId desc", new Object[]{Integer.valueOf(levelId), levelId + ",%", "%," + levelId, "%," + levelId + ",%", account, account}, this.cls);
    }
}
