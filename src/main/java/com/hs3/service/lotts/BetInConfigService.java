package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetInConfigDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInConfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betInConfigService")
public class BetInConfigService {
    @Autowired
    private BetInConfigDao betInConfigDao;

    public void save(BetInConfig m) {
        this.betInConfigDao.save(m);
    }

    public List<BetInConfig> list(Page p) {
        return this.betInConfigDao.list(p);
    }

    public List<BetInConfig> listWithOrder(Page p) {
        return this.betInConfigDao.listWithOrder(p);
    }

    public BetInConfig find(Integer id) {
        return (BetInConfig) this.betInConfigDao.find(id);
    }

    public int update(BetInConfig m) {
        return this.betInConfigDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.betInConfigDao.delete(ids);
    }
}
