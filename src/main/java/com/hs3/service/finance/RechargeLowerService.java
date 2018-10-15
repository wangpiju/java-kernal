package com.hs3.service.finance;

import com.hs3.dao.finance.RechargeLowerDao;
import com.hs3.db.Page;
import com.hs3.entity.finance.RechargeLower;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("rechargeLowerService")
public class RechargeLowerService {
    @Autowired
    private RechargeLowerDao rechargeLowerDao;

    public List<RechargeLower> listByCond(RechargeLower m, Date startTime, Date endTime, Page page) {
        return this.rechargeLowerDao.listByCond(m, startTime, endTime, page);
    }

    public void save(RechargeLower m) {
        this.rechargeLowerDao.save(m);
    }

    public List<RechargeLower> list(Page p) {
        return this.rechargeLowerDao.list(p);
    }

    public List<RechargeLower> listWithOrder(Page p) {
        return this.rechargeLowerDao.listWithOrder(p);
    }

    public RechargeLower find(String id) {
        return (RechargeLower) this.rechargeLowerDao.find(id);
    }

    public int update(RechargeLower m) {
        return this.rechargeLowerDao.update(m);
    }

    public int delete(List<String> ids) {
        return this.rechargeLowerDao.delete(ids);
    }
}
