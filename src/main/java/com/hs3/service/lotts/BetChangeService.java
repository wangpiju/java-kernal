package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetChangeDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetChange;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betChangeService")
public class BetChangeService {
    @Autowired
    private BetChangeDao betChangeDao;

    public void save(BetChange m) {
        this.betChangeDao.save(m);
    }

    public List<BetChange> list(Page p) {
        return this.betChangeDao.list(p);
    }

    public List<BetChange> listWithOrder(Page p) {
        return this.betChangeDao.listWithOrder(p);
    }

    public BetChange find(Integer id) {
        return (BetChange) this.betChangeDao.find(id);
    }

    public int update(BetChange m) {
        return this.betChangeDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.betChangeDao.delete(ids);
    }
}
