package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetInRuleDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInRule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betInRuleService")
public class BetInRuleService {
    @Autowired
    private BetInRuleDao betInRuleDao;

    public void save(BetInRule m) {
        this.betInRuleDao.save(m);
    }

    public List<BetInRule> list(Page p) {
        return this.betInRuleDao.list(p);
    }

    public List<BetInRule> listWithOrder(Page p) {
        return this.betInRuleDao.listWithOrder(p);
    }

    public BetInRule find(Integer id) {
        return (BetInRule) this.betInRuleDao.find(id);
    }

    public int update(BetInRule m) {
        return this.betInRuleDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.betInRuleDao.delete(ids);
    }
}
