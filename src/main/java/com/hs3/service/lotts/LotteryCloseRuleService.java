package com.hs3.service.lotts;

import com.hs3.dao.lotts.LotteryCloseRuleDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.LotteryCloseRule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("lotteryCloseRuleService")
public class LotteryCloseRuleService {
    @Autowired
    private LotteryCloseRuleDao lotteryCloseRuleDao;

    public List<LotteryCloseRule> list(String lotteryId, Page p) {
        return this.lotteryCloseRuleDao.listByLotteryId(lotteryId, p);
    }

    public List<LotteryCloseRule> listByStatus(String lotteryId, int status) {
        return this.lotteryCloseRuleDao.listByStatus(lotteryId, status);
    }

    public int delete(Integer id) {
        return this.lotteryCloseRuleDao.delete(id);
    }

    public int delete(List<Integer> ids) {
        return this.lotteryCloseRuleDao.delete(ids);
    }

    public LotteryCloseRule find(Integer id) {
        return (LotteryCloseRule) this.lotteryCloseRuleDao.find(id);
    }

    public int update(LotteryCloseRule m) {
        return this.lotteryCloseRuleDao.update(m);
    }

    public void save(LotteryCloseRule m) {
        this.lotteryCloseRuleDao.save(m);
    }
}
