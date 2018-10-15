package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetInRuleFirstDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInRuleFirst;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betInRuleFirstService")
public class BetInRuleFirstService {
    @Autowired
    private BetInRuleFirstDao betInRuleFirstDao;

    public List<BetInRuleFirst> listByCond(BetInRuleFirst m, Page p) {
        return this.betInRuleFirstDao.listByCond(m, p);
    }

    public void save(BetInRuleFirst m) {
        this.betInRuleFirstDao.save(m);
    }

    public List<BetInRuleFirst> list(Page p) {
        return this.betInRuleFirstDao.list(p);
    }

    public BetInRuleFirst find(Integer id) {
        return (BetInRuleFirst) this.betInRuleFirstDao.find(id);
    }

    public int update(BetInRuleFirst m) {
        return this.betInRuleFirstDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.betInRuleFirstDao.delete(ids);
    }
}
