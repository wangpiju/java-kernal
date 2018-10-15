package com.hs3.service.lotts;

import com.hs3.dao.lotts.LotteryGroupDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.LotteryGroup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("lotteryGroupService")
public class LotteryGroupService {
    @Autowired
    private LotteryGroupDao lotteryGroupDao;

    public LotteryGroup find(Integer id) {
        return (LotteryGroup) this.lotteryGroupDao.find(id);
    }

    public int save(LotteryGroup m) {
        return this.lotteryGroupDao.save(m);
    }

    public int update(LotteryGroup m) {
        return this.lotteryGroupDao.update(m);
    }

    public int delete(List<Integer> id) {
        return this.lotteryGroupDao.delete(id);
    }

    public List<LotteryGroup> list(Page p, Integer status) {
        return this.lotteryGroupDao.listByOrder(p, status);
    }
}
