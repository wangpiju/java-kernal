package com.hs3.service.lotts;

import com.hs3.dao.lotts.SeasonForDateDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.SeasonForDate;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("seasonForDateService")
public class SeasonForDateService {
    @Autowired
    private SeasonForDateDao seasonForDateDao;

    public int update(SeasonForDate s) {
        return this.seasonForDateDao.update(s);
    }

    public void save(SeasonForDate s) {
        this.seasonForDateDao.save(s);
    }

    public SeasonForDate getByLotteryId(String lotteryId) {
        return this.seasonForDateDao.getByLotteryId(lotteryId);
    }

    public List<SeasonForDate> list(String lotteryId, Page p) {
        return this.seasonForDateDao.list(lotteryId, p);
    }

    public List<SeasonForDate> listAll(Page p) {
        return this.seasonForDateDao.list(p);
    }

    public int delete(Integer id) {
        return this.seasonForDateDao.delete(id);
    }

    public SeasonForDate find(Serializable id) {
        return (SeasonForDate) this.seasonForDateDao.find(id);
    }
}
