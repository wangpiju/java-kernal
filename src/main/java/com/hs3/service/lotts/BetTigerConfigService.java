package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetTigerConfigDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetTigerConfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betTigerConfigService")
public class BetTigerConfigService {
    @Autowired
    private BetTigerConfigDao betTigerConfigDao;

    public void save(BetTigerConfig m) {
        this.betTigerConfigDao.save(m);
    }

    public List<BetTigerConfig> list(Page p) {
        return this.betTigerConfigDao.list(p);
    }

    public List<BetTigerConfig> listWithOrder(Page p) {
        return this.betTigerConfigDao.listWithOrder(p);
    }

    public BetTigerConfig find(Integer id) {
        return (BetTigerConfig) this.betTigerConfigDao.find(id);
    }

    public int update(BetTigerConfig m) {
        return this.betTigerConfigDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.betTigerConfigDao.delete(ids);
    }
}
