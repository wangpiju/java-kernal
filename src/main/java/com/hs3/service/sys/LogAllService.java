package com.hs3.service.sys;

import com.hs3.dao.sys.LogAllDao;
import com.hs3.db.Page;
import com.hs3.entity.sys.LogAll;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogAllService {
    @Autowired
    private LogAllDao logAllDao;

    public LogAll find(Integer id) {
        return (LogAll) this.logAllDao.find(id);
    }

    public List<LogAll> list(Page p) {
        return this.logAllDao.list(p);
    }

    public List<LogAll> listByCond(LogAll m, Date startTime, Date endTime, Page page) {
        return this.logAllDao.listByCond(m, startTime, endTime, page);
    }

    public int save(LogAll logAll) {
        return this.logAllDao.saveAuto(logAll);
    }

    public int delete(Integer id) {
        return this.logAllDao.delete(id);
    }

    public int delete(List<Integer> ids) {
        return this.logAllDao.delete(ids);
    }
}
