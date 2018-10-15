package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetInDao;
import com.hs3.dao.lotts.BetInTotalDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInTotal;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betInTotalService")
public class BetInTotalService {
    @Autowired
    private BetInTotalDao betInTotalDao;
    @Autowired
    private BetInDao betInDao;

    public List<BetInTotal> findByCond(boolean hasMark, BetInTotal m, Date beginTime, Date endTime, boolean isIncludeChild, Page p) {
        return this.betInTotalDao.findByCond(hasMark, m, beginTime, endTime, isIncludeChild, p);
    }

    public void save(BetInTotal m) {
        this.betInTotalDao.save(m);
    }

    public List<BetInTotal> list(Page p) {
        return this.betInTotalDao.list(p);
    }

    public List<BetInTotal> listWithOrder(Page p) {
        return this.betInTotalDao.listWithOrder(p);
    }

    public BetInTotal find(Integer id) {
        return (BetInTotal) this.betInTotalDao.find(id);
    }

    public int update(BetInTotal m) {
        return this.betInTotalDao.update(m);
    }

    public int delete(List<Integer> ids) {
        for (Integer id : ids) {
            this.betInDao.deleteByBetId(((BetInTotal) this.betInTotalDao.find(id)).getBetId());
        }
        return this.betInTotalDao.delete(ids);
    }
}
