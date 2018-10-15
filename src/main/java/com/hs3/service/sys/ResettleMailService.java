package com.hs3.service.sys;

import com.hs3.dao.sys.ResettleMailDao;
import com.hs3.db.Page;
import com.hs3.entity.sys.ResettleMail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("resettleMailService")
public class ResettleMailService {
    @Autowired
    private ResettleMailDao resettleMailDao;

    public void save(ResettleMail m) {
        this.resettleMailDao.save(m);
    }

    public int update(ResettleMail m) {
        return this.resettleMailDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.resettleMailDao.delete(ids);
    }

    public List<ResettleMail> list(Page page) {
        return this.resettleMailDao.list(page);
    }

    public List<ResettleMail> listWithOrder(Page p) {
        return this.resettleMailDao.listWithOrder(p);
    }

    public ResettleMail find(Integer id) {
        return (ResettleMail) this.resettleMailDao.find(id);
    }

    public List<ResettleMail> listByStatusAndType(Integer type, Integer status) {
        return this.resettleMailDao.listByStatusAndType(type, status);
    }
}
