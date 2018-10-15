package com.hs3.service.bank;

import com.hs3.dao.bank.RechargeWayDao;
import com.hs3.db.Page;
import com.hs3.entity.bank.RechargeWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("rechargeWayService")
public class RechargeWayService {

    @Autowired
    private RechargeWayDao rechargeWayDao;

    public List<RechargeWay> list(Page p) {
        return this.rechargeWayDao.list(p);
    }

    public void save(RechargeWay m) {
        this.rechargeWayDao.save(m);
    }

    public int update(RechargeWay m) {
        return this.rechargeWayDao.update(m);
    }

    public RechargeWay find(Integer id) {
        return (RechargeWay)this.rechargeWayDao.find(id);
    }


}
