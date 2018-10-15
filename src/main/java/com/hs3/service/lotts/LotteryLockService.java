package com.hs3.service.lotts;

import com.hs3.dao.lotts.LotteryLockDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.LotteryLock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LotteryLockService {
    @Autowired
    private LotteryLockDao lotteryLockDao;

    public void save(LotteryLock m) {
        this.lotteryLockDao.save(m);
    }

    public int update(LotteryLock m) {
        return this.lotteryLockDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.lotteryLockDao.delete(ids);
    }

    public LotteryLock find(Integer id) {
        return (LotteryLock) this.lotteryLockDao.find(id);
    }

    public LotteryLock findByLotteryId(String lotteryId) {
        return this.lotteryLockDao.findByLotteryId(lotteryId);
    }

    public List<LotteryLock> list(Page p) {
        return this.lotteryLockDao.list(p);
    }
}
