package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetInPriceDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInPrice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betInPriceService")
public class BetInPriceService {
    @Autowired
    private BetInPriceDao betInPriceDao;

    public void save(BetInPrice m) {
        this.betInPriceDao.save(m);
    }

    public List<BetInPrice> list(Page p) {
        return this.betInPriceDao.list(p);
    }

    public List<BetInPrice> listWithOrder(Page p) {
        return this.betInPriceDao.listWithOrder(p);
    }

    public BetInPrice find(Integer id) {
        return (BetInPrice) this.betInPriceDao.find(id);
    }

    public int update(BetInPrice m) {
        return this.betInPriceDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.betInPriceDao.delete(ids);
    }
}
