package com.hs3.service.lotts;

import com.hs3.dao.lotts.LotteryCrawlerConfigDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.LotteryCrawlerConfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("lotteryCrawlerConfigService")
public class LotteryCrawlerConfigService {
    @Autowired
    LotteryCrawlerConfigDao lotteryCrawlerConfigDao;

    public void save(LotteryCrawlerConfig m) {
        this.lotteryCrawlerConfigDao.save(m);
    }

    public int update(LotteryCrawlerConfig m) {
        return this.lotteryCrawlerConfigDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.lotteryCrawlerConfigDao.delete(ids);
    }

    public List<LotteryCrawlerConfig> list(Page page) {
        return this.lotteryCrawlerConfigDao.list(page);
    }

    public List<LotteryCrawlerConfig> listByLotteryIdAndStatus(String lotteryId, Integer status, Integer type) {
        return this.lotteryCrawlerConfigDao.listByLotteryIdAndStatus(lotteryId, status, type);
    }

    public List<LotteryCrawlerConfig> listByLotteryId(String lotteryId, Page p) {
        return this.lotteryCrawlerConfigDao.listByLotteryId(lotteryId, p);
    }

    public List<LotteryCrawlerConfig> listByLotteryId(String lotteryId, Integer status) {
        return this.lotteryCrawlerConfigDao.listByLotteryId(lotteryId, status);
    }

    public LotteryCrawlerConfig find(Integer id) {
        return (LotteryCrawlerConfig) this.lotteryCrawlerConfigDao.find(id);
    }
}
