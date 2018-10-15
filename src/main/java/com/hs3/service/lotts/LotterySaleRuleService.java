package com.hs3.service.lotts;

import com.hs3.dao.lotts.LotterySaleRuleDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.LotterySaleRule;

import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("lotterySaleRuleService")
public class LotterySaleRuleService {
    private static final Logger logger = LoggerFactory.getLogger(LotterySaleRuleService.class);
    @Autowired
    private LotterySaleRuleDao lotterySaleRuleDao;
    @Autowired
    private LotteryJobService lotteryJobService;

    public List<LotterySaleRule> list(String lotteryId, Page p) {
        return this.lotterySaleRuleDao.list(lotteryId, p);
    }

    public List<LotterySaleRule> listByStatus(String lotteryId, Integer status) {
        return this.lotterySaleRuleDao.listByStatus(lotteryId, status);
    }

    public int delete(Integer id) {
        LotterySaleRule rule = (LotterySaleRule) this.lotterySaleRuleDao.find(id);
        if (rule == null) {
            return 0;
        }
        int i = this.lotterySaleRuleDao.delete(id);
        if (i == 1) {
            this.lotteryJobService.deleteJob(rule);
        }
        return i;
    }

    public int delete(List<Integer> ids) {
        int i = 0;
        for (Integer id : ids) {
            i += delete(id);
        }
        return i;
    }

    public LotterySaleRule find(Integer id) {
        return (LotterySaleRule) this.lotterySaleRuleDao.find(id);
    }

    public int update(LotterySaleRule m) {
        LotterySaleRule old = (LotterySaleRule) this.lotterySaleRuleDao.find(m.getId());
        if (old == null) {
            return 0;
        }
        this.lotteryJobService.deleteJob(old);
        int i = this.lotterySaleRuleDao.update(m);
        if (i == 1) {
            this.lotteryJobService.addJob(m);
        }
        return i;
    }

    public void save(LotterySaleRule m) {
        this.lotterySaleRuleDao.save(m);
        this.lotteryJobService.addJob(m);
    }
}
