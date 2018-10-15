package com.hs3.service.sys;

import com.hs3.dao.sys.Log4jDao;
import com.hs3.db.Page;
import com.hs3.entity.sys.Log4j;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Log4jService {
    @Autowired
    private Log4jDao log4jDao;

    public List<Log4j> list(Page p) {
        return this.log4jDao.list(p);
    }

    public List<Log4j> list(String lever, String clazz, String method, String message, Date begin, Date end, Page p) {
        return this.log4jDao.list(lever, clazz, method, message, begin, end, p);
    }

    public int delete(List<Integer> ids) {
        return this.log4jDao.delete(ids);
    }
}
