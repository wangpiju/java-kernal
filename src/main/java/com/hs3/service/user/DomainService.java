package com.hs3.service.user;

import com.hs3.dao.user.DomainDao;
import com.hs3.db.Page;
import com.hs3.entity.users.Domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("domainService")
public class DomainService {
    @Autowired
    private DomainDao domainDao;

    public void save(Domain m) {
        this.domainDao.save(m);
    }

    public int update(Domain m) {
        return this.domainDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.domainDao.delete(ids);
    }

    public boolean setStatus(Integer id, Integer status) {
        return 1 == this.domainDao.setStatus(id, status);
    }

    public Domain find(Integer id) {
        return (Domain) this.domainDao.find(id);
    }

    public List<Domain> list(Page p) {
        return this.domainDao.list(p);
    }

    public List<?> listByAccount(String account) {
        return this.domainDao.listByAccount(account);
    }

    public Object listByAccountNot(String account) {
        return this.domainDao.listByAccountNot(account);
    }

    public void addByAccount(String account, List<Integer> ids) {
        this.domainDao.addByAccount(account, ids);
    }

    public int deleteByAccount(String account, List<Integer> ids) {
        return this.domainDao.deleteByAccount(account, ids);
    }
}
