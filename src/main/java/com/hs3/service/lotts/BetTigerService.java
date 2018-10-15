package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetTigerDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetTiger;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betTigerService")
public class BetTigerService {
    @Autowired
    private BetTigerDao betTigerDao;

    public void save(BetTiger m) {
        this.betTigerDao.save(m);
    }

    public List<BetTiger> listByAccount(String account, int count) {
        return this.betTigerDao.listByAccount(account, count);
    }

    public List<BetTiger> listByCond(BetTiger m, Date startTime, Date endTime, Page page) {
        return this.betTigerDao.listByCond(m, startTime, endTime, page);
    }

    public List<BetTiger> list(Page p) {
        return this.betTigerDao.list(p);
    }

    public List<BetTiger> listWithOrder(Page p) {
        return this.betTigerDao.listWithOrder(p);
    }

    public BetTiger find(Integer id) {
        return (BetTiger) this.betTigerDao.find(id);
    }

    public int update(BetTiger m) {
        return this.betTigerDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.betTigerDao.delete(ids);
    }
}
