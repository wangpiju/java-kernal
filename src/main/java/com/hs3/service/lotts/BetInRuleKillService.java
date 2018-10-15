package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetInRuleKillDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInRuleKill;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betInRuleKillService")
public class BetInRuleKillService {
    @Autowired
    private BetInRuleKillDao betInRuleKillDao;

    public void save(BetInRuleKill m) {
        this.betInRuleKillDao.save(m);
    }

    public List<BetInRuleKill> listByCond(BetInRuleKill m, Page p) {
        return this.betInRuleKillDao.listByCond(m, p);
    }

    public List<BetInRuleKill> listByType(Integer type, Page p) {
        BetInRuleKill m = new BetInRuleKill();
        m.setType(type);

        return this.betInRuleKillDao.listByCond(m, p);
    }

    public List<BetInRuleKill> list(Page p) {
        return this.betInRuleKillDao.list(p);
    }

    public List<BetInRuleKill> listWithOrder(Page p) {
        return this.betInRuleKillDao.listWithOrder(p);
    }

    public BetInRuleKill find(Integer id) {
        return (BetInRuleKill) this.betInRuleKillDao.find(id);
    }

    public int update(BetInRuleKill m) {
        return this.betInRuleKillDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.betInRuleKillDao.delete(ids);
    }
}
