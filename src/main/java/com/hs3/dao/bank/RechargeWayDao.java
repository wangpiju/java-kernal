package com.hs3.dao.bank;

import com.hs3.dao.BaseDao;
import com.hs3.db.Page;
import com.hs3.entity.bank.RechargeWay;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 支付渠道
 *
 * @author Stephen Zhou
 */
@Repository("rechargeWayDao")
public class RechargeWayDao extends BaseDao<RechargeWay> {

    public List<RechargeWay> listRechargeWay() {
        return this.dbSession.list("SELECT id,alino,alias,opentype,minmoney,maxmoney,content,ordernum,waytype,qrcodeurl,attfirst,attsecond,attthird,bnId,attempty,randomNow,randomOld FROM t_recharge_way where opentype = 0 order by ordernum desc", this.cls);
    }

    public List<RechargeWay> listRechargeWayAll() {
        return this.dbSession.list("SELECT * FROM t_recharge_way where opentype = 0 order by ordernum desc", this.cls);
    }

    public RechargeWay findByID(Integer id) {
        String sql = "SELECT id,alino,alias,opentype,minmoney,maxmoney,content,ordernum,waytype,qrcodeurl,attfirst,attsecond,attthird,bnId,attempty,randomNow,randomOld from t_recharge_way where id = ?";
        Object[] args = {id};
        return this.dbSession.getObject(sql, args, this.cls);
    }

    public List<RechargeWay> list(Page p) {
        return this.dbSession.list("SELECT * FROM t_recharge_way order by ordernum desc", this.cls, p);
    }


    public void save(RechargeWay m) {
        this.dbSession.update("INSERT INTO t_recharge_way(alino,alias,opentype,minmoney,maxmoney,content,waytype,qrcodeurl,attfirst,attsecond,attthird,imgData,bnId,attempty,randomNow,randomOld) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[]{m.getAlino(), m.getAlias(),m.getOpentype(),m.getMinmoney(),m.getMaxmoney(),m.getContent(),m.getWaytype(),m.getQrcodeurl(),m.getAttfirst(),m.getAttsecond(),m.getAttthird(),m.getImgData(),m.getBnId(),m.getAttempty(),m.getRandomNow(),m.getRandomOld()});
    }

    public int update(RechargeWay m) {
        return this.dbSession.update("UPDATE t_recharge_way SET alias=?,opentype=?,minmoney=?,maxmoney=?,content=?,waytype=?,qrcodeurl=?,attfirst=?,attsecond=?,attthird=?,imgData=?,bnId=?,attempty=?,randomNow=?,randomOld=? WHERE id=?",
                new Object[]{m.getAlias(),m.getOpentype(),m.getMinmoney(),m.getMaxmoney(),m.getContent(),m.getWaytype(),m.getQrcodeurl(),m.getAttfirst(),m.getAttsecond(),m.getAttthird(),m.getImgData(),m.getBnId(),m.getAttempty(),m.getRandomNow(),m.getRandomOld(), m.getId()});
    }




}
